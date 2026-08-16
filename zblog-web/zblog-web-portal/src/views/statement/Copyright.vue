<template>
  <div class="statement-page">
    <v-container max-width="900">
      <v-card class="statement-card" flat>
        <!-- 标题 -->
        <v-card-title class="statement-title">
          版权声明
        </v-card-title>

        <v-card-text class="statement-content">

          <!-- 一、原创内容 -->
          <section>
            <h3>一、原创内容</h3>

            <p>
              本站发布的原创文章、教程、经验分享、技术文档及其他原创内容，
              除特别声明外，相关著作权归本站作者所有。
            </p>

            <p>
              未经许可，任何个人或组织不得对本站原创内容进行未经授权的商业使用、
              大量复制或其他可能损害作者合法权益的使用。
            </p>

            <p>
              如需转载或引用本站原创内容，请注明作者及原文出处，并尽可能保留原文链接。
              对于合理引用，请遵守相关法律法规及版权规定。
            </p>
          </section>

          <!-- 二、开源项目 -->
          <section>
            <h3>二、开源项目及代码</h3>

            <p>
              本站网站程序及相关源代码以开源项目形式发布，项目源代码采用
              <strong>MIT License</strong> 进行授权。
            </p>

            <p>
              在遵守 MIT License 相关条款的前提下，您可以对项目源代码进行使用、
              复制、修改、合并、发布、分发、再许可及商业使用。
            </p>

            <p>
              MIT License 仅适用于本项目属于开源许可证授权范围内的源代码，
              不代表对本站文章、图片、音乐、视频、字体以及其他第三方资源授予任何使用权利。
            </p>

            <p>
              具体授权范围及相关条件，请以项目代码仓库中的
              <strong>LICENSE</strong> 文件为准。
            </p>
          </section>

          <!-- 三、第三方内容及素材 -->
          <section>
            <h3>三、第三方内容及素材</h3>

            <p>
              本站部分页面可能使用或引用来自第三方的文字、图片、插画、音乐、
              视频、字体、图标、软件、代码、文档、商标及其他网络资源。
            </p>

            <p>
              上述第三方资源的著作权、商标权及其他相关权利归相应权利人所有，
              不属于本站开源项目 MIT License 的授权范围。
            </p>

            <p>
              本站部分图片、音乐、视频及其他素材可能通过购买、授权、
              开放许可或其他合法方式取得使用权限，并根据相应的授权条件进行使用。
            </p>

            <p>
              对于通过外部 URL、CDN 或其他网络地址加载的第三方资源，
              相关资源本身不属于本站源代码的一部分，本站也不会因此取得相关资源的所有权。
            </p>

            <p>
              使用本站开源项目进行部署、二次开发或其他用途时，
              如相关配置继续引用第三方资源，使用者应自行确认相关资源的版权及授权范围，
              并遵守相应权利人的许可协议及使用要求。
            </p>
          </section>

          <!-- 四、Demo 与展示环境 -->
          <section>
            <h3>四、Demo 及展示环境</h3>

            <p>
              本站后续可能提供基于本项目部署的在线 Demo 或示例环境，
              主要用于展示项目功能及使用效果。
            </p>

            <p>
              Demo 中展示的部分文章、图片、音乐、视频、字体及其他内容，
              可能属于本站作者原创内容或第三方资源。
            </p>

            <p>
              Demo 中使用的第三方资源仅代表该展示环境中的使用情况，
              不属于本项目 MIT License 的授权内容，也不会随项目源代码一并授权或分发。
            </p>
          </section>

          <!-- 五、部署与技术支持 -->
          <section>
            <h3>五、部署与技术支持</h3>

            <p>
              本项目代码以 MIT License 开源，任何人均可以根据许可证条款
              自行部署和使用本项目。
            </p>

            <!-- <p>
              如果您在项目部署、环境配置、服务器配置、功能使用过程中
              遇到问题，也可以联系作者获取相关技术支持。
            </p>

            <p>
              对于需要人工协助完成的部署、环境配置、问题排查、功能定制或二次开发等服务，
              作者可能根据实际工作内容提供有偿技术支持。
            </p> -->
          </section>

          <!-- 六、转载与引用 -->
          <section>
            <h3>六、转载与引用</h3>

            <p>
              欢迎在遵守相关法律法规及版权规定的前提下合理引用本站内容。
            </p>

            <p>
              对于转载或引用本站原创文章、教程及其他内容，
              请注明作者、原文出处，并保留原文链接。
            </p>

            <p>
              转载或引用时不得对原文进行可能导致事实歪曲、断章取义
              或造成读者误解的修改。
            </p>
          </section>

          <!-- 七、版权问题 -->
          <section>
            <h3>七、版权问题</h3>

            <p>
              如果您认为本站发布的文字、图片、音乐、视频、代码或其他内容
              侵犯了您的合法权益，请及时与本站联系。
            </p>

            <p>
              您可以通过
              <router-link to="/infringement">
                侵权联系
              </router-link>
              页面提供的联系方式进行反馈。
            </p>

            <p>
              为便于我们快速核实和处理相关问题，请尽可能提供具体的内容链接、
              权利证明及相关说明。我们将在收到有效反馈后及时进行核实，
              并根据实际情况采取相应处理措施。
            </p>
          </section>

          <!-- 八、声明更新 -->
          <section>
            <h3>八、声明更新</h3>

            <p>
              本版权声明可能根据网站内容、项目授权方式及相关法律法规的变化进行调整。
              更新后的声明将在本页面发布。
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

/* =========================
   标题
   ========================= */

.statement-title {
  padding: 28px 32px 10px;
  font-size: 24px;
  font-weight: 600;
}

/* =========================
   内容
   ========================= */

.statement-content {
  padding: 10px 32px 32px;
  color: #555;
  font-size: 14px;
  line-height: 1.9;
}

.statement-content section {
  margin-top: 28px;
}

.statement-content section:first-child {
  margin-top: 10px;
}

.statement-content h3 {
  margin-bottom: 10px;
  font-size: 17px;
  font-weight: 600;
  color: #333;
}

.statement-content p {
  margin: 9px 0;
  text-align: justify;
}

.statement-content strong {
  color: #333;
  font-weight: 600;
}

/* 页面内部链接 */

.statement-content a {
  color: inherit;
  text-decoration: underline;
}

.statement-content a:hover {
  opacity: .7;
}

/* =========================
   手机适配
   ========================= */

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
    line-height: 1.85;
  }

  .statement-content section {
    margin-top: 24px;
  }

  .statement-content h3 {
    margin-bottom: 8px;
    font-size: 16px;
  }

  .statement-content p {
    margin: 8px 0;
    text-align: left;
  }
}
</style>
