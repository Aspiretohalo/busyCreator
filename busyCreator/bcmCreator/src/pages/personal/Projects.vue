<template>
    <div>
        <h2>项目</h2>
        <ul>
            <li v-for="project in projects" :key="project.title">
                <strong>{{ project.title }}</strong>: {{ project.description }}
            </li>
        </ul>
    </div>
</template>

<script setup lang="ts">
const projects = [
    { title: '项目 A', description: '这是项目 A 的描述。' },
    { title: '项目 B', description: '这是项目 B 的描述。' },
];
</script>

<style lang="scss" scoped>
/* 添加样式 */
</style>