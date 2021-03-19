package com.service;

import com.domain.User;
import com.util.PageInfo;

import java.util.List;
import java.util.Map;

public interface UserServiceInterface {
    User checkLogin(User user);

    PageInfo findAll(Map<String,Object> map);

    Long total(Map<String,Object> mapx);

    void addUser(User user);

    void deleteUser(String uno);

    void updateUser(User user);

    User selectOneUser(String uno);

    void deletesUser(String unos);

    List<User> findAllUser(Map<String,Object> map);

    void upPswd(Map<String,Object> map);
}
