<template>
    <!-- <div class="find-item" @click="showNotOpenTip"> -->
    <div class="find-item" @click="handleClick(item.path)">
        <div class="item-info">

            <h3 class="item-title">
                <img class="icon-star" src="../../assets/icon/创意星.png" alt="">
                {{ item.title }}
            </h3>
            <p class="item-description">{{ item.description }}</p>
        </div>
        <img :src="item.image" alt="Item Image" class="item-image" />
    </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
const props = defineProps<{
    item: {
        id: number;
        image: string;
        title: string;
        description: string;
        path: string;
    };
}>();

const router = useRouter();

const handleClick = (path: string) => {
    router.push(path);
}
const goToDetail = (id: number) => {
    showNotOpenTip
};
const showNotOpenTip = () => {
    ElMessage({
        message: '暂未开放，敬请期待',
        type: 'warning'
    });
};
</script>

<style lang="scss" scoped>
.find-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin: 5px;
    width: 31%;
    /* 设置最大宽度 */
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    overflow: hidden;
    cursor: pointer;
    background: linear-gradient(137.4deg, rgba(156, 202, 255, 0.15) 4.85%, rgba(218, 206, 255, 0.15) 92.08%);

    &:hover {
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); // 增加阴影效果
        transform: scale(1.02); // 轻微放大效果
        transition: transform 0.2s, box-shadow 0.2s; // 添加过渡效果
    }

    .item-image {
        width: 170px;
        height: 160px;
        /* 固定高度 */
        object-fit: cover;
        /* 保持图像比例 */
    }

    .item-info {
        padding: 10px;

        .item-title {
            font-size: 1.2rem;
            font-weight: bold;
            display: flex;
            justify-content: center;

            .icon-star {
                width: 24px;
            }
        }

        .item-description {
            font-size: 0.9rem;
            color: #666;
        }
    }
}
</style>