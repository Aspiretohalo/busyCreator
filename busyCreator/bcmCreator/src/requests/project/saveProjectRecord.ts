import { ElMessage } from "element-plus";
import myAxios from "../../plugins/myAxios";

const saveProjectRecord = async (item: { id: number, projectId: number, content: string, isSubmit: string }) => {
    try {
        const response = await myAxios.post('/project/version/save', {
            id: item.id,
            projectId: item.projectId,
            content: item.content,
            isSubmit: item.isSubmit
        }, {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('token'),
                'Content-Type': 'application/json'
            }
        });
        ElMessage({
            showClose: true,
            message: '保存成功',
            type: 'success',
        })
        return response.data.data
    } catch (error) {
        ElMessage({
            showClose: true,
            message: '保存失败',
            type: 'error',
        })
        console.error('获取信息失败', error);
    }
}
export default saveProjectRecord;