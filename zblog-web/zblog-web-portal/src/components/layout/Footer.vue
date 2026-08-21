<template>
  <v-footer app padless absolute>
    <div
      class="footer-wrap"
      :class="{ 'default-gradient': !websiteConfig.footerBackground }"
      :style="footerStyle"
    >
      <!-- 第一行：网站签名 -->
      <div v-if="websiteConfig.websiteSignature" class="footer-signature">
        {{ websiteConfig.websiteSignature }}
      </div>

      <!-- 第二行：快捷导航 -->
      <nav class="footer-nav">
        <router-link to="/">首页</router-link>
        <router-link to="/article">文章</router-link>
        <router-link to="/friend">友链</router-link>
        <router-link to="/message">留言</router-link>
        <a v-if="websiteConfig.documentUrl" :href="websiteConfig.documentUrl" target="_blank" rel="noopener noreferrer">
          文档
        </a>
        <a v-if="websiteConfig.giteeUrl" :href="websiteConfig.giteeUrl" target="_blank" rel="noopener noreferrer">
          Gitee
        </a>
        <a v-if="websiteConfig.githubUrl" :href="websiteConfig.githubUrl" target="_blank" rel="noopener noreferrer">
          Github
        </a>
      </nav>

      <!-- 第三行：版权 + 备案 + 网站声明 -->
      <div class="footer-info">
        <!-- 版权 -->
        <span class="footer-copy">
          © {{ websiteConfig.websiteCreateTime | year }} -
          {{ currentYear }}
          {{ websiteConfig.websiteAuthor }}
        </span>
        <!-- 备案 -->
        <template v-if="websiteConfig.websiteRecordNo">
          <span class="footer-separator">·</span>
          <a href="https://beian.miit.gov.cn/" target="_blank" rel="noopener noreferrer" class="footer-record">
            {{ websiteConfig.websiteRecordNo }}
          </a>
        </template>
        <!-- 网站声明 -->
        <span class="footer-separator">·</span>
        <nav class="footer-notice">
          <router-link to="/copyright" target="_blank">版权声明</router-link>
          <span class="footer-separator">·</span>
          <router-link to="/disclaimer">免责声明</router-link>
          <span class="footer-separator">·</span>
          <router-link to="/infringement">侵权联系</router-link>
        </nav>
      </div>
    </div>
  </v-footer>
</template>

<script>
export default {
  computed: {
    /**
     * 网站配置信息
     */
    websiteConfig() {
      return this.$store.state.websiteConfig
    },
    /**
     * 当前年份
     */
    currentYear() {
      return new Date().getFullYear()
    },
    /**
     * Footer 背景
     */
    footerStyle() {
      if (this.websiteConfig.footerBackground) {
        return {
          backgroundImage: `
            linear-gradient(
              rgba(0, 0, 0, .35),
              rgba(0, 0, 0, .35)
            ),
            url(${this.websiteConfig.footerBackground})
          `,
          backgroundSize: 'cover',
          backgroundPosition: 'center'
        }
      }
      return {}
    }
  }
}
</script>

<style scoped>
.footer-wrap {
  width: 100%;
  padding: 24px 20px 18px;
  color: rgba(255, 255, 255, .82);
  font-size: 12px;
  text-align: center;
  position: relative;
  background-size: cover;
  background-position: center;
}

/* =========================
   默认渐变背景
   ========================= */

.default-gradient {
  background: linear-gradient(
    -45deg,
    #ee7752,
    #ce3e75,
    #23a6d5,
    #23d5ab
  );
  background-size: 400% 400%;
  animation: gradient 12s ease infinite;
}

.footer-signature {
  font-size: 17px;
  font-weight: 500;
  line-height: 1.6;
  color: rgba(255, 255, 255, .95);
  letter-spacing: 1px;
}

.footer-nav {
  margin-top: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  gap: 0;
}
.footer-nav a {
  color: rgba(255, 255, 255, .82);
  text-decoration: none;
  line-height: 1.8;
  transition: color .2s ease,
  opacity .2s ease;
}
.footer-nav a:hover {
  color: #fff;
}
.footer-nav a + a::before {
  content: '·';
  margin: 0 12px;
  color: rgba(255, 255, 255, .35);
}

.footer-info {
  margin-top: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-wrap: wrap;
  line-height: 1.8;
  color: rgba(255, 255, 255, .58);
}

.footer-copy {
  white-space: nowrap;
}

.footer-record {
  color: rgba(255, 255, 255, .62);
  text-decoration: none;
  white-space: nowrap;
  transition: color .2s ease;
}

.footer-record:hover {
  color: #fff;
}

.footer-notice {
  display: flex;
  align-items: center;
  white-space: nowrap;
}
.footer-notice a {
  color: rgba(255, 255, 255, .58);
  text-decoration: none;
  transition: color .2s ease;
}
.footer-notice a:hover {
  color: #fff;
  text-decoration: underline;
}

.footer-separator {
  margin: 0 9px;
  color: rgba(255, 255, 255, .3);
}

@keyframes gradient {
  0% {
    background-position: 0 50%;
  }
  50% {
    background-position: 100% 50%;
  }
  100% {
    background-position: 0 50%;
  }
}

/* =========================
   手机端
   ========================= */

@media screen and (max-width: 700px) {
  .footer-wrap {
    padding: 20px 12px 16px;
    font-size: 12px;
  }
  .footer-signature {
    font-size: 16px;
    letter-spacing: 0;
  }
  .footer-nav {
    margin-top: 12px;
    padding: 0 4px;
    line-height: 2;
  }
  .footer-nav a + a::before {
    margin: 0 8px;
  }
  .footer-info {
    margin-top: 10px;
    padding: 0 4px;
    line-height: 2;
    justify-content: center;
  }
  .footer-copy {
    white-space: normal;
  }
  .footer-record {
    white-space: normal;
  }
  .footer-notice {
    white-space: normal;
  }
  .footer-separator {
    margin: 0 6px;
  }
}
</style>
