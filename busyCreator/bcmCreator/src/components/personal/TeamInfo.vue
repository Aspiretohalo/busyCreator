<template>
    <div class="team-info">
        <h2>团队信息</h2>
        <p><strong>团队名称：</strong>{{ teamName }}</p>
        <p><strong>团队成员：</strong>{{ members.join(', ') }}</p>
    </div>
</template>

<script setup lang="ts">
const props = defineProps<{
    teamName: string;
    members: string[];
}>();
</script>

<style lang="scss" scoped>
.team-info {
    margin-bottom: 20px;
}
</style>