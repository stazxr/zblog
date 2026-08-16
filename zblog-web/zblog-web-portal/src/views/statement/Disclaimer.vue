<template>
  <div class="statement-page">
    <v-container max-width="900">
      <v-card class="statement-card" flat>
        <v-card-title class="statement-title">
          免责声明
        </v-card-title>

        <v-card-text class="statement-content">
          <section>
            <h3>一、内容性质</h3>
            <p>
              本站内容主要用于个人学习、技术研究、经验分享及交流。
              文章中的观点、技术方案、配置方法、代码示例及相关说明，
              均代表作者在发布时的个人理解和实践。
            </p>
          </section>

          <section>
            <h3>二、内容准确性</h3>
            <p>
              作者会尽力保证本站内容的准确性和完整性，但由于软件版本、
              操作系统、运行环境、依赖组件以及实际业务场景可能存在差异，
              本站无法保证所有内容始终适用于所有环境。
            </p>
            <p>
              技术文章可能随着相关技术的发展而失效或发生变化，
              请以相关软件、框架及官方文档的最新信息为准。
            </p>
          </section>

          <section>
            <h3>三、使用风险</h3>
            <p>
              使用本站提供的代码、配置、脚本、技术方案或其他资料前，
              请根据实际环境进行充分测试和风险评估。
            </p>
            <p>
              对于因直接或间接使用本站内容而产生的数据丢失、系统故障、
              服务中断或其他损失，请在使用前自行评估相关风险。
            </p>
          </section>

          <section>
            <h3>四、第三方链接</h3>
            <p>
              本站可能包含指向第三方网站、开源项目或其他外部资源的链接。
              相关网站及资源由第三方独立运营，其内容、服务及相关政策
              由相应权利人负责。
            </p>
            <p>
              本站不对第三方网站的内容、可用性、准确性或安全性作出保证。
            </p>
          </section>

          <section>
            <h3>五、内容更新</h3>
            <p>
              本站内容可能因技术发展、软件版本变化或作者实践经验变化
              而进行调整、修改或删除，恕不另行通知。
            </p>
          </section>
        </v-card-text>
      </v-card>
    </v-container>
  </div>
</template>

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
}
</style>
