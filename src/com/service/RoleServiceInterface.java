package com.service;

import com.domain.Role;
import com.util.OthPageInfo;

import java.util.List;
import java.util.Map;

public interface RoleServiceInterface {
    OthPageInfo selectRoles(Map<String,Object> map);

    List<Role> findRoles(String uno);

    List<Role> unFindRoles(String uno);

    void updUserRole(String uno,String rnos);

    void upRole(Map<String,Object> map);

    void deleteRole(String rno);

    void setFnForRole(String rno ,String fnos);


    List<Integer> findFnosByRno(Integer rno);

}
