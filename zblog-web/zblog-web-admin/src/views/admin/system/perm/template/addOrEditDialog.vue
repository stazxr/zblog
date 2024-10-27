<template>
  <div>
    <el-dialog append-to-body :close-on-click-modal="false" :before-close="handleClose" :visible.sync="dialogVisible" :title="dialogTitle" width="580px">
      <el-form ref="addForm" :inline="true" :model="addForm" :rules="addRules" size="small" label-width="80px">
        <el-form-item label="权限类型" prop="permType">
          <el-radio-group v-model="addForm.permType" :disabled="addForm.id != null && addForm.id !== ''" size="mini" style="width: 178px" @change="permTypeChange">
            <el-radio-button label="1">目录</el-radio-button>
            <el-radio-button label="2">菜单</el-radio-button>
            <el-radio-button label="3">按钮</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-show="addForm.permType.toString() !== '3'" label="菜单图标" prop="icon">
          <el-popover placement="bottom-start" trigger="click" width="450" @show="$refs['iconSelect'].reset()">
            <icon-select ref="iconSelect" @selected="iconSelected" />
            <el-input slot="reference" v-model="addForm.icon" style="width: 450px" placeholder="点击选择图标" readonly>
              <svg-icon v-if="addForm.icon" slot="prefix" :icon-class="addForm.icon" class="el-input__icon" style="height: 32px;width: 16px" />
              <i v-else slot="prefix" class="el-icon-search el-input__icon" />
            </el-input>
          </el-popover>
        </el-form-item>
        <el-form-item v-show="addForm.permType.toString() !== '3'" label="外链菜单" prop="iFrame">
          <el-radio-group v-model="addForm.iFrame" :disabled="addForm.id != null && addForm.id !== ''" size="mini" @change="iFrameChange">
            <el-radio-button label="true">是</el-radio-button>
            <el-radio-button label="false">否</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-show="addForm.permType.toString() === '2'" label="菜单缓存" prop="cache">
          <el-radio-group v-model="addForm.cache" size="mini">
            <el-radio-button label="true">是</el-radio-button>
            <el-radio-button label="false">否</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-show="addForm.permType.toString() !== '3'" label="菜单可见" prop="hidden">
          <el-radio-group v-model="addForm.hidden" size="mini">
            <el-radio-button label="false">是</el-radio-button>
            <el-radio-button label="true">否</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="addForm.permType.toString() !== '1'" label="权限编码" prop="permCode">
          <el-select
            v-model="addForm.permCode"
            :title="addForm.permCode"
            filterable
            clearable
            :disabled="addForm.iFrame != null && addForm.iFrame.toString() === 'true'"
            placeholder="权限编码"
            style="width: 178px"
            @change="choosePermCode"
          >
            <el-option
              v-for="item in permCodes"
              :key="item.value"
              :label="item.value"
              :value="item.value"
              :title="item.name"
              :disabled="item.disabled && item.value !== oldPermCode"
              style="width: 178px"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="addForm.permType.toString() !== '3'" label="菜单名称" prop="permName">
          <el-input v-model="addForm.permName" :style="addForm.permType.toString() === '0' ? 'width: 450px' : 'width: 178px'" placeholder="菜单名称" maxlength="20" show-word-limit />
        </el-form-item>
        <el-form-item v-if="addForm.permType.toString() === '3'" label="按钮名称" prop="permName">
          <el-input v-model="addForm.permName" placeholder="按钮名称" style="width: 178px" maxlength="20" show-word-limit />
        </el-form-item>
        <el-form-item v-if="addForm.permType.toString() !== '3'" label="路由地址" prop="routerPath">
          <el-input v-model="addForm.routerPath" placeholder="路由地址" style="width: 178px" />
        </el-form-item>
        <el-form-item label="权限排序" prop="sort">
          <el-input-number v-model.number="addForm.sort" :min="0" :max="99999" step-strictly controls-position="right" style="width: 178px" />
        </el-form-item>
        <el-form-item v-if="(addForm.iFrame == null || addForm.iFrame.toString() === 'false') && addForm.permType.toString() === '2'" label="组件名称" prop="componentName">
          <el-input v-model="addForm.componentName" placeholder="匹配组件内Name字段" style="width: 178px" />
        </el-form-item>
        <el-form-item v-if="(addForm.iFrame == null || addForm.iFrame.toString() === 'false') && addForm.permType.toString() === '2'" label="组件路径" prop="componentPath">
          <el-input v-model="addForm.componentPath" placeholder="组件路径" style="width: 178px" />
        </el-form-item>
        <el-form-item label="访问级别" prop="permLevel">
          <el-select v-model="addForm.permLevel" clearable placeholder="访问级别" style="width: 178px">
            <el-option v-for="item in permLevels" :key="item.value" :label="item.name" :value="item.value" :title="item.desc" />
          </el-select>
        </el-form-item>
        <el-form-item label="权限状态" prop="enabled">
          <el-select v-model="addForm.enabled" placeholder="是否启用" style="width: 178px">
            <el-option v-for="item in permEnabled" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="上级类目" prop="pid">
          <treeselect v-model="addForm.pid" :options="menus" :normalizer="normalizer" no-results-text="搜索结果为空" placeholder="选择上级类目" style="width: 450px" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="cancel">取 消</el-button>
        <el-button :loading="submitLoading" type="primary" @click="submit">确 认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import IconSelect from '@/components/IconSelect'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
export default {
  components: {
    IconSelect,
    Treeselect
  },
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    },
    dialogTitle: {
      type: String,
      default: ''
    },
    dataId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      submitLoading: false,
      menus: [],
      permCodes: [],
      oldPermCode: '',
      permLevels: [
        { name: '公开', value: 1, desc: '不需要登录，可以直接访问' },
        { name: '认证', value: 2, desc: '需要登录才可以访问' },
        { name: '授权', value: 3, desc: '需要授权才可以访问' }
      ],
      permEnabled: [
        { name: '启用', value: true },
        { name: '禁用', value: false }
      ],
      addForm: {
        id: '',
        pid: 0,
        permName: '',
        permCode: '',
        permLevel: '',
        permType: 1,
        routerPath: '',
        componentName: '',
        componentPath: '',
        sort: 99999,
        icon: '',
        enabled: true,
        cache: true,
        iFrame: false,
        hidden: false
      },
      addRules: {
        permCode: [
          { required: true, message: '请选择权限编码', trigger: 'blur' }
        ],
        permName: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ],
        routerPath: [
          { required: true, message: '请输入地址', trigger: 'blur' }
        ],
        componentName: [
          { required: true, message: '请输入组件名称', trigger: 'blur' }
        ],
        componentPath: [
          { required: true, message: '请输入组件地址', trigger: 'blur' }
        ],
        sort: [
          { required: true, message: '请输入权限排序', trigger: 'blur' }
        ],
        permLevel: [
          { required: true, message: '请选择访问级别', trigger: 'blur' }
        ],
        enabled: [
          { required: true, message: '请选择权限状态', trigger: 'blur' }
        ],
        pid: [
          { required: true, message: '请选择上级类目', trigger: 'blur' }
        ]
      }
    }
  },
  watch: {
    dataId(id) {
      this.addForm.id = id
    }
  },
  methods: {
    initData() {
      this.getMenus()
      this.getPermCodes()
      this.$nextTick(() => {
        this.getPermInfo()
      })
    },
    getMenus() {
      const param = {
        iFrame: false,
        needTop: true,
        onlyShowMenu: true
      }
      this.$mapi.perm.queryPermTree(param).then(res => {
        this.menus = res.data
      }).catch(_ => {
        this.addForm.pid = ''
        this.menus = []
      })
    },
    getPermCodes() {
      this.$mapi.perm.queryPermCodes().then(res => {
        this.permCodes = res.data
      }).catch(_ => {
        this.permCodes = []
      })
    },
    getPermInfo() {
      if (this.addForm.id != null && this.addForm.id !== '') {
        this.$mapi.perm.queryPermDetail({ permId: this.addForm.id }).then(res => {
          const { data } = res
          Object.keys(this.addForm).forEach(key => {
            this.addForm[key] = data[key]
          })
          if (this.addForm.pid == null) {
            this.addForm.pid = 0
          }

          this.permTypeChange(this.addForm.permType)
          this.oldPermCode = this.addForm.permCode
        }).catch(_ => {
          this.doClose()
        })
      }
    },
    normalizer(node) {
      // null, no array, empty
      const nodeData = {}
      if (node.children == null || !node.children.length || node.children.length < 1) {
        delete node.children
        nodeData['id'] = node['id']
        nodeData['label'] = node['permName']
      } else {
        nodeData['id'] = node['id']
        nodeData['label'] = node['permName']
        nodeData['children'] = node['children']
      }
      return nodeData
    },
    iconSelected(name) {
      this.addForm.icon = name
    },
    doClose(result = false) {
      this.addForm = {
        id: '',
        pid: 0,
        permName: '',
        permCode: '',
        permLevel: '',
        permType: 1,
        routerPath: '',
        componentName: '',
        componentPath: '',
        sort: 99999,
        icon: '',
        enabled: true,
        cache: true,
        iFrame: false,
        hidden: false
      }
      this.$refs['addForm'].resetFields()
      this.submitLoading = false
      this.menus = []
      this.permCodes = []
      this.oldPermCode = ''
      this.$emit('addOrEditDone', result)
    },
    handleClose() {
      if (!this.submitLoading) {
        this.$confirm('确认关闭？').then(_ => {
          this.doClose()
        }).catch(_ => {})
      }
    },
    cancel() {
      this.handleClose()
    },
    choosePermCode(code) {
      code = code == null || code === '' ? this.addForm.permCode : code
      if (code == null || code === '') {
        this.addForm.permName = ''
        this.addForm.permLevel = ''
      } else {
        this.$mapi.router.queryRouterByCode({ code }).then(res => {
          const { data } = res
          this.addForm.permName = data['name']
          this.addForm.permLevel = data['permLevel']
        })
      }
    },
    iFrameChange(isIFrame) {
      isIFrame = isIFrame == null || isIFrame === '' ? this.addForm.iFrame : isIFrame
      if (isIFrame != null && isIFrame.toString() === 'true') {
        this.addForm.permCode = ''
      }
    },
    permTypeChange(permType) {
      switch (permType.toString()) {
        case '1':
        case '2':
          this.addRules.permCode[0].required = false
          break
        case '3':
          this.addRules.permCode[0].required = true
          break
      }
    },
    dealEmptyData() {
      switch (this.addForm.permType) {
        case '1':
          this.addForm.cache = ''
          this.addForm.permCode = ''
          this.addForm.componentPath = ''
          this.addForm.componentName = ''
          break
        case '3':
          this.addForm.icon = ''
          this.addForm.iFrame = ''
          this.addForm.cache = ''
          this.addForm.hidden = ''
          this.addForm.componentPath = ''
          this.addForm.componentName = ''
          this.addForm.routerPath = ''
          break
      }
    },
    submit() {
      // deal empty data
      this.dealEmptyData()
      this.$refs['addForm'].validate((valid) => {
        if (valid) {
          this.submitLoading = true
          if (this.addForm.id == null || this.addForm.id === '') {
            // add
            this.$mapi.perm.addPerm(this.addForm).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.perm.editPerm(this.addForm).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          }
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
::v-deep .el-input-number .el-input__inner {
  text-align: left;
}
::v-deep .vue-treeselect__control,::v-deep .vue-treeselect__placeholder,::v-deep .vue-treeselect__single-value {
  height: 30px;
  line-height: 30px;
}
::v-deep .vue-treeselect--has-value .vue-treeselect__input {
  height: 30px;
  line-height: 30px;
}
</style>
