<template>
  <div v-show="showEmojiPicker" class="emoji-wrapper">
    <template v-if="emojiList.length">
      <span v-for="emoji of emojiList" :key="emoji.id" class="emoji-item" @click="selectEmoji(emoji.name, emoji.code)">
        <v-img :lazy-src="emoji.url" :src="emoji.url" :title="emoji.name" class="emoji" width="24" height="24" />
      </span>
    </template>
    <div v-else class="emoji-empty">
      暂无表情
    </div>
  </div>
</template>

<script>
export default {
  props: {
    showEmojiPicker: {
      type: Boolean,
      default: false
    },
    emojiList: {
      type: Array,
      required: true
    }
  },
  methods: {
    selectEmoji(name, code) {
      this.$emit('selectEmoji', name, code)
    }
  }
}
</script>

<style scoped>
.emoji-wrapper {
  max-height: 150px;
  overflow-y: auto;
}

/* 表情项 */
.emoji-item {
  display: inline-block;
  border-radius: 0.25rem;
  cursor: var(--globalPointer);
  transition: background 0.2s ease;
}

.emoji-item:hover {
  background: #dddddd;
}

/* 表情图片 */
.emoji {
  display: inline-block;
  margin: 0.25rem;
  user-select: none;
  vertical-align: middle;
}

/* 滚动条 */
.emoji-wrapper::-webkit-scrollbar {
  width: 6px;
}

.emoji-wrapper::-webkit-scrollbar-thumb {
  border-radius: 3px;
  background: #c0c4cc;
}

.emoji-wrapper::-webkit-scrollbar-track {
  background: transparent;
}

/* 暂无表情 */
.emoji-empty {
  padding: 20px 0;
  color: #909399;
  font-size: 13px;
  text-align: center;
}
</style>
