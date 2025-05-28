<template>
    <div class="box">
        <el-upload ref="upload" v-if="biz == 'parse_ideas'" class="upload-demo" drag action="" :file-list="fileList"
            :limit="1" :http-request="handleUpload" multiple :on-progress="handleProgress">
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
                将文件拖拽上来或者 <em>点击上传</em>
            </div>
            <template #tip>
                <div class="el-upload__tip">
                    jpg、png、docx、doc、pdf files with a size less than 1mb
                </div>
            </template>
        </el-upload>

        <el-upload ref="upload" v-else-if="biz == 'parse_files'" class="upload-demo1" action="" :file-list="fileList"
            :limit="1" :http-request="handleUpload" multiple accept=".png,.jpg" :on-progress="handleProgress">
            <icon-attachment class="inputIcon" icon-color=""></icon-attachment>
        </el-upload>

        <el-upload ref="upload" v-else class="upload-demo" drag action="" :file-list="fileList" :limit="1"
            :http-request="handleUpload" multiple :on-progress="handleProgress">
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">
                将文件拖拽上来或者 <em>点击上传</em>
            </div>
            <template #tip>
                <div class="el-upload__tip">
                    jpg/png files with a size less than 500kb
                </div>
            </template>
        </el-upload>
    </div>
</template>

<script setup lang="ts">
import { UploadFilled } from '@element-plus/icons-vue';
import { onMounted, ref } from 'vue';
import uploadFile from '../requests/common/uploadFile';
import { UploadUserFile } from 'element-plus';
import iconAttachment from './icons/create/icon-attachment.vue';

// 定义接收的 props
const props = defineProps<{
    biz: string;
}>();

// 定义事件
const emit = defineEmits(['uploadSuccess', 'uploadStart', 'uploadProgress']);

const fileList = ref<UploadUserFile[]>([]);

// 定义上传文件的方法
const handleUpload = async (req: any, item?: any) => {
    emit('uploadStart'); // 通知父组件开始上传
    const response = await uploadFile(req.file, props.biz);
    if (response) {
        emit('uploadSuccess', response); // 通知父组件上传成功
    }
};

// 上传进度事件
const handleProgress = (event: any) => {
    emit('uploadProgress', event);
};
</script>

<style lang="scss" scoped>
.box {
    width: 100%;

    .upload-demo {
        width: 100%;
    }

    .upload-demo1 {
        display: flex;
        width: 100%;
    }

    .inputIcon {
        width: 1.5rem;
        height: 1.5rem;
        margin: 5px;
        cursor: pointer;
    }
}
</style>