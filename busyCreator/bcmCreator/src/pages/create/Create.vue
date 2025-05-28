<template>
    <div class="box">
        <transition name="fade">
            <div v-if="showLoading" class="inspiration-loading-mask">
                <div class="presetList inspiration-fullscreen" :class="{ 'shrink': shrinkToNormal }">
                    <div class="presetCard" v-for="item in inspiration" :key="item.title">
                        <div class="presetCardTitle">{{ item.title }}</div>
                        <div class="presetCardContent">{{ truncateText(item.content, 50) }}</div>
                    </div>
                </div>
                <div class="loading-spinner">
                    <el-icon class="is-loading" style="font-size: 48px;">
                        <Loading />
                    </el-icon>
                    <div style="margin-top: 16px; color: #409EFF;">灵感加载中...</div>
                </div>
            </div>
        </transition>
        <div v-show="!showLoading" class="main-content">
            <div class="historySession">
                <el-button class="historyBtn" @click="gotoHistoryPage">
                    <icon-history class="icon" icon-color=""></icon-history>
                    历史会话
                </el-button>
            </div>
            <div class="mainBox">
                <div class="title">
                    <span ref="textRef"></span>
                </div>
                <div class="tips">根据您的主题，给您推荐的灵感是：</div>
                <div class="presetList">
                    <div class="presetCard" v-for="item in inspiration" :key="item.title" @click="handleAsk(item)">
                        <div class="presetCardTitle">
                            {{ item.title }}
                        </div>
                        <div class="presetCardContent">
                            {{ truncateText(item.content, 50) }}
                        </div>
                    </div>
                </div>
                <div class="mainInput">
                    <SendInput @send-message="handleMessage" />
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import iconHistory from '../../components/icons/create/icon-history.vue';
import SendInput from '../../components/create/SendInput.vue';
import { useRouter, useRoute } from 'vue-router';
import { useStore } from 'vuex';
import createSession from '../../requests/create/createSession';
import { ref, onMounted, nextTick } from 'vue';
import listInspiration from '../../requests/inspiration/listInspiration';
import { Loading } from '@element-plus/icons-vue';
import searchInspiration from '../../requests/find/searchInspiration';

const store = useStore();
const router = useRouter()
const route = useRoute();
const inspiration = ref([])
const showLoading = ref(false);
const shrinkToNormal = ref(false);

const gotoHistoryPage = () => {
    router.push('/historySession')
}

const handleMessage = async (message: string, type: string) => {
    try {
        // 创建新会话
        const sessionId = await createSession();

        // 提交消息到 Vuex
        store.commit('setMessage', message);
        // 提交消息到 Vuex
        store.commit('setAiMessageType', type);
        // 跳转到聊天页面
        router.push({ name: 'chat', params: { sessionId: sessionId } });
    } catch (error) {
        console.error('发送消息错误:', error);
    }
};

const handleAsk = async (item: any) => {
    let message = '类型：' + item.title + '。要求：' + item.content
    await handleMessage(message, item.type)
}

const textRef = ref<HTMLElement | null>(null);
const text = "Hi！我是小芒， 尝试解锁无限创意，让文字跃然纸上！";

const typeWriter = async () => {
    if (!textRef.value) return;

    const punctuations = ['！', '，', '。', '、', '；', '：'];
    let currentText = '';

    for (let i = 0; i < text.length; i++) {
        currentText += text[i];
        if (textRef.value)
            textRef.value.textContent = currentText;

        // 如果当前字符是标点符号，停顿时间更长
        if (punctuations.includes(text[i])) {
            await new Promise(resolve => setTimeout(resolve, 800)); // 标点符号停顿 800ms
        } else {
            await new Promise(resolve => setTimeout(resolve, 150)); // 普通字符停顿 150ms
        }
    }

    // 全部文字显示完后等待 5 秒
    await new Promise(resolve => setTimeout(resolve, 5000));

    // 清除文字并重新开始
    currentText = '';
    if (textRef.value)
        textRef.value.textContent = currentText;

    // 重新开始打字机效果
    await typeWriter();
};

// 添加截断文本的函数
const truncateText = (text: string, maxLength: number) => {
    if (text.length <= maxLength) return text;
    return text.slice(0, maxLength) + '...';
};

onMounted(async () => {
    typeWriter();
    // inspiration.value = await listInspiration()
    // 从 Vuex 中获取灵感数据
    inspiration.value = store.state.inspirationData;
    if (inspiration.value.length == 0) {
        inspiration.value = await searchInspiration("测试")
    }
    // 判断是否带有灵感加载 query
    if (route.query.inspirationLoading === '1') {
        showLoading.value = true;
        // 2秒后触发缩小动画
        setTimeout(() => {
            shrinkToNormal.value = true;
            // 动画0.8s后关闭loading
            setTimeout(() => {
                showLoading.value = false;
            }, 800);
        }, 1500);
    }
});
</script>

<style lang="scss" scoped>
.box {
    padding: 2rem;

    .historySession {
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
        margin: 0 auto;

        .title {
            font-size: 2.5rem;
            text-align: center;
            min-height: 100px; // 防止文字出现时页面抖动

            span {
                display: inline-block;
                white-space: pre-wrap;
            }
        }

        .tips {
            font-size: 22px;
        }

        .presetList {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 15px;
            padding: 10px 0;

            .presetCard {
                background: linear-gradient(145deg, rgba(240, 245, 255, 0.9) 0%, rgba(232, 240, 254, 0.9) 100%);
                border-radius: 12px;
                padding: 16px;
                cursor: pointer;
                border: 1px solid rgba(174, 207, 255, 0.3); // 更柔和的边框
                transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
                position: relative;
                overflow: hidden;
                backdrop-filter: blur(2px); // 毛玻璃效果

                &::before {
                    content: "";
                    position: absolute;
                    top: -50%;
                    left: -50%;
                    width: 200%;
                    height: 200%;
                    background: linear-gradient(45deg,
                            rgba(255, 255, 255, 0.3) 25%,
                            transparent 25%,
                            transparent 50%,
                            rgba(255, 255, 255, 0.3) 50%,
                            rgba(255, 255, 255, 0.3) 75%,
                            transparent 75%);
                    transform: rotate(30deg);
                    transition: 0.5s;
                    opacity: 0;
                }

                &:hover {
                    transform: translateY(-3px);
                    box-shadow: 0 6px 18px rgba(136, 173, 234, 0.2);
                    background: linear-gradient(145deg, rgba(235, 242, 255, 0.95) 0%, rgba(225, 236, 253, 0.95) 100%);

                    &::before {
                        opacity: 0.4;
                        animation: shine 1.5s linear infinite;
                    }

                    .presetCardTitle {
                        color: #4a6da7; // 深海蓝色
                    }
                }

                &:active {
                    transform: translateY(-1px) scale(0.98);
                }

                .presetCardTitle {
                    font-size: 1.1rem;
                    font-weight: 600;
                    color: #5a7ab5; // 中等蓝灰色
                    margin-bottom: 8px;
                    letter-spacing: 0.5px;
                    transition: color 0.3s;
                    position: relative;
                    z-index: 1;
                }

                .presetCardContent {
                    font-size: 0.92rem;
                    line-height: 1.5;
                    color: #6b84b3; // 浅灰蓝色
                    opacity: 0.9;
                    position: relative;
                    z-index: 1;
                    display: -webkit-box;
                    -webkit-box-orient: vertical;
                    -webkit-line-clamp: 2;
                    overflow: hidden;
                    text-overflow: ellipsis;
                }
            }
        }

        @keyframes shine {
            from {
                transform: translateX(-50%) rotate(30deg);
            }

            to {
                transform: translateX(50%) rotate(30deg);
            }
        }
    }

    .mainInput {
        position: fixed;
        bottom: 100px;
        width: 58%;
    }

    .icon {
        width: 1rem;
        height: 1rem;
        margin-right: 5px;
    }
}

.inspiration-loading-mask {
    position: fixed;
    z-index: 99999;
    left: 0;
    top: 0;
    right: 0;
    bottom: 0;
    background: rgba(255, 255, 255, 0.98);
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    transition: all 0.5s;
}

.inspiration-fullscreen {
    position: absolute;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%) scale(1.2);
    width: 80vw;
    max-width: 900px;
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    transition: all 0.8s cubic-bezier(.4, 2, .6, 1);
    z-index: 100001;
}

.inspiration-fullscreen.shrink {
    left: 50%;
    top: 180px; // 你可以根据实际presetList的目标位置微调
    transform: translate(-50%, 0) scale(1);
    width: 70%;
    max-width: 900px;
}

.loading-spinner {
    position: absolute;
    left: 50%;
    top: 70%;
    transform: translate(-50%, 0);
    display: flex;
    flex-direction: column;
    align-items: center;
    z-index: 100002;
}

.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.5s;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}
</style>