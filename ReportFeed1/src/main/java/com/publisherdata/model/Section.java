package com.publisherdata.model;

public class Section {

	
	private String id;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSectionUrl() {
		return sectionUrl;
	}
	public void setSectionUrl(String sectionUrl) {
		this.sectionUrl = sectionUrl;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	private String sectionUrl;
    private String siteId;
    public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	private String sectionName;

}
