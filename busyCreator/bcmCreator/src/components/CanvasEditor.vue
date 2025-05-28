<template>
    <div class="canvas-editor busy-scrollbar" ref="editorRef"></div>
</template>

<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import Editor from '@hufe921/canvas-editor';
import type { IEditorOption } from '@hufe921/canvas-editor';
import { EditorMode, RowFlex } from '@hufe921/canvas-editor';

const editorRef = ref();

onMounted(() => {
    // 定义编辑器配置项
    const data = {
        header: [
            {
                value: '页眉',
                size: 12,
                rowFlex: RowFlex.CENTER
            }
        ],
        main: [
            {
                value: 'Hello World',
            }
        ],
        footer: [
            {
                value: '页脚',
                size: 12,
                rowFlex: RowFlex.CENTER
            }
        ],
    };

    const options: IEditorOption = {
        mode: EditorMode.EDIT,
        control: {
            placeholderColor: '#cccccc', // 设置占位符颜色
            bracketColor: '#000000', // 设置括号颜色
            prefix: '[', // 设置前缀
            postfix: ']', // 设置后缀
            borderWidth: 1, // 设置边框宽度
            borderColor: '#ccc', // 设置边框颜色
            activeBackgroundColor: '#f0f0f0', // 设置激活状态下的背景颜色
        },
    };

    // 初始化编辑器
    const instance = new Editor(
        editorRef.value,
        data,
        options
    );
});
</script>

<style scoped>
.canvas-editor {
    /* 设置编辑器宽度 */
    height: calc(100vh - 120px);
    border: 1px solid #ccc;
    /* 设置边框 */
    overflow-y: auto;
    /* 仅添加纵向滚动条 */
    overflow-x: hidden;
    /* 隐藏横向滚动条 */
}
</style>