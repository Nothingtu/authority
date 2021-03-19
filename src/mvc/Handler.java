package mvc;

import com.alibaba.fastjson.JSONObject;
import mvc.Exception.NotFoundReturnValueException;
import mvc.Annotation.ParameterAnnotation;
import mvc.Annotation.RequestMapping;
import mvc.Annotation.ResponseBody;
import mvc.Exception.ParameterException;
import mvc.Exception.RequestNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.*;


/**
 * 这个类的产生没有特殊的意义
 * 只是为了让DispatcherServlet看起来更整洁点
 * 但功能却是核心方法
 */
public class Handler {
    /*
    此注释是针对以下两个集合而言的
    这两个集合实际上是可以合并的  但合并不合理
    为了提高性性能 objectMap集合管理的对象必须是生命周期托管 且是懒加载的机制
    如果合并了 则在类加载是objectMap里就创建了全部的对象 会影响性能
     */

    //配置文件的缓存: 类名+类全名
    private HashMap<String, String> realClassNameMap = new HashMap<String, String>();

    String getScanPackageName() {
        return this.realClassNameMap.get("scanPackage");
    }

    //集合为：类全名+controller对象
    // DispatcherServlet是继承了HttpServlet 所以DispatcherServlet的对象是惟一的，
    // 所以这个类的这个集合也是惟一的  进而保证了controller类对象的唯一
    private HashMap<String, Object> objectMap = new HashMap<String, Object>();

    //集合为：最终执行类的（对象+方法名+方法）
    private HashMap<Object, HashMap<String, Method>> objectMethodMap = new HashMap<Object, HashMap<String, Method>>();

    //通过方法名和对象获取对应的方法
    Method getMethod(Object obj, String name) {
        return objectMethodMap.get(obj).get(name);
    }

    //设计一个方法 通过类名名字获取类全名
    String getFullClassName(String simpleClassName) {
        if (realClassNameMap.get(simpleClassName) == null) {
            throw new RequestNotFoundException("没有找到请求");
        }
        return realClassNameMap.get(simpleClassName);
    }


    //设计一个方法 对realClassNameMap进行初始化
    //读取的是配置文件simpleNameAndClass.properties或者xml中的scanPackage
    boolean readNameProperties() {
        boolean result = false;
        try {
            // 是为了给类名和类全名指定一个一一对应关系而已
            Properties properties = new Properties();
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("simpleNameAndClassName.properties"));
            Enumeration en = properties.propertyNames();
            if (en != null) {//如果en非等于空 则表示simpleNameAndClassName.properties里面有内容
                while (en.hasMoreElements()) {
                    String key = (String) en.nextElement();
                    String value = properties.getProperty(key);
                    realClassNameMap.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {//表示没有配置文件simpleNameAndClassName.properties  信息在xml配置中
            result = true;
        }
        return result;
    }

    //集合为：请求名+类全名
    private HashMap<String, String> requestFullClassName = new HashMap<>();

    HashMap<String, String> getRequestFullClassName() {
        return this.requestFullClassName;
    }

    //扫描包的所有类上的注解
    void scanAnnotation(String packageNames) throws ClassNotFoundException {
        //有可能一个scanPackage对应好0,1,多个包
        String[] packagesNames = packageNames.split(",");
        if (packagesNames == null) {
            return;
        }
        for (String packageName : packagesNames) {
            //通过类加载器 获取包名对应的路径名 获取文件所在的URL
            URL url = Thread.currentThread().getContextClassLoader().getResource(packageName.replace(".", "\\"));
            if (url == null) {
                continue;
            }
            String packagePath = url.getPath();
            //重申：File表示的是文件或文件夹
            File packageFile = new File(packagePath);
            //过滤器 FileFilter
            File[] files = packageFile.listFiles(new FileFilter() {
                public boolean accept(File file) {
                    if (file.isFile() && file.getName().endsWith("class")) {
                        return true;
                    }
                    return false;
                }
            });
            for (File file : files) {
                //通过file获取对应的文件名：xxx.class
                String fileName = file.getName();
                //通过文件名获取对应的类名
                String simpleClassName = fileName.replace("\\", ".");
                String fullClassName = packageName + "." + simpleClassName.substring(0, simpleClassName.indexOf("."));
                //通过类全名获取对应的Class
                Class clazz = Class.forName(fullClassName);
                //通过clazz获取类上的注解  注解必须和请求的内容完全一致
                RequestMapping requestClassMapping = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
                if (requestClassMapping != null) {//说明类或者方法上有注解 注解的内容：类名.do
                    requestFullClassName.put(requestClassMapping.value(), fullClassName);
                } else {
                    Method[] methods = clazz.getDeclaredMethods();
                    for (Method method : methods) {
                        RequestMapping requestMethodMapping = method.getAnnotation(RequestMapping.class);
                        if (requestMethodMapping != null) {//
                            requestFullClassName.put(requestMethodMapping.value(), fullClassName);
                        } else {//类上和方法上都没有注解
                            throw new RequestNotFoundException("没有找到方法  请检查注解");
                        }
                    }
                }
            }
        }
    }


    //通过类全名获取类对象  因为通过反射获取的方法在执行时需要类对象
    //并向objectMethodMap集合写入数据
    synchronized Object getBean(String fullClassName) {
        //上来就直接从集合里取
        Object obj = objectMap.get(fullClassName);
        if (obj == null) {
            try {
                Class clazz = Class.forName(fullClassName);
                obj = clazz.getConstructor().newInstance();
                objectMap.put(fullClassName, obj);
                //在找到obj的同时将obj对象类里的所有方法放入集合objectMethodMap
                this.findAllMethod(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return obj;
    }

    //通过对象找到方法名+方法 初始化objectMethodMap集合
    private void findAllMethod(Object obj) {
        Class clazz = obj.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        HashMap<String, Method> methodNameMethodMap = new HashMap<String, Method>();
        for (Method method : methods) {
            methodNameMethodMap.put(method.getName(), method);
        }
        objectMethodMap.put(obj, methodNameMethodMap);
    }


    /*参数的自动注入DI
    找寻方法执行所需要的参数 即通过request.getParameter()方法获取请求中的value
     但此处的key有几种情况: 1.直接在方法参数前添加注解
                         2.是一个domain实体对象
                         3.是一个集合
                         4.是原生的request和response
                         5.数组
                         6.无参数
    */
    Object[] getFinalParameters(Method method, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Parameter[] parameters = method.getParameters();
        if (parameters == null || parameters.length == 0) {//表示请求的方法没有参数
            return null;
        }
        Object[] objects = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Class parameterClass = parameter.getType();
            ParameterAnnotation parameterAnnotation = parameter.getAnnotation(ParameterAnnotation.class);
            if (parameterAnnotation != null) {//注解里的内容与请求里的key相同
                objects[i] = this.parseParameterAnnotation(parameterAnnotation, parameterClass, request);
            } else if (parameterClass.isArray()) {
                throw new ParameterException("处理不了数组参数");
            } else {
                if (parameterClass == Map.class || parameterClass == List.class || parameterClass == Set.class || parameterClass == Enum.class) {
                    throw new ParameterException("处理不了接口  请提供具体的集合类型");
                }
                if (parameterClass == HttpServletRequest.class) {
                    objects[i] = request;
                    continue;
                }
                if (parameterClass == HttpServletResponse.class) {
                    objects[i] = response;
                    continue;
                } else {
                    Object parameterObject = parameterClass.getConstructor().newInstance();
                    if (parameterObject instanceof Map) {
                        objects[i] = this.parseHashMap(request, parameterObject);
                    }
                    if (parameterObject instanceof Object) {//是domain是对象
                        objects[i] = this.parseDomain(parameterClass, parameterObject, request);
                    }
                }
            }
        }
        return objects;
    }

    //hashMap的解析和注入方法
    private Object parseHashMap(HttpServletRequest request, Object parameterObject) {
        Map<String, Object> paramMap = (Map<String, Object>) parameterObject;
        Enumeration en = request.getParameterNames();
        while (en.hasMoreElements()) {
            String key = (String) en.nextElement();
            String value = request.getParameter(key);
            paramMap.put(key, value);
        }
        return paramMap;

    }

    //参数前带注解的解析并注入方法
    private Object parseParameterAnnotation(ParameterAnnotation parameterAnnotation, Class parameterClass, HttpServletRequest request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Object obj = null;
        String key = parameterAnnotation.value();
        String value = request.getParameter(key);
        if (value != null && !"".equals(value)) {
            if (parameterClass == String.class) {
                obj = value;
            } else if (parameterClass == int.class || parameterClass == Integer.class) {
                obj = new Integer(value);
            } else if (parameterClass == byte.class || parameterClass == Byte.class) {
                obj = new Byte(value);
            } else if (parameterClass == float.class || parameterClass == Float.class) {
                obj = new Float(value);
            } else if (parameterClass == short.class || parameterClass == Short.class) {
                obj = new Short(value);
            }else if (parameterClass == double.class || parameterClass == Double.class) {
                obj =new Double(value);
            }else if (parameterClass == long.class || parameterClass == Long.class) {
                obj = new Long(value);
            }
        }
        return obj;
    }

    //实体对象的解析和注入
    private Object parseDomain(Class parameterClass, Object parameterObject, HttpServletRequest request) throws Exception {
        Field[] fields = parameterClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Class fieldType = field.getType();
            String firstLetter = fieldName.substring(0, 1);
            String otherLetters = fieldName.substring(1);
            StringBuilder setMethodName = new StringBuilder("set");
            setMethodName.append(firstLetter.toUpperCase());
            setMethodName.append(otherLetters);
            Method setMethod = parameterClass.getMethod(setMethodName.toString(), fieldType);
            if (request.getParameter(fieldName) != null) {
                setMethod.invoke(parameterObject, fieldType.getConstructor(String.class).newInstance(request.getParameter(fieldName)));
            }
        }
        return parameterObject;
    }


    //A 解析最终执行的方法的返回值
    void parseFinalResult(Method method, Object methodResult, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //先判断返回值的类型
        if (methodResult != null) {//判断返回值是否为空
            if (methodResult instanceof ModelAndView) {//返回值是ModelAndView 类型的
                //先将methodResult造型回真是的类型
                ModelAndView mav = (ModelAndView) methodResult;
                //获取mav中的Map集合 并将集合里的内容存到request里
                HashMap<String, Object> attributeMap = mav.getAttributeMaps();
                this.parseAttributeHashMap(attributeMap, request);
                //解析mav中的字符属性
                String viewResourceName = mav.getViewResourceName();
                this.parseViewResourceName(viewResourceName, request, response);
            } else if (methodResult instanceof String) {//返回值是String类型的  因为方法的参数有可能是response和request 客户可能用原生的writer()进行相应
                this.parseString(method, methodResult, request, response);
            } else {//domain实体之类的  集合
                ResponseBody responseBody = method.getAnnotation(ResponseBody.class);
                if (responseBody != null) {// 有注解 表示返回的是数据
                    response.setCharacterEncoding("UTF-8");
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("jsonObject", methodResult);
                    response.getWriter().write(jsonObject.toString());
                } else {
                    //抛出异常
                }
            }

        } else {
            //抛出一个异常
//            throw new NotFoundReturnValueException("action类里的方法没有返回值");
        }
        //先获取方法上是否有注解
        //没有注解表示客户用的是原生的方法 writer方法在做相应 所以不用做处理
    }

    //A 的小弟 获取mav中的Map集合 并将集合里的内容存到request里
    private void parseAttributeHashMap(HashMap<String, Object> attributeMap, HttpServletRequest request) {
        if (attributeMap != null) {
            HttpSession session = request.getSession();
            Set<String> keys = attributeMap.keySet();
            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                Object obj = attributeMap.get(key);
                session.setAttribute(key, obj);
            }
        }
    }

    //A的小弟  解析viewResourceName 并作出相应
    private void parseViewResourceName(String viewResourceName, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (viewResourceName != null) {
            String[] values = viewResourceName.split(":");
            //ViewResourceName 默认格式welcome.jsp 表示转发
            if (values.length == 1) {
                request.getRequestDispatcher(viewResourceName).forward(request, response);
            }
            //ViewResourceName 默认格式redirect:main.jsp 表示重定向
            if (values.length == 2) {
                if ("redirect".equalsIgnoreCase(values[0])) {
                    response.sendRedirect(values[1]);
                } else {
                    //抛出异常
                    //转发个是错误 应以redirect开头
                }
            }
        }
    }

    //A的小弟 当返回值是String时
    private void parseString(Method method, Object methodResult, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ResponseBody responseBody = method.getAnnotation(ResponseBody.class);
        if (responseBody != null) {//有注解 返回值是一个数据
            response.setContentType("text/html;UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write((String) methodResult);
            response.getWriter().flush();
        } else {//返回值是一个路径
            //调用解析字符串的方法
            this.parseViewResourceName((String) methodResult, request, response);
        }
    }


}





