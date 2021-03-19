package com.util;

import com.dao.impl.UserDao;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: authority_management
 * @description: 用来控制对象的单例
 * @author: zhang jie
 * @create: 2021-03-11 23:22
 */
public class MySpring {

    private static Map<String,Object>  objectMap = new HashMap<>();

    public static <T>T getBean(String className) {
        Object obj = objectMap.get(className);
        if(obj == null){
            synchronized (MySpring.class) {
                if(obj == null){
                    Class clazz = null;
                    try {
                        clazz = Class.forName(className);
                        obj = clazz.newInstance();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                    objectMap.put(className,obj);
                }
            }
        }
        return (T)obj;
    }

}
