<template>
    <div class="project-list">
        <h2>项目</h2>
        <ul>
            <li v-for="project in projects" :key="project.title">
                <strong>{{ project.title }}</strong>: {{ project.description }}
            </li>
        </ul>
    </div>
</template>

<script setup lang="ts">
const props = defineProps<{
    projects: Array<{
        title: string;
        description: string;
    }>;
}>();
</script>

<style lang="scss" scoped>
.project-list {
    margin-bottom: 20px;
}
</style>