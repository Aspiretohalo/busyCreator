import { ElMessage } from "element-plus";
import myAxios from "../../plugins/myAxios";

const submitProjectRecord = async (id: number, recordComment: string) => {
    try {
        const response = await myAxios.post('/project/version/submit', {
            id: id,
            recordComment: recordComment
        }, {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('token'),
                'Content-Type': 'application/json'
            }
        });
        // 显示成功消息
        ElMessage({
            showClose: true,
            message: '提交版本成功',
            type: 'success',
        });

        return response.data.data
    } catch (error) {
        // 显示错误消息
        ElMessage({
            showClose: true,
            message: '提交版本失败',
            type: 'error',
        });
        console.error('获取信息失败', error);
    }
}
export default submitProjectRecord;