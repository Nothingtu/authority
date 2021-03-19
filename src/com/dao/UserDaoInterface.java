package com.dao;

import com.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDaoInterface {

    User selectOne(String uname);

    Long findAllUserTotal(Map<String,Object> map);

    List<User> findAllByCond(Map<String,Object> map);

   void createUser(User user);

    void updateUser(User user);

    void deleteUser(String uno);

    User selectOneUser(String uno);

    void updPswd(Map<String,Object> map);
}



