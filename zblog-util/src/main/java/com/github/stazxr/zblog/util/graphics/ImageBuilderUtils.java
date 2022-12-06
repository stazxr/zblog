package com.github.stazxr.zblog.util.graphics;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;

/**
 * 动态生成图片工具类
 *
 * @author SunTao
 * @since 2021-08-11
 */
@Slf4j
@SuppressWarnings("all")
public class ImageBuilderUtils {
    /**
     * 根据图片文字动态生成图片
     *
     * @param content 内容
     * @param uploadPath 上传路径
     * @return 文件上传地址
     */
    public static String buildFontImage(String content, String uploadPath) {
        try {
            Font font = new Font("微软雅黑", Font.BOLD, 36);

            // 计算字体宽度和高度
            // FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font); // 过时
            // int width = 0;
            // for (int i = 0; i < content.length(); i++) {
            //     width += metrics.charWidth(content.charAt(i));
            // }
            // int height = metrics.getHeight();
            // int ascent = metrics.getAscent();
            // int leading = metrics.getLeading();

            FontMetrics fontMetrics = getFontMetrics(font);
            int width = fontMetrics.stringWidth(content);
            int height = fontMetrics.getHeight();
            int ascent = fontMetrics.getAscent();
            int leading = fontMetrics.getLeading();

            // 计算图片大小，设置图片信息，比例为16:9   height = width * (9 / 16)
            int imageWidth = width + 100;
            int imageHeight = imageWidth * 9 / 16;
            BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);

            // 获取画笔
            Graphics2D graphics = image.createGraphics();

            // 设置画笔的基本信息
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            graphics.setBackground(Color.BLACK);

            // 填充红色的正方形
            graphics.setColor(Color.BLACK);
            int rectX = (imageWidth - width) / 2;
            int rectY = (imageHeight - height) / 3;
            graphics.fillRect(rectX, rectY, width, height);

            // 填充文字
            graphics.setColor(Color.WHITE);
            graphics.setFont(font);
            graphics.drawString(content, rectX, rectY + ascent + leading);
            graphics.dispose();

            // 输出图片
            String savePath = uploadPath + "articleImgBuilder_" + System.currentTimeMillis() + ".jpg";
            ImageIO.write(image, "JPEG", new FileOutputStream(savePath));
            return savePath;
        } catch (Exception e) {
            log.error("生成文章略缩图失败", e);
            return "";
        }
    }

    private static FontMetrics getFontMetrics(Font font) {
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        return g.getFontMetrics(font);
    }
}
