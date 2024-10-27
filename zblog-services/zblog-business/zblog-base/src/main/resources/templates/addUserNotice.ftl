<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html lang="zh"  xmlns:th="http://www.thymeleaf.org">
<head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <style type="text/css">
        @page { margin: 0; }
    </style>
</head>
<body style="margin: 0; padding: 0; color: #000; font: 100% SimSun, Microsoft YaHei, Times New Roman, Verdana, Arial, Helvetica, sans-serif;">
<div style="height: auto; width: 820px; min-width: 820px; margin: 20px auto 0; border: 1px solid #eee;">
    <div style="padding: 10px 10px 0;">
        <p style="margin-bottom: 10px;padding-bottom: 0;">尊敬的用户，您好：</p>
        <p style="text-indent: 2em; margin-bottom: 10px;">您的账号已开立成功，账号：[[${username}]]，初始密码：[[${password}]]。</p>
        <div class="foot-hr hr" style="z-index: 111; width: 800px; margin: 100px auto 0; border-top: 1px solid #DA251D;"></div>
        <div style="text-align: center; font-size: 12px; padding: 20px 0; font-family: Microsoft YaHei, serif;">
            Copyright &copy;[[${year}]]
            <a style="color: #999;" href="https://www.suntaoblog.com/" target="_blank">Z-BLOG</a> 后台管理系统 All Rights Reserved.
        </div>
    </div>
</div>
</body>
</html>
