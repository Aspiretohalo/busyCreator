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
                        <el-form :model="form">
                            <el-form-item>
                                <el-input v-model="form.userAccount" placeholder="账号" />
                            </el-form-item>
                            <el-form-item>
                                <el-input v-model="form.userPassword" placeholder="密码" type="password">

                                </el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-button class="login_btn ultra-btn" type="primary" round
                                    @click="handleLogin(form)">登录/注册</el-button>
                            </el-form-item>
                        </el-form>
                        <el-text class="protocol">注册或登录即代表您同意《用户协议》和《隐私协议》</el-text>
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
import { reactive } from 'vue'
import handleLogin from '../requests/user/handleLogin';

interface RuleForm {
    userAccount: string,
    userPassword: string,
}

const form = reactive<RuleForm>({
    userAccount: '',
    userPassword: '',
})

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
        margin-top: 40px;
    }

    .protocol {
        margin-top: 40px;
        font-size: 14px;
    }
}

:deep(.el-dialog--center .el-dialog__body) {
    text-align: center;
}
</style>