package com.websystique.springmvc.service;



import java.util.List;
import java.util.Set;

import com.publisherdata.model.Article;
import com.publisherdata.model.PublisherReport;
import com.publisherdata.model.Section;
import com.publisherdata.model.Site;
import com.websystique.springmvc.model.Reports;



public interface ReportService {
	
	List<PublisherReport> extractReports(long id, String dateRange);
	
	
	List<PublisherReport> extractReportsChannel(long id, String dateRange, String channel);

	List<PublisherReport> extractReportsChannelSection(long id, String dateRange, String channel, String sectionName);
	
	List<PublisherReport> extractReportsChannelArticle(long id, String dateRange, String channel, String articleName);
	
	List<PublisherReport> extractReportsChannelLive(long id, String dateRange, String channel);

    List<Article> getArticleList(String siteid);
	
    List<Section> getSectionList(String siteid);
//	List<PublisherReport> extractReportsChannelNames(long id, String dateRange, String channelname);
	List<Site> getSiteList(String userid);
    
    Set<String> extractChannelNames(long id,String dateRange);

    //Set<String> extractSections(long id,String dateRange,String channelname);
    
   // Set<String> extractArticles(long id,String dateRange,String channelName);

  //  Set<String> extractSectionArticles(long id,String dateRange,String channelName,String sectionname);

}
