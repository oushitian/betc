package com.jolly.betc.common.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.jolly.betc.common.utils.anno.JcExcelImport;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Excel工具类
 * 目前支持导入的
 *
 * @author zuoqiuming
 * @since 2018/07/25 13:45
 */
public class JcExcelUtils {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JcExcelUtils.class);

    /**
     * 识别是否为Excel文件
     *
     * @param inputStream:流不会进行关闭
     * @return
     */
    public static boolean isExcel(BufferedInputStream inputStream) {
        if (inputStream == null) {
            return false;
        }
        try {
            if (POIFSFileSystem.hasPOIFSHeader(inputStream)) {
                return true;
            }
            if (POIXMLDocument.hasOOXMLHeader(inputStream)) {
                return true;
            }
        } catch (IOException e) {
            logger.warn("isExcel error", e);
            return false;
        }
        return false;
    }

    /**
     * parse指定的流为相应的泛型类集合,方法外部进行流的关闭处理
     *
     * @param inputStream Excel文件输入流
     * @param clazz       指定泛型Class
     * @param <T>         泛型
     * @return 当输入流符合要求时, 返回集合数据, 当文件类型不符合要求的, 返回null
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> parseObject(InputStream inputStream, Class<T> clazz) throws Exception {
        if (inputStream == null) {
            return new ArrayList<>(0);
        }
        Workbook wb;
        List<T> list = null;
        try {
            Field[] fields = ReflectionUtils.getClassFieldsAndSuperClassFields(clazz);
            Map<String, Field> textToKey = new HashMap<String, Field>();
            JcExcelImport excelField;
            for (Field field : fields) {
                excelField = field.getAnnotation(JcExcelImport.class);
                if (excelField == null) {
                    continue;
                }
                textToKey.put(excelField.name().trim(), field);
            }
            wb = WorkbookFactory.create(inputStream);
            Sheet sheet = wb.getSheetAt(0);
            Row title = sheet.getRow(0);
            // 标题数组，后面用到，根据索引去标题名称，通过标题名称去字段名称用到 textToKey
            String[] titles = new String[title.getPhysicalNumberOfCells()];
            for (int i = 0; i < title.getPhysicalNumberOfCells(); i++) {
                titles[i] = StringUtils.trimToEmpty(title.getCell(i).getStringCellValue());
            }
            list = new ArrayList<T>();
            T e;
            int rowIndex = 0;
            int columnCount = titles.length;
            Cell cell;
            Row row;
            for (Iterator<Row> it = sheet.rowIterator(); it.hasNext(); ) {
                row = it.next();
                if (rowIndex++ == 0) {
                    continue;
                }
                if (row == null) {
                    break;
                }
                //默认构造器
                e = clazz.newInstance();
                boolean containValue = false;
                for (int i = 0; i < columnCount; i++) {
                    cell = row.getCell(i);
                    if (readCellContent(textToKey.get(titles[i]), cell, e)) {
                        containValue = true;
                    }
                }
                if (containValue) {
                    list.add(e);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return list;
    }

    /**
     * 从单元格读取数据，根据不同的数据类型，使用不同的方式读取<br>
     * 有时候POI自作聪明，经常和我们期待的数据格式不一样，会报异常，<br>
     *
     * @param cell 单元格对象
     * @param bean
     * @throws Exception
     */
    private static boolean readCellContent(Field field, Cell cell, Object bean) throws Exception {
        if (cell == null || field == null) {
            return false;
        }
        Object cellValue = null;

        switch (cell.getCellType()) {
            case XSSFCell.CELL_TYPE_STRING:
                String cellValueStr = cell.getStringCellValue();
                if (cellValueStr != null) {
                    cellValue = cellValueStr.trim();
                }
                break;
            case XSSFCell.CELL_TYPE_NUMERIC:
                cellValue = cell.getNumericCellValue();
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    Date date = DateUtil.getJavaDate(cell.getNumericCellValue());
                    if (date != null) {
                        cellValue = DateFormatUtils.format(date, "yyyy/MM/dd HH:mm:ss");
                    }
                } else {
                    DataFormatter formatter = new DataFormatter();
                    cellValue = formatter.formatCellValue(cell);
                    // cellValue = clearPointZore(String.valueOf(cellValue));
                }
                break;
            case XSSFCell.CELL_TYPE_BLANK:
                cellValue = null;
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN:
                cellValue = cell.getBooleanCellValue();
                break;
            case XSSFCell.CELL_TYPE_ERROR:
                cellValue = cell.getErrorCellValue();
                break;
            case XSSFCell.CELL_TYPE_FORMULA:
                cellValue = cell.getCellFormula();
                break;
            default:
                cellValue = cell.getStringCellValue();
                break;
        }
        if (cellValue == null) {
            return false;
        }
        field.setAccessible(true);
        try {
            cellValue = ConvertUtils.convert(String.valueOf(cellValue), field.getType());
            if (cellValue != null) {
                field.set(bean, cellValue);
            }
        } catch (Exception ex) {
//            logger.debug("convert error={}",ex.getMessage());
        }
        return true;
    }

    /**
     * 把dataList生成到97~2003版本中
     *
     * @param dataList
     * @param excelXls 97~2003版本
     * @return
     */
    public static <T> boolean toXLS(List<T> dataList, Class<T> pojoClass, File excelXls, String sheetName) {
        return exportExcel(dataList, pojoClass, excelXls, ExcelType.HSSF, sheetName);
    }

    /**
     * 把dataList生成到excel2007以上版本文件
     *
     * @param dataList
     * @param excelXlsx excel2007以上版本文件
     * @return
     */
    public static <T> boolean toXLSX(List<T> dataList, Class<T> pojoClass, File excelXlsx, String sheetName) {
        return exportExcel(dataList, pojoClass, excelXlsx, ExcelType.XSSF, sheetName);
    }

    /**
     * 大数据量的导出,采用sxssf方式
     *
     * @param dataList
     * @param sheetName
     * @return
     */
    public static <T> Workbook appendToBigExcel(List<T> dataList, Class<T> pojoClass, String sheetName) {
        ExportParams outParams = new ExportParams();
        outParams.setSheetName(sheetName);
        return ExcelExportUtil.exportBigExcel(outParams, pojoClass, dataList);
    }

    /**
     * 生成大Excel
     *
     * @param workbook
     * @param excel
     */
    public static boolean toBigExcel(Workbook workbook, File excel) {
        FileOutputStream fos = null;
        try {
            makeDirIfNotExists(excel);
            fos = new FileOutputStream(excel);
            workbook.write(fos);
            return true;
        } catch (Exception e) {
            logger.warn("toBigExcel error", e);
        } finally {
            closeExportBigExcel(fos);
        }
        return false;
    }

    /**
     * 关闭大文件输出流
     *
     * @param out
     */
    public static void closeExportBigExcel(OutputStream out) {
        IOUtils.closeQuietly(out);
        ExcelExportUtil.closeExportBigExcel();
    }

    /**
     * 自动构建目录
     *
     * @param file 已生成的文件(非目录)
     */
    public static void makeDirIfNotExists(File file) {
        //自动构建目录
        File fileDir = file.getParentFile();
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
    }

    /**
     * 导出excel基础方法,使用easypoi做底层支持
     *
     * @param dataList
     * @param excel
     * @param excelType
     * @param sheetName
     * @return
     */
    private static <T> boolean exportExcel(List<T> dataList, Class<T> pojoClass, File excel, ExcelType excelType, String sheetName) {
        if (dataList == null) {
            logger.warn("exportExcel's dataList is empty");
            return false;
        }
        if (excel == null) {
            logger.warn("excel file is empty");
            return false;
        }
        makeDirIfNotExists(excel);
        ExportParams outParams = new ExportParams();
        outParams.setSheetName(sheetName);
        outParams.setType(excelType);
        FileOutputStream fos = null;
        try {
            Workbook workbook = ExcelExportUtil.exportExcel(outParams, pojoClass, dataList);
            fos = new FileOutputStream(excel);
            workbook.write(fos);
            return true;
        } catch (Exception e) {
            logger.warn("exportExcel error", e);
        } finally {
            IOUtils.closeQuietly(fos);
        }
        return false;
    }


}
