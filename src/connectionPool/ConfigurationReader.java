package connectionPool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

/**
 * 该类里的所有方法均为静态
 * 而静态块的加载需要加载类
 * 什么时候加载类？--->如果类A引用到了类B的某个成员变量 则会自动加载类B
 */

public class ConfigurationReader {

    //配置文件缓存集合：key+value
    private static HashMap<String,String> proMap = new HashMap<String,String>();

    private static  Properties properties;
    //初始化缓存集合
    static{
        try {
            properties = new Properties();
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("Configuration.properties");
            properties.load((inputStream));
            Enumeration en  = properties.propertyNames();
            while(en.hasMoreElements()){
                String key = (String)en.nextElement();
                String value = (String)properties.get(key);
                proMap.put(key,value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String get(String key){
        return proMap.get(key);
    }
}

