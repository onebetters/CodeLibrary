package com.zzc.easyExcel;

import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Administrator
 */
@UtilityClass
public class ResponseBuilder {

    /**
     * EasyExcel web写操作设置
     *
     * @param response response
     * @param fileName 文件名
     */
    public static void setExportResponseHeader(final HttpServletResponse response, final String fileName)
            throws UnsupportedEncodingException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        //这里URLEncoder.encode可以防止中文乱码 当然和easyExcel没有关系
        String encodeFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + encodeFileName + ".xlsx");
    }
}
