<template>
    <div>
        <div class="common-layout">
            <el-container style="padding: 0;">
                <el-header style="height: 80px;padding: 0;">
                    <TopNav></TopNav>
                </el-header>
                <el-main class="main bgc">
                    <div class="IDcard w-margin" style="margin-top: 0;">
                        <div class="name" :style="{
                            // background: `url(${getVIPGrade(form.experience)?.img})`,
                            backgroundSize: 'cover',
                            backgroundPosition: 'center',
                            backgroundRepeat: 'no-repeat', border: 0, borderRadius: '15px', height: '150px', width: '300px'
                        }">
                            <img style="width: 52px;height: 52px;border-radius: 50%;margin-top: 70px;margin-left: 10px;"
                                :src="form.userAvatar" />
                            <!-- <span v-if="form.experience < 30000"
                                style="margin-left: 20px;font-size: 24px;margin-top: 70px;">{{
                            form.userName }}</span> -->
                            <span v-if="form.experience < 30000 && be"
                                style="margin-left: 20px;font-size: 24px;margin-top: 70px;">阿优</span>
                            <span v-if="form.experience < 30000 && !be"
                                style="margin-left: 20px;font-size: 24px;margin-top: 70px;">小明</span>

                            <!-- <span v-else style="margin-left: 20px;margin-top: 70px;color: #ccc;font-size:24px;">{{
                            form.userName
                        }}</span> -->
                            <span class="medal">
                                <!-- <img :src="getMedalImg(form.medal)?.img" @click="dialogVisible = true" /> -->
                                <img v-if="!(form.certification == 0)" style="width: 80px;"
                                    src="https://cdn.huodongxing.com/Content/v2.0/img/vip/a1.png" />
                            </span>
                        </div>

                    </div>

                    <el-dialog v-model="dialogVisible" width="400">
                        <template #header="{ titleId, titleClass }">
                            <div class="my-header">
                                <h4 :id="titleId" :class="titleClass">珍藏勋章(已拥有)</h4>
                                <div class="tip" style="color:#eebe77;">tip：达成隐藏条件即可获得，快探索网站或者问问已经得到的朋友吧！</div>
                            </div>
                        </template>
                        <div class="medal_img">
                            <el-checkbox v-for="o in Medal" :key="o" style="height: 80px;margin-right: 0;"
                                v-model="o.checked" size="large" border @change="handleCheckboxChange(o)">
                                <div class="msg">
                                    <img :src="o.medal.medalImg" style="width: 50px;">
                                    <div>{{ o.medal.medalName }}</div>
                                </div>
                            </el-checkbox>
                        </div>
                        <template #footer>
                            <span class="dialog-footer">
                                <el-button round @click="dialogFormVisible = false">取消</el-button>
                                <el-button type="primary" round @click="handleWear(); dialogVisible = false">
                                    佩戴
                                </el-button>
                            </span>
                        </template>
                    </el-dialog>
                    <div class="message w-margin">
                        <el-card class="msg_card">
                            <el-button type="success" plain style="width: 100%;"
                                @click="handleEdit()">编辑个人资料</el-button>
                            <el-dialog v-model="dialogFormVisible" title="个人资料" width="35%">
                                <el-form :model="form" label-width="80px">
                                    <el-form-item label="姓名">
                                        <el-input v-model="form.userName" autocomplete="off" />
                                    </el-form-item>
                                    <el-form-item label="性别">
                                        <el-input v-model="form.gender" autocomplete="off" />
                                    </el-form-item>
                                    <el-form-item label="公司">
                                        <el-input v-model="form.company" autocomplete="off" />
                                    </el-form-item>
                                    <el-form-item label="部门">
                                        <el-input v-model="form.department" autocomplete="off" />
                                    </el-form-item>
                                    <el-form-item label="职位">
                                        <el-input v-model="form.position" autocomplete="off" />
                                    </el-form-item>
                                    <el-form-item label="联系电话">
                                        <el-input v-model="form.phoneNumber" disabled autocomplete="off" />
                                    </el-form-item>
                                    <el-form-item label="邮箱">
                                        <el-input v-model="form.email" autocomplete="off" />
                                    </el-form-item>
                                </el-form>
                                <template #footer>
                                    <span class="dialog-footer">
                                        <el-button round @click="dialogFormVisible = false">取消</el-button>
                                        <el-button type="primary" round @click="saveAndClose">
                                            保存
                                        </el-button>
                                    </span>
                                </template>
                            </el-dialog>
                            <el-button type="warning" plain style="width: 100%; margin-top: 20px;margin-left: 0;"
                                @click="dialogRecognizeVisible = true" v-if="form.certification == 0">线下参会认证</el-button>
                            <el-button type="warning" plain style="width: 100%; margin-top: 20px;margin-left: 0;"
                                @click="dialogTicketisible = true" v-else>
                                <el-icon size="20">
                                    <Ticket />
                                </el-icon>数字票夹
                            </el-button>
                            <el-dialog v-model="dialogRecognizeVisible" title="线下参会认证" width="500" align-center>
                                <el-form :model="RecognizeCode">
                                    <el-form-item label="专属认证码" label-width="100px">
                                        <el-input v-model="RecognizeCode.code" autocomplete="off" />
                                    </el-form-item>
                                </el-form>
                                <template #footer>
                                    <span class="dialog-footer">
                                        <el-button round @click="dialogRecognizeVisible = false">取消</el-button>
                                        <el-button type="primary" round
                                            @click="handleRecognize(); dialogVisible = false">
                                            认证
                                        </el-button>
                                    </span>
                                </template>
                            </el-dialog>
                            <el-dialog v-model="dialogTicketisible" title="我的票夹" width="500" align-center>
                                <img :src="getTicket(form.certification)" style="width: 100%;border-radius: 8px;">
                                <template #footer>
                                    <span class="dialog-footer">
                                        <el-button type="primary" round
                                            @click="handleSave(); dialogTicketisible = false">
                                            保存海报
                                        </el-button>
                                    </span>
                                </template>
                            </el-dialog>
                            <div style="margin-top: 50px;">个人简介</div>
                            <div class="data">
                                <h4>性别</h4><span>{{ form.gender }}</span>
                            </div>
                            <div class="data">
                                <h4>公司</h4><span>{{ form.company }}</span>
                            </div>
                            <div class="data">
                                <h4>部门</h4><span>{{ form.department }}</span>
                            </div>
                            <div class="data">
                                <h4>职位</h4><span>{{ form.position }}</span>
                            </div>
                            <div class="data">
                                <h4>联系电话</h4><span>{{ form.phoneNumber }}</span>
                            </div>
                            <div class="data">
                                <h4>邮箱</h4><span>{{ form.email }}</span>
                            </div>
                        </el-card>
                        <div class="msg_right">
                            <div class="small">
                                <el-card style="width: 49%;height: 100%;">
                                    <span>数据</span>
                                    <div class="actions">
                                        <div><el-icon>
                                                <img src="../assets/icon/thumb.svg" width="20px" alt="">
                                            </el-icon><span>点赞</span>
                                            <div class="amount">{{ ThumbBlogShare.thumbCount }}</div>
                                        </div>
                                        <div><el-icon>
                                                <ChatLineRound />
                                            </el-icon><span>评论</span>
                                            <div class="amount">{{ ThumbBlogShare.blogCount }}</div>
                                        </div>

                                    </div>
                                </el-card>
                                <el-card class="SignIn">
                                    <span>我的积分</span><el-link class="integral_detail"
                                        @click="checkIntegral()">积分详情<el-icon>
                                            <ArrowRight />
                                        </el-icon></el-link>
                                    <div class="integral">
                                        <img src="../assets/icon/金币.png"
                                            style="height: 36px;width: 36px;margin-right:10px;"><span
                                            style="color: #eebe77;"> {{
                                                form.integral
                                            }}</span>
                                    </div>
                                    <el-button type="warning" style="margin-left: 300px;margin-top: 30px;" plain
                                        round>查看积分规则</el-button>

                                </el-card>
                            </div>
                            <el-card class="record">
                                <el-tabs type="card" class="demo-tabs">
                                    <el-tab-pane label="议程订阅">
                                        <div class="subscription">
                                            <el-empty description="暂无数据" style="margin: 0 auto;"
                                                v-if="ActivityAndMeetingChoice.meeting.length == 0" />

                                            <div class="subscription_content" v-else
                                                v-for="o in ActivityAndMeetingChoice.meeting" :key="o.itemId">
                                                <img style="object-fit: cover;width: 270px; height: 180px; margin-top: 10px;margin-right: 20px;border-radius: 10px;"
                                                    :src="o.itemCover" />
                                                <div class="title">{{ o.itemTitle }}</div>
                                                <el-tag class="subscription_name">{{ o.itemType }}</el-tag>
                                            </div>
                                        </div>
                                    </el-tab-pane>
                                    <el-tab-pane label="活动报名">
                                        <div class="subscription">
                                            <el-empty description="暂无数据" style="margin: 0 auto;"
                                                v-if="ActivityAndMeetingChoice.activity.length == 0" />
                                            <div class="subscription_content" v-else
                                                v-for="o in ActivityAndMeetingChoice.activity" :key="o.itemId">
                                                <img style="object-fit: cover;width: 270px; height: 180px; margin-top: 10px;margin-right: 20px; border-radius: 10px;"
                                                    :src="o.itemCover" />
                                                <div class="title">{{ o.itemTitle }}</div>
                                            </div>
                                        </div>
                                    </el-tab-pane>

                                </el-tabs>
                            </el-card>
                        </div>
                    </div>
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

import { ChatLineRound, ArrowRight } from '@element-plus/icons-vue'
import { reactive, ref } from 'vue'
import state from '../store/state'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter();
const dialogRecognizeVisible = ref(false)
const dialogTicketisible = ref(false)

const dialogVisible = ref(false)
const Medal: any = ref(JSON.parse(sessionStorage.getItem("Medal") || "null") || "")
const ActivityAndMeetingChoice: any = ref(JSON.parse(sessionStorage.getItem("ActivityAndMeetingChoice") || "null") || "")
const ThumbBlogShare: any = reactive(JSON.parse(sessionStorage.getItem("ThumbBlogShare") || "null") || "")
const handleCheckboxChange = (checkbox: any) => {
    Medal.value.forEach((item: any) => {
        if (item !== checkbox) {
            item.checked = false;
        }
    });
    state.CheckedMedal = checkbox.medal.medalId
};
const handleWear = async () => {
    // await setWornMedal(state.CheckedMedal)
    form.medal = (JSON.parse(sessionStorage.getItem("User") || "null") || "").medal
}
const dialogFormVisible = ref(false)

const handleEdit = () => {
    dialogFormVisible.value = true
}

const form: any = reactive(JSON.parse(sessionStorage.getItem("User") || "null") || "")

const checkIntegral = () => {
    router.push('/integral/integralDetails')
}
const handleSave = () => {
    let save = document.createElement('a');
    // <a href=''></a>
    save.href = getTicket(form.certification)
    // 下载的名字
    save.download = '西湖论剑·海报'
    save.target = '_blank';
    // 直接回调a的点击事件
    save.click()
}

const RecognizeCode = reactive({
    code: ''
})
const handleRecognize = async () => {
    // await setCertification(Math.floor(Math.random() * 3) + 1)
    dialogRecognizeVisible.value = false
}
const getTicket = (id: number) => {
    switch (id) {
        case 1:
            return 'https://wlsd-1317662942.cos.ap-nanjing.myqcloud.com/ticket%2Fticket1.png'

        case 2:
            return 'https://wlsd-1317662942.cos.ap-nanjing.myqcloud.com/ticket%2Fticket2.png'

        case 3:
            return 'https://wlsd-1317662942.cos.ap-nanjing.myqcloud.com/ticket%2Fticket3.png'
        default:
            return ''
    }
}

const be = ref(true)


const saveAndClose = () => {
    dialogFormVisible.value = false;
    be.value = false;

    ElMessage({
        message: '修改成功！',
        type: 'success',
    })
};
</script>

<style lang="scss" scoped>
* {
    text-align: left;
}

:deep(.el-checkbox__inner) {
    width: 0;
    height: 0;
    border: 0;
}

:deep(.el-checkbox.el-checkbox--large .el-checkbox__inner) {
    width: 0;
    height: 0;
}

:deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
    background: transparent;
    border: transparent;
}

:deep(.el-checkbox.is-bordered) {
    border: 0;
}

:deep(.el-card) {
    --el-dialog-width: 35%;
    border-radius: 15px;
    background-color: rgba($color: #fff, $alpha: 0.5);
}

:deep(.el-dialog__body) {
    padding: 30px;
}

:deep(.el-dialog) {
    border-radius: 15px;
}

.medal_img {
    display: flex;
    justify-content: space-around;
    flex-wrap: wrap;

    img {
        margin-top: 10px;
    }
}

.IDcard {
    display: flex;
    justify-content: flex-start;
}

.name {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    // margin: 0 auto;
    width: 1200px;

    span {
        font-size: 48px;
    }


}

.medal {
    display: flex;
    align-items: center;
    margin-top: 70px;

    img {
        cursor: pointer;
        margin-left: 20px;
        width: 40px;
    }
}

.message {
    display: flex;
    justify-content: flex-start;

    .msg_card {
        width: 350px;
        //
        height: 800px;

        .data {
            margin-top: 20px;
        }
    }

    .msg_right {
        margin-left: 20px;
        width: 100%;

        .small {
            display: flex;
            flex-wrap: nowrap;
            justify-content: space-between;
            width: 100%;
            height: 200px;
            margin-bottom: 20px;

            .actions {
                margin-top: 20px;
                font-size: 20px;
                display: flex;
                justify-content: space-around;
                align-items: center;

                .amount {
                    margin-top: 20px;
                    text-align: center;
                }

            }

            .SignIn {
                width: 49%;

                .integral_detail {
                    float: right;
                }

                .integral {
                    font-size: 28px;
                    font-weight: 700;
                    margin-top: 30px;
                    margin-left: 120px;
                    display: flex;
                    align-items: center;
                }

                .btn {
                    margin-top: 20px;
                    width: 80px;
                }
            }
        }

        .record {
            width: 100%;
            margin-top: 20px;

            .subscription {
                display: flex;
                flex-wrap: wrap;

                .subscription_content {
                    position: relative;

                    img {
                        cursor: pointer;
                    }

                    .title {
                        color: #e6e6e6;
                        position: absolute;
                        bottom: 15px;
                        left: 2px;
                        font-size: 12px;
                        background: rgba(0, 0, 0, .5);
                    }

                    .subscription_name {
                        position: absolute;
                        top: 15px;
                        left: 10px;
                    }
                }
            }

            .thumb {
                display: flex;
                flex-wrap: wrap;

                .thumb_content {
                    position: relative;

                    .thumb_name {
                        position: absolute;
                        top: 15px;
                        left: 10px;
                    }
                }

            }
        }
    }

}
</style>