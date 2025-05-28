<template>
    <div class="box">
        <div class="user-info">
            <div class="user-info-left">
                <div class="user-info-left-img">
                    <img :src="userData.userAvatar" style="width: 100%; height: 100%;" alt="AI小说作家头像">
                </div>
                <div class="user-info-left-text">
                    <div class="user-name">
                        您好，
                        <span> {{ userData.userName }} </span>
                    </div>
                    <div class="user-date">
                        欢迎您使用AI创作平台，加油码字吧～
                    </div>
                </div>
            </div>
            <div class="user-info-right">
                <div class="user-info-right-item" v-for="item in userInfoItems" :key="item.title">
                    <div class="number">
                        {{ item.number }} <span class="data-unit">
                            {{ item.unit }}
                        </span>
                    </div>
                    <div class="title">{{ item.title }}</div>
                </div>
            </div>
        </div>
        <div class="personal-msg">
            <div class="main-content-top-box">
                <h2 style="margin-bottom: 20px;">个人信息</h2>
                <div class="btns">
                    <div class="btn1" v-if="isModify">
                        <el-button @click="cancelEdit">取消</el-button>
                        <el-button type="primary" @click="onSubmit">确定</el-button>
                    </div>
                    <div class="btn2" v-else>
                        <el-button @click="handleEdit">编辑</el-button>
                    </div>
                </div>
            </div>

            <el-form :model="userData" label-width="100" :label-position="'left'" :disabled="!isModify">
                <el-form-item label="用户名">
                    <el-input v-model="userData.userName" />
                </el-form-item>
                <el-form-item label="账号">
                    <el-input v-model="userData.userAccount" />
                </el-form-item>
                <el-form-item label="性别">
                    <el-radio-group v-model="userData.sex">
                        <el-radio value="0">男</el-radio>
                        <el-radio value="1">女</el-radio>
                    </el-radio-group>
                </el-form-item>
                <el-form-item label="邮箱">
                    <el-input v-model="userData.email" />
                </el-form-item>
                <el-form-item label="简介">
                    <el-input v-model="userData.profile" type="textarea" />
                </el-form-item>
            </el-form>

            <div class="main-content-top-box">
                <h2 style="margin-bottom: 20px;">认证信息</h2>
            </div>
            <div class="authIdCrad">
                <div>您还未进行实名认证，请前往认证</div>
                <div class="auth-but" @click="showNotOpenTip">前往认证</div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import updateUser from '../../requests/user/updateUser'; // 引入 updateUser
import getUserData from '../../requests/user/getUserData';
import { ElMessage } from 'element-plus';

const userData = reactive({
    userName: '',
    userAccount: '',
    profile: '',
    email: '',
    sex: '',
    userAvatar: ''
});

onMounted(async () => {
    const user = await getUserData()
    userInfoItems.value[0].number = user?.joinDays
    userInfoItems.value[1].number = user?.userProjectCountVO.projectCount
    userInfoItems.value[2].number = user?.userProjectCountVO.worksCount

    Object.assign(userData, user)
})

const isModify = ref(false);
const userInfoItems = ref([
    {
        number: 13,
        title: "入驻天数",
        unit: "天"
    }, {
        number: 6,
        title: "项目数",
        unit: "项"
    }, {
        number: 3,
        title: "作品数",
        unit: "项"
    }, {
        number: 66,
        title: "灵感值",
        unit: "个"
    },
]);



const handleEdit = () => {
    isModify.value = true; // 进入编辑状态
};

const cancelEdit = () => {
    isModify.value = false; // 退出编辑状态
};

const onSubmit = async () => {
    console.log('提交数据:', userData);
    await updateUser({
        userName: userData.userName,
        userAccount: userData.userAccount,
        sex: userData.sex,
        profile: userData.profile
    });
    isModify.value = false; // 提交后退出编辑状态
};

const showNotOpenTip = () => {
    ElMessage({
        message: '暂未开放，敬请期待',
        type: 'warning'
    });
};
</script>

<style lang="scss" scoped>
.user-info {
    width: 100%;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 26px;
    margin-left: 20px;
    margin-top: 20px;
    padding-right: 24px;

    .user-info-left {
        display: -webkit-box;
        display: flex;
        justify-content: flex-start;
        align-items: center;
        width: 37%;
        margin-right: 16px;

        .user-info-left-img {
            width: 54px;
            height: 54px;
            border-radius: 100%;
            overflow: hidden;
            border: #E0E0E0 0.5px solid;
            cursor: pointer;
        }

        .user-info-left-text {
            height: 54px;
            margin-left: 15px;
            cursor: pointer;

            .user-name {
                color: #1E1F24;
                font-size: 24px;
                font-weight: 600;
                line-height: 24px;
                text-align: left;
            }

            .user-date {
                font-family: PingFangSC-Regular;
                font-size: 14px;
                font-weight: 400;
                line-height: 14px;
                text-align: left;
                color: rgba(30, 31, 36, 0.5);
                margin-top: 14px;
            }
        }
    }

    .user-info-right {
        display: flex;
        justify-content: space-around;
        width: 63%;

        .user-info-right-item {
            display: flex;
            flex-direction: column;
            height: 54px;
            text-align: left;
            cursor: pointer;

            .number {
                font-family: baidunumber-Medium;
                font-size: 28px;
                font-weight: 500;
                line-height: 28px;
                text-align: left;
                color: #1E1F24;

                .data-unit {
                    font-family: PingFangSC-Regular;
                    font-size: 12px;
                    font-weight: 400;
                    line-height: 18px;
                    color: rgba(30, 31, 36, 0.5);
                }
            }

            .title {
                color: rgba(30, 31, 36, 0.5);
                font-size: 14px;
                font-weight: 400;
                line-height: 14px;
                margin-top: 8px;
            }

        }
    }
}

.personal-msg {
    margin-left: 20px;
    width: 91%;
    padding: 30px;
    margin-right: 16px;
    border-radius: 14px;
    background-color: #FFFFFF;
    display: flex;
    flex-direction: column;
    overflow: hidden;

    .main-content-top-box {
        width: 100%;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
    }

    .authIdCrad {
        width: 100%;
        height: 100px;
        color: #999;
        display: flex;
        flex-direction: column;
        align-items: center;



        .el-form-item {
            margin-bottom: 22px;
        }


        .el-form-item__label {
            text-align: right;
            vertical-align: middle;
            float: left;
            font-size: 14px;
            color: rgb(96, 98, 102);
            line-height: 40px;
            box-sizing: border-box;
            padding: 0px 12px 0px 0px;
        }
    }
}

.auth-but {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100px;
    min-width: 80px;
    height: 42px;
    margin-top: 10px;
    color: #fff;
    cursor: pointer;
    border-radius: 8px;
    background: linear-gradient(96.34deg, rgb(120, 90, 255) 0%, rgb(78, 110, 242) 100%);

    &:hover {
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); // 增加阴影效果
        transform: scale(1.02); // 轻微放大效果
        transition: transform 0.2s, box-shadow 0.2s; // 添加过渡效果
    }
}
</style>