<template>
    <div class="find-list">
        <FindToolItem v-for="item in toolItems" :key="item.title" :item="item" />
    </div>
</template>

<script setup lang="ts">
import FindToolItem from './FindToolItem.vue';

// 改名为 toolItems，避免与 props 冲突
const toolItems = [
    {
        title: "辅助小说创作",
        description: "轻松的小说创作，引领入门的好帮手！",
        image: "https://files-1317662942.cos.ap-nanjing.myqcloud.com/%E5%AD%A6%E4%B9%A0.png",
        path: "/create"
    }, {
        title: "智能评论",
        description: "AI生成各类评论，兼顾时效性与多样性！",
        image: "https://files-1317662942.cos.ap-nanjing.myqcloud.com/%E8%AF%84%E8%AE%BA.png",
        path: "/create"
    }, {
        title: "精准广告营销",
        description: "助您精准定位受众，提高广告转化率！",
        image: "https://files-1317662942.cos.ap-nanjing.myqcloud.com/%E5%B9%BF%E5%91%8A.png",
        path: "/create"
    }
];

// 如果这个组件确实需要从外部接收 items，保留这个 props 定义
// 否则可以删除这部分
// const props = defineProps<{
//     items: Array<{
//         id: number;
//         cover: string;
//         name: string;
//         profile: string;
//         collects: number;
//     }>;
// }>();
</script>

<style lang="scss" scoped>
.find-list {
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;
    gap: 10px;
    width: 69%;
}
</style>