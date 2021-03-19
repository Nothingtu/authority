package com.action;


import com.domain.User;
import com.service.impl.UserService;
import com.util.MySpring;
import com.util.PageInfo;
import mvc.Annotation.ParameterAnnotation;
import mvc.Annotation.RequestMapping;

import mvc.Annotation.ResponseBody;
import mvc.ModelAndView;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.EvaluationWorkbook;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

public class UserAction {

    private UserService userService = MySpring.getBean("com.service.impl.UserService");

    @RequestMapping("login")
    public ModelAndView login(User user){
        User resultUser = userService.checkLogin(user);
        ModelAndView modelAndView = new ModelAndView();
        if(resultUser!=null && user.getUpassword().equals(resultUser.getUpassword())){
            modelAndView.setViewResourceName("main.jsp");
            modelAndView.setAttributeMap("uname",resultUser.getUname());
            modelAndView.setAttributeMap("uuno",resultUser.getUno());
            modelAndView.setAttributeMap("upassword",resultUser.getUpassword());
        }else{
            modelAndView.setViewResourceName("index.jsp");
            modelAndView.setAttributeMap("flag",false);
        }
        return modelAndView;

    }

    //用户的注销
    @RequestMapping("urgt")
    public String urgt(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/";//此处的“/”等价于“index.jsp”
    }

    @RequestMapping("userList")
    public ModelAndView userList(@ParameterAnnotation("uno") Integer uno, @ParameterAnnotation("uname")String uname, @ParameterAnnotation("usex")String usex
                                ,@ParameterAnnotation("page")Integer page,@ParameterAnnotation("row")Integer row){
        if(page == null){
            page = 1;
            row =5;
        }
        Map<String,Object> map = new HashMap<>();
        map.put("uno",uno);
        map.put("page",page);
        map.put("row",row);
        map.put("usex",usex);
        map.put("uname",uname);
        PageInfo pageInfo = userService.findAll(map);
        ModelAndView mv = new ModelAndView();
        mv.setAttributeMap("pageInfo",pageInfo);
        mv.setAttributeMap("uname",uname);
        mv.setAttributeMap("usex",usex);
        mv.setAttributeMap("uno",uno);
        mv.setViewResourceName("redirect:userList.jsp");
        return mv;
    }

    @ResponseBody
    @RequestMapping("createUser")
    public String createUser(User user){
        System.out.println(user);
        userService.addUser(user);
        return "保存成功";
    }

    @RequestMapping("deleteUser")
    public ModelAndView deleteUser(@ParameterAnnotation("uno")String uno){
        userService.deleteUser(uno);
        ModelAndView mv = new ModelAndView();
        mv.setViewResourceName("redirect:userList.do");
        return mv;
    }

    //用户的修改
    @RequestMapping("updateUser")
    public ModelAndView updateUser(User user){
        userService.updateUser(user);
        ModelAndView mv = new ModelAndView();
        mv.setViewResourceName("redirect:userList.do");
        return mv;
    }

    //先查询一个用户
    @RequestMapping("selectOneUser")
    public ModelAndView selectOneUser(@ParameterAnnotation("uno")String uno){
        User user = userService.selectOneUser(uno);
        ModelAndView mv = new ModelAndView();
        mv.setAttributeMap("user",user);
        mv.setViewResourceName("updateUser.jsp");
        return mv;
    }


    //批量删除
    @RequestMapping("deletesUser")
    public ModelAndView deletesUser(@ParameterAnnotation("unos") String unos){
        userService.deletesUser(unos);
        ModelAndView mv = new ModelAndView();
        mv.setViewResourceName("redirect:userList.do");
        return mv;
    }

    //批量导入 表头的顺序必须为   uname  upassword  urealname    uage   usex
    @RequestMapping("addsUser")
    public ModelAndView addsUser(HttpServletRequest request){
        try {
            List<User> userList = new ArrayList<>();

            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload fileUpload = new ServletFileUpload(factory);
            List<FileItem> items = fileUpload.parseRequest(request);

            FileItem item = items.get(0);
            InputStream inputStream = item.getInputStream();

            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                Cell c1 = row.getCell(0);
                Cell c2 = row.getCell(1);
                Cell c3 = row.getCell(2);
                Cell c4 = row.getCell(3);
                Cell c5 = row.getCell(4);

                String uname = c1.getStringCellValue();
                String upassword = (int) c2.getNumericCellValue() + "";
                String urealname = c3.getStringCellValue();
                String uage = (int) c4.getNumericCellValue() + "";
                String usex = c5.getStringCellValue();

                //uno, uname, upassword, createtime,  urealname, usex, uage, del, yl1, yl2
                userList.add(new User(null, uname, upassword, null, urealname, usex, Integer.parseInt(uage), 1, null, null));
            }

            for (User user : userList) {
                userService.addUser(user);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            ModelAndView mv = new ModelAndView();
            mv.setViewResourceName("redirect:userList.do");
            return mv;
        }

    }

    //模板下载userTemplateDownload
    @RequestMapping("userTemplateDownload")
    public void userTemplateDownload(HttpServletRequest request,HttpServletResponse response){
        try {
            String fileName = request.getParameter("fileName");
            String path = Thread.currentThread().getContextClassLoader().getResource("files").getPath();

            FileInputStream fileInputStream = new FileInputStream(path+fileName);


//            response.setContentType("application/x-msdownload");

            OutputStream writer = response.getOutputStream();
            response.setHeader("content-disposition","attachment;filename=" + fileName);

            int b = fileInputStream.read();

            while(b != -1){
                writer.write(b);
                b = fileInputStream.read();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //批量导出
    @RequestMapping("exportsUser")
    public void exportsUser(HttpServletResponse response){

        try {
            Map<String,Object> map = new HashMap<>();
            //#{start},#{row}
            map.put("start",0);
            map.put("row",Integer.MAX_VALUE);
            List<User> list = userService.findAllUser(map);


            //将list集合中的user信息写入虚拟的表格中
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet();

            //设置样式
            CellStyle style = workbook.createCellStyle();
            style.setAlignment(HorizontalAlignment.CENTER);
            Font font = workbook.createFont();
            font.setBold(true);
            style.setFont(font);

            {
                Row row = sheet.createRow(0);
                Cell c1 = row.createCell(0);
                Cell c2 = row.createCell(1);
                Cell c3 = row.createCell(2);
                Cell c4 = row.createCell(3);
                Cell c5 = row.createCell(4);

                c1.setCellValue("用户编号");
                c2.setCellValue("昵称");
                c3.setCellValue("真实姓名");
                c4.setCellValue("年龄");
                c5.setCellValue("性别");

                c1.setCellStyle(style);
                c2.setCellStyle(style);
                c3.setCellStyle(style);
                c4.setCellStyle(style);
                c5.setCellStyle(style);

            }
            for (int i = 1; i <= list.size(); i++) {
                User user = list.get(i-1);
                Row row = sheet.createRow(i);
                Cell c1 = row.createCell(0);
                Cell c2 = row.createCell(1);
                Cell c3 = row.createCell(2);
                Cell c4 = row.createCell(3);
                Cell c5 = row.createCell(4);

                c1.setCellValue(user.getUno());
                c2.setCellValue(user.getUname());
                c3.setCellValue(user.getUrealname());
                c4.setCellValue(user.getUage());
                c5.setCellValue(user.getUsex());
            }
            //将虚拟表格中的数据写入files文件中
            //先获取files文件夹所在的目录
            String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
            File file = new File(path,"users.xlsx");
            OutputStream os = new FileOutputStream(file);
            workbook.write(os);

            //将files中的users.xlsx写入到客户端
            OutputStream writer = response.getOutputStream();
            response.setHeader("content-disposition","attachment;filename=users.xlsx");

            FileInputStream fileInputStream = new FileInputStream(file);
            int b = fileInputStream.read();
            while(b != -1){
                writer.write(b);
                b = fileInputStream.read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    //修改密码的表单请求
    @RequestMapping("updatePswd")
    public String updatePswd(){
        return "redirect:upassform.jsp";
    }

    //确认修改密码
    @ResponseBody
    @RequestMapping("confirmPswd")
    public String confirmPswd(HttpServletRequest request){
        String npass = request.getParameter("npass");
        String opass = request.getParameter("opass");
        if(npass.equals(opass)) return "新密码和旧密码一致";
        Integer uno = (Integer)request.getSession().getAttribute("uuno");
        Map<String,Object> map = new HashMap<>();
        map.put("upassword",npass);
        map.put("uno",uno);
        userService.upPswd(map);
        return "修改成功";
    }



}
