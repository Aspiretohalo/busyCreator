import myAxios from "../../plugins/myAxios";

const searchWorks = async (keyword: string) => {
    try {
        const response = await myAxios.get('/project/works/search', {
            params: {
                keyword: keyword
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
export default searchWorks;