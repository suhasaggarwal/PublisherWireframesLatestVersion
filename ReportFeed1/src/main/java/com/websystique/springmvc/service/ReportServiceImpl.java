package com.websystique.springmvc.service;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.elasticsearch.plugin.nlpcn.executors.CsvExtractorException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import util.GetMiddlewareData;

import com.publisherdata.Daos.AggregationModule;
import com.publisherdata.model.Article;
import com.publisherdata.model.PublisherReport;
import com.publisherdata.model.Section;
import com.publisherdata.model.Site;
import com.websystique.springmvc.model.Reports;

@Service("reportService")
@Transactional
public class ReportServiceImpl implements ReportService{
	
	private static final AtomicLong counter = new AtomicLong();
	
//	ReportDAOImpl repDAO = ReportDAOImpl.getInstance();
	
	List<PublisherReport> report= new ArrayList<PublisherReport>();
	
	Set<String> channelNames = new HashSet<String>();
	
    String channelName = null;
	
    AggregationModule module =  AggregationModule.getInstance();
    
    public List<PublisherReport> extractReports(long id,String dateRange){
	
        String [] dateInterval = dateRange.split(",");
    	
    	
    	if(id == 1){
    	 	try {
				
    	 		module.setUp();
    	 		report=module.countBrandName(dateInterval[0], dateInterval[1]);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
    	}
    	
    	
    	if(id == 2){
    	 	try {
    	 		module.setUp();
				report=module.countBrowser(dateInterval[0], dateInterval[1]);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
    	}
    	
    	
    	
       if(id == 3){
    	 	try {
    	 		module.setUp();
    	 		report=module.countOS(dateInterval[0], dateInterval[1]);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
    	}
    	

      
       if(id == 4){
   	 	try {
   	 	    module.setUp();
			report=module.countModel(dateInterval[0], dateInterval[1]);
		} catch (CsvExtractorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		    return report;
   	    }
       
       
       
    	if(id == 5 ){
    	 	try {
    	 		module.setUp();
				report=module.countCity(dateInterval[0], dateInterval[1]);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
    	}
    	
    	
    	if(id == 6 ){
    	 	try {
    	 		module.setUp();
				report=module.countfingerprint(dateInterval[0], dateInterval[1]);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
    	}
    	
    	if(id == 7 ){
    	 	try {
    	 		module.setUp();
				report=module.countAudienceSegment(dateInterval[0], dateInterval[1]);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
    	}
    	
    	
    	if(id == 8 ){
    	 	try {
    	 		module.setUp();
				report=module.gettimeofday(dateInterval[0], dateInterval[1]);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
    	}
    	
    	if(id == 9 ){
    	 	try {
    	 		module.setUp();
				report=module.countPinCode(dateInterval[0], dateInterval[1]);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
    	}
    	
    	
    	if(id == 10 ){
    	 	try {
    	 		module.setUp();
				report=module.countLatLong(dateInterval[0], dateInterval[1]);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
    	}
    	
    	if(id == 11 ){
    	 	try {
    	 		module.setUp();
				report=module.gettimeofdayQuarter(dateInterval[0], dateInterval[1]);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
    	}
    	
    	if(id == 12 ){
    	 	try {
    	 		module.setUp();
				report=module.gettimeofdayDaily(dateInterval[0], dateInterval[1]);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
    	}
    	
   
    	if(id == 13 ){
    	 	try {
    	 		module.setUp();
				report=module.countAgegroup(dateInterval[0], dateInterval[1]);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
    	}
    	
    	if(id == 14 ){
    	 	try {
    	 		module.setUp();
				report=module.countGender(dateInterval[0], dateInterval[1]);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
    	}
    	
    	if(id == 15 ){
    	 	try {
    	 		module.setUp();
				report=module.countISP(dateInterval[0], dateInterval[1]);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
    	}
    	
    	if(id == 16 ){
    	 	try {
    	 		module.setUp();
				report=module.getOrg(dateInterval[0], dateInterval[1]);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
    	}
    	
    	if(id == 17 ){
    	 	try {
    	 		module.setUp();
				report=module.getdayQuarterdata(dateInterval[0], dateInterval[1]);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
    	}
    	
    	
     return report;
    
    }
    
    	 public List<PublisherReport> extractReportsChannel(long id,String dateRange, String channel){
    			
    	        String [] dateInterval = dateRange.split(",");
    	    	
    	    	
    	    	if(id == 1){
    	    	 	try {
    					
    	    	 		module.setUp();
    	    	 		report=module.countBrandNameChannel(dateInterval[0], dateInterval[1],channel);
    				} catch (CsvExtractorException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			    return report;
    	    	}
    	    	
    	    	
    	    	if(id == 2){
    	    	 	try {
    	    	 		module.setUp();
    					report=module.countBrowserChannel(dateInterval[0], dateInterval[1],channel);
    				} catch (CsvExtractorException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			    return report;
    	    	}
    	    	
    	    	
    	    	
    	       if(id == 3){
    	    	 	try {
    	    	 		module.setUp();
    	    	 		report=module.countOSChannel(dateInterval[0], dateInterval[1],channel);
    				} catch (CsvExtractorException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			    return report;
    	    	}
    	    	

    	      
    	       if(id == 4){
    	   	 	try {
    	   	 	    module.setUp();
    				report=module.countModelChannel(dateInterval[0], dateInterval[1],channel);
    			} catch (CsvExtractorException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    			    return report;
    	   	    }
    	       
    	       
    	       
    	    	if(id == 5 ){
    	    	 	try {
    	    	 		module.setUp();
    					report=module.countCityChannel(dateInterval[0], dateInterval[1],channel);
    				} catch (CsvExtractorException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			    return report;
    	    	}
    	    	
    	    	
    	    	if(id == 6 ){
    	    	 	try {
    	    	 		module.setUp();
    					report=module.countfingerprintChannel(dateInterval[0], dateInterval[1],channel);
    				} catch (CsvExtractorException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			    return report;
    	    	}
    	    	
    	    	
    	    	if(id == 7 ){
    	    	 	try {
    	    	 		module.setUp();
    					report=module.countAudiencesegmentChannel(dateInterval[0], dateInterval[1],channel);
    				} catch (CsvExtractorException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			    return report;
    	    	}
    	    	
    	    	
    	    	if(id == 8 ){
    	    	 	try {
    	    	 		module.setUp();
    					report=module.gettimeofdayChannel(dateInterval[0], dateInterval[1],channel);
    				} catch (CsvExtractorException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			    return report;
    	    	}
    	    	
    	    	
    	    	if(id == 9 ){
    	    	 	try {
    	    	 		module.setUp();
    					report=module.countPinCodeChannel(dateInterval[0], dateInterval[1],channel);
    				} catch (CsvExtractorException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			    return report;
    	    	}
    	    	
    	    	
    	    	if(id == 10 ){
    	    	 	try {
    	    	 		module.setUp();
    					report=module.countLatLongChannel(dateInterval[0], dateInterval[1],channel);
    				} catch (CsvExtractorException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			    return report;
    	    	}
    	    	
    	    	
    	    	if(id == 11 ){
    	    	 	try {
    	    	 		module.setUp();
    					report=module.gettimeofdayQuarterChannel(dateInterval[0], dateInterval[1],channel);
    				} catch (CsvExtractorException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			    return report;
    	    	}
    	    	
    	    	
    	    	if(id == 12 ){
    	    	 	try {
    	    	 		module.setUp();
    					report=module.gettimeofdayDailyChannel(dateInterval[0], dateInterval[1],channel);
    				} catch (CsvExtractorException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			    return report;
    	    	}
    	    	
    	    	

    	    	if(id == 13 ){
    	    	 	try {
    	    	 		module.setUp();
    					report=module.getAgegroupChannel(dateInterval[0], dateInterval[1],channel);
    				} catch (CsvExtractorException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			    return report;
    	    	}
    	    	
    	    	

    	    	if(id == 14 ){
    	    	 	try {
    	    	 		module.setUp();
    			//		report=module.getGenderChannel(dateInterval[0], dateInterval[1],channel);
    				} catch (CsvExtractorException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			    return report;
    	    	}
    	    	
    	    	

    	    	if(id == 15 ){
    	    	 	try {
    	    	 		module.setUp();
    					report=module.getISPChannel(dateInterval[0], dateInterval[1],channel);
    				} catch (CsvExtractorException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			    return report;
    	    	}
    	    	
    	    	

    	    	if(id == 16 ){
    	    	 	try {
    	    	 		module.setUp();
    					report=module.getOrgChannel(dateInterval[0], dateInterval[1],channel);
    				} catch (CsvExtractorException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			    return report;
    	    	}
    	    	
    	    	
    	    	if(id == 17 ){
    	    	 	try {
    	    	 		module.setUp();
    					report=module.getdayQuarterdataChannel(dateInterval[0], dateInterval[1],channel);
    				} catch (CsvExtractorException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
    			    return report;
    	    	}
    	    	
    	    	 if(id == 18 ){
     	    	 	try {
     	    	 		module.setUp();
     	 				report=module.counttotalvisitorsChannel(dateInterval[0], dateInterval[1],channel);
     	 			} catch (CsvExtractorException e) {
     	 				// TODO Auto-generated catch block
     	 				e.printStackTrace();
     	 			} catch (Exception e) {
     	 				// TODO Auto-generated catch block
     	 				e.printStackTrace();
     	 			}
     	 		    return report;
     	    	}
     	 	   	
     	 	   	
     	 		if(id == 19 ){
     	    	 	try {
     	    	 		module.setUp();
     	 				report=module.countUniqueVisitorsChannel(dateInterval[0], dateInterval[1],channel);
     	 			} catch (CsvExtractorException e) {
     	 				// TODO Auto-generated catch block
     	 				e.printStackTrace();
     	 			} catch (Exception e) {
     	 				// TODO Auto-generated catch block
     	 				e.printStackTrace();
     	 			}
     	 		    return report;
     	    	}
    	    	
    	    	
    	    	
    	    	
    	    	
    	    	
    	    	
    	    	
    	    	
    	   	  if(id == 24 ){
    	    	 	try {
    	    	 		module.setUp();
    	 				report=module.countNewUsersChannelDatewise(dateInterval[0], dateInterval[1],channel);
    	 			} catch (CsvExtractorException e) {
    	 				// TODO Auto-generated catch block
    	 				e.printStackTrace();
    	 			} catch (Exception e) {
    	 				// TODO Auto-generated catch block
    	 				e.printStackTrace();
    	 			}
    	 		    return report;
    	    	}
    	 	   	
    	 	   	
    	 		if(id == 25 ){
    	    	 	try {
    	    	 		module.setUp();
    	 				report=module.countReturningUsersChannelDatewise(dateInterval[0], dateInterval[1],channel);
    	 			} catch (CsvExtractorException e) {
    	 				// TODO Auto-generated catch block
    	 				e.printStackTrace();
    	 			} catch (Exception e) {
    	 				// TODO Auto-generated catch block
    	 				e.printStackTrace();
    	 			}
    	 		    return report;
    	    	}
    	 	    	
    	    	 
    	 		if(id == 26 ){
    	    	 	try {
    	    	 		module.setUp();
    	 				report=module.countLoyalUsersChannelDatewise(dateInterval[0], dateInterval[1],channel);
    	 			} catch (CsvExtractorException e) {
    	 				// TODO Auto-generated catch block
    	 				e.printStackTrace();
    	 			} catch (Exception e) {
    	 				// TODO Auto-generated catch block
    	 				e.printStackTrace();
    	 			}
    	 		    return report;
    	    	}
    	 		
    	 				
    	    	
          	return report;
    

    }
		
    	 public List<PublisherReport> extractReportsChannelArticle(long id,String dateRange, String channel, String articleName){
 			
 	        String [] dateInterval = dateRange.split(",");
 	    	
 	    	
 	    	if(id == 1){
 	    	 	try {
 					
 	    	 		module.setUp();
 	    	 		report=module.countBrandNameChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 2){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countBrowserChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	
 	       if(id == 3){
 	    	 	try {
 	    	 		module.setUp();
 	    	 		report=module.countOSChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	

 	      
 	       if(id == 4){
 	   	 	try {
 	   	 	    module.setUp();
 				report=module.countModelChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
 			} catch (CsvExtractorException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			} catch (Exception e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 			    return report;
 	   	    }
 	       
 	       
 	       
 	    	if(id == 5 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countCityChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 6 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countfingerprintChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 7 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countAudiencesegmentChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 8 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.gettimeofdayChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 9 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countPinCodeChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 10 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countLatLongChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 11 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.gettimeofdayQuarterChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 12 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.gettimeofdayDailyChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	

 	    	if(id == 13 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.getAgegroupChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	

 	    	if(id == 14 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.getGenderChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	

 	    	if(id == 15 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.getISPChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	

 	    	if(id == 16 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.getOrgChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 17 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.getdayQuarterdataChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 18 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.getChannelArticleReferredPostsList(dateInterval[0], dateInterval[1],channel,articleName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	
           
 	    	}	 
    	 
    	
 	    	if(id == 19 ){
	    	 	try {
	    	 		module.setUp();
					report=module.getChannelArticleReferrerList1(dateInterval[0], dateInterval[1],channel,articleName);
				} catch (CsvExtractorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    return report;
	    	}
    	 
    	 
 	    	if(id == 20 ){
	    	 	try {
	    	 		module.setUp();
					report=module.counttotalvisitorsChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
				} catch (CsvExtractorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    return report;
	    	}
    	 
 	    	if(id == 21 ){
	    	 	try {
	    	 		module.setUp();
					report=module.counttotalvisitorsChannelArticleDatewise(dateInterval[0], dateInterval[1],channel,articleName);
				} catch (CsvExtractorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    return report;
	    	}
 	    	
 	    	if(id == 22 ){
	    	 	try {
	    	 		module.setUp();
					report=module.countfingerprintChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
				} catch (CsvExtractorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    return report;
	    	}
    	
 	    	
 	    	if(id == 23 ){
	    	 	try {
	    	 		module.setUp();
					report=module.countfingerprintChannelArticleDatewise(dateInterval[0], dateInterval[1],channel,articleName);
				} catch (CsvExtractorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    return report;
	    	}
 	    	
 	    	
 	   	  if(id == 24 ){
    	 	try {
    	 		module.setUp();
				report=module.countNewUsersChannelArticleDatewise(dateInterval[0], dateInterval[1],channel,articleName);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
    	}
 	   	
 	   	
 		if(id == 25 ){
    	 	try {
    	 		module.setUp();
				report=module.countReturningUsersChannelArticleDatewise(dateInterval[0], dateInterval[1],channel,articleName);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
    	}
 	    	
    	 
 		if(id == 26 ){
    	 	try {
    	 		module.setUp();
				report=module.countLoyalUsersChannelArticleDatewise(dateInterval[0], dateInterval[1],channel,articleName);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
    	}
   
    	
 		if(id ==27){
 			
 			try {
    	 		module.setUp();
				report=module.countLoyalUsersChannelArticleDatewise(dateInterval[0], dateInterval[1],channel,articleName);
			} catch (CsvExtractorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return report;
 			
 			
 			
 			
 		}
 		
 		  if(id == 28){
 	 	   	 	try {
 	 	   	 	    module.setUp();
 	 				report=module.getDeviceTypeChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
 	 			} catch (CsvExtractorException e) {
 	 				// TODO Auto-generated catch block
 	 				e.printStackTrace();
 	 			} catch (Exception e) {
 	 				// TODO Auto-generated catch block
 	 				e.printStackTrace();
 	 			}
 	 			    return report;
 	 	   	    }
 		
 		

 		  if(id == 29){
 	 	   	 	try {
 	 	   	 	    module.setUp();
 	 				report=module.getIncomeChannelArticle(dateInterval[0], dateInterval[1],channel,articleName);
 	 			} catch (CsvExtractorException e) {
 	 				// TODO Auto-generated catch block
 	 				e.printStackTrace();
 	 			} catch (Exception e) {
 	 				// TODO Auto-generated catch block
 	 				e.printStackTrace();
 	 			}
 	 			    return report;
 	 	   	    }
 		  
 		  
 		  
 		  if(id == 30){
	 	   	 	try {
	 	   	 	    module.setUp();
	 				report=module.getArticleMetaData(dateInterval[0], dateInterval[1],channel,articleName);
	 			} catch (CsvExtractorException e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			} catch (Exception e) {
	 				// TODO Auto-generated catch block
	 				e.printStackTrace();
	 			}
	 			    return report;
	 	   	    }
 		  
 		  
 		  
 		  
    	 return report;
    	 
    	 }	
 		

    	 public List<PublisherReport> extractReportsChannelSection(long id,String dateRange, String channel, String SectionName){
 			
 	        String [] dateInterval = dateRange.split(",");
 	    	
 	    	
 	    	if(id == 1){
 	    	 	try {
 					
 	    	 		module.setUp();
 	    	 		report=module.countBrandNameChannelSection(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 2){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countBrowserChannelSection(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	
 	       if(id == 3){
 	    	 	try {
 	    	 		module.setUp();
 	    	 		report=module.countOSChannelSection(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	

 	      
 	       if(id == 4){
 	   	 	try {
 	   	 	    module.setUp();
 				report=module.countModelChannelSection(dateInterval[0], dateInterval[1],channel,SectionName);
 			} catch (CsvExtractorException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			} catch (Exception e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 			    return report;
 	   	    }
 	       
 	       
 	       
 	    	if(id == 5 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countCityChannelSection(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 6 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countfingerprintChannelSection(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 7 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countAudiencesegmentChannelSection(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 8 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.gettimeofdayChannelSection(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 9 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countPinCodeChannelSection(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 10 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countLatLongChannelSection(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 11 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.gettimeofdayQuarterChannelSection(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 12 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.gettimeofdayDailyChannelSection(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	

 	    	if(id == 13 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.getAgegroupChannelSection(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	

 	    	if(id == 14 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.getGenderChannelSection(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	

 	    	if(id == 15 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.getISPChannelSection(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	

 	    	if(id == 16 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.getOrgChannelSection(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 17 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.getdayQuarterdataChannelSection(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 18 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.getChannelSectionReferredPostsList(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	
           
 	    	}	 
    	 
    	
 	    	if(id == 19 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.getChannelSectionReferrerList(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
    	 
    	 
 	    	if(id == 20 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.counttotalvisitorsChannelSection(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
    	 
 	    	if(id == 21 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.counttotalvisitorsChannelSectionDatewise(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	if(id == 22 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countfingerprintChannelSection(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
    	
 	    	
 	    	if(id == 23 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countfingerprintChannelSectionDatewise(dateInterval[0], dateInterval[1],channel,SectionName);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	   	  if(id == 24 ){
    	 	try {
    	 		module.setUp();
 				report=module.countNewUsersChannelSectionDatewise(dateInterval[0], dateInterval[1],channel,SectionName);
 			} catch (CsvExtractorException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			} catch (Exception e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 		    return report;
    	}
 	   	
 	   	
 		if(id == 25 ){
    	 	try {
    	 		module.setUp();
 				report=module.countReturningUsersChannelSectionDatewise(dateInterval[0], dateInterval[1],channel,SectionName);
 			} catch (CsvExtractorException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			} catch (Exception e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 		    return report;
    	}
 	    	
    	 
 		if(id == 26 ){
    	 	try {
    	 		module.setUp();
 				report=module.countLoyalUsersChannelSectionDatewise(dateInterval[0], dateInterval[1],channel,SectionName);
 			} catch (CsvExtractorException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			} catch (Exception e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 		    return report;
    	}
 		
 				
    	
 		if(id == 27 ){
	    	 	try {
	    	 		module.setUp();
					report=module.counttotalvisitorsChannelSectionDateHourlywise(dateInterval[0], dateInterval[1],channel,SectionName);
				} catch (CsvExtractorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    return report;
	    	}
 		
 		
 		
 		
 		if(id == 28 ){
	    	 	try {
	    	 		module.setUp();
					report=module.counttotalvisitorsChannelSectionDateHourlyMinutewise(dateInterval[0], dateInterval[1],channel,SectionName);
				} catch (CsvExtractorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    return report;
	    	}
 		
 		
 		
 		
 		
 		
 		
 		
 		
 		
 		
 		return report;
    	 
    	 }	
    	 
    	
    	 public List<PublisherReport> extractReportsChannelLive(long id,String dateRange, String channel){
 			
 	        String [] dateInterval = dateRange.split(",");
 	    	
 	    	
 	    	if(id == 1){
 	    	 	try {
 					
 	    	 		module.setUp();
 	    	 		report=module.countBrandNameChannelLive(dateInterval[0], dateInterval[1],channel);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 2){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countBrowserChannelLive(dateInterval[0], dateInterval[1],channel);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	
 	       if(id == 3){
 	    	 	try {
 	    	 		module.setUp();
 	    	 		report=module.countOSChannelLive(dateInterval[0], dateInterval[1],channel);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	

 	      
 	       if(id == 4){
 	   	 	try {
 	   	 	    module.setUp();
 				report=module.countModelChannelLive(dateInterval[0], dateInterval[1],channel);
 			} catch (CsvExtractorException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			} catch (Exception e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 			    return report;
 	   	    }
 	       
 	       
 	       
 	    	if(id == 5 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countCityChannelLive(dateInterval[0], dateInterval[1],channel);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 6 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countfingerprintChannelLive(dateInterval[0], dateInterval[1],channel);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 7 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countAudiencesegmentChannelLive(dateInterval[0], dateInterval[1],channel);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 8 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.gettimeofdayChannelLive(dateInterval[0], dateInterval[1],channel);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 9 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countPinCodeChannelLive(dateInterval[0], dateInterval[1],channel);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 10 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.countLatLongChannelLive(dateInterval[0], dateInterval[1],channel);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 11 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.gettimeofdayQuarterChannelLive(dateInterval[0], dateInterval[1],channel);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 12 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.gettimeofdayDailyChannelLive(dateInterval[0], dateInterval[1],channel);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	

 	    	if(id == 13 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.getAgegroupChannelLive(dateInterval[0], dateInterval[1],channel);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	

 	    	if(id == 14 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.getGenderChannelLive(dateInterval[0], dateInterval[1],channel);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	

 	    	if(id == 15 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.getISPChannelLive(dateInterval[0], dateInterval[1],channel);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	

 	    	if(id == 16 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.getOrgChannelLive(dateInterval[0], dateInterval[1],channel);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	
 	    	if(id == 17 ){
 	    	 	try {
 	    	 		module.setUp();
 					report=module.getdayQuarterdataChannelLive(dateInterval[0], dateInterval[1],channel);
 				} catch (CsvExtractorException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				} catch (Exception e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}
 			    return report;
 	    	}
 	    	
 	    	 if(id == 18 ){
  	    	 	try {
  	    	 		module.setUp();
  	 				report=module.counttotalvisitorsChannelLive(dateInterval[0], dateInterval[1],channel);
  	 			} catch (CsvExtractorException e) {
  	 				// TODO Auto-generated catch block
  	 				e.printStackTrace();
  	 			} catch (Exception e) {
  	 				// TODO Auto-generated catch block
  	 				e.printStackTrace();
  	 			}
  	 		    return report;
  	    	}
  	 	   	
  	 	   	
  	 		if(id == 19 ){
  	    	 	try {
  	    	 		module.setUp();
  	 				report=module.countUniqueVisitorsChannelLive(dateInterval[0], dateInterval[1],channel);
  	 			} catch (CsvExtractorException e) {
  	 				// TODO Auto-generated catch block
  	 				e.printStackTrace();
  	 			} catch (Exception e) {
  	 				// TODO Auto-generated catch block
  	 				e.printStackTrace();
  	 			}
  	 		    return report;
  	    	}
 	    	
 	    	
 	    	
 	    	
 	    	
 	    	
 	    	
 	    	
 	    	
 	   	  if(id == 24 ){
 	    	 	try {
 	    	 		module.setUp();
 	 				report=module.countNewUsersChannelLiveDatewise(dateInterval[0], dateInterval[1],channel);
 	 			} catch (CsvExtractorException e) {
 	 				// TODO Auto-generated catch block
 	 				e.printStackTrace();
 	 			} catch (Exception e) {
 	 				// TODO Auto-generated catch block
 	 				e.printStackTrace();
 	 			}
 	 		    return report;
 	    	}
 	 	   	
 	 	   	
 	 		if(id == 25 ){
 	    	 	try {
 	    	 		module.setUp();
 	 				report=module.countReturningUsersChannelLiveDatewise(dateInterval[0], dateInterval[1],channel);
 	 			} catch (CsvExtractorException e) {
 	 				// TODO Auto-generated catch block
 	 				e.printStackTrace();
 	 			} catch (Exception e) {
 	 				// TODO Auto-generated catch block
 	 				e.printStackTrace();
 	 			}
 	 		    return report;
 	    	}
 	 	    	
 	    	 
 	 		if(id == 26 ){
 	    	 	try {
 	    	 		module.setUp();
 	 				report=module.countLoyalUsersChannelLiveDatewise(dateInterval[0], dateInterval[1],channel);
 	 			} catch (CsvExtractorException e) {
 	 				// TODO Auto-generated catch block
 	 				e.printStackTrace();
 	 			} catch (Exception e) {
 	 				// TODO Auto-generated catch block
 	 				e.printStackTrace();
 	 			}
 	 		    return report;
 	    	}
 	 		
 	 				
 	 		if(id == 27 ){
 	    	 	try {
 	    	 		module.setUp();
 	 				report=module.getTopPostsbyTotalPageviewschannelLive(dateInterval[0], dateInterval[1],channel);
 	 			} catch (CsvExtractorException e) {
 	 				// TODO Auto-generated catch block
 	 				e.printStackTrace();
 	 			} catch (Exception e) {
 	 				// TODO Auto-generated catch block
 	 				e.printStackTrace();
 	 			}
 	 		    return report;
 	    	}
 	 		
 	 		
 	 		if(id == 28 ){
 	    	 	try {
 	    	 		module.setUp();
 	 				report=module.getTopPostsbyUniqueViewschannelLive(dateInterval[0], dateInterval[1],channel);
 	 			} catch (CsvExtractorException e) {
 	 				// TODO Auto-generated catch block
 	 				e.printStackTrace();
 	 			} catch (Exception e) {
 	 				// TODO Auto-generated catch block
 	 				e.printStackTrace();
 	 			}
 	 		    return report;
 	    	}
 	 		
 	 		if(id == 29 ){
 	    	 	try {
 	    	 		module.setUp();
 	 				report=module.getRefererPostsChannelLive(dateInterval[0], dateInterval[1],channel);
 	 			} catch (CsvExtractorException e) {
 	 				// TODO Auto-generated catch block
 	 				e.printStackTrace();
 	 			} catch (Exception e) {
 	 				// TODO Auto-generated catch block
 	 				e.printStackTrace();
 	 			}
 	 		    return report;
 	    	}
 	 		
 	 		if(id == 30 ){
 	    	 	try {
 	    	 		module.setUp();
 	 				report=module.getNewContentChannelLive(dateInterval[0], dateInterval[1],channel);
 	 			} catch (CsvExtractorException e) {
 	 				// TODO Auto-generated catch block
 	 				e.printStackTrace();
 	 			} catch (Exception e) {
 	 				// TODO Auto-generated catch block
 	 				e.printStackTrace();
 	 			}
 	 		    return report;
 	    	}
 	 		
 	 		
       	return report;
 

 } 
    	 
    	 
    	 public  List<Site> getSiteList(String userid)	 
    	    {
    	    	
    	       List<Site> siteobj = GetMiddlewareData.getSiteData(userid);
    	    
    	       return siteobj;
    	    }
    	    	 	 
    	 
    	 
    	 	 
    public  List<Section> getSectionList(String siteid)	 
    {
    	
    	List<Section> sectionobj = GetMiddlewareData.getSectionData(siteid);
    
       return sectionobj;
    }
    	 
    	
    public  List<Article> getArticleList(String siteid)
    {
    	List<Article> articleobj = GetMiddlewareData.getArticleData(siteid);
    	
    	return articleobj;
    	
    }

    public Set<String> extractChannelNames(long id,String dateRange){
      
    	  String [] dateInterval = dateRange.split(",");

          if( id == 16){
        	try {
        		module.setUp();
				channelNames = module.getChannelList(dateInterval[0], dateInterval[1]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return  channelNames;
           
          }
    
          return channelNames;
    
    
    }



}
