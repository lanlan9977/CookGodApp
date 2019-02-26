package com.cookgod.order;

import java.sql.Date;

public class FoodOrderVO {

    private static final long serialVersionUID = 3L;
    private String food_or_ID;
    private String food_or_status;
    private Date food_or_start;
    private Date food_or_send;
    private Date food_or_rcv;
    private Date food_or_end;
    private String food_or_name;
    private String food_or_addr;
    private String food_or_tel;
    private String cust_ID;
    public String getFood_or_ID() {
        return food_or_ID;
    }
    public void setFood_or_ID(String food_or_ID) {
        this.food_or_ID = food_or_ID;
    }
    public String getFood_or_status() {
        return food_or_status;
    }
    public void setFood_or_status(String food_or_status) {
        this.food_or_status = food_or_status;
    }
    public Date getFood_or_start() {
        return food_or_start;
    }
    public void setFood_or_start(Date food_or_start) {
        this.food_or_start = food_or_start;
    }
    public Date getFood_or_send() {
        return food_or_send;
    }
    public void setFood_or_send(Date food_or_send) {
        this.food_or_send = food_or_send;
    }
    public Date getFood_or_rcv() {
        return food_or_rcv;
    }
    public void setFood_or_rcv(Date food_or_rcv) {
        this.food_or_rcv = food_or_rcv;
    }
    public Date getFood_or_end() {
        return food_or_end;
    }
    public void setFood_or_end(Date food_or_end) {
        this.food_or_end = food_or_end;
    }
    public String getFood_or_name() {
        return food_or_name;
    }
    public void setFood_or_name(String food_or_name) {
        this.food_or_name = food_or_name;
    }
    public String getFood_or_addr() {
        return food_or_addr;
    }
    public void setFood_or_addr(String food_or_addr) {
        this.food_or_addr = food_or_addr;
    }
    public String getFood_or_tel() {
        return food_or_tel;
    }
    public void setFood_or_tel(String food_or_tel) {
        this.food_or_tel = food_or_tel;
    }
    public String getCust_ID() {
        return cust_ID;
    }
    public void setCust_ID(String cust_ID) {
        this.cust_ID = cust_ID;
    }
}
