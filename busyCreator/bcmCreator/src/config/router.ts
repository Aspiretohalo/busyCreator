import * as VueRouter from 'vue-router'
import routes from "./route";
import { ElMessage } from 'element-plus'
import { ref } from 'vue'

const router = VueRouter.createRouter({
    history: VueRouter.createWebHashHistory(),
    routes,
    scrollBehavior(to, from, savedPosition) {
        // 始终滚动到顶部
        return { top: 0 }
    },
})

const jwt = ref()

// 全局路由守卫
router.beforeEach(async (to: any, from: any) => {
    jwt.value = localStorage.getItem('token')

    // 白名单路由，不需要登录就能访问
    const whiteList = ['/', '/login', '/register'] // 主页和登录页不需要验证

    // 如果没有token且访问的不是白名单页面
    if (!jwt.value && !whiteList.includes(to.path)) {
        ElMessage({
            showClose: true,
            message: '请先登录！',
            type: 'warning',
        })
        // 将用户重定向到登录页面
        return { path: '/login' }
    }
})

export default router