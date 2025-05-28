import { ElMessage } from "element-plus";
import myAxios from "../../plugins/myAxios";

const handleThumbAndCollect = async (item: { projectId: number,  type: string }) => {
    try {
        const response = await myAxios.post('/project/thumb', {
            projectId: item.projectId,
            type: item.type,
        }, {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('token'),
                'Content-Type': 'application/json'
            }
        });
        ElMessage({
            showClose: true,
            message: '操作成功',
            type: 'success',
        })
        // 处理响应数据
        console.log(response);
        return response.data.data
    } catch (error) {
        ElMessage({
            showClose: true,
            message: '操作失败',
            type: 'error',
        })
        console.error('获取信息失败', error);
    }
}
export default handleThumbAndCollect;