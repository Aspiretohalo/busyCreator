<template>
    <div class="notice-container">
        <div v-if="notices.length === 0" class="empty-notice">
            <el-empty description="暂无通知消息" />
        </div>
        <div v-else class="notice-list">
            <div v-for="(notice, index) in notices" :key="index" class="notice-item"
                :class="{ 'unread': !notice.read }">
                <div class="notice-title">
                    <el-tag size="small" :type="getTagType(notice.type)">{{ notice.type }}</el-tag>
                    <span class="notice-date">{{ formatDate(notice.date) }}</span>
                </div>
                <div class="notice-content">{{ notice.content }}</div>
                <div class="notice-actions">
                    <el-button type="text" size="small" @click="markAsRead(index)" v-if="!notice.read">标为已读</el-button>
                    <el-button type="text" size="small" @click="deleteNotice(index)">删除</el-button>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { ElEmpty, ElTag, ElButton } from 'element-plus';

// 通知类型定义
interface Notice {
    type: string;
    content: string;
    date: Date;
    read: boolean;
}

// 通知列表
const notices = ref<Notice[]>([]);

// 添加测试数据
const addTestNotices = () => {
    notices.value = [
        {
            type: '审核通过',
            content: '您的作品《时光之门：穿越时空的爱恋》已审核通过，现已发布上线。',
            date: new Date(Date.now() - 1000 * 60 * 60 * 2), // 2小时前
            read: false
        },
        {
            type: '审核未通过',
            content: '您的作品《隐秘的小镇：谜案重重》审核未通过，原因：含有不适宜内容，请修改后重新提交。',
            date: new Date(Date.now() - 1000 * 60 * 60 * 24), // 1天前
            read: true
        },
        {
            type: '审核通过',
            content: '您的作品《星际迷航：未知的宇宙之旅》已有超过 100 次阅读，继续保持！',
            date: new Date(Date.now() - 1000 * 60 * 60 * 24 * 3), // 3天前
            read: true
        }
    ];
};

// 添加新通知
const addNotice = (notice: Notice) => {
    notices.value.unshift(notice);
};

// 标记为已读
const markAsRead = (index: number) => {
    notices.value[index].read = true;
};

// 删除通知
const deleteNotice = (index: number) => {
    notices.value.splice(index, 1);
};

// 清空所有通知
const clearAllNotices = () => {
    notices.value = [];
};

// 获取未读通知数量
const getUnreadCount = () => {
    return notices.value.filter(notice => !notice.read).length;
};

// 根据通知类型获取标签类型
const getTagType = (type: string) => {
    switch (type) {
        case '审核通过':
            return 'success';
        case '审核未通过':
            return 'danger';
        case '系统通知':
            return 'info';
        default:
            return 'info';
    }
};

// 格式化日期
const formatDate = (date: Date) => {
    const now = new Date();
    const diff = now.getTime() - date.getTime();

    // 一分钟内
    if (diff < 60 * 1000) {
        return '刚刚';
    }

    // 一小时内
    if (diff < 60 * 60 * 1000) {
        return `${Math.floor(diff / (60 * 1000))}分钟前`;
    }

    // 一天内
    if (diff < 24 * 60 * 60 * 1000) {
        return `${Math.floor(diff / (60 * 60 * 1000))}小时前`;
    }

    // 一周内
    if (diff < 7 * 24 * 60 * 60 * 1000) {
        return `${Math.floor(diff / (24 * 60 * 60 * 1000))}天前`;
    }

    // 其他情况显示日期
    return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`;
};

// 暴露方法给父组件
defineExpose({
    addNotice,
    addTestNotices,
    markAsRead,
    deleteNotice,
    clearAllNotices,
    getUnreadCount
});
</script>

<style lang="scss" scoped>
.notice-container {
    width: 100%;
    max-height: 400px;
    overflow-y: auto;
    padding: 10px 0;
}

.empty-notice {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 150px;
}

.notice-list {
    .notice-item {
        padding: 12px 16px;
        border-bottom: 1px solid #f0f0f0;
        transition: background-color 0.3s;

        &:hover {
            background-color: #f5f7fa;
        }

        &.unread {
            position: relative;
            background-color: #ecf5ff;

            &::before {
                content: '';
                position: absolute;
                left: 0;
                top: 0;
                bottom: 0;
                width: 3px;
                background-color: #409EFF;
            }
        }

        &:last-child {
            border-bottom: none;
        }

        .notice-title {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 8px;

            .notice-date {
                font-size: 12px;
                color: #909399;
            }
        }

        .notice-content {
            font-size: 14px;
            color: #303133;
            line-height: 1.5;
            margin-bottom: 8px;
        }

        .notice-actions {
            display: flex;
            justify-content: flex-start;
            /* 修改这里 */
            align-items: center;
            /* 添加对齐方式 */
        }
    }
}

// 自定义滚动条
.notice-container {
    &::-webkit-scrollbar {
        width: 6px;
        background-color: transparent;
    }

    &::-webkit-scrollbar-thumb {
        background-color: #c0c4cc;
        border-radius: 3px;
    }

    &::-webkit-scrollbar-track {
        background-color: transparent;
    }
}
</style>
