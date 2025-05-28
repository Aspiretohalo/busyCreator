import myAxios from "../../plugins/myAxios";

const setVersion = async (projectId: number, version: number) => {
    try {
        const response = await myAxios.get('/project/version/specified', {
            params: {
                projectId: projectId,
                version: version
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
export default setVersion

