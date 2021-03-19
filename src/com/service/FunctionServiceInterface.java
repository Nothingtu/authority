package com.service;

import com.domain.Function;

import java.util.List;
import java.util.Map;

/**
 * @program: authority_management
 * @description:
 * @author: zhang jie
 * @create: 2021-03-16 15:25
 */
public interface FunctionServiceInterface {

    List<Function> findAll();

    void addFunction(Map<String,Object> map);

    void deleteFunction(String fname);
}
