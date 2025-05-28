<template>
  <div class="editor">
    <Toolbar class="toolbar" :editor="editorRef" :defaultConfig="toolbarConfig" :mode="mode" />
    <div class="editor-container">
      <Editor class="content busy-scrollbar" v-model="valueHtml" :defaultConfig="editorConfig" :mode="mode"
        @onCreated="handleCreated" />
    </div>
  </div>
</template>
<script lang="ts" setup>
import "@wangeditor/editor/dist/css/style.css"; // 引入 css
import { onBeforeUnmount, ref, shallowRef, watch, onMounted } from "vue";
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";

// 定义接收的 props
const props = defineProps<{
  modelValue: string; // 使用 v-model 绑定的内容
}>();
const mode = ref("default");
const emit = defineEmits(["update:modelValue"]); // 定义 emit 事件

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef();

// 内容 HTML
const valueHtml = ref(props.modelValue || "");

// 监听 modelValue 的变化
watch(
  () => props.modelValue,
  (newValue) => {
    valueHtml.value = newValue; // 更新内容
    // console.log('valueHtml', valueHtml.value);
  }
);

// 监听内容变化并更新父组件
watch(valueHtml, (newValue) => {
  emit("update:modelValue", newValue); // 触发更新
});

const toolbarConfig = {};
const editorConfig = { placeholder: "请输入内容..." };

// 添加最后编辑时间记录
const lastEditTime = ref(Date.now());
const isEditorFocused = ref(false);

// 处理编辑器创建
const handleCreated = (editor: any) => {
  editorRef.value = editor;

  // 监听光标变化
  editor.on('focus', () => {
    isEditorFocused.value = true;
    lastEditTime.value = Date.now();
  });

  editor.on('blur', () => {
    isEditorFocused.value = false;
  });

  // 监听内容变化
  editor.on('change', () => {
    lastEditTime.value = Date.now();
  });
};

// 定时检查是否需要提示
let checkInterval: ReturnType<typeof setInterval>;

onMounted(() => {
  checkInterval = setInterval(() => {
    if (isEditorFocused.value && Date.now() - lastEditTime.value > 10000) {
      // 超过10秒未编辑，触发事件
      const content = editorRef.value?.getHtml() || '';
      emit('needCreativeSuggestions', content);
      // 重置计时器
      lastEditTime.value = Date.now();
    }
  }, 1000); // 每秒检查一次
});

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value;
  if (editor == null) return;
  editor.destroy();
  clearInterval(checkInterval);
});
</script>
<style lang="scss" scoped>
.editor {
  width: 100%;
  max-width: 1200px;
  height: calc(100vh - 120px); // 保持整体高度
  border: 1px solid #ccc;
  margin: 0 auto;
  display: flex;
  flex-direction: column; // 垂直布局

  .toolbar {
    border-bottom: 1px solid #ccc;
    flex-shrink: 0; // 工具栏不压缩
  }

  .editor-container {
    flex: 1; // 占据剩余空间
    overflow: hidden; // 防止出现双滚动条
    position: relative; // 为绝对定位提供参考

    .content {
      position: absolute; // 绝对定位
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      overflow-y: auto; // 只有内容区域可以滚动
      overflow-x: hidden; // 隐藏横向滚动条
    }
  }
}

// 自定义滚动条样式（如果需要的话）
.busy-scrollbar {
  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-thumb {
    background-color: #ccc;
    border-radius: 3px;
  }

  &::-webkit-scrollbar-track {
    background-color: #f1f1f1;
  }
}
</style>
