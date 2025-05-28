<template>
    <div class="box">
        <div class="title">
            历史会话
        </div>
        <div class="sessionList">
            <div class="sessionCard" @click="gotoSessionPage(o.id)" v-for="o in aiSessions" :key="o.id">
                <div class="sessionContent">
                    {{ o.title }}
                </div>
                <div class="lastTime">
                    {{ formatDateTime(o.insertTime) }}
                </div>
            </div>
        </div>

    </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue';
import getSessions from '../../requests/create/getSessionsByUserId';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { formatDateTime } from '../../utils/date';

const router = useRouter()

let aiSessions: any = ref([])
onMounted(async () => {
    aiSessions.value = await getSessions()
})
const gotoSessionPage = (id: number) => {
    router.push({ name: 'chat', params: { sessionId: id } })
}
</script>

<style lang="scss" scoped>
.box {
    padding: 2rem;
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 70%;
    margin: 0 auto;

    .title {
        font-size: 2.5rem;
        text-align: center;
    }

    .sessionList {
        .sessionCard {
            display: flex;
            justify-content: space-between;
            padding: 15px;
            margin-top: 10px;
            border-radius: 5px;
            background-color: #eee;
            cursor: pointer;
        }
    }
}
</style>