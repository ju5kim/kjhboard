package com.kjh.board.vo;

import java.util.Date;
import java.util.List;

public class AuthMember {
private String userid;
private String userpw;
private String userName;
private boolean enabled;

private Date regdate;
private Date updateDate;
private List<AuthVO> authList;



public AuthMember() {
}

public AuthMember(String userid, String userpw, String userName, boolean enabled, Date regdate, Date updateDate,
		List<AuthVO> authList) {
	this.userid = userid;
	this.userpw = userpw;
	this.userName = userName;
	this.enabled = enabled;
	this.regdate = regdate;
	this.updateDate = updateDate;
	this.authList = authList;
}
public String getUserid() {
	return userid;
}
public void setUserid(String userid) {
	this.userid = userid;
}
public String getUserpw() {
	return userpw;
}
public void setUserpw(String userpw) {
	this.userpw = userpw;
}
public String getUserName() {
	return userName;
}
public void setUserName(String userName) {
	this.userName = userName;
}
public boolean isEnabled() {
	return enabled;
}
public void setEnabled(boolean enabled) {
	this.enabled = enabled;
}
public Date getRegdate() {
	return regdate;
}
public void setRegdate(Date regdate) {
	this.regdate = regdate;
}
public Date getUpdateDate() {
	return updateDate;
}
public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
}
public List<AuthVO> getAuthList() {
	return authList;
}
public void setAuthList(List<AuthVO> authList) {
	this.authList = authList;
}



}
