package com.cookgod.order;

public class DishVO implements java.io.Serializable{

	private String dish_ID;
	private String dish_name;
	private String dish_status;
	private String dish_resume;
	private Integer dish_price;
	
	public String getDish_ID() {
		return dish_ID;
	}
	public void setDish_ID(String dish_ID) {
		this.dish_ID = dish_ID;
	}
	public String getDish_name() {
		return dish_name;
	}
	public void setDish_name(String dish_name) {
		this.dish_name = dish_name;
	}
	public String getDish_status() {
		return dish_status;
	}
	public void setDish_status(String dish_status) {
		this.dish_status = dish_status;
	}
	public String getDish_resume() {
		return dish_resume;
	}
	public void setDish_resume(String dish_resume) {
		this.dish_resume = dish_resume;
	}
	public Integer getDish_price() {
		return dish_price;
	}
	public void setDish_price(Integer dish_price) {
		this.dish_price = dish_price;
	}
	
	
	
}
