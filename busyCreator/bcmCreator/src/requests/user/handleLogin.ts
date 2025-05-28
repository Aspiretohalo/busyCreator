import router from "../../config/router";
import myAxios from "../../plugins/myAxios";
import { ElMessage } from 'element-plus'

const handleLogin = async (user: { userAccount: string, userPassword: string }) => {
    try {
        // 创建章节，即将章节信息传给后端，存入数据库
        const response = await myAxios.post('/user/login', {
            userAccount: user.userAccount,
            userPassword: user.userPassword
        }, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        localStorage.setItem('token', response.data.data.jwt)
        // localStorage.setItem('User', JSON.stringify(response.data.data))
        ElMessage({
            showClose: true,
            message: '登录成功',
            type: 'success',
        })
        router.push('/')
    } catch (error) {
        ElMessage({
            showClose: true,
            message: '账号或密码错误',
            type: 'error',
        })
        console.error('账号或密码错误', error);
    }
}
export default handleLogin;