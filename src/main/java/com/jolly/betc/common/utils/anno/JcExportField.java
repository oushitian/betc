package com.jolly.betc.common.utils.anno;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注导出file时的字段信息
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface JcExportField {
    //字段在file中的title,如果为空,则以字段名作为title
    String name() default "";

    //数据精度,暂不支持
//    int precision() default 2;

    String dateFormat() default "";
}
