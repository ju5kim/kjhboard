package com.kjh.board.vo;

public class AuthVO {
private String m_num;
private String auth;


public AuthVO() {
}


public AuthVO(String userid, String auth) {
	this.m_num = userid;
	this.auth = auth;
}

public String getUserid() {
	return m_num;
}
public void setUserid(String userid) {
	this.m_num = userid;
}
public String getAuth() {
	return auth;
}
public void setAuth(String auth) {
	this.auth = auth;
}

}

