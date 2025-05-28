import myAxios from "../../plugins/myAxios";
import { ElMessage } from 'element-plus';

const getUserData = async () => {
    try {
        const response = await myAxios.get('/user/get/login', {
            headers: {
                'Content-Type': 'application/json',
                Authorization: 'Bearer ' + localStorage.getItem('token') // 如果需要 token 进行身份验证
            }
        });
        return response.data.data; // 返回用户数据
    } catch (error) {
        ElMessage({
            showClose: true,
            message: '获取用户数据失败，请重试',
            type: 'error',
        });
        console.error('获取用户数据失败', error);
        throw error; // 抛出错误以便调用者处理
    }
}

export default getUserData; 