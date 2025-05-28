<template>
    <div v-loading="isParsing">
        <el-form :label-position="labelPosition" label-width="auto" :model="ideasForm" :rules="rules" ref="formRef">
            <el-form-item label="请上传jpg、png、docx、doc、pdf类型文件（上传之后会为您自动解析出描述内容）">
                <Upload @uploadSuccess="handleUploadSuccess" @uploadStart="handleBeforeUpload"
                    @before-upload="handleBeforeUpload" biz="parse_ideas"></Upload>
            </el-form-item>
            <el-form-item label="创作类型" prop="type">
                <el-select-v2 v-model="selectedType" placeholder="选择您想生成的类型" :options="ideaTypeOptions"
                    @change="updateTypeLabel" />
            </el-form-item>
            <!-- <el-form-item label="文体风格" prop="genre">
                <el-select-v2 v-model="selectedGenre" placeholder="选择您想生成的文体风格" :options="genreOptions"
                    @change="updateGenreLabel" />
            </el-form-item> -->
            <el-form-item label="主题" prop="theme">
                <el-input v-model="ideasForm.theme" placeholder="添加一些创意的主题" />
            </el-form-item>
            <el-form-item label="描述内容" prop="description">
                <el-input v-model="ideasForm.description" type="textarea" :rows="3" placeholder="补充一些描述内容" />
            </el-form-item>
        </el-form>
        <el-button class="ultra-btn" type="primary" size="large" @click="confirmToCreate">立即生成</el-button>
    </div>
</template>

<script lang="ts" setup>
import { reactive, ref, watch } from 'vue';
import Upload from '../Upload.vue';
import { FormProps } from 'element-plus';
import { useStore } from 'vuex';

const store = useStore();
const emit = defineEmits(['close-drawer']); // 定义事件

const props = defineProps<{
    exist: boolean; // 接收 exist 属性
    content: string;// 传入需要修改润色的内容
}>();

const labelPosition = ref<FormProps['labelPosition']>('top');
const ideaTypeOptions = ref([
    { value: 1, label: '广告' },
    { value: 2, label: '新闻' },
    { value: 3, label: '大纲' },
    { value: 4, label: '小说' },
]);
const genreOptions = ref([
    { value: 1, label: '正式严谨' },
    { value: 2, label: '日常轻松' },
    { value: 3, label: '文艺优美' },
    { value: 4, label: '新闻客观' },
    { value: 5, label: '商业推广' },
    { value: 6, label: '幽默诙谐' },
]);

const ideasForm = reactive({
    type: '',
    theme: '',
    description: '',
    genre: '',
    content: props.content
});

const selectedType = ref(''); // 用于存储选中的类型的 value
const selectedGenre = ref(''); // 用于存储选中的类型的 value

const rules = {
    type: [{ required: true, message: '请选择创作类型', trigger: 'change' }],
    theme: [{ required: true, message: '主题不能为空', trigger: 'blur' }],
    description: [{ required: true, message: '描述内容不能为空', trigger: 'blur' }],
    genre: [{ required: true, message: '请选择文体风格', trigger: 'change' }]
};

const formRef = ref(); // 引用表单

const isParsing = ref(false); // 新增解析loading状态

const updateTypeLabel = (value: any) => {
    const selectedOption = ideaTypeOptions.value.find(option => option.value === value);
    if (selectedOption) {
        ideasForm.type = selectedOption.label; // 将选中的 label 赋值给 ideasForm.type
    }
};
const updateGenreLabel = (value: any) => {
    const selectedOption = genreOptions.value.find(option => option.value === value);
    if (selectedOption) {
        ideasForm.genre = selectedOption.label; // 将选中的 label 赋值给 ideasForm.type
    }
};

const handleUploadSuccess = async (data: any) => {
    try {
        // 假设解析过程是异步的
        // await new Promise(resolve => setTimeout(resolve, 800)); // 模拟延迟
        ideasForm.theme = data.theme;
        ideasForm.description = data.description;
    } finally {
        isParsing.value = false;
        console.log(isParsing.value);
    }
};

const handleBeforeUpload = () => {
    isParsing.value = true;
    console.log(isParsing.value);

};

const confirmToCreate = async () => {
    // 验证表单
    const isValid = await formRef.value.validate();
    if (!isValid) {
        return; // 如果验证不通过，直接返回
    }

    console.log('立即生成');
    // await GenerateIdeas(ideasForm);
    // 提交消息到 Vuex
    store.commit('setIdeasForm', ideasForm);
    emit('close-drawer'); // 发出事件以关闭 drawer
};

// 重置表单数据的方法
const resetForm = () => {
    ideasForm.type = '';
    ideasForm.theme = '';
    ideasForm.description = '';
    ideasForm.genre = '';
    selectedType.value = '';
    selectedGenre.value = '';
    formRef.value.clearValidate(); // 清除表单验证状态
};

// 监听 exist 属性的变化
watch(() => props.exist, (newValue) => {
    if (!newValue) { // 如果 exist 为 false
        resetForm(); // 重置表单数据
    }
});

</script>

<style lang="scss" scoped></style>