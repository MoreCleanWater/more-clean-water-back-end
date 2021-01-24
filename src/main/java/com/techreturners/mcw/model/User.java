package com.techreturners.mcw.model;

public class User {

	private Long user_id;
	private String first_name;
	private String last_name;
	private String email;
	private String password;
	private String salt_value;
	private Integer postcode_id;
	private Boolean is_admin;
	private Boolean is_remove;
	
	public User() {}
	
	public User(Long user_id, Integer postcode_id,String first_name,String last_name, String password, String salt_value,String email,
			Boolean is_admin, Boolean is_remove) {
		super();
		this.user_id=user_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.password = password;
		this.salt_value = salt_value;
		this.postcode_id = postcode_id;
		this.is_admin = is_admin;
		this.is_remove = is_remove;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
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

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt_value() {
		return salt_value;
	}

	public void setSalt_value(String salt_value) {
		this.salt_value = salt_value;
	}

	public Integer getPostcode_id() {
		return postcode_id;
	}

	public void setPostcode_id(Integer postcode_id) {
		this.postcode_id = postcode_id;
	}

	public Boolean getIs_admin() {
		return is_admin;
	}

	public void setIs_admin(Boolean is_admin) {
		this.is_admin = is_admin;
	}

	public Boolean getIs_remove() {
		return is_remove;
	}

	public void setIs_remove(Boolean is_remove) {
		this.is_remove = is_remove;
	}

}
