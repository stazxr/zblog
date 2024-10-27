import ConfirmComponent from './Confirm.vue'

const Confirm = {}

Confirm.install = function(Vue) {
  // 生成一个Vue的子类
  const ToastConstructor = Vue.extend(ConfirmComponent)
  // 生成一个该子类的实例
  const instance = new ToastConstructor()

  // 将这个实例挂载在我创建的div上，并将此div加入全局挂载点内部
  instance.$mount(document.createElement('div'))
  document.body.appendChild(instance.$el)

  // 通过Vue的原型注册一个方法，让所有实例共享这个方法
  Vue.prototype.$confirm = (options) => {
    return new Promise((resolve, reject) => {
      if (options.message) {
        instance.message = options.message
      }

      instance.ok = function() {
        resolve(true)
        instance.show = false
      }

      instance.cancel = function() {
        instance.show = false
      }

      instance.show = true
    }).catch(_ => {
      instance.show = false
    })
  }
}

export default Confirm

