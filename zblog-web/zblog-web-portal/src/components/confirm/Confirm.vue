<template>
  <div v-if="show" class="dialog-wrapper">
    <div class="overlay" />
    <v-card class="dialog">
      <v-card-title>提示</v-card-title>
      <v-card-text>
        {{ message }}
      </v-card-text>
      <v-card-actions style="float: right">
        <v-btn outlined small @click="cancel">取消</v-btn>
        <v-btn outlined small style="margin-left: 10px;" @click="ok">确定</v-btn>
      </v-card-actions>
    </v-card>
  </div>
</template>
<script>
export default {
  data() {
    return {
      message: '您确定这么做吗？',
      show: false
    }
  },
  methods: {
    ok() {
      this.show = false
    },
    cancel() {
      this.show = false
    }
  }
}
</script>

<style>
.dialog-wrapper{
  align-items: center;
  display: flex;
  height: 100%;
  justify-content: center;
  left: 0;
  pointer-events: none;
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 6;
  transition: all 0.2s cubic-bezier(0.25, 0.8, 0.25, 1) 0s, z-index 1ms ease 0s;
  outline: none;
}
.dialog{
  overflow-y: auto;
  pointer-events: auto;
  width: 100%;
  z-index: inherit;
  box-shadow: rgba(0, 0, 0, 0.2) 0 11px 15px -7px, rgba(0, 0, 0, 0.14) 0 24px 38px 3px, rgba(0, 0, 0, 0.12) 0 9px 46px 8px;
  border-radius: 4px;
  margin: 24px;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1) 0s;
  max-width:290px;
}
.overlay{
  align-items: center;
  border-radius: inherit;
  display: flex;
  justify-content: center;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  pointer-events: auto;
  background:#000;
  opacity:0.46;
}
</style>
