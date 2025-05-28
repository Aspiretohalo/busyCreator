import myAxios from "../../plugins/myAxios";

const joinTeam = async (teamId: number) => {
    try {
        const response = await myAxios.get('/team/join', {
            params: {
                teamId: teamId
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
export default joinTeam;

