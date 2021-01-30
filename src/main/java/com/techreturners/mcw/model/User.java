package com.techreturners.mcw.model;

public class User {
	private Long userId;
	private String userName;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String saltValue;
	private Integer postcodeId;
	private Integer countyId;
	private String postcode;
	private String county;
	private Boolean isActive;
	private Boolean isSubscriber;

	public User() {
	}

	// for list in Admin Module
	public User(Long userId, Integer countyId, String userName, String firstName, String lastName, String email,
			String password, String postcode, Boolean isActive, Boolean isSubscriber) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.postcode = postcode;
		this.countyId = countyId;
		this.isActive = isActive;
		this.isSubscriber = isSubscriber;
	}

	// for User listing 
	public User(Long userId,String userName, String postcode,String county,Integer countyId, String firstName, String lastName, String email,
			Boolean isActive, Boolean isSubscriber) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.postcode = postcode;
		this.county = county;
		this.countyId = countyId;
		this.isActive = isActive;
		this.isSubscriber = isSubscriber;
	}

	// for authentication
	public User(Long userId, String userName, String password, String saltValue, Boolean isActive) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.saltValue = saltValue;
		this.isActive = isActive;
	}

	// for getting emails
	public User(String firstName, String lastName, String email, Boolean isSubscriber) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.isSubscriber = isSubscriber;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}
	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getSaltValue() {
		return saltValue;
	}

	public void setSaltValue(String saltValue) {
		this.saltValue = saltValue;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsSubscriber() {
		return isSubscriber;
	}

	public void setIsSubscriber(Boolean isSubscriber) {
		this.isSubscriber = isSubscriber;
	}

}
