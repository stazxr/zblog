import Cookies from 'js-cookie'

const state = {
  // 侧边栏设置
  sidebar: {
    // 控制侧边栏的展开/关闭, 默认展开
    opened: !!+Cookies.get('sidebarStatus') || true,
    // 控制侧边栏是否需要动画效果
    withoutAnimation: false
  },
  // 设备类型
  device: 'desktop',
  // 全局 UI 尺寸配置
  size: Cookies.get('size') || 'small'
}

const mutations = {
  // 切换侧边栏的显示状态
  TOGGLE_SIDEBAR: state => {
    state.sidebar.opened = !state.sidebar.opened
    state.sidebar.withoutAnimation = false
    Cookies.set('sidebarStatus', state.sidebar.opened ? 1 : 0)
  },
  // 切换当前设备类型
  TOGGLE_DEVICE: (state, device) => {
    state.device = device
  },
  // 关闭侧边栏
  CLOSE_SIDEBAR: (state, withoutAnimation = false) => {
    state.sidebar.opened = false
    state.sidebar.withoutAnimation = withoutAnimation
    Cookies.set('sidebarStatus', 0)
  },
  // 设置元素尺寸
  SET_SIZE: (state, size) => {
    state.size = size
    Cookies.set('size', size)
  }
}

const actions = {
  // 异步切换侧边栏的显示状态
  ToggleSideBar({ commit }) {
    commit('TOGGLE_SIDEBAR')
  },
  // 异步切换当前设备类型
  ToggleDevice({ commit }, device) {
    commit('TOGGLE_DEVICE', device)
  },
  // 异步关闭侧边栏
  CloseSideBar({ commit }, { withoutAnimation }) {
    commit('CLOSE_SIDEBAR', withoutAnimation)
  },
  // 异步设置元素尺寸
  SetSize({ commit }, size) {
    commit('SET_SIZE', size)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
