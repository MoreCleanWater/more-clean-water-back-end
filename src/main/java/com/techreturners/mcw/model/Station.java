package com.techreturners.mcw.model;

import java.util.Date;

public class Station {

	private Long station_id;
	private Integer postcode_id;
	private Integer size;
	private String capacity;
	private Date  installation_date;
	private Boolean is_working;
	
	public Station() {}
	
	public Station(Long station_id, Integer postcode_id, Integer size, String capacity, Date installation_date,
			Boolean is_working) {
		super();
		this.station_id = station_id;
		this.postcode_id = postcode_id;
		this.size = size;
		this.capacity = capacity;
		this.installation_date = installation_date;
		this.is_working = is_working;
	}


	public Long getStation_id() {
		return station_id;
	}
	public void setStation_id(Long station_id) {
		this.station_id = station_id;
	}
	public Integer getPostcode_id() {
		return postcode_id;
	}
	public void setPostcode_id(Integer postcode_id) {
		this.postcode_id = postcode_id;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}
	public String getCapacity() {
		return capacity;
	}
	public void setCapacity(String capacity) {
		this.capacity = capacity;
	}
	public Date getInstallation_date() {
		return installation_date;
	}
	public void setInstallation_date(Date installation_date) {
		this.installation_date = installation_date;
	}
	public Boolean getIs_working() {
		return is_working;
	}
	public void setIs_working(Boolean is_working) {
		this.is_working = is_working;
	}
	
}
