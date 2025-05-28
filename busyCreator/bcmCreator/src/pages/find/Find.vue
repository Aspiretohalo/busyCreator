<template>
  <div>
    <div class="bannerBox">
      <div class="text">
        <div class="main">AI创作助手，创作快人一步</div>
        <div class="sub">
          一站式AI创作平台：一键续润扩，3分钟成文，海量灵感助力爆款
        </div>
      </div>
      <div class="cards">
        <div class="ideaCards">
          <FindIdeaCard v-for="item in items_IdeaCard" :key="item.title" :item="item" @click="handleClick(item)" />
        </div>
        <FindToolList />
      </div>
      <el-dialog v-model="dialogVisible" title="灵感搜索" width="500px">
        <div class="inspiration-dialog" v-loading="loadingFlag">
          <div class="dialog-content">
            <div class="input-prompt">输入本次使用平台想找的灵感</div>
            <el-input v-model="inspirationInput" type="textarea" :rows="3" placeholder="请输入您想寻找的灵感关键词或主题..."
              class="inspiration-input" />
            <div class="dialog-actions">
              <el-button type="primary" @click="handleSearch" class="ultra-btn">前往</el-button>
            </div>
          </div>
        </div>
      </el-dialog>
    </div>

    <h2>优质作品推荐</h2>
    <FindList :items="items" />
  </div>
</template>

<script setup lang="ts">
import FindIdeaCard from "@/components/find/FindIdeaCard.vue";
import FindList from "@/components/find/FindList.vue";
import FindToolList from "@/components/find/FindToolList.vue";
import listWorks from "../../requests/find/listWorks";
import { onMounted, ref } from "vue";
import listWorksLogout from "../../requests/find/listWorksLogout";
import searchInspiration from "../../requests/find/searchInspiration";
import { useStore } from 'vuex'; // 引入 Vuex store
import { useRouter } from 'vue-router';
import { ElMessage } from "element-plus";
const router = useRouter();
const store = useStore(); // 获取 Vuex store
const token: any = ref(localStorage.getItem('token') || null)
const items = ref([]);
const dialogVisible = ref(false)
const items_IdeaCard = ref([
  {
    title: "文体风格",
    description: "快来模仿一种喜欢的文体创作吧！",
    buttonText: "尝试文体",
  },
  {
    title: "灵感素材",
    description: "无限的灵感等你来尝试！",
    buttonText: "寻找灵感",
  },
]);

const handleClick = (item: any) => {
  if (item.buttonText === '尝试文体') {

  } else if (item.buttonText === '寻找灵感') {
    dialogVisible.value = true
  }
}
// 增加输入框的响应式变量
const inspirationInput = ref('');
const inspirationList = ref('');
const loadingFlag = ref(false);

// 搜索处理函数
const handleSearch = async () => {
  // 检查输入是否为空
  if (inspirationInput.value.trim()) {
    // 显示加载动画
    loadingFlag.value = true;

    try {
      // 调用搜索逻辑
      console.log('搜索灵感：', inspirationInput.value);
      const searchResult = await searchInspiration(inspirationInput.value);

      // 将结果存储到 Vuex 中
      store.commit('setInspirationData', searchResult);
      console.log(store.state.inspirationData);

      setTimeout(() => {

      }, 1500)
      // 跳转到创建页面
      router.push('/create');
      ElMessage({
        showClose: true,
        message: '已根据主题为您推荐灵感！',
        type: 'success',
      })
      dialogVisible.value = false;

    } catch (error) {
      console.error('搜索失败：', error);
    } finally {
      // 隐藏加载动画
      loadingFlag.value = false;
    }
  }
};

onMounted(async () => {
  if (token.value !== null) {
    items.value = await listWorks();
  } else {
    items.value = await listWorksLogout();
  }
});
</script>

<style lang="scss" scoped>
.bannerBox {
  display: flex;
  flex-direction: column;
  background-color: #fff;
  width: 95%;
  border-radius: 20px;
  margin: 20px;
  padding: 10px;

  .text {
    .main {
      font-weight: 600;
      font-size: 20px;
      margin: 10px;
    }

    .sub {
      margin: 10px;
      color: rgba(30, 31, 36, 0.5);
    }
  }

  .cards {
    display: flex;

    .ideaCards {
      width: 31%;
      margin-right: 10px;
    }
  }
}

/* 添加样式以确保页面整体美观 */
h2 {
  text-align: left;
  margin: 20px;
}

.inspiration-dialog {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 10px 0 0 0;

  .dialog-content {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }

  .input-prompt {
    font-size: 16px;
    font-weight: 500;
    margin-bottom: 10px;
    color: #409EFF;
    text-align: left;
    width: 100%;
  }

  .inspiration-input {
    width: 100%;
    margin-bottom: 20px;
  }

  .dialog-actions {
    width: 100%;
    display: flex;
    justify-content: flex-end;
  }

  .send-btn {
    min-width: 80px;
    font-size: 16px;
    border-radius: 20px;
    background: linear-gradient(90deg, #409EFF 0%, #66b1ff 100%);
    color: #fff;
    border: none;
    box-shadow: 0 2px 8px rgba(64, 158, 255, 0.08);
    transition: background 0.3s;
  }

  .send-btn:hover {
    background: linear-gradient(90deg, #66b1ff 0%, #409EFF 100%);
  }
}
</style>
