<template>
    <el-menu class="el-menu-demo menu" :default-active="defaultMenu" :router="true">
        <el-menu-item index="/">
            <el-icon>
                <icon-find icon-color=""></icon-find>
            </el-icon>
            <span>发现</span>
        </el-menu-item>
        <el-menu-item index="/create">
            <el-icon>
                <icon-stars icon-color=""></icon-stars>
            </el-icon>
            <span>创作</span>
        </el-menu-item>
        <el-menu-item index="/project">
            <el-icon>
                <icon-project icon-color=""></icon-project>
            </el-icon>
            <span>项目</span>
        </el-menu-item>
        <el-menu-item index="/team">
            <el-icon>
                <icon-team icon-color=""></icon-team>
            </el-icon>
            <span>团队</span>
        </el-menu-item>
        <el-menu-item index="/personal/profile">
            <el-icon>
                <icon-my icon-color=""></icon-my>
            </el-icon>
            <span>我的</span>
        </el-menu-item>
    </el-menu>

</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import iconMy from './icons/sidebar/icon-my.vue';
import iconFind from './icons/sidebar/icon-find.vue';
import iconProject from './icons/sidebar/icon-project.vue';
import iconStars from './icons/sidebar/icon-stars.vue';
import iconTeam from './icons/sidebar/icon-team.vue';

const route = useRoute();
const defaultMenu = computed(() => { return route.path })

</script>

<style lang="scss" scoped>
.menu {
    height: 100%;
    padding: 10px;
    background: transparent;

    .el-menu-item {
        border-radius: 10px;
        margin-bottom: 5px;
    }

    .el-menu-item.is-active {
        background-color: #ddd;
    }
}
</style>