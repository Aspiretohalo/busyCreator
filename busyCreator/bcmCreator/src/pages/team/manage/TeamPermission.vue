<template>
    <div class="box">
        <div class="layout">
            <div class="main">

                <el-table :data="members" style="width: 100%">
                    <el-table-column prop="userAvatar" label="用户头像">
                        <template #default="{ row }">
                            <div class="avatar-container">
                                <img :src="row.userAvatar" alt="用户头像" class="avatar-image" />
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="userName" label="用户昵称" />
                    <el-table-column prop="email" label="用户邮箱" />
                    <el-table-column prop="teamRole" label="成员权限">
                        <template #default="{ row }">
                            <el-tag :type="row.teamRole === 'ADMIN' ? 'warning' : 'info'">
                                {{ row.teamRole }}
                            </el-tag>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import listMemberInfoByTeamId from '../../../requests/team/listMemberInfoByTeamId';

const route = useRoute();
const members = ref([]);

onMounted(async () => {
    const teamId = Number(route.params.id); // 获取路由参数
    members.value = await listMemberInfoByTeamId(teamId); // 获取团队权限
});
</script>

<style lang="scss" scoped>
.box {
    display: flex;
    width: 100%;
    margin-top: 20px;
    border-radius: 15px;

    .main {
        width: 1200px;
        margin: 0 auto;
    }
}

.permissions-list {
    margin-top: 20px;

    .permission-item {
        margin-bottom: 15px;
        padding: 15px;
        border: 1px solid #e0e0e0;
        border-radius: 8px;
        background-color: #f9f9f9;

        .permission-title {
            font-weight: bold;
            font-size: 18px;
        }

        .permission-description {
            color: rgba(30, 31, 36, 0.5);
        }
    }
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