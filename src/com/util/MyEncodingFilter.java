package com.util;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: authority_management
 * @description: 用来处理中文乱码
 * @author: zhang jie
 * @create: 2021-03-14 01:27
 */
@WebFilter("/")
public class MyEncodingFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        System.out.println(request.getParameter("uname"));
        chain.doFilter(request, response);
    }
}
