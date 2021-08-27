package com.zzc.utils;

import com.sun.imageio.plugins.bmp.BMPImageReader;
import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.jpeg.JPEGImageReader;
import com.sun.imageio.plugins.png.PNGImageReader;
import com.sun.imageio.plugins.wbmp.WBMPImageReader;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

/**
 * @author Administrator
 */
@Slf4j
@UtilityClass
public class ImageUtils {

    private static final String WX_LOGO_URL_PREFIX = "https://wx.qlogo.cn/mmopen/vi_32/";

    // 微信用户头像图片的 URL。URL 最后一个数值代表正方形头像大小（有 0、46、64、96、132 数值可选，0 代表 640x640 的正方形头像，46 表示 46x46 的正方形头像，剩余数值以此类推。默认132），用户没有头像时该项为空。若用户更换头像，原有头像 URL 将失效。
    private static final String WX_LOGO_URL_SIZE_SUFFIX_0 = "/0";

    private static final String WX_LOGO_URL_SIZE_SUFFIX_132 = "/132";

    public String getImageSuffix(final byte[] bytes) {
        try (MemoryCacheImageInputStream memoryCacheImageInputStream = new MemoryCacheImageInputStream(new ByteArrayInputStream(bytes))) {
            final Iterator<ImageReader> iterator = ImageIO.getImageReaders(memoryCacheImageInputStream);
            while (iterator.hasNext()) {
                final ImageReader reader = iterator.next();
                final String format = reader.getFormatName();
                if (StringUtils.isNotBlank(format)) {
                    return StringUtils.lowerCase(format, Locale.ENGLISH);
                }
                if (reader.getClass().isAssignableFrom(JPEGImageReader.class)) {
                    return "jpeg";
                }
                if (reader.getClass().isAssignableFrom(PNGImageReader.class)) {
                    return "png";
                }
                if (reader.getClass().isAssignableFrom(GIFImageReader.class)) {
                    return "gif";
                }
                if (reader.getClass().isAssignableFrom(BMPImageReader.class)) {
                    return "bmp";
                }
                if (reader.getClass().isAssignableFrom(WBMPImageReader.class)) {
                    return "wbmp";
                }
            }
        } catch (IOException e) {
            log.error("解析文件后缀名异常", e);
        }
        return null;
    }

    public String compress(String imageUrl) {
        if (StringUtils.isBlank(imageUrl)) {
            return imageUrl;
        }

        if (isWxLogo(imageUrl)) {
            return compressWxLogo(imageUrl);
        }
        return imageUrl;
    }

    private boolean isWxLogo(String imageUrl) {
        return imageUrl.startsWith(WX_LOGO_URL_PREFIX);
    }

    private String compressWxLogo(String imageUrl) {
        // 简单处理，将/0结尾的头像转换为/132；132尺寸在小程序里已足够使用，而再压缩所减小的文件大小并不明显
        if (imageUrl.endsWith(WX_LOGO_URL_SIZE_SUFFIX_0)) {
            return imageUrl.substring(0, imageUrl.length() - WX_LOGO_URL_SIZE_SUFFIX_0.length()) + WX_LOGO_URL_SIZE_SUFFIX_132;
        }
        return imageUrl;
    }
}
