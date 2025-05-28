<template>
    <div class="profile-info">
        <h2>个人信息</h2>
        <p><strong>姓名：</strong>{{ name }}</p>
        <p><strong>职位：</strong>{{ position }}</p>
        <p><strong>简介：</strong>{{ bio }}</p>
    </div>
</template>

<script setup lang="ts">
const props = defineProps<{
    name: string;
    position: string;
    bio: string;
}>();
</script>

<style lang="scss" scoped>
.profile-info {
    margin-bottom: 20px;
}
</style>