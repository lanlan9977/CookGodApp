package com.cookgod.broadcast;

import java.sql.Timestamp;

public class BroadcastVO implements java.io.Serializable ,Comparable<BroadcastVO> {



	private String broadcast_ID;
	private Timestamp broadcast_start;
	private String broadcast_con;
	private String broadcast_status;
	private String cust_ID;

	public BroadcastVO() {

	}

	public String getBroadcast_ID() {
		return broadcast_ID;
	}

	public void setBroadcast_ID(String broadcast_ID) {
		this.broadcast_ID = broadcast_ID;
	}

	public Timestamp getBroadcast_start() {
		return broadcast_start;
	}

	public void setBroadcast_start(Timestamp broadcast_start) {
		this.broadcast_start = broadcast_start;
	}

	public String getBroadcast_con() {
		return broadcast_con;
	}

	public void setBroadcast_con(String broadcast_con) {
		this.broadcast_con = broadcast_con;
	}

	public String getBroadcast_status() {
		return broadcast_status;
	}

	public void setBroadcast_status(String broadcast_status) {
		this.broadcast_status = broadcast_status;
	}

	public String getCust_ID() {
		return cust_ID;
	}

	public void setCust_ID(String cust_ID) {
		this.cust_ID = cust_ID;
	}

	@Override
	public int compareTo(BroadcastVO o) {
		return -getBroadcast_start().compareTo(o.getBroadcast_start());
	}




}
