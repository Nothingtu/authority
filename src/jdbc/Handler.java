package jdbc;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 这个类的产生是为了让sqlSession看起来更简洁
 * 增强封装
 */
public class Handler {

    //设计一个方法 关闭所有的流
    public void closeAllStream(ResultSet rs, PreparedStatement pstat, Connection conn){
        try {
            if(rs != null){rs.close();}
            if(pstat != null){pstat.close();}
            if(conn != null){conn.close();}
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    //String sql ="insert into user values(#{uid},#{uname},#{upassword})"; 格式
    // 设计一个方法解析sql 将上面的sql解析成SqlAndKey
    SqlAndKey parseSql(String sql){
        StringBuilder builder = new StringBuilder();
        List<String> keyList = new ArrayList<String>();
        while(true){
            int left = sql.indexOf("#{");
            int right = sql.indexOf("}");
            if(left<(right-2)){
                String key = sql.substring(left+2,right);
                keyList.add(key);
                builder.append(sql.substring(0,left));
                builder.append("?");
            }else{
                builder.append(sql);
                break;
            }
            sql = sql.substring(right+1);
        }
        return new SqlAndKey(builder.toString(),keyList);
    }

    //A 设计一个方法 给sql语句中的问号赋值
    void setSql(PreparedStatement pstat,List<String> keyList,Object obj) throws SQLException, IllegalAccessException, NoSuchFieldException {
        //根据mvc的分层架构思想 可以得知controller得到的信息可能是以以下的几种方式(obj)传递过来的
        // domain类型    Map集合    常规类型（基本类型和String）
        //或者其他的数据类型 主要用来存储信息用的 例如数组 List 集合 因为数组和list集合不合适用在此处 故没有做具体的逻辑判断
        if(obj == null) return;
        Class clazz = obj.getClass();
        if(clazz == int.class || clazz == Integer.class){
            pstat.setInt(1, (Integer) obj);
        }else if(clazz == float.class || clazz == Float.class){
            pstat.setFloat(1, (Float) obj);
        }else if(clazz == Double.class || clazz == double.class){
            pstat.setDouble(1, (Double) obj);
        }else if(clazz == String.class ){
            pstat.setString(1, (String) obj);
        }else{
            if(clazz == Map.class || clazz == HashMap.class){
                this.setMap(pstat,keyList,obj);
            }else{//domain对象
                this.setDomain(pstat,keyList,obj);
            }
        }

    }

    //A小弟方法 参数是Map类型的pstat拼接
    private void setMap(PreparedStatement pstat,List<String> keyList,Object obj){
        try {
            for(int i = 0;i<keyList.size();i++) {
                String key = keyList.get(i);
                Object value = ((Map)obj).get(key);
                pstat.setObject(i+1,value);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //A小弟方法 domain类型的pstat拼接
    private void setDomain(PreparedStatement pstat,List<String> keyList,Object obj) throws NoSuchFieldException, IllegalAccessException, SQLException {
        for(int i = 1;i<=keyList.size();i++){
            Field field = obj.getClass().getDeclaredField(keyList.get(i-1));
            field.setAccessible(true);
            Object value = field.get(obj);
//            if(value!=null){
                pstat.setObject(i, value);
//            }else{//允许赋空值
//                throw new SqlQuestionMarkInitException("#{}赋值不合理  请检查参数是否正确");
//            }
        }
    }






    //--------------------------------------------------------------------------------------

    //组装单条的的查询信息
     <T>T assembleOne(ResultSet rs,Class resultType){
        //此处的resultType可能为Map.class  domain.class 或者常用的类型（八个基本类型和String）
        Object obj = null;
        if(rs!=null && resultType != null){
            try {
                if(resultType == int.class||resultType == Integer.class){
                    obj = rs.getInt(1);
                }else if(resultType == double.class||resultType == Double.class){
                    obj = rs.getDouble(1);
                }else if(resultType == byte.class||resultType == Byte.class){
                    obj = rs.getByte(1);
                }else if(resultType == long.class||resultType == Long.class){
                    obj = rs.getLong(1);
                }else if(resultType == float.class||resultType == Float.class){
                    obj = rs.getFloat(1);
                }else if(resultType == short.class||resultType == Short.class){
                    obj = rs.getShort(1);
                }else if(resultType == String.class){
                    obj = rs.getString(1);
                }else{
                    if(resultType == Map.class){
                        obj = this.getMap(rs);
                    }else{//若为domain对象
                        obj= this.getDomain(rs,resultType);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{//rs为空 没有查到信息
            throw new SqlQuestionMarkInitException("没有查询到结果 请检查dao查询单条方法");
        }
        return (T)obj;
    }

    //组装成Map集合
    private Map<String,Object> getMap(ResultSet rs){
        Map<String,Object> resultMap = new HashMap<String,Object>();
        try{
            ResultSetMetaData rsmd = rs.getMetaData();//获取结果集rs中的全部key
            for(int i=1;i<=rsmd.getColumnCount();i++){
                String key = rsmd.getColumnName(i);
                Object ob = rs.getObject(key);
                resultMap.put(key,ob);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return  resultMap;
    }

    //组装成domain对象
    private <T>T getDomain(ResultSet rs,Class resultType) throws Exception {
        Object obj = resultType.getConstructor().newInstance();
        try{
            ResultSetMetaData rsmd = rs.getMetaData();//获取结果集rs中的全部key
            for(int i=1;i<=rsmd.getColumnCount();i++){
                String key = rsmd.getColumnName(i);
                //通过列名获取对应的值value
                Field field = resultType.getDeclaredField(key);
                field.setAccessible(true);
                Object value = rs.getObject(key);
                if(value!=null){
                    field.set(obj,value);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return (T)obj;
    }


}
