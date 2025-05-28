import myAxios from "../../plugins/myAxios";
import { ElMessage } from 'element-plus';

const listMemberInfoByTeamId = async (teamId: number) => {
    try {
        const response = await myAxios.get(`team/member/list`, {
            params: {
                id: teamId
            },
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('token'),
            }
        });
        return response.data.data; // 返回项目数据
    } catch (error) {
        ElMessage({
            showClose: true,
            message: '获取数据失败，请重试',
            type: 'error',
        });
        console.error('获取数据失败', error);
        throw error; // 抛出错误以便调用者处理
    }
}

export default listMemberInfoByTeamId; 