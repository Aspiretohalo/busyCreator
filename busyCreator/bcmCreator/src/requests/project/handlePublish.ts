import myAxios from "../../plugins/myAxios";

const handlePublish = async (projectId: number, projectRecordId: number) => {
    try {
        const response = await myAxios.get('/project/publish', {
            params: {
                projectId: projectId,
                projectRecordId: projectRecordId,
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
export default handlePublish;

