import { ElMessage } from "element-plus";
import myAxios from "../../plugins/myAxios";

const updateTeam = async (team: { id: number, name: string, profile: string, avatar: string }) => {
    try {
        const response = await myAxios.post('/team/update', {
            id: team.id,
            name: team.name,
            profile: team.profile,
        }, {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('token'),
                'Content-Type': 'application/json'
            }
        });
        ElMessage({
            showClose: true,
            message: '修改成功',
            type: 'success',
        })
        // 处理响应数据
        console.log(response);
        return response.data.data
    } catch (error) {
        ElMessage({
            showClose: true,
            message: '创建失败',
            type: 'error',
        })
        console.error('获取信息失败', error);
    }
}
export default updateTeam;