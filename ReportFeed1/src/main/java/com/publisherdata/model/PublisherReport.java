package com.publisherdata.model;

import java.util.ArrayList;
import java.util.List;

import com.websystique.springmvc.model.Reports;

//Publisher Report Structure Defining Publisher Report Fields

public class PublisherReport {

	    private String subcategorycode;
	
		public String getSubcategorycode() {
			return subcategorycode;
		}

		public void setSubcategorycode(String subcategorycode) {
			this.subcategorycode = subcategorycode;
		}


		private String date;
		
		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getAudience_segment() {
			return audience_segment;
		}

		public void setAudience_segment(String audience_segment) {
			this.audience_segment = audience_segment;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getOs() {
			return os;
		}

		public void setOs(String os) {
			this.os = os;
		}

		
		   public String getISP() {
				return ISP;
			}

			public void setISP(String iSP) {
				ISP = iSP;
			}

			public String getOrganisation() {
				return organisation;
			}

			public void setOrganisation(String organisation) {
				this.organisation = organisation;
			}


			private String ISP;
		    
		    private String organisation;
		
		
		    
		private String subcategory;
	
		public String getSubcategory() {
			return subcategory;
		}

		public void setSubcategory(String subcategory) {
			this.subcategory = subcategory;
		}


		private String channelName;
		
		private String browser;
		
		public String getBrowser() {
			return browser;
		}

		public void setBrowser(String browser) {
			this.browser = browser;
		}

		public String getChannelName() {
			return channelName;
		}

		public void setChannelName(String channelName) {
			this.channelName = channelName;
		}

		

		public String getBrandname() {
			return brandname;
		}

		public void setBrandname(String brandname) {
			this.brandname = brandname;
		}


		
		
		
		private List<PublisherReport> audience_segment_data = new ArrayList<PublisherReport>();
		
		public List<PublisherReport> getAudience_segment_data() {
			return audience_segment_data;
		}

		public void setAudience_segment_data(List<PublisherReport> audience_segment_data) {
			this.audience_segment_data = audience_segment_data;
		}

		
		public String getDevice_type() {
			return device_type;
		}

		public void setDevice_type(String device_type) {
			this.device_type = device_type;
		}


		private String device_type;
		
		private String publisher_pages;
		
		private String time_of_day;
		
		private String audience_segment;
		
		private String city;
		
		private String postalcode;
		
		private String latitude_longitude;
		
		private String os;
		
		private String devicecode;
		
		private String audienceSegmentCode;
		
		public String getAudienceSegmentCode() {
			return audienceSegmentCode;
		}

		public void setAudienceSegmentCode(String audienceSegmentCode) {
			this.audienceSegmentCode = audienceSegmentCode;
		}

		public String getDevicecode() {
			return devicecode;
		}

		public void setDevicecode(String devicecode) {
			this.devicecode = devicecode;
		}

		public String getLocationcode() {
			return locationcode;
		}

		public void setLocationcode(String locationcode) {
			this.locationcode = locationcode;
		}

		public String getOscode() {
			return oscode;
		}

		public void setOscode(String oscode) {
			this.oscode = oscode;
		}


		private String locationcode;
		
		private String oscode;
		
		public String incomelevel;
		
		
		

		public String getIncomelevel() {
			return incomelevel;
		}

		public void setIncomelevel(String incomelevel) {
			this.incomelevel = incomelevel;
		}


		private String mobile_device_model_name;

	    public String getMobile_device_model_name() {
			return mobile_device_model_name;
		}

		public void setMobile_device_model_name(String mobile_device_model_name) {
			this.mobile_device_model_name = mobile_device_model_name;
		}


		private String brandname;
	    
	    public String getArticle() {
			return Article;
		}

		public void setArticle(String article) {
			Article = article;
		}

		public String getSection() {
			return Section;
		}

		public void setSection(String section) {
			Section = section;
		}


		private String Article;
	    
		private String Country;
		
		public String getCountry() {
			return Country;
		}

		public void setCountry(String country) {
			Country = country;
		}

		public String getState() {
			return State;
		}

		public void setState(String state) {
			State = state;
		}


		private String State;
		
	    private String Section;

	    public String getReach() {
			return reach;
		}

		public void setReach(String reach) {
			this.reach = reach;
		}


		private String reach;
	    
		public String getPublisher_pages() {
			return publisher_pages;
		}

		public void setPublisher_pages(String publisher_pages) {
			this.publisher_pages = publisher_pages;
		}

		public String getTime_of_day() {
			return time_of_day;
		}

		public void setTime_of_day(String time_of_day) {
			this.time_of_day = time_of_day;
		}

		public String getPostalcode() {
			return postalcode;
		}

		public void setPostalcode(String postalcode) {
			this.postalcode = postalcode;
		}

		public String getLatitude_longitude() {
			return latitude_longitude;
		}

		public void setLatitude_longitude(String latitude_longitude) {
			this.latitude_longitude = latitude_longitude;
		}

		public String getCount() {
			return count;
		}

		public void setCount(String count) {
			this.count = count;
		}


		private String count;
	    
	    
	    public String getAge() {
			return age;
		}

		public void setAge(String age) {
			this.age = age;
		}

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		private String age;
	    
	    private String gender;
	    
	    private String referrerSource;

	    private String screen_properties;
	    
	    
	    public String getScreen_properties() {
			return screen_properties;
		}

		public void setScreen_properties(String screen_properties) {
			this.screen_properties = screen_properties;
		}

		public String getTotalvisits() {
			return totalvisits;
		}

		public void setTotalvisits(String totalvisits) {
			this.totalvisits = totalvisits;
		}


		private String totalvisits;
	    
	    
		public String getReferrerSource() {
			return referrerSource;
		}

		public void setReferrerSource(String referrerSource) {
			this.referrerSource = referrerSource;
		}

		
	
		  public String articleAuthor;

			public String getArticleAuthor() {
				return articleAuthor;
			}

			public void setArticleAuthor(String articleAuthor) {
				this.articleAuthor = articleAuthor;
			}

	 
			public String articleTags;

			public String getArticleTags() {
				return articleTags;
			}

			public void setArticleTags(String articleTags) {
				this.articleTags = articleTags;
			}




          private String externalWorldCount;
          
          public String getExternalWorldCount() {
			return externalWorldCount;
		}

		public void setExternalWorldCount(String externalWorldCount) {
			this.externalWorldCount = externalWorldCount;
		}

		public String getAverageTime() {
			return averageTime;
		}

		public void setAverageTime(String averageTime) {
			this.averageTime = averageTime;
		}


		private String averageTime;


        public String getVisitorCount() {
			return visitorCount;
		}

		public void setVisitorCount(String visitorCount) {
			this.visitorCount = visitorCount;
		}


		private String visitorCount;

        public String getEngagementTime() {
			return engagementTime;
		}

		public void setEngagementTime(String engagementTime) {
			this.engagementTime = engagementTime;
		}


		public String getArticleTitle() {
			return articleTitle;
		}

		public void setArticleTitle(String articleTitle) {
			this.articleTitle = articleTitle;
		}


		private String articleTitle;
		
		private String engagementTime;
        
        private String minutesperVisitor;

		public String getMinutesperVisitor() {
			return minutesperVisitor;
		}

		public void setMinutesperVisitor(String minutesperVisitor) {
			this.minutesperVisitor = minutesperVisitor;
		}
		
     
		private String OSversion;

		public String getOSversion() {
			return OSversion;
		}

		public void setOSversion(String oSversion) {
			OSversion = oSversion;
		}
		
		
        public String getTrafficType() {
			return trafficType;
		}

		public void setTrafficType(String trafficType) {
			this.trafficType = trafficType;
		}

		public String getSiteExperience() {
			return siteExperience;
		}

		public void setSiteExperience(String siteExperience) {
			this.siteExperience = siteExperience;
		}


		private String trafficType;
        
        private String siteExperience;
		
        public String getRetention() {
			return retention;
		}

		public void setRetention(String retention) {
			this.retention = retention;
		}


		private String retention;


		private String ArticleVersion;

		public String getArticleVersion() {
			return ArticleVersion;
		}

		public void setArticleVersion(String articleVersion) {
			ArticleVersion = articleVersion;
		}
		
		
        private String visitorType;

		public String getVisitorType() {
			return visitorType;
		}

		public void setVisitorType(String visitorType) {
			this.visitorType = visitorType;
		}
		
		private String referrerType;

		public String getReferrerType() {
			return referrerType;
		}

		public void setReferrerType(String referrerType) {
			this.referrerType = referrerType;
		}
		
		

}














