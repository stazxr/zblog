<template>
  <div>
    <el-dialog
      append-to-bodyq
      :title="dialogTitle"
      :fullscreen="isMobile"
      :close-on-click-modal="false"
      :before-close="handleClose"
      :visible.sync="dialogVisible"
      width="580px"
    >
      <el-form ref="addOrEditForm" :inline="!isMobile" :model="formData" :rules="formRules" label-width="80px">
        <el-form-item label="权限类型" prop="permType">
          <el-radio-group
            v-model="formData.permType"
            :disabled="formData.id != null && formData.id !== ''"
            :style="isMobile ? '' : 'width: 178px;'"
            @change="permTypeChangeEvent"
          >
            <el-radio-button label="1">目录</el-radio-button>
            <el-radio-button label="2">菜单</el-radio-button>
            <el-radio-button label="3">按钮</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <!-- 当权限类型不为按钮的时候，显示图标 -->
        <el-form-item v-show="formData.permType.toString() !== '3'" label="菜单图标" prop="icon">
          <el-popover placement="bottom-start" trigger="click" :width="isMobile ? '100%' : '450'" @show="$refs.iconSelect.reset()">
            <icon-select ref="iconSelect" :style="[isMobile ? { 'width': '100%' } : { 'width': '450px' }]" @selected="iconSelectedEvent" />
            <el-input
              slot="reference"
              v-model="formData.icon"
              :style="[isMobile ? { } : { 'width': '450px' }]"
              placeholder="点击选择图标"
              readonly
            >
              <svg-icon v-if="formData.icon" slot="prefix" :icon-class="formData.icon" class="el-input__icon" style="height: 32px;width: 16px" />
              <i v-else slot="prefix" class="el-icon-search el-input__icon" />
            </el-input>
          </el-popover>
        </el-form-item>
        <!-- 当权限类型不为按钮的时候，显示是否外链 -->
        <el-form-item v-show="formData.permType.toString() !== '3'" label="外链菜单" prop="iFrame">
          <el-radio-group v-model="formData.iFrame" :disabled="formData.id != null && formData.id !== ''" @change="iFrameChangeEvent">
            <el-radio-button label="true">是</el-radio-button>
            <el-radio-button label="false">否</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <!-- 当权限类型不为按钮的时候，显示是否隐藏 -->
        <el-form-item v-show="formData.permType.toString() !== '3'" label="菜单可见" prop="hidden">
          <el-radio-group v-model="formData.hidden">
            <el-radio-button label="false">是</el-radio-button>
            <el-radio-button label="true">否</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <!-- 当权限类型是菜单的时候，显示是否缓存 -->
        <el-form-item v-show="formData.permType.toString() === '2'" label="菜单缓存" prop="cacheable">
          <el-radio-group v-model="formData.cacheable">
            <el-radio-button label="true">是</el-radio-button>
            <el-radio-button label="false">否</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <!-- 当权限类型不是目录时，显示权限编码 -->
        <el-form-item v-if="formData.permType.toString() !== '1'" label="权限编码" prop="permCode">
          <el-select
            v-model="formData.permCode"
            :title="formData.permCode"
            filterable
            clearable
            :disabled="formData.iFrame != null && formData.iFrame.toString() === 'true'"
            :style="isMobile ? '' : 'width: 178px;'"
            placeholder="权限编码"
            @change="permCodeChangeEvent"
          >
            <el-option
              v-for="item in permCodes"
              :key="item.value"
              :label="item.value"
              :value="item.value"
              :title="item.name"
              :disabled="item.disabled && item.value !== oldPermCode"
              :style="isMobile ? '' : 'width: 178px;'"
            />
          </el-select>
        </el-form-item>
        <!-- 菜单名称/按钮名称 -->
        <el-form-item :label="formData.permType.toString() !== '3' ? '菜单名称' : '按钮名称'" prop="permName">
          <el-input
            v-model="formData.permName"
            :placeholder="formData.permType.toString() !== '3' ? '' : ''"
            :style="isMobile ? '' : 'width: 178px;'"
            maxlength="20"
            show-word-limit
          />
        </el-form-item>
        <!-- 当权限类型不是按钮时，显示路由地址 -->
        <el-form-item v-if="formData.permType.toString() !== '3'" label="路由地址" prop="routerPath">
          <el-input v-model="formData.routerPath" placeholder="路由地址" :style="isMobile ? '' : 'width: 178px;'" />
        </el-form-item>
        <!-- 权限排序 -->
        <el-form-item label="权限排序" prop="sort">
          <el-input-number
            v-model.number="formData.sort"
            :min="0"
            :max="99999"
            :style="isMobile ? 'width: 100%;' : 'width: 178px;'"
            step-strictly
            controls-position="right"
          />
        </el-form-item>
        <!-- 当权限类型是菜单时，且不为外链，显示组件名称 -->
        <el-form-item v-if="(formData.iFrame == null || formData.iFrame.toString() === 'false') && formData.permType.toString() === '2'" label="组件名称" prop="componentName">
          <el-input v-model="formData.componentName" :style="isMobile ? '' : 'width: 178px;'" placeholder="前端 Vue 组件的名称" />
        </el-form-item>
        <!-- 当权限类型是菜单时，且不为外链，显示组件路径 -->
        <el-form-item v-if="(formData.iFrame == null || formData.iFrame.toString() === 'false') && formData.permType.toString() === '2'" label="组件路径" prop="componentPath">
          <el-input v-model="formData.componentPath" :style="isMobile ? '' : 'width: 178px;'" placeholder="前端 Vue 组件在 views 目录下的相对路径" />
        </el-form-item>
        <!-- 访问级别 -->
        <el-form-item label="访问级别" prop="permLevel">
          <el-select v-model="formData.permLevel" :style="isMobile ? '' : 'width: 178px;'" placeholder="访问级别" clearable>
            <el-option v-for="item in permLevels" :key="item.value" :label="item.name" :value="item.value" :title="item.desc" />
          </el-select>
        </el-form-item>
        <!-- 权限状态 -->
        <el-form-item label="权限状态" prop="enabled">
          <el-select v-model="formData.enabled" :style="isMobile ? '' : 'width: 178px;'" placeholder="是否启用">
            <el-option v-for="item in permEnabled" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="上级类目" prop="pid">
          <tree-select
            v-model="formData.pid"
            :options="menus"
            :normalizer="normalizer"
            :style="isMobile ? '' : 'width: 450px;'"
            :searchable="false"
            :max-height="200"
            placeholder="选择上级类目"
            no-options-text="列表为空"
            no-results-text="搜索结果为空"
          />
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
import TreeSelect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
export default {
  components: {
    IconSelect,
    TreeSelect
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
      menus: [], // 菜单树
      permCodes: [], // 权限编码列表
      oldPermCode: '', // 当编辑时，原权限的权限编码
      permLevels: [
        { name: '公开', value: 1, desc: '不需要登录，可以直接访问' },
        { name: '认证', value: 2, desc: '需要登录才可以访问' },
        { name: '授权', value: 4, desc: '需要授权才可以访问' }
      ],
      permEnabled: [
        { name: '启用', value: true },
        { name: '禁用', value: false }
      ],
      formData: {
        id: null,
        pid: 0,
        permName: null,
        permCode: null,
        permType: 1, // 权限类型，默认目录
        permLevel: null,
        routerPath: null,
        componentName: null,
        componentPath: null,
        sort: 99999,
        icon: null,
        enabled: true,
        cacheable: null,
        iFrame: false,
        hidden: false
      },
      formRules: {
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
  computed: {
    isMobile() {
      return this.$store.state.app.device === 'mobile'
    }
  },
  watch: {
    dataId(id) {
      this.formData.id = id
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
        if (this.menus.length === 0) {
          this.formData.pid = null
        }
      }).catch(_ => {
        this.formData.pid = null
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
      if (this.formData.id != null && this.formData.id !== '') {
        this.$mapi.perm.queryPermDetail({ permId: this.formData.id }).then(res => {
          const { data } = res
          Object.keys(this.formData).forEach(key => {
            this.formData[key] = data[key]
          })
          if (this.formData.pid == null) {
            this.formData.pid = 0
          }

          this.permTypeChangeEvent(this.formData.permType)
          this.oldPermCode = this.formData.permCode
        }).catch(_ => {
          this.doClose()
        })
      }
    },
    permTypeChangeEvent(permType) {
      switch (permType.toString()) {
        case '1':
        case '2':
          // 目录和菜单：权限编码非必填
          this.formRules.permCode[0].required = false
          if (this.formData.cacheable === null) {
            this.formData.cacheable = true // 菜单默认开启缓存
          }
          break
        case '3':
          // 按钮：权限编码必填
          this.formRules.permCode[0].required = true
          break
      }
    },
    iconSelectedEvent(name) {
      this.formData.icon = name
    },
    iFrameChangeEvent(isIFrame) {
      isIFrame = isIFrame == null || isIFrame === '' ? this.formData.iFrame : isIFrame
      if (isIFrame != null && isIFrame.toString() === 'true') {
        this.formData.permCode = null
        this.formData.componentName = null
        this.formData.componentPath = null
      }
    },
    permCodeChangeEvent(code) {
      code = code == null || code === '' ? this.formData.permCode : code
      if (code == null || code === '') {
        this.formData.permName = null
        this.formData.permLevel = null
      } else {
        this.$mapi.perm.queryResourceByPermCode({ 'permCode': code }).then(res => {
          const { data } = res
          this.formData.permName = data['resourceName']
          this.formData.permLevel = data['resourceLevel']
        }).catch(_ => {
          this.formData.permCode = null
        })
      }
    },
    normalizer(node) {
      // null, no array, empty
      const nodeData = { }
      if (node.children == null || !node.children.length || node.children.length === 0) {
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
    submit() {
      console.log('this.f', this.formData)
      this.dealEmptyData() // 空数据处理
      this.$refs.addOrEditForm.validate((valid) => {
        if (valid) {
          this.submitLoading = true
          if (this.formData.id == null || this.formData.id === '') {
            // add
            this.$mapi.perm.addPerm(this.formData).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          } else {
            // edit
            this.$mapi.perm.editPerm(this.formData).then(res => {
              this.$message.success(res.message)
              this.doClose(true)
            }).finally(_ => {
              this.submitLoading = false
            })
          }
        }
      })
    },
    dealEmptyData() {
      switch (this.formData.permType) {
        case '1': // 目录
          this.formData.cacheable = null // 是否缓存为 NULL
          this.formData.permCode = null // 权限编码为 NULL
          this.formData.componentName = null // 组件名称为 NULL
          this.formData.componentPath = null // 组件路径为 NULL
          break
        case '2': // 菜单
          if (this.formData.iFrame) {
            this.formData.permCode = null // 外链权限编码为 NULL
            this.formData.componentName = null // 外链组件名称为 NULL
            this.formData.componentPath = null // 外链组件路径为 NULL
          }
          break
        case '3': // 按钮
          this.formData.icon = null // 图标为 NULL
          this.formData.iFrame = null // 是否外链为 NULL
          this.formData.cacheable = null // 是否缓存为 NULL
          this.formData.hidden = null // 是否隐藏为 NULL
          this.formData.componentName = null // 组件名称为 NULL
          this.formData.componentPath = null // 组件路径为 NULL
          this.formData.routerPath = null // 路由地址为 NULL
          break
      }
    },
    cancel() {
      this.handleClose()
    },
    handleClose() {
      if (!this.submitLoading) {
        this.$confirm('是否确认关闭弹窗？').then(_ => {
          this.doClose()
        }).catch(_ => {})
      }
    },
    doClose(result = false) {
      this.formData = {
        id: null,
        pid: 0,
        permName: null,
        permCode: null,
        permType: 1, // 权限类型，默认目录
        permLevel: null,
        routerPath: null,
        componentName: null,
        componentPath: null,
        sort: 99999,
        icon: null,
        enabled: true,
        cacheable: null,
        iFrame: false,
        hidden: false
      }
      this.$refs.addOrEditForm.resetFields()
      this.submitLoading = false
      this.menus = []
      this.permCodes = []
      this.oldPermCode = ''
      this.$emit('addOrEditDone', result)
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
