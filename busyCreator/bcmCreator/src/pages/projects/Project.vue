<template>
  <div class="box">
    <div class="box-top">
      <div class="left-section">
        <h2>项目</h2>
        <div class="ultra-btn" @click="handleOpen()">创建项目</div>
        <div class="ultra-btn" @click="handleOpen2()" style="margin-left: 20px">
          任务指派
        </div>
      </div>
      <div class="tab-container">
        <span :class="{ active: currentTab === 'all' }" @click="filterItems('all')">全部</span>
        <span :class="{ active: currentTab === 'published' }" @click="filterItems('published')">已发布</span>
        <span :class="{ active: currentTab === 'unpublished' }" @click="filterItems('unpublished')">未发布</span>
      </div>
    </div>
    <div class="item-cards">
      <div class="item-card" v-for="item in filteredItems" :key="item?.id" @click="goToDetail(item?.id)">
        <div class="item-card-top">
          <div class="item-card-avatar">
            <img :src="item.cover" alt="" />
          </div>
          <div class="item-card-msg">
            <div class="name">
              {{ item.name }}
            </div>
            <div class="description">
              {{ item.profile }}
            </div>
          </div>
        </div>
        <el-tag v-if="item.isPublic === '1'" class="status-tag" type="success">已发布</el-tag>
        <el-tag v-else class="status-tag" type="warning">未发布</el-tag>
      </div>
    </div>
    <el-dialog v-model="dialogFormVisible" title="新建项目" width="500">
      <el-form :model="itemData">
        <el-form-item label="项目名称" label-width="auto">
          <el-input v-model="itemData.name" autocomplete="off" />
        </el-form-item>
        <el-form-item label="团队选择" label-width="auto">
          <el-autocomplete v-model="state" :fetch-suggestions="querySearch" clearable class="inline-input w-50"
            placeholder="请输入团队名称" @select="handleSelect" />
        </el-form-item>
        <el-form-item label="标签设置" label-width="auto">
          <div class="tag-list">
            <el-tag v-for="tag in tags" :key="tag" effect="light" closable :disable-transitions="false"
              @close="handleClose(tag)" class="tag-item">
              {{ tag }}
            </el-tag>
            <el-input v-if="inputVisible" ref="InputRef" v-model="inputValue" class="w-20" size="small"
              @keyup.enter="handleCreateTag" @blur="handleCreateTag" />
            <el-button v-else class="button-new-tag" size="small" @click="showInput">
              + 新增
            </el-button>
          </div>
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button class="ultra-btn" type="primary" @click="
            handleCreate();
          dialogFormVisible = false;
          ">
            新建
          </el-button>
        </div>
      </template>
    </el-dialog>
    <el-dialog v-model="dialogTaskVisible" title="任务中心" width="1200">
      <template #header="">
        <div class="my-header">
          <h2>任务中心</h2>
          <el-button class="createBtn ultra-btn" type="primary" @click="handleOpen3()">
            新建任务
          </el-button>
        </div>
      </template>
      <el-table :data="paginatedTaskData" style="width: 100%">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="content" label="任务内容" />
        <el-table-column prop="projectName" label="所属项目" />
        <el-table-column prop="assigneeName" label="任务对象" width="100" />
        <el-table-column prop="finishDate" label="计划完成时间" width="120" />
        <el-table-column prop="workTime" label="花费工时" width="150">
          <template #default="scope">
            <el-input-number v-model="scope.row.workTime" :min="1" :max="10" size="small"
              :disabled="!(scope.row.isSubmitted === '0' && scope.row.status === '1')" />
          </template>
        </el-table-column>
        <el-table-column prop="note" label="备注" width="250">
          <template #default="scope">
            <el-input v-model="scope.row.note" :rows="2" type="textarea" placeholder="请输入备注"
              :disabled="!(scope.row.isSubmitted === '0' && scope.row.status === '1')" />
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" min-width="80">
          <template #default="scope">
            <el-button v-if="scope.row.status === '1'" type="primary"
              :disabled="!(scope.row.isSubmitted === '0' && scope.row.status === '1')"
              @click="handleSave(scope.row)">保存</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="currentPage" :page-size="pageSize" layout="total, prev, pager, next"
        :total="taskTableData.length" @current-change="handlePageChange" />
    </el-dialog>
    <el-dialog v-model="dialogTaskCreateVisible" title="新建任务" width="1200" @close="handleTaskDialogClose">
      <el-form ref="taskFormRef" :model="taskData" :rules="taskRules" label-position="right">
        <el-form-item label="任务内容" label-width="120" prop="content">
          <el-input v-model="taskData.content" autocomplete="off" placeholder="请输入任务内容" />
        </el-form-item>
        <el-form-item label="所属项目" label-width="120" prop="projectName">
          <el-select-v2 v-model="taskData.projectName" placeholder="所属项目" :options="projectOptions"
            @change="updateProjectSelect" />
        </el-form-item>
        <el-form-item label="任务对象" label-width="120" prop="assigneeName">
          <el-select-v2 v-model="taskData.assigneeName" placeholder="任务执行人" :options="assigneeNameOptions"
            :disabled="!taskData.projectName" @change="updateAssigneeSelect" />
        </el-form-item>
        <el-form-item label="计划完成时间" label-width="120" prop="finishDate">
          <el-date-picker v-model="taskData.finishDate" type="date" placeholder="Pick a day" size="default" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button class="ultra-btn" type="primary" @click="handleTaskCreate()">
            新建
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, computed, nextTick, watch } from "vue";
import { useRouter } from "vue-router";
import listProjects from "../../requests/project/listProjects";
import createProject from "../../requests/project/createProject";
import listTeams from "../../requests/team/listTeams";
import { ElInput } from "element-plus";
import type { InputInstance } from "element-plus";
import createTask from "../../requests/task/createTask";
import listAdminProject from "../../requests/task/listAdminProject";
import listTeamMember from "../../requests/task/listTeamMember";
import listTask from "../../requests/task/listTask";
import { formatDate } from '../../utils/date';
import updateTask from '../../requests/task/updateTask';

const router = useRouter();
const dialogFormVisible = ref(false);
const dialogTaskVisible = ref(false);
const dialogTaskCreateVisible = ref(false);
const items = ref([]);
const restaurants = ref<RestaurantItem[]>([]);
const state = ref("");
const currentTab = ref("all"); // 当前选中的标签
const tags = ref<string[]>([]); // 用于存储标签
const inputValue = ref("");
const inputVisible = ref(false);
const InputRef = ref<InputInstance>();

const taskTableData = ref([
  {
    name: "任务1",
    projectName: "所属项目",
    assigneeName: "小c",
    finishDate: "2025-02-19",
    workTime: 1,
    note: "",
  },
]);
// 用于表单提交
const taskData = reactive({
  content: "",
  projectId: 0,
  projectName: "",
  assigneeId: 0,
  assigneeName: "",
  finishDate: "",
  workTime: "",
  note: "",
});

const projectOptions = ref([]);
const assigneeNameOptions = ref([]);
const taskFormRef = ref(null)

interface RestaurantItem {
  value: string;
  teamId: number;
}

const refreshData = async () => {
  items.value = await listProjects(); // 获取已加入团队的项目
  const teams = await listTeams(); // 获取未加入的团队
  restaurants.value = teams.map((item: any) => ({
    value: item.name,
    teamId: item.id,
  }));
};

onMounted(async () => {
  await refreshData(); // 初始加载数据
});

const handleOpen = () => {
  dialogFormVisible.value = true;
};
const handleOpen2 = async () => {
  const tasks = await listTask();
  taskTableData.value = tasks.map(task => ({
    ...task,
    finishDate: formatDate(task.finishDate), // 格式化 finishDate
  }));
  dialogTaskVisible.value = true;
};
const handleOpen3 = async () => {
  let adminProjects = await listAdminProject()
  projectOptions.value = adminProjects.map((item: any) => ({
    value: item.id,
    label: item.name, // 如果没有备注则显示'无备注'
  }));
  dialogTaskCreateVisible.value = true;
};
const handleCreate = async () => {
  itemData.teamId = selectedTeamId.value;
  await createProject(itemData);
  await refreshData(); // 刷新数据
  dialogFormVisible.value = false;
};

// 添加关闭对话框时的处理函数
const handleTaskDialogClose = () => {
  taskFormRef.value?.resetFields()
  taskData.content = ''
  taskData.projectName = ''
  taskData.assigneeName = ''
  taskData.finishDate = ''
}

// 在 select 改变的时候，把值赋给表单
const updateProjectSelect = (value: any) => {
  const selectedOption = projectOptions.value.find(option => option.value === value);
  if (selectedOption) {
    taskData.projectId = Number(selectedOption.value);
  }
};
const updateAssigneeSelect = (value: any) => {
  const selectedOption = assigneeNameOptions.value.find(option => option.value === value);
  if (selectedOption) {
    taskData.assigneeId = Number(selectedOption.value);
  }
};

const handleTaskCreate = async () => {
  if (!taskFormRef.value) return

  await taskFormRef.value.validate(async (valid, fields) => {
    if (valid) {
      // 这里是原来的创建任务逻辑
      let task = {
        content: taskData.content, projectId: taskData.projectId, assigneeId: taskData.assigneeId, finishDate: taskData.finishDate
      }

      await createTask(task)

      handleTaskDialogClose() // 创建成功后清空表单
      dialogTaskCreateVisible.value = false
      await refreshTaskData(); // 创建成功后重新获取任务数据
    } else {
      console.log('验证失败', fields)
      return false
    }
  })
}


const itemData = reactive({
  name: "",
  profile: "",
  cover:
    "https://ss2.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3156919332,3306242375&fm=253&gp=0.jpg",
  teamId: 0,
  tags: tags.value,
});

const selectedTeamId = ref(); // 用于存储选中的团队 ID

const querySearch = (queryString: string, cb: any) => {
  const results = queryString
    ? restaurants.value.filter(createFilter(queryString))
    : restaurants.value;
  cb(results);
};

const createFilter = (queryString: string) => {
  return (restaurant: RestaurantItem) => {
    return (
      restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
    );
  };
};

const handleSelect = (item: RestaurantItem) => {
  selectedTeamId.value = item.teamId; // 存储选中的团队 ID
};

const goToDetail = (id: number) => {
  router.push({ name: "projectDetail", params: { id } }); // 跳转到项目详情页
};

const filteredItems = computed(() => {
  if (currentTab.value === "published") {
    return items.value.filter((item) => item.isPublic === "1"); // 过滤已发布项目
  } else if (currentTab.value === "unpublished") {
    return items.value.filter((item) => item.isPublic === "0"); // 过滤未发布项目
  }
  return items.value; // 默认返回所有项目
});

const filterItems = (tab: string) => {
  currentTab.value = tab; // 更新当前选中的标签
};

const handleCreateTag = () => {
  if (inputValue.value) {
    tags.value.push(inputValue.value); // 将新标签字符串添加到数组中
  }
  console.log(tags.value);
  inputVisible.value = false;
  inputValue.value = "";
};

const handleClose = (tag: string) => {
  tags.value.splice(tags.value.indexOf(tag), 1); // 根据字符串删除标签
};

const showInput = () => {
  inputVisible.value = true;
  nextTick(() => {
    InputRef.value!.input!.focus();
  });
};

const taskRules = {
  content: [
    { required: true, message: '请输入任务内容', trigger: 'blur' }
  ],
  projectName: [
    { required: true, message: '请选择所属项目', trigger: 'change' }
  ],
  assigneeName: [
    { required: true, message: '请选择任务执行人', trigger: 'change' }
  ],
  finishDate: [
    { required: true, message: '请选择计划完成时间', trigger: 'change' }
  ]
}

const fetchAssignees = async (projectId: any) => {

  // 假设有一个函数 listProjectMembers 用于获取项目成员
  const members = await listTeamMember(projectId);
  assigneeNameOptions.value = members.map((member: any) => ({
    value: member.id,
    label: member.userName,
  }));
  console.log('assigneeNameOptions', assigneeNameOptions.value);
};

watch(() => taskData.projectName, (newProjectId) => {
  if (newProjectId) { // 仅在 projectName 不为空时执行
    taskData.assigneeName = '';
    fetchAssignees(newProjectId);
  }
});

const handleSave = async (row) => {
  try {
    await updateTask({
      taskId: row.id, // 假设任务 ID 存储在 row.id
      workTime: row.workTime,
      note: row.note
    });
    console.log('任务更新成功');
    await refreshTaskData(); // 更新成功后重新获取任务数据
  } catch (error) {
    console.error('任务更新失败', error);
  }
};

const refreshTaskData = async () => {
  const tasks = await listTask();
  taskTableData.value = tasks.map(task => ({
    ...task,
    finishDate: formatDate(task.finishDate), // 格式化 finishDate
  }));
};

// todo 一段非常不正规的前端分页
const currentPage = ref(1);
const pageSize = ref(10);

const paginatedTaskData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value;
  const end = start + pageSize.value;
  return taskTableData.value.slice(start, end);
});

const handlePageChange = (page) => {
  currentPage.value = page;
};
</script>

<style lang="scss" scoped>
.box {
  padding: 20px;
}

.my-header {
  display: flex;

  .createBtn {
    margin-left: 20px;
  }
}

.box-top {
  display: flex;
  justify-content: space-between; // 左右分布
  align-items: center;

  .left-section {
    display: flex;
    align-items: center;

    h2 {
      margin-right: 20px;
    }
  }
}

.item-cards {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  gap: 30px;
  margin-left: 20px;
}

.item-card {
  display: flex;
  flex-direction: column;
  width: 260px;
  height: 90px;
  border-radius: 15px;
  background-color: #fff;
  padding: 20px;
  margin-top: 20px;
  cursor: pointer;

  &:hover {
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); // 增加阴影效果
    transform: scale(1.02); // 轻微放大效果
    transition: transform 0.2s, box-shadow 0.2s; // 添加过渡效果
  }

  .item-card-top {
    display: flex;
    align-items: center;

    .item-card-avatar {
      img {
        object-fit: cover;
        width: 60px;
        height: 60px;
        border-radius: 100%;
      }

      width: 60px;
      height: 60px;
    }

    .item-card-msg {
      margin-left: 10px;

      .name {
        font-weight: bold;
        font-size: 20px;
        width: 190px;
      }

      .description {
        color: rgba(30, 31, 36, 0.5);
      }
    }
  }

  .status-tag {
    margin-top: 10px; // 调整标签与卡片内容的间距
    align-self: flex-end; // 右下角对齐
  }

  .member-count {
    display: flex;
    justify-content: flex-end;
    margin-top: 10px;
  }
}

/* src/pages/team/manage/TeamProject.vue */
.item-card-msg .name {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.item-card-msg .description {
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  /* 限制为两行 */
  -webkit-box-orient: vertical;
  text-overflow: ellipsis;
}

.tab-container {
  display: flex;

  span {
    margin-right: 20px;
    cursor: pointer;
    padding: 10px;
    border-radius: 5px;
    transition: background-color 0.3s;

    &.active {
      background-color: #007bff; // 激活状态的背景色
      color: white; // 激活状态的文字颜色
    }

    &:hover {
      background-color: #0056b3; // 悬停状态的背景色
      color: white; // 悬停状态的文字颜色
    }
  }
}

.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
</style>
