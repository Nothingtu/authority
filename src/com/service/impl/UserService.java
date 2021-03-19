package com.service.impl;

import com.dao.UserDaoInterface;
import com.domain.User;
import com.service.UserServiceInterface;
import com.util.MySpring;
import com.util.PageInfo;

import java.util.List;
import java.util.Map;

public class UserService implements UserServiceInterface {

    private UserDaoInterface userDao = MySpring.getBean("com.dao.impl.UserDao");

    public User checkLogin(User user){
         return userDao.selectOne(user.getUname());
    }

    public Long total(Map<String,Object> map){
        return userDao.findAllUserTotal(map);
    }

    //过滤查询带分页
    public PageInfo findAll(Map<String,Object> map){
        Integer page = (Integer)map.get("page");
        if(page == null) page =1;
        Integer row = (Integer)map.get("row");
        Long total = total(map);
        if(total == 0) total = 1L;
        Integer maxPage = (int)Math.ceil(1.0*total/row);
        page = Math.min(page,maxPage);

        //分页的起始索引
        int start  = (page -1)*row;
        map.put("start",start);
        map.put("page",page);
        List<User> userList = userDao.findAllByCond(map);
        return  new PageInfo(row,maxPage,page,userList);
    }

    //查询所有的用户，不带条件
    public List<User> findAllUser(Map<String,Object> map){
        return userDao.findAllByCond(map);
    }
    //新增用户
    public void addUser(User user){
        userDao.createUser(user);
    }

    //用户的删除
    public void deleteUser(String uno){
        userDao.deleteUser(uno);
    }

    public void updateUser(User user){
        userDao.updateUser(user);
    }

    public User selectOneUser(String uno){
        return userDao.selectOneUser(uno);
    }

    //批量删除的方法
    public void deletesUser(String unos){
        String[] strs = unos.split(",");
        for(int i =0;i<strs.length;i++){
            userDao.deleteUser(strs[i]);
        }
    }


    //修改密码
    public void upPswd(Map<String,Object> map){
        userDao.updPswd(map);
    }
}
