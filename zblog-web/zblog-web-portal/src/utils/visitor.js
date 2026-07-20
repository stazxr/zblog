import portal from '@/api/portal'
import { v4 as uuidv4 } from 'uuid'

const KEY = 'z_visitor_id'

/**
 * 获取访客id
 */
export function getVisitorId() {
  let visitorId = localStorage.getItem(KEY)
  if (!visitorId) {
    visitorId = uuidv4().replace(/-/g, '')
    localStorage.setItem(KEY, visitorId)
  }
  return visitorId
}

/**
 * 获取访客日志
 */
export function recordVisitorLog() {
  try {
    portal.recordVisitorLog()
  } catch (e) {
    console.error('记录操作日志异常', e)
  }
}
