package com.cookgod.chef;

import java.sql.*;

public class ChefSchVO implements java.io.Serializable{
	
	private String chef_ID;
	private String chef_name;
	private Date chef_sch_date;
	private String chef_sch_status;
	
	public String getChef_ID() {
		return chef_ID;
	}
	public void setChef_ID(String chef_ID) {
		this.chef_ID = chef_ID;
	}
	public String getChef_name() {
		return chef_name;
	}
	public void setChef_name(String chef_name) {
		this.chef_name = chef_name;
	}
	public Date getChef_sch_date() {
		return chef_sch_date;
	}
	public void setChef_sch_date(Date chef_sch_date) {
		this.chef_sch_date = chef_sch_date;
	}
	public String getChef_sch_status() {
		return chef_sch_status;
	}
	public void setChef_sch_status(String chef_sch_status) {
		this.chef_sch_status = chef_sch_status;
	}	
}
