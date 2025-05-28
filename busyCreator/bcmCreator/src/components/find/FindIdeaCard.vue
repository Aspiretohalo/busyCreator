<template>
    <!-- <div class="find-item" @click="showNotOpenTip"> -->
    <div class="find-item">
        <div class="item-info">
            <h3 class="item-title">
                {{ item?.title }}
            </h3>
            <p class="item-description">{{ item?.description }}</p>
        </div>
        <div class="btn">{{ item?.buttonText }}</div>
    </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
const props = defineProps<{
    item: {
        id: number;
        buttonText: string;
        title: string;
        description: string;
    };
}>();

const router = useRouter();

const goToDetail = (id: number) => {
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
    align-items: center;
    justify-content: space-between;
    margin: 5px;
    padding: 0 10px;
    height: 47%;
    /* 设置最大宽度 */
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    overflow: hidden;
    background: linear-gradient(100.36deg, rgba(250, 222, 179, 0.15) 3.34%, rgba(156, 224, 239, 0.15) 98.63%);

    .item-info {
        padding: 10px;

        .item-title {
            font-size: 1.2rem;
            font-weight: bold;
            display: flex;
            justify-content: flex-start;
        }

        .item-description {
            font-size: 0.9rem;
            color: #666;
        }
    }

    .btn {
        display: flex;
        justify-content: center;
        align-items: center;
        width: 80px;
        min-width: 80px;
        height: 36px;
        color: #fff;
        cursor: pointer;
        border-radius: 8px;
        background: linear-gradient(96.34deg, rgb(120, 90, 255) 0%, rgb(78, 110, 242) 100%);

        &:hover {
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); // 增加阴影效果
            transform: scale(1.02); // 轻微放大效果
            transition: transform 0.2s, box-shadow 0.2s; // 添加过渡效果
        }
    }

}
</style>