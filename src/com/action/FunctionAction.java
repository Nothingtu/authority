package com.action;

import com.domain.Function;
import com.service.impl.FunctionService;
import com.util.MySpring;
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
 * @create: 2021-03-16 15:23
 */
public class FunctionAction {

    private FunctionService functionService = MySpring.getBean("com.service.impl.FunctionService");

    @RequestMapping("funList")
    public String funList(){
        return "fnList.jsp";
    }

    //查询所有的功能，并进行规整子父关系
    @ResponseBody
    @RequestMapping("funs")
    public List<Function> funs(){
        return  functionService.findAll();
    }


    @ResponseBody
    @RequestMapping("addFunction")
    public void addFunction(@ParameterAnnotation("fname")String fname,@ParameterAnnotation("fhref")String fhref,
                            @ParameterAnnotation("flag")Integer flag,@ParameterAnnotation("pname")String pname){
        Map<String,Object> map = new HashMap<>();
        map.put("fname",fname);
        map.put("fhref",fhref);
        map.put("flag",flag);
        map.put("pname",pname);

        functionService.addFunction(map);
    }


    @ResponseBody
    @RequestMapping("updateFunction")
    public void updateFunction(@ParameterAnnotation("fname")String fname,@ParameterAnnotation("fhref")String fhref,
                            @ParameterAnnotation("flag")Integer flag,@ParameterAnnotation("oname")String oname){
        Map<String,Object> map = new HashMap<>();
        map.put("fname",fname);
        map.put("fhref",fhref);
        map.put("flag",flag);
        map.put("oname",oname);
        functionService.updateFunction(map);
    }

    @ResponseBody
    @RequestMapping("deleteFunction")
    public void deleteFunction(@ParameterAnnotation("fname")String fname){
        System.out.println("fname1 =" + fname);
        functionService.deleteFunction(fname);
    }
}
