package jdbc;

import java.util.ArrayList;
import java.util.List;

/**
 *这个类的产生是为了在解析sql时返回值的方便包装新的sql和key
 */
public class SqlAndKey {
    private String sql ;

    //因为sql中的？的个数不确定 ，也为了后续方便遍历 用ArrayList
    private List<String> keyList = new ArrayList();


    public SqlAndKey(String sql,List<String> keyList){
        this.keyList = keyList;
        this.sql = sql;
    }

    @Override
    public String toString() {
        return "SqlAndKey{" +
                "sql='" + sql + '\'' +
                ", keyList=" + keyList +
                '}';
    }

    public String getSql(){
        return this.sql;
    }
    public List<String> getKeyList(){
        return this.keyList;
    }
}
