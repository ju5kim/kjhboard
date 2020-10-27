package com.kjh.board.vo;

public class ImageVO {
	private String image_num;
	private String image_file_name;
	private String reg_date;
	private String b_num;
	
	public ImageVO() {
	}
	public ImageVO(String image_num, String image_file_name, String reg_date, String b_num) {
		this.image_num = image_num;
		this.image_file_name = image_file_name;
		this.reg_date = reg_date;
		this.b_num = b_num;
	}
	public String getImage_num() {
		return image_num;
	}
	public void setImage_num(String image_num) {
		this.image_num = image_num;
	}
	public String getImage_file_name() {
		return image_file_name;
	}
	public void setImage_file_name(String image_file_name) {
		this.image_file_name = image_file_name;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getB_num() {
		return b_num;
	}
	public void setB_num(String b_num) {
		this.b_num = b_num;
	}
	
	
}
