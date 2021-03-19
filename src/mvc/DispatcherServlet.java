package mvc;

import mvc.Exception.NoProperties;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


/**
 * 这个类相当于一个入口 负责请求的分发 到相应的controller类下
 */
public class DispatcherServlet extends HttpServlet {

    private Handler handler = new Handler();

    //重写HttpServlet类里的service方法
    public void service(HttpServletRequest request,HttpServletResponse response)  {
        try {
        //0.先处理字符集
        request.setCharacterEncoding("UTF-8");
        //获取请求的uri
        String uri = request.getRequestURI();
        //1.接受请求传过来的参数（请求的方法名）
        String requestContent = request.getParameter("method");
        String fullClassName = null;
        if(requestContent!=null){
            String simpleClassName = uri.substring(uri.lastIndexOf("/")+1,uri.indexOf("."));
            //2.获取类全名
            fullClassName = handler.getFullClassName(simpleClassName);
        }else{
            requestContent = uri.substring(uri.lastIndexOf("/")+1,uri.indexOf("."));
            fullClassName = handler.getRequestFullClassName().get(requestContent);
        }
        //3.通过类全名获取类对象
        Object obj=handler.getBean(fullClassName);
        //通过对象和方法名找到相应的方法
        Method method = handler.getMethod(obj,requestContent);
        //获取方法执行所需要的参数
        Object[] objects = handler.getFinalParameters(method,request,response);
        //4.执行方法 并处理最终的结果
        Object methodResult = method.invoke(obj,objects);
        handler.parseFinalResult(method,methodResult,request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //程序启动时就会自动执行读取配置文件Configuration.properties
    //通过init方法调用readNameProperties()
    public void init(ServletConfig config){
        boolean result = handler.readNameProperties();
        String packNames= null;
        if(result==false) {//false表示有配置文件Configuration.properties：类名+类全名
            //此处的packNames有可能为空 说明Configuration.properties配置的是请求名和对应的类名
            packNames = handler.getScanPackageName();
        }else {
            packNames =config.getInitParameter("scanPackage");
        }
        if(packNames == null){
            throw new NoProperties("请检查配置文件中是否有scanPackage 或者web.xml中是否有配置init-param");
        }else{
            try {
                handler.scanAnnotation(packNames);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }


}
