<template>
    <div class="box">
        <div class="bubble">
            {{ content }}
        </div>
        <img class="avatar" :src="user?.userAvatar" alt="">
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import getUserData from '../../requests/user/getUserData';
const props = defineProps<{
    content: string;
}>();
const user: any = ref()

onMounted(async () => {
    user.value = await getUserData()
});
</script>

<style lang="scss" scoped>
.box {
    display: flex;
    justify-content: right;

    .bubble {
        max-width: 83%;
        // min-width: 10%;
        background-color: #fff;
        margin-right: 10px;
        border-radius: 8px;
        padding: 10px;
    }

    .avatar {
        height: 3rem;
        width: 3rem;
        border-radius: 50%;
    }
}
</style>
