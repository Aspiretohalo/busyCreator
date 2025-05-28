import myAxios from "../../plugins/myAxios";

const listWorks = async () => {
    try {
        const response = await myAxios.get('/project/works/list', {
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
export default listWorks;

