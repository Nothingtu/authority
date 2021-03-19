package com.dao.impl;

import com.dao.RoleDaoInterface;
import com.domain.Role;
import com.util.MySpring;
import jdbc.SqlSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: authority_management
 * @description:
 * @author: zhang jie
 * @create: 2021-03-15 20:17
 */
public class RoleDao implements RoleDaoInterface {

    private SqlSession sqlSession = MySpring.getBean("jdbc.SqlSession");

    //按照过滤条件查询分页
    public List<Role> findByFilterAndPage(Map<String,Object> map){
        String sql = "select * from am_role where del =1 ";

        Integer rno = (Integer) map.get("rno");
        if(rno != null)  sql += " and rno = #{rno} ";
        String rname = (String)map.get("rname");
        if(rname != null && "" != rname)  sql += " and rname like concat(#{rname},'%')  ";
        String description = (String) map.get("description");
        if(description != null && ""!= description) sql+= " and description like concat(#{description},'%') ";

        sql += "order by createtime desc ";
        sql += " limit #{start},#{length} ";

        return sqlSession.selectList(sql,map,Role.class);
    }

    //查询总数
    public long total(Map<String,Object> map){
        String sql = "select count(*) from am_role where del =1 ";

        Integer rno = (Integer) map.get("rno");
        if(rno != null)  sql += " and rno = #{rno} ";
        String rname = (String)map.get("rname");
        if(rname != null && "" != rname)  sql += " and rname like concat(#{rname},'%')  ";
        String description = (String) map.get("description");
        if(description != null && ""!= description) sql+= " and description like concat(#{description},'%') ";
        return sqlSession.selectOne(sql,map,long.class);
    }

    public List<Role> findRoles(String uno){
        String sql = "select * from am_role where rno in (select rno from am_user_role where uno = #{uno})";
        return sqlSession.selectList(sql,uno,Role.class);
    }

    public List<Role> unfindRoles(String uno){
        String sql = "select * from am_role where rno not in (select rno from am_user_role where uno = #{uno})";
        return sqlSession.selectList(sql,uno,Role.class);
    }

    //根据指定的用户编号删除所有的角色
    public void deleteSettedRoles(Integer uno){
        String sql = "delete from am_user_role where uno =#{uno}";
        sqlSession.delete(sql,uno);
    }

    //给用户指定功能
    public void addRoleToUser(Map<String,Object> map){
        String sql = "insert into am_user_role values(#{uno},#{rno})";
        sqlSession.insert(sql,map);
    }

    //修改角色
    public void upRole(Map<String,Object> map){
        String sql = "update am_role set rname = #{rname},description = #{description} where rno = #{rno}";
        sqlSession.update(sql,map);
    }

    //删除角色
    public void deleteRole(Integer rno){
        String sql = "delete  am_role  where rno = #{rno}";
        sqlSession.update(sql,rno);
    }

    //删除所有的功能
    public void deleteFn(Integer rno){
        String sql = "delete from am_role_function where rno = #{rno}";
        sqlSession.delete(sql,rno);
    }


    //一次给一个rno赋值一个fn
    public void setFnForRole(Map<String,Object> map){
        String sql = "insert into am_role_function values(#{rno},#{fno})";
        sqlSession.insert(sql,map);
    }

    //查询该角色已经拥有的功能
    public List<Integer> findFnosByRno(Integer rno){
        String sql ="select fno from am_role_function where rno = #{rno}";
        return sqlSession.selectList(sql,rno, Integer.class);
    }
}
