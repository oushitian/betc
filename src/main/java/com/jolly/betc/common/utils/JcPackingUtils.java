package com.jolly.betc.common.utils;

import com.jolly.betc.common.utils.anno.JcExportField;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 封装bean的property为list工具类
 * Created by zqm on 2016/9/9.
 */
public class JcPackingUtils {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(JcPackingUtils.class);

    /**
     * bean的列表封装成field值的列表,
     * ExportExcludingFiled注解的field不包含
     *
     * @param beanList     bean的列表
     * @param titlePrepend
     * @return bean的列表封装成field值的列表
     */
    public static List<List<Object>> convetBeansToList(List<?> beanList, boolean titlePrepend) throws Exception {
        if (beanList == null || beanList.isEmpty()) {
            return new ArrayList<List<Object>>(0);
        }
        return convetBeansToList(beanList, titlePrepend, beanList.get(0).getClass());
    }

    public static List<List<Object>> convetBeansToList(List<?> beanList, boolean titlePrepend, Class targetClass) throws Exception {
        if (targetClass == null) {
            return convetBeansToList(beanList, titlePrepend);
        }
        List<List<Object>> rowList = new ArrayList<>();
        Field[] fields = targetClass.getDeclaredFields();
        List<ExportColumnField> targetFields = new ArrayList<>(fields.length);
        if (titlePrepend) {
            List<Object> columnNameList = new ArrayList<>(fields.length);
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                JcExportField fieldAnno = field.getAnnotation(JcExportField.class);
                if (fieldAnno != null) {
                    field.setAccessible(true);
                    //get field title as file colume name
                    targetFields.add(new ExportColumnField(field, fieldAnno));
                    if (fieldAnno != null && !StringUtils.isBlank(fieldAnno.name())) {
                        columnNameList.add(fieldAnno.name());
                    } else {
                        columnNameList.add(field.getName());//default
                    }
                }
            }
            rowList.add(columnNameList);//title row
        }
        if (beanList != null && !beanList.isEmpty()) {
            for (Object bean : beanList) {
                rowList.add(convetBeanToListByFields(bean, targetFields));
            }
        }
        return rowList;
    }

    /**
     * bean封装成field值的列表
     * ExportExcludingFiled注解的field不包含
     *
     * @param bean
     * @return
     */
    public static List<Object> convetBeanToList(Object bean) throws Exception {
        if (bean == null) {
            return new ArrayList<>(0);
        }
        List<Object> beanList = new ArrayList<>(1);
        beanList.add(bean);
        List<Object> fieldValueList = null;
        List<List<Object>> rowList = convetBeansToList(beanList, false);
        if (rowList.size() > 0) {
            fieldValueList = rowList.get(0);
        } else {
            fieldValueList = new ArrayList<>(1);
        }
        return fieldValueList;
    }

    private static List<Object> convetBeanToListByFields(Object bean, List<ExportColumnField> fields) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        if (bean == null || fields == null) {
            return new ArrayList<>(0);
        }
        List<Object> fieldValueList = new ArrayList<>(fields.size());
        for (ExportColumnField ecf : fields) {
            Field f = ecf.getField();
            PropertyDescriptor pd = new PropertyDescriptor(f.getName(), bean.getClass());
            Method rM = pd.getReadMethod();
            Object value = null;
            if (rM != null) {
                value = rM.invoke(bean);
            } else {
                value = f.get(bean);
            }
            if (value == null) {
                value = "";
            } else {
                //必要时进行类型转换
                value = convertData(value, ecf.getExportFieldAnno());
            }
            fieldValueList.add(value);
        }
        return fieldValueList;
    }

    private static class ExportColumnField {
        private Field field;
        private JcExportField exportFieldAnno;

        public ExportColumnField() {

        }

        public ExportColumnField(Field field, JcExportField exportFieldAnno) {
            this.field = field;
            this.exportFieldAnno = exportFieldAnno;
        }

        public Field getField() {
            return field;
        }

        public void setField(Field field) {
            this.field = field;
        }

        public JcExportField getExportFieldAnno() {
            return exportFieldAnno;
        }

        public void setExportFieldAnno(JcExportField exportFieldAnno) {
            this.exportFieldAnno = exportFieldAnno;
        }
    }

    /**
     * 转换特殊类型
     *
     * @param value
     * @return
     */
    private static Object convertData(Object value, JcExportField exportFieldAnno) {
        if (value == null) {
            return "";
        }
        Object valueConverted = value;
        String dateFormat = exportFieldAnno.dateFormat();
        if (exportFieldAnno != null) {
            //进行类型转换
            //目前只支持对int型的日期进行转换,后期可以进行扩展成TypeConverter的形式
            if (StringUtils.isNotBlank(dateFormat)) {
                if (Integer.class.equals(value.getClass())) {
                    //由于日期为空的默认为0,显示为空
                    if ((Integer) value <= 0) {
                        return "";
                    }
                    //默认为秒,转成毫秒
                    try {
                        valueConverted = DateFormatUtils.format(new Date(((Integer) value) * 1000L), dateFormat, Locale.SIMPLIFIED_CHINESE);
                    } catch (Exception ex) {
                        logger.error("", ex);
                    }
                }
            }
        }
        return valueConverted;
    }

}
