package com.cookgod.foodsup;


public class DishFoodVO implements java.io.Serializable{

	private String dish_ID;
	private String food_ID;
	private Integer dish_f_qty;
	private String dish_f_unit;
	
	
	public String getDish_ID() {
		return dish_ID;
	}
	public void setDish_ID(String dish_ID) {
		this.dish_ID = dish_ID;
	}
	public String getFood_ID() {
		return food_ID;
	}
	public void setFood_ID(String food_ID) {
		this.food_ID = food_ID;
	}
	public Integer getDish_f_qty() {
		return dish_f_qty;
	}
	public void setDish_f_qty(Integer dish_f_qty) {
		this.dish_f_qty = dish_f_qty;
	}
	public String getDish_f_unit() {
		return dish_f_unit;
	}
	public void setDish_f_unit(String dish_f_unit) {
		this.dish_f_unit = dish_f_unit;
	}
	
	
	
	
}
