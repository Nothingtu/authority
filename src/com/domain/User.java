package com.domain;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private Integer uno;
    private String uname;
    private String upassword;
    private String urealname;
    private String usex;
    private Integer uage;

    private String createtime;

    @Override
    public String toString() {
        return "User{" +
                "uno=" + uno +
                ", uname='" + uname + '\'' +
                ", upassword='" + upassword + '\'' +
                ", urealname='" + urealname + '\'' +
                ", usex='" + usex + '\'' +
                ", uage=" + uage +
                ", createtime='" + createtime + '\'' +
                ", del=" + del +
                ", yl1='" + yl1 + '\'' +
                ", yl2='" + yl2 + '\'' +
                ", roleList=" + roleList +
                '}';
    }

    //删除标识  1表示未删除
    private Integer del;

    private String yl1;
    private String yl2;

    private List<Role> roleList;

    public User() {
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public User(Integer uno, String uname, String upassword, String createtime, String urealname, String usex, Integer uage, Integer del, String yl1, String yl2) {
        this.createtime = createtime;
        this.uno = uno;
        this.uname = uname;
        this.upassword = upassword;
        this.urealname = urealname;
        this.usex = usex;
        this.del = del;
        this.uage = uage;
        this.yl1 = yl1;
        this.yl2 = yl2;
    }


    public void setUno(Integer uno) {
        this.uno = uno;
    }

    public Integer getUno() {
        return this.uno;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUname() {
        return this.uname;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }

    public String getUpassword() {
        return this.upassword;
    }

    public void setUrealname(String urealname) {
        this.urealname = urealname;
    }

    public String getUrealname() {
        return this.urealname;
    }

    public void setUsex(String usex) {
        this.usex = usex;
    }

    public String getUsex() {
        return this.usex;
    }

    public void setUage(Integer uage) {
        this.uage = uage;
    }

    public Integer getUage() {
        return this.uage;
    }

    public void setDel(Integer del) {
        this.del = del;
    }

    public Integer getDel() {
        return this.del;
    }

    public void setYl1(String yl1) {
        this.yl1 = yl1;
    }

    private String getYl1() {
        return this.yl1;
    }

    public void setYl2(String yl2) {
        this.yl2 = yl2;
    }

    private String getYl2() {
        return this.yl2;
    }


    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreatetime() {
        return this.createtime;
    }
}
