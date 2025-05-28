import { createStore } from 'vuex'
import state from './state'
import getters from './getter'
import mutations from './mutations'

const store: any = createStore({
    state: state,
    getters: getters,
    mutations: mutations,
})

export default store