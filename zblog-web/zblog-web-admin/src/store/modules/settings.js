import variables from '@/assets/styles/element-variables.scss'
import defaultSettings from '@/settings'
const { tagsView, fixedHeader, sidebarLogo, showFooter, footerTxt, caseNumber, webApi } = defaultSettings

const state = {
  theme: variables['theme'],
  showSettings: false,
  tagsView: tagsView,
  fixedHeader: fixedHeader,
  sidebarLogo: sidebarLogo,
  showFooter: showFooter,
  footerTxt: footerTxt,
  caseNumber: caseNumber,
  webApi: webApi
}

const forbiddenKey = ['tokenKey', 'refTokenKey']

const mutations = {
  CHANGE_SETTING: (state, { key, value }) => {
    if (state.hasOwnProperty(key)) {
      if (forbiddenKey.indexOf(key) === -1) {
        state[key] = value
      }
    }
  }
}

const actions = {
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
