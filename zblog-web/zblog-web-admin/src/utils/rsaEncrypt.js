import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

// 后端提供
const publicKey = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCI0IgHhe8Z9Mim7SXv4sLorWt40YSoivIu3' +
  'K/rZWHtJ6P9/ZxvA1u/LzOoainGnuXEcC0D3RW4qn2FUZ5WtORI5C9F99v2zWHLUYULsSfL3yvnM3zQ1dUcVRAKWf' +
  'dHTVvz4QUad21d3GZQmgS4CsB0iC/XPfgdrbSiVYI9I4tRNwIDAQAB'

// 加密
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对需要加密的数据进行加密
}

