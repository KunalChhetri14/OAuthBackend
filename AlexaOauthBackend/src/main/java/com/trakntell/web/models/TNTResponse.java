package com.trakntell.web.models;

public class TNTResponse {
	public static final String STATUS_OK	= "ok";
	public static final String STATUS_ERROR	= "error";
	
	private Object response;
	private String status;
	private String errormsg;
	
	public TNTResponse() {}
	
	public TNTResponse(String status, Object resp) {
		this.status = status;
		this.response = resp;
	}

	public Object getResponse() {
		return response;
	}

	public void setResponse(Object response) {
		this.response = response;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	
	
}
