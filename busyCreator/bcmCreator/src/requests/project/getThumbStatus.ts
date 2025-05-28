import myAxios from "../../plugins/myAxios";

const getThumbStatus = async (id: number) => {
    try {
        const response = await myAxios.get('/project/thumb/get', {
            params: {
                id: id
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
export default getThumbStatus;

