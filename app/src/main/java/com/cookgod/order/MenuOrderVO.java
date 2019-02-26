package com.cookgod.order;

import java.sql.Timestamp;
import java.util.Date;

public class MenuOrderVO implements java.io.Serializable {
    private String menu_od_ID;
    private String menu_od_status;
    private Timestamp menu_od_start;
    private Timestamp menu_od_book;

    public MenuOrderVO() {
    }

    public MenuOrderVO(String menu_od_ID, String menu_od_status, Timestamp menu_od_start, Timestamp menu_od_book, Date menu_od_end, Integer menu_od_rate, String menu_od_msg, String cust_ID, String chef_ID, String menu_ID) {
        this.menu_od_ID = menu_od_ID;
        this.menu_od_status = menu_od_status;
        this.menu_od_start = menu_od_start;
        this.menu_od_book = menu_od_book;
        this.menu_od_end = menu_od_end;
        this.menu_od_rate = menu_od_rate;

        this.menu_od_msg = menu_od_msg;
        this.cust_ID = cust_ID;
        this.chef_ID = chef_ID;
        this.menu_ID = menu_ID;
    }

    private Date menu_od_end;
    private Integer menu_od_rate;
    private String menu_od_msg;
    private String cust_ID;
    private String chef_ID;
    private String menu_ID;

    public String getMenu_od_ID() {
        return menu_od_ID;
    }
    public void setMenu_od_ID(String menu_od_ID) {
        this.menu_od_ID = menu_od_ID;
    }
    public String getMenu_od_status() {
        return menu_od_status;
    }
    public void setMenu_od_status(String menu_od_status) {
        this.menu_od_status = menu_od_status;
    }
    public Timestamp getMenu_od_start() {
        return menu_od_start;
    }
    public void setMenu_od_start(Timestamp menu_od_start) {
        this.menu_od_start = menu_od_start;
    }
    public Timestamp getMenu_od_book() {
        return menu_od_book;
    }
    public void setMenu_od_book(Timestamp menu_od_book) {
        this.menu_od_book = menu_od_book;
    }
    public Date getMenu_od_end() {
        return menu_od_end;
    }
    public void setMenu_od_end(Date menu_od_end) {
        this.menu_od_end = menu_od_end;
    }
    public Integer getMenu_od_rate() {
        return menu_od_rate;
    }
    public void setMenu_od_rate(Integer menu_od_rate) {
        this.menu_od_rate = menu_od_rate;
    }
    public String getMenu_od_msg() {
        return menu_od_msg;
    }
    public void setMenu_od_msg(String menu_od_msg) {
        this.menu_od_msg = menu_od_msg;
    }
    public String getCust_ID() {
        return cust_ID;
    }
    public void setCust_ID(String cust_ID) {
        this.cust_ID = cust_ID;
    }
    public String getChef_ID() {
        return chef_ID;
    }
    public void setChef_ID(String chef_ID) {
        this.chef_ID = chef_ID;
    }
    public String getMenu_ID() {
        return menu_ID;
    }
    public void setMenu_ID(String menu_ID) {
        this.menu_ID = menu_ID;
    }
}
