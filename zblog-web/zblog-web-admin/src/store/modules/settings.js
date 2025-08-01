import variables from '@/assets/styles/element-variables.scss'
import defaultSettings from '@/settings'

// 获取默认配置项
const { title, tagsView, fixedHeader, sidebarLogo, showFooter, footerTxt, caseNumber, webApi, forbiddenKeys } = defaultSettings

// 禁止修改的配置项
const _forbiddenKeys = forbiddenKeys || []

const state = {
  // 主题配置
  theme: variables['theme'],
  // 是否显示设置面板
  showSettings: false,
  // 网站标题
  title: title,
  // 是否显示标签栏
  tagsView: tagsView,
  // 是否固定头部
  fixedHeader: fixedHeader,
  // 是否显示侧边栏的 Logo
  sidebarLogo: sidebarLogo,
  // 是否显示设置的底部信息
  showFooter: showFooter,
  // 底部信息
  footerTxt: footerTxt,
  // 备案号
  caseNumber: caseNumber,
  // 前台访问地址
  webApi: webApi
}

const mutations = {
  // 修改设置项
  CHANGE_SETTING: (state, { key, value }) => {
    if (state.hasOwnProperty(key)) {
      if (_forbiddenKeys.indexOf(key) === -1) {
        state[key] = value
      }
    }
  }
}

const actions = {
  // 异步修改设置项
  ChangeSetting({ commit }, data) {
    commit('CHANGE_SETTING', data)
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
