import myAxios from "../../plugins/myAxios";

const getQAbySessionId = async (sessionId: number) => {
    try {
        const response = await myAxios.get('/ai/get/QandA', {
            params: { sessionId },
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('token'),
            }
        });
        return response.data.data
    } catch (error) {
        console.error('获取信息失败', error);
    }
};
export default getQAbySessionId;

