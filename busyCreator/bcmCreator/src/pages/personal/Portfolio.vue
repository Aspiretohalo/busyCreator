<template>
    <div>
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
const portfolioItems = [
    {
        title: '作品 1',
        description: '这是作品 1 的描述。',
        image: 'https://example.com/image1.jpg',
    },
    {
        title: '作品 2',
        description: '这是作品 2 的描述。',
        image: 'https://example.com/image2.jpg',
    },
    // 添加更多作品...
];
</script>

<style lang="scss" scoped>
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