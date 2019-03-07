package com.cookgod.chef;

import java.sql.Timestamp;

public class ChefOrderVO implements java.io.Serializable {
	public ChefOrderVO(String chef_or_status, Timestamp chef_or_start, Timestamp chef_or_send, Timestamp chef_or_rcv,
			Timestamp chef_or_end, String chef_or_name, String chef_or_addr, String chef_or_tel, String chef_ID) {
		super();
		this.chef_or_status = chef_or_status;
		this.chef_or_start = chef_or_start;
		this.chef_or_send = chef_or_send;
		this.chef_or_rcv = chef_or_rcv;
		this.chef_or_end = chef_or_end;
		this.chef_or_name = chef_or_name;
		this.chef_or_addr = chef_or_addr;
		this.chef_or_tel = chef_or_tel;
		this.chef_ID = chef_ID;
	}





	public ChefOrderVO() {
		super();
		// TODO Auto-generated constructor stub
	}





	private String chef_or_ID;
	private String chef_or_status;
	private Timestamp chef_or_start;
	private Timestamp chef_or_send;
	private Timestamp chef_or_rcv;
	private Timestamp chef_or_end;
	private String chef_or_name;
	private String chef_or_addr;
	private String chef_or_tel;
	private String chef_ID;


	public String getChef_or_ID() {
		return chef_or_ID;
	}

	public void setChef_or_ID(String chef_or_ID) {
		this.chef_or_ID = chef_or_ID;
	}

	public String getChef_or_status() {
		return chef_or_status;
	}

	public void setChef_or_status(String chef_or_status) {
		this.chef_or_status = chef_or_status;
	}

	public Timestamp getChef_or_start() {
		return chef_or_start;
	}

	public void setChef_or_start(Timestamp chef_or_start) {
		this.chef_or_start = chef_or_start;
	}

	public Timestamp getChef_or_send() {
		return chef_or_send;
	}

	public void setChef_or_send(Timestamp chef_or_send) {
		this.chef_or_send = chef_or_send;
	}

	public Timestamp getChef_or_rcv() {
		return chef_or_rcv;
	}

	public void setChef_or_rcv(Timestamp chef_or_rcv) {
		this.chef_or_rcv = chef_or_rcv;
	}

	public Timestamp getChef_or_end() {
		return chef_or_end;
	}

	public void setChef_or_end(Timestamp chef_or_end) {
		this.chef_or_end = chef_or_end;
	}

	public String getChef_or_name() {
		return chef_or_name;
	}

	public void setChef_or_name(String chef_or_name) {
		this.chef_or_name = chef_or_name;
	}

	public String getChef_or_addr() {
		return chef_or_addr;
	}

	public void setChef_or_addr(String chef_or_addr) {
		this.chef_or_addr = chef_or_addr;
	}

	public String getChef_or_tel() {
		return chef_or_tel;
	}

	public void setChef_or_tel(String chef_or_tel) {
		this.chef_or_tel = chef_or_tel;
	}

	public String getChef_ID() {
		return chef_ID;
	}

	public void setChef_ID(String chef_ID) {
		this.chef_ID = chef_ID;
	}

}
