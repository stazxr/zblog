/**
 * 清除定时器并重置定时器标记
 * @param {number | null} timer - 定时器ID
 * @returns {null} - 返回null表示定时器已清除
 */
export function clearTimer(timer) {
  if (timer) {
    console.log('Clear Interval', timer)
    clearInterval(timer)
    return null
  }
  return timer
}
