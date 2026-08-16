<template>
  <div class="statement-page">
    <v-container max-width="900">
      <v-card class="statement-card" flat>
        <v-card-title class="statement-title">
          侵权联系
        </v-card-title>

        <v-card-text class="statement-content">
          <section>
            <h3>一、联系说明</h3>
            <p>
              如果您认为本站发布的文字、图片、代码、文档或其他内容
              侵犯了您的合法权益，请及时与本站联系。
            </p>
            <p>
              我们重视相关版权问题，并会在收到有效反馈后及时核实和处理。
            </p>
          </section>

          <section>
            <h3>二、联系方式</h3>

            <div class="contact-box">
              <div class="contact-label">
                联系邮箱
              </div>

              <a
                class="contact-email"
                :href="'mailto:' + email"
              >
                {{ email }}
              </a>
            </div>
          </section>

          <section>
            <h3>三、建议提供的信息</h3>
            <p>
              为便于我们快速核实和处理，请在联系时尽可能提供以下信息：
            </p>

            <ol>
              <li>您的姓名或名称及有效联系方式；</li>
              <li>您认为享有相关权利的内容及权利证明；</li>
              <li>涉嫌侵权内容的具体网页链接；</li>
              <li>您认为存在侵权的具体内容及相关说明；</li>
              <li>其他有助于我们判断和处理问题的材料。</li>
            </ol>
          </section>

          <section>
            <h3>四、处理方式</h3>
            <p>
              收到相关反馈后，我们会根据提供的信息进行核实。
              对于经核实存在相关问题的内容，我们将根据实际情况
              采取删除、屏蔽、断开相关链接或其他必要的处理措施。
            </p>
          </section>

          <section>
            <h3>五、说明</h3>
            <p>
              为避免误处理，对于无法确认权利归属、缺少必要信息
              或无法定位具体内容的反馈，可能需要您补充相关材料。
            </p>
          </section>
        </v-card-text>
      </v-card>
    </v-container>
  </div>
</template>

<script>
export default {
  computed: {
    email() {
      // 后续可以改成 this.$store.state.websiteConfig.email
      return 'xxx@qq.com'
    }
  }
}
</script>

<style scoped>
.statement-page {
  min-height: calc(100vh - 64px);
  padding: 40px 0;
}

.statement-card {
  border-radius: 12px;
}

.statement-title {
  padding: 28px 32px 10px;
  font-size: 24px;
  font-weight: 600;
}

.statement-content {
  padding: 10px 32px 32px;
  color: #555;
  font-size: 14px;
  line-height: 1.9;
}

.statement-content section {
  margin-top: 25px;
}

.statement-content section:first-child {
  margin-top: 10px;
}

.statement-content h3 {
  margin-bottom: 10px;
  font-size: 17px;
  color: #333;
}

.statement-content p {
  margin: 8px 0;
}

.statement-content ol {
  padding-left: 25px;
}

.statement-content li {
  margin: 6px 0;
}

.contact-box {
  display: inline-flex;
  align-items: center;
  gap: 15px;
  margin-top: 5px;
  padding: 12px 18px;
  background: #f7f8fa;
  border-radius: 8px;
}

.contact-label {
  color: #777;
}

.contact-email {
  color: #333;
  font-weight: 500;
  text-decoration: none;
}

.contact-email:hover {
  text-decoration: underline;
}

@media screen and (max-width: 700px) {
  .statement-page {
    padding: 20px 0;
  }

  .statement-title {
    padding: 22px 20px 8px;
    font-size: 21px;
  }

  .statement-content {
    padding: 8px 20px 25px;
    font-size: 13px;
  }

  .statement-content h3 {
    font-size: 16px;
  }

  .contact-box {
    display: flex;
    align-items: flex-start;
    flex-direction: column;
    gap: 3px;
  }
}
</style>
