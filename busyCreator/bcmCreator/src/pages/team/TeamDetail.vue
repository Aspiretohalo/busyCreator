<template>
    <div class="box">
        <TopNavDetail :team-info="item" from-page="/team"></TopNavDetail>
        <div class="layout">
            <div v-if="item" class="main">
                <div class="item-card-top">
                    <div class="item-card-avatar">
                        <img :src="item.avatar" alt="">
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
                <div class="member-count">成员:{{ item.memberCount }}</div>

                <el-menu :default-active="activeIndex" :router="true" class="el-menu-demo" mode="horizontal"
                    @select="handleSelect">
                    <el-menu-item index="project">团队项目</el-menu-item>
                    <el-menu-item index="permission">成员权限</el-menu-item>
                    <el-menu-item index="setting">团队设置</el-menu-item>
                </el-menu>
                <div class="detail">
                    <router-view></router-view>
                </div>
            </div>
            <div v-else>
                <p>加载中...</p>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import TopNavDetail from '@/components/TopNavDetail.vue';
import getTeamById from '../../requests/team/getTeamById';

const route = useRoute();
const item = ref(null);
const activeIndex = ref('project'); // 默认值

onMounted(async () => {
    const itemId = Number(route.params.id); // 获取路由参数
    item.value = await getTeamById(itemId);

    // 根据当前路由设置 activeIndex
    if (route.name) {
        activeIndex.value = route.name === 'teamProject' ? 'project' :
            route.name === 'teamPermission' ? 'permission' :
                route.name === 'teamSetting' ? 'setting' : 'project';
    }
});

const handleSelect = (key: string, keyPath: string[]) => {
    console.log(key, keyPath);
}
</script>

<style lang="scss" scoped>
/* 添加样式以确保页面整体美观 */
h1 {
    text-align: center;
    margin-bottom: 20px;
}

.el-menu {
    background-color: transparent;
}

.box {
    display: flex;
    width: 100%;

    .layout {
        margin-top: 100px;
        display: flex;
        justify-content: center;
        width: 100%;

        .main {
            width: 1200px;
            /* 设置宽度为 1200px */
            margin: 0 auto;
            /* 居中显示 */
        }
    }
}

.item-card-top {
    display: flex;
    align-items: center;

    .item-card-avatar {
        img {
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
        }

        .description {
            color: rgba(30, 31, 36, 0.5);
        }
    }
}

.member-count {
    display: flex;
    justify-content: flex-end;
    margin-top: 10px;
}
</style>