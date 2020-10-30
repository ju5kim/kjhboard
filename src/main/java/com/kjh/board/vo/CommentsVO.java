package com.kjh.board.vo;

public class CommentsVO {

	private String c_num;
	private String c_c_num;
	private String m_num;
	private String c_content;
	private String c_reg_date;
	private String b_num;

	public CommentsVO() {
	}

	public CommentsVO(String c_num, String c_c_num, String m_num, String c_content, String c_reg_date, String b_num) {
		this.c_num = c_num;
		this.c_c_num = c_c_num;
		this.m_num = m_num;
		this.c_content = c_content;
		this.c_reg_date = c_reg_date;
		this.b_num = b_num;
	}

	public String getC_num() {
		return c_num;
	}

	public void setC_num(String c_num) {
		this.c_num = c_num;
	}

	public String getC_c_num() {
		return c_c_num;
	}

	public void setC_c_num(String c_c_num) {
		this.c_c_num = c_c_num;
	}

	public String getM_num() {
		return m_num;
	}

	public void setM_num(String m_num) {
		this.m_num = m_num;
	}

	public String getC_content() {
		return c_content;
	}

	public void setC_content(String c_content) {
		this.c_content = c_content;
	}

	public String getC_reg_date() {
		return c_reg_date;
	}

	public void setC_reg_date(String c_reg_date) {
		this.c_reg_date = c_reg_date;
	}

	public String getB_num() {
		return b_num;
	}

	public void setB_num(String b_num) {
		this.b_num = b_num;
	}

}
