package com.domain;

import java.io.Serializable;

public class Role implements Serializable {
    private Integer rno;
    private String rname;
    private String description;
    private Integer del;
    private String createtime;

    private String yl1;
    private String yl2;

    public Role() {
    }

    public Role(Integer rno, String rname, String description, Integer del, String createtime, String yl1, String yl2) {
        this.rno = rno;
        this.rname = rname;
        this.description = description;

        this.del = del;
        this.createtime = createtime;

        this.yl1 = yl1;
        this.yl2 = yl2;

    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getCreatetime() {
        return this.createtime;
    }

    public void setRno(Integer rno) {
        this.rno = rno;
    }

    public Integer getRno() {
        return this.rno;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getRname() {
        return this.rname;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDel(Integer del_) {
        this.del = del;
    }

    public Integer getDel() {
        return this.del;
    }

    public void setYl1(String yl1) {
        this.yl1 = yl1;
    }

    public String getYl1() {
        return this.yl1;
    }

    public void setYl2(String yl2) {
        this.yl2 = yl2;
    }

    public String getYl2() {
        return this.yl2;
    }


}

