import myAxios from "../../plugins/myAxios";

const createSession = async () => {
    try {
        const response = await myAxios.post('/ai/create/session', {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('token'),
            }
        });
        // 处理响应数据
        console.log(response);
        return response.data.data
    } catch (error) {
        console.error('获取信息失败', error);
    }
}
export default createSession;