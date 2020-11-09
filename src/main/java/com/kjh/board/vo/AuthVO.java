package com.kjh.board.vo;

public class AuthVO {
private String userid;
private String auth;


public AuthVO() {
}


public AuthVO(String userid, String auth) {
	super();
	this.userid = userid;
	this.auth = auth;
}

public String getUserid() {
	return userid;
}
public void setUserid(String userid) {
	this.userid = userid;
}
public String getAuth() {
	return auth;
}
public void setAuth(String auth) {
	this.auth = auth;
}

}

