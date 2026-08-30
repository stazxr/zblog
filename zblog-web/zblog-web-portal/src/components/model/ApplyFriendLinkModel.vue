<template>
  <!-- 搜索框 -->
  <v-dialog v-model="applyFriendLinkFlag" max-width="760" persistent :fullscreen="isMobile">
    <v-card class="friend-link-dialog">
      <v-card-title class="friend-link-dialog-title">
        <v-icon left>
          mdi-account-plus-outline
        </v-icon>
        申请友链

        <v-spacer />

        <v-btn icon @click="applyFriendLinkFlag = false">
          <v-icon>
            mdi-close
          </v-icon>
        </v-btn>
      </v-card-title>

      <v-divider />

      <v-card-text class="friend-link-dialog-content">
        <div class="apply-tip">
          <v-icon size="20">
            mdi-information-outline
          </v-icon>
          <span>
            请先添加本站友链信息，再提交申请。审核通过后将展示在友链页面。
          </span>
        </div>

        <v-form ref="friendLinkForm" v-model="friendLinkValid">
          <v-text-field v-model="friendLinkForm.name" label="网站名称" outlined dense :rules="[rules.required]" />
          <v-text-field v-model="friendLinkForm.url" label="网站地址" outlined dense :rules="[rules.required, rules.url]" />
          <v-text-field v-model="friendLinkForm.logo" label="网站封面" outlined dense :rules="[rules.required, rules.url]" />
          <v-textarea v-model="friendLinkForm.description" label="网站简介" outlined rows="3" counter="200" :rules="[rules.required]" />
        </v-form>
      </v-card-text>

      <v-divider />

      <v-card-actions class="friend-link-dialog-actions">
        <v-spacer />
        <v-btn text @click="applyFriendLinkFlag = false">
          取消
        </v-btn>
        <v-btn color="primary" depressed :loading="friendLinkSubmitting" @click="submitFriendLink">
          提交申请
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  data() {
    return {
      // 表单是否有效
      friendLinkValid: false,

      // 提交状态
      friendLinkSubmitting: false,

      // 友链申请表单
      friendLinkForm: {
        name: '',
        url: '',
        logo: '',
        description: ''
      },

      rules: {
        required: value => !!value || '不能为空',
        url: value => {
          if (!value) {
            return true
          }

          const pattern = /^https?:\/\/.+/i

          return pattern.test(value) || '请输入正确的网址'
        }
      }
    }
  },
  computed: {
    applyFriendLinkFlag: {
      set(value) {
        this.$store.state.applyFriendLinkFlag = value
      },
      get() {
        return this.$store.state.applyFriendLinkFlag
      }
    },
    isMobile() {
      return this.$store.state.isMobile
    }
  },
  watch: {
    applyFriendLinkFlag(value) {
      if (value) {
        this.resetFriendLinkForm()
      }
    }
  },
  methods: {
    /**
     * 重置友链申请表单
     */
    resetFriendLinkForm() {
      this.friendLinkForm = {
        name: '',
        url: '',
        logo: '',
        description: ''
      }

      this.friendLinkValid = false
      this.$nextTick(() => {
        if (this.$refs.friendLinkForm) {
          this.$refs.friendLinkForm.resetValidation()
        }
      })
    },
    /**
     * 提交友链申请
     */
    async submitFriendLink() {
      const valid = this.$refs.friendLinkForm.validate()
      if (!valid) {
        return
      }

      try {
        this.friendLinkSubmitting = true
        await this.$mapi.portal.applyFriendLink(this.friendLinkForm)
        this.$toast({ type: 'success', message: '申请已提交，请等待审核' })
        this.applyFriendLinkFlag = false
      } catch (error) {
        console.error('提交友链申请失败', error)
        this.$toast({ type: 'error', message: error.message })
      } finally {
        this.friendLinkSubmitting = false
      }
    }
  }
}
</script>

<style scoped>
.apply-tip {
  margin: 6px 0;
}
</style>
