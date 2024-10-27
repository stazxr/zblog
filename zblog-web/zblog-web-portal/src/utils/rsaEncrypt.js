import JSEncrypt from 'jsencrypt/bin/jsencrypt.min'

// 密钥对生成 http://web.chacuo.net/netrsakeypair

const publicKey = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQEo7CNrbXiXyWPvFsEsSk9xNI\n' +
  'l0914VMVIHCGyXmXik3oUvQruPLvIOzC0Efdq3xK6SFLBxuRZOBVJxhW6jQ7s9II\n' +
  'rAXFJykfWSFf+BDggmhY89kLtCW77W62SRLWzgdCEsRHjo+y+YEu1fw1Efompgn8\n' +
  '8FL6qq131IkYpL9iWwIDAQAB'

// 加密
export function encrypt(txt) {
  const encryptor = new JSEncrypt()
  encryptor.setPublicKey(publicKey) // 设置公钥
  return encryptor.encrypt(txt) // 对需要加密的数据进行加密
}
