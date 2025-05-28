<template>
    <div>
        <div class="tabs">
            <router-link v-for="tab in tabs" :key="tab.name" :to="{ name: tab.name }" class="tab-button"
                active-class="active">
                {{ tab.label }}
            </router-link>
        </div>
        <div class="tab-content">
            <slot />
        </div>
    </div>
</template>

<script setup lang="ts">
const props = defineProps<{
    tabs: Array<{ name: string; label: string }>;
}>();
</script>

<style lang="scss" scoped>
.tabs {
    display: flex;
    margin-bottom: 20px;
}

.tab-button {
    padding: 10px;
    margin-right: 10px;
    cursor: pointer;
    border: none;
    background-color: #f0f0f0;
    border-radius: 5px;
    text-decoration: underline;
    color: black;
    font-weight: bold;
}

.tab-button.active {
    background-color: #007bff;
    color: white;
}
</style>