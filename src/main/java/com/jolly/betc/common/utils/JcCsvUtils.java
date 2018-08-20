package com.jolly.betc.common.utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.io.*;
import java.util.List;

/**
 * CSV工具类
 *
 * @author zuoqiuming
 * @since 2018/07/25 13:52
 */
public class JcCsvUtils {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JcCsvUtils.class);

    /**
     * 把dataList生成到csv
     * 会自动添加标题
     *
     * @param dataList
     * @param
     */
    public static void toCsvFile(List<?> dataList, Class<?> pojoClass, File csvFile) {
        toCsvFile(dataList, pojoClass, true, csvFile, "GBK");
    }

    /**
     * 把dataList生成到csv文件
     *
     * @param dataList
     * @param pojoClass
     * @param titleAppend:是否需要添加标题
     * @param csvFile
     */
    public static void toCsvFile(List<?> dataList, Class<?> pojoClass, boolean titleAppend, File csvFile, String encoding) {
        if (dataList == null || dataList.isEmpty() || csvFile == null) {
            return;
        }
        BufferedWriter bw = null;
        try {
            List<List<Object>> rowList = JcPackingUtils.convetBeansToList(dataList, titleAppend, pojoClass);
            bw = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(csvFile, true), encoding));
            for (List<Object> row : rowList) {
                for (Object column : row) {
                    String content = StringEscapeUtils.escapeCsv(String.valueOf(column));
                    if (NumberUtils.isDigits(content) && content.startsWith("0")) {
                        //防止0开头的字符串不能正常显示
                        //同时也会导致数据失真,要谨慎选择,也可以选择导出excel方式
                        bw.write("\t");
                    }
                    bw.write(content);
                    bw.write(",");
                }
                bw.newLine();
            }
            bw.flush();
        } catch (Exception e) {
            logger.error("toCsvFile error", e);
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * 基于pojo的方式生成csv(建议少量对象)
     * 把dataList生成到CSV格式的文本
     * 要关注内存的占用
     * 如果数据量较大,例如10w+,则建议分批生成到文件,然后进行压缩下载
     *
     * @param dataList
     * @param pojoClass
     * @param
     */
    public static StringBuilder toCsvText(List<?> dataList, Class<?> pojoClass) throws Exception {
        return toCsvText(JcPackingUtils.convetBeansToList(dataList, true, pojoClass));
    }

    /**
     * 把dataList生成到CSV格式的文本
     * 要关注内存的占用
     * 如果数据量较大,例如10w+,则建议分批生成到文件,然后进行压缩下载
     *
     * @param dataList
     * @return
     */
    public static StringBuilder toCsvText(List<List<Object>> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return new StringBuilder();
        }
        StringBuilder csvBuffer = new StringBuilder();
        for (List<Object> row : dataList) {
            for (Object column : row) {
                String content = StringEscapeUtils.escapeCsv(String.valueOf(column));
                if (NumberUtils.isDigits(content) && content.startsWith("0")) {
                    //防止0开头的字符串不能正常显示
                    //同时也会导致数据失真,要谨慎选择,也可以选择导出excel方式
                    content = "\t" + content;
                }
                csvBuffer.append(content).append(",");
            }
            csvBuffer.append("\r\n");
        }
        return csvBuffer;
    }

}