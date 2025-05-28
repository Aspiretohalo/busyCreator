import myAxios from "../../plugins/myAxios";
import { ElMessage } from 'element-plus'

const updateUser = async (user: { userName: string, userAccount: string, sex: string, profile: string }) => {
    try {
        // 创建章节，即将章节信息传给后端，存入数据库
        const response = await myAxios.post('/user/update/my', {
            userName: user.userName,
            userAccount: user.userAccount,
            sex: user.sex,
            profile: user.profile
        }, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        ElMessage({
            showClose: true,
            message: '用户信息更新成功',
            type: 'success',
        })
    } catch (error) {
        ElMessage({
            showClose: true,
            message: '更新失败，请重试',
            type: 'error',
        })
        console.error('更新失败', error);
    }
}
export default updateUser;