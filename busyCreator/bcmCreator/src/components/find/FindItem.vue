<template>
    <div class="find-item" @click="goToDetail(item.id)">
        <img :src="item.cover" alt="Item Image" class="item-image" />
        <div class="item-info">
            <h3 class="item-title">{{ item.name }}</h3>
            <p class="item-description">{{ item.profile }}</p>
            <div class="item-collects">
                <span class="like-icon">
                    <icon-thumb class="icon" icon-color="#666" />
                    <span class="count">{{ item.thumbCount }}</span>
                </span>
                <span class="collect-icon">
                    <icon-bookmark class="icon" icon-color="#666" />
                    <span class="count">{{ item.bookmarkCount }}</span>
                </span>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router';
import iconThumb from '../icons/topNav/icon-thumb.vue';
import iconBookmark from '../icons/topNav/icon-bookmark.vue';
const props = defineProps<{
    item: {
        id: number;
        cover: string;
        name: string;
        profile: string;
        thumbCount: number;
        bookmarkCount: number;
    };
}>();

const router = useRouter();

const goToDetail = (id: number) => {
    router.push({ name: 'worksDetail', params: { id } }); // 跳转到项目详情页
};

</script>

<style lang="scss" scoped>
.find-item {
    display: flex;
    flex-direction: column;
    margin: 5px;
    width: 100%;
    /* 让每个项目占满一行 */
    max-width: 260px;
    /* 设置最大宽度 */
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
    overflow: hidden;
    cursor: pointer;

    &:hover {
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); // 增加阴影效果
        transform: scale(1.02); // 轻微放大效果
        transition: transform 0.2s, box-shadow 0.2s; // 添加过渡效果
    }
}

.item-image {
    width: 100%;
    height: 150px;
    /* 固定高度 */
    object-fit: cover;
    /* 保持图像比例 */
}

.item-info {
    padding: 10px;
}

.item-title {
    font-size: 1.2rem;
    font-weight: bold;
    max-height: 24px;
    /* 设置最大高度，确保只显示一行 */
    overflow: hidden;
    /* 隐藏溢出部分 */
    text-overflow: ellipsis;
    /* 溢出部分用省略号表示 */
    white-space: nowrap;
    /* 不换行 */
}

.item-description {
    font-size: 0.9rem;
    color: #666;
    max-height: 80px;
    /* 设置最大高度 */
    overflow: hidden;
    /* 隐藏溢出部分 */
    text-overflow: ellipsis;
    /* 溢出部分用省略号表示 */
    display: -webkit-box;
    /* 使其成为多行文本 */
    -webkit-box-orient: vertical;
    /* 垂直排列 */
    -webkit-line-clamp: 4;
    /* 限制为四行 */
}

.item-collects {
    font-size: 0.8rem;
    color: #999;
    display: flex;
    align-items: center;
    /* 垂直居中对齐 */
    gap: 5px;
    /* 图标与数字之间的间距 */
}

.like-icon,
.collect-icon {
    display: flex;
    align-items: center;
    /* 垂直居中对齐 */

    .icon {
        width: 1rem;
        height: 1rem;
    }

    .count {
        color: #666;
        margin-left: 5px;
    }
}
</style>