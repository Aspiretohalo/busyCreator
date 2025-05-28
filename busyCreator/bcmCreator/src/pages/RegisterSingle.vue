<template>
    <div>
        <div class="common-layout">
            <el-container style="padding: 0;">
                <el-header style="height: 60px;padding: 0;">
                    <TopNav></TopNav>
                </el-header>
                <el-main class="main">
                    <el-card class="box-card w-margin">
                        <img class="logo" src="@/assets/logo/logo_main.png" alt="">
                        <el-form ref="ruleFormRef" :model="form" :rules="rules">
                            <el-form-item prop="userAccount">
                                <el-input v-model="form.userAccount" placeholder="账号" />
                            </el-form-item>
                            <el-form-item prop="userPassword">
                                <el-input v-model="form.userPassword" placeholder="密码" type="password" show-password />
                            </el-form-item>
                            <el-form-item prop="confirmPassword">
                                <el-input v-model="form.confirmPassword" placeholder="确认密码" type="password"
                                    show-password />
                            </el-form-item>
                            <el-form-item>
                                <el-button class="login_btn ultra-btn" type="primary" round
                                    @click="submitForm(ruleFormRef)">注册</el-button>
                            </el-form-item>
                        </el-form>
                        <div class="bottom-links">
                            <div class="login-link" @click="goToLogin">已有账号？立即登录</div>
                            <el-text class="protocol">注册或登录即代表您同意《用户协议》和《隐私协议》</el-text>
                        </div>
                    </el-card>
                </el-main>
                <el-footer style="padding: 0;">
                    <Bottom></Bottom>
                </el-footer>
            </el-container>
        </div>
    </div>
</template>

<script lang="ts" setup>
import TopNav from '../components/TopNav.vue'
import Bottom from '../components/Bottom.vue'
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import handleRegister from '../requests/user/handleRegister'
import type { FormInstance, FormRules } from 'element-plus'
import { ElMessage } from 'element-plus'

const router = useRouter()
const ruleFormRef = ref<FormInstance>()

interface RuleForm {
    userAccount: string
    userPassword: string
    confirmPassword: string
}

const form = reactive<RuleForm>({
    userAccount: '',
    userPassword: '',
    confirmPassword: ''
})

// 验证密码
const validatePass = (rule: any, value: any, callback: any) => {
    if (value === '') {
        callback(new Error('请输入密码'))
    } else {
        // 密码必须包含字母和数字，长度在8-16位之间
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,16}$/
        if (!passwordRegex.test(value)) {
            callback(new Error('密码必须包含字母和数字，长度在8-16位之间'))
        }
        if (form.confirmPassword !== '') {
            if (!ruleFormRef.value) return
            ruleFormRef.value.validateField('confirmPassword', () => null)
        }
        callback()
    }
}

// 验证确认密码
const validatePass2 = (rule: any, value: any, callback: any) => {
    if (value === '') {
        callback(new Error('请再次输入密码'))
    } else if (value !== form.userPassword) {
        callback(new Error('两次输入密码不一致!'))
    } else {
        callback()
    }
}

// 验证账号
const validateAccount = (rule: any, value: any, callback: any) => {
    if (value === '') {
        callback(new Error('请输入账号'))
    } else {
        // 账号长度在4-16位之间，只能包含字母、数字和下划线
        const accountRegex = /^[a-zA-Z0-9_]{4,16}$/
        if (!accountRegex.test(value)) {
            callback(new Error('账号长度在4-16位之间，只能包含字母、数字和下划线'))
        }
        callback()
    }
}

const rules = reactive<FormRules>({
    userAccount: [{ validator: validateAccount, trigger: 'blur' }],
    userPassword: [{ validator: validatePass, trigger: 'blur' }],
    confirmPassword: [{ validator: validatePass2, trigger: 'blur' }]
})

const submitForm = async (formEl: FormInstance | undefined) => {
    if (!formEl) return
    await formEl.validate((valid, fields) => {
        if (valid) {
            handleRegister(form)
        } else {
            ElMessage.error('请正确填写注册信息！')
            return false
        }
    })
}

const goToLogin = () => {
    router.push('/login')
}
</script>

<style lang="scss" scoped>
:deep(.el-dialog) {
    border-radius: 15px;
}

:deep(.el-form-item__label) {
    font-size: 18px;
}

:deep(.el-card__body) {
    padding: 50px 40px;
}

:deep(.el-input__inner) {
    height: 42px;
}

.box-card {
    width: 26%;
    height: 500px;

    .logo {
        height: 42px;
        margin-bottom: 30px;
    }

    .wxLogin {
        display: flex;

        span {
            font-size: 16px;
        }

        .wx_img {
            cursor: pointer;
            margin-left: 30px;
        }
    }

    .login_btn {
        width: 100%;
        height: 45px;
        margin-top: 10px;
    }

    .protocol {
        margin-top: 10px;
        font-size: 14px;
    }
}

.bottom-links {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 20px;

    .login-link {
        color: #6d6d6d;
        cursor: pointer;
        font-size: 14px;

        &:hover {
            text-decoration: underline;
        }
    }
}

:deep(.el-dialog--center .el-dialog__body) {
    text-align: center;
}
</style>