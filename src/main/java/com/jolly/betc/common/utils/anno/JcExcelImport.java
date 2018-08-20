package com.jolly.betc.common.utils.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 在bean中标注字段信息,用于导入
 * @author zuoqiuming
 * @since 2018/07/25 13:49
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
public @interface JcExcelImport {
    /**
     * 指定field和excel列名的关系,不指定时采用fieldName作为列名
     * 一般建议配置
     *
     * @return
     */
    String name() default "";

}