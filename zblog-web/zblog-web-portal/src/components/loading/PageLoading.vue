<template>
  <div v-if="show" class="loading-box" onclick="click()">
    <div class="loading-bg">
      <div class="loading-img" />
      <div class="loading-image-dot" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'PageLoading',
  props: {
    show: {
      type: Boolean,
      default: false
    }
  },
  methods: {
    click() {
      event.stopPropagation()
    }
  }
}
</script>

<style scoped>
.loading-box {
  width: 100vw;
  height: 100vh;
  z-index: 9999;
  position: fixed;
  pointer-events: none;
  top: 0;
}
.loading-bg {
  display: -webkit-box;
  display: -moz-box;
  display: -webkit-flex;
  display: -ms-flexbox;
  width: 100vw;
  height: 100vh;
  z-index: 9999;
  display: flex;
  position: fixed;
  background: rgba(234, 234, 234, 0.8);
  opacity: 1;
  -ms-filter: none;
  filter: none;
  -webkit-transition: 0.3s;
  -moz-transition: 0.3s;
  -o-transition: 0.3s;
  -ms-transition: 0.3s;
  transition: 0.3s;
}
.loading-img {
  width: 128px;
  height: 128px;
  border-radius: 50%;
  margin: auto;
  border: 5px solid #b7d3ee;
  background: url("https://file.suntaoblog.com/image/siteImg.png") no-repeat center center;
  background-size: cover;
  -webkit-animation-duration: 0.3s;
  -moz-animation-duration: 0.3s;
  -o-animation-duration: 0.3s;
  -ms-animation-duration: 0.3s;
  animation-duration: 0.3s;
  -webkit-animation-name: loadingAction;
  -moz-animation-name: loadingAction;
  -o-animation-name: loadingAction;
  -ms-animation-name: loadingAction;
  animation-name: loadingAction;
  -webkit-animation-iteration-count: infinite;
  -moz-animation-iteration-count: infinite;
  -o-animation-iteration-count: infinite;
  -ms-animation-iteration-count: infinite;
  animation-iteration-count: infinite;
  -webkit-animation-direction: alternate;
  -moz-animation-direction: alternate;
  -o-animation-direction: alternate;
  -ms-animation-direction: alternate;
  animation-direction: alternate;
}
.loading-image-dot {
  width: 30px;
  height: 30px;
  background: #6bdf8f;
  position: absolute;
  border-radius: 50%;
  border: 5px solid #fff;
  top: 50%;
  left: 50%;
  -webkit-transform: translate(32px, 32px);
  -moz-transform: translate(32px, 32px);
  -o-transform: translate(32px, 32px);
  -ms-transform: translate(32px, 32px);
  transform: translate(32px, 32px);
}

@-moz-keyframes loadingAction {
  0% {
    opacity: 1;
    -ms-filter: none;
    filter: none;
  }
  100% {
    opacity: 0.6;
    -ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=60)";
    filter: alpha(opacity=60);
  }
}
@-webkit-keyframes loadingAction {
  0% {
    opacity: 1;
    -ms-filter: none;
    filter: none;
  }
  100% {
    opacity: 0.6;
    -ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=60)";
    filter: alpha(opacity=60);
  }
}
@-o-keyframes loadingAction {
  0% {
    opacity: 1;
    -ms-filter: none;
    filter: none;
  }
  100% {
    opacity: 0.6;
    -ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=60)";
    filter: alpha(opacity=60);
  }
}
@keyframes loadingAction {
  0% {
    opacity: 1;
    -ms-filter: none;
    filter: none;
  }
  100% {
    opacity: 0.6;
    -ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=60)";
    filter: alpha(opacity=60);
  }
}
</style>
