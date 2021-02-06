package com.techreturners.mcw.model;

public class Alert {
	
	private Long userId;
	private Long alert_id;
	private Integer countyId;
	private String alertType;
	private Boolean isRead;
	public Alert() {}
	
	public Alert(Long alert_id,String alertType, Boolean isRead) {
		super();
		this.alert_id = alert_id;
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

	public Long getAlert_id() {
		return alert_id;
	}

	public void setAlert_id(Long alert_id) {
		this.alert_id = alert_id;
	}
	
	
	

}
