package com.techreturners.mcw.model;

import java.util.Date;

public class Station {

	private Integer stationId;
	private Integer postcodeId;
	private Integer countyId;
	private String postcode;
	private String lat;
	private String lang;
	private Integer size;
	private String capacity;
	private Date installationDate;
	private String installDate;


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
	public Station(Integer stationId, Integer countyId, String postcode, Integer size, String capacity,
			String installDate, Boolean isWorking) {
		super();
		this.stationId = stationId;
		this.countyId = countyId;
		this.postcode = postcode;
		this.size = size;
		this.capacity = capacity;
		this.installDate = installDate;
		this.isWorking = isWorking;
	}
	
	public Station(Integer stationId, Integer countyId, String postcode,String lat,String lang, Integer size, String capacity,
			String installDate, Boolean isWorking) {
		super();
		this.stationId = stationId;
		this.countyId = countyId;
		this.postcode = postcode;
		this.lat = lat;
		this.lang = lang;
		this.size = size;
		this.capacity = capacity;
		this.installDate = installDate;
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

	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
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

	public String getInstallDate() {
		return installDate;
	}

	public void setInstallDate(String installDate) {
		this.installDate = installDate;
	}

	public Boolean getIsWorking() {
		return isWorking;
	}

	public void setIsWorking(Boolean isWorking) {
		this.isWorking = isWorking;
	}

}
