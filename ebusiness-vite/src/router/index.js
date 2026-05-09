import { createRouter, createWebHistory } from 'vue-router'
//before
import IndexView from '../views/before/IndexView.vue'
import GoodsDetailView from '../views/before/GoodsDetailView.vue'
import RegisterView from '../views/before/RegisterView.vue'
import LoginView from '../views/before/LoginView.vue'
import MyselfInfoView from '../views/before/MyselfInfoView.vue'
import MyOrderView from '../views/before/MyOrderView.vue'
import MyFocusView from '../views/before/MyFocusView.vue'
import MyCartView from '../views/before/MyCartView.vue'
import GoOrderView from '../views/before/GoOrderView.vue'
//admin
import HomeView from '../views/admin/HomeView.vue'
import TypeManage from '../views/admin/TypeManageView.vue'
import GoodsManage from '../views/admin/GoodsManageView.vue'
import OrderManage from '../views/admin/OrderManageView.vue'
import AdminLoginView from '../views/admin/LoginView.vue'
import SalesStatistics from '../views/admin/SalesStatisticsView.vue'
import OrderStatistics from '../views/admin/OrderStatisticsView.vue'

const routes = [
  {
    path: '/',
    name: 'index',
    meta: {
      title:'首页'
    },
    component: IndexView
  },
  {
    path: '/goodsDetail',
    name: 'goodsDetail',
    meta: {
      title:'商品详情'
    },
    component: GoodsDetailView
  },
  {
    path: '/register',
    name: 'register',
    meta: {
      title:'注册'
    },
    component: RegisterView
  },
  {
    path: '/login',
    name: 'login',
    meta: {
      title:'登录'
    },
    component: LoginView
  },
  {
    path: '/myselfinfo',
    name: 'myselfinfo',
    component: MyselfInfoView,
    meta: {
      auth:true, 
      act:true,
      title:'个人中心'
    }//需要验证登录权限
  },
  {
    path: '/myorder',
    name: 'myorder',
    component: MyOrderView,
    meta: {
      auth:true, 
      act:true,
      title:'我的订单'
    }//需要验证登录权限
  },
  {
    path: '/myfocus',
    name: 'myfocus',
    component: MyFocusView,
    meta: {
      auth:true, 
      act:true,
      title:'我的收藏'
    }//需要验证登录权限
  },
  {
    path: '/mycart',
    name: 'mycart',
    component: MyCartView,
    meta: {
      auth:true,
      act:true,
      title:'购物车'
      }//需要验证登录权限
  },
  {
    path: '/goOrder',
    name: 'goOrder',
    component: GoOrderView,
    meta: {
      auth:true, 
      act:true,
      title:'确认订单'
    }//需要验证登录权限
  },
  //admin
  {
		path: '/adminLogin',
    name: 'adminLogin',
    meta: {
      title:'后台登录'
    },
		component: AdminLoginView
	},
	{
		path: '/home',
		name: 'home',
		component: HomeView,
		redirect: '/home/typemanage',
		meta:{
      auth:true, 
      act:false,
      title:'后台管理'
    },//需要验证登录权限
		children: [
			{
				path: '/home/typemanage',
				component: TypeManage
			},
			{
				path: '/home/goodsmanage',
				component: GoodsManage
			},
			{
				path: '/home/ordermanage',
				component: OrderManage
			},
			{
				path: '/home/salesstatistics',
				component: SalesStatistics
			},
			{
				path: '/home/orderstatistics',
				component: OrderStatistics
			}
		]
	}
]
const router = createRouter({
  //推荐使用HTML 5模式
  history: createWebHistory(),
  routes
})
export default router
