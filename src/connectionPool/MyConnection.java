package connectionPool;

import java.sql.*;

/**
 * 这个类是为了给connection增加一个状态而产生的
 */
public class MyConnection extends  AbstractConnection{

    //true 表示这个connection处于空闲状态  false表示正在被使用
    private boolean used = true;

    //包装一个connection作为属性
    private Connection connection;

    private static String url = ConfigurationReader.get("url");
    private static String className =ConfigurationReader.get("className");
    private static String useName = ConfigurationReader.get("useName");
    private static String password = ConfigurationReader.get("password");

    //驱动类只需要加载一次  故用静态块
    static{
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    {
        try {
            connection  = DriverManager.getConnection(url,useName,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setUsed(boolean used ){
        this.used = used;
    }
    public boolean isUsed(){
        return this.used;
    }

    public Connection getConnection(){
        return this.connection;
    }

    //-----------------以下是重写AbstractConnection中的方法-------------------------

    public void close() throws SQLException {
        this.used = false;
    }

    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }
}
