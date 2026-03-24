import communal from '@/api/communal.js'
import home from '@/api/home.js'
import menu from '@/api/menu.js'
import userCenter from '@/api/userCenter.js'
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

// TODO
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
  user, role, perm, dict,
  log, host, interfaces,
  version, file, cache, session,

  column, category, tag, article, webSetting, friendLink, talk, page, album, message, comment, test
}
