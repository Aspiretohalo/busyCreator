import myAxios from "../../plugins/myAxios";

const listTeamMember = async (projectId: number) => {
    try {
        const response = await myAxios.get('/task/team/member/list', {
            params: {
                project_id: projectId
            },
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('token'),
            }
        });
        // 处理响应数据
        return response.data.data
    } catch (error) {
        console.error('获取信息失败', error);
    }
};
export default listTeamMember;

