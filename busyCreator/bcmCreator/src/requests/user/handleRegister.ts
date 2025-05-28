import router from "../../config/router";
import myAxios from "../../plugins/myAxios";
import { ElMessage } from 'element-plus'

const handleRegister = async (user: { userAccount: string, userPassword: string, confirmPassword: string }) => {
    try {
        // 创建章节，即将章节信息传给后端，存入数据库
        const response = await myAxios.post('/user/register', {
            userAccount: user.userAccount,
            userPassword: user.userPassword,
            confirmPassword: user.confirmPassword
        }, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (response.data.code === 0) {
            ElMessage({
                showClose: true,
                message: '注册成功',
                type: 'success',
            })
            router.push('/login')
        } else {
            ElMessage({
                showClose: true,
                message: response.data.message,
                type: 'error',
            })
        }

    } catch (error) {
        ElMessage({
            showClose: true,
            message: '账号或密码错误',
            type: 'error',
        })
        console.error('账号或密码错误', error);
    }
}
export default handleRegister;