<template>
  <div class="card-container">
    <el-card class="main-card">
      <div class="card-title">{{ this.$route.meta.title || this.$route.name }}</div>
      <div class="talk-container">
        <Editor ref="editor" v-model="talk.content" class="editor-wrapper" placeholder="说点什么吧" />
        <div class="operation-wrapper">
          <div class="left-wrapper">
            <el-popover placement="bottom-start" width="460" trigger="click">
              <span v-for="(value, key, index) of emojiList" :key="index" class="emoji-item" @click="addEmoji(key, value)">
                <img :src="value" :title="key" class="emoji" width="24" height="24" alt="">
              </span>
              <i slot="reference" class="iconfont biaoqing operation-btn" />
            </el-popover>
            <el-upload
              ref="upload"
              name="file"
              multiple
              :limit="maxUploadSize"
              :action="$store.state.api.fileUploadApi"
              :headers="headers"
              :on-exceed="handleExceed"
              :on-error="handleError"
              :on-success="handleSuccess"
              :before-upload="beforeUpload"
              :show-file-list="false"
            >
              <i class="iconfont tupian operation-btn" />
            </el-upload>
          </div>
          <div class="right-wrapper">
            <!-- 是否置顶 -->
            <el-switch v-model="talk.isTop" style="margin-right:16px" inactive-text="置顶" :active-value="true" :inactive-value="false" />
            <!-- 说说状态 -->
            <el-dropdown trigger="click" style="margin-right:16px" @command="handleCommand">
              <span class="talk-status">
                {{ dropdownTitle }}<i class="el-icon-arrow-down el-icon--right" />
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item v-for="(item, index) of statusList" :key="index" :command="item.status">
                  {{ item.desc }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <el-button type="primary" size="small" :loading="submitLoading" @click="submit">
              发布
            </el-button>
          </div>
        </div>
        <el-upload
          v-show="imagesList.length > 0"
          action="#"
          list-type="picture-card"
          :class="{hide:hideUpload}"
          :file-list="imagesList"
        >
          <i slot="default" class="el-icon-plus" />
          <div slot="file" slot-scope="{ file }">
            <img class="el-upload-list__item-thumbnail" :src="file['downloadUrl']" alt="">
            <span class="el-upload-list__item-actions">
              <span class="el-upload-list__item-delete" @click="handleRemove(file)">
                <i class="el-icon-delete" />
              </span>
            </span>
          </div>
        </el-upload>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as imageConversion from 'image-conversion'
import EmojiList from '@/assets/emoji/emoji'
import Editor from './template/Editor'
import { getToken } from '@/utils/token'
export default {
  name: 'AddOrEditTalk',
  components: {
    Editor
  },
  data() {
    return {
      submitLoading: false,
      emojiList: EmojiList,
      hideUpload: true,
      maxUploadSize: 9,
      headers: {
        Authorization: ''
      },
      imagesList: [],
      statusList: [
        { status: 1, desc: '公开' },
        { status: 2, desc: '私密' }
      ],
      talk: {
        id: null,
        content: '',
        isTop: false,
        status: 1,
        images: ''
      }
    }
  },
  computed: {
    dropdownTitle() {
      let desc = ''
      this.statusList.forEach(item => {
        if (item.status === this.talk.status) {
          desc = item.desc
        }
      })
      return desc
    }
  },
  created() {
    // TIPS: 本菜单需要配置关闭缓存
    this.$nextTick(() => {
      this.initData()
    })
  },
  methods: {
    initData() {
      if (this.$route.query.talkId && this.$route.query.talkId !== '') {
        this.$mapi.talk.queryTalkDetail({ talkId: this.$route.query.talkId }).then(({ data }) => {
          Object.keys(this.talk).forEach(key => {
            this.talk[key] = data[key]
          })

          this.imagesList = data['imagesFileList']
        })
      }
    },
    handleCommand(command) {
      this.talk.status = command
    },
    addEmoji(key, value) {
      this.$refs.editor.addText(
        "<img src= '" + value + "' width='24' height='24' alt='" + key + "' style='margin: 0 1px; vertical-align: text-bottom'/>"
      )
    },
    submit() {
      if (this.talk.content.trim() === '') {
        this.$message.error('请填写说说内容')
        return
      }

      // 转换图片
      this.submitLoading = true
      if (this.imagesList.length > 0) {
        let imgList = ''
        this.imagesList.forEach(item => {
          imgList = imgList + item['downloadUrl'] + ','
        })
        this.talk.images = imgList.length > 0 ? imgList.substring(0, imgList.length - 1) : ''
      } else {
        this.talk.images = ''
      }

      const param = {
        ... this.talk,
        imagesList: this.imagesList
      }

      this.$mapi.talk.addOrEditTalk(param).then(res => {
        this.$message.success(res.message)
        this.$router.push({ name: 'Talk' })

        // 清理数据
        this.$refs.editor.clear()
        this.talk = {
          id: null,
          content: '',
          isTop: 0,
          status: 1,
          images: ''
        }
        this.imagesList = []
      }).finally(_ => {
        this.submitLoading = false
      })
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 ${this.maxUploadSize} 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
    },
    handleRemove(file) {
      if (file && file.id) {
        this.$mapi.file.deleteFile({ fileId: file.id }).then(_ => {
          this.imagesList.forEach((item, index) => {
            if (item.id === file.id) {
              this.imagesList.splice(index, 1)
            }
          })
        })
      }
    },
    handleError(err, file, fileList) {
      try {
        this.$message.error(JSON.parse(err.message.toString()).message)
      } catch {
        this.$message.error('系统发生未知错误')
      }
    },
    handleSuccess(response, file, fileList) {
      if (response.code === 200) {
        // success
        this.imagesList.push(response.data[0])
      } else {
        // error
        this.$refs.upload.handleError(response, file)
      }
    },
    beforeUpload(file) {
      this.headers.Authorization = getToken()
      return new Promise(resolve => {
        if (file.size / 1024 < this.$config.UPLOAD_SIZE) {
          resolve(file)
        }

        // 压缩到200KB,这里的200就是要压缩的大小,可自定义
        imageConversion.compressAccurately(file, this.$config.UPLOAD_SIZE).then(res => {
          resolve(res)
        })
      })
    }
  }
}
</script>

<style scoped>
.talk-container {
  margin-top: 40px;
}
.editor-wrapper {
  min-height: 150px;
}
.operation-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
}
.operation-btn {
  cursor: pointer;
  color: #838383;
  font-size: 20px;
  margin-right: 12px;
}
.talk-status {
  cursor: pointer;
  font-size: 12px;
  color: #999;
}
.emoji {
  user-select: none;
  margin: 0.25rem;
  display: inline-block;
  vertical-align: middle;
}
.emoji-item {
  cursor: pointer;
  display: inline-block;
}
.emoji-item:hover {
  transition: all 0.2s;
  border-radius: 0.25rem;
  background: #dddddd;
}
.left-wrapper {
  display: flex;
  width: 50%;
}
::v-deep .hide .el-upload--picture-card {
  display: none;
}
</style>
