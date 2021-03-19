package com.dao.impl;

import com.dao.UserDaoInterface;
import com.domain.User;
import com.util.MySpring;
import jdbc.SqlSession;

import java.util.List;
import java.util.Map;

public class UserDao implements UserDaoInterface {

    SqlSession sqlSession = MySpring.getBean("jdbc.SqlSession");

    //仅仅只是查询一个用户，不带其他的条件。通过名字
    @Override
    public User selectOne(String uname) {
        String sql = "select * from am_user where uname =#{uname} and del=1";
        return sqlSession.selectOne(sql,uname,User.class);
    }

    //找寻符合当前查询条件的结果总数
    public Long findAllUserTotal(Map<String,Object> map){
        StringBuilder builder = new StringBuilder("select count(*) from am_user where del =1 ");
        if(map.get("uno") != null)  builder.append("and uno = #{uno} ");
        if(map.get("uname") != null && "" != map.get("uname"))  builder.append("and uname like concat( #{uname},'%') ");
        if(map.get("usex") != null && "" != map.get("usex"))  builder.append(" and usex = #{usex}");
        return  sqlSession.selectOne(builder.toString(),map,long.class);
    }

    //找寻符合当前查询条件的结果
    public List<User> findAllByCond(Map<String,Object> map){

        StringBuilder builder = new StringBuilder("select * from am_user where del =1 ");
        if(map.get("uno") != null)  builder.append("and uno = #{uno} ");
        if(map.get("uname") != null && "" != map.get("uname"))  builder.append("and uname like concat( #{uname},'%') ");
        if(map.get("usex") != null && "" != map.get("usex"))  builder.append("and usex = #{usex} ");
        builder.append("limit #{start},#{row} ") ;
        return sqlSession.selectList(builder.toString(),map,User.class);
    }

    public void createUser(User user){
        String sql = "insert into am_user values(null,#{uname},#{upassword},#{urealname},#{usex},#{uage},1,now(),#{yl1},#{yl2})";
        sqlSession.insert(sql,user);
    }

    //用户信息的"删除" 2表示“删除”
    public void deleteUser(String uno){
        String sql = "update am_user set del = 2 where uno = #{uno}";
        sqlSession.update(sql,uno);
    }

    //用户信息的"修改"
    public void updateUser(User user){
        String sql = "update am_user set uname = #{uname} ,createtime = now() ,urealname = #{urealname},uage = #{uage} ,usex = #{usex} where uno = #{uno}";
        sqlSession.update(sql,user);
    }

    //通过编号查询一个用户
    public User selectOneUser(String uno){
        String sql = "select * from am_user where uno = ${uno}";
        return (User)sqlSession.selectOne(sql,uno,User.class);
    }


    //修改密码
    public void updPswd(Map<String,Object> map){
        String sql = "update am_user set upassword = #{upassword} where uno = #{uno}";
        sqlSession.update(sql,map);
    }
}
