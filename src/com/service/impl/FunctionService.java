package com.service.impl;

import com.dao.FunctionDaoInterface;
import com.domain.Function;
import com.service.FunctionServiceInterface;
import jdbc.SqlSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @program: authority_management
 * @description:
 * @author: zhang jie
 * @create: 2021-03-16 15:25
 */
public class FunctionService implements FunctionServiceInterface {

    private FunctionDaoInterface functionDao = new SqlSession().getProxy(com.dao.FunctionDaoInterface.class);

    public List<Function> findAll() {
        List<Function> functions = functionDao.findAll();
        return reload(functions, -1);
    }

    /**
     * @Description: 设计一个私有的方法，作为该类的私有成员。仅供自己用。将list集合中的Function对象按照父子关系进行规整
     * 指的注意的是：在定义Function类时，flag=1表示的是该功能是一个菜单，2表示的是一个按钮
     * @Param: resource 表示的是从集合中查到的全部function对象   ；pno表示的父级菜单的编号。
     * @return: 返回的集合是以有子父关系的list集合
     * @Author: zhang jie
     * @date:
     */
    private List reload(List<Function> resource, int pno) {
        //这个集合是用来装载规整好了的Function的
        List<Function> functionList = new ArrayList<>();
        for (Function function : resource) {
            if (function.getFhref() == null) function.setFhref("未定义");
            if (function.getPno() == pno) {
                if (function.getFlag() == 2) {
                    //表示该功能是一个按钮功能
                    functionList.add(function);
                } else {
                    //表示该功能是一个菜单功能

                    //先获取该菜单功能的编号
                    int no = function.getFno();
                    //再去resource中找寻父级菜单编号为no的所有子元素
                    function.setChildren(reload(resource, no));
                    //将装好了子元素的function装入functionList
                    functionList.add(function);
                }
            }
        }
        return functionList;
    }


    public void addFunction(Map<String, Object> map) {
        String pname = (String) map.get("pname");
        if(!"-1".equals(pname)){
            Integer pno = functionDao.findFnoByName(pname);
            map.put("pno", pno);
        }
        else map.put("pno", "-1");
        functionDao.addFunction(map);
    }

    public void updateFunction(Map<String, Object> map){
        String oname = (String) map.get("oname");
        Integer fno = functionDao.findFnoByName(oname);
        map.put("fno", fno);

        String fhref = (String) map.get("fhref");
        if("未定义".equals(fhref)){
            fhref = null;
            map.put("fhref", fhref);
        }
        functionDao.updateFunction(map);
    }


    public void deleteFunction(String fname){
        System.out.println("fname2 =" + fname);
        functionDao.deleteFunction(fname);
    }


}
