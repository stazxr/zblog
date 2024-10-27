<template>
  <div class="paging">
    <!-- 上一页按钮 第一页不显示 -->
    <a v-show="current !== 1" class="ml-1 mr-1" @click="prePage">上一页</a>
    <!-- 小于6页直接显示 -->
    <template v-if="totalPage < 6">
      <a v-for="i of totalPage" :key="i" :class="'ml-1 mr-1 ' + isActive(i)" @click="changeReplyCurrent(i)">
        {{ i }}
      </a>
    </template>
    <!-- 大于等于6页且在前两页 -->
    <template v-else-if="current < 3">
      <a v-for="i in 4" :key="i" :class="'ml-1 mr-1 ' + isActive(i)" @click="changeReplyCurrent(i)">
        {{ i }}
      </a>
      <span class="ml-1 mr-1">···</span>
      <a class="ml-1 mr-1" @click="changeReplyCurrent(totalPage)">
        {{ totalPage }}
      </a>
    </template>
    <!-- 大于等于6页且在3-4页 -->
    <template v-else-if="current < 5">
      <a v-for="i in current + 2" :key="i" :class="'ml-1 mr-1 ' + isActive(i)" @click="changeReplyCurrent(i)">
        {{ i }}
      </a>
      <span v-if="current + 2 < totalPage - 1" class="ml-1 mr-1">···</span>
      <a v-if="current + 2 < totalPage" class="ml-1 mr-1" @click="changeReplyCurrent(totalPage)">
        {{ totalPage }}
      </a>
    </template>
    <!-- 大于等于6页且在最后两页 -->
    <template v-else-if="current > totalPage - 2">
      <a class="ml-1 mr-1" @click="changeReplyCurrent(1)">1</a>
      <span class="ml-1 mr-1">···</span>
      <a v-for="i in 4" :key="i" :class="'ml-1 mr-1 ' + isActive(i + (totalPage - 4))" @click="changeReplyCurrent(i + (totalPage - 4))">
        {{ i + (totalPage - 4) }}
      </a>
    </template>
    <!-- 大于等于6页且在最后三四页 -->
    <template v-else-if="current > totalPage - 4">
      <a class="ml-1 mr-1" @click="changeReplyCurrent(1)">1</a>
      <span class="ml-1 mr-1">···</span>
      <a v-for="i in totalPage - current + 3" :key="i" :class="'ml-1 mr-1 ' + isActive(i + current - 3)" @click="changeReplyCurrent(i + current - 3)">
        {{ i + current - 3 }}
      </a>
    </template>
    <!-- 大于等于6页且在中间页 -->
    <template v-else>
      <a class="ml-1 mr-1" @click="changeReplyCurrent(1)">1</a>
      <span class="ml-1 mr-1">···</span>
      <a class="ml-1 mr-1" @click="changeReplyCurrent(current - 2)">
        {{ current - 2 }}
      </a>
      <a class="ml-1 mr-1" @click="changeReplyCurrent(current - 1)">
        {{ current - 1 }}
      </a>
      <a class="active ml-1 mr-1">{{ current }}</a>
      <a class="ml-1 mr-1" @click="changeReplyCurrent(current + 1)">
        {{ current + 1 }}
      </a>
      <a class="ml-1 mr-1" @click="changeReplyCurrent(current + 2)">
        {{ current + 2 }}
      </a>
      <span class="ml-1 mr-1">···</span>
      <a class="ml-1 mr-1" @click="changeReplyCurrent(totalPage)">{{
        totalPage
      }}</a>
    </template>
    <!-- 下一页按钮, 最后一页不显示 -->
    <a v-show="current !== totalPage" class="ml-1 mr-1" @click="nextPage">
      下一页
    </a>
  </div>
</template>
<script>
export default {
  props: {
    totalPage: {
      type: Number,
      default: null
    },
    index: {
      type: Number,
      default: null
    },
    commentId: {
      type: String,
      default: null
    }
  },
  data: function() {
    return {
      current: 1
    }
  },
  computed: {
    isActive() {
      return function(i) {
        if (i === this.current) {
          return 'active'
        }
      }
    }
  },
  methods: {
    changeReplyCurrent(i) {
      this.current = i
      this.$emit('changeReplyCurrent', this.current, this.index, this.commentId)
    },
    prePage() {
      this.current -= 1
      this.$emit('changeReplyCurrent', this.current, this.index, this.commentId)
    },
    nextPage() {
      this.current += 1
      this.$emit('changeReplyCurrent', this.current, this.index, this.commentId)
    }
  }
}
</script>

<style scoped>
.paging a {
  display: inline-block;
  color: #222;
}
.active {
  color: #00a1d6 !important;
  font-weight: bold;
}
</style>
