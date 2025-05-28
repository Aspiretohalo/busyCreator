<template>
    <div class="floating-assistant" :style="{ right: position.x + 'px', bottom: position.y + 'px' }"
        @mousedown.prevent="startDrag" @contextmenu.prevent="showContextMenu" :class="{ 'show-tip': showTip }">
        <!-- 助手头像 -->
        <div class="assistant-avatar" :class="{ 'pulse': !isDragging }">
            <img src="https://files-1317662942.cos.ap-nanjing.myqcloud.com/fruit/mango.png" alt="助手" draggable="false">
        </div>

        <!-- 提示气泡 -->
        <div class="tip-bubble" v-if="showTip">
            <div class="tip-title">个性化创作提示：</div>
            <div class="tip-list">
                <div class="tip-item" v-for="(tip, index) in currentTips" :key="index">
                    {{ tip }}
                </div>
            </div>
            <div class="tip-indicator"></div>
        </div>

        <!-- 右键菜单 -->
        <div class="menu-bubble" v-if="showMenu">
            <div class="menu-item" @click.stop="handleMenuClick('spellCheck')">
                <i class="el-icon-check"></i>
                错别字检测
            </div>
            <div class="menu-item" @click.stop="handleMenuClick('styleChange')">
                <i class="el-icon-magic-stick"></i>
                文风转变
            </div>
        </div>
    </div>

    <!-- 独立于浮动助手的对话框（确保z-index最高） -->
    <el-dialog v-model="spellCheckDialogVisible" title="错别字检测" width="500px" :close-on-click-modal="false" center>
        <div v-if="isChecking" class="checking-container">
            <el-icon class="is-loading">
                <Loading />
            </el-icon>
            <span>正在检查错别字...</span>
        </div>
        <div v-else class="spell-check-container">
            <div class="spell-check-results">
                <template v-if="spellCheckResults.length > 0">
                    <div v-for="(item, index) in spellCheckResults" :key="index" class="spell-check-item">
                        <div class="word-info">
                            <div class="wrong-word">
                                <span class="label">错误用词：</span>
                                <span class="word">{{ item.wrong }}</span>
                            </div>
                            <div class="correct-word">
                                <span class="label">建议用词：</span>
                                <span class="word">{{ item.correct }}</span>
                            </div>
                        </div>
                        <el-button size="small" type="primary" @click="confirmReplace(index, item.wrong, item.correct)"
                            :disabled="item.replaced">
                            {{ item.replaced ? '已替换' : '确认替换' }}
                        </el-button>
                    </div>
                </template>
                <el-empty v-else description="未发现错别字" />
            </div>

            <div class="dialog-footer">
                <el-button type="primary" @click="recheckSpelling" :loading="isChecking">
                    <el-icon class="el-icon--left">
                        <Refresh />
                    </el-icon>
                    重新检测
                </el-button>
                <el-button @click="spellCheckDialogVisible = false">关闭</el-button>
            </div>
        </div>
    </el-dialog>
    <el-dialog v-model="styleChangeDialogVisible" title="文风转换" width="600px" :close-on-click-modal="false" center>
        <div class="style-change-container">
            <!-- 风格选择 -->
            <div class="style-options" v-if="!isTransforming && !transformedText">
                <div class="style-option" v-for="style in writingStyles" :key="style.id" @click="selectStyle(style)"
                    :class="{ 'selected': selectedStyle === style.id }">
                    <div class="style-icon">
                        <i :class="style.icon"></i>
                    </div>
                    <div class="style-info">
                        <h3>{{ style.name }}</h3>
                        <p>{{ style.description }}</p>
                    </div>
                </div>
            </div>

            <!-- 转换中状态 -->
            <div v-if="isTransforming" class="transforming-container">
                <div class="loading-section">
                    <el-icon class="is-loading">
                        <Loading />
                    </el-icon>
                    <span>正在转换文风，请稍候...</span>
                </div>

                <!-- 打字机效果输出 -->
                <div class="typewriter-output" ref="typewriterOutput">
                    <div v-html="transformedText" style="font-size: 20px;color: #000;"></div>
                    <span class="cursor" v-if="isTyping">|</span>
                </div>
            </div>

            <!-- 转换完成状态 -->
            <div v-if="transformedText && !isTransforming" class="transformed-container">
                <div class="typewriter-output" ref="typewriterOutput">
                    <div v-html="transformedText"></div>
                </div>
            </div>

            <div class="dialog-footer">
                <el-button type="primary" @click="startTransform" :disabled="!selectedStyle || isTransforming"
                    v-if="!isTransforming && !transformedText">
                    开始转换
                </el-button>
                <el-button @click="resetTransform" v-if="transformedText">
                    重新选择
                </el-button>
                <el-button @click="closeStyleDialog">关闭</el-button>
            </div>
        </div>
    </el-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted, nextTick } from 'vue';
import { ElMessage, ElDialog, ElEmpty } from 'element-plus';
import { Loading } from '@element-plus/icons-vue';
import { Refresh } from '@element-plus/icons-vue';
import { TEXT_CONTENT } from '../assets/text/textStyle'
// 位置状态
const position = ref({ x: 20, y: 200 });
const isDragging = ref(false);
const dragOffset = ref({ x: 0, y: 0 });

// 提示相关
const showTip = ref(false);
const currentTips = ref<string[]>([]);

// 菜单相关
const showMenu = ref(false);

// 错别字检测相关
const spellCheckDialogVisible = ref(false);
const isChecking = ref(false);

// 开始拖动
const startDrag = (e: MouseEvent) => {
    if (e.button === 0) {
        isDragging.value = true;
        dragOffset.value = {
            x: window.innerWidth - e.clientX - position.value.x,
            y: window.innerHeight - e.clientY - position.value.y
        };
        document.addEventListener('mousemove', onDrag);
        document.addEventListener('mouseup', stopDrag);
    }
};

// 拖动中
const onDrag = (e: MouseEvent) => {
    if (isDragging.value && e.buttons === 1) {
        const assistantElement = document.querySelector('.floating-assistant') as HTMLElement;
        const assistantWidth = assistantElement?.offsetWidth || 70;
        const assistantHeight = assistantElement?.offsetHeight || 70;

        const newX = window.innerWidth - e.clientX - dragOffset.value.x;
        const newY = window.innerHeight - e.clientY - dragOffset.value.y;

        position.value = {
            x: Math.min(Math.max(0, newX), window.innerWidth - assistantWidth),
            y: Math.min(Math.max(0, newY), window.innerHeight - assistantHeight)
        };
    } else {
        stopDrag();
    }
};

// 停止拖动
const stopDrag = () => {
    isDragging.value = false;
    document.removeEventListener('mousemove', onDrag);
    document.removeEventListener('mouseup', stopDrag);
};

// 显示右键菜单
const showContextMenu = (e: MouseEvent) => {
    e.preventDefault();
    showMenu.value = true;
    showTip.value = false;
};

// 处理菜单点击
const handleMenuClick = async (action: string) => {
    switch (action) {
        case 'spellCheck':
            console.log('spellCheck');

            spellCheckDialogVisible.value = true;
            isChecking.value = true;
            showMenu.value = false;

            try {
                // 模拟API调用延迟
                await new Promise(resolve => setTimeout(resolve, 3000));

                // 模拟检测结果
                spellCheckResults.value = [
                    { wrong: '周起', correct: '皱起', replaced: false },
                    { wrong: '港子', correct: '巷子', replaced: false },
                ];
            } catch (error) {
                ElMessage.error('检查错别字时发生错误');
            } finally {
                isChecking.value = false;
            }
            break;

        case 'styleChange':
            showMenu.value = false;
            styleChangeDialogVisible.value = true;
            break;
    }
};

// 窗口大小变化处理
const handleResize = () => {
    const assistantElement = document.querySelector('.floating-assistant') as HTMLElement;
    const assistantWidth = assistantElement?.offsetWidth || 70;
    const assistantHeight = assistantElement?.offsetHeight || 70;

    position.value = {
        x: Math.min(position.value.x, window.innerWidth - assistantWidth),
        y: Math.min(position.value.y, window.innerHeight - assistantHeight)
    };
};

// 点击外部关闭菜单和提示
const handleClick = (e: MouseEvent) => {
    const target = e.target as HTMLElement;
    if (!target.closest('.floating-assistant')) {
        showTip.value = false;
        showMenu.value = false;
    }
};

// 修改spellCheckResults的类型和初始化
const spellCheckResults = ref<Array<{
    wrong: string,
    correct: string,
    replaced?: boolean
}>>([]);

// 定义emit事件
const emit = defineEmits(['replace-word']);

// 修改confirmReplace方法
const confirmReplace = (index: number, wrongWord: string, correctWord: string) => {
    spellCheckResults.value[index].replaced = true;
    ElMessage.success(`已替换 "${wrongWord}" 为 "${correctWord}"`);

    // 触发事件将替换信息传递给父组件
    emit('replace-word', {
        wrong: wrongWord,
        correct: correctWord,
        index: index
    });
};


// 重新检测方法
const recheckSpelling = async () => {
    isChecking.value = true;
    spellCheckResults.value = [];

    try {
        // 模拟API调用延迟
        await new Promise(resolve => setTimeout(resolve, 1500));

        // 模拟新的检测结果
        spellCheckResults.value = [
            { wrong: '周起', correct: '皱起', replaced: false },
            { wrong: '港子', correct: '巷子', replaced: false },
        ];
    } catch (error) {
        ElMessage.error('重新检测时发生错误');
    } finally {
        isChecking.value = false;
    }
};
// 新增的文风转换相关状态
const styleChangeDialogVisible = ref(false);
const isTransforming = ref(false);
const selectedStyle = ref<string | null>(null);
const transformedText = ref<string>(''); // 定义 transformedText
const isTyping = ref(false);
let typewriterStopFlag = false;

// 文风选项
const writingStyles = ref([
    {
        id: 'hlm',
        name: '红楼梦风格',
        description: '模仿《红楼梦》的文风，细腻描写人物情感和生活细节',
        icon: 'el-icon-notebook-2',
        prompt: '请用《红楼梦》的文风重写这段文字，细腻描写人物情感和生活细节，使用文言文和白话文结合的方式。'
    },
    {
        id: 'xyj',
        name: '西游记风格',
        description: '模仿《西游记》的文风，充满奇幻色彩和冒险精神',
        icon: 'el-icon-data-board',
        prompt: '请用《西游记》的文风重写这段文字，充满奇幻色彩和冒险精神，使用文言文和白话文结合的方式。'
    },
    {
        id: 'sgyy',
        name: '三国演义风格',
        description: '模仿《三国演义》的文风，展现历史的宏大和人物的英勇',
        icon: 'el-icon-chat-line-round',
        prompt: '请用《三国演义》的文风重写这段文字，展现历史的宏大和人物的英勇，使用文言文和白话文结合的方式。'
    },
    {
        id: 'shz',
        name: '水浒传风格',
        description: '模仿《水浒传》的文风，展现英雄豪情和江湖义气',
        icon: 'el-icon-tickets',
        prompt: '请用《水浒传》的文风重写这段文字，展现英雄豪情和江湖义气，使用文言文和白话文结合的方式。'
    }
]);

const selectStyle = (style: any) => {
    selectedStyle.value = style.id;
};

const startTransform = async () => {
    if (!selectedStyle.value) return;

    isTransforming.value = true;
    transformedText.value = '';
    typewriterStopFlag = false;

    try {
        // 模拟API调用延迟
        await new Promise(resolve => setTimeout(resolve, 3000));

        // 获取转换后的文本
        // const selectedText = 
        const selectedText = TEXT_CONTENT[selectedStyle.value as keyof typeof TEXT_CONTENT];

        // 打字机效果输出
        await typewriterEffect(selectedText);

    } catch (error) {
        ElMessage.error('文风转换时发生错误');
    } finally {
        isTransforming.value = false;
    }
};

const typewriterEffect = async (text: string) => {
    for (const char of text) {
        if (typewriterStopFlag) break;
        transformedText.value += char; // 逐字追加到 transformedText
        await new Promise(resolve => setTimeout(resolve, 50)); // 控制打字速度
    }
};

const resetTransform = () => {
    typewriterStopFlag = true;
    selectedStyle.value = null;
    transformedText.value = '';
};

const closeStyleDialog = () => {
    typewriterStopFlag = true;
    styleChangeDialogVisible.value = false;
    selectedStyle.value = null;
    transformedText.value = '';
};

onMounted(() => {
    // 5秒后显示提示
    setTimeout(() => {
        currentTips.value = [
            '林悦在旧上海是否会遇到真实的历史人物，从而影响历史的走向？',
            '她是否会再次遇到类似的神秘门，每次开启都带她进入一个全新的时空？',
            '点击"智能生成"按钮，让我来帮你创作内容吧！'
        ];
        showTip.value = true;
    }, 5000);

    window.addEventListener('resize', handleResize);
    document.addEventListener('click', handleClick);
});

onUnmounted(() => {
    window.removeEventListener('resize', handleResize);
    document.removeEventListener('click', handleClick);
});
</script>



<style lang="scss" scoped>
.floating-assistant {
    position: fixed;
    z-index: 9999;
    cursor: move;
    user-select: none;
    pointer-events: all;
    filter: drop-shadow(0 4px 12px rgba(0, 0, 0, 0.15));

    .assistant-avatar {
        width: 70px;
        height: 70px;
        border-radius: 50%;
        overflow: hidden;
        background: linear-gradient(145deg, #ffffff, #f0f0f0);
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
        transition: all 0.3s ease;
        pointer-events: none;
        border: 3px solid #fff;

        &.pulse {
            animation: pulse 2s infinite;
        }

        img {
            width: 100%;
            height: 100%;
            object-fit: cover;
            pointer-events: none;
            -webkit-user-drag: none;
            user-select: none;
            transform: scale(1.1);
        }

        &:hover {
            transform: scale(1.1) rotate(5deg);
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.25);
        }
    }

    .tip-bubble {
        pointer-events: auto;
        position: absolute;
        right: 85px;
        top: 50%;
        transform: translateY(-50%);
        background: linear-gradient(145deg, #ffffff, #f8f8f8);
        padding: 20px;
        border-radius: 16px;
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
        max-width: 400px;
        min-width: 320px;
        font-size: 14px;
        color: #333;
        animation: fadeIn 0.4s ease-out;
        border: 1px solid rgba(0, 0, 0, 0.1);

        .tip-title {
            font-weight: bold;
            margin-bottom: 12px;
            color: #409EFF;
            font-size: 16px;
        }

        .tip-list {
            display: flex;
            flex-direction: column;
            gap: 12px;

            .tip-item {
                background: rgba(64, 158, 255, 0.05);
                padding: 12px 16px;
                border-radius: 8px;
                line-height: 1.5;
                color: #666;
                transition: all 0.3s;
                border-left: 3px solid #409EFF;
                cursor: pointer;

                &:hover {
                    background: rgba(64, 158, 255, 0.1);
                    color: #409EFF;
                    transform: translateX(5px);
                }

                &:active {
                    background: rgba(64, 158, 255, 0.15);
                }
            }
        }

        .tip-indicator {
            position: absolute;
            right: -3px;
            top: 50%;
            transform: translateY(-50%);
            width: 6px;
            height: 6px;
            background-color: #409EFF;
            border-radius: 50%;
            animation: blink 1s infinite;
        }
    }

    .menu-bubble {
        position: absolute;
        right: 85px;
        top: 50%;
        transform: translateY(-50%);
        background: white;
        padding: 8px 0;
        border-radius: 12px;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        min-width: 200px;
        animation: fadeIn 0.3s ease-out;
        border: 1px solid rgba(0, 0, 0, 0.1);
        z-index: 1000;
        cursor: default;

        .menu-item {
            padding: 12px 20px;
            display: flex;
            align-items: center;
            cursor: pointer;
            transition: all 0.3s;
            color: #333;
            font-size: 14px;

            i {
                margin-right: 8px;
                font-size: 16px;
            }

            &:hover {
                background-color: #f5f7fa;
                color: #409EFF;
            }

            &:active {
                background-color: #e8f3ff;
            }
        }
    }
}

/* 动画效果 */
@keyframes pulse {
    0% {
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
        transform: scale(1);
    }

    50% {
        box-shadow: 0 4px 25px rgba(76, 175, 80, 0.4);
        transform: scale(1.05);
    }

    100% {
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
        transform: scale(1);
    }
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translate(-20px, -50%);
    }

    to {
        opacity: 1;
        transform: translate(0, -50%);
    }
}

@keyframes blink {

    0%,
    100% {
        opacity: 1;
    }

    50% {
        opacity: 0.5;
    }
}

/* 错别字检测样式 */
.checking-container {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 30px 0;

    .el-icon {
        font-size: 24px;
        margin-right: 10px;
        color: #409EFF;
    }
}

.spell-check-results {
    max-height: 400px;
    overflow-y: auto;

    .spell-check-item {
        padding: 15px;
        border: 1px solid #ebeef5;
        border-radius: 8px;
        margin-bottom: 10px;
        background-color: #f8f9fa;

        &:last-child {
            margin-bottom: 0;
        }

        .wrong-word,
        .correct-word {
            display: flex;
            align-items: center;
            margin: 5px 0;

            .label {
                color: #909399;
                width: 80px;
                flex-shrink: 0;
            }

            .word {
                color: #303133;
            }
        }

        .wrong-word .word {
            color: #f56c6c;
            text-decoration: line-through;
        }

        .correct-word .word {
            color: #67c23a;
        }
    }
}

.spell-check-container {
    display: flex;
    flex-direction: column;
    max-height: 60vh; // 限制对话框高度
}

.spell-check-results {
    flex: 1;
    overflow-y: auto;
    margin-bottom: 20px;
    padding-right: 10px; // 给滚动条留空间

    .spell-check-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 12px;
        border: 1px solid #ebeef5;
        border-radius: 8px;
        margin-bottom: 10px;
        background-color: #f8f9fa;
        transition: all 0.3s;

        &:hover {
            box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
        }

        .word-info {
            flex: 1;
            margin-right: 15px;

            .wrong-word,
            .correct-word {
                display: flex;
                align-items: center;
                margin: 5px 0;

                .label {
                    color: #909399;
                    width: 80px;
                    flex-shrink: 0;
                }

                .word {
                    color: #303133;
                }
            }

            .wrong-word .word {
                color: #f56c6c;
                text-decoration: line-through;
            }

            .correct-word .word {
                color: #67c23a;
            }
        }
    }
}

.dialog-footer {
    display: flex;
    justify-content: flex-end;
    padding-top: 15px;
    border-top: 1px solid #ebeef5;
}

.style-change-container {
    display: flex;
    flex-direction: column;
    height: 100%;
}

.style-options {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 16px;
    margin-bottom: 20px;
}

.style-option {
    display: flex;
    align-items: center;
    padding: 16px;
    border: 1px solid #ebeef5;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.3s;
    background-color: #f8f9fa;

    &:hover {
        border-color: #409EFF;
        box-shadow: 0 2px 12px rgba(64, 158, 255, 0.1);
    }

    &.selected {
        border-color: #409EFF;
        background-color: #ecf5ff;
    }

    .style-icon {
        margin-right: 16px;
        font-size: 24px;
        color: #409EFF;
    }

    .style-info {
        flex: 1;

        h3 {
            margin: 0 0 8px 0;
            font-size: 16px;
            color: #303133;
        }

        p {
            margin: 0;
            font-size: 14px;
            color: #909399;
        }
    }
}

.transforming-container {
    display: flex;
    flex-direction: column;
    flex: 1;
}

.loading-section {
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 20px 0;
    color: #409EFF;

    .el-icon {
        font-size: 24px;
        margin-right: 10px;
        animation: rotate 2s linear infinite;
    }
}

.typewriter-output {
    flex: 1;
    max-height: 300px;
    overflow-y: auto;
    padding: 16px;
    background-color: #f8f9fa;
    border-radius: 8px;
    font-family: 'Courier New', monospace;
    line-height: 1.8;
    white-space: pre-wrap;
    border: 1px solid #ebeef5;

    .output-line {
        margin-bottom: 8px;
    }

    .cursor {
        animation: blink 1s infinite;
        color: #409EFF;
    }
}

.dialog-footer {
    display: flex;
    justify-content: flex-end;
    padding-top: 20px;
    margin-top: 20px;
    border-top: 1px solid #ebeef5;
}

@keyframes rotate {
    from {
        transform: rotate(0deg);
    }

    to {
        transform: rotate(360deg);
    }
}

@keyframes blink {

    0%,
    100% {
        opacity: 1;
    }

    50% {
        opacity: 0;
    }
}
</style>