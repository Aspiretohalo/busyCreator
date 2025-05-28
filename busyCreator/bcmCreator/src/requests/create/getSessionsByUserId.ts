import myAxios from "../../plugins/myAxios";

const getSessionsByUserId = async () => {
    try {
        const response = await myAxios.get('/ai/get/sessions', {
            headers: {
                Authorization: 'Bearer ' + localStorage.getItem('token'),
            }
        });
        return response.data.data
    } catch (error) {
        console.error('获取信息失败', error);
    }
};
export default getSessionsByUserId;

