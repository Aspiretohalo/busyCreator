<template>
    <div class="topNav">
        <div class="middle">
            <div class="left">
                <div class="backBtn wrapper" @click="goToBack()" @mouseenter="setHover('back', true)"
                    @mouseleave="setHover('back', false)">
                    <icon-back :icon-color="iconColor.back" class="icon" />
                </div>
                <div class="author">
                    <img class="avatar" :src="teamInfo?.avatar" alt="">
                    <span class="authorName">{{ teamInfo?.name }}</span>
                </div>
            </div>

            <div class="right" v-if="fromPage === '/'">
                <div class="rightIcon wrapper" :class="{ highlight: thumbStatus?.isThumbed }"
                    @mouseenter="setHover('thumb', true)" @mouseleave="setHover('thumb', false)"
                    @click="handleThumb('thumb')">
                    <icon-thumb :icon-color="iconColor.thumb" class="icon" />
                    <span :style="{ color: iconColor.thumb }" class="count">{{ thumbStatus?.thumbCount }}</span>
                </div>
                <div class="rightIcon wrapper" :class="{ highlight: thumbStatus?.isBookmarked }"
                    @mouseenter="setHover('bookmark', true)" @mouseleave="setHover('bookmark', false)"
                    @click="handleThumb('collect')">
                    <icon-bookmark :icon-color="iconColor.bookmark" class="icon" />
                    <span :style="{ color: iconColor.bookmark }" class="count">{{ thumbStatus?.bookmarkCount }}</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import iconBack from './icons/topNav/icon-back.vue';
import iconThumb from './icons/topNav/icon-thumb.vue';
import iconBookmark from './icons/topNav/icon-bookmark.vue';
import handleThumbAndCollect from '../requests/project/handleThumbAndCollect';
import { ElMessage } from 'element-plus';

const props = defineProps<{
    teamInfo?: {
        id: string;
        name: string;
        avatar: string;
        profile: string;
        adminId: string;
        memberList: string;
    } | null;
    fromPage: string;
    projectId?: number;
    thumbStatus?: {
        thumbCount: number;
        bookmarkCount: number;
        isThumbed: boolean;
        isBookmarked: boolean;
    } | null;
}>();
const router = useRouter();

// 添加 emit 定义
const emit = defineEmits(['update-thumb-status']);

const goToBack = () => {
    if (props.fromPage) {
        router.push(props.fromPage);
    } else {
        router.back();
    }
}

// 创建独立的悬停状态
const isHover = {
    back: ref(false),
    thumb: ref(false),
    bookmark: ref(false),
};

// 计算属性根据悬停状态和点赞/收藏状态返回颜色
const iconColor = computed(() => {
    return {
        back: isHover.back.value ? 'white' : '',
        thumb: isHover.thumb.value || props.thumbStatus?.isThumbed ? 'white' : '',
        bookmark: isHover.bookmark.value || props.thumbStatus?.isBookmarked ? 'white' : '',
    };
});

// 更新鼠标悬停状态的函数
function setHover(button: string, status: boolean) {
    isHover[button].value = status;
}

// 分别为点赞和收藏添加最后操作时间记录
const lastThumbTime = ref(0);
const lastBookmarkTime = ref(0);
const THROTTLE_TIME = 2000; // 2秒节流时间

const handleThumb = async (type: string) => {
    if (!props.projectId) {
        console.warn('No projectId provided');
        return;
    }

    const now = Date.now();
    const lastTime = type === 'thumb' ? lastThumbTime : lastBookmarkTime;
    const isLiked = type === 'thumb' ? props.thumbStatus?.isThumbed : props.thumbStatus?.isBookmarked;

    // 如果是取消操作，允许执行
    // 如果是点赞操作，检查节流时间
    if (!isLiked && now - lastTime.value < THROTTLE_TIME) {
        ElMessage({
            message: '操作太频繁，请稍后再试',
            type: 'warning',
            duration: 2000
        });
        return;
    }

    try {
        await handleThumbAndCollect({
            projectId: props.projectId,
            type
        });

        // 更新最后操作时间（仅在点赞操作时更新）
        if (!isLiked) {
            if (type === 'thumb') {
                lastThumbTime.value = now;
            } else {
                lastBookmarkTime.value = now;
            }
        }

        // 触发更新事件
        emit('update-thumb-status');
    } catch (error) {
        ElMessage({
            message: '操作失败，请重试',
            type: 'error',
            duration: 2000
        });
    }
}
</script>

<style lang="scss" scoped>
.wrapper {
    width: 2.8rem;
    height: 2.8rem;
    border-radius: 8px;
    background-color: #eee;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-right: 1rem;
    cursor: pointer;
    padding: 0 0.5rem;

    .icon {
        width: 1.5rem;
        height: 1.5rem;
    }
}

.wrapper:hover {
    transition: 0.8s;
    background-color: #666;
}

// 新增高亮样式
.highlight {
    background-color: #666;
    color: white;
}

.topNav {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 80px;
    width: 100vw;
    border-bottom: 1px solid rgb(222, 222, 222);
    background-color: #fff;
    position: fixed;
    z-index: 1000;

    .middle {
        display: flex;
        align-items: center;
        flex-grow: 1;
        padding: 0 1rem;

        .left {
            display: flex;
            align-items: center;

            .author {
                display: flex;
                align-items: center;

                .avatar {
                    width: 2.8rem;
                    height: 2.8rem;
                    border-radius: 50%;
                    overflow: hidden;
                }

                .authorName {
                    margin-left: 1rem;
                }
            }
        }

        .right {
            display: flex;
            align-items: center;

            .rightIcon {
                position: relative;
                margin: 1rem;
            }
        }
    }
}

.team-info {
    margin-top: 10px;
    text-align: center;
}

// 可以添加一个禁用状态的样式
.wrapper.disabled {
    opacity: 0.6;
    cursor: not-allowed;
}
</style>