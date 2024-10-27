/* eslint-disable */

/**
 * Date对象的补充函数
 *
 * 阿债 https://gitee.com/azhai/datetime.js
 */

Date.prototype.toMidnight = function() {
  this.setHours(0)
  this.setMinutes(0)
  this.setSeconds(0)
  this.setMilliseconds(0)
  return this
}

Date.prototype.daysAgo = function(days, midnight) {
  days = days ? days - 0 : 0
  const date = new Date(this.getTime() - days * 8.64E7)
  return midnight ? date.toMidnight() : date
}

Date.prototype.monthBegin = function(offset) {
  offset = offset ? offset - 0 : 0
  const days = this.getDate() - 1 - offset
  return this.daysAgo(days, true)
}

Date.prototype.quarterBegin = function() {
  const month = this.getMonth() - this.getMonth() % 3
  return new Date(this.getFullYear(), month, 1).toMidnight()
}

Date.prototype.yearBegin = function() {
  return new Date(this.getFullYear(), 0, 1).toMidnight()
}

export default Date
