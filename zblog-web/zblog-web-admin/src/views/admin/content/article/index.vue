<template>
  <div class="app-container">
    <div class="head-container">
      <div class="head-nav-tabs">
        <span v-show="!isMobile" class="article-status">状态</span>
        <ul class="nav nav-pills">
          <li v-for="tab in statusTabs" :key="tab.key" class="nav-item">
            <span :ref="'tab-' + tab.field" class="nav-link" :class="{ active: filters.tagStatus === tab.key }" @click="statusTabChange(tab.key)">
              {{ tab.label }}({{ articleCountInfo[tab.field] || 0 }})
            </span>
          </li>
        </ul>
      </div>
      <div class="search-opts">
        <muses-search-form ref="searchForm" :model="filters" label-position="right" label-width="0" :offset="0" :item-width="140">
          <muses-search-form-item label="" prop="search-title">
            <el-input id="search-title" v-model="filters.title" clearable placeholder="文章标题" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item label="" prop="search-slug">
            <el-input id="search-slug" v-model="filters.slug" clearable placeholder="SLUG" @keyup.enter.native="search" />
          </muses-search-form-item>
          <muses-search-form-item btn btn-open-name="" btn-close-name="">
            <el-button type="success" @click="search()">查 询</el-button>
            <el-button type="warning" @click="resetSearch()">重 置</el-button>
          </muses-search-form-item>
        </muses-search-form>
      </div>
      <div class="crud-opts">
        <span class="crud-opts-left">
          <el-button v-perm="['TAGSA001']" type="success" @click="addArticle">新增</el-button>
        </span>
      </div>
    </div>
    <div class="components-container">
      <el-table
        ref="articleTable"
        v-loading="tableLoading"
        :data="tableData"
        :header-cell-style="{background:'#FAFAFA'}"
        row-key="id"
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="45" />
        <el-table-column :show-overflow-tooltip="true" prop="articleImgLinkList" label="文章封面" align="center" width="180" fixed="left">
          <template slot-scope="scope">
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
        <el-table-column :show-overflow-tooltip="true" prop="title" label="文章标题" align="left" width="200" fixed="left">
          <template slot-scope="scope">
            {{ scope.row['title'] }}
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="articleType" label="文章类型" width="80" align="center">
          <template slot-scope="scope">
            <span v-if="scope.row['articleType'] === 1" style="font-size: 12px">原创</span>
            <span v-else-if="scope.row['articleType'] === 2">
              <el-tooltip :content="scope.row['reprintLink']" placement="top" effect="light">
                <el-link type="primary" :href="scope.row['reprintLink']" :disabled="scope.row['reprintLink'] === ''" :title="scope.row['reprintLink']" :underline="true" target="_blank" style="font-size: 12px">
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
          <template slot-scope="scope">
            <el-tag v-for="item of scope.row['tagList']" :key="item.id" :type="item.type === 2 ? 'info' : ''" style="margin-right:0.2rem;margin-top:0.2rem;">
              {{ item.name }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="articleStatus" label="文章状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row['articleStatus'] === 1" type="info" effect="dark">草稿</el-tag>
            <el-tag v-else-if="scope.row['articleStatus'] === 2" effect="dark">待审核</el-tag>
            <el-tag v-else-if="scope.row['articleStatus'] === 3" effect="dark">待审核</el-tag>
            <el-tag v-else-if="scope.row['articleStatus'] === 4" type="warning" effect="dark">
              {{ '审核拒绝：' + scope.row['desc'] }}
            </el-tag>
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
        <el-table-column :show-overflow-tooltip="true" prop="articleStatus" label="发布范围" width="100" align="center">
          <template v-slot="scope">
            <el-tag v-if="scope.row['articlePerm'] === 1" effect="dark">全部可见</el-tag>
            <!-- <el-tag v-else-if="scope.row['articlePerm'] === 2">登录可见</el-tag> -->
            <el-tag v-else-if="scope.row['articlePerm'] === 3" type="warning" effect="dark">仅我可见</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="tagList" label="评论状态" width="100" align="center">
          <template v-slot="scope">
            <el-tag v-if="scope.row['commentFlag']" effect="dark">开启评论</el-tag>
            <el-tag v-else type="warning" effect="dark">关闭评论</el-tag>
          </template>
        </el-table-column>
        <!-- <el-table-column :show-overflow-tooltip="true" prop="authorNickname" label="作者" width="100" align="center" /> -->
        <el-table-column :show-overflow-tooltip="true" prop="createTime" label="最后修改时间" width="150" align="center">
          <template v-slot="scope">
            <span v-if="scope.row['updateTime'] !== ''">{{ scope.row['updateTime'] }}</span>
            <span v-else>{{ scope.row['createTime'] }}</span>
          </template>
        </el-table-column>
        <el-table-column :show-overflow-tooltip="true" prop="wordCount" label="字数" width="70" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="viewCount" label="浏览量" width="70" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="likeCount" label="点赞量" width="70" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="commentCount" label="评论数" width="70" align="center" />
        <el-table-column :show-overflow-tooltip="true" prop="desc" label="备注" width="150" align="center" />
        <div slot="empty">
          <el-empty :image="nodataImg" description=" " />
        </div>
      </el-table>
      <div class="pagination-container">
        <el-pagination
          :total="total"
          :current-page.sync="page"
          :page-size.sync="pageSize"
          :page-sizes="[5, 10, 15, 20, 25]"
          layout="total, prev, pager, next, sizes"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script>
import nodataImg from '@/assets/images/nodata.png'
export default {
  name: 'Article',
  data() {
    return {
      statusTabs: [
        { key: 1, label: '全部', field: 'totalCount' },
        { key: 2, label: '公开', field: 'publicCount' },
        { key: 3, label: '私密', field: 'privateCount' },
        { key: 4, label: '待审核', field: 'pendingAuditCount' },
        { key: 5, label: '待发布', field: 'pendingPublishCount' },
        { key: 6, label: '待处理', field: 'pendingDealCount' },
        { key: 7, label: '草稿箱', field: 'draftCount' },
        { key: 8, label: '回收站', field: 'recycleCount' }
      ],
      articleCountInfo: {
        // 全部
        totalCount: 0,
        // 公开
        publicCount: 0,
        // 私密
        privateCount: 0,
        // 待审核
        pendingAuditCount: 0,
        // 待发布
        pendingPublishCount: 0,
        // 待处理（比如审核不通过 / 待整改）
        pendingDealCount: 0,
        // 草稿箱
        draftCount: 0,
        // 回收站
        recycleCount: 0
      },
      filters: {
        tagStatus: 1,
        title: null,
        categoryId: null,
        tagId: null,
        articleType: null,
        commentFlag: null
      },
      categoryList: [],
      articleDefaultImg: '',
      tableData: [],
      tableLoading: false,
      nodataImg: nodataImg,
      rows: [],
      total: 0,
      page: 1,
      pageSize: 5,
      previewArticleDialogVisible: false
    }
  },
  computed: {
    isMobile() {
      return this.$store.state.app.device === 'mobile'
    }
  },
  mounted() {
    this.listTableData()
    this.queryCategoryTree()
    // this.getDefaultArticleImg()
  },
  activated() {
    if (this.$route.query._ts && this.$route.query._ts !== '') {
      this.statusTabChange(this.$route.query._ts)
    } else {
      if (this.$route.query.refresh) {
        this.page = 1
        this.listTableData()
      }
    }
  },
  methods: {
    handleSelectionChange(val) {
      this.rows = val
    },
    // 查询
    statusTabChange(status) {
      this.page = 1
      this.filters.tagStatus = status
      this.listTableData()
    },
    search() {
      this.page = 1
      this.listTableData()
    },
    resetSearch() {
      Object.keys(this.filters).forEach(key => { this.filters[key] = null })
      this.filters.tagStatus = 1
      this.page = 1
      this.listTableData()
    },
    handleSizeChange(size) {
      this.page = 1
      this.pageSize = size
      this.listTableData()
    },
    handlePageChange(page) {
      this.page = page
      this.listTableData()
    },
    listTableData() {
      const param = {
        ... this.filters,
        page: this.page,
        pageSize: this.pageSize
      }
      this.tableLoading = true
      this.$mapi.article.pageMyList(param).then(res => {
        const { data } = res
        this.total = data.total
        this.tableData = data.records
      }).catch(_ => {
        this.total = 0
        this.tableData = []
      }).finally(() => {
        this.rows = []
        this.tableLoading = false
        this.$refs.articleTable.setCurrentRow()
        this.loadMyArticleCountInfo()
      })
    },
    loadMyArticleCountInfo() {
      this.$mapi.article.queryMyArticleCountInfo().then(res => {
        const { data } = res
        Object.keys(this.articleCountInfo).forEach(key => {
          this.articleCountInfo[key] = data[key] || 0
        })
      })
    },
    queryCategoryTree() {
      this.$mapi.category.queryPublicCategoryTree().then(({ data }) => {
        this.categoryList = data
      }).catch(_ => {
        this.categoryList = []
      })
    },
    // TODO
    getDefaultArticleImg() {
      this.$mapi.article.queryArticleDefaultImg().then(res => {
        const { data } = res
        this.articleDefaultImg = data || ''
      })
    },
    addArticle() {
      this.$router.push({
        name: 'AddArticle'
      })
    },
    editArticle(row) {
      this.$router.push({
        name: 'AddArticle',
        query: {
          articleId: row.id
        }
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
    recycleToDraftBox(row) {
      this.$mapi.article.recycleToDraftBox({ articleId: row.id }).then(res => {
        this.$message.success(res.message)
        this.listTableData()
      })
    },
    moveToRecycleBin(row) {
      this.$mapi.article.moveToRecycleBin({ articleId: row.id }).then(res => {
        this.$message.success(res.message)
        this.listTableData()
      })
    },
    batchMoveToRecycleBin() {
      const articleIds = []
      this.rows.forEach(row => {
        articleIds.push(row.id)
      })

      if (articleIds.length === 0) {
        this.$message.warning('请选择要删除的文章列表')
        return
      }

      this.$mapi.article.batchMoveToRecycleBin(articleIds).then(res => {
        this.$message.success(res.message)
        this.listTableData()
      })
    },
    deleteArticle(row) {
      this.$mapi.article.deleteArticle({ articleId: row.id }).then(res => {
        this.$message.success(res.message)
        this.listTableData()
      })
    },
    clearRecycleBin() {
      this.$mapi.article.clearRecycleBin().then(res => {
        this.$message.success(res.message)
        this.listTableData()
      })
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
  font-weight: bold;
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
