<template>
    <div class="input-box">
        <el-input class="search" v-model="input" size="large" placeholder="输入您的需求" @keydown.enter="sendWords">
            <template #prefix>
                <Upload @uploadSuccess="handleUploadSuccess" :biz="'parse_files'"></Upload>
            </template>
            <template #suffix>
                <div class="send" @click="sendWords" @mouseenter="setHover(true)" @mouseleave="setHover(false)">
                    <!-- <icon-send class="inputIcon" :icon-color="iconColor"></icon-send> -->
                    <img src="../../assets/icon/芒果按钮.png" alt="" class="inputIcon">

                </div>
            </template>
        </el-input>
    </div>
</template>

<script setup lang="ts">
// import iconSend from '../icons/create/icon-send.vue';
import { ref, computed } from 'vue';
import { ElMessage } from 'element-plus';
import Upload from '../Upload.vue';

const emit = defineEmits(['send-message']);
const input = ref('');
const parsedIdeas = ref('');
const sendWords = async () => {
    console.log('点击');

    if (input.value.trim() === '') {
        ElMessage.error('请先输入内容！');
        return;
    }
    let message = parsedIdeas.value + input.value
    input.value = ''; // 清空输入框
    emit('send-message', message);
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

const handleUploadSuccess = (data: any) => {
    // tranferIdeas(data)
    parsedIdeas.value = '主题：' + data.theme + '。描述：' + data.description + '。'
};



</script>

<style lang="scss" scoped>
.search {
    height: 50px;

    .inputIcon {
        width: 1.5rem;
        height: 1.5rem;
        margin: 5px;
        cursor: pointer;
    }

    .send {
        display: flex;
        align-items: center;
        background-color: #eee;
        border-radius: 5px;
    }

    .send:hover {
        transition: 0.8s;
        background-color: rgb(255, 184, 71);
    }
}

.files {
    position: relative;

    img {
        position: absolute;
        margin-top: 10px;
        height: 80px;
        transition: filter 0.3s ease;
        cursor: pointer;
    }

    .blurred {
        filter: blur(2px);
    }

    .delete-hint {
        position: absolute;
        left: 40px;
        transform: translate(-50%, -50%);
        background-color: rgba(0, 0, 0, 0.7);
        color: white;
        padding: 5px;
        border-radius: 3px;
        font-size: 0.8rem;
        z-index: 1;
    }
}
</style>
