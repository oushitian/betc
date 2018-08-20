package com.jolly.betc.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 反射工具类
 *
 * @author zuoqiuming
 * @since 2018/04/17 上午 10:40
 */
public class ReflectionUtils {
    /**
     * 获取target的第一个泛型参数Class
     * 递归从父级进行尝试获取,直到获取第一个值为止
     *
     * @param target
     * @return 第一个泛型
     */
    public static Class getFirstGenericType(Class target) {
        return getGenericType(target, 1);
    }

    /**
     * 获取target的第number个泛型参数Class
     * 递归从父级进行尝试获取,直到获取第一个值为止
     *
     * @param target
     * @param number 代表取的第几个参数,从1开始
     * @return 第number个泛型
     */
    public static Class getGenericType(Class target, Integer number) {
        if (target == null || number == null || number < 1) {
            return null;
        }
        Type genType = target.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return getGenericType(target.getSuperclass(), number);
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (params.length >= number) {
            return (Class) params[number - 1];
        }
        return null;
    }

    public static Field[] getClassFieldsAndSuperClassFields(Class clazz) {
        return getAllFields(clazz).toArray(new Field[0]);
    }

    public static List<Field> getAllFields(Class clazz) {
        List<Field> fields = new ArrayList<>();
        if (clazz == null) {
            return fields;
        }
        fields.addAll(getAllFields(clazz.getSuperclass()));
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return fields;
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {

    }

}
