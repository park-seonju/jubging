import Vue from 'vue'
import VueRouter from 'vue-router'

import SignUp from '@/views/user/SignUp.vue'

import Mission from '@/components/mission/Mission.vue'
Vue.use(VueRouter)

const routes = [
  {
    path: '/signup',
    name: 'SignUp',
    component: SignUp
  },
  {
    path: '/mission',
    name: 'mission',
    component: Mission,
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
