package com.publisherdata.model;

import java.util.List;

public class SectionResponse {

	
	public List<Section> getData() {
		return data;
	}
	public void setData(List<Section> data) {
		this.data = data;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	private List<Section> data;
	private String code;
	private String status;
	private String message;
	
	
	
	
}
