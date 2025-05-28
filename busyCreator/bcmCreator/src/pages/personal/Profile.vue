<template>
    <div>
        <h2>个人信息</h2>
        <p><strong>姓名：</strong>{{ name }}</p>
        <p><strong>职位：</strong>{{ position }}</p>
        <p><strong>简介：</strong>{{ bio }}</p>
    </div>
</template>

<script setup lang="ts">
const name = '张三';
const position = '前端开发工程师';
const bio = '热爱编程，喜欢探索新技术。';
</script>

<style lang="scss" scoped>
/* 添加样式 */
</style>