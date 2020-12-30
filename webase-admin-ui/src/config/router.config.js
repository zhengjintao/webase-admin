// eslint-disable-next-line
import {BasicLayout, BlankLayout, PageView, RouteView, UserLayout} from '@/layouts'
import { bxAnaalyse } from '@/core/icons'

export const asyncRouterMap = [

  {
    path: '/',
    name: 'index',
    component: BasicLayout,
    meta: { title: '首页' },
    redirect: '/dashboard/workplace',
    children: [
      // user
      {
        path: '/user',
        name: 'user',
        component: PageView,
        redirect: '/user/system-user',
        meta: { title: '用户管理', icon: 'check-circle-o', permission: ['result'] },
        children: [
          {
            path: '/user/system-user',
            name: 'UserList',
            component: () => import(/* webpackChunkName: "result" */ '@/pages/user/UserList'),
            meta: { title: '用户列表', keepAlive: false, hiddenHeaderContent: true, permission: ['result'] }
          }, {
            path: '/user/system-role',
            name: 'RoleList',
            component: () => import(/* webpackChunkName: "result" */ '@/pages/user/RoleList'),
            meta: { title: '角色列表', keepAlive: false, hiddenHeaderContent: true, permission: ['result'] }
          }, {
            path: '/user/system-permission',
            name: 'PermissionList',
            component: () => import(/* webpackChunkName: "result" */ '@/pages/user/PermissionList'),
            meta: { title: '权限列表', keepAlive: false, hiddenHeaderContent: true, permission: ['result'] }
          }
        ]
      },
      // dashboard
      {
        path: '/dashboard',
        name: 'dashboard',
        redirect: '/dashboard/workplace',
        component: RouteView,
        meta: { title: 'Dashboard', keepAlive: true, icon: bxAnaalyse, permission: ['dashboard'] },
        children: [
          {
            path: '/dashboard/analysis',
            name: 'Analysis',
            component: () => import('@/pages/dashboard/Analysis'),
            meta: { title: '分析页', keepAlive: false, permission: ['dashboard'] }
          },
          // 外部链接
          {
            path: 'https://www.baidu.com/',
            name: 'Monitor',
            meta: { title: '监控页（外部）', target: '_blank' }
          },
          {
            path: '/dashboard/workplace',
            name: 'Workplace',
            component: () => import('@/pages/dashboard/Workplace'),
            meta: { title: '工作台', keepAlive: true, permission: ['dashboard'] }
          }
        ]
      },

      // Exception
      {
        path: '/exception',
        name: 'exception',
        component: RouteView,
        redirect: '/exception/403',
        meta: { title: '异常页', icon: 'warning', permission: ['exception'] },
        children: [
          {
            path: '/exception/403',
            name: 'Exception403',
            component: () => import(/* webpackChunkName: "fail" */ '@/pages/exception/403'),
            meta: { title: '403', permission: ['exception'] }
          },
          {
            path: '/exception/404',
            name: 'Exception404',
            component: () => import(/* webpackChunkName: "fail" */ '@/pages/exception/404'),
            meta: { title: '404', permission: ['exception'] }
          },
          {
            path: '/exception/500',
            name: 'Exception500',
            component: () => import(/* webpackChunkName: "fail" */ '@/pages/exception/500'),
            meta: { title: '500', permission: ['exception'] }
          }
        ]
      },

      // account
      {
        path: '/account',
        component: RouteView,
        redirect: '/account/center',
        name: 'account',
        meta: { title: '个人页', icon: 'user', keepAlive: true, permission: ['user'] },
        children: [
          {
            path: '/account/center',
            name: 'center',
            component: () => import('@/pages/account/center/Index'),
            meta: { title: '个人中心', keepAlive: true, permission: ['user'] }
          },
          {
            path: '/account/settings',
            name: 'settings',
            component: () => import('@/pages/account/settings/Index'),
            meta: { title: '个人设置', hideHeader: true, permission: ['user'] },
            redirect: '/account/settings/base',
            hideChildrenInMenu: true,
            children: [
              {
                path: '/account/settings/base',
                name: 'BaseSettings',
                component: () => import('@/pages/account/settings/BaseSetting'),
                meta: { title: '基本设置', permission: ['user'] }
              },
              {
                path: '/account/settings/security',
                name: 'SecuritySettings',
                component: () => import('@/pages/account/settings/Security'),
                meta: { title: '安全设置', keepAlive: true, permission: ['user'] }
              },
              {
                path: '/account/settings/custom',
                name: 'CustomSettings',
                component: () => import('@/pages/account/settings/Custom'),
                meta: { title: '个性化设置', keepAlive: true, permission: ['user'] }
              },
              {
                path: '/account/settings/binding',
                name: 'BindingSettings',
                component: () => import('@/pages/account/settings/Binding'),
                meta: { title: '账户绑定', keepAlive: true, permission: ['user'] }
              },
              {
                path: '/account/settings/notification',
                name: 'NotificationSettings',
                component: () => import('@/pages/account/settings/Notification'),
                meta: { title: '新消息通知', keepAlive: true, permission: ['user'] }
              }
            ]
          }
        ]
      },

      {
        path: '/ship',
        component: RouteView,
        redirect: '/ship/list',
        name: 'ship',
        meta: { title: 'Shiping', icon: 'user', keepAlive: true },
        children: [
          {
            path: '/ship/list',
            name: 'ship',
            component: () => import('@/pages/ship/list/List'),
            meta: { title: 'Shiping', keepAlive: true }
          }
        ]
      }
    ]
  },
  {
    path: '*', redirect: '/404', hidden: true
  }
]

/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',
    hidden: true,
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import(/* webpackChunkName: "user" */ '@/pages/user/Login')
      },
      {
        path: 'register',
        name: 'register',
        component: () => import(/* webpackChunkName: "user" */ '@/pages/user/Register')
      },
      {
        path: 'register-result',
        name: 'registerResult',
        component: () => import(/* webpackChunkName: "user" */ '@/pages/user/RegisterResult')
      },
      {
        path: 'recover',
        name: 'recover',
        component: undefined
      }
    ]
  },

  {
    path: '/test',
    component: BlankLayout,
    redirect: '/test/home',
    children: [
      {
        path: 'home',
        name: 'TestHome',
        component: () => import('@/pages/Home')
      }
    ]
  },

  {
    path: '/test2',
    component: BlankLayout,
    redirect: '/test2/home',
    children: [
      {
        path: 'home',
        name: 'Test2Home',
        component: () => import('@/pages/ship/list/List')
      }
    ]
  },

  {
    path: '/404',
    component: () => import(/* webpackChunkName: "fail" */ '@/pages/exception/404')
  }

]
