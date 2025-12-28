<template>
  <div class="setting-layout">
    <el-card class="setting-card" shadow="hover">
      <!-- 标题 -->
      <div v-if="title" class="setting-title">
        {{ title }}
      </div>

      <!-- 表单内容 -->
      <div class="setting-content">
        <slot />
      </div>

      <!-- 底部操作区 -->
      <div v-if="$slots.actions" class="setting-actions">
        <slot name="actions" />
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'SettingLayout',
  props: {
    title: {
      type: String,
      default: ''
    }
  }
}
</script>

<style scoped>
.setting-layout {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

.setting-card {
  width: 100%;
  max-width: 420px;
  border-radius: 10px;
}

.setting-title {
  text-align: center;
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 24px;
}

.setting-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 30px;
}

/* 手机端 */
@media (max-width: 768px) {
  .setting-layout {
    margin-top: 16px;
  }

  .setting-actions {
    flex-direction: column;
  }

  .setting-actions ::v-deep .el-button {
    width: 100%;
  }
}
</style>
