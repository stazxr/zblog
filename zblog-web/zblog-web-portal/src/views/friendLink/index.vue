<template>
  <div>
    <div class="banner" :style="cover">
      <h1 class="banner-title">友情链接</h1>
    </div>
    <v-card class="blog-container">
      <div class="friend-tip">
        这里汇聚优秀博客、个人站点以及我的一些项目足迹。
      </div>
      <friend-group
        title="精选友链"
        icon="❤️"
        desc="我长期关注并推荐的优秀网站"
        :list="friendData"
      />
      <friend-group
        title="友情链接"
        icon="🔗"
        desc="互相认可的博客伙伴"
        :list="friendData"
      />
      <friend-group
        title="我的足迹"
        icon="🚀"
        desc="我的项目、站点以及开源衍生作品"
        :list="friendData"
      />
      <v-divider class="my-10" />
      <!-- 申请 -->
      <div class="section-title">
        申请友链
      </div>
      <v-alert
        dense
        text
        type="info"
      > 填写网站信息，审核通过后将在友链列表展示。
      </v-alert>
      <v-form
        ref="form"
        v-model="valid"
        class="apply-form"
      >
        <v-text-field
          v-model="form.name"
          label="网站名称"
          placeholder="例如：孙涛博客"
          outlined
          dense
          :rules="[rules.required]"
        />
        <v-text-field
          v-model="form.linkUrl"
          label="网站地址"
          placeholder="https://www.xxx.com"
          outlined
          dense
          :rules="[rules.required,rules.url]"
        />
        <v-text-field
          v-model="form.headUrl"
          label="网站头像"
          placeholder="https://xxx.com/avatar.png"
          outlined
          dense
          :rules="[rules.url]"
        />
        <v-textarea
          v-model="form.linkRemark"
          label="网站介绍"
          placeholder="简单介绍一下你的网站"
          outlined
          rows="3"
          :rules="[rules.required]"
        />
        <v-text-field
          v-model="form.contact"
          label="联系方式"
          placeholder="QQ / 邮箱 / 微信"
          outlined
          dense
        />
        <v-btn
          color="primary"
          :loading="submitLoading"
          :disabled="!valid"
          @click="submitApply"
        > 提交申请
        </v-btn>
      </v-form>
      <v-divider class="divider"/>
      <!-- 说明 -->
      <div class="section-title">
        <svg class="iconfont_svg" aria-hidden="true">
          <use xlink:href="#icon-xiaoxi"/>
        </svg>
        友链说明
      </div>
      <blockquote>
        <div>
          1. 网站内容需符合互联网规范
        </div>
        <div>
          2. 网站能够正常访问
        </div>
        <div>
          3. 不接受纯广告、采集站点
        </div>
        <div>
          4. 审核通过后会展示友链
        </div>
        <div>
          5. 如长期无法访问，本站会移除链接
        </div>
      </blockquote>
    </v-card>
  </div>
</template>
<script>
import { getPageRandomCover } from '@/utils/theme'
import FriendGroup from '@/components/friend-group/FriendGroup'
export default {
  name: 'FriendLink',
  components: {
    FriendGroup
  },
  data() {
    return {
      cover: null,
      friendData: [],
      valid: false,
      submitLoading: false,
      form: {
        name: '',
        linkUrl: '',
        headUrl: '',
        linkRemark: '',
        contact: ''
      },
      rules: {
        required: v => !!v || '不能为空',
        url: v => {
          if (!v) {
            return true
          }
          return /^https?:\/\/.+/.test(v) || '请输入正确的网址'
        }
      }
    }
  },
  created() {
    this.cover = getPageRandomCover(this.$store.state.pages, 'friendLink')
  },
  mounted() {
    this.$nextTick(() => {
      this.loadFriendLink()
    })
  },
  methods: {
    loadFriendLink() {
      this.$mapi.portal.queryFriendLinkList().then(({ data }) => {
        this.friendData = data
      }).catch(e => {
        console.log('load friend link error', e)
        this.$toast({ type: 'error', message: '友链列表加载失败...' })
      })
    }
  }
}
</script>
<style scoped>
.friend-tip {
  padding: 20px;
  background: #f5f7fa;
  border-radius: 10px;
  margin-bottom: 35px;
  color: #666;
}
.section-title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 20px;
}
</style>
