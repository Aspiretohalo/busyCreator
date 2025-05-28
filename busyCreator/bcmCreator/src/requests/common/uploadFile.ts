import myAxios from "../../plugins/myAxios";
import { ElMessage } from 'element-plus'

const uploadFile = async (file: File, biz: string) => {
    try {
        // 创建FormData对象并添加文件
        const formData = new FormData();
        formData.append('file', file);
        formData.append('biz', biz);

        // 发送文件到后端
        const response = await myAxios.post('/file/upload', formData, {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('token'),
                'Content-Type': 'multipart/form-data'
            }
        });
        if (response.data.code == 50000) {
            ElMessage({
                showClose: true,
                message: '文件上传失败',
                type: 'error',
            });
            return;
        }
        // 显示成功消息
        ElMessage({
            showClose: true,
            message: '文件上传成功',
            type: 'success',
        });
        return response.data.data
    } catch (error) {
        // 显示错误消息
        ElMessage({
            showClose: true,
            message: '文件上传失败',
            type: 'error',
        });

        // 打印错误信息到控制台
        console.error('文件上传失败', error);
    }
}
export default uploadFile;