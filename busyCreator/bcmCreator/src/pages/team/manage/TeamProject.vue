<template>
    <div class="box">
        <div class="main">
            <div class="tab-container">
                <span :class="{ active: currentTab === 'all' }" @click="filterItems('all')">全部</span>
                <span :class="{ active: currentTab === 'published' }" @click="filterItems('published')">已发布</span>
                <span :class="{ active: currentTab === 'unpublished' }" @click="filterItems('unpublished')">未发布</span>
            </div>
            <div class="item-cards">

                <div class="item-card" v-for="item in filteredItems" :key="item?.id" @click="goToDetail(item?.id)">
                    <div class="item-card-top">
                        <div class="item-card-avatar">
                            <img :src="item.cover" alt="" class="cover-image">
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
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import getTeamById from '../../../requests/team/getTeamById';
import getProjectsByTeamId from '../../../requests/team/listProjectsByTeamId';
const currentTab = ref('all'); // 当前选中的标签
const router = useRouter();
const route = useRoute();
const teamInfo = ref(null);
const items = ref([]);

onMounted(async () => {
    const teamId = Number(route.params.id); // 获取路由参数
    teamInfo.value = await getTeamById(teamId); // 获取团队信息
    items.value = await getProjectsByTeamId(teamId); // 获取团队项目
});

const goToDetail = (id: number) => {
    router.push({ name: 'projectDetail', params: { id } }); // 跳转到项目详情页
};

const filteredItems = computed(() => {
    if (currentTab.value === 'published') {
        return items.value.filter(item => item.isPublic === "1"); // 过滤已发布项目
    } else if (currentTab.value === 'unpublished') {
        return items.value.filter(item => item.isPublic === "0"); // 过滤未发布项目
    }
    return items.value; // 默认返回所有项目
});

const filterItems = (tab: string) => {
    currentTab.value = tab; // 更新当前选中的标签
};

</script>

<style lang="scss" scoped>
.box {
    display: flex;
    width: 100%;
    margin-top: 20px;
    border-radius: 15px;

    .main {
        width: 1200px;
        margin: 0 auto;
    }
}

.item-cards {
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;
    gap: 10px;
    margin: 8px;
}

.item-card {
    display: flex;
    flex-direction: column;
    width: 245px;
    height: 80px;
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
            img.cover-image {
                width: 60px;
                height: 60px;
                object-fit: cover;
                border-radius: 50%;
            }
        }

        .item-card-msg {
            margin-left: 10px;
            width: 180px;

            .name {
                font-weight: bold;
                font-size: 20px;
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
    margin-bottom: 20px;

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
</style>
