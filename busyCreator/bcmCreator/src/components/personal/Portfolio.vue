<template>
    <div class="portfolio">
        <h2>作品集</h2>
        <div class="portfolio-items">
            <div v-for="item in portfolioItems" :key="item.title" class="portfolio-item">
                <img :src="item.image" alt="作品图" class="portfolio-image" />
                <h3>{{ item.title }}</h3>
                <p>{{ item.description }}</p>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
const props = defineProps<{
    portfolioItems: Array<{
        title: string;
        description: string;
        image: string;
    }>;
}>();
</script>

<style lang="scss" scoped>
.portfolio {
    margin-bottom: 20px;
}

.portfolio-items {
    display: flex;
    flex-wrap: wrap;
}

.portfolio-item {
    width: calc(33.33% - 20px);
    margin: 10px;
}

.portfolio-image {
    width: 100%;
    height: auto;
    border-radius: 8px;
}
</style>