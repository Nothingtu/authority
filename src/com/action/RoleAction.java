package com.action;

import com.service.impl.RoleService;
import com.util.MySpring;
import com.util.OthPageInfo;
import mvc.Annotation.ParameterAnnotation;
import mvc.Annotation.RequestMapping;
import mvc.Annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: authority_management
 * @description:
 * @author: zhang jie
 * @create: 2021-03-15 18:21
 */
public class RoleAction {

    private RoleService roleService = MySpring.getBean("com.service.impl.RoleService");

    @ResponseBody
    @RequestMapping("selectRoles")
    public OthPageInfo selectRoles(@ParameterAnnotation("rno")Integer rno, @ParameterAnnotation("rname")String rname,
                                   @ParameterAnnotation("description")String description , @ParameterAnnotation("page")Integer page , @ParameterAnnotation("row")Integer row){
        Map<String,Object> map = new HashMap<>();
        map.put("rno",rno);
        map.put("rname",rname);
        map.put("description",description);
        map.put("page",page);
        map.put("row",row);
        OthPageInfo othPageInfo = roleService.selectRoles(map);
        return othPageInfo;
    }


    @RequestMapping("findLinkedRoles")
    @ResponseBody
    public List findLinkedRoles(@ParameterAnnotation("uno")String uno){
        return roleService.findRoles(uno);
    }


    @RequestMapping("findUnlinkedRoles")
    @ResponseBody
    public List findUnlinkedRoles(@ParameterAnnotation("uno")String uno){
        return roleService.unFindRoles(uno);
    }

    @RequestMapping("saveSetRoles")
    @ResponseBody
    public String saveSetRoles(@ParameterAnnotation("uno")String uno,@ParameterAnnotation("rnos")String rnos){
        roleService.updUserRole(uno,rnos);
        return "保存成功";
    }

    //修改角色
    @RequestMapping("updRole")
    @ResponseBody
    public void updRole(@ParameterAnnotation("rname")String rname,
                          @ParameterAnnotation("rno")String rno,@ParameterAnnotation("description")String description){
        Map<String,Object> map = new HashMap<>();
        map.put("rname",rname);
        map.put("rno",Integer.parseInt(rno));
        map.put("description",description);
        roleService.upRole(map);
    }

    //删除角色
    @RequestMapping("deleteRole")
    @ResponseBody
    public void deleteRole(@ParameterAnnotation("rno")String rno){
        roleService.deleteRole(rno);
    }

    //给角色设置功能
    @RequestMapping("saveFnForRole")
    @ResponseBody
    public String saveFnForRole(@ParameterAnnotation("rno")String rno,@ParameterAnnotation("fnos")String fnos){
        roleService.setFnForRole(rno,fnos);
        return "保存成功";
    }



    //给角色设置功能
    @RequestMapping("findLinkedFunction")
    @ResponseBody
    public List<Integer> findLinkedFunction(@ParameterAnnotation("rno")String rno){
        return roleService.findFnosByRno(Integer.parseInt(rno));
    }

}
