<template>
    <div class="box">
        <div class="historySession">
            <el-button class="historyBtn" @click="gotoHistoryPage">
                <icon-history class="icon" icon-color=""></icon-history>
                历史会话
            </el-button>
            <el-button class="historyBtn" @click="gotoCreatePage">
                <icon-history class="icon" icon-color=""></icon-history>
                新建会话
            </el-button>
        </div>
        <div class="mainBox">
            <div class="chatWindow busy-scrollbar" ref="chatWindowRef">
                <!-- 调试信息 -->
                <div v-for="(item, index) in chatData" :key="index">
                    <!-- 显示问题 -->
                    <chat-bubble :content="item.question.questionText" />
                    <!-- 显示历史回答 -->
                    <chat-bubble-reply v-for="answer in item.answers" :key="answer.id" :content="answer.answerText"
                        :is-typing="false" />
                </div>
                <!-- 显示正在输入的回答 -->
                <chat-bubble-reply v-if="isStreaming" :content="streamingContent" :is-typing="true" />

                <!-- 提示选项区域 - 只在非生成状态显示 -->
                <div v-if="!isStreaming && suggestionOptions.length > 0" class="suggestion-options">
                    <div class="suggestion-label">您可能想问：</div>
                    <div class="suggestion-list">
                        <div v-for="(option, index) in suggestionOptions" :key="index" class="suggestion-item"
                            @click="handleSuggestionClick(option)">
                            {{ option }}
                        </div>
                    </div>
                </div>
            </div>

            <div class="mainInput">
                <SendInput @send-message="handleMessage" />
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import iconHistory from '../../components/icons/create/icon-history.vue';
import SendInput from '../../components/create/SendInput.vue';
import ChatBubble from '../../components/create/ChatBubble.vue';
import ChatBubbleReply from '../../components/create/ChatBubbleReply.vue';
import getQAbySessionId from '../../requests/create/getQAbySessionId';
import sendQuestion from '../../requests/create/sendQuestion';
import { useRouter, useRoute } from 'vue-router';
import { onMounted, ref, nextTick, watch } from 'vue';
import { useStore } from 'vuex'; // 引入 Vuex store

// 添加类型定义
interface Answer {
    answerText: string;
    id: number;
}

interface ChatItem {
    question: {
        questionText: string;
    };
    answers: Answer[];
}

const store = useStore(); // 获取 Vuex store
const route = useRoute();
const router = useRouter();
const chatWindowRef = ref<HTMLElement | null>(null);
const chatData = ref<ChatItem[]>([]);
const isStreaming = ref(false);
const streamingContent = ref('');
const suggestionOptions = ref<string[]>([]);
const responseComplete = ref(false); // 用于标记响应是否完成

// 可能的提示选项列表
const suggestionsList = [
    '云小夏有哪些性格特点？',
    '季辰的背景故事能详细描述一下吗？',
    '恋爱拯救系统是如何影响云小夏决策的？',
    '云小夏是如何适应系统任务的压力的？',
    '季辰的哪些行为让云小夏感到意外？',
    '云小夏和季辰的互动有哪些有趣的地方？',
    '系统任务对云小夏和季辰的关系有什么影响？',
    '云小夏和季辰的日常对话有哪些特点？',
    '系统任务对云小夏和季辰的日常生活有什么影响？',
    '云小夏和季辰的互动有哪些感人的瞬间？',
];

// 生成随机提示选项
const generateSuggestionOptions = () => {
    // 从提示列表中随机选择3个不同的选项
    const shuffled = [...suggestionsList].sort(() => 0.5 - Math.random());

    // 选择前3个
    suggestionOptions.value = shuffled.slice(0, 3);
    console.log('生成提示选项:', suggestionOptions.value);

    // 确保DOM更新后提示选项显示
    nextTick(() => {
        console.log('DOM更新后提示选项:', suggestionOptions.value.length);
    });
};

// 处理提示选项点击
const handleSuggestionClick = (option: string) => {
    console.log('选项被点击:', option);
    handleMessage(option, "novel"); // 使用点击的提示作为新的用户输入
};

onMounted(async () => {
    const sessionId = Number(route.params.sessionId);
    chatData.value = await getQAbySessionId(sessionId);

    // 初始化后立即生成提示选项
    generateSuggestionOptions();

    scrollToBottom();

    const message = store.state.message; // 从 Vuex 中获取消息
    const type = store.state.aiMessageType; // 从 Vuex 中获取消息

    if (message) {
        await handleMessage(message, type); // 调用 handleMessage 方法
    } else {
        // 如果没有初始消息，也确保显示提示选项
        // 使用定时器确保提示选项显示
        ensureSuggestionsDisplayed();
    }
});

const scrollToBottom = () => {
    nextTick(() => {
        if (chatWindowRef.value) {
            chatWindowRef.value.scrollTop = chatWindowRef.value.scrollHeight;
        }
    });
};

// 监听 streamingContent 的变化
watch(() => streamingContent.value, (newContent, oldContent) => {
    console.log('streamingContent 发生变化:', {
        old: oldContent,
        new: newContent,
        isStreaming: isStreaming.value
    });
    scrollToBottom();

}, { immediate: true });

// 修改监听流式响应完成的逻辑，确保始终生成新的提示选项
watch(() => isStreaming.value, (newValue, oldValue) => {
    // 只要不在流式生成状态，就确保有提示选项
    if (!newValue) {
        // 延迟一点时间生成提示选项，确保UI先更新
        setTimeout(() => {
            generateSuggestionOptions();
            console.log('生成新的提示选项:', suggestionOptions.value);
        }, 100);
    }
});

const gotoHistoryPage = () => {
    router.push('/historySession');
};

const gotoCreatePage = () => {
    router.push('/create');
};

const handleMessage = async (message: string, type: string) => {
    try {
        console.log('发送消息:', type, message);

        // 重置响应完成标志
        responseComplete.value = false;

        // 如果有正在进行的对话，先保存到历史记录中
        if (isStreaming.value && streamingContent.value) {
            const lastChat = chatData.value[chatData.value.length - 1];
            if (lastChat) {
                lastChat.answers = [{
                    answerText: streamingContent.value,
                    id: Date.now()
                }];
            }
            // 结束上一次的流式响应
            isStreaming.value = false;
            streamingContent.value = '';
        }

        // 清空提示选项
        suggestionOptions.value = [];

        // 添加新的聊天记录
        chatData.value.push({
            question: { questionText: message },
            answers: []
        });

        // 开始新的流式响应
        await nextTick();  // 等待 DOM 更新
        isStreaming.value = true;
        streamingContent.value = '';  // 确保新回答从空开始
        scrollToBottom();

        // 创建 EventSource 实例
        const eventSource = sendQuestion(
            Number(route.params.sessionId),
            message,
            "123",
            type
        );

        console.log('绑定消息处理器');
        eventSource.onmessage = (event) => {
            if (!event.data) return;
            let newContent = event.data;

            // 保证 markdown 换行
            newContent = newContent.replace(/\n/g, '\n\n');

            streamingContent.value += newContent;
            nextTick(() => {
                scrollToBottom();
            });
        };

        console.log('事件监听器绑定完成');

        // 当eventSource完成时确保生成提示选项
        const finalizeResponse = () => {
            console.log('完成响应，准备生成提示选项');
            responseComplete.value = true;

            // 先确保响应已保存
            if (streamingContent.value) {
                const lastChat = chatData.value[chatData.value.length - 1];
                if (lastChat) {
                    lastChat.answers = [{
                        answerText: streamingContent.value,
                        id: Date.now()
                    }];
                }
            }

            // 关闭流式响应状态
            isStreaming.value = false;

            // 生成新的提示选项，使用多个延迟时间点尝试确保显示
            generateSuggestionOptions();

            // 添加额外检查，确保提示选项显示
            setTimeout(() => {
                console.log('300ms后检查');
                ensureSuggestionsDisplayed();
            }, 300);

            setTimeout(() => {
                console.log('1000ms后检查');
                ensureSuggestionsDisplayed();
            }, 1000);
        };

        // 错误处理
        eventSource.onerror = (error) => {
            console.error('SSE错误:', error);
            eventSource.close();
            finalizeResponse();
        };

        // 结束流式响应
        // 注意：EventSource 没有 onclose 事件，这里需要使用自定义处理
        // 在收到特定消息或错误时关闭连接
        const originalClose = eventSource.close;
        eventSource.close = function () {
            console.log('SSE连接关闭');
            finalizeResponse();
            originalClose.call(eventSource);
        };

    } catch (error) {
        console.error('处理消息错误:', error);
        isStreaming.value = false;
        streamingContent.value = '';  // 清空流式内容
    } finally {
        // 删除 Vuex 中的消息
        store.commit('clearMessage'); // 清除消息
    }
};

// 确保提示选项显示的函数
const ensureSuggestionsDisplayed = () => {
    console.log('确保提示选项显示，当前状态:', {
        isStreaming: isStreaming.value,
        options: suggestionOptions.value.length,
        responseComplete: responseComplete.value
    });

    // 如果当前没有提示选项，且不在流式生成状态，则生成
    if (suggestionOptions.value.length === 0 && !isStreaming.value) {
        console.log('生成新提示选项');
        generateSuggestionOptions();
    }

    // 定时检查提示选项状态
    setTimeout(() => {
        if (suggestionOptions.value.length === 0 && !isStreaming.value) {
            console.log('提示选项为空，重新生成');
            generateSuggestionOptions();
        }
    }, 1000);
};
</script>


<style lang="scss" scoped>
.box {
    padding: 2rem;
    // height: 50%;

    .historySession {
        display: flex;

        .historyBtn {
            display: flex;
            align-items: center;
            padding: 1.3rem;
        }
    }

    .mainBox {
        display: flex;
        flex-direction: column;
        justify-content: center;
        width: 70%;
        height: 80vh;
        /* 设置聊天窗口高度为屏幕的一半 */
        margin: 0 auto;

        .chatWindow {
            overflow-y: auto;
            /* 当内容超出时，显示垂直滚动条 */
            overflow-x: hidden;
            /* 隐藏水平滚动条 */
            margin-top: 10px;
            height: 100%;
            /* 使聊天窗口占满父容器 */
        }

        .title {
            font-size: 2.5rem;
            text-align: center;
        }

        .presetList {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            width: 100%;

            .presetCard {
                width: 30.5%;
                height: 100px;
                margin-top: 10px;
                border-radius: 10px;
                padding: 10px;
                cursor: pointer;

                .presetCardTitle {
                    font-size: 1rem;
                    font-weight: bold;
                }

                .presetCardContent {
                    margin-top: 10px;
                    font-size: 0.9rem;
                }
            }
        }
    }

    .mainInput {

        width: 100%;
        margin-top: 30px;
    }

    .icon {
        width: 1rem;
        height: 1rem;
        margin-right: 5px;
    }
}

.suggestion-options {
    display: flex;
    flex-direction: column;
    width: 100%;
    margin: 20px 0;
    padding: 15px;
    // background-color: #f9f9f9;
    border-radius: 10px;
    // box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);

    .suggestion-label {
        margin-bottom: 10px;
        font-weight: bold;
        color: #606266;
        font-size: 14px;
    }

    .suggestion-list {
        display: flex;
        flex-wrap: wrap;
        gap: 10px;
    }

    .suggestion-item {
        background-color: #ffffff;
        border: 1px solid #e4e7ed;
        border-radius: 20px;
        padding: 8px 16px;
        font-size: 14px;
        color: #606266;
        cursor: pointer;
        transition: all 0.3s;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;

        &:hover {
            background-color: #ecf5ff;
            color: #409EFF;
            border-color: #d9ecff;
            transform: translateY(-2px);
            box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
        }

        &:active {
            transform: translateY(0);
        }
    }
}

// 添加自定义滚动条样式
.busy-scrollbar {
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