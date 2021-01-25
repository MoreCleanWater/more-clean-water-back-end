package com.techreturners.mcw.model;

public class County {

	private Long countyId;
	private String county;
    County() {}

	public County(Long countyId, String county) {
		super();
		this.countyId = countyId;
		this.county = county;
	}
	public Long getCountyId() {
		return countyId;
	}
	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}

}
