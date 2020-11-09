package com.kjh.board.vo;

public class KjhMemberVO {
	private String m_num;
	private String m_id;
	private String m_pw;
	private String m_name;
	private String m_phone;
	private String m_email;
	private String m_addr;
	private String m_reg_date;
	private String m_update_date;
	private String salt;

	public KjhMemberVO() {
		
	}
	public KjhMemberVO(String m_num, String m_id, String m_pw, String m_name, String m_phone, String m_email,
			String m_addr, String m_reg_date, String m_update_date,String salt) {
		this.m_num = m_num;
		this.m_id = m_id;
		this.m_pw = m_pw;
		this.m_name = m_name;
		this.m_phone = m_phone;
		this.m_email = m_email;
		this.m_addr = m_addr;
		this.m_reg_date = m_reg_date;
		this.m_update_date = m_update_date;
	}
	public String getM_num() {
		return m_num;
	}
	public void setM_num(String m_num) {
		this.m_num = m_num;
	}
	public String getM_id() {
		return m_id;
	}
	public void setM_id(String m_id) {
		this.m_id = m_id;
	}
	public String getM_pw() {
		return m_pw;
	}
	public void setM_pw(String m_pw) {
		this.m_pw = m_pw;
	}
	public String getM_name() {
		return m_name;
	}
	public void setM_name(String m_name) {
		this.m_name = m_name;
	}
	public String getM_phone() {
		return m_phone;
	}
	public void setM_phone(String m_phone) {
		this.m_phone = m_phone;
	}
	public String getM_email() {
		return m_email;
	}
	public void setM_email(String m_email) {
		this.m_email = m_email;
	}
	public String getM_addr() {
		return m_addr;
	}
	public void setM_addr(String m_addr) {
		this.m_addr = m_addr;
	}
	public String getM_reg_date() {
		return m_reg_date;
	}
	public void setM_reg_date(String m_reg_date) {
		this.m_reg_date = m_reg_date;
	}
	public String getM_update_date() {
		return m_update_date;
	}
	public void setM_update_date(String m_update_date) {
		this.m_update_date = m_update_date;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
		
	}
}
