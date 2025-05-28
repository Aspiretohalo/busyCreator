<template>
    <div class="topNav" :style="{ backgroundColor: bgColor }">
        <div class="middle">
            <img class="logo" src="@/assets/logo/logo_main.png" @click="goToIndex()">

            <div class="search-wrapper">
                <el-input class="search" v-model="input" style="width: 36rem" size="large" placeholder="输入作品名称"
                    v-if="route.path != '/login' && route.path != '/register'" @focus="handleFocus"
                    @input="handleInputChange">
                    <template #append>
                        <el-button :icon="Search" @click="handleSearch()" />
                    </template>
                </el-input>
                <div v-if="showResults" class="search-results">
                    <template v-if="searchResults.length > 0">
                        <div v-for="item in searchResults" :key="item.id" class="search-item"
                            @click="goToDetail(item.id)">
                            <img v-if="item.cover" :src="item.cover" alt="封面" class="cover-image" />
                            <div class="item-info">
                                <div class="item-name">{{ item.name }}</div>
                                <div class="item-profile">{{ item.profile }}</div>
                            </div>
                            <div class="item-team">{{ item.teamInfo?.name }}</div>
                        </div>
                    </template>
                    <template v-else>
                        <el-empty description="没有找到相关内容" />
                    </template>
                </div>
            </div>
            <div class="login" v-if="token == null && route.path != '/login' && route.path != '/register'">
                <el-link type="info" :underline="false" style="border-right: 1px solid;padding-right: 3px;"
                    @click="goToLogin()">登录</el-link>
                <el-link type="info" :underline="false" style="padding-left: 3px;" @click="goToRegister()">注册</el-link>
            </div>

            <div class="user" v-else-if="token != null && route.path != '/login' && route.path != '/register'">
                <el-popover placement="bottom" :width="350" trigger="manual" v-model:visible="noticePopoverVisible"
                    popper-class="notice-popover" :stop-popper-mouse-event="true">
                    <template #reference>
                        <div class="wrapper" @mouseenter="setHover(true)" @mouseleave="setHover(false)"
                            @click.stop="toggleNoticePopover">
                            <icon-notice :icon-color="iconColor" class="icon" />
                            <div v-if="unreadNoticeCount > 0" class="notice-badge">{{ unreadNoticeCount }}</div>
                        </div>
                    </template>
                    <div class="notice-popover-title">
                        <div class="title-text">通知消息</div>
                        <div class="title-actions">
                            <el-button type="text" @click="refreshNotices">刷新</el-button>
                            <el-button type="text" @click="clearNotices">清空</el-button>
                        </div>
                    </div>
                    <Notice ref="noticeRef" />
                </el-popover>
                <div class="vipBadge">
                    <img v-if="user?.vipId" src="../assets/vip/vip.png" alt="">
                    <img v-else src="../assets/vip/common.png" alt="">
                </div>
                <el-popover placement="bottom-end" :width="300" trigger="click">
                    <template #reference>
                        <el-link type="info" :underline="false" style="font-size: 16px;">
                            <img class="avatar" :src="user?.userAvatar"></el-link>
                    </template>
                    <div class="msg">
                        <el-avatar :size="32" :src="user?.userAvatar" />
                        <h2 style="margin-left: 20px;">{{ user?.userName }}</h2>
                    </div>

                    <van-cell title="个人主页" to="/personal" is-link>
                        <template #icon>
                            <img class="img_icon mr" src="../assets/icon/个人.svg">
                        </template>
                    </van-cell>
                    <van-cell title="退出登录" is-link @click="logout()">
                        <template #icon>
                            <img class="img_icon mr" src="../assets/icon/退出.svg">
                        </template>
                    </van-cell>
                </el-popover>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Search } from '@element-plus/icons-vue'
import iconNotice from './icons/topNav/icon-notice.vue';
import getUserData from '../requests/user/getUserData';
import searchWorks from '../requests/project/searchWorks';
import { ElEmpty, ElMessage } from 'element-plus';
import Notice from './Notice.vue';

const input = ref('')
const route = useRoute();
const router = useRouter();

const token: any = ref(localStorage.getItem('token') || null)
const user: any = ref()
const showResults = ref(false);
const searchResults = ref<any[]>([]);
const noticeRef = ref<any>(null);
const noticePopoverVisible = ref(false);
const unreadNoticeCount = ref(0);

onMounted(async () => {
    if (token.value !== null)
        user.value = await getUserData()
    window.addEventListener('scroll', handleScroll);
    window.addEventListener('click', handleClickOutside); // 监听点击事件

    // 初始化通知
    setTimeout(() => {
        if (noticeRef.value) {
            noticeRef.value.addTestNotices();
            updateUnreadCount();
        }
    }, 500);
});

onBeforeUnmount(() => {
    window.removeEventListener('scroll', handleScroll);
    window.removeEventListener('click', handleClickOutside); // 移除点击事件监听
});

// 添加防抖函数
const debounce = (fn: Function, delay: number) => {
    let timer: NodeJS.Timeout | null = null;
    return (...args: any[]) => {
        if (timer) clearTimeout(timer);
        timer = setTimeout(() => {
            fn.apply(null, args);
        }, delay);
    };
};

// 处理输入变化的函数
const handleInputChange = debounce(async () => {
    if (input.value.trim() === '') {
        searchResults.value = [];
        showResults.value = true;
        return;
    }
    await handleSearch();
}, 300); // 300ms 的防抖延迟

const handleSearch = async () => {
    searchResults.value = await searchWorks(input.value);
    showResults.value = true;
}
const goToDetail = (id: number) => {
    router.push({ name: 'worksDetail', params: { id } }); // 跳转到项目详情页
};
const goToIndex = () => {
    router.push('/')
}

const logout = () => {
    localStorage.clear()
    sessionStorage.clear()
    router.push('/login')
}

const goToLogin = () => {
    router.push('/login')
}

const goToRegister = () => {
    router.push('/login')
}

const handleFocus = () => {
    if (input.value === '')
        searchResults.value = []
    showResults.value = true; // 输入框获得焦点时显示结果
};

const handleClickOutside = (event: MouseEvent) => {
    const target = event.target as HTMLElement;

    // 搜索结果处理
    if (!target.closest('.search') && !target.closest('.search-results')) {
        showResults.value = false; // 点击外部时隐藏搜索结果
    }

    // 通知框处理 - 如果点击的不是通知按钮且不是通知相关元素，则关闭通知框
    if (noticePopoverVisible.value &&
        !target.closest('.wrapper') &&
        !target.closest('.el-popover') &&
        !target.closest('.notice-popover')) {
        noticePopoverVisible.value = false;
    }
};

const handleScroll = () => {
    const scrollY = window.scrollY;
    // 根据滚动位置设置背景颜色
    bgColor.value = scrollY > 50 ? 'white' : 'transparent';
};

// 创建一个响应式变量来跟踪鼠标悬停状态
const isHover = ref(false);

// 计算属性根据悬停状态返回颜色
const iconColor = computed(() => {
    return isHover.value ? 'white' : '';
});

// 更新鼠标悬停状态的函数
function setHover(status: boolean) {
    isHover.value = status;
}

const bgColor = ref('transparent'); // 初始化背景颜色为透明

// 添加通知相关方法
const toggleNoticePopover = (event?: MouseEvent) => {
    // 直接切换可见性状态
    if (event) {
        event.stopPropagation(); // 阻止事件冒泡
    }

    noticePopoverVisible.value = !noticePopoverVisible.value;
    if (noticePopoverVisible.value) {
        updateUnreadCount();
    }
};

const updateUnreadCount = () => {
    if (noticeRef.value) {
        unreadNoticeCount.value = noticeRef.value.getUnreadCount();
    }
};

const refreshNotices = async () => {
    // 实际应用中，这里应该调用API获取最新通知
    ElMessage.success('通知已更新');
    updateUnreadCount();
};

const clearNotices = () => {
    if (noticeRef.value) {
        noticeRef.value.clearAllNotices();
        unreadNoticeCount.value = 0;
    }
};

</script>

<style lang="scss" scoped>
:deep(.el-menu--horizontal>.el-menu-item) {
    font-size: 14px;
}

.img_icon {
    width: 20px;
}

.mr {
    margin-right: 10px;
}

.msg {
    display: flex;
    justify-content: flex-start;
}

.topNav {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 60px;
    width: 100vw;
    border-bottom: 1px solid rgb(222, 222, 222);
    transition: background-color 0.3s; // 添加过渡效果
    position: fixed;
    z-index: 1000;

    .logo {
        height: 58px;
        cursor: pointer;
    }

    :deep(.el-menu-item) {
        padding: 0 10px;
    }

    :deep(.el-menu--horizontal) {
        border-bottom: 0;
    }

    .user {
        display: flex;

        .wrapper {
            height: 2.5rem;
            width: 2.5rem;
            border-radius: 50%;
            background-color: #fff;
            display: flex;
            justify-content: center;
            align-items: center;
            margin-right: 1rem;
            cursor: pointer;
            position: relative;

            .icon {
                width: 1.5rem;
                height: 1.5rem;
            }

            .notice-badge {
                position: absolute;
                top: -5px;
                right: -5px;
                min-width: 16px;
                height: 16px;
                padding: 0 4px;
                border-radius: 8px;
                background-color: #f56c6c;
                color: white;
                font-size: 12px;
                line-height: 16px;
                text-align: center;
            }
        }

        .wrapper:hover {
            transition: 0.8s;
            background-color: #666;
        }

        .vipBadge {
            margin-right: 1rem;
            cursor: pointer;

            img {
                height: 2.5rem;
                width: 2.5rem;
            }

        }

        .avatar {
            height: 40px;
            width: 40px;
            border-radius: 50%;
        }
    }

    .middle {
        display: flex;
        align-items: center;
        padding: 0 20px;
    }

    .search-wrapper {
        position: relative;
        margin-left: 20px; // 调整与logo的间距
    }

    .search-results {
        width: 36rem; // 与输入框宽度一致
        min-height: 16rem;
        border: 1px solid #ccc;
        background-color: white;
        position: absolute;
        left: 0; // 与输入框左对齐
        top: 100%; // 紧贴输入框底部
        z-index: 1000;
        border-radius: 8px;
        margin-top: 2px; // 添加一点间距
        box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    }
}

:hover :deep(.van-cell) {
    background-color: #eee;
}

.search-item {
    display: flex;
    align-items: center;
    padding: 10px;
    cursor: pointer;

    .cover-image {
        width: 50px;
        /* 设置封面图的宽度 */
        height: 50px;
        /* 设置封面图的高度 */
        object-fit: cover;
        /* 保持图像比例 */
        border-radius: 4px;
        /* 圆角 */
    }

    .item-info {
        margin-left: 10px;

        /* 设置信息的左边距 */
        .item-name {
            font-weight: bold;
            /* 加粗名称 */
            white-space: nowrap;
            /* 不换行 */
            overflow: hidden;
            /* 超出部分隐藏 */
            text-overflow: ellipsis;
            /* 溢出部分用省略号表示 */
            max-width: 300px;
            /* 设置最大宽度 */
        }

        .item-profile {
            color: #868686;
            white-space: nowrap;
            /* 不换行 */
            overflow: hidden;
            /* 超出部分隐藏 */
            text-overflow: ellipsis;
            /* 溢出部分用省略号表示 */
            max-width: 400px;
            /* 设置最大宽度 */
        }
    }

    .item-team {
        color: #666;
        /* 设置团队名称的颜色 */
        white-space: nowrap;
        /* 不换行 */
        overflow: hidden;
        /* 超出部分隐藏 */
        text-overflow: ellipsis;
        /* 溢出部分用省略号表示 */
        max-width: 150px;
        /* 设置最大宽度 */
    }
}

.search-item:hover {
    background-color: #f0f0f0;
}

.notice-popover-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 0 10px 0;
    border-bottom: 1px solid #ebeef5;
    margin-bottom: 10px;

    .title-text {
        font-weight: bold;
        font-size: 16px;
        color: #303133;
    }

    .title-actions {
        display: flex;
        gap: 10px;
    }
}

// 添加全局样式，确保通知弹出框样式正确
:deep(.notice-popover) {
    padding: 15px !important;
}
</style>