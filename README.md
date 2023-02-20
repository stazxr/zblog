## 博客介绍

<p align=center>
  <a href="https://github.com/stazxr/zblog">
    <img src="https://file.suntaoblog.com/image/github/header.png" alt="风丶宇的个人博客" style="border-radius: 50%">
  </a>
</p>
<p align=center>
   基于 Springboot + Vue 开发的前后端分离博客
</p>

<p align="center">
   <a target="_blank" href="https://github.com/stazxr/zblog">
      <img alt="" src="https://img.shields.io/badge/%E6%81%8B%E9%95%BF%E5%AE%89%E5%85%AE-%E4%B8%BB%E9%A1%B5-pink"/>
      <img alt="" src="https://img.shields.io/github/license/stazxr/zblog"/>
      <img alt="" src="https://img.shields.io/badge/JDK-1.8+-green.svg"/>
      <img alt="" src="https://img.shields.io/badge/springboot-2.5.2.RELEASE-green"/>
      <img alt="" src="https://img.shields.io/badge/vue-2.6.14-green"/>
      <img alt="" src="https://img.shields.io/badge/mysql-5.1.47-green"/>
      <img alt="" src="https://img.shields.io/badge/mybatis--plus-3.5.2-green"/>
      <img alt="" src="https://img.shields.io/badge/redis-6.1.3-green"/>
      <img alt="" src="https://img.shields.io/badge/rabbitmq-3.8.0-green"/>
   </a>
</p>

[在线地址](#在线地址) | [目录结构](#目录结构) | [项目特点](#项目特点) | [运行环境](#运行环境) | [开发环境](#开发环境) | [快速开始](#快速开始) | [注意事项](#注意事项) | [交流群](#交流群) | [鸣谢](#鸣谢) | [项目截图](#项目截图)

## 在线地址

**项目链接：** [https://www.suntaoblog.com](https://www.suntaoblog.com)

**后台链接：** [https://admin.suntaoblog.com](https://admin.suntaoblog.com)

测试账号：test，密码：123456，可登入后台查看。

**Github地址（后端）：** [https://github.com/stazxr/zblog](https://github.com/stazxr/zblog)

**Github地址（前台）：** [https://github.com/stazxr/zblog-web-portal](https://github.com/stazxr/zblog-web-portal)

**Github地址（后台）：** [https://github.com/stazxr/zblog-web-admin](https://github.com/stazxr/zblog-web-admin)

您的 star 是我坚持的动力，感谢大家的支持，欢迎提交 pr 共同改进项目。

## 目录结构

**前端项目**

zblog-web-portal：[前台](https://github.com/stazxr/zblog)

zblog-web-admin：[后台](https://github.com/stazxr/zblog-web-admin)

**后端项目**

zblog：[后端](https://github.com/stazxr/zblog)

**数据库**

SQL 文件位于[后端](https://github.com/stazxr/zblog)项目的 doc 目录下，需要 MYSQL5.7 版本。

base.sql（基础表）：由 user、role、permission、router、interface、dict、log、file、version、server_node 及一些中间表和临时表组成

service.sql（业务表）：由 article、article_category、article_tag、article_column、friend_link、website_config、visitor、talk、page、
album、message、comment 及一些中间表和临时表组成。

xxljob.sql（任务表）：由 xxljob 自身的表和部分自定义的表组成。

ps：安装 MYSQL 后，请先执行 base.sql，然后在执行 service.sql。

**后端目录结构及功能介绍**

后端目录是按模块划分的，总体分为：util、core、base、service（业务）、log（扩展）、xxljob（扩展）、notify（扩展） 模块

```
zblog
├── doc                      --  简单的笔记
├── sql                      --  数据库初始化脚本（5.7）
├── zblog-base               --  ZBLOG 框架的基础功能组件
├────── component            --  三方组件封装
├────────── captcha          --  验证码封装（基于 EasyCaptcha），支持算术、中文、数字等类型
├────────── email            --  邮箱封装，支持发送普通文本邮件、HTML邮件、带图片的邮件、带附件的邮件 
├────────── file             --  文件上传组件，支持本地、阿里云、七牛云上传
├────────── id               --  全局 ID 生成器，支持单机、多机部署两种模式
├────────── router           --  路由管理器，实现系统接口扫描和加载功能
├────────── security         --  安全配置，实现基于 nimbus-jose-jwt 的授权、认证、续签方案
├────────── webssh           --  WebSocket 支持，主要用于实现 Webssh 终端
├────── controller           --  控制层，包含用户管理、角色管理、权限管理、路由管理、字典管理、操作日志、接口日志、数据监控、服务监控、版本维护、存储管理、节点管理等模块
├────── converter            --  对象转换器（基于 mapstruct），支持 Vo、Dto、Entity 之间的转换
├────── domain               --  领域对象：Bo、Dto、Entity、Enums、Vo
├────── mapper               --  数据层
├────── runner               --  执行器，用于系统启动后执行一些操作，如初始化路由，初始化验证码、初始化黑白名单等
├────── service              --  业务层
├────── util                 --  Base 组件使用的工具类和常量类，eg：GenerateIdUtils
├── zblog-core               --  ZBLOG 框架的核心组件
├────── annotation           --  自定义注解
├────── base                 --  基础类，BaseEntity、BaseDto、BaseVo、BaseMapper、BaseConverter 等
├────── cache                --  自定义系统缓存，支持 Memory 和 Redis 两种模式
├────── config               --  配置中心，缓存、线程、WebMvc、MybatisPlus、统一返回
├────── enums                --  自定义枚举，ResultCode
├────── exception            --  自定义异常，全局异常处理
├────── handler              --  处理器，实现 mybatis-plus 的字段自动填充
├────── model                --  模型对象，Result，ErrorMeta
├────── util                 --  Core 组件使用的工具类扩展
├── zblog-log                --  ZBLOG 框架的日志组件，提供切面日志等功能，可以通过 @EnableLog 注解来开启日志功能
├────── annotation           --  日志相关注解，包含 @EnableLog（是否开启全局注解），@IgnoredLog（不记录日志），@Log（操作日志） 注解
├────── aop                  --  日志切面的实现
├────── domain               --  领域对象：Bo、Dto、Entity、Enums、Vo
├────── mapper               --  数据层
├────── service              --  业务层
├── zblog-notify             --  ZBLOG 框架的消息组件（待实现）
├── zblog-service            --  ZBLOG 框架的业务组件，主要是 ZBLOG 的业务功能（发博文、发说说、留言等）
├────── controller           --  控制层，包含文章相关模块，网站管理相关模块等
├────── converter            --  对象转换器（基于 mapstruct），支持 Vo、Dto、Entity 之间的转换
├────── domain               --  领域对象：Bo、Dto、Entity、Enums、Vo
├────── mapper               --  数据层
├────── rabbitmq             --  消息队列，内置文章定时发布功能（需安装 RabbitMQ 环境）
├────── service              --  业务层
├────── strategy             --  策略模式，内置文章搜索策略（目前只支持 Mysql）
├────── util                 --  Core 组件使用的工具类和常量类
├────── ZblogApplocation     --  项目启动类，一切都从这里开始~
├── zblog-util               --  ZBLOG 框架的工具组件，实现字符串、日期、加解密、正则、POI、IO、Assert、OSS、JSCH 等工具的的封装
├── zblog-xxljob             --  ZBLOG 框架的任务组件（基于 XXLJOB）（待实现）
└── pom.xml                  --  pom.xml，版本中心
```

## 项目特点

- 前台参考 Hexo 设计，美观简洁，响应式体验好
- 后台参考 ElAdmin 设计，侧边栏，历史标签，面包屑自动生成
- 前后端分离部署，适应当前潮流。
- 基于 nimbus-jose-jwt 的授权、认证、续签方案
- 支持动态权限修改，采用RBAC模型，前端菜单、按钮和后台权限实时更新
- 统一返回结果、返回状态码，方便前台判断请求结果
- 切面日志，支持统计系统的接口日志和用户的操作日志
- 前后端统一异常拦截处理，统一输出异常，避免繁琐的判断
- 采用 WangEditor 编辑器（后续会扩展 Markdown 编辑器）
- 参考 CSDN 的文章发布样式，支持定时发布、文章审核、代码高亮、上传视频和附件等特性
- 支持发布说说、相册，随时分享趣事
- 留言采用弹幕墙组件，更加炫酷
- 脚手架使用，除 service 模块是业务模块外，其他模块可做脚手架使用
- 代码严格遵循阿里巴巴开发规范，模块化开发，利于开发者学习

## 运行环境

**服务器：** 阿里云2核2G 宽带1Mbps CentOS8.2

**CDN：** 阿里云全站加速

**对象存储：** 阿里云OSS

**数据库：** 阿里云MySQL

**缓存：** 阿里云Redis

## 开发环境

| 开发环境        | 版本    |
|-------------|-------|
| JDK         | 1.8   |
| MySQL       | 5.7   |
| Redis       | 6.1.3 |
| RabbitMQ    | 3.8.0 |

## 快速开始

### 本地环境搭建

详见文章：[本地环境搭建（待补充）]()

### 项目配置指导

详见文章：[项目配置指导（待补充）]()

### 服务器部署项目

详见文章：[项目部署教程-服务器篇（待补充）]()

### 容器部署项目

详见文章：[项目部署教程-容器篇（待补充）]()

### 阿里云CDN加速配置指导

详见文章：[阿里云CDN加速配置指导（待补充）]()

## 注意事项

- 项目拉下来运行后，可到后台管理页面网站配置处修改博客相关信息
- 邮箱配置，第三方授权配置需要自己申请
- 项目默认采用本地文件上传模式，如果需要使用阿里云OSS，请自己申请，七牛云可以免费申请10G，可参考文章【[七牛云OSS申请（待补充）]()】

## 项目总结

博客作为新手入门项目是十分不错的，项目所用的技术栈覆盖的也比较广，适合初学者学习。主要难点在于权限管理、第三方登录、websocket这块。做的不好的地方请大家见谅，有问题的或者有好的建议可以私聊联系我。

## 鸣谢

- 感谢 [PanJiaChen](https://github.com/elunez/eladmin) 大佬提供的前端模板（后台）

- 感谢 [风丶宇](https://github.com/X1192176811/blog) 大佬提供的前端模板（前台），仓库已下线

## 交流群

QQ：760210629

## 项目截图

![首页01](https://file.suntaoblog.com/image/github/page1.jpg)

![首页02](https://file.suntaoblog.com/image/github/page2.jpg)

![搜索](https://file.suntaoblog.com/image/github/page3.jpg)

![专栏](https://file.suntaoblog.com/image/github/page4.jpg)

![分类](https://file.suntaoblog.com/image/github/page5.jpg)

![标签](https://file.suntaoblog.com/image/github/page6.jpg)

![说说](https://file.suntaoblog.com/image/github/page7.jpg)

![友链](https://file.suntaoblog.com/image/github/page8.jpg)

![但幕墙](https://file.suntaoblog.com/image/github/page9.jpg)

![文章-白天](https://file.suntaoblog.com/image/github/page10.jpg)

![文章-黑夜](https://file.suntaoblog.com/image/github/page11.jpg)