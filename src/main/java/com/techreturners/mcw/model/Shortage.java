package com.techreturners.mcw.model;

public class Shortage {
	
	private Integer catId;
	private Integer countyId;
	private String category;
	private String basearea;
	private String county;
	private String isWaterShortage;
	
	public Shortage() {}
	public Shortage(Integer catId, Integer countyId, String category, String basearea, String county,
			String isWaterShortage) {
		super();
		this.catId = catId;
		this.countyId = countyId;
		this.category = category;
		this.basearea = basearea;
		this.county = county;
		this.isWaterShortage = isWaterShortage;
	}
	public Integer getCatId() {
		return catId;
	}
	public void setCatId(Integer catId) {
		this.catId = catId;
	}
	public Integer getCountyId() {
		return countyId;
	}
	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getBasearea() {
		return basearea;
	}
	public void setBasearea(String basearea) {
		this.basearea = basearea;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getIsWaterShortage() {
		return isWaterShortage;
	}
	public void setIsWaterShortage(String isWaterShortage) {
		this.isWaterShortage = isWaterShortage;
	}

	
}
