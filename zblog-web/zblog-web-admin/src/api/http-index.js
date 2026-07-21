// base
import communal from '@/api/communal.js'
import login from '@/api/login.js'
import home from '@/api/home.js'
import menu from '@/api/menu.js'
import userCenter from '@/api/userCenter.js'
import auth from '@/api/base/auth.js'
import user from '@/api/base/system/user.js'
import role from '@/api/base/system/role.js'
import perm from '@/api/base/system/perm.js'
import dict from '@/api/base/system/dict.js'
import log from '@/api/base/monitor/log.js'
import host from '@/api/base/monitor/host.js'
import interfaces from '@/api/base/monitor/interface.js'
import version from '@/api/base/maintain/version.js'
import file from '@/api/base/maintain/file.js'
import cache from '@/api/base/maintain/cache.js'
import session from '@/api/base/maintain/session.js'

// website
import friendLink from '@/api/service/website/friendLink.js'
import page from '@/api/service/website/page.js'
import theme from '@/api/service/website/theme.js'

// content
import category from '@/api/service/content/category.js'
import tag from '@/api/service/content/tag.js'
import article from '@/api/service/content/article.js'

// webfeed
import barrageMessage from '@/api/service/webfeed/barrageMessage.js'

// analytics
import visitor from '@/api/service/analytics/visitor.js'
import visitorLog from '@/api/service/analytics/visitorLog.js'

// TODO
import column from '@/api/service/column.js'
import webSetting from '@/api/service/webSetting.js'
import talk from '@/api/service/talk.js'
import album from '@/api/service/album.js'
import comment from '@/api/service/comment.js'
import test from '@/api/service/test.js'

export default {
  communal, login, home, menu, userCenter, auth,
  user, role, perm, dict,
  log, host, interfaces,
  version, file, cache, session,
  friendLink, page, theme,
  category, tag, article,
  // 网站动态
  barrageMessage,
  // 运营分析
  visitor, visitorLog,
  column, webSetting, talk, album, comment, test
}
