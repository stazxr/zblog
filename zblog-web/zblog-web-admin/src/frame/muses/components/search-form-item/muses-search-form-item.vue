<template>
  <el-form-item
    ref="musesSearchFormItem"
    name="musesSearchFormItem"
    :style="{ minWidth: expandInfo1.minWidth + 'px' }"
    :class="{ 'muses-auto-btn': isShowBtn, 'muses-hidden': isHidden }"
    v-bind="$attrs"
    v-on="$listeners"
  >
    <slot />
    <slot name="label" />
    <!-- 收起/展开 -->
    <el-button v-show="isNeedShowBtn && isShowBtn" class="muses-expend" type="text" @click="handleClick1">
      {{ expandInfo1.isShowAll ? btnCloseName : btnOpenName }}
      <i class="el-icon-d-arrow-left" :class="{'is-active': !expandInfo1.isShowAll }" />
    </el-button>
  </el-form-item>
</template>

<script>
export default {
  name: 'MusesSearchFormItem',
  inheritAttrs: false,
  props: {
    // 是否是搜索按钮
    btn: {
      type: Boolean,
      default: false
    },
    btnCloseName: {
      type: String,
      default: '收起'
    },
    btnOpenName: {
      type: String,
      default: '展开'
    }
  },
  data() {
    return {
      isShow: false,
      isShowBtn: false
    }
  },
  computed: {
    isHidden() {
      return this.expandInfo.isShowAll ? false : !this.isShow
    },
    isNeedShowBtn() {
      return this.expandInfo.nodeLength > this.expandInfo.count
    },
    expandInfo1() {
      return this.expandInfo
    },
    handleClick1() {
      return this.handleClick
    }
  },
  watch: {
    expandInfo: {
      handler(val) {
        this.setBase(val)
      },
      deep: true
    }
  },
  inject: ['expandInfo', 'handleClick'],
  methods: {
    setBase(val) {
      let base = base = (1 / val.count) * 100 + '%'
      this.$refs.musesSearchFormItem.$el['style'].flexBasis = base
    }
  }
}
</script>
<style lang="scss" scoped>
.el-form-item {
  // 平均分配每一个 el-form-item 的长度
  // flex: 1;
}
.muses-auto-btn {
  display: block !important;
  max-width: none;
  ::v-deep.el-form-item__content {
    display: flex;
    justify-content: flex-start;
    margin-left: 0 !important;
    .el-button {
      padding-left: 15px;
      padding-right: 15px;
      margin: 0 5px;
    }
    .el-button--text {
      padding-left: 0;
      padding-right: 0;
    }
  }
  i {
    transform: rotateZ(90deg);
  }
  .is-active {
    transform: rotateZ(-90deg);
  }
}
.muses-hidden {
  display: none;
}
//.justify-content-flex-end {
//  display: flex;
//  justify-content: flex-end;
//}
</style>
