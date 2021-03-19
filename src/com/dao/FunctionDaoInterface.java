package com.dao;

import com.domain.Function;
import jdbc.annotation.Insert;
import jdbc.annotation.Select;
import jdbc.annotation.Update;

import java.util.List;
import java.util.Map;

/**
 * @program: authority_management
 * @description:
 * @author: zhang jie
 * @create: 2021-03-16 15:24
 */
public interface FunctionDaoInterface {

    @Select("select * from am_function where del = 1")
    List<Function> findAll();

    @Select("select fno from am_function where fname = #{fname}")
    Integer findFnoByName(String fname);

    @Insert("insert into am_function values(null,#{fname},#{fhref},#{flag},null,#{pno},1,null,null,now())")
    void addFunction(Map<String,Object> map);

    @Update("update am_function set fname = #{fname} ,fhref = #{fhref} ,flag = #{flag} where fno = #{fno} ")
    void updateFunction(Map<String,Object> map);

    @Update("update am_function set del = 2 where fname = #{fname}")
    void deleteFunction(String fname);
}
