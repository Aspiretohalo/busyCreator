<template>
    <div class="works-detail">
        <TopNavDetail :team-info="project?.teamInfo" from-page="/" :project-id="Number(route.params.id)" :thumb-status="{
            thumbCount: project?.thumbCount || 0,
            bookmarkCount: project?.bookmarkCount || 0,
            isThumbed: thumbStatusObj?.isThumbed || false,
            isBookmarked: thumbStatusObj?.isBookmarked || false,
        }" @update-thumb-status="updateThumbStatus" class="top-nav">
        </TopNavDetail>
        <div class="content-container">
            <div class="box">
                <div v-if="project">
                    <h2>{{ project.name }}</h2>
                    <p>{{ project.profile }}</p>
                    <!-- 其他项目详情 -->
                    <div class="content" v-html="project.content"></div>
                </div>
                <div v-else>
                    <p>加载中...</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import TopNavDetail from '@/components/TopNavDetail.vue';
import getItemById from '../../../requests/find/getItemById';
import getThumbStatus from '../../../requests/project/getThumbStatus';

const route = useRoute();
const project = ref(null);
const thumbStatusObj = ref();

// 更新点赞状态的函数
const updateThumbStatus = async () => {
    const projectId = Number(route.params.id);
    // 重新获取项目信息和点赞状态
    project.value = await getItemById(projectId);
    thumbStatusObj.value = await getThumbStatus(projectId);
};

onMounted(async () => {
    const projectId = Number(route.params.id);
    project.value = await getItemById(projectId);
    thumbStatusObj.value = await getThumbStatus(projectId);
});
</script>

<style lang="scss" scoped>
.works-detail {
    min-height: 100vh;
    display: flex;
    flex-direction: column;
}

.top-nav {
    position: sticky;
    top: 0;
    z-index: 100;
    background-color: #fff;
}

.content-container {
    flex: 1;
    background-color: #f5f5f5;
    display: flex;
    justify-content: center;
}

.box {
    background-color: #fff;
    border-radius: 8px;
    padding: 32px 48px; // 增加内边距使内容更集中
    width: 1200px;

    h2 {
        font-size: 28px;
        margin-bottom: 16px;
        text-align: center;
    }

    p {
        color: #666;
        font-size: 16px;
        line-height: 1.6;
        margin-bottom: 32px;
        text-align: justify;
        text-indent: 2em;
    }

    .content {
        padding: 32px;
        border-radius: 4px;
        margin-bottom: 32px;
        line-height: 1.8; // 增加行高提高可读性

        // 内容样式
        :deep(p) {
            margin-bottom: 16px;
        }

        :deep(img) {
            max-width: 100%;
            height: auto;
            margin: 16px 0;
        }

        :deep(h1, h2, h3, h4, h5, h6) {
            margin: 24px 0 16px;
        }
    }
}
</style>