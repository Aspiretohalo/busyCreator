import myAxios from "../../plugins/myAxios";

const previewFile = async (fileId: number) => {
    try {
        const response = await myAxios.get('/files/preview', {
            params: {
                id: fileId
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
export default previewFile;

