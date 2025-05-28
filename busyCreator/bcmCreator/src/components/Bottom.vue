<template>
    <div class="bottom">
        <!-- <div class="bah">xx网安备xxxxxxx号
            互联网新闻信息服务许可
            ©2025 xxxxx
            使用前必读</div> -->
    </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter();

const goToIndex = () => {
    router.push('/')
}
</script>

<style lang="scss" scoped>
.bottom {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100vw;
    // height: 300px;


    .logo {
        height: 48px;
        cursor: pointer;
    }
}
</style>