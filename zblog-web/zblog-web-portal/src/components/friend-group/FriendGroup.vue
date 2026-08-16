<template>
  <section v-if="list.length" class="friend-group">
    <div class="group-title">
      <span>{{ icon }}</span> {{ title }}
    </div>
    <div class="group-desc">{{ desc }}</div>
    <v-row>
      <v-col v-for="item in list" :key="item.id" md="3" sm="6" cols="12">
        <a :href="item.url" target="_blank" class="friend-card">
          <div class="cover">
            <img :src="item.logo" alt="加载失败">
          </div>
          <div class="content">
            <div class="name"> {{ item.name }}</div>
            <div class="desc"> {{ item.description }}</div>
            <div class="time"> 🗓 {{ item.createTime }}</div>
          </div>
        </a>
      </v-col>
    </v-row>
  </section>
</template>
<script>export default {
  props: {
    title: String,
    icon: String,
    desc: String,
    list: {
      type: Array,
      default: () => []
    }
  }
}</script>
<style scoped>
.friend-group {
  margin-bottom: 50px;
}

.group-title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 5px;
}

.group-desc {
  color: #999;
  font-size: 14px;
  margin-bottom: 20px;
}

/* 卡片 */
.friend-card {
  display: block;
  background: white;
  border-radius: 12px;
  overflow: hidden;
  text-decoration: none;
  color: #333;
  box-shadow: 0 4px 15px rgba(0, 0, 0, .08);
  transition: .3s;
}

.friend-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 10px 25px rgba(0, 0, 0, .15);
}

.cover {
  height: 150px;
  overflow: hidden;
  background: #eee;
}

.cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: .5s;
}

.friend-card:hover img {
  transform: scale(1.08);
}

.content {
  padding: 15px;
}

.name {
  font-size: 18px;
  font-weight: bold;
}

.desc {
  margin-top: 8px;
  height: 42px;
  font-size: 14px;
  color: #666;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.time {
  margin-top: 12px;
  font-size: 12px;
  color: #999;
}
</style>
