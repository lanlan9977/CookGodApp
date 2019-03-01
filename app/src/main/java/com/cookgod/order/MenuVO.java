package com.cookgod.order;

public class MenuVO implements java.io.Serializable {
	
	private String menu_ID;
	private String menu_name;
	private String menu_resume;
	private String menu_status;
	private Integer menu_price;

	public MenuVO() {

	}
	
	public String getMenu_ID() {
		return menu_ID;
	}

	public void setMenu_ID(String menu_ID) {
		this.menu_ID = menu_ID;
	}

	public String getMenu_name() {
		return menu_name;
	}

	public void setMenu_name(String menu_name) {
		this.menu_name = menu_name;
	}

	public String getMenu_resume() {
		return menu_resume;
	}

	public void setMenu_resume(String menu_resume) {
		this.menu_resume = menu_resume;
	}

	public String getMenu_status() {
		return menu_status;
	}

	public void setMenu_status(String menu_status) {
		this.menu_status = menu_status;
	}

	public Integer getMenu_price() {
		return menu_price;
	}

	public void setMenu_price(Integer menu_price) {
		this.menu_price = menu_price;
	}

}
