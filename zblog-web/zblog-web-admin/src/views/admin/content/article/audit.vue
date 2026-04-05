<template>
  <div class="app-container">
    <div class="head-container">
      <div class="head-nav-tabs">
        <span class="article-status">状态</span>
        <ul class="nav nav-pills">
          <li class="nav-item">
            <span ref="tab-all" class="nav-link" :class="{ 'active': isAllActive }" @click="statusTabChange(1)"> 全部({{ countInfo.allCount }}) </span>
          </li>
          <li class="nav-item">
            <span ref="tab-published" class="nav-link" :class="{ 'active': isPublishedActive }" @click="statusTabChange(2)"> 已发布({{ countInfo.publishedCount }}) </span>
          </li>
          <li class="nav-item">
            <span ref="tab-audit" class="nav-link" :class="{ 'active': isAuditActive }" @click="statusTabChange(3)"> 待审核({{ countInfo.auditCount }}) </span>
          </li>
          <li class="nav-item">
            <span ref="tab-publish" class="nav-link" :class="{ 'active': isPublishActive }" @click="statusTabChange(5)"> 待发布({{ countInfo.publishCount }}) </span>
          </li>
          <li class="nav-item">
            <span ref="tab-deal" class="nav-link" :class="{ 'active': isDealActive }" @click="statusTabChange(4)"> 待作者处理({{ countInfo.dealCount }}) </span>
          </li>
        </ul>
      </div>
      <div>
        <el-input v-model="filters.title" clearable placeholder="文章标题" style="width: 180px" class="filter-item" @keyup.enter.native="search" />
        <el-input v-model="filters.keywords" clearable placeholder="关键字（标签名称）" style="width: 180px" class="filter-item" @keyup.enter.native="search" />
        <el-input v-model="filters.author" clearable placeholder="作者（昵称、用户名）" style="width: 180px" class="filter-item" @keyup.enter.native="search" />
        <el-select v-model="filters.categoryId" clearable placeholder="文章分类" style="width: 150px" class="filter-item">
          <el-option-group v-for="group in categoryList" :key="group.id" :label="group.name">
            <el-option v-for="item in group.children" :key="item.id" :value="item.id" :label="item.name" />
          </el-option-group>
        </el-select>
        <el-select v-model="filters.articleType" clearable placeholder="文章类型" style="width: 150px" class="filter-item">
          <el-option label="原创" value="1" />
          <el-option label="转载" value="2" />
          <el-option label="翻译" value="3" />
        </el-select>
        <span>
          <el-button class="filter-item" size="small" type="success" icon="el-icon-search" @click="search">查询</el-button>
          <el-button class="filter-item" size="small" type="warning" icon="el-icon-refresh-left" @click="resetSearch">重置</el-button>
        </span>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-show="filters.tagStatus === 3" v-perm="['auditArticle']" size="small" type="primary" @click="batchAuditArticle()">
            审核
          </el-button>
          <el-button v-show="false" v-perm="['pushArticle']" size="small" type="info" @click="pushArticle()">
            推送
          </el-button>
          <el-button v-show="false" v-perm="['offlineArticle']" size="small" type="danger" @click="offlineArticle()">
            下线
          </el-button>
        </span>
      </div>
    </div>

    <div class="components-container">
      <el-table v-if="!showEmpty" ref="articleTable" v-loading="tableLoading" :data="tableData" border style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="45" />
        <el-table-column :show-overflow-tooltip="true" prop="articleImgLinkList" label="文章封面" align="center" width="180" fixed="left">
          <template v-slot="scope">
            <el-image
              class="article-cover"
              :src="scope.row['coverImageType'] === 3 ? articleDefaultImg : scope.row['articleImgLinkList'] && scope.row['articleImgLinkList'].length > 0 ? scope.row['articleImgLinkList'][0] : ''"
              :preview-src-list="scope.row['articleImgLinkList']"
            >
              <div slot="error" class="image-slot">
                <span v-if="scope.row['coverImageType'] === 5">无封面</span>
                <span v-else>加载失败</span>
              </div>
            </el-image>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="title" label="文章标题" align="left" width="200" fixed="left" />
        <el-table-column :show-overflow-tooltip="true" prop="articleType" label="文章类型" width="80" align="center">
          <template v-slot="scope">
            <span v-if="scope.row['articleType'] === 1" style="font-size: 12px">原创</span>
            <span v-else-if="scope.row['articleType'] === 2">
              <el-tooltip :content="scope.row['reprintLink']" placement="top" effect="light">
                <el-link type="primary" :href="scope.row['reprintLink']" :disabled="scope.row['reprintLink'] === ''" title="ss" :underline="true" target="_blank" style="font-size: 12px">
                  转载
                </el-link>
              </el-tooltip>
            </span>
            <span v-else-if="scope.row['articleType'] === 3">
              <el-tooltip :content="scope.row['reprintLink']" placement="top" effect="light">
                <el-link type="primary" :href="scope.row['reprintLink']" :disabled="scope.row['reprintLink'] === ''" :underline="true" target="_blank" style="font-size: 12px">
                  翻译
                </el-link>
              </el-tooltip>
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="categoryName" label="文章分类" width="100" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="tagList" label="文章标签" width="250" align="center">
          <template v-slot="scope">
            <el-tag v-for="item of scope.row['tagList']" :key="item.id" :type="item.type === 2 ? 'info' : ''" style="margin-right:0.2rem;margin-top:0.2rem;">
              {{ item.name }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="viewCount" label="浏览量" width="70" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="likeCount" label="点赞量" width="70" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="commentCount" label="评论数" width="70" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="tagList" label="评论状态" width="100" align="center">
          <template v-slot="scope">
            <el-tag v-if="scope.row['commentFlag']" effect="dark">开启评论</el-tag>
            <el-tag v-else type="warning" effect="dark">关闭评论</el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="articleStatus" label="文章状态" width="100" align="center">
          <template v-slot="scope">
            <el-tag v-if="scope.row['articleStatus'] === 1" type="info" effect="dark">草稿</el-tag>
            <el-tag v-else-if="scope.row['articleStatus'] === 2" effect="dark">待审核</el-tag>
            <el-tag v-else-if="scope.row['articleStatus'] === 3" effect="dark">待审核</el-tag>
            <el-tag v-else-if="scope.row['articleStatus'] === 4" type="warning" effect="dark">审核拒绝</el-tag>
            <el-tag v-else-if="scope.row['articleStatus'] === 5" type="success" effect="dark">
              {{ scope.row['articlePerm'] === 3 ? '私密文章' : '已发布' }}
            </el-tag>
            <el-tag v-else-if="scope.row['articleStatus'] === 6" type="danger" effect="dark">临时下线</el-tag>
            <el-tag v-else-if="scope.row['articleStatus'] === 7" type="danger" effect="dark">待整改</el-tag>
            <el-tag v-else-if="scope.row['articleStatus'] === 8" type="info" effect="dark">回收站</el-tag>
            <el-tag v-else-if="scope.row['articleStatus'] === 9" effect="dark">待发布</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="authorNickname" label="作者" width="100" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="createTime" label="最后修改时间" width="150" align="center">
          <template v-slot="scope">
            <span v-if="scope.row['updateTime'] !== ''">{{ scope.row['updateTime'] }}</span>
            <span v-else>{{ scope.row['createTime'] }}</span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="desc" label="备注" width="150" align="center" />
        <el-table-column label="操作" align="center" width="200" fixed="right">
          <template v-slot="scope">
            <el-button-group>
              <el-button type="info" size="mini" @click="showArticleDetail(scope.row)">预览</el-button>
              <el-button v-show="scope.row['articleStatus'] === 2 || scope.row['articleStatus'] === 3" v-perm="['auditArticle']" type="primary" size="mini" @click="auditArticle(scope.row)">审核</el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <el-result v-if="showEmpty" title="空空如也" sub-title="这里没有需要处理的事务。">
        <template slot="icon">
          <el-image :src="emptyImg" style="width: 200px;" />
        </template>
      </el-result>
    </div>

    <div class="pagination-container">
      <el-pagination
        :total="total"
        :current-page="page"
        :page-size="pageSize"
        :page-sizes="[5, 10, 15, 20, 25]"
        style="margin-top: 8px;"
        :hide-on-single-page="true"
        layout="total, prev, pager, next, sizes"
        @size-change="sizeChange"
        @current-change="pageChange"
      />
    </div>

    <auditDialog ref="auditDialogRef" :dialog-visible="auditDialogVisible" @auditDone="auditDone" />

    <el-dialog :visible.sync="previewArticleDialogVisible" title="预览" width="900px">
      <previewArticle ref="previewArticleRef" />
      <div slot="footer" class="dialog-footer">
        <el-button type="default" @click="closePreviewArticleDialog">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import EmptyImg from '@/assets/images/empty.png'
import auditDialog from '@/views/admin/web/article/template/auditDialog'
import previewArticle from '@/views/admin/web/article/template/previewArticle'
export default {
  name: 'ArticleAudit',
  components: {
    auditDialog, previewArticle
  },
  data() {
    return {
      categoryList: [],
      articleDefaultImg: '',
      emptyImg: EmptyImg,
      showEmpty: false,
      filters: {
        tagStatus: 1,
        title: '',
        keywords: '',
        author: '',
        categoryId: '',
        articleType: ''
      },
      countInfo: {
        allCount: 0,
        publishedCount: 0,
        auditCount: 0,
        dealCount: 0,
        publishCount: 0
      },
      tableData: [],
      selectRows: [],
      tableLoading: false,
      total: 0,
      page: 1,
      pageSize: 5,
      auditDialogVisible: false,
      previewArticleDialogVisible: false
    }
  },
  computed: {
    isAllActive() {
      return this.filters.tagStatus === 1
    },
    isPublishedActive() {
      return this.filters.tagStatus === 2
    },
    isAuditActive() {
      return this.filters.tagStatus === 3
    },
    isDealActive() {
      return this.filters.tagStatus === 4
    },
    isPublishActive() {
      return this.filters.tagStatus === 5
    }
  },
  mounted() {
    this.getDefaultArticleImg()
    this.queryCategoryTree()
    this.listTableData()
  },
  activated() {
    if (this.$route.query.refresh) {
      this.listTableData()
    }
  },
  methods: {
    handleSelectionChange(val) {
      this.selectRows = val
    },
    statusTabChange(status) {
      this.page = 1
      this.filters.tagStatus = status
      this.listTableData()
    },
    getDefaultArticleImg() {
      this.$mapi.article.queryArticleDefaultImg().then(res => {
        const { data } = res
        this.articleDefaultImg = data || ''
      })
    },
    queryCategoryTree() {
      this.$mapi.article.queryCategoryTree().then(({ data }) => {
        this.categoryList = data
      }).catch(_ => {
        this.categoryList = []
      })
    },
    search() {
      this.page = 1
      this.listTableData()
    },
    resetSearch() {
      this.filters.tagStatus = 1
      this.filters.title = ''
      this.filters.keywords = ''
      this.filters.author = ''
      this.filters.categoryId = ''
      this.filters.articleType = ''

      this.page = 1
      this.listTableData()
    },
    listTableData() {
      const param = {
        ... this.filters,
        page: this.page,
        pageSize: this.pageSize
      }

      this.tableData = []
      this.showEmpty = false
      this.tableLoading = true
      this.$mapi.article.pageAuditArticleList(param).then(({ data }) => {
        const { dataList, countInfo } = data
        this.tableData = dataList.list
        this.total = dataList.total

        Object.keys(this.countInfo).forEach(key => {
          this.countInfo[key] = countInfo[key] || 0
        })
      }).catch(_ => {
        this.tableData = []
        this.total = 0

        Object.keys(this.countInfo).forEach(key => {
          this.countInfo[key] = 0
        })
      }).finally(() => {
        this.tableLoading = false
        this.showEmpty = this.tableData.length === 0
      })
    },
    showArticleDetail(row) {
      this.previewArticleDialogVisible = true
      this.$nextTick(() => {
        this.$refs.previewArticleRef.initData(2, row.id)
      })
    },
    closePreviewArticleDialog() {
      this.previewArticleDialogVisible = false
    },
    auditArticle(row) {
      const data = []
      data.push(row)

      this.auditDialogVisible = true
      this.$refs.auditDialogRef.initData(data)
    },
    batchAuditArticle() {
      if (this.selectRows.length === 0) {
        this.$message.error('请选择需要审核的文章')
        return
      }

      const data = []
      this.selectRows.forEach(row => {
        data.push(row)
      })

      this.auditDialogVisible = true
      this.$refs.auditDialogRef.initData(data)
    },
    auditDone(result = false) {
      this.auditDialogVisible = false
      if (result) {
        this.listTableData()
      }
    },
    pushArticle() {

    },
    offlineArticle() {

    },
    sizeChange(size) {
      this.page = 1
      this.pageSize = size
      this.listTableData()
    },
    pageChange(page) {
      this.page = page
      this.listTableData()
    }
  }
}
</script>

<style scoped>
.head-nav-tabs {
  position: relative;
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -webkit-box-align: center;
  -ms-flex-align: center;
  align-items: center;
  height: 30px;
  margin-top: -10px;
  margin-bottom: 15px;
}
.head-nav-tabs .article-status {
  font-size: 14px;
  color: #999aaa;
  line-height: 36px;
  height: 30px;
  margin-right: 24px;
}
.head-nav-tabs .nav {
  display: -webkit-box;
  display: -ms-flexbox;
  display: flex;
  -ms-flex-wrap: wrap;
  flex-wrap: wrap;
  padding-left: 0;
  margin-bottom: 0;
  list-style: none;
  color: #555666;
}
.head-nav-tabs .nav li {
  padding: 0;
  margin: 0;
  list-style: none;
}
.head-nav-tabs .nav-link.active {
  color: #222226;
  background-color: transparent;
  border-radius: 0;
  font-weight: 500;
  position: relative;
}
.head-nav-tabs .nav-link:hover {
  color: #222226;
  background-color: transparent;
  border-radius: 0;
  font-weight: 500;
  position: relative;
}
.head-nav-tabs .nav-link {
  margin-right: 24px;
  font-size: .875rem;
  color: #999;
  padding: 0 0 10px;
  display: block;
  cursor: pointer;
}

.article-cover {
  position: relative;
  width: 100%;
  height: 90px;
  border-radius: 4px;
}

::v-deep .image-slot, .demo-image__placeholder .image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 14px;
}
</style>
