<template>
  <div class="box">
    <TopNavDetail :team-info="project?.teamInfo" from-page="/project"></TopNavDetail>
    <h1>项目详情</h1>
    <FloatingAssistant ref="assistantRef" @replace-word="handleWordReplace" />
    <div class="project-content">
      <div class="project-modify-record">
        <div class="project-content-top">
          <h2 style="margin: 5px;">修改记录</h2>
          <div class="ultra-btn btn-mt10" @click="specifiedVersion()" v-if="projectRecordFormalList.length > 0">
            指定版本
          </div>
          <div class="ultra-btn btn-mt10" @click="publishWorks()">
            发布作品
          </div>
        </div>
        <el-dialog v-model="dialogVersionVisible" title="指定版本" width="500">
          <el-form :model="versionData">
            <el-form-item label="版本">
              <el-select-v2 v-model="versionData.versionId" placeholder="版本" :options="versionOptions" />
            </el-form-item>
          </el-form>
          <template #footer>
            <div class="dialog-footer">
              <el-button @click="dialogVersionVisible = false">取消</el-button>
              <el-button type="primary" @click="handleSpecificed()">
                确认
              </el-button>
            </div>
          </template>
        </el-dialog>
        <el-dialog v-model="dialogPublishVisible" title="发布确认" width="500">
          正式版本确认发布后将会由本平台进行智能审核，若出现违规内容将会被退回！
          <template #footer>
            <div class="dialog-footer">
              <el-button @click="dialogVersionVisible = false">取消</el-button>
              <el-button type="primary" @click="assurePublish()">
                确认
              </el-button>
            </div>
          </template>
        </el-dialog>
        <div class="record" v-if="projectRecordList.length > 0" v-for="item in projectRecordList" :key="item.id"
          @click="chooseRecord(item.id)" :class="{ highlighted: selectedRecordId === item.id }">
          <div class="record-item">
            <div class="record-content">
              <span class="text-content">{{
                item.recordComment || item.content
              }}</span>
              <el-tag v-if="project?.version === item.id" type="success" class="version-tag">正式版本</el-tag>
            </div>
          </div>
        </div>
        <el-empty v-else description="没有修改记录" />
      </div>
      <div class="project-editor">
        <WangEditor v-loading="isStreaming" class="editor" v-model="content"
          @needCreativeSuggestions="handleCreativeSuggestions" />
      </div>
      <div class="project-message">
        <div class="author">
          <img class="avatar" :src="user?.userAvatar" alt="" />
          <span class="authorName">{{ user?.userName }}</span>
        </div>
        <h2 style="margin-top: 20px">项目信息</h2>
        <div class="message">
          <div class="name">项目</div>
          <div class="name message-right">{{ project?.name }}</div>
        </div>
        <div class="message">
          <div class="profile">项目简介</div>
          <div class="profile message-right">{{ project?.profile }}</div>
        </div>
        <h3 style="margin-top: 20px">管理员</h3>
        <div class="avatar-pool">
          <div v-for="admin in adminMembers" :key="admin.id">
            <img class="avatar" :src="admin.userAvatar" alt="" />
          </div>
        </div>
        <h3 style="margin-top: 20px">项目成员</h3>
        <div class="avatar-pool">
          <div v-for="member in memberMembers" :key="member.id">
            <img class="avatar" :src="member.userAvatar" alt="" />
          </div>
        </div>

        <div class="button-container">
          <div class="ultra-btn btn-mt10" @click="handleSave()" v-if="isModified">
            保存修改
          </div>
          <div class="ultra-btn btn-mt10" @click="dialogSubmitVisible = true" v-if="!isModified">
            提交版本
          </div>
          <div class="ultra-btn btn-mt10" @click="drawerVisible = true">
            智能生成
          </div>
        </div>
      </div>
      <el-dialog v-model="dialogSubmitVisible" title="提交版本" width="500">
        <el-form :model="submitData" :rules="rules" ref="ruleFormRef">
          <el-form-item label="备注信息" prop="recordComment">
            <el-input v-model="submitData.recordComment" autocomplete="off" />
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="dialogSubmitVisible = false">取消</el-button>
            <el-button type="primary" @click="handleSubmit(ruleFormRef)">
              确认
            </el-button>
          </div>
        </template>
      </el-dialog>
      <el-drawer v-model="drawerVisible" direction="rtl">
        <template #header>
          <h2>创意生成与润色</h2>
        </template>
        <template #default>
          <div>
            <IdeaForm @close-drawer="handleGenerate" :exist="drawerVisible" :content="content" />
          </div>
        </template>
      </el-drawer>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive, computed, watch } from "vue";
import { useRoute } from "vue-router";
import TopNavDetail from "@/components/TopNavDetail.vue";
import WangEditor from "@/components/WangEditor.vue";
import getItemById from "../../../requests/find/getItemById";
import getProjectRecord from "../../../requests/project/getProjectRecord";
import listProjectRecord from "../../../requests/project/listProjectRecord";
import getUserData from "../../../requests/user/getUserData";
import listMemberInfoByTeamId from "../../../requests/team/listMemberInfoByTeamId";
import saveProjectRecord from "../../../requests/project/saveProjectRecord";
import submitProjectRecord from "../../../requests/project/submitProjectRecord";
import { FormInstance, FormRules } from "element-plus";
import listProjectRecordFormal from "../../../requests/project/listProjectRecordFormal";
import setVersion from "../../../requests/project/setVersion";
import IdeaForm from "../../../components/create/IdeaForm.vue";
import handlePublish from "../../../requests/project/handlePublish";
import GenerateIdeas from "../../../requests/create/GenerateIdeas";
import FloatingAssistant from '@/components/FloatingAssistant.vue';
import { useStore } from 'vuex';

const store = useStore(); // 获取 Vuex store
const dialogSubmitVisible = ref(false);
const dialogVersionVisible = ref(false);
const dialogPublishVisible = ref(false);
const route = useRoute();
const user = ref(null);
const adminMembers = ref([]); // 管理员池
const memberMembers = ref([]); // 成员池
const project = ref(null);
const projectRecord = ref();
const projectRecordList = ref([]);
const projectRecordFormalList = ref([]);
const content = ref("");
const selectedRecordId = ref("0"); // 用于跟踪选中的记录 ID
const drawerVisible = ref(false);
const submitData = reactive<RuleForm>({
  recordComment: "",
});
const versionData = reactive({
  versionId: "1",
});
const versionOptions = ref([]);
const projectId = ref(); // 获取路由参数
const isStreaming = ref(false);
const streamingContent = ref('');
const assistantRef = ref();

// 计算属性 isModified
const isModified = computed(() => {
  return content.value !== projectRecord.value?.content; // 判断内容是否已修改
});

onMounted(async () => {
  projectId.value = Number(route.params.id); // 获取路由参数
  project.value = await getItemById(projectId.value); // 拿到项目信息
  await loadProjectRecords(); // 加载项目记录
  await loadVersionOptions(); // 加载版本选项
  user.value = await getUserData();

  const members = await listMemberInfoByTeamId(project.value?.teamId);
  // 分类管理员和成员
  adminMembers.value = members.filter(
    (member: any) => member.teamRole === "ADMIN"
  );
  memberMembers.value = members.filter(
    (member: any) => member.teamRole === "MEMBER"
  );
});
const handleWordReplace = ({ wrong, correct }) => {
  // 执行全局替换
  const regex = new RegExp(escapeRegExp(wrong), 'g');
  content.value = content.value.replace(regex, correct);

  console.log(`已将 "${wrong}" 替换为 "${correct}"`);
};

// 辅助函数：转义正则特殊字符
function escapeRegExp(string) {
  return string.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}
// 封装加载项目记录的函数
const loadProjectRecords = async () => {
  projectRecordList.value = await listProjectRecord(projectId.value); // 拿到项目记录列表
  if (project.value?.version !== "0") {
    selectedRecordId.value = project.value?.version;
    await loadProjectRecord(selectedRecordId.value); // 加载项目记录
  } else if (
    project.value?.version === "0" &&
    projectRecordList.value.length > 0
  ) {
    selectedRecordId.value = projectRecordList.value[0].id;
    await loadProjectRecord(selectedRecordId.value); // 加载项目记录
  } else {
    selectedRecordId.value = "0";
    await loadProjectRecord(selectedRecordId.value); // 加载项目记录
  }
};

const loadVersionOptions = async () => {
  projectRecordFormalList.value = await listProjectRecordFormal(
    projectId.value
  );
  versionOptions.value = projectRecordFormalList.value.map((record) => ({
    value: record.id,
    label: record.recordComment || "无备注", // 如果没有备注则显示'无备注'
  }));
};

// 封装加载单个项目记录的函数
const loadProjectRecord = async (version: number) => {
  projectRecord.value = await getProjectRecord(version);
  content.value =
    projectRecord.value.content == null ? "" : projectRecord.value.content;
};

interface RuleForm {
  recordComment: string;
}

const ruleFormRef = ref<FormInstance>();

const rules = reactive<FormRules<RuleForm>>({
  recordComment: [
    {
      required: true,
      min: 1,
      max: 255,
      message: "请输入备注",
      trigger: "blur",
    },
  ],
});
// 选择记录并获取项目记录
const chooseRecord = async (id: number) => {
  selectedRecordId.value = id; // 设置选中的记录 ID
  await loadProjectRecord(id); // 根据 ID 获取项目记录
};

const handleSave = async () => {
  let id;

  if (projectRecord.value === undefined) id = 0;
  else id = projectRecord.value.id;
  // console.log('content', content.value);

  await saveProjectRecord({
    id: id,
    projectId: projectId.value,
    content: content.value, // 使用编辑器内容
    isSubmit: projectRecord.value?.isSubmit,
  });
  projectRecordList.value = await listProjectRecord(projectId.value);

  if (project.value?.version !== "0") {
    selectedRecordId.value = project.value?.version;
    await loadProjectRecord(selectedRecordId.value); // 重新加载项目记录
  }
};

const handleSubmit = async (formEl: FormInstance | undefined) => {
  if (!formEl) return;
  await formEl.validate(async (valid, fields) => {
    if (valid) {
      console.log("submit!");
      // 提交逻辑
      await submitProjectRecord(
        selectedRecordId.value,
        submitData.recordComment
      );
      dialogSubmitVisible.value = false;
      await loadProjectRecords();
      await loadProjectRecord(selectedRecordId.value); // 重新加载项目记录
      project.value = await getItemById(projectId.value); // 拿到项目信息
      projectRecordFormalList.value = await listProjectRecordFormal(
        projectId.value
      );
    } else {
      console.log("error submit!", fields);
    }
  });
};
const specifiedVersion = async () => {
  project.value = await getItemById(projectId.value); // 拿到项目信息
  await loadVersionOptions(); // 加载版本选项
  versionData.versionId = project.value?.version;
  dialogVersionVisible.value = true;
};
const handleSpecificed = async () => {
  await setVersion(projectId.value, Number(versionData.versionId));
  project.value = await getItemById(projectId.value); // 拿到项目信息
  dialogVersionVisible.value = false;
};
const publishWorks = async () => {
  // 确认发布
  dialogPublishVisible.value = true;
};
const assurePublish = async () => {
  await handlePublish(project.value?.id, project.value?.version)
  dialogPublishVisible.value = false;
};
const handleGenerate = async () => {
  try {
    // 如果有正在进行的生成，先终止
    if (isStreaming.value) {
      isStreaming.value = false;
      streamingContent.value = '';
    }

    // 清空原有内容
    content.value = '';
    streamingContent.value = '';
    isStreaming.value = true;

    // 显示加载动画
    console.log('加载中...');
    drawerVisible.value = false; // 关闭抽屉
    await new Promise((resolve) => setTimeout(resolve, 3000)); // 加载3秒

    console.log('加载完成，开始打字机效果');
    isStreaming.value = false;
    // 模拟打字机效果
    const simulateTyping = (text: string) => {
      let index = 0;
      const typingInterval = setInterval(() => {
        if (index < text.length) {
          streamingContent.value += text[index];
          content.value = streamingContent.value; // 更新编辑器内容
          index++;
        } else {
          clearInterval(typingInterval);
          isStreaming.value = false;
        }
      }, 50); // 每50毫秒输出一个字符
    };

    // 开始模拟打字机效果
    simulateTyping(paragraph);
  } catch (error) {
    console.error('处理生成请求错误:', error);
    isStreaming.value = false;
    streamingContent.value = '';
    content.value = '';
  }
};
// todo 临时注释掉的生成方法
// // 处理关闭抽屉的事件
// const handleGenerate = async () => {
//   try {
//     const ideasForm = store.state.ideasForm;
//     console.log('发送创意生成请求:', ideasForm);

//     // 如果有正在进行的生成，先终止
//     if (isStreaming.value) {
//       isStreaming.value = false;
//       streamingContent.value = '';
//     }

//     // 清空原有内容
//     content.value = '';
//     streamingContent.value = '';

//     // // 开始新的流式响应
//     // isStreaming.value = true;

//     // // 创建 EventSource 实例
//     // const eventSource = GenerateIdeas(ideasForm);
//     // // 关闭抽屉
//     // drawerVisible.value = false;

//     // console.log('绑定消息处理器');
//     // eventSource.onmessage = (event) => {
//     //   if (!event.data) {
//     //     console.log('空数据，跳过');
//     //     return;
//     //   }

//     //   // 处理接收到的数据
//     //   let newContent = event.data;
//     //   if (newContent.startsWith('data:')) {
//     //     newContent = newContent.slice(5).trim();
//     //   }

//     // 更新内容
//     // if (newContent && newContent !== '**' && newContent !== '**。') {
//     //   streamingContent.value += newContent;
//     //   // 实时更新编辑器内容
//     //   content.value = streamingContent.value;
//     // }
//     // };

//     // // 错误处理
//     // eventSource.onerror = (error) => {
//     //   console.error('SSE错误:', error);
//     //   eventSource.close();

//     //   // 保存当前生成的内容
//     //   if (streamingContent.value) {
//     //     content.value = streamingContent.value;
//     //   }
//     //   isStreaming.value = false;
//     //   streamingContent.value = '';
//     // };

//     // // 结束流式响应
//     // const originalClose = eventSource.close;
//     // eventSource.close = () => {
//     //   originalClose.call(eventSource);
//     //   console.log('SSE连接关闭');

//     //   // 保存最终生成的内容
//     //   if (streamingContent.value) {
//     //     content.value = streamingContent.value;
//     //   }
//     //   isStreaming.value = false;
//     // };

//   } catch (error) {
//     console.error('处理生成请求错误:', error);
//     isStreaming.value = false;
//     streamingContent.value = '';
//     content.value = '';
//   } finally {
//     // 清除 Vuex 中的表单数据
//     store.commit('clearIdeasForm');
//   }
// };

// 添加监听器来处理内容变化
watch(() => streamingContent.value, (newContent, oldContent) => {
  // console.log('streamingContent 发生变化:', {
  //   old: oldContent,
  //   new: newContent,
  //   isStreaming: isStreaming.value
  // });

  // 如果正在生成中，实时更新编辑器内容
  if (isStreaming.value) {
    content.value = newContent;
  }
}, { immediate: true });

const handleCreativeSuggestions = (content: string) => {
  assistantRef.value?.generateCreativeSuggestions(content);
};

const paragraph = '雨丝斜斜地割在车窗上，李明将警徽别得更紧了些。后视镜里，那座青砖黛瓦的小镇正被暮色吞没，只有镇口石碑上的"永宁镇"三个金字还泛着幽光。他握着方向盘的手指突然僵住——副驾驶座上的档案袋无风自动，边缘沾着的血迹正缓缓渗入泛黄的纸张。"欢迎来到永宁。"沙哑的女声从四面八方响起，李明猛地回头，空荡的后座飘着层薄雾，雾中浮着双泛红的眼睛。警用手电的光柱扫过，只来得及捕捉到白大褂上绣着的倒五芒星。警车冲进镇公所时撞翻了告示牌，散落的寻人启事背面都印着相同的图腾。李明踉跄着闯进值班室，老邮差正用头撞碎玻璃鱼缸，临死前眼睛瞪得像突出来的金鱼。"第三起了。"女法医摘下口罩时，李明看见她嘴角有道新鲜的牙印。解剖台上，死者太阳穴凹陷处渗出黑色粘液，"都是被某种频率震碎脑膜，可这镇上连手机信号塔都没有。"钟楼突然敲响午夜十二响，每声都让李明太阳穴的血管暴跳。他冲上钟楼时，巨大的铜钟正在自主摇晃，钟舌上缠着条人腿。透过钟孔望下去，全镇的窗户同时亮起蓝光，居民们举着手机围成圆阵，手机屏幕上映出无数倒悬的面孔。"他们在给祂充电。"神父从忏悔室爬出来时，整张脸都贴着鱼鳞。他塞给李明的银十字架突然融化成水银，"记住，永远别让祂看清你的脸。"李明的后颈突然传来灼热感，摸到的创可贴下，那颗本该愈合的子弹伤正在渗出黑色液体。手机屏幕亮起，来电显示是自己三个月前的号码，话筒里传来自己陌生的声音："别查了，他们已经把你的指纹刻在石碑背面。"当第一缕晨光穿透云层，李明发现自己正站在镇口石碑前。碑阴处果然多出个指纹凹槽，形状与他右手食指完美契合。远处传来孩童的诵唱，他看见整个小镇正在晨雾中缓缓旋转，青石板路上浮现出蜿蜒的血痕，正通向自己紧闭的公寓门。'
</script>

<style lang="scss" scoped>
h1 {
  text-align: center;
  margin-bottom: 20px;
}

.box {
  display: flex;
  flex-direction: column;
  align-items: center;
  /* 水平居中 */

  .project-content {
    display: flex;
    width: 100%;
    /* 设置内容宽度 */
    // max-width: 1200px;
    /* 设置最大宽度 */
    margin: 0 auto;
    /* 居中 */

    .project-modify-record {
      flex: 1;
      /* 左侧部分占比 */
      margin-right: 10px;

      /* 右侧间距 */
      .project-content-top {
        display: flex;
        align-items: center;
        justify-content: space-between;
      }

      .record {
        cursor: pointer;
        margin-top: 10px;
        margin-left: 10px;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
        background-color: #f9f9f9;

        .record-item {
          display: flex;
          flex-direction: column;
          /* 垂直排列 */
          margin-bottom: 10px;

          .record-title {
            font-weight: bold;
            font-size: 1.1rem;
          }

          .record-date {
            font-size: 0.9rem;
            color: #888;
          }

          .record-content {
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 0.9rem;
            margin-top: 5px;
          }
        }

        &.highlighted {
          background-color: #e9e9e9;
          /* 高亮颜色 */
        }
      }
    }

    .project-editor {
      flex: 3;
      /* 中间部分占比 */
      display: flex;
      justify-content: center;
      padding: 20px;
      border: 1px solid #ccc;

      .editor {
        width: 100%;
        /* 确保编辑器宽度为100% */
      }
    }

    .project-message {
      flex: 1;
      /* 右侧部分占比 */
      margin-left: 10px;
      /* 左侧间距 */
      padding: 20px;
      border: 1px solid #ccc;
      /* 添加边框 */
      border-radius: 10px;
      /* 设置弧度 */
      background-color: rgba(249, 249, 249, 0.8);
      /* 添加透明度的背景颜色 */

      .author {
        display: flex;
        align-items: center;

        .avatar {
          width: 2.8rem;
          height: 2.8rem;
          border-radius: 50%;
          overflow: hidden;
        }

        .authorName {
          margin-left: 1rem;
        }
      }

      .name {
        font-weight: 600;
      }

      .message {
        display: flex;
        justify-content: space-between;
        margin-top: 10px;

        .message-right {
          width: 70%;
          max-height: 220px;
          text-align: justify;
          overflow: hidden;
          /* 超出部分隐藏 */

          // white-space: nowrap;
          /* 不换行 */
          text-overflow: ellipsis;
        }
      }

      .avatar-pool {
        display: flex;
        flex-wrap: wrap;
        justify-content: flex-start;
        padding: 10px;
        width: 95%;
        border-radius: 8px;
        background-color: rgba(231, 210, 210, 0.5);
        /* 添加透明度的背景颜色 */

        .avatar {
          width: 2.8rem;
          height: 2.8rem;
          border-radius: 50%;
          overflow: hidden;
          margin: 8px;
        }
      }

      .button-container {
        display: flex;
        justify-content: flex-start;
        /* 从容器的起始位置开始排列 */
        align-items: center;
        /* 垂直居中对齐 */
        gap: 10px;
        /* 按钮之间的间距 */
      }
    }
  }

  .version-tag {
    margin-left: 10px;
    /* 添加左边距以分隔文本和标签 */
  }
}

.text-content {
  overflow: hidden;
  /* 隐藏溢出内容 */
  white-space: nowrap;
  /* 不换行 */
  text-overflow: ellipsis;
  /* 溢出部分用省略号表示 */
  max-width: 220px;
  /* 设置最大宽度，根据需要调整 */
}
</style>
