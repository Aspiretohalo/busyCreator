<template>
    <div class="box">
        <img class="avatar" src="https://files-1317662942.cos.ap-nanjing.myqcloud.com/fruit/mango.png" alt="">
        <div class="bubble" v-loading="isTyping && !content">
            <div class="debug" style="font-size: 12px; color: #666;">
                isTyping: {{ isTyping }}, length: {{ content.length }}
            </div>
            <v-md-editor v-model="currentContent" mode="preview" :disabled="true"></v-md-editor>
            <div class="function-btn">
                <el-button type="primary" plain @click="dialogVisible = true; loadData()">应用</el-button>
            </div>
        </div>
        <el-dialog v-model="dialogVisible" title="选择项目" width="500">
            <el-form :model="projectData">
                <el-form-item label="项目">
                    <el-select-v2 v-model="projectData.id" placeholder="项目" :options="options" />
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="dialogVisible = false">取消</el-button>
                    <el-button type="primary" @click="handleApply();">确认</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup lang="ts">
import { reactive, ref, watch, computed } from 'vue';
import listProjects from '../../requests/project/listProjects';
import '@kangc/v-md-editor/lib/style/base-editor.css';

const props = defineProps<{
    content: string;
    isTyping?: boolean;  // true 表示当前正在输入的回答，false 表示历史记录
}>();

const displayContent = ref('');
const currentIndex = ref(0);
const typewriterSpeed = 50;

// 打字机效果函数
const typeWriter = () => {
    if (currentIndex.value < props.content.length) {
        displayContent.value = props.content.slice(0, currentIndex.value + 1);
        currentIndex.value++;
        setTimeout(typeWriter, typewriterSpeed);
    }
};

const dialogVisible = ref(false);
const projectData = reactive({
    id: '1'
});
const options = ref([]);

const loadData = async () => {
    const project = await listProjects(); // 获取已加入团队的项目
    options.value = project.map(record => ({
        value: record.id,
        label: record.name // 如果没有备注则显示'无备注'
    }));
};

const handleApply = () => {
    // 选择项目，并新建项目记录
};

// 只有当 isTyping 为 true 时才使用打字机效果
watch(() => props.content, (newContent) => {
    if (!props.isTyping) {
        // 历史记录直接显示完整内容
        displayContent.value = newContent;
        return;
    }

    // 当前回答使用打字机效果
    const newLength = newContent.length;
    const currentLength = displayContent.value.length;

    if (newLength > currentLength) {
        // 有新内容时，从当前位置继续打字
        currentIndex.value = currentLength;
        typeWriter();
    }
}, { immediate: true });

// 计算属性，动态绑定 v-model
const currentContent = computed({
    get: () => props.isTyping ? displayContent.value : props.content,
    set: (value) => {
        if (props.isTyping) {
            displayContent.value = value;
        } else {
            // 如果不是打字机效果，直接更新 content
            // 注意：这里可能需要根据实际需求调整逻辑
            props.content = value;
        }
    }
});
</script>

<style lang="scss" scoped>
.box {
    display: flex;
    justify-content: left;
    width: 100%;
    /* 确保父容器宽度足够 */
}

.bubble {
    width: 83%;
    /* 设置为100% */
    background-color: #fff;
    border-radius: 8px;
    padding: 10px;
    word-wrap: break-word;

    :deep(.v-md-editor) {
        border: none;
        width: 100%;
        /* 设置宽度为100% */
        min-width: 300px;
        /* 设置最小宽度 */
        height: auto;
        /* 确保高度自适应 */
    }
}

.avatar {
    height: 3rem;
    width: 3rem;
    margin-right: 10px;
}

.function-btn {
    display: flex;
    justify-content: flex-start;
    margin-top: 10px;
}
</style>