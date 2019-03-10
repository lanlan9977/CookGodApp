package com.cookgod.foodsup;

import java.io.Serializable;

public class FoodVO implements  java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private String food_ID;
	private String food_name;
	private String food_type_ID;
	public String getFood_ID() {
		return food_ID;
	}
	public void setFood_ID(String food_ID) {
		this.food_ID = food_ID;
	}
	public String getFood_name() {
		return food_name;
	}
	public void setFood_name(String food_name) {
		this.food_name = food_name;
	}
	public String getFood_type_ID() {
		return food_type_ID;
	}
	public void setFood_type_ID(String food_type_ID) {
		this.food_type_ID = food_type_ID;
	}
	
	
	
}
