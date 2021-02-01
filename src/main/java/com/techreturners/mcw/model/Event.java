package com.techreturners.mcw.model;

public class Event {

	private Integer eventId;
	private String link;
	private String title;
	private String description;
	private String eventDate;
	private String eventTime;
	private Boolean isCancelled;

	public Event() {
	}

	public Event(Integer eventId, String link, String title, String description, String eventDate, String eventTime,
			Boolean isCancelled) {
		super();
		this.eventId = eventId;
		this.link = link;
		this.title = title;
		this.description = description;
		this.eventDate = eventDate;
		this.eventTime = eventTime;
		this.isCancelled = isCancelled;
	}

	public Integer getEventId() {
		return eventId;
	}

	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public Boolean getIsCancelled() {
		return isCancelled;
	}

	public void setIsCancellede(Boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

}
