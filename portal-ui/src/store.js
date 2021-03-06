import Vue from 'vue';
import Vuex from 'vuex';
// npm install --save hey-global
import G from "hey-global";

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        User: {},
        siderCollapsed: false
    },
    mutations: {
        updateAccount(state, data) {
            state.User = data;
        },
        updateSiderCollapse(state, isShow) {
            setTimeout(() => {
                // 触发重置页面大小的事件
                G.trigger('page_resize');
            }, 600);
            state.siderCollapsed = isShow;
        }
    },
    actions: {
        updateAccount(context, data) {
            context.commit('updateAccount', data);
        },
        updateSiderCollapse(context, data) {
            context.commit('updateSiderCollapse', data);
        }
    },
    getters: {
        account: state => {
            return state.User;
        },
        siderCollapsed: state => {
            return state.siderCollapsed;
        }
    }
});