<template>
    <div class="box">
        <div class="box-top">
            <div class="left-section">
                <h2>我的团队</h2>
                <div class="ultra-btn" @click="handleOpen()">创建团队</div>
                <div class="ultra-btn" style="margin-left: 20px;" @click="handleJoinOpen()">加入团队</div>
            </div>
        </div>
        <div class="item-cards">
            <div class="item-card" v-for="item in items" :key="item?.id" @click="goToDetail(item?.id)">
                <div class="item-card-top">
                    <div class="item-card-avatar">
                        <img :src="item.avatar" alt="">
                    </div>
                    <div class="item-card-msg">
                        <div class="name">
                            {{ item.name }}
                        </div>
                        <div class="description">
                            {{ item.profile }}
                        </div>
                    </div>
                </div>
                <div class="member-count">成员:{{ item.memberCount }}</div>
            </div>
        </div>
        <el-dialog v-model="dialogFormVisible" title="新建团队" width="500">
            <el-form :model="teamData">
                <el-form-item label="团队名称" label-width="auto">
                    <el-input v-model="teamData.name" autocomplete="off" />
                </el-form-item>
                <el-form-item label="团队简介" label-width="auto">
                    <el-input v-model="teamData.profile" autocomplete="off" />
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button class="ultra-btn" type="primary" @click="handleCreate(); dialogFormVisible = false">
                        新建
                    </el-button>
                </div>
            </template>
        </el-dialog>
        <el-dialog v-model="dialogJoinVisible" title="加入团队" width="500">
            <el-autocomplete v-model="state" :fetch-suggestions="querySearch" clearable class="inline-input w-50"
                placeholder="请输入团队名称" @select="handleSelect" />
            <template #footer>
                <div class="dialog-footer">
                    <el-button class="ultra-btn" type="primary"
                        @click="handleJoin(selectedTeamId!); dialogJoinVisible = false">
                        加入团队
                    </el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import listTeams from '../../requests/team/listTeams'
import createTeam from '../../requests/team/createTeam'
import { useRouter } from 'vue-router';
import listTeamsNotJoin from '../../requests/team/listTeamsNotJoin';
import joinTeam from '../../requests/team/joinTeam';

const router = useRouter();
const dialogFormVisible = ref(false)
const dialogJoinVisible = ref(false)
const items = ref([])
const restaurants = ref<RestaurantItem[]>([])

interface RestaurantItem {
    value: string
    teamId: number
}

const refreshData = async () => {
    items.value = await listTeams(); // 获取已加入的团队
    const itemsNotJoin = await listTeamsNotJoin(); // 获取未加入的团队
    restaurants.value = itemsNotJoin.map((item: any) => ({
        value: item.name,
        teamId: item.id
    }));
}

onMounted(async () => {
    await refreshData(); // 初始加载数据
})

const handleOpen = () => {
    dialogFormVisible.value = true
}

const handleJoinOpen = () => {
    dialogJoinVisible.value = true
}

const handleJoin = async (teamId: number) => {
    await joinTeam(teamId)
    await refreshData(); // 刷新数据
    dialogJoinVisible.value = false
}

const handleCreate = async () => {
    await createTeam(teamData)
    await refreshData(); // 刷新数据
    dialogFormVisible.value = false
}

const teamData = reactive({
    name: '',
    profile: '',
    avatar: 'https://files-1317662942.cos.ap-nanjing.myqcloud.com/fruit%2Fmango.png',
})

const state = ref('')
const selectedTeamId = ref<number | null>(null); // 用于存储选中的团队 ID

const querySearch = (queryString: string, cb: any) => {
    const results = queryString
        ? restaurants.value.filter(createFilter(queryString))
        : restaurants.value
    cb(results)
}

const createFilter = (queryString: string) => {
    return (restaurant: RestaurantItem) => {
        return (
            restaurant.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0
        )
    }
}

const handleSelect = (item: RestaurantItem) => {
    selectedTeamId.value = item.teamId; // 存储选中的团队 ID
    console.log(item)
}

const goToDetail = (id: number) => {
    router.push({ name: 'teamProject', params: { id } }); // 跳转到项目详情页
};
</script>

<style lang="scss" scoped>
.box {
    padding: 20px;
}

.box-top {
    display: flex;
    justify-content: space-between; // 左右分布
    align-items: center;

    .left-section {
        display: flex;
        align-items: center;

        h2 {
            margin-right: 20px;
        }
    }
}

.item-cards {
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;
    gap: 30px;
    margin-left: 20px;
}

.item-card {
    display: flex;
    flex-direction: column;
    width: 260px;
    height: 90px;
    border-radius: 15px;
    background-color: #fff;
    padding: 20px;
    margin-top: 20px;
    cursor: pointer;

    &:hover {
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2); // 增加阴影效果
        transform: scale(1.02); // 轻微放大效果
        transition: transform 0.2s, box-shadow 0.2s; // 添加过渡效果
    }

    .item-card-top {
        display: flex;
        align-items: center;

        .item-card-avatar {
            img {
                object-fit: cover;
                width: 60px;
                height: 60px;
                border-radius: 100%;
            }

            width: 60px;
            height: 60px;
        }

        .item-card-msg {
            margin-left: 10px;

            .name {
                font-weight: bold;
                font-size: 20px;
                width: 190px;
            }

            .description {
                color: rgba(30, 31, 36, 0.5);
            }
        }
    }

    .status-tag {
        margin-top: 10px; // 调整标签与卡片内容的间距
        align-self: flex-end; // 右下角对齐
    }

    .member-count {
        display: flex;
        justify-content: flex-end;
        margin-top: 10px;
    }
}

/* src/pages/team/manage/TeamProject.vue */
.item-card-msg .name {
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}

.item-card-msg .description {
    overflow: hidden;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    /* 限制为两行 */
    -webkit-box-orient: vertical;
    text-overflow: ellipsis;
}
</style>