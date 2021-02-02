package com.techreturners.mcw.model;

public class Alert {
	
	private Long userId;
	private Integer countyId;
	private String alertType;
	private Boolean isRead;
	public Alert() {}
	
	public Alert(String alertType, Boolean isRead) {
		super();
		this.alertType = alertType;
		this.isRead = isRead;
	}

	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getCountyId() {
		return countyId;
	}
	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public Boolean getIsRead() {
		return isRead;
	}
	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}
	
	
	

}
