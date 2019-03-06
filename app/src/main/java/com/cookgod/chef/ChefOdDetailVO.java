package com.cookgod.chef;

public class ChefOdDetailVO implements java.io.Serializable {

	private String chef_or_ID;
	private String food_sup_ID;
	private String food_ID;
	private Integer chef_od_qty;
	private Integer chef_od_stotal;

	public ChefOdDetailVO() {

	}
	
	public String getChef_or_ID() {
		return chef_or_ID;
	}

	public void setChef_or_ID(String chef_or_ID) {
		this.chef_or_ID = chef_or_ID;
	}

	public String getFood_sup_ID() {
		return food_sup_ID;
	}

	public void setFood_sup_ID(String food_sup_ID) {
		this.food_sup_ID = food_sup_ID;
	}

	public String getFood_ID() {
		return food_ID;
	}

	public void setFood_ID(String food_ID) {
		this.food_ID = food_ID;
	}

	public Integer getChef_od_qty() {
		return chef_od_qty;
	}

	public void setChef_od_qty(Integer chef_od_qty) {
		this.chef_od_qty = chef_od_qty;
	}

	public Integer getChef_od_stotal() {
		return chef_od_stotal;
	}

	public void setChef_od_stotal(Integer chef_od_stotal) {
		this.chef_od_stotal = chef_od_stotal;
	}



}
