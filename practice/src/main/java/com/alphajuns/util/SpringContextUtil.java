package com.alphajuns.util;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

/**
 * @ClassName SpringContextUtil
 * @Description 获取Spring对象工具类
 * @Author AlphaJunS
 * @Date 2020/3/18 20:49
 * @Version 1.0
 */
@Repository("springContextUtil")
public class SpringContextUtil implements ApplicationContextAware {

    /**
     * Spring应用上下文
     */
    private static ApplicationContext applicationContext;

    /**
     * @Description 实现ApplicationContextAware接口的回调方法，设置上下文环境
     * @param applicationContext
     * @return void
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * @Description 获取spring上下文对象
     * @param
     * @return org.springframework.context.ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * @Description 获取bean对象
     * @param name
     * @return java.lang.Object
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * @Description
     * @param name
     * @param requiredType
     * @return java.lang.Object
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static Object getBean(String name, Class requiredType) throws BeansException {
        return applicationContext.getBean(name, requiredType);
    }

    /**
     * @Description 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     * @param name
     * @return boolean
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * @Description 判断以给定名字注册的bean定义是一个singleton还是一个prototype
     * @param name
     * @return boolean
     */
    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.isSingleton(name);
    }

    /**
     * @Description 获取注册对象的类型
     * @param name
     * @return java.lang.Class
     */
    @SuppressWarnings("rawtypes")
    public static Class getType(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getType(name);
    }

    /**
     * @Description 如果给定的bean名字在bean定义中有别名，则返回这些别名
     * @param name
     * @return java.lang.String[]
     */
    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getAliases(name);
    }
}
