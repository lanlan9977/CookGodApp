package idv.david.foodgodapp;

import java.io.Serializable;
import java.util.Date;

public class MenuOrder implements Serializable {

    private String menu_or_id;
    private Integer menu_or_status;
    private Date menu_or_start;
    private Date menu_or_appt;
    private Date menu_or_end;
    private Integer menu_or_rate;
    private String menu_or_msg;
    private String cust_id;
    private String chef_id;
    private String menu_id;

    public MenuOrder() {
    }

    public MenuOrder(String menu_or_id, Integer menu_or_status, Date menu_or_start, Date menu_or_appt, Date menu_or_end, Integer menu_or_rate, String menu_or_msg, String cust_id, String chef_id, String menu_id) {
        this.menu_or_id = menu_or_id;
        this.menu_or_status = menu_or_status;
        this.menu_or_start = menu_or_start;
        this.menu_or_appt = menu_or_appt;
        this.menu_or_end = menu_or_end;
        this.menu_or_rate = menu_or_rate;
        this.menu_or_msg = menu_or_msg;
        this.cust_id = cust_id;
        this.chef_id = chef_id;
        this.menu_id = menu_id;
    }

    public String getMenu_or_id() {
        return menu_or_id;
    }

    public void setMenu_or_id(String menu_or_id) {
        this.menu_or_id = menu_or_id;
    }

    public Integer getMenu_or_status() {
        return menu_or_status;
    }

    public void setMenu_or_status(Integer menu_or_status) {
        this.menu_or_status = menu_or_status;
    }

    public Date getMenu_or_start() {
        return menu_or_start;
    }

    public void setMenu_or_start(Date menu_or_start) {
        this.menu_or_start = menu_or_start;
    }

    public Date getMenu_or_appt() {
        return menu_or_appt;
    }

    public void setMenu_or_appt(Date menu_or_appt) {
        this.menu_or_appt = menu_or_appt;
    }

    public Date getMenu_or_end() {
        return menu_or_end;
    }

    public void setMenu_or_end(Date menu_or_end) {
        this.menu_or_end = menu_or_end;
    }

    public Integer getMenu_or_rate() {
        return menu_or_rate;
    }

    public void setMenu_or_rate(Integer menu_or_rate) {
        this.menu_or_rate = menu_or_rate;
    }

    public String getMenu_or_msg() {
        return menu_or_msg;
    }

    public void setMenu_or_msg(String menu_or_msg) {
        this.menu_or_msg = menu_or_msg;
    }

    public String getCust_id() {
        return cust_id;
    }

    public void setCust_id(String cust_id) {
        this.cust_id = cust_id;
    }

    public String getChef_id() {
        return chef_id;
    }

    public void setChef_id(String chef_id) {
        this.chef_id = chef_id;
    }

    public String getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(String menu_id) {
        this.menu_id = menu_id;
    }

}
