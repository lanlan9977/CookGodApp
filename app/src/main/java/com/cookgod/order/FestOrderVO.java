package com.cookgod.order;

import java.sql.Date;

public class FestOrderVO  implements java.io.Serializable {
	private String fest_or_ID;
	private String fest_or_status;
	private Integer fest_or_price;
	private Date fest_or_start;
	private Date fest_or_send;
	private Date fest_or_end;
	private String fest_or_disc;
	private String cust_ID;
	
	public String getFest_or_ID() {
		return fest_or_ID;
	}
	public void setFest_or_ID(String fest_or_ID) {
		this.fest_or_ID = fest_or_ID;
	}
	public String getFest_or_status() {
		return fest_or_status;
	}
	public void setFest_or_status(String fest_or_status) {
		this.fest_or_status = fest_or_status;
	}
	public Integer getFest_or_price() {
		return fest_or_price;
	}
	public void setFest_or_price(Integer fest_or_price) {
		this.fest_or_price = fest_or_price;
	}
	public Date getFest_or_start() {
		return fest_or_start;
	}
	public void setFest_or_start(Date fest_or_start) {
		this.fest_or_start = fest_or_start;
	}
	public Date getFest_or_send() {
		return fest_or_send;
	}
	public void setFest_or_send(Date fest_or_send) {
		this.fest_or_send = fest_or_send;
	}
	public Date getFest_or_end() {
		return fest_or_end;
	}
	public void setFest_or_end(Date fest_or_end) {
		this.fest_or_end = fest_or_end;
	}
	public String getFest_or_disc() {
		return fest_or_disc;
	}
	public void setFest_or_disc(String fest_or_disc) {
		this.fest_or_disc = fest_or_disc;
	}
	public String getCust_ID() {
		return cust_ID;
	}
	public void setCust_ID(String cust_ID) {
		this.cust_ID = cust_ID;
	}
	
	

}
