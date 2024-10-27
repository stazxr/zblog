<template>
  <div>
    <el-dialog title="文章审核" append-to-body :close-on-click-modal="false" :before-close="handleClose" :visible.sync="dialogVisible" width="720px">
      <el-form ref="addForm" :inline="true" :model="addForm" :rules="addRules" size="small" label-width="100px">
        <el-form-item label="文章列表: ">
          <el-table :data="tableData" height="250" border style="width: 560px">
            <el-table-column :show-overflow-tooltip="true" prop="title" label="标题" align="left" width="150px" />
            <el-table-column :show-overflow-tooltip="true" prop="createTime" label="提交时间" align="left" width="150px">
              <template v-slot="scope">
                <span v-if="scope.row['updateTime'] !== ''">{{ scope.row['updateTime'] }}</span>
                <span v-else>{{ scope.row['createTime'] }}</span>
              </template>
            </el-table-column>
            <el-table-column :show-overflow-tooltip="true" prop="articleStatus" label="状态" align="left" width="80px">
              <template v-slot="scope">
                <span v-if="scope.row['articleStatus'] === 1">草稿</span>
                <span v-else-if="scope.row['articleStatus'] === 2">待审核</span>
                <span v-else-if="scope.row['articleStatus'] === 3">待审核</span>
                <span v-else-if="scope.row['articleStatus'] === 4">审核拒绝</span>
                <span v-else-if="scope.row['articleStatus'] === 5">
                  {{ scope.row['articlePerm'] === 3 ? '私密文章' : '已发布' }}
                </span>
                <span v-else-if="scope.row['articleStatus'] === 6">临时下线</span>
                <span v-else-if="scope.row['articleStatus'] === 7">待整改</span>
                <span v-else-if="scope.row['articleStatus'] === 8">回收站</span>
                <span v-else-if="scope.row['articleStatus'] === 9">待发布</span>
                <span v-else>-</span>
              </template>
            </el-table-column>
            <el-table-column :show-overflow-tooltip="true" prop="desc" label="备注" align="left" />
          </el-table>
        </el-form-item>
        <el-form-item label="审核状态: " prop="status">
          <el-radio-group v-model="addForm.status">
            <el-radio v-for="item in statusEnums" :key="item.value" :label="item.value">{{ item.name }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审核意见: " prop="opinion">
          <el-input v-model="addForm.opinion" type="textarea" :rows="4" maxlength="100" show-word-limit style="width: 560px" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" @click="cancel">取 消</el-button>
        <el-button :loading="submitLoading" type="primary" @click="submit">提 交</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  props: {
    dialogVisible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      tableData: [],
      submitLoading: false,
      statusEnums: [
        { name: '审核通过', value: true },
        { name: '审核拒绝', value: false }
      ],
      addForm: {
        status: true,
        opinion: '审核通过'
      },
      addRules: {
        status: [
          { required: true, message: '请选择审核状态', trigger: 'blur' }
        ],
        opinion: [
          { required: true, message: '请填写审核意见', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    initData(data) {
      this.$nextTick(() => {
        this.tableData = data
      })
    },
    doClose(result = false) {
      this.addForm = {
        status: true,
        opinion: '审核通过'
      }
      this.tableData = []
      this.$refs['addForm'].resetFields()
      this.submitLoading = false
      this.$emit('auditDone', result)
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
    submit() {
      this.$refs['addForm'].validate((valid) => {
        if (valid) {
          const articleIds = []
          this.tableData.forEach(row => {
            articleIds.push(row.id)
          })

          const param = {
            status: this.addForm.status,
            opinion: this.addForm.opinion,
            articleIds: articleIds
          }

          this.submitLoading = true
          this.$mapi.article.auditArticle(param).then(_ => {
            this.$message.success('审核成功')
            this.doClose(true)
          }).finally(_ => {
            this.submitLoading = false
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
