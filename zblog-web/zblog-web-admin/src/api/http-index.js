// communal
import communal from '@/api/communal.js'
import home from '@/api/home.js'
import menu from '@/api/menu.js'
import userCenter from '@/api/userCenter.js'

// system
import user from '@/api/base/system/user.js'
import role from '@/api/base/system/role.js'
import perm from '@/api/base/system/perm.js'
import dict from '@/api/base/system/dict.js'

// monitor
import log from '@/api/base/monitor/log.js'
import host from '@/api/base/monitor/host.js'
import interfaces from '@/api/base/monitor/interface.js'

// maintain
import version from '@/api/base/maintain/version.js'
import file from '@/api/base/maintain/file.js'

// TODO service
import column from '@/api/service/column.js'
import category from '@/api/service/category.js'
import tag from '@/api/service/tag.js'
import article from '@/api/service/article.js'
import webSetting from '@/api/service/webSetting.js'
import friendLink from '@/api/service/friendLink.js'
import talk from '@/api/service/talk.js'
import page from '@/api/service/page.js'
import album from '@/api/service/album.js'
import message from '@/api/service/message.js'
import comment from '@/api/service/comment.js'
import test from '@/api/service/test.js'

export default {
  communal, home, menu, userCenter,
  // 系统管理
  user, role, perm, dict,
  // 系统监控
  log, host, interfaces,
  // 系统运维
  version, file,
  column, category, tag, article, webSetting, friendLink, talk, page, album, message, comment, test
}
