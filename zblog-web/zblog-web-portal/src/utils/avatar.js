const avatarContext = require.context('@/assets/avatar', true, /\.(png|jpg|jpeg)$/)

export function getAvatar(path) {
  if (!path) {
    return require('@/assets/avatar/default.png')
  }

  // 外链
  if (/^(https?:|http?:)/.test(path)) {
    return path
  }

  try {
    const relativePath = path.replace('/avatar/', '')
    return avatarContext('./' + relativePath)
  } catch (e) {
    console.warn('avatar not found:', path)
    return require('@/assets/avatar/default.png')
  }
}
