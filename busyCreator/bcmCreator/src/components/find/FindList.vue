<template>
    <div class="find-list">
        <FindItem v-for="item in items" :key="item.id" :item="item" />
    </div>
</template>

<script setup lang="ts">
import FindItem from './FindItem.vue';

const props = defineProps<{
    items: Array<{
        id: number;
        cover: string;
        name: string;
        profile: string;
        thumbCount: number;
        bookmarkCount: number;
    }>;
}>();
</script>

<style lang="scss" scoped>
.find-list {
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;
    gap: 10px;
    margin-left: 20px;
}
</style>