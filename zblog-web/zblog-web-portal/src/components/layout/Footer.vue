<template>
  <v-footer app padless absolute>
    <div
      class="footer-wrap footer-theme"
      :class="{ 'default-gradient': !websiteConfig.footerBackground }"
      :style="footerStyle"
    >
      <!-- 网站签名 -->
      <div v-if="websiteConfig.websiteSignature" class="footer-signature">
        {{ websiteConfig.websiteSignature }}
      </div>
      <!-- 快捷导航 -->
      <nav class="footer-nav">
        <router-link to="/">
          首页
        </router-link>
        <router-link to="/friend-link">
          申请友链
        </router-link>
        <router-link to="/message">
          留言
        </router-link>
        <a v-if="websiteLinks['GITEE']" :href="websiteLinks['ABOUT_ME']" target="_blank">
          关于我
        </a>
        <a v-if="websiteLinks['GITEE']" :href="websiteLinks['DOCUMENT']" target="_blank">
          文档
        </a>
        <a v-if="websiteLinks['GITEE']" :href="websiteLinks['GITEE']" target="_blank">
          Gitee
        </a>
        <a v-if="websiteLinks['GITHUB']" :href="websiteLinks['GITHUB']" target="_blank">
          Github
        </a>
        <a v-if="websiteLinks['GITEE']" :href="websiteLinks['ISSUE']" target="_blank">
          Issue
        </a>
      </nav>

      <!-- 底部信息 -->
      <div class="footer-info">
        <!-- 版权 -->
        <span class="footer-copy">
          ©
          <template v-if="websiteConfig.websiteCreateTime">
            {{ websiteConfig.websiteCreateTime | year }} -
          </template>
          {{ currentYear }}
          {{ websiteConfig.websiteName }}
        </span>

        <!-- 网站备案 -->
        <div class="footer-records">
          <!-- ICP备案 -->
          <template v-if="websiteConfig.websiteIcpNo">
            <a href="https://beian.miit.gov.cn/" target="_blank" rel="noopener noreferrer" class="footer-record">
              {{ websiteConfig.websiteIcpNo }}
            </a>
          </template>

          <!-- 公安备案 -->
          <template v-if="websiteConfig.websitePoliceNo">
            <a href="https://www.beian.gov.cn/" target="_blank" rel="noopener noreferrer" class="footer-record">
              {{ websiteConfig.websitePoliceNo }}
            </a>
          </template>
        </div>

        <!-- 网站声明 -->
        <nav class="footer-notice">
          <router-link to="/copyright">
            版权声明
          </router-link>
          <router-link to="/disclaimer">
            免责声明
          </router-link>
          <router-link to="/infringement">
            侵权联系
          </router-link>
        </nav>
      </div>
    </div>
  </v-footer>
</template>

<script>
export default {
  computed: {
    /**
     * 网站配置
     */
    websiteConfig() {
      return this.$store.state.websiteConfig
    },
    /**
     * 网站链接配置
     */
    websiteLinks() {
      return this.$store.state.links
    },
    /**
     * 当前年份
     */
    currentYear() {
      return new Date().getFullYear()
    },
    /**
     * Footer背景
     */
    footerStyle() {
      if (this.websiteConfig.footerBackground) {
        return {
          backgroundImage: `
            linear-gradient(
              rgba(0, 0, 0, .45),
              rgba(0, 0, 0, .45)
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
  font-size: 12px;
  text-align: center;
  color: rgba(255, 255, 255, .85);
  position: relative;
  background-size: cover;
  background-position: center;
  text-shadow: 0 1px 3px rgba(0, 0, 0, .65);
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
  font-weight: 600;
  line-height: 1.6;
  color: #fff !important;
  letter-spacing: 1px;
  text-shadow: 0 2px 5px rgba(0, 0, 0, .6);
}

.footer-nav {
  margin-top: 14px;
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 0;
}
.footer-nav a {
  display: inline-flex;
  align-items: center;
}
.footer-theme .footer-nav a {
  color: rgba(255, 255, 255, .9) !important;
  text-decoration: none;
  line-height: 1.8;
  transition: color .2s ease, opacity .2s ease;
  text-shadow: 0 1px 3px rgba(0, 0, 0, .7);
}
.footer-theme .footer-nav a:hover {
  color: #fff !important;
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
  gap: 6px;
  flex-wrap: wrap;
  line-height: 1.8;
  color: rgba(255, 255, 255, .75);
  text-shadow: 0 1px 3px rgba(0, 0, 0, .7);
}

.footer-records, .footer-notice {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}
.footer-records::before, .footer-notice::before {
  content: '·';
  margin: 0 8px;
  color: rgba(255, 255, 255, .35);
}

.footer-copy {
  white-space: nowrap;
}

.footer-theme .footer-record {
  color: rgba(255, 255, 255, .75) !important;
  text-decoration: none;
  white-space: nowrap;
  transition: color .2s ease;
}

.footer-theme .footer-record:hover {
  color: #fff !important;
}

.footer-notice {
  display: flex;
  align-items: center;
  white-space: nowrap;
}

.footer-theme .footer-notice a {
  color: rgba(255, 255, 255, .75) !important;
  text-decoration: none;
  transition: color .2s ease;
}

.footer-theme .footer-notice a:hover {
  color: #fff !important;
  text-decoration: underline;
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
  .footer-nav a {
    margin: 0 8px;
  }
  .footer-nav a + a::before {
    display: none;
  }
  .footer-info {
    flex-direction: column;
    margin-top: 10px;
    padding: 0 4px;
    line-height: 2;
  }
  .footer-records::before, .footer-notice::before {
    display: none;
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
}
</style>
