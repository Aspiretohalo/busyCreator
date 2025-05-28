<template>
    <div class="common-layout">
        <el-container style="padding: 0;">
            <el-header style="height: 60px;padding: 0;">
                <TopNav></TopNav>
            </el-header>
            <el-container>
                <el-aside width="200px" style="position: fixed;height: 100vh;">
                    <SideNav></SideNav>
                </el-aside>
                <el-main style="padding: 0; padding-left: 200px;width: 100vw;overflow: hidden;">
                    <RouterView></RouterView>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script lang="ts" setup>
import TopNav from '../components/TopNav.vue'
import SideNav from '../components/SideNav.vue'
</script>

<style lang="scss" scoped>
:deep(.el-card) {
    border-radius: 15px;
}
</style>