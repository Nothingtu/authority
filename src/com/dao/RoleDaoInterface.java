package com.dao;

import com.domain.Role;

import java.util.List;
import java.util.Map;

/**
 * @program: authority_management
 * @description:
 * @author: zhang jie
 * @create: 2021-03-15 20:17
 */
public interface RoleDaoInterface {

    List<Role> findByFilterAndPage(Map<String,Object> map);

    long total(Map<String,Object> map);

    List<Role> findRoles(String uno);

    List<Role> unfindRoles(String uno);

    void deleteSettedRoles(Integer uno);

    void addRoleToUser(Map<String,Object> map);

    void upRole(Map<String,Object> map);

    void deleteRole(Integer rno);

    void deleteFn(Integer rno);

    void setFnForRole(Map<String,Object> map);

    List<Integer> findFnosByRno(Integer rno);
}
