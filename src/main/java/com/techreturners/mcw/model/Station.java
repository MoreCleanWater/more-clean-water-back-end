package com.techreturners.mcw.model;

import java.util.Date;

public class Station {

	private Integer stationId;
	private Integer postcodeId;
	private Integer countyId;
	private String postcode;
	private Integer size;
	private String capacity;
	private Date installationDate;

	public Station(Integer stationId, Integer countyId, String postcode, Integer size, String capacity,
			Date installationDate, Boolean isWorking) {
		super();
		this.stationId = stationId;
		this.countyId = countyId;
		this.postcode = postcode;
		this.size = size;
		this.capacity = capacity;
		this.installationDate = installationDate;
		this.isWorking = isWorking;
	}

	private Boolean isWorking;

	public Station() {
	}

	public Integer getStationId() {
		return stationId;
	}

	public void setStationId(Integer stationId) {
		this.stationId = stationId;
	}

	public Integer getPostcodeId() {
		return postcodeId;
	}

	public void setPostcodeId(Integer postcodeId) {
		this.postcodeId = postcodeId;
	}

	public Integer getCountyId() {
		return countyId;
	}

	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
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

	public Date getInstallationDate() {
		return installationDate;
	}

	public void setInstallationDate(Date installationDate) {
		this.installationDate = installationDate;
	}

	public Boolean getIsWorking() {
		return isWorking;
	}

	public void setIsWorking(Boolean isWorking) {
		this.isWorking = isWorking;
	}

}
