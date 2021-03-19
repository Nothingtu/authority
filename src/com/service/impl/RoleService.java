package com.service.impl;

import com.dao.impl.RoleDao;
import com.domain.Role;
import com.service.RoleServiceInterface;
import com.util.MySpring;
import com.util.OthPageInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: authority_management
 * @description:
 * @author: zhang jie
 * @create: 2021-03-15 20:20
 */
public class RoleService implements RoleServiceInterface {

    private RoleDao roleDao = MySpring.getBean("com.dao.impl.RoleDao");

    //按照条件分页查询
    public OthPageInfo selectRoles(Map<String, Object> map) {
        //按照当前条件查询的最大行记录数
        int total = (int) roleDao.total(map);

        int row = (int) map.get("row");
        int maxPage = (total % row == 0 ? total / row : total / row + 1);
        int page = (int) map.get("page");
        page = Math.min(page, maxPage);
        int start = (page - 1) * row;
        int length = row;

        map.put("page",page);
        map.put("start",start);
        map.put("length",length);

        List<Role> roleList = roleDao.findByFilterAndPage(map);
        return new OthPageInfo(page,maxPage,roleList);
    }

    public List<Role> findRoles(String uno){
        return roleDao.findRoles(uno);
    }

    public List<Role> unFindRoles(String uno){
        return roleDao.unfindRoles(uno);
    }

    //修改用户的角色
    public void updUserRole(String uno,String rnos){
        roleDao.deleteSettedRoles(Integer.parseInt(uno));
        String[] rrnos = rnos.split(",");
        Map<String,Object> map = new HashMap<>();
        map.put("uno",uno);
        for (String rno : rrnos) {
            map.put("rno",Integer.parseInt(rno));
            roleDao.addRoleToUser(map);
        }
    }


    //修改角色
    public void upRole(Map<String,Object> map){
        roleDao.upRole(map);
    }

    //删除角色
    public void deleteRole(String rno){
        roleDao.deleteRole(Integer.parseInt(rno));
    }

    //设定指定角色的功能
    public void setFnForRole(String rno ,String fnos){

        roleDao.deleteFn(Integer.parseInt(rno));

        String[] nos = fnos.split(",");
        Map<String,Object> map = new HashMap<>();
        map.put("rno",Integer.parseInt(rno));
        for(int i = 0 ;i < nos.length;i++){
            map.put("fno",Integer.parseInt(nos[i]));
            roleDao.setFnForRole(map);
        }
    }

    //查询该角色已经拥有的功能
    public List<Integer> findFnosByRno(Integer rno){
        System.out.println("查询到的功能编号："+roleDao.findFnosByRno(rno));
        return roleDao.findFnosByRno(rno);
    }
}
