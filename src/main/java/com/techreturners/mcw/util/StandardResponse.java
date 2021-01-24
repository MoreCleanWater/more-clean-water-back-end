package com.techreturners.mcw.util;

public class StandardResponse {

	/*  Needs to be explored and implemented to customise and handle the error/success messages  */

	public enum StatusResponse {

		SUCCESS("Success"), ERROR("Error");

		final private String status;

		StatusResponse(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}
	}
}
