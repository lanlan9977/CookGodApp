package com.cookgod.foodsup;

import java.util.Objects;

public class FoodMallVO implements java.io.Serializable {
	
	private static final long serialVersionUID = 2L;
	private String food_sup_ID;


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		FoodMallVO that = (FoodMallVO) o;
		return Objects.equals(food_sup_ID, that.food_sup_ID) &&
				Objects.equals(food_ID, that.food_ID);
	}

	@Override
	public int hashCode() {

		return Objects.hash(food_sup_ID, food_ID);
	}

	private String food_ID;
	private String food_m_name;
	private String food_m_status;
	private Integer food_m_price;
	private String food_m_unit;
	private String food_m_place;
	private byte[] food_m_pic;
	private String food_m_resume;
	private Integer food_m_rate;
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
	public String getFood_m_name() {
		return food_m_name;
	}
	public void setFood_m_name(String food_m_name) {
		this.food_m_name = food_m_name;
	}
	public String getFood_m_status() {
		return food_m_status;
	}
	public void setFood_m_status(String food_m_status) {
		this.food_m_status = food_m_status;
	}
	public Integer getFood_m_price() {
		return food_m_price;
	}
	public void setFood_m_price(Integer food_m_price) {
		this.food_m_price = food_m_price;
	}
	public String getFood_m_unit() {
		return food_m_unit;
	}
	public void setFood_m_unit(String food_m_unit) {
		this.food_m_unit = food_m_unit;
	}
	public String getFood_m_place() {
		return food_m_place;
	}
	public void setFood_m_place(String food_m_place) {
		this.food_m_place = food_m_place;
	}
	public byte[] getFood_m_pic() {
		return food_m_pic;
	}
	public void setFood_m_pic(byte[] food_m_pic) {
		this.food_m_pic = food_m_pic;
	}
	public String getFood_m_resume() {
		return food_m_resume;
	}
	public void setFood_m_resume(String food_m_resume) {
		this.food_m_resume = food_m_resume;
	}
	public Integer getFood_m_rate() {
		return food_m_rate;
	}
	public void setFood_m_rate(Integer food_m_rate) {
		this.food_m_rate = food_m_rate;
	}
	
	
}
