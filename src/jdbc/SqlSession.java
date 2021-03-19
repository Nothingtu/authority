package jdbc;

import connectionPool.ConnectionPool;
import jdbc.annotation.Delete;
import jdbc.annotation.Insert;
import jdbc.annotation.Select;
import jdbc.annotation.Update;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 这个类是dao的直接小弟
 */

public class SqlSession {

    private Handler handler = new Handler();

    //================================方案一 ======================================
    //此方法负责增删改
    private void uid(String sql, Object obj) throws SQLException, NoSuchFieldException, IllegalAccessException {
        SqlAndKey sqlAndKey = handler.parseSql(sql);
        ConnectionPool pool = ConnectionPool.newInstance();
        Connection conn = pool.getConnection();
        PreparedStatement pstat = conn.prepareStatement(sqlAndKey.getSql());
        handler.setSql(pstat,sqlAndKey.getKeyList(),obj);
        pstat.executeUpdate();
        handler.closeAllStream(null,pstat,conn);
    }

    //增
    public void insert(String sql, Object obj){
       try{
           this.uid(sql,obj);
       }catch(Exception e){
           e.printStackTrace();
       }
    }
    // 删
    public void delete(String sql, Object obj){
        try{
            this.uid(sql,obj);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    // 改
    public void update(String sql, Object obj){
        try{
            this.uid(sql,obj);
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    //---------------------------------------------------------------------------------------------------------
    //以下负责查询
    //多条查询
    public <T> List<T> selectList(String sql, Object oj, Class resultType){
        List<T> list = new ArrayList<T>();
        try {
            SqlAndKey sqlAndKey = handler.parseSql(sql);
            ConnectionPool pool = ConnectionPool.newInstance();
            Connection conn = pool.getConnection();
            PreparedStatement pstat = conn.prepareStatement(sqlAndKey.getSql());
            handler.setSql(pstat,sqlAndKey.getKeyList(),oj);
            ResultSet rs = pstat.executeQuery();
            //根据给定的resultType来组装查询的信息
            while(rs.next()){
                list.add((T)handler.assembleOne(rs,resultType));
            }
            handler.closeAllStream(rs,pstat,conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    //单条查询
    public <T>T selectOne(String sql,Object oj,Class resultType){
        //resultType为用户指定的返回值类型
        T obj = null;
        try {
            SqlAndKey sqlAndKey = handler.parseSql(sql);//解析sql语句
            ConnectionPool pool = ConnectionPool.newInstance();
            Connection conn = pool.getConnection();
            PreparedStatement pstat = conn.prepareStatement(sqlAndKey.getSql());
            //给拼接好的sql中的问号赋值
            handler.setSql(pstat,sqlAndKey.getKeyList(),oj);
            ResultSet rs = pstat.executeQuery();
            //根据给定的resultType来组装查询的信息
            if(rs.next()){
                obj = handler.assembleOne(rs,resultType);
            }
            handler.closeAllStream(rs,pstat,conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }



    /*
    //======================================方案二 ====================================================
    //动态数组obj里的元素必须和sql中的问号书序一致
    private void uid(String sql,Object... obj){
        try {
            ConnectionPool pool = ConnectionPool.newInstance();
            Connection connection =pool.getConnection();
            PreparedStatement pstat = connection.prepareStatement(sql);
            for(int i = 0;i<obj.length;i++){
                pstat.setObject(i+1,obj[i]);
            }
            pstat.executeUpdate();
            handler.closeAllStream(null,pstat,connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void insert(String sql,Object... obj){
        this.uid(sql,obj);
    }

    void delete(String sql,Object... obj){
        this.uid(sql,obj);
    }

    void update(String sql,Object... obj){
        this.uid(sql,obj);
    }


    //查询单条
    //domain 类必须实现RowMapping抽象类
    <T> T selectOne(String sql,RowMapping rm,Object... obj){
        T ob = null;
        try {
            ConnectionPool pool = ConnectionPool.newInstance();
            Connection connection =pool.getConnection();
            PreparedStatement pstat = connection.prepareStatement(sql);
            for(int i = 0;i<obj.length;i++){
                pstat.setObject(i+1,obj[i]);
            }
            ResultSet rs = pstat.executeQuery();
            if(rs!=null){
                ob = (T)rm.setMappingRow(rs);
            }
            handler.closeAllStream(null,pstat,connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ob;
    }

    //查询多条记录
    <T> List<T> selectList(String sql,RowMapping rm,Object... obj){
        List<T> list  = new ArrayList<T>();
        try {
            ConnectionPool pool = ConnectionPool.newInstance();
            Connection connection =pool.getConnection();
            PreparedStatement pstat = connection.prepareStatement(sql);
            for(int i = 0;i<obj.length;i++){
                pstat.setObject(i+1,obj[i]);
            }
            ResultSet rs = pstat.executeQuery();
            while(rs.next()){
                if(rs!=null){
                    list.add((T)rm.setMappingRow(rs));
                }
            }
            handler.closeAllStream(null,pstat,connection);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;

    }


     */



    //***************************************************
    //增加一个静态代理
    public  <T>T getProxy(Class clazz){
        Object obj = Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //先获取方法上的注解
                Annotation an = method.getAnnotations()[0];
                //获取注解的类型
                Class type = an.annotationType();
                //获取注解上的sql
                Method valueMethod = type.getDeclaredMethod("value");
                String sql = (String)valueMethod.invoke(an);
                Object parameters = (args==null?null:args[0]);
                if(type == Insert.class){
                    SqlSession.this.insert(sql,parameters);
                }else if(type == Delete.class){
                    SqlSession.this.delete(sql,parameters);
                }else if(type == Update.class){
                    SqlSession.this.update(sql,parameters);
                }else if(type == Select.class){
                    Class resultType = method.getReturnType();
                    if(resultType == List.class){//查询的是多条
                        Type type1 = method.getGenericReturnType();
                        ParameterizedType parameterizedType = (ParameterizedType)type1;
                        Type[] types = parameterizedType.getActualTypeArguments();//获取泛型里面的全部类型
                        return SqlSession.this.selectList(sql,parameters,(Class)types[0]);//Type是Class的顶级接口 需要造型会Class
                    }else {
                        return SqlSession.this.selectOne(sql, parameters, resultType);
                    }
                }else{
                    System.out.println("不好好玩");
                }
                return null;
            }
        });
        return (T)obj;
    }
}
