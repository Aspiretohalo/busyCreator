<template>
    <div class="box">
        <div class="layout">
            <div class="main">
                <div class="settings-form">
                    <el-form :model="settings" label-width="100">
                        <el-form-item label="团队名称">
                            <el-input v-model="settings.name" />
                        </el-form-item>
                        <el-form-item label="团队描述">
                            <el-input v-model="settings.profile" type="textarea" />
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="saveSettings">保存设置</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import getTeamById from '../../../requests/team/getTeamById';
import updateTeam from '../../../requests/team/updateTeam';

const route = useRoute();
const settings = ref({
    id: 0,
    name: '',
    profile: '',
    avatar: ''
});

onMounted(async () => {
    const teamId = Number(route.params.id);
    const teamData = await getTeamById(teamId);
    if (teamData) {
        settings.value.id = teamData.id;
        settings.value.name = teamData.name;
        settings.value.profile = teamData.profile;
    }
});

const saveSettings = async () => {
    await updateTeam(settings.value);
};
</script>

<style lang="scss" scoped>
.box {
    display: flex;
    width: 100%;
    background-color: #fff;
    margin-top: 20px;
    border-radius: 15px;

    .main {
        padding: 20px;
        width: 1200px;
        margin: 0 auto;
    }
}

.settings-form {
    margin-top: 20px;
    width: 90%;
}

.avatar-container {
    background-color: #fff;
    /* 白色背景 */
    border-radius: 50%;
    /* 圆形背景 */
    padding: 5px;
    /* 内边距 */
    display: inline-block;
    /* 使背景适应内容 */
}

.avatar-image {
    width: 40px;
    /* 设置头像宽度 */
    height: 40px;
    /* 设置头像高度 */
    border-radius: 50%;
    /* 圆形头像 */
}
</style>