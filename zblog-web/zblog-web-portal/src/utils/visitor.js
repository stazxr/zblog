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
