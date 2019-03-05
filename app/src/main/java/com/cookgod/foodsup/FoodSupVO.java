package com.cookgod.foodsup;

import java.io.Serializable;

public class FoodSupVO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private String food_sup_ID;
	private String food_sup_name;
	private String food_sup_tel;
	private String food_sup_status;
	private String food_sup_resume;
	public String getFood_sup_ID() {
		return food_sup_ID;
	}
	public void setFood_sup_ID(String food_sup_ID) {
		this.food_sup_ID = food_sup_ID;
	}
	public String getFood_sup_name() {
		return food_sup_name;
	}
	public void setFood_sup_name(String food_sup_name) {
		this.food_sup_name = food_sup_name;
	}
	public String getFood_sup_tel() {
		return food_sup_tel;
	}
	public void setFood_sup_tel(String food_sup_tel) {
		this.food_sup_tel = food_sup_tel;
	}
	public String getFood_sup_status() {
		return food_sup_status;
	}
	public void setFood_sup_status(String food_sup_status) {
		this.food_sup_status = food_sup_status;
	}
	public String getFood_sup_resume() {
		return food_sup_resume;
	}
	public void setFood_sup_resume(String food_sup_resume) {
		this.food_sup_resume = food_sup_resume;
	}
	
	
}
