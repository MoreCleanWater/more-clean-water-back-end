package com.techreturners.mcw.model;

public class Section {

	private Long station_id;
	private String name;
	private String description;

	public Section() {
	}

	public Section(Long station_id,String name, String description) {
		super();
		this.station_id = station_id;
		this.name = name;
		this.description = description;
	}

	public Long getStation_id() {
		return station_id;
	}

	public void setStation_id(Long station_id) {
		this.station_id = station_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
