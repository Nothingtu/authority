package connectionPool;

import java.sql.Connection;
import java.util.ArrayList;

/**
 * MyConnection的连接池 专门用来管理MyConnection
 */
public class ConnectionPool {
    //单利设计模式
    private ConnectionPool(){}
    private volatile static ConnectionPool connectionPool =null;
    public static ConnectionPool newInstance(){
        if(connectionPool == null){
            synchronized (ConnectionPool.class){
                if(connectionPool == null){
                    connectionPool = new ConnectionPool();
                }
            }
        }
        return connectionPool;
    }

    //专门用来管理MyConnection 的集合
    private  ArrayList<MyConnection> connectionList = new ArrayList<>();

    //connectionList给定的初始化长度
    private static int initNumber =Integer.parseInt(ConfigurationReader.get("initNumber"));

//    {
//        String value = ConfigurationReader.get("initNumber");
//        if(value != null){
//            initNumber = Integer.parseInt(value);
//        }
//    }
    //初始化connectionList
    {
        for(int i= 0;i<initNumber;i++){
            connectionList.add(new MyConnection());
        }
    }





    //设计一个方法 从connectionList中获取MyConnection
    private MyConnection findMyConnection(){
        MyConnection myConnection = null;
        for(int i = 0 ;i<initNumber;i++){
            myConnection = connectionList.get(i);
            if(myConnection.isUsed() == true){
                myConnection.setUsed(false);
                break;
            }
        }
        return myConnection;

    }

    //添加一个获取MyConnection时的等待的机制
    public Connection getConnection(){
        MyConnection myConnection = null;
        int count = 0;//记录循环的次数(次数刚好能计算出时间)
        while(myConnection==null && count < 1000){//太快啦
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myConnection = this.findMyConnection();
            count++;
        }
        if(myConnection==null){
            //超过5秒钟 但是还没有找到
            //自定义异常
            throw new ConnectionNotFoundException("系统繁忙 请稍后重试");
        }
//        try {
//            while(true){
//                Thread.sleep(100);
//                myConnection = this.findMyConnection();
//                count++;
//                if(myConnection != null){
//                    break;
//                }else if(count == 15 && myConnection == null){
//                        throw new ConnectionNotFoundException("等待超时 请刷新再试");
//                }
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return myConnection;
    }

}
