import { ElMessage } from "element-plus";
import myAxios from "../../plugins/myAxios";

const createProject = async (team: { name: string, profile: string, cover: string, teamId: number, tags: string[] }) => {
    try {
        const response = await myAxios.post('/project/create', {
            name: team.name,
            profile: team.profile,
            cover: team.cover,
            teamId: team.teamId,
            tags: team.tags
        }, {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('token'),
                'Content-Type': 'application/json'
            }
        });
        ElMessage({
            showClose: true,
            message: '创建成功',
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
export default createProject;