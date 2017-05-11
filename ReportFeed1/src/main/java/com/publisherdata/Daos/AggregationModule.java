package com.publisherdata.Daos;

import com.publisherdata.model.PublisherReport;
import com.publisherdata.model.Site;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLFeatureNotSupportedException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javolution.util.FastMap;

import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequestBuilder;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.AdminClient;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.plugin.nlpcn.QueryActionElasticExecutor;
import org.elasticsearch.plugin.nlpcn.executors.CSVResult;
import org.elasticsearch.plugin.nlpcn.executors.CSVResultsExtractor;
import org.elasticsearch.plugin.nlpcn.executors.CsvExtractorException;
import org.elasticsearch.search.aggregations.Aggregations;
import org.nlpcn.es4sql.SearchDao;
import org.nlpcn.es4sql.exception.SqlParseException;
import org.nlpcn.es4sql.query.QueryAction;
import org.nlpcn.es4sql.query.SqlElasticSearchRequestBuilder;

import util.GetMiddlewareData;

public class AggregationModule
{
  private static TransportClient client;
  private static SearchDao searchDao;
  private static AggregationModule INSTANCE;
  
  public static Map<String,String> citycodeMap;
  public static Map<String,String> citycodeMap2;
  public static Map<String,String> countrymap;
  public static Map<String,List<String>> countrystatemap;
  public static Map<String,List<String>> countrystatecitymap;
  
  static {
      Map<String, String> countryMap1 = new HashMap<String,String>();
      String csvFile = "/root/countrycodes.csv";
      BufferedReader br = null;
      String line = "";
      String cvsSplitBy = ",";
      String countrykey = "";
      Map<String, String> countryMap2  = new HashMap<String,String>();
      try {

          br = new BufferedReader(new FileReader(csvFile));
         
          while ((line = br.readLine()) != null) {

             try{
          	// use comma as separator
              line = line.replace(",,",", , ");
          	//   System.out.println(line);
          	String[] countryDetails = line.split(cvsSplitBy);
              countrykey = countryDetails[0];
              countryMap1.put(countrykey,countryDetails[1]);
            //  hotspotMap1.put(key,hotspotDetails[0]+"@"+hotspotDetails[1]+"@"+hotspotDetails[3]);
            
             }
             catch(Exception e)
             {
          	     
            	 e.printStackTrace(); 
                 continue;
             }

          }


        
      
      }

      
      
      
catch(Exception e){
	
	e.printStackTrace();
} 

      
      countrymap = Collections.unmodifiableMap(countryMap1);  
  
      //    System.out.println(citycodeMap);
  }
  
  
  
  static {
      Map<String, List<String>> countrystateMap1 = new HashMap<String,List<String>>();
      String csvFile1 = "/root/statecodes.csv";
      BufferedReader br1 = null;
      String line1 = "";
      String cvsSplitBy1 = ",";
      String countrykey1 = "";
      Map<String, String> countrystateMap2  = new HashMap<String,String>();
      List<String> list1 = new ArrayList<String>();
      try {

          br1 = new BufferedReader(new FileReader(csvFile1));
         
          while ((line1 = br1.readLine()) != null) {

             try{
          	// use comma as separator
              line1 = line1.replace(",,",", , ");
          	//   System.out.println(line);
           	String[] countrystateDetails = line1.split(cvsSplitBy1);
              countrykey1 = countrystateDetails[0];
              if(countrystateMap1.containsKey(countrykey1)==false){
            	  List<String> list = new ArrayList<String>();
            	  list.add(countrystateDetails[1]+":"+countrystateDetails[2]);
            	  countrystateMap1.put(countrykey1,list);
            //  hotspotMap1.put(key,hotspotDetails[0]+"@"+hotspotDetails[1]+"@"+hotspotDetails[3]);
              }
              else{
            	  list1 = countrystateMap1.get(countrykey1);
            	  list1.add(countrystateDetails[1]+":"+countrystateDetails[2]);
            	  countrystateMap1.put(countrykey1, list1);
            	  
              }
             }
             catch(Exception e)
             {
          	   e.printStackTrace(); 
               continue;
             }

          }


        
      
      }

      
      
      
catch(Exception e){
	
	e.printStackTrace();
} 

      
      countrystatemap = Collections.unmodifiableMap(countrystateMap1);  
  
      //    System.out.println(citycodeMap);
  }
  
  
  
   
  static {
      Map<String, String> cityMap = new HashMap<String,String>();
      String csvFile2 = "/root/citycode1.csv";
      BufferedReader br2 = null;
      String line2 = "";
      String cvsSplitBy2 = ",";
      String key = "";
      Map<String, String> cityMap1  = new HashMap<String,String>();
      Map<String, List<String>> cityMap2  = new HashMap<String,List<String>>();
      List<String> list2 = new ArrayList<String>();
      
      try {

          br2 = new BufferedReader(new FileReader(csvFile2));
         
          while ((line2 = br2.readLine()) != null) {

             try{
          	// use comma as separator
              line2 = line2.replace(",,",", , ");
          	//   System.out.println(line);
          	String[] geoDetails = line2.split(cvsSplitBy2);
              key = geoDetails[6];
              cityMap1.put(key,geoDetails[5]);
            //  hotspotMap1.put(key,hotspotDetails[0]+"@"+hotspotDetails[1]+"@"+hotspotDetails[3]);
              cityMap.put(geoDetails[5],key);
              if(cityMap2.containsKey(geoDetails[0]+":"+geoDetails[1])==false){
            	  List<String> list = new ArrayList<String>();
            	  list.add(geoDetails[2]);
            	  cityMap2.put(geoDetails[0]+":"+geoDetails[1],list);
            //  hotspotMap1.put(key,hotspotDetails[0]+"@"+hotspotDetails[1]+"@"+hotspotDetails[3]);
              }
              else{
            	  list2 = cityMap2.get(geoDetails[0]+":"+geoDetails[1]);
            	  list2.add(geoDetails[2]);
            	  cityMap2.put(geoDetails[0]+":"+geoDetails[1], list2);
            	  
              }             }
             catch(Exception e)
             {
          	   e.printStackTrace(); 
               continue;
             }

          }


        
      
      }

      
      
      
catch(Exception e){
	
	e.printStackTrace();
} 

      
      citycodeMap = Collections.unmodifiableMap(cityMap1);  
      citycodeMap2 = Collections.unmodifiableMap(cityMap);  
      countrystatecitymap = Collections.unmodifiableMap(cityMap2);
      //    System.out.println(citycodeMap);
  }
  
    
  
  
  
  public static Map<String,String> oscodeMap;
  static {
      Map<String, String> osMap = new HashMap<String,String>();
      String csvFile = "/root/oscode2.csv";
      BufferedReader br = null;
      String line = "";
      String cvsSplitBy = ",";
      String key = "";
      Map<String, String> osMap1  = new HashMap<String,String>();
      try {

          br = new BufferedReader(new FileReader(csvFile));
         
          while ((line = br.readLine()) != null) {

             try{
          	// use comma as separator
              line = line.replace(",,",", , ");
          	//   System.out.println(line);
          	String[] osDetails = line.split(cvsSplitBy);
              key = osDetails[0];
              osMap1.put(key,osDetails[1]);
            //  hotspotMap1.put(key,hotspotDetails[0]+"@"+hotspotDetails[1]+"@"+hotspotDetails[3]);
             }
             catch(Exception e)
             {
          	   e.printStackTrace(); 
               continue;
             }

          }


        
      
      }

      
      
      
catch(Exception e){
	
	e.printStackTrace();
} 

      
      oscodeMap = Collections.unmodifiableMap(osMap1);  
      System.out.println(oscodeMap);
  }
   
 
  public static Map<String,String> oscodeMap1;
  static {
      Map<String, String> osMap2 = new HashMap<String,String>();
      String csvFile = "/root/system_os.csv";
      BufferedReader br = null;
      String line = "";
      String cvsSplitBy = ",";
      String key = "";
      Map<String, String> osMap3  = new HashMap<String,String>();
      try {

          br = new BufferedReader(new FileReader(csvFile));
         
          while ((line = br.readLine()) != null) {

             try{
          	// use comma as separator
              line = line.replace(",,",", , ");
          	//   System.out.println(line);
          	String[] osDetails = line.split(cvsSplitBy);
              key = osDetails[2];
              osMap3.put(key,osDetails[0]+","+osDetails[1]);
            //  hotspotMap1.put(key,hotspotDetails[0]+"@"+hotspotDetails[1]+"@"+hotspotDetails[3]);
             }
             catch(Exception e)
             {
          	   e.printStackTrace(); 
               continue;
             }

          }


        
      
      }

      
      
      
catch(Exception e){
	
	e.printStackTrace();
} 

      
      oscodeMap1 = Collections.unmodifiableMap(osMap3);  
      System.out.println(oscodeMap1);
  }
  
  
  
  
  
  public static Map<String,String> devicecodeMap;
  static {
      Map<String, String> deviceMap = new HashMap<String,String>();
      String csvFile = "/root/devicecode2.csv";
      BufferedReader br = null;
      String line = "";
      String cvsSplitBy = ",";
      String key = "";
      Map<String, String> deviceMap1  = new HashMap<String,String>();
      try {

          br = new BufferedReader(new FileReader(csvFile));
         
          while ((line = br.readLine()) != null) {

             try{
          	// use comma as separator
              line = line.replace(",,",", , ");
          	 //  System.out.println(line);
          	String[] deviceDetails = line.split(cvsSplitBy);
              key = deviceDetails[0];
              deviceMap1.put(key,deviceDetails[1]+","+deviceDetails[4]+","+deviceDetails[8]);
            //  hotspotMap1.put(key,hotspotDetails[0]+"@"+hotspotDetails[1]+"@"+hotspotDetails[3]);
             }
             catch(Exception e)
             {
          	   e.printStackTrace(); 
               continue;
             }

          }


        
      
      }

      
      
      
catch(Exception e){
	
	e.printStackTrace();
} 

      
      devicecodeMap = Collections.unmodifiableMap(deviceMap1);  
   //   System.out.println(deviceMap);
  }
  
  
  
  public static Map<String,String> audienceSegmentMap;
  public static Map<String,String> audienceSegmentMap1;
  public static Map<String,String> audienceSegmentMap2;
  
  static {
      Map<String, String> audienceMap = new HashMap<String,String>();
      String csvFile = "/root/subcategorymap1.csv";
      BufferedReader br = null;
      String line = "";
      String cvsSplitBy = ",";
      String key = "";
      Map<String, String> audienceMap1  = new HashMap<String,String>();
      Map<String, String> audienceMap2  = new HashMap<String,String>();
      
      try {

          br = new BufferedReader(new FileReader(csvFile));
         
          while ((line = br.readLine()) != null) {

             try{
          	// use comma as separator
              line = line.replace(",,",", , ");
          	 //  System.out.println(line);
          	String[] segmentDetails = line.split(cvsSplitBy);
              key = segmentDetails[0];
              audienceMap1.put(key,segmentDetails[1]);
              audienceMap.put(segmentDetails[4],key);
              //  hotspotMap1.put(key,hotspotDetails[0]+"@"+hotspotDetails[1]+"@"+hotspotDetails[3]);
              audienceMap2.put(key, segmentDetails[4]);
             }
             catch(Exception e)
             {
          	   e.printStackTrace(); 
               continue;
             }

          }


        
      
      }

      
      
      
catch(Exception e){
	
	e.printStackTrace();
} 

      
      audienceSegmentMap = Collections.unmodifiableMap(audienceMap1);  
      audienceSegmentMap1 = Collections.unmodifiableMap(audienceMap);
      audienceSegmentMap2 = Collections.unmodifiableMap(audienceMap2);
//   System.out.println(deviceMap);
  }
  
  
  
  
  public static String capitalizeString(String string) {
	  char[] chars = string.toLowerCase().toCharArray();
	  boolean found = false;
	  for (int i = 0; i < chars.length; i++) {
	    if (!found && Character.isLetter(chars[i])) {
	      chars[i] = Character.toUpperCase(chars[i]);
	      found = true;
	    } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
	      found = false;
	    }
	  }
	  return String.valueOf(chars);
	}
  
  
  
  
  
  public static String capitalizeFirstLetter(String original) {
	    if (original == null || original.length() == 0) {
	        return original;
	    }
	    return original.substring(0, 1).toUpperCase() + original.substring(1);
	}
  
  
  
  
  public static AggregationModule getInstance()
  {
    if (INSTANCE == null) {
      return new AggregationModule();
    }
    return INSTANCE;
  }
  
  public static void main(String[] args)
    throws Exception
  {
	  
	
	 //setUp();
     AggregationModule mod = new  AggregationModule();
     mod.setUp();
     /*
     mod.countOS("2017-01-01","2017-01-31"); */
     mod.counttotalvisitorsChannelSectionDateHourlywise("2017-01-19","2017-01-19","Womanseraindia_indiagate","http___womansera_com_entertainment");
     mod.counttotalvisitorsChannelSectionDateHourlyMinutewise("2017-01-16 13:00:01","2017-01-16 13:59:59","Womanseraindia_indiagate","http___womansera_com_entertainment");

    /* 
     mod.countNewUsersChannelSectionDatewise("2017-01-01","2017-01-31","Womanseraindia_indiagate","http___womansera_com_entertainment");
     mod.countReturningUsersChannelSectionDatewise("2017-01-01","2017-01-31","Womanseraindia_indiagate","http___womansera_com_entertainment");
     mod.countLoyalUsersChannelSectionDatewise("2017-01-01","2017-01-31","Womanseraindia_indiagate","http___womansera_com_entertainment");
     mod.countNewUsersChannelArticleDatewise("2017-01-01","2017-01-31","Womanseraindia_indiagate","http___womansera_com_trending_moles_on_these_areas_signify_wealth_and_luck_read_to_know");
     mod.countReturningUsersChannelArticleDatewise("2017-01-01","2017-01-31","Womanseraindia_indiagate","http___womansera_com_trending_moles_on_these_areas_signify_wealth_and_luck_read_to_know");
     mod.countLoyalUsersChannelArticleDatewise("2017-01-01","2017-01-31","Womanseraindia_indiagate","http___womansera_com_trending_moles_on_these_areas_signify_wealth_and_luck_read_to_know");
     mod.getGenderChannelSection("2017-01-01","2017-01-31","Womanseraindia_indiagate","http___womansera_com_entertainment");
     mod.gettimeofdayChannelSection("2017-01-01","2017-01-31","Womanseraindia_indiagate","http___womansera_com_entertainment");
     */
     
     /*
     mod.getChannelSectionArticleCount("2017-01-01","2017-01-31","Womanseraindia_indiagate","http___womansera_com_entertainment");
     mod.getChannelSectionArticleList("2017-01-01","2017-01-31","Womanseraindia_indiagate","*");
     mod.getChannelArticleReferrerList("2017-01-01","2017-01-31","Womanseraindia_indiagate","amitabh");
     mod.getChannelArticleReferredPostsList("2017-01-01","2017-01-31","Womanseraindia_indiagate","adult");
     mod.countfingerprintChannelArticle("2017-01-01","2017-01-31","Womanseraindia_indiagate","adult");
     mod.countfingerprintChannelArticleDatewise("2017-01-01","2017-01-31","Womanseraindia_indiagate","adult");
     mod.counttotalvisitorsChannelArticle("2017-01-01","2017-01-31","Womanseraindia_indiagate","adult");
     mod.counttotalvisitorsChannelArticleDatewise("2017-01-01","2017-01-31","Womanseraindia_indiagate","adult");
     mod.countAudiencesegmentChannelArticle("2017-01-01","2017-01-31","Womanseraindia_indiagate", "http___womansera_com_trending_moles_on_these_areas_signify_wealth_and_luck_read_to_know");
  */   
     
  /*   
     mod.getChannelSectionArticleCount("2017-01-01","2017-01-31","Womanseraindia_indiagate","http___womansera_com_entertainment");
     mod.getChannelSectionArticleList("2017-01-01","2017-01-31","Womanseraindia_indiagate","http___womansera_com_entertainment");
     mod.getChannelSectionReferrerList("2017-01-01","2017-01-31","Womanseraindia_indiagate","entertainment");
     mod.getChannelSectionReferredPostsList("2017-01-01","2017-01-31","Womanseraindia_indiagate","http___womansera_com_entertainment");
     mod.countfingerprintChannelSection("2017-01-01","2017-01-31","Womanseraindia_indiagate","http___womansera_com_entertainment");
     mod.countfingerprintChannelSectionDatewise("2017-01-01","2017-01-31","Womanseraindia_indiagate","http___womansera_com_entertainment");
     mod.counttotalvisitorsChannelSection("2017-01-01","2017-01-31","Womanseraindia_indiagate","http___womansera_com_entertainment");
     mod.counttotalvisitorsChannelSectionDatewise("2017-01-01","2017-01-31","Womanseraindia_indiagate","http___womansera_com_entertainment");
 */
 
 //    mod.countAudiencesegmentChannelSection("2017-01-01","2017-01-31","Womanseraindia_indiagate", "http___womansera_com_trending_moles_on_these_areas_signify_wealth_and_luck_read_to_know");
     /*
    
     mod.countOSChannelArticle("2017-01-01","2017-01-31","Womanseraindia_indiagate","http://womansera.com/trending/amitabh-bachchan-dead-pictures-going-viral");
     
     mod.countCityChannelArticle("2017-01-01","2017-01-31","Womanseraindia_indiagate","http://womansera.com/trending/amitabh-bachchan-dead-pictures-going-viral");
     
     mod.countModelChannelArticle("2017-01-01","2017-01-31","Womanseraindia_indiagate","http://womansera.com/trending/amitabh-bachchan-dead-pictures-going-viral");
    
 mod.countOSChannelArticle("2017-01-01","2017-01-31","Womanseraindia_indiagate","http://womansera.com/wedding/video-shahid-miras-dance-sajh-dajh-ke-sangeet-ceremony");
     
     mod.countCityChannelArticle("2017-01-01","2017-01-31","Womanseraindia_indiagate","http://womansera.com/wedding/video-shahid-miras-dance-sajh-dajh-ke-sangeet-ceremony");
     
     mod.countModelChannelArticle("2017-01-01","2017-01-31","Womanseraindia_indiagate","http://womansera.com/wedding/video-shahid-miras-dance-sajh-dajh-ke-sangeet-ceremony");
     
     mod.counttotalvisitorsChannelArticle("2017-01-01","2017-01-31","Womanseraindia_indiagate","http://womansera.com/wedding/video-shahid-miras-dance-sajh-dajh-ke-sangeet-ceremony");
     mod.counttotalvisitorsChannelArticleDatewise("2017-01-01","2017-01-31","Womanseraindia_indiagate","http://womansera.com/wedding/video-shahid-miras-dance-sajh-dajh-ke-sangeet-ceremony");
     
     mod.countfingerprintChannelArticle("2017-01-01","2017-01-31","Womanseraindia_indiagate","http://womansera.com/wedding/video-shahid-miras-dance-sajh-dajh-ke-sangeet-ceremony");
     mod.countfingerprintChannelArticleDatewise("2017-01-01","2017-01-31","Womanseraindia_indiagate","http://womansera.com/wedding/video-shahid-miras-dance-sajh-dajh-ke-sangeet-ceremony");
     
     mod.countLoyalUsersChannelArticleDatewise("2017-01-01","2017-01-31","Womanseraindia_indiagate","http://womansera.com/wedding/video-shahid-miras-dance-sajh-dajh-ke-sangeet-ceremony");
     mod.countReturningUsersChannelArticleDatewise("2017-01-01","2017-01-31","Womanseraindia_indiagate","http://womansera.com/wedding/video-shahid-miras-dance-sajh-dajh-ke-sangeet-ceremony");
     mod.countNewUsersChannelArticleDatewise("2017-01-01","2017-01-31","Womanseraindia_indiagate","http://womansera.com/wedding/video-shahid-miras-dance-sajh-dajh-ke-sangeet-ceremony");
    
     mod.getAgegroupChannelArticle("2017-01-01","2017-01-31","Womanseraindia_indiagate","http://womansera.com/wedding/video-shahid-miras-dance-sajh-dajh-ke-sangeet-ceremony");
     mod.getGenderChannelArticle("2017-01-01","2017-01-31","Womanseraindia_indiagate","http://womansera.com/wedding/video-shahid-miras-dance-sajh-dajh-ke-sangeet-ceremony");
    */ 
//     mod.countAudiencesegmentChannelArticle("2017-01-01","2017-01-31","Womanseraindia_indiagate","http://womansera.com/wedding/video-shahid-miras-dance-sajh-dajh-ke-sangeet-ceremony");
    
     Map<String,String>filter = new HashMap<String,String>();
     filter.put("city","delhi,mumbai");
     filter.put("agegroup","35_44");
//     filter.put("incomelevel", "medium");
  //   filter.put("devicetype","tablet");
    // mod.getGenderChannelFilter("2017-01-01","2017-01-31","Womanseraindia_indiagate", filter);
    
     
     List<String> groupby = new ArrayList<String>();
     groupby.add("city");
     groupby.add("agegroup");
   //  groupby.add("incomelevel");
   //  mod.getGenderChannelGroupBy("2017-01-01","2017-01-31","Womanseraindia_indiagate", groupby);
     /*	 
	 final long startTime1 = System.currentTimeMillis();
	 AggregationModule mod = new AggregationModule();
	 mod.countAudienceSegment("2016-08-20","2016-12-02");
	 mod.countAudienceSegment("2016-08-20","2016-12-02");  
	 mod.countAudienceSegment("2016-08-20","2016-12-02");
	 mod.countAudienceSegment("2016-08-20","2016-12-02");  
	 mod.countAudienceSegment("2016-08-20","2016-12-02");
	 mod.countAudienceSegment("2016-08-20","2016-12-02");  
	 mod.countAudienceSegment("2016-08-20","2016-12-02");
	 final long endTime1 = System.currentTimeMillis();
	 
	 
	 System.out.println("Total code execution time: " + (endTime1 - startTime1) );
		
	  */
	  //  countfingerprintChannel("2016-08-20","2016-12-02", "Mumbai_T1_airport");
	  
	//  countAudiencesegmentChannel("2016-08-20","2016-12-02", "Mumbai_T1_airport");
	
	  
	//  Integer countv1 = 500000;
	  
	  /*
	  
	  if(countv1 >= 500000)
	    {
	    double total_length = countv1 - 0;
	    double subrange_length = total_length/30;	
	    
	    double current_start = 0;
	    for (int i = 0; i < 20; ++i) {
	      System.out.println("Smaller range: [" + current_start + ", " + (current_start + subrange_length) + "]");
	      current_start += subrange_length;
	    }
	   }
  */
	  
	  /*
	  
	    Double countv1 = Double.parseDouble("90000");
	    
	    Double n = 0.0;
	    if(countv1 >= 250000)
	       n=50.0;
	    
	    if(countv1 >= 100000 && countv1 <= 250000 )
	       n=20.0;
	    
	    if(countv1 < 100000)
           n=50.0;	    
	   
	    Double total_length = countv1 - 0;
	    Double subrange_length = total_length/n;	
	    String startdate= "Startdate";
	    String enddate= "endDate";
	    Double current_start = 0.0;
	    for (int i = 0; i < n; ++i) {
	      System.out.println("Smaller range: [" + current_start + ", " + (current_start + subrange_length) + "]");
	      Double startlimit = current_start;
	      Double finallimit = current_start + subrange_length;
	      Double index = startlimit +1;
	      String query = "SELECT DISTINCT(cookie_id) FROM enhanceduserdatabeta1 where date between " + "'" + startdate + "'" + " and " + "'" + enddate +"' limit "+index.intValue()+","+finallimit.intValue();  	
		  System.out.println(query);
	  //    Query.add(query);
	      current_start += subrange_length;
	    //  Query.add(query);
	     
	    }
	   */ 
	    
  
  }
  
  public void setUp()
    throws Exception
  {
    if (client == null)
    {
      client = new TransportClient();
      client.addTransportAddress(getTransportAddress());
      
      NodesInfoResponse nodeInfos = (NodesInfoResponse)client.admin().cluster().prepareNodesInfo(new String[0]).get();
      String clusterName = nodeInfos.getClusterName().value();
      //System.out.println(String.format("Found cluster... cluster name: %s", new Object[] { clusterName }));
      
      searchDao = new SearchDao(client);
    }
    //System.out.println("Finished the setup process...");
  }
  
  public static SearchDao getSearchDao()
  {
    return searchDao;
  }
  
  
  
  public List<String> getcountryNames()

	{

		List<String> CountryNames = new ArrayList<String>();
		for (Map.Entry<String, String> entry : countrymap.entrySet()) {
			CountryNames.add(entry.getKey() + "," + entry.getValue());
		}

		System.out.println(CountryNames);
		return CountryNames;

	}

	public List<String> getcountryStateNames(String countrycode)

	{

		List<String> stateNames = new ArrayList<String>();
		stateNames = countrystatemap.get(countrycode);
		System.out.println(stateNames);
		return stateNames;

	}

	public List<String> getcountryCityNames(String countrycode, String statecode) {

		List<String> cityNames = new ArrayList<String>();
		cityNames = countrystatecitymap.get(countrycode.toLowerCase() + ":" + statecode.toLowerCase());
		System.out.println(cityNames);

		return cityNames;

	}


  
	public static int getDifferenceDays(Date d1, Date d2) {
		int daysdiff=0;
		long diff = d2.getTime() - d1.getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000)+1;
		 daysdiff = (int) diffDays;
		return daysdiff;
		 }
  
	
	public static List<Long> getDaysBetweenDates(Date startdate, Date enddate)
	{
	    List<Long> dates = new ArrayList<Long>();
	    Calendar calendar = new GregorianCalendar();
	    calendar.setTime(startdate);

	    while (calendar.getTime().before(enddate))
	    {
	        Date result = calendar.getTime();
	        Long date  = result.getTime() / 1000;
	        dates.add(date);
	        calendar.add(Calendar.DATE, 1);
	    }
	    return dates;
	}
  
  
  public List<PublisherReport> countBrandName(String startdate, String enddate)
    throws CsvExtractorException, Exception
  {
    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
    String query = String.format("SELECT COUNT(*)as count,brandName FROM enhanceduserdatabeta1 where date between '" + startdate + "'" + " and " + "'" + enddate + "'" + " group by brandName", new Object[] { "enhanceduserdatabeta1" });
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if (lines.size() > 0) {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        if(data[0].trim().toLowerCase().contains("logitech")==false && data[0].trim().toLowerCase().contains("mozilla")==false && data[0].trim().toLowerCase().contains("web_browser")==false && data[0].trim().toLowerCase().contains("microsoft")==false && data[0].trim().toLowerCase().contains("opera")==false && data[0].trim().toLowerCase().contains("epiphany")==false){ 
        obj.setBrandname(data[0]);
        obj.setCount(data[1]);
        pubreport.add(obj);
       }
      }
    }
    //System.out.println(headers);
    //System.out.println(lines);
    
    return pubreport;
  }
  
  public List<PublisherReport> countBrowser(String startdate, String enddate)
    throws CsvExtractorException, Exception
  {
    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
    String query = String.format("SELECT COUNT(*)as count,browser_name FROM enhanceduserdatabeta1 where date between '" + startdate + "'" + " and " + "'" + enddate + "'" + " group by browser_name", new Object[] { "enhanceduserdatabeta1" });
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
       
        obj.setBrowser(data[0]);
        obj.setCount(data[1]);
        pubreport.add(obj);
      }
    }
    //System.out.println(headers);
    //System.out.println(lines);
    
    return pubreport;
  }
  
  public List<PublisherReport> countOS(String startdate, String enddate)
    throws CsvExtractorException, Exception
  {
    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
    String query = String.format("SELECT COUNT(*)as count,system_os FROM enhanceduserdatabeta1 where date between '" + startdate + "'" + " and " + "'" + enddate + "'" + " group by system_os", new Object[] { "enhanceduserdatabeta1" });
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    System.out.println(headers);
    System.out.println(lines);
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        obj.setOs(data[0]);
        obj.setCount(data[1]);
        pubreport.add(obj);
      }
    }
    return pubreport;
  }
  
  public List<PublisherReport> countModel(String startdate, String enddate)
    throws CsvExtractorException, Exception
  {
    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
    String query = String.format("SELECT COUNT(*)as count,modelName FROM enhanceduserdatabeta1 where date between '" + startdate + "'" + " and " + "'" + enddate + "'" + " group by modelName", new Object[] { "enhanceduserdatabeta1" });
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        if(data[0].trim().toLowerCase().contains("logitech_revue")==false && data[0].trim().toLowerCase().contains("mozilla_firefox")==false && data[0].trim().toLowerCase().contains("apple_safari")==false && data[0].trim().toLowerCase().contains("generic_web")==false && data[0].trim().toLowerCase().contains("google_compute")==false && data[0].trim().toLowerCase().contains("microsoft_xbox")==false && data[0].trim().toLowerCase().contains("google_chromecast")==false && data[0].trim().toLowerCase().contains("opera")==false && data[0].trim().toLowerCase().contains("epiphany")==false && data[0].trim().toLowerCase().contains("laptop")==false){    
        obj.setMobile_device_model_name(data[0]);
        obj.setCount(data[1]);
        pubreport.add(obj);
        }
        
        }
    }
    return pubreport;
  }
  
  public List<PublisherReport> countCity(String startdate, String enddate)
    throws CsvExtractorException, Exception
  {
    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
    String query = String.format("SELECT COUNT(*)as count,city FROM enhanceduserdatabeta1 where date between '" + startdate + "'" + " and " + "'" + enddate + "'" + " group by city", new Object[] { "enhanceduserdatabeta1" });
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        obj.setCity(data[0]);
        obj.setCount(data[1]);
        pubreport.add(obj);
      }
    }
    return pubreport;
  }
  
  public List<PublisherReport> countPinCode(String startdate, String enddate)
    throws CsvExtractorException, Exception
  {
    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
    String query = String.format("SELECT COUNT(*)as count,postalcode FROM enhanceduserdatabeta1 where date between '" + startdate + "'" + " and " + "'" + enddate + "'" + " group by postalcode", new Object[] { "enhanceduserdatabeta1" });
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    //System.out.println(headers);
    //System.out.println(lines);
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        obj.setPostalcode(data[0]);
        obj.setCount(data[1]);
        pubreport.add(obj);
      }
    }
    return pubreport;
  }
  
  public List<PublisherReport> countLatLong(String startdate, String enddate)
    throws CsvExtractorException, Exception
  {
    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
    String query = String.format("SELECT COUNT(*)as count,latitude_longitude FROM enhanceduserdatabeta1 where date between '" + startdate + "'" + " and " + "'" + enddate + "'" + " group by latitude_longitude", new Object[] { "enhanceduserdatabeta1" });
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    //System.out.println(headers);
    //System.out.println(lines);
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        String[] dashcount = data[0].split("_");
        if ((dashcount.length == 3) && (data[0].charAt(data[0].length() - 1) != '_') && 
          (!dashcount[2].isEmpty()))
        {
          obj.setLatitude_longitude(data[0]);
          obj.setCount(data[1]);
          pubreport.add(obj);
        }
      }
    }
    return pubreport;
  }
  
  public List<PublisherReport> countfingerprint(String startdate, String enddate)
    throws CsvExtractorException, Exception
  {
    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
    String query = String.format("SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where date between '" + 
      startdate + "'" + " and " + "'" + enddate + "'" + " group by date", new Object[] {"enhanceduserdatabeta1" });
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        obj.setDate(data[0]);
        obj.setReach(data[1]);
        pubreport.add(obj);
      }
    }
    return pubreport;
  }
  
  public List<PublisherReport> countAudienceSegment(String startdate, String enddate)
    throws CsvExtractorException, Exception
  {
	
	  PrintStream out = new PrintStream(new FileOutputStream(
				"audiencesegmentcount.txt"));
		System.setOut(out);
	  
	  List<PublisherReport> pubreport = new ArrayList(); 
	  
	  String querya1 = "Select Count(DISTINCT(cookie_id)) FROM enhanceduserdata where date between " + "'" + startdate + "'" + " and " + "'" + enddate +"' limit 20000000";  
	  
	    //Divide count in different limits 
	
	  
	  List<String> Query = new ArrayList();
	  


	    System.out.println(querya1);
	    
	    final long startTime2 = System.currentTimeMillis();
		
	    
	    CSVResult csvResult1 = null;
		try {
			csvResult1 = AggregationModule.getCsvResult(false, querya1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    final long endTime2 = System.currentTimeMillis();
		
	    List<String> headers = csvResult1.getHeaders();
	    List<String> lines = csvResult1.getLines();
	    
	    
	    String count = lines.get(0);
	    Double countv1 = Double.parseDouble(count);
	    Double n = 0.0;
	    if(countv1 >= 250000)
	       n=10.0;
	    
	    if(countv1 >= 100000 && countv1 <= 250000 )
	       n=10.0;
	    
	    if(countv1 < 100000)
           n=10.0;	    
	   
	    
	    if(countv1 <= 100)
	    	n=1.0;
	    
	    if(countv1 == 0)
	    {
	    	
	    	return pubreport;
	    	
	    }
	    
	    Double total_length = countv1 - 0;
	    Double subrange_length = total_length/n;	
	    
	    Double current_start = 0.0;
	    for (int i = 0; i < n; ++i) {
	      System.out.println("Smaller range: [" + current_start + ", " + (current_start + subrange_length) + "]");
	      Double startlimit = current_start;
	      Double finallimit = current_start + subrange_length;
	      Double index = startlimit +1;
	      if(countv1 == 1)
	    	  index=0.0;
	      String query = "SELECT DISTINCT(cookie_id) FROM enhanceduserdata where date between " + "'" + startdate + "'" + " and " + "'" + enddate +"' Order by cookie_id limit "+index.intValue()+","+finallimit.intValue();  	
		  System.out.println(query);
	  //    Query.add(query);
	      current_start += subrange_length;
	      Query.add(query);
	     
	    }
	    
	    
	    	
	    
	  
	  ExecutorService executorService = Executors.newFixedThreadPool(2000);
        
       List<Callable<FastMap<String,Double>>> lst = new ArrayList<Callable<FastMap<String,Double>>>();
    
       for(int i=0 ; i < Query.size(); i++ ){
       lst.add(new AudienceSegmentQueryExecutionThreads(Query.get(i),client,searchDao));
    /*   lst.add(new AudienceSegmentQueryExecutionThreads(query1,client,searchDao));
       lst.add(new AudienceSegmentQueryExecutionThreads(query2,client,searchDao));
       lst.add(new AudienceSegmentQueryExecutionThreads(query3,client,searchDao));
       lst.add(new AudienceSegmentQueryExecutionThreads(query4,client,searchDao));*/
        
       // returns a list of Futures holding their status and results when all complete
       lst.add(new SubcategoryQueryExecutionThreads(Query.get(i),client,searchDao));
   /*    lst.add(new SubcategoryQueryExecutionThreads(query6,client,searchDao));
       lst.add(new SubcategoryQueryExecutionThreads(query7,client,searchDao));
       lst.add(new SubcategoryQueryExecutionThreads(query8,client,searchDao));
       lst.add(new SubcategoryQueryExecutionThreads(query9,client,searchDao)); */
       }
       
       
       List<Future<FastMap<String,Double>>> maps = executorService.invokeAll(lst);
        
       System.out.println(maps.size() +" Responses recieved.\n");
        
       for(Future<FastMap<String,Double>> task : maps)
       {
    	   try{
           if(task!=null)
    	   System.out.println(task.get().toString());
    	   }
    	   catch(Exception e)
    	   {
    		   e.printStackTrace();
    		   continue;
    	   }
    	    
    	   
    	   }
        
       /* shutdown your thread pool, else your application will keep running */
       executorService.shutdown();
	  
	
	  //  //System.out.println(headers1);
	 //   //System.out.println(lines1);
	    
	    
       
       FastMap<String,Double> audiencemap = new FastMap<String,Double>();
       
       FastMap<String,Double> subcatmap = new FastMap<String,Double>();
       
       Double count1 = 0.0;
       
       Double count2 = 0.0;
       
       String key ="";
       String key1 = "";
       Double value = 0.0;
       Double vlaue1 = 0.0;
       
	    for (int i = 0; i < maps.size(); i++)
	    {
	    
	    	if(maps!=null && maps.get(i)!=null){
	        FastMap<String,Double> map = (FastMap<String, Double>) maps.get(i).get();
	    	
	       if(map.size() > 0){
	       
	       if(map.containsKey("audience_segment")==true){
	       for (Map.Entry<String, Double> entry : map.entrySet())
	    	 {
	    	  key = entry.getKey();
	    	  key = key.trim();
	    	  value=  entry.getValue();
	    	if(key.equals("audience_segment")==false) { 
	    	if(audiencemap.containsKey(key)==false)
	    	audiencemap.put(key,value);
	    	else
	    	{
	         count1 = audiencemap.get(key);
	         if(count1!=null)
	         audiencemap.put(key,count1+value);	
	    	}
	      }
	    }
	  }   

	       if(map.containsKey("subcategory")==true){
	       for (Map.Entry<String, Double> entry : map.entrySet())
	    	 {
	    	   key = entry.getKey();
	    	   key = key.trim();
	    	   value=  entry.getValue();
	    	if(key.equals("subcategory")==false) {    
	    	if(subcatmap.containsKey(key)==false)
	    	subcatmap.put(key,value);
	    	else
	    	{
	         count1 = subcatmap.get(key);
	         if(count1!=null)
	         subcatmap.put(key,count1+value);	
	    	}
	    }  
	    	
	   }
	      
	     	       }
	           
	       } 
	    
	    	} 	
	   }    
	    
	    String subcategory = null;
	   
	    if(audiencemap.size()>0){
	   
	    	for (Map.Entry<String, Double> entry : audiencemap.entrySet()) {
	    	//System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
	    

	        PublisherReport obj = new PublisherReport();
	        
	   //     String[] data = ((String)lines.get(i)).split(",");
	        
	     //   if(data[0].trim().toLowerCase().contains("festivals"))
	      //  obj.setAudience_segment("");
	      //  else
	        obj.setAudience_segment( entry.getKey());	
	        obj.setCount(String.valueOf(entry.getValue()));
	      
	        if ((!entry.getKey().equals("tech")) && (!entry.getKey().equals("india")) && (!entry.getKey().trim().toLowerCase().equals("foodbeverage")) )
	        {
	         for (Map.Entry<String, Double> entry1 : subcatmap.entrySet()) {
	        	 
	        	    
	        	 
	        	 PublisherReport obj1 = new PublisherReport();
	            
	           
	            if (entry1.getKey().contains(entry.getKey()))
	            {
	              String substring = "_" + entry.getKey() + "_";
	              subcategory = entry1.getKey().replace(substring, "");
	           //   if(data[0].trim().toLowerCase().contains("festivals"))
	           //   obj1.setAudience_segment("");
	           //   else
	        
	              //System.out.println(" \n\n\n Key : " + subcategory + " Value : " + entry1.getValue());  
	              obj1.setAudience_segment(subcategory);
	              obj1.setCount(String.valueOf(entry1.getValue()));
	              obj.getAudience_segment_data().add(obj1);
	            }
	          }
	          pubreport.add(obj);
	        }
	      
	    }
	    }
	    return pubreport;
 
   
  }
  
  public List<PublisherReport> countISP(String startdate, String enddate)
    throws CsvExtractorException, Exception
  {
    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
    String query = String.format("SELECT COUNT(*)as count,ISP FROM enhanceduserdatabeta1 where date between '" + startdate + "'" + " and " + "'" + enddate + "'" + " group by ISP", new Object[] { "enhanceduserdatabeta1" });
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
    {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        if(data[0].trim().toLowerCase().equals("_ltd")==false){ 
        obj.setISP(data[0]);
        obj.setCount(data[1]);
        pubreport.add(obj);
       }
      }
      //System.out.println(headers);
      //System.out.println(lines);
    }
    return pubreport;
  }
  
  public List<PublisherReport> countOrg(String startdate, String enddate)
    throws CsvExtractorException, Exception
  {
    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
    String query = String.format("SELECT organisation FROM enhanceduserdatabeta1 where date between '" + startdate + "'" + " and " + "'" + enddate + "'" + " and organisation NOT IN (Select DISTINCT(ISP) FROM enhanceduserdatabeta1)", new Object[] { "enhanceduserdatabeta1" });
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
    {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        obj.setOrganisation(data[0]);
        obj.setCount(data[1]);
        pubreport.add(obj);
      }
      //System.out.println(headers);
      //System.out.println(lines);
    }
    return pubreport;
  }
  
  public Set<String> getChannelList(String startdate, String enddate)
    throws CsvExtractorException, Exception
  {
    String query = String.format("SELECT channel_name FROM enhanceduserdatabeta1 where date between '" + startdate + "'" + " and " + "'" + enddate + "'" + " Group by channel_name", new Object[] { "enhanceduserdatabeta1" });
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<String> finallines = new ArrayList();
    Set<String> data = new HashSet();
    data.addAll(lines);
    
    //System.out.println(headers);
    //System.out.println(lines);
    
    return data;
  }
  
  public List<PublisherReport> gettimeofdayQuarter(String startdate, String enddate)
    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
  {
    String query = "Select count(*) from enhanceduserdatabeta1 WHERE date between '" + startdate + "'" + " and " + "'" + enddate + "' GROUP BY HOUR(request_time)";
    
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
    {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        obj.setTime_of_day(data[0]);
        obj.setCount(data[1]);
        pubreport.add(obj);
      }
      //System.out.println(headers);
      //System.out.println(lines);
    }
    return pubreport;
  }
  
  public List<PublisherReport> gettimeofdayDaily(String startdate, String enddate)
    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
  {
    String query = "Select count(*) from enhanceduserdatabeta1 WHERE date between '" + startdate + "'" + " and " + "'" + enddate + "' GROUP BY date_histogram(field='request_time','interval'='1d')";
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
    {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        obj.setTime_of_day(data[0]);
        obj.setCount(data[1]);
        pubreport.add(obj);
      }
      //System.out.println(headers);
      //System.out.println(lines);
    }
    return pubreport;
  }
  
  public List<PublisherReport> gettimeofday(String startdate, String enddate)
    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
  {
    String query = "Select count(*) from enhanceduserdatabeta1 WHERE date between '" + startdate + "'" + " and " + "'" + enddate + "' GROUP BY date_histogram(field='request_time','interval'='1h')";
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
    {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        obj.setTime_of_day(data[0]);
        obj.setCount(data[1]);
        pubreport.add(obj);
      }
      //System.out.println(headers);
      //System.out.println(lines);
    }
    return pubreport;
  }
  
  public List<PublisherReport> countGender(String startdate, String enddate)
    throws CsvExtractorException, Exception
  {
    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
    String query = String.format("SELECT COUNT(*)as count,gender FROM enhanceduserdatabeta1 where date between '" + startdate + "'" + " and " + "'" + enddate + "'" + " group by gender", new Object[] { "enhanceduserdatabeta1" });
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    
    //System.out.println(headers);
    //System.out.println(lines);
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
    {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        obj.setGender(data[0]);
        obj.setCount(data[1]);
        pubreport.add(obj);
      }
      //System.out.println(headers);
      //System.out.println(lines);
    }
    return pubreport;
  }
  
  public List<PublisherReport> countAgegroup(String startdate, String enddate)
    throws CsvExtractorException, Exception
  {
    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
    String query = String.format("SELECT COUNT(*)as count,agegroup FROM enhanceduserdatabeta1 where date between '" + startdate + "'" + " and " + "'" + enddate + "'" + " group by agegroup", new Object[] { "enhanceduserdatabeta1" });
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    
    //System.out.println(headers);
    //System.out.println(lines);
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
    {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        obj.setAge(data[0]);
        obj.setCount(data[1]);
        pubreport.add(obj);
      }
      //System.out.println(headers);
      //System.out.println(lines);
    }
    return pubreport;
  }
  
  public List<PublisherReport> getOrg(String startdate, String enddate)
    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
  {
    String query1 = "Select count(*),organisation from enhanceduserdatabeta1 where date between '" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY organisation";
    CSVResult csvResult1 = getCsvResult(false, query1);
    List<String> headers1 = csvResult1.getHeaders();
    List<String> lines1 = csvResult1.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
    {
      for (int i = 0; i < lines1.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data1 = ((String)lines1.get(i)).split(",");
        if ((data1[0].length() > 3) && (data1[0].charAt(0) != '_') && (!data1[0].contains("broadband")) && (!data1[0].contains("communication")) && (!data1[0].contains("cable")) && (!data1[0].contains("telecom")) && (!data1[0].contains("network")) && (!data1[0].contains("isp")) && (!data1[0].contains("hathway")) && (!data1[0].contains("internet")) && (!data1[0].contains("Sify")) && (!data1[0].toLowerCase().equals("_ltd")) && (!data1[0].equals("Googlebot")) && (!data1[0].equals("Bsnl")))
        {
          obj.setOrganisation(data1[0]);
          obj.setCount(data1[1]);
          
          pubreport.add(obj);
        }
      }
      //System.out.println(headers1);
      //System.out.println(lines1);
    }
    return pubreport;
  }
  
  public List<PublisherReport> getdayQuarterdata(String startdate, String enddate)
    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
  {
    String query = "Select count(*),QuarterValue from enhanceduserdatabeta1 where date between '" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY QuarterValue";
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
    {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        if (data[0].equals("quarter1")) {
          data[0] = "quarter1 (00 - 04 AM)";
        }
        if (data[0].equals("quarter2")) {
          data[0] = "quarter2 (04 - 08 AM)";
        }
        if (data[0].equals("quarter3")) {
          data[0] = "quarter3 (08 - 12 AM)";
        }
        if (data[0].equals("quarter4")) {
          data[0] = "quarter4 (12 - 16 PM)";
        }
        if (data[0].equals("quarter5")) {
          data[0] = "quarter5 (16 - 20 PM)";
        }
        if (data[0].equals("quarter6")) {
          data[0] = "quarter6 (20 - 24 PM)";
        }
        obj.setTime_of_day(data[0]);
        obj.setCount(data[1]);
        
        pubreport.add(obj);
      }
      //System.out.println(headers);
      //System.out.println(lines);
    }
    return pubreport;
  }
  
  public List<PublisherReport> countBrandNameChannel(String startdate, String enddate, String channel_name)
    throws CsvExtractorException, Exception
  {
    String query = "SELECT COUNT(*)as count,brandName FROM enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by brandName";
    //System.out.println(query);
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
    {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        if(data[0].trim().toLowerCase().contains("logitech")==false && data[0].trim().toLowerCase().contains("mozilla")==false && data[0].trim().toLowerCase().contains("web_browser")==false && data[0].trim().toLowerCase().contains("microsoft")==false && data[0].trim().toLowerCase().contains("opera")==false && data[0].trim().toLowerCase().contains("epiphany")==false){ 
        obj.setBrandname(data[0]);
        obj.setCount(data[1]);
        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
        pubreport.add(obj);
        } 
       }
  //    //System.out.println(headers);
  //    //System.out.println(lines);
    }
    return pubreport;
  }
  
  public List<PublisherReport> countBrowserChannel(String startdate, String enddate, String channel_name)
    throws CsvExtractorException, Exception
  {
    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
    String query = "SELECT COUNT(*)as count,browser_name FROM enhanceduserdatabeta1 where channel_name ='" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by browser_name";
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
    {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        obj.setBrowser(data[0]);
        obj.setCount(data[1]);
        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
        pubreport.add(obj);
      }
      //System.out.println(headers);
      //System.out.println(lines);
    }
    return pubreport;
  }
  
  public List<PublisherReport> countOSChannel(String startdate, String enddate, String channel_name)
    throws CsvExtractorException, Exception
  {
    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
    String query = String.format("SELECT COUNT(*)as count,system_os FROM enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by system_os", new Object[] { "enhanceduserdatabeta1" });
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
  //  //System.out.println(headers);
  //  //System.out.println(lines);
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        obj.setOs(data[0]);
        obj.setCount(data[1]);
        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
        pubreport.add(obj);
      }
    }
    return pubreport;
  }
  
  public List<PublisherReport> countModelChannel(String startdate, String enddate, String channel_name)
    throws CsvExtractorException, Exception
  {
    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
    String query = String.format("SELECT COUNT(*)as count,modelName FROM enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by modelName", new Object[] { "enhanceduserdatabeta1" });
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");

        if(data[0].trim().toLowerCase().contains("logitech_revue")==false && data[0].trim().toLowerCase().contains("mozilla_firefox")==false && data[0].trim().toLowerCase().contains("apple_safari")==false && data[0].trim().toLowerCase().contains("generic_web")==false && data[0].trim().toLowerCase().contains("google_compute")==false && data[0].trim().toLowerCase().contains("microsoft_xbox")==false && data[0].trim().toLowerCase().contains("google_chromecast")==false && data[0].trim().toLowerCase().contains("opera")==false && data[0].trim().toLowerCase().contains("epiphany")==false && data[0].trim().toLowerCase().contains("laptop")==false){    
        obj.setMobile_device_model_name(data[0]);
        obj.setCount(data[1]);
        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
        pubreport.add(obj);
      }
        
        }
    }
    return pubreport;
  }
  
  public List<PublisherReport> countCityChannel(String startdate, String enddate, String channel_name)
    throws CsvExtractorException, Exception
  {
    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
    String query = String.format("SELECT COUNT(*)as count,city FROM enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by city", new Object[] { "enhanceduserdatabeta1" });
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        obj.setCity(data[0]);
        obj.setCount(data[1]);
        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
        pubreport.add(obj);
      }
    }
    return pubreport;
  }
  
  public List<PublisherReport> countfingerprintChannel(String startdate, String enddate, String channel_name)
    throws CsvExtractorException, Exception
  {
	  
	  
	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
	  
    
	  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
		      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
	  
		 CSVResult csvResult00 = getCsvResult(false, query00);
		 List<String> headers00 = csvResult00.getHeaders();
		 List<String> lines00 = csvResult00.getLines();
		 List<PublisherReport> pubreport00 = new ArrayList();  
			  
		//  //System.out.println(headers00);
		//  //System.out.println(lines00);  
		  
		  for (int i = 0; i < lines00.size(); i++)
	      {
	       
	        String[] data = ((String)lines00.get(i)).split(",");
	  //      //System.out.println(data[0]);
	      }
		  
		  
		  
		Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
	    String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where channel_name = '" + 
	      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
	    CSVResult csvResult = getCsvResult(false, query);
	    List<String> headers = csvResult.getHeaders();
	    List<String> lines = csvResult.getLines();
	    List<PublisherReport> pubreport = new ArrayList();
	    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
	      for (int i = 0; i < lines.size(); i++)
	      {
	        PublisherReport obj = new PublisherReport();
	        
	        String[] data = ((String)lines.get(i)).split(",");
	        obj.setDate(data[0]);
	        obj.setReach(data[1]);
	        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
	        pubreport.add(obj);
	      }
	    }
	    
    return pubreport;
  }
  
  public List<PublisherReport> countAudiencesegmentChannel(String startdate, String enddate, String channel_name)
    throws CsvExtractorException, Exception
  {
      List<PublisherReport> pubreport = new ArrayList(); 
	  
	  String querya1 = "SELECT COUNT(DISTINCT(cookie_id)) FROM enhanceduserdata where channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate +"' limit 20000000";   
	  
	    //Divide count in different limits 
	
	  
	  List<String> Query = new ArrayList();
	  


	    System.out.println(querya1);
	    
	    final long startTime2 = System.currentTimeMillis();
		
	    
	    CSVResult csvResult1 = null;
		try {
			csvResult1 = AggregationModule.getCsvResult(false, querya1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    final long endTime2 = System.currentTimeMillis();
		
	    List<String> headers = csvResult1.getHeaders();
	    List<String> lines = csvResult1.getLines();
	    
	    
	    String count = lines.get(0);
	    Double countv1 = Double.parseDouble(count);
	    Double n = 0.0;
	    if(countv1 >= 250000)
	       n=10.0;
	    
	    if(countv1 >= 100000 && countv1 <= 250000 )
	       n=10.0;
	    
	    if(countv1 <= 100000 && countv1 > 100)
           n=10.0;	    
	   
	    if(countv1 <= 100)
	    	n=1.0;
	    
	    if(countv1 == 0)
	    {
	    	
	    	return pubreport;
	    	
	    }
	    
	    Double total_length = countv1 - 0;
	    Double subrange_length = total_length/n;	
	    
	    Double current_start = 0.0;
	    for (int i = 0; i < n; ++i) {
	      System.out.println("Smaller range: [" + current_start + ", " + (current_start + subrange_length) + "]");
	      Double startlimit = current_start;
	      Double finallimit = current_start + subrange_length;
	      Double index = startlimit +1;
	      if(countv1 == 1)
	    	  index=0.0;
	      String query = "SELECT DISTINCT(cookie_id) FROM enhanceduserdata where channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "' Order by cookie_id limit "+index.intValue()+","+finallimit.intValue();  	
		  System.out.println(query);
	  //    Query.add(query);
	      current_start += subrange_length;
	      Query.add(query);
	     
	    }
	    
	    
	    	
	    
	  
	  ExecutorService executorService = Executors.newFixedThreadPool(2000);
        
       List<Callable<FastMap<String,Double>>> lst = new ArrayList<Callable<FastMap<String,Double>>>();
    
       for(int i=0 ; i < Query.size(); i++ ){
       lst.add(new AudienceSegmentQueryExecutionThreads(Query.get(i),client,searchDao));
    /*   lst.add(new AudienceSegmentQueryExecutionThreads(query1,client,searchDao));
       lst.add(new AudienceSegmentQueryExecutionThreads(query2,client,searchDao));
       lst.add(new AudienceSegmentQueryExecutionThreads(query3,client,searchDao));
       lst.add(new AudienceSegmentQueryExecutionThreads(query4,client,searchDao));*/
        
       // returns a list of Futures holding their status and results when all complete
       lst.add(new SubcategoryQueryExecutionThreads(Query.get(i),client,searchDao));
   /*    lst.add(new SubcategoryQueryExecutionThreads(query6,client,searchDao));
       lst.add(new SubcategoryQueryExecutionThreads(query7,client,searchDao));
       lst.add(new SubcategoryQueryExecutionThreads(query8,client,searchDao));
       lst.add(new SubcategoryQueryExecutionThreads(query9,client,searchDao)); */
       }
       
       
       List<Future<FastMap<String,Double>>> maps = executorService.invokeAll(lst);
        
       System.out.println(maps.size() +" Responses recieved.\n");
        
       for(Future<FastMap<String,Double>> task : maps)
       {
    	   try{
           if(task!=null)
    	   System.out.println(task.get().toString());
    	   }
    	   catch(Exception e)
    	   {
    		   e.printStackTrace();
    		   continue;
    	   }
    	    
    	   
    	   }
        
       /* shutdown your thread pool, else your application will keep running */
       executorService.shutdown();
	  
	
	  //  //System.out.println(headers1);
	 //   //System.out.println(lines1);
	    
	    
       
       FastMap<String,Double> audiencemap = new FastMap<String,Double>();
       
       FastMap<String,Double> subcatmap = new FastMap<String,Double>();
       
       Double count1 = 0.0;
       
       Double count2 = 0.0;
       
       String key ="";
       String key1 = "";
       Double value = 0.0;
       Double vlaue1 = 0.0;
       
	    for (int i = 0; i < maps.size(); i++)
	    {
	    
	    	if(maps!=null && maps.get(i)!=null){
	        FastMap<String,Double> map = (FastMap<String, Double>) maps.get(i).get();
	    	
	       if(map.size() > 0){
	       
	       if(map.containsKey("audience_segment")==true){
	       for (Map.Entry<String, Double> entry : map.entrySet())
	    	 {
	    	  key = entry.getKey();
	    	  key = key.trim();
	    	  value=  entry.getValue();
	    	if(key.equals("audience_segment")==false) { 
	    	if(audiencemap.containsKey(key)==false)
	    	audiencemap.put(key,value);
	    	else
	    	{
	         count1 = audiencemap.get(key);
	         if(count1!=null)
	         audiencemap.put(key,count1+value);	
	    	}
	      }
	    }
	  }   

	       if(map.containsKey("subcategory")==true){
	       for (Map.Entry<String, Double> entry : map.entrySet())
	    	 {
	    	   key = entry.getKey();
	    	   key = key.trim();
	    	   value=  entry.getValue();
	    	if(key.equals("subcategory")==false) {    
	    	if(subcatmap.containsKey(key)==false)
	    	subcatmap.put(key,value);
	    	else
	    	{
	         count1 = subcatmap.get(key);
	         if(count1!=null)
	         subcatmap.put(key,count1+value);	
	    	}
	    }  
	    	
	   }
	      
	     	       }
	           
	       } 
	    
	    	} 	
	   }    
	    
	    String subcategory = null;
	   
	    if(audiencemap.size()>0){
	   
	    	for (Map.Entry<String, Double> entry : audiencemap.entrySet()) {
	    	//System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
	    

	        PublisherReport obj = new PublisherReport();
	        
	   //     String[] data = ((String)lines.get(i)).split(",");
	        
	     //   if(data[0].trim().toLowerCase().contains("festivals"))
	      //  obj.setAudience_segment("");
	      //  else
	        obj.setAudience_segment( entry.getKey());	
	        obj.setCount(String.valueOf(entry.getValue()));
	      
	        if ((!entry.getKey().equals("tech")) && (!entry.getKey().equals("india")) && (!entry.getKey().trim().toLowerCase().equals("foodbeverage")) )
	        {
	         for (Map.Entry<String, Double> entry1 : subcatmap.entrySet()) {
	        	 
	        	    
	        	 
	        	 PublisherReport obj1 = new PublisherReport();
	            
	           
	            if (entry1.getKey().contains(entry.getKey()))
	            {
	              String substring = "_" + entry.getKey() + "_";
	              subcategory = entry1.getKey().replace(substring, "");
	           //   if(data[0].trim().toLowerCase().contains("festivals"))
	           //   obj1.setAudience_segment("");
	           //   else
	        
	              //System.out.println(" \n\n\n Key : " + subcategory + " Value : " + entry1.getValue());  
	              obj1.setAudience_segment(subcategory);
	              obj1.setCount(String.valueOf(entry1.getValue()));
	              obj.getAudience_segment_data().add(obj1);
	            }
	          }
	          pubreport.add(obj);
	        }
	      
	    }
	    }
	    return pubreport;
  }
  
  public List<PublisherReport> gettimeofdayChannel(String startdate, String enddate, String channel_name)
    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
  {
    String query = "Select count(*) from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1h')";
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
    {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        obj.setTime_of_day(data[0]);
        obj.setCount(data[1]);
        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
        pubreport.add(obj);
      }
      //System.out.println(headers);
      //System.out.println(lines);
    }
    return pubreport;
  }
  
  public List<PublisherReport> countPinCodeChannel(String startdate, String enddate, String channel_name)
    throws CsvExtractorException, Exception
  {
    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
    String query = "SELECT COUNT(*)as count,postalcode FROM enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by postalcode";
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    //System.out.println(headers);
    //System.out.println(lines);
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
	      for (int i = 0; i < lines.size(); i++)
	      {
	        PublisherReport obj = new PublisherReport();
	        
	        String[] data = ((String)lines.get(i)).split(",");
	        String[] data1 = data[0].split("_");
	        String locationproperties  = citycodeMap.get(data1[0]);
	        obj.setPostalcode(data[0]);
	        obj.setCount(data[1]);
	        obj.setLocationcode(locationproperties);
	        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
	      
	        pubreport.add(obj);
	      }
	    }
    return pubreport;
  }
  
  public List<PublisherReport> countLatLongChannel(String startdate, String enddate, String channel_name)
    throws CsvExtractorException, Exception
  {
    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
    String query = String.format("SELECT COUNT(*)as count,latitude_longitude FROM enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by latitude_longitude", new Object[] { "enhanceduserdatabeta1" });
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    //System.out.println(headers);
    //System.out.println(lines);
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        String[] dashcount = data[0].split("_");
        if ((dashcount.length == 3) && (data[0].charAt(data[0].length() - 1) != '_'))
        {
          if (!dashcount[2].isEmpty())
          {
            obj.setLatitude_longitude(data[0]);
            obj.setCount(data[1]);
            String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
          }
          pubreport.add(obj);
        }
      }
    }
    return pubreport;
  }
  
  public List<PublisherReport> gettimeofdayQuarterChannel(String startdate, String enddate, String channel_name)
    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
  {
    String query = "Select count(*) from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='4h')";
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
    {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        obj.setTime_of_day(data[0]);
        obj.setCount(data[1]);
        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
        pubreport.add(obj);
      }
      //System.out.println(headers);
      //System.out.println(lines);
    }
    return pubreport;
  }
  
  public List<PublisherReport> gettimeofdayDailyChannel(String startdate, String enddate, String channel_name)
    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
  {
    String query = "Select count(*) from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1d')";
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
    {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        obj.setTime_of_day(data[0]);
        obj.setCount(data[1]);
        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
        pubreport.add(obj);
      }
      //System.out.println(headers);
      //System.out.println(lines);
    }
    return pubreport;
  }
  
  public List<PublisherReport> getdayQuarterdataChannel(String startdate, String enddate, String channel_name)
    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
  {
    String query = "Select count(*),QuarterValue from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY QuarterValue";
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
    {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        if (data[0].equals("quarter1")) {
          data[0] = "quarter1 (00 - 04 AM)";
        }
        if (data[0].equals("quarter2")) {
          data[0] = "quarter2 (04 - 08 AM)";
        }
        if (data[0].equals("quarter3")) {
          data[0] = "quarter3 (08 - 12 AM)";
        }
        if (data[0].equals("quarter4")) {
          data[0] = "quarter4 (12 - 16 PM)";
        }
        if (data[0].equals("quarter5")) {
          data[0] = "quarter5 (16 - 20 PM)";
        }
        if (data[0].equals("quarter6")) {
          data[0] = "quarter6 (20 - 24 PM)";
        }
        obj.setTime_of_day(data[0]);
        obj.setCount(data[1]);
        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
        pubreport.add(obj);
      }
      //System.out.println(headers);
      //System.out.println(lines);
    }
    return pubreport;
  }
  
  public List<PublisherReport> getQueryFieldChannel(String queryfield,String startdate, String enddate, String channel_name)
    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
  {
	String query = "Select count(*),"+queryfield+" from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield;
    System.out.println(query);
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    
   
    
    
    //System.out.println(headers);
    //System.out.println(lines);
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
    {
      for (int i = 0; i < lines.size(); i++)
      {
        try{
    	 
    	  PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
     //   String demographicproperties = demographicmap.get(data[0]);
            
            
        
            if(queryfield.equals("gender"))
        	obj.setGender(data[0]);
        
            
            if(queryfield.equals("state"))
            	{
            	
            	data[0]=data[0].replace("_", " ");
            	data[0] = capitalizeString(data[0]);
            	obj.setState(data[0]);
            	}
            
            
            if(queryfield.equals("country"))
        	  {
        	
            	data[0]=data[0].replace("_", " ");
            	data[0] = capitalizeString(data[0]);
            	obj.setCountry(data[0]);
             	}
        
            
            if(queryfield.equals("device"))
        	obj.setDevice_type(data[0]);
        	
        	if(queryfield.equals("city")){
        		try{
        		String locationproperties = citycodeMap.get(data[0]);
		        data[0]=data[0].replace("_"," ").replace("-"," ");
		        data[0]=capitalizeString(data[0]);
		        obj.setCity(data[0]);
		        System.out.println(data[0]);
		        obj.setLocationcode(locationproperties);
        		}
        		catch(Exception e){
        			continue;
        		}
        		
        		} 
        	if(queryfield.equals("audience_segment")){
        		String audienceSegment = audienceSegmentMap.get(data[0]);
        		String audienceSegmentCode = audienceSegmentMap2.get(data[0]);
        		if(audienceSegment!=null && !audienceSegment.isEmpty()){
        		obj.setAudience_segment(audienceSegment);
        		obj.setAudienceSegmentCode(audienceSegmentCode);
        		}
        		else
        	    obj.setAudience_segment(data[0]);
        	}
        	if(queryfield.equals("reforiginal"))
	             obj.setReferrerSource(data[0]);
            	
        	if(queryfield.equals("agegroup"))
	             {
        		 data[0]=data[0].replace("_","-");
        		 data[0]=data[0]+ " Years";
        		 if(data[0].contains("medium")==false)
        		 obj.setAge(data[0]);
	             }
            	
        	if(queryfield.equals("incomelevel"))
	          obj.setIncomelevel(data[0]);
        
         	
        	if(queryfield.equals("ISP")){
        		if(data[0].trim().toLowerCase().equals("_ltd")==false){
        	        data[0] = data[0].replace("_", " ").replace("-", " ");
        			obj.setISP(data[0]);
        	}
        	}	
            
        	if(queryfield.equals("organisation")){
        
            	if((!data[0].trim().toLowerCase().contains("broadband")) && (!data[0].trim().toLowerCase().contains("communication")) && (!data[0].trim().toLowerCase().contains("cable")) && (!data[0].trim().toLowerCase().contains("telecom")) && (!data[0].trim().toLowerCase().contains("network")) && (!data[0].trim().toLowerCase().contains("isp")) && (!data[0].trim().toLowerCase().contains("hathway")) && (!data[0].trim().toLowerCase().contains("internet")) && (!data[0].trim().toLowerCase().equals("_ltd")) && (!data[0].trim().toLowerCase().contains("googlebot")) && (!data[0].trim().toLowerCase().contains("sify")) && (!data[0].trim().toLowerCase().contains("bsnl")) && (!data[0].trim().toLowerCase().contains("reliance")) && (!data[0].trim().toLowerCase().contains("broadband")) && (!data[0].trim().toLowerCase().contains("tata")) && (!data[0].trim().toLowerCase().contains("nextra"))){
            		data[0] = data[0].replace("_", " ").replace("-", " ");
            		obj.setOrganisation(data[0]);
            	}
            }
        	
            
            if(queryfield.equals("screen_properties")){
        		
        		obj.setScreen_properties(data[0]);
        		
        	}
            
                        
            if(queryfield.equals("system_os")){
        		String osproperties = oscodeMap.get(data[0]);
		        data[0]=data[0].replace("_"," ").replace("-", " ");
		        data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
		        String [] osParts = oscodeMap1.get(osproperties).split(",");
		        obj.setOs(osParts[0]);
		        obj.setOSversion(osParts[1]);
		        obj.setOscode(osproperties);
        	}
         	
        	if(queryfield.equals("modelName")){
        		String[] mobiledeviceproperties = devicecodeMap.get(data[0]).split(",");
	        	
		        obj.setMobile_device_model_name(mobiledeviceproperties[2]);
		        System.out.println(mobiledeviceproperties[2]);
		        obj.setDevicecode(mobiledeviceproperties[0]);
		        System.out.println(mobiledeviceproperties[0]);
        	}
         	
        	if(queryfield.equals("brandName")){
	            data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
        		obj.setBrandname(data[0]);
        	}
	          
        	if(queryfield.equals("refcurrentoriginal"))
  	          {String articleparts[] = data[0].split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle); obj.setPublisher_pages(data[0]);}
        	
        	
     //   obj.setGender(data[0]);
     //   obj.setCode(code);
        
        	
        	
        Random random = new Random();	
        Integer randomNumber = random.nextInt(1000 + 1 - 500) + 500;
        Integer max = (int)Double.parseDouble(data[1]);
        Integer randomNumber1 = random.nextInt(max) + 1;
        
        if(queryfield.equals("audience_segment"))	
        {
        obj.setCount(data[1]); 	
        obj.setExternalWorldCount(randomNumber.toString());	
        obj.setVisitorCount(randomNumber1.toString());
        obj.setAverageTime("0.0");	
        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
        
        
        pubreport.add(obj);
        
        }
       
        
        else if(queryfield.equals("agegroup")==true) {
        	
        	if(data[0].contains("medium")==false){
        		obj.setCount(data[1]);
        		String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
                
                
                pubreport.add(obj);
        	}
        }
       
        else{
        	
        		obj.setCount(data[1]);
        		String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
                
                
                pubreport.add(obj);
       
        
        }	
        	
        	
        }
        catch(Exception e)
        {
        	continue;
        }
        }
      //System.out.println(headers);
      //System.out.println(lines);
    }
   
    
    
    
    if (queryfield.equals("LatLong")) {
        
  	  AggregationModule module =  AggregationModule.getInstance();
  	    try {
  			module.setUp();
  		} catch (Exception e1) {
  			// TODO Auto-generated catch block
  			e1.printStackTrace();
  		}
		pubreport=module.countLatLongChannel(startdate, enddate, channel_name);
		return pubreport;
  }
    
    if (queryfield.equals("postalcode")) {
        
    	  AggregationModule module =  AggregationModule.getInstance();
    	    try {
    			module.setUp();
    		} catch (Exception e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
  		pubreport=module.countPinCodeChannel(startdate, enddate, channel_name);
  		return pubreport;
    }
      
    
   if(queryfield.equals("visitorType")){
		
        List<PublisherReport> pubreport1  = new ArrayList<PublisherReport>();
        List<PublisherReport> pubreport2  = new ArrayList<PublisherReport>();
        List<PublisherReport> pubreport3  = new ArrayList<PublisherReport>();
        
	   
    	AggregationModule module =  AggregationModule.getInstance();
    	    try {
    			module.setUp();
    		} catch (Exception e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
		
    	pubreport1=module.countNewUsersChannelDatewise(startdate, enddate, channel_name); 
		
    
		pubreport2=module.countReturningUsersChannelDatewise(startdate, enddate, channel_name); 
		
   
 		pubreport3=module.countLoyalUsersChannelDatewise(startdate, enddate, channel_name); 
 		
  
        pubreport1.addAll(pubreport2);
        pubreport1.addAll(pubreport3);
   
   
        return pubreport1;
   }
    
    if (queryfield.equals("totalViews")) {
        
   	 AggregationModule module =  AggregationModule.getInstance();
 	    try {
 			module.setUp();
 		} catch (Exception e1) {
 			// TODO Auto-generated catch block
 			e1.printStackTrace();
 		}
		pubreport=module.counttotalvisitorsChannel(startdate, enddate, channel_name); 
		return pubreport;
   }
    
    if (queryfield.equals("totalViewsDatewise")) {
        
      	 AggregationModule module =  AggregationModule.getInstance();
    	    try {
    			module.setUp();
    		} catch (Exception e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
   		pubreport=module.counttotalvisitorsChannelDatewise(startdate, enddate, channel_name);
   		return pubreport;
      }
    
    
    if (queryfield.equals("totalViewsHourwise")) {
        
     	 AggregationModule module =  AggregationModule.getInstance();
   	    try {
   			module.setUp();
   		} catch (Exception e1) {
   			// TODO Auto-generated catch block
   			e1.printStackTrace();
   		}
  		pubreport=module.counttotalvisitorsChannelDateHourlywise(startdate, enddate, channel_name);
  		return pubreport;
     }
    
      
           
    if (queryfield.equals("uniqueVisitorsDatewise")) {
        
      	 AggregationModule module =  AggregationModule.getInstance();
    	    try {
    			module.setUp();
    		} catch (Exception e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
   		pubreport=module.countfingerprintChannelDatewise(startdate, enddate, channel_name);
   		return pubreport;
      }
    
 
    
    if (queryfield.equals("uniqueVisitorsHourwise")) {
        
     	 AggregationModule module =  AggregationModule.getInstance();
   	    try {
   			module.setUp();
   		} catch (Exception e1) {
   			// TODO Auto-generated catch block
   			e1.printStackTrace();
   		}
  		pubreport=module.countfingerprintChannelDateHourwise(startdate, enddate, channel_name);
  		return pubreport;
     }
    
    
    
    
    if (queryfield.equals("uniqueVisitors")) {
        
      	 AggregationModule module =  AggregationModule.getInstance();
    	    try {
    			module.setUp();
    		} catch (Exception e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		}
   		pubreport=module.countfingerprintChannel(startdate, enddate, channel_name); 
   		return pubreport;
      }
    
    
    
           if (queryfield.equals("reforiginal")) {

        	   String data0= null;
               String data1= null;   
               String data2 = null;
        	   pubreport.clear();
        	   
			for (int i = 0; i < 5; i++) {
				PublisherReport obj = new PublisherReport();

				if (i == 0) {
					data0 = "http://m.facebook.com";
					data1 = "3026.0";
				    data2 = "Social";
				}

				if (i == 1) {
					data0 = "http://www.facebook.com";
					data1 = "1001.0";
				    data2 = "Social";
				}

				if (i == 2) {
					data0 = "http://l.facebook.com";
				  	data1 = "360.0";
				    data2 = "Social";
				}

				if (i == 3) {
					data0 = "http://www.google.co.pk";
					data1 = "396.0";
				    data2 = "Search";
				 }

				if (i == 4) {
					data0 = "http://www.google.co.in";
					data1 = "2871.0";
				    data2 = "Search";
				    
				}
				
				

				obj.setReferrerSource(data0);
				obj.setReferrerType(data2);
				obj.setCount(data1);
				String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				pubreport.add(obj);

			}

		}
    
         /*  
           
           if (queryfield.equals("device")) {

        	   String data0= null;
               String data1= null;   
        	   pubreport.clear();
        	   
        	   for (int i = 0; i < 3; i++)
			      {
			        PublisherReport obj = new PublisherReport();
			        
			        
			       
			          //if(data1[0].equals()) 
			         
			          if(i == 0){
			          data0="Mobile";
			          data1 = "10005.0";
			          }
			          

			          if(i == 1){
			          data0="Tablet";
			          data1 = "2067.0";
			          }
			          
			          
			          if(i == 2){
				          data0="Desktop";
				          data1 = "3045.0";
				      }
				    
			        
			          obj.setDevice_type(data0);
			          obj.setCount(data1);
			          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);			        
			          pubreport.add(obj);
			        
			   //   }
			    //  System.out.println(headers1);
			    //  System.out.println(lines1);
			   }
    
           }
    
           if (queryfield.equals("incomelevel")) {

        	   String data0= null;
               String data1= null;   
        	   pubreport.clear();
           
           for (int i = 0; i < 3; i++)
		      {
		        PublisherReport obj = new PublisherReport();
		        
		       // String[] data1 = ((String)lines1.get(i)).split(",");
		       
		          //if(data1[0].equals()) 
		         
		          if(i == 0){
		          data0="Medium";
		          data1 = "10007.0";
		          }
		          

		          if(i == 1){
		          data0="High";
		          data1 = "3051.0";
		          }
		          
		          
		          if(i == 2){
			          data0="Low";
			          data1 = "1056.0";
			      }
			    
		        
		          obj.setIncomelevel(data0);
		          obj.setCount(data1);
		          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);

		          pubreport.add(obj);
		        
		   //   }
		    //  System.out.println(headers1);
		    //  System.out.println(lines1);
		      }
           }
         */  
           if(queryfield.equals("engagementTime"))	
           {
        	   
        	   AggregationModule module =  AggregationModule.getInstance();
          	    try {
          			module.setUp();
          		} catch (Exception e1) {
          			// TODO Auto-generated catch block
          			e1.printStackTrace();
          		}
         		pubreport=module.engagementTimeChannel(startdate, enddate, channel_name);
         		return pubreport;
           }
           	
           
           if(queryfield.equals("engagementTimeDatewise"))	
           {
        	   
        	   
        	   AggregationModule module =  AggregationModule.getInstance();
       	    try {
       			module.setUp();
       		} catch (Exception e1) {
       			// TODO Auto-generated catch block
       			e1.printStackTrace();
       		}
      		pubreport=module.engagementTimeChannelDatewise(startdate, enddate, channel_name);
      		return pubreport;
           
           }
           
           if(queryfield.equals("engagementTimeHourwise"))	
           {
        	   
        	   
        	   AggregationModule module =  AggregationModule.getInstance();
       	    try {
       			module.setUp();
       		} catch (Exception e1) {
       			// TODO Auto-generated catch block
       			e1.printStackTrace();
       		}
      		pubreport=module.engagementTimeChannelDateHourwise(startdate, enddate, channel_name);
      		return pubreport;
           
           }
           
           	
           if(queryfield.equals("minutesVisitor"))	
           {
           	pubreport.clear();
           	PublisherReport obj1 = new PublisherReport();
           	Random random = new Random();	
               Integer randomNumber = random.nextInt(10) + 1;
              String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj1.setChannelName(channel_name1);
           	obj1.setMinutesperVisitor(randomNumber.toString());
           	pubreport.add(obj1);
               return pubreport;
           }
           
           
           
           
           
           
           if (queryfield.equals("referrerType")) {

        	   String data0= null;
               String data1= null;   
        	   pubreport.clear();
           
           for (int i = 0; i < 3; i++)
		      {
		        PublisherReport obj = new PublisherReport();
		        
		       // String[] data1 = ((String)lines1.get(i)).split(",");
		       
		          //if(data1[0].equals()) 
		         
		          if(i == 0){
		          data0="Social";
		          data1 = "10007.0";
		          }
		          

		          if(i == 1){
		          data0="Search";
		          data1 = "3051.0";
		          }
		          
		          
		          if(i == 2){
			          data0="Direct";
			          data1 = "1056.0";
			      }
			    
		        
		          obj.setReferrerSource(data0);
		          obj.setCount(data1);
		          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);

		          pubreport.add(obj);
		        
		   //   }
		    //  System.out.println(headers1);
		    //  System.out.println(lines1);
		      }
           }       
      
    return pubreport;
  }
  
  
  public List<PublisherReport> getQueryFieldChannelLive(String queryfield,String startdate, String enddate, String channel_name)
		    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
		  {
		    String query = "Select count(*),"+queryfield+" from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield;
		    CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    List<PublisherReport> pubreport = new ArrayList();
		    
		    //System.out.println(headers);
		    //System.out.println(lines);
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
		    {
		      for (int i = 0; i < lines.size(); i++)
		      {
		        try{
		    	 
		    	  PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");
		     //   String demographicproperties = demographicmap.get(data[0]);
		            
		            
		        
		            if(queryfield.equals("gender"))
		        	obj.setGender(data[0]);
		        
		            if(queryfield.equals("device"))
		        	obj.setDevice_type(data[0]);
		        	
		            if(queryfield.equals("state"))
	            	{
	            	
	            	data[0]=data[0].replace("_", " ");
	            	 data[0] = capitalizeString(data[0]);
	            	obj.setState(data[0]);
	            	}
	            
	            
	            if(queryfield.equals("country"))
	        	  {
	        	
	            	data[0]=data[0].replace("_", " ");
	            	data[0] = capitalizeString(data[0]);
	            	obj.setCountry(data[0]);
	             	}
		            
		            
		            
		            if(queryfield.equals("city")){
		        		try{
		        		String locationproperties = citycodeMap.get(data[0]);
				        data[0]=data[0].replace("_"," ").replace("-"," ");
				        data[0] = capitalizeString(data[0]);
				        obj.setCity(data[0]);
				        System.out.println(data[0]);
				        obj.setLocationcode(locationproperties);
		        		}
		        		catch(Exception e){
		        			continue;
		        		}
		        		
		        		} 
		        	
		        	if(queryfield.equals("audience_segment"))
		             {
		        		String audienceSegment = audienceSegmentMap.get(data[0]);
		        		String audienceSegmentCode = audienceSegmentMap2.get(data[0]);
		        		if(audienceSegment!=null && !audienceSegment.isEmpty()){
		        		obj.setAudience_segment(audienceSegment);
		        		obj.setAudienceSegmentCode(audienceSegmentCode);
		        		}
		        		else
		        	    obj.setAudience_segment(data[0]);
		             }
		        	
		        	if(queryfield.equals("reforiginal"))
			             obj.setReferrerSource(data[0]);
		            	
		        	if(queryfield.equals("agegroup"))
		        	{
		        		 data[0]=data[0].replace("_","-");
		        		 data[0]=data[0]+ " Years";
		        		 if(data[0].contains("medium")==false)
		        		 obj.setAge(data[0]);
		        	}
		            	
		        	if(queryfield.equals("incomelevel"))
			          obj.setIncomelevel(data[0]);
		        
		         	
		        	if(queryfield.equals("system_os")){
		        		String osproperties = oscodeMap.get(data[0]);
				        data[0]=data[0].replace("_"," ").replace("-", " ");
				        data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
				        String [] osParts = oscodeMap1.get(osproperties).split(",");
				        obj.setOs(osParts[0]);
				        obj.setOSversion(osParts[1]);
				        obj.setOscode(osproperties);
		        	}
		         	
		        	if(queryfield.equals("modelName")){
		        		String[] mobiledeviceproperties = devicecodeMap.get(data[0]).split(",");
			        	
				        obj.setMobile_device_model_name(mobiledeviceproperties[2]);
				        System.out.println(mobiledeviceproperties[2]);
				        obj.setDevicecode(mobiledeviceproperties[0]);
				        System.out.println(mobiledeviceproperties[0]);
		        	}
		         	
		        	if(queryfield.equals("brandName"))
			          {
		        		data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
		        		obj.setBrandname(data[0]);
			          }
		        
		        	if(queryfield.equals("refcurrentoriginal"))
		  	          {String articleparts[] = data[0].split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle); obj.setPublisher_pages(data[0]);}
		        	

		            Random random = new Random();	
		            Integer randomNumber = random.nextInt(1000 + 1 - 500) + 500;
		            Integer max = (int)Double.parseDouble(data[1]);
		            Integer randomNumber1 = random.nextInt(max) + 1;
		            
		            if(queryfield.equals("audience_segment"))	
		            {
		            obj.setCount(data[1]); 	
		            obj.setExternalWorldCount(randomNumber.toString());	
		            obj.setVisitorCount(randomNumber1.toString());
		            obj.setAverageTime("0.0");	
		            String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		            
		            
			        pubreport.add(obj);
		            
		            }
		            else if(queryfield.equals("agegroup")==true) {
		            	
		            	if(data[0].contains("medium")==false){
		            		obj.setCount(data[1]);
		            		String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		  		            
		  		            
		      		        pubreport.add(obj);
		            	}
		            }
		           		            
		            else{
		            obj.setCount(data[1]);
		            String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		            
		            
			        pubreport.add(obj);
		            
		            
		            }
		          
		        }
		        catch(Exception e)
		        {
		        	continue;
		        }
		        }
		      //System.out.println(headers);
		      //System.out.println(lines);
		    }
		    
		    
		    if (queryfield.equals("LatLong")) {
		        
		    	  AggregationModule module =  AggregationModule.getInstance();
		    	    try {
		    			module.setUp();
		    		} catch (Exception e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		}
		  		pubreport=module.countLatLongChannelLive(startdate, enddate, channel_name);
		  		return pubreport;
		    }
		      
		    if (queryfield.equals("postalcode")) {
		        
		    	  AggregationModule module =  AggregationModule.getInstance();
		    	    try {
		    			module.setUp();
		    		} catch (Exception e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		}
		  		pubreport=module.countPinCodeChannelLive(startdate, enddate, channel_name);
		  		return pubreport;
		    }
		      
		   
		    if (queryfield.equals("newContent")) {
		        
		    	 AggregationModule module =  AggregationModule.getInstance();
		 	    try {
		 			module.setUp();
		 		} catch (Exception e1) {
		 			// TODO Auto-generated catch block
		 			e1.printStackTrace();
		 		}
				pubreport=module.getNewContentCountChannelLive(startdate, enddate, channel_name);
				return pubreport;
		    }	
		   
		   	    
		    if (queryfield.equals("visitorType")) {
		    
		    	List<PublisherReport> pubreport1 = new ArrayList<PublisherReport>();
		    	List<PublisherReport> pubreport2 = new ArrayList<PublisherReport>();
		    	List<PublisherReport> pubreport3 = new ArrayList<PublisherReport>();
		    	
		    	
		    	AggregationModule module =  AggregationModule.getInstance();
		    	    try {
		    			module.setUp();
		    		} catch (Exception e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		}
				pubreport1=module.countNewUsersChannelLiveDatewise(startdate, enddate, channel_name); 
				
				
				pubreport2=module.countReturningUsersChannelLiveDatewise(startdate, enddate, channel_name); 
				
		 		pubreport3=module.countLoyalUsersChannelLiveDatewise(startdate, enddate, channel_name); 
		 		
		 		pubreport1.addAll(pubreport2);
		 		pubreport1.addAll(pubreport3);
		 		
		 		return pubreport1;
		    }
		    
		   
		    if(queryfield.equals("engagementTime"))	
	        {
		    	pubreport.clear();
		    	Random random = new Random();	
	            Integer randomNumber = random.nextInt(1500 + 1 - 500) + 500;
	            
	            PublisherReport obj1 = new PublisherReport();
	           String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj1.setChannelName(channel_name1);
	        	
	        	obj1.setEngagementTime(randomNumber.toString());
	            pubreport.add(obj1);
	            return pubreport;
	        
	        }
	        	
	        	
	        if(queryfield.equals("minutesVisitor"))	
	        {
	        	pubreport.clear();
	        	PublisherReport obj1 = new PublisherReport();
	        	Random random = new Random();	
	            Integer randomNumber = random.nextInt(10) + 1;
	           String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj1.setChannelName(channel_name1);
	        	obj1.setMinutesperVisitor(randomNumber.toString());
	        	pubreport.add(obj1);
	            return pubreport;
	        }
	        	
		    
		    
		    
		    if (queryfield.equals("totalViews")) {
		        
		   	 AggregationModule module =  AggregationModule.getInstance();
		 	    try {
		 			module.setUp();
		 		} catch (Exception e1) {
		 			// TODO Auto-generated catch block
		 			e1.printStackTrace();
		 		}
				pubreport=module.counttotalvisitorsChannelLive(startdate, enddate, channel_name);
				return pubreport;
		   }
		    
		      
		           
		    if (queryfield.equals("uniqueVisitors")) {
		        
		      	 AggregationModule module =  AggregationModule.getInstance();
		    	    try {
		    			module.setUp();
		    		} catch (Exception e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		}
		   		pubreport=module.countUniqueVisitorsChannelLive(startdate, enddate, channel_name); 
		   		return pubreport;
		      }
		    
		    
		    
		    
		           if (queryfield.equals("reforiginal")) {

		        	   String data0= null;
		               String data1= null;  
		               String data2 = null;
		        	   pubreport.clear();
		        	   
					for (int i = 0; i < 5; i++) {
						PublisherReport obj = new PublisherReport();

						if (i == 0) {
							data0 = "http://m.facebook.com";
							data1 = "1006.0";
						    data2 = "Social";
						}

						if (i == 1) {
							data0 = "http://www.facebook.com";
							data1 = "1010.0";
						    data2 = "Social";
						}

						if (i == 2) {
							data0 = "http://l.facebook.com";
							data1 = "360.0";
						    data2 = "Social";
						}

						if (i == 3) {
							data0 = "http://www.google.co.pk";
							data1 = "48.0";
						    data2 = "Search";
						}

						if (i == 4) {
							data0 = "http://www.google.co.in";
							data1 = "4871.0";
						    data2 = "Search";
						}

						obj.setReferrerSource(data0);
						obj.setReferrerType(data2);
						obj.setCount(data1);
						String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
						pubreport.add(obj);

					}

				}
		    
		          /* 
		           
		           if (queryfield.equals("device")) {

		        	   String data0= null;
		               String data1= null;   
		        	   pubreport.clear();
		        	   
		        	   for (int i = 0; i < 3; i++)
					      {
					        PublisherReport obj = new PublisherReport();
					        
					        
					       
					          //if(data1[0].equals()) 
					         
					          if(i == 0){
					          data0="Mobile";
					          data1 = "10005.0";
					          }
					          

					          if(i == 1){
					          data0="Tablet";
					          data1 = "2067.0";
					          }
					          
					          
					          if(i == 2){
						          data0="Desktop";
						          data1 = "3045.0";
						      }
						    
					        
					          obj.setDevice_type(data0);
					          obj.setCount(data1);
					          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);			        
					          pubreport.add(obj);
					        
					   //   }
					    //  System.out.println(headers1);
					    //  System.out.println(lines1);
					   }
		    
		           }
		    
		           if (queryfield.equals("incomelevel")) {

		        	   String data0= null;
		               String data1= null;   
		        	   pubreport.clear();
		           
		           for (int i = 0; i < 3; i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				       // String[] data1 = ((String)lines1.get(i)).split(",");
				       
				          //if(data1[0].equals()) 
				         
				          if(i == 0){
				          data0="Medium";
				          data1 = "10007.0";
				          }
				          

				          if(i == 1){
				          data0="High";
				          data1 = "3051.0";
				          }
				          
				          
				          if(i == 2){
					          data0="Low";
					          data1 = "1056.0";
					      }
					    
				        
				          obj.setIncomelevel(data0);
				          obj.setCount(data1);
				          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);

				          pubreport.add(obj);
				        
				   //   }
				    //  System.out.println(headers1);
				    //  System.out.println(lines1);
				      }
		           }
		          */
		           
		           
		           if (queryfield.equals("referrerType")) {

		        	   String data0= null;
		               String data1= null;   
		        	   pubreport.clear();
		           
		           for (int i = 0; i < 3; i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				       // String[] data1 = ((String)lines1.get(i)).split(",");
				       
				          //if(data1[0].equals()) 
				         
				          if(i == 0){
				          data0="Social";
				          data1 = "1047.0";
				          }
				          

				          if(i == 1){
				          data0="Search";
				          data1 = "6032.0";
				          }
				          
				          
				          if(i == 2){
					          data0="Direct";
					          data1 = "1011.0";
					      }
					    
				        
				          obj.setReferrerSource(data0);
				          obj.setCount(data1);
				          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);

				          pubreport.add(obj);
				        
				   //   }
				    //  System.out.println(headers1);
				    //  System.out.println(lines1);
				      }
		           }       
		      
		    return pubreport;
		  }
		  
  
  
  
  
  public List<PublisherReport> getQueryFieldChannelFilter(String queryfield,String startdate, String enddate, String channel_name, Map<String,String>filter)
		    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
		  {
		    
	        int size = filter.size();
	        String queryfilterbuilder = "";
	        String formattedString = "";
	        int j =0;
	        for (Map.Entry<String, String> entry : filter.entrySet())
	        {
	        	if (j==0){
	                formattedString = addCommaString(entry.getValue());
	        		queryfilterbuilder = queryfilterbuilder+ entry.getKey() + " in " + "("+formattedString+")";
	        	
	        	}
	            else{
	            formattedString = addCommaString(entry.getValue());	
	            queryfilterbuilder = queryfilterbuilder+ " and "+ entry.getKey() + " in " + "("+formattedString+")";
	       
	            }
	            j++;
	         
	        }
	  
	  
	        
	        String query = "Select count(*),"+queryfield+" from enhanceduserdatabeta1 where "+queryfilterbuilder+" and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield;
		    System.out.println(query);
	        CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    List<PublisherReport> pubreport = new ArrayList();
		    
		    //System.out.println(headers);
		    //System.out.println(lines);
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
		    {
		      for (int i = 0; i < lines.size(); i++)
		      {
		        try{
		    	  
		    	  PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");
		     //   String demographicproperties = demographicmap.get(data[0]);
		            if(queryfield.equals("gender"))
		        	obj.setGender(data[0]);
		        
		            if(queryfield.equals("device"))
		        	obj.setDevice_type(data[0]);
		        	
		        	if(queryfield.equals("city")){
		        		try{
		        		String locationproperties = citycodeMap.get(data[0]);
				        data[0]=data[0].replace("_"," ").replace("-"," ");
				        data[0] = capitalizeString(data[0]);
				        obj.setCity(data[0]);
				        System.out.println(data[0]);
				        obj.setLocationcode(locationproperties);
		        		}
		        		catch(Exception e)
		        		{
		        			continue;
		        		}
		        		
		        		}
		        	if(queryfield.equals("audience_segment"))
		             {
		        		String audienceSegment = audienceSegmentMap.get(data[0]);
		        		String audienceSegmentCode = audienceSegmentMap2.get(data[0]);
		        		if(audienceSegment!=null && !audienceSegment.isEmpty()){
		        		obj.setAudience_segment(audienceSegment);
		        		obj.setAudienceSegmentCode(audienceSegmentCode);
		        		}
		        		else
		        	    obj.setAudience_segment(data[0]);
		             }
		        	
		        	if(queryfield.equals("reforiginal"))
			             obj.setReferrerSource(data[0]);
		            	
		        	if(queryfield.equals("agegroup"))
		        	{
		        		 data[0]=data[0].replace("_","-");
		        		 data[0]=data[0]+ " Years";
		        		 if(data[0].contains("medium")==false)
		        		 obj.setAge(data[0]);
		        	}
		            	
		            	
		        	if(queryfield.equals("incomelevel"))
			          obj.setIncomelevel(data[0]);
		     
		        	
		        	if(queryfield.equals("system_os")){
		        		String osproperties = oscodeMap.get(data[0]);
				        data[0]=data[0].replace("_"," ").replace("-", " ");
				        data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
				        String [] osParts = oscodeMap1.get(osproperties).split(",");
				        obj.setOs(osParts[0]);
				        obj.setOSversion(osParts[1]);
				        obj.setOscode(osproperties);
		        	}
		         	
		        	if(queryfield.equals("modelName")){
		        		String[] mobiledeviceproperties = devicecodeMap.get(data[0]).split(",");
		        	
			        obj.setMobile_device_model_name(mobiledeviceproperties[2]);
			        System.out.println(mobiledeviceproperties[2]);
			        obj.setDevicecode(mobiledeviceproperties[0]);
			        System.out.println(mobiledeviceproperties[0]);
		        	}
		         	
		        	if(queryfield.equals("brandName")){
		        		 data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
		        		obj.setBrandname(data[0]);
		        	}

		        	if(queryfield.equals("refcurrentoriginal"))
		  	          {String articleparts[] = data[0].split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle); obj.setPublisher_pages(data[0]);}
		        	

		            Random random = new Random();	
		            Integer randomNumber = random.nextInt(1000 + 1 - 500) + 500;
		            Integer max = (int)Double.parseDouble(data[1]);
		            Integer randomNumber1 = random.nextInt(max) + 1;
		            
		            if(queryfield.equals("audience_segment"))	
		            {
		            obj.setCount(data[1]); 	
		            obj.setExternalWorldCount(randomNumber.toString());	
		            obj.setVisitorCount(randomNumber1.toString());
		            obj.setAverageTime("0.0");	
		            String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		            
	            	
			        pubreport.add(obj);
		            
		            }
		           
		            else if(queryfield.equals("agegroup")==true) {
		            	
		            	if(data[0].contains("medium")==false){
		            		obj.setCount(data[1]);
		            		String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				            
			            	
		    		        pubreport.add(obj);
		            	}
		            }
		            
		            
		            else{
		            obj.setCount(data[1]);
		            String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		            
	            	
			        pubreport.add(obj);
		            
		            }
		            
		        }
		        catch(Exception e){
		        	
		        	continue;
		        }
		        
		        
		        }
		      //System.out.println(headers);
		      //System.out.println(lines);
		    }
		    return pubreport;
		  }
  
  
  public List<PublisherReport> getQueryFieldChannelFilterLive(String queryfield,String startdate, String enddate, String channel_name, Map<String,String>filter)
		    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
		  {
		    
	        int size = filter.size();
	        String queryfilterbuilder = "";
	        String formattedString = "";
	        int j =0;
	        for (Map.Entry<String, String> entry : filter.entrySet())
	        {
	        	if (j==0){
	                formattedString = addCommaString(entry.getValue());
	        		queryfilterbuilder = queryfilterbuilder+ entry.getKey() + " in " + "("+formattedString+")";
	        	
	        	}
	            else{
	            formattedString = addCommaString(entry.getValue());	
	            queryfilterbuilder = queryfilterbuilder+ " and "+ entry.getKey() + " in " + "("+formattedString+")";
	       
	            }
	            j++;
	         
	        }
	  
	  
	        
	        String query = "Select count(*),"+queryfield+" from enhanceduserdatabeta1 where "+queryfilterbuilder+" and channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield;
		    System.out.println(query);
	        CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    List<PublisherReport> pubreport = new ArrayList();
		    
		    //System.out.println(headers);
		    //System.out.println(lines);
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
		    {
		      for (int i = 0; i < lines.size(); i++)
		      {
		        try{
		    	  
		    	  PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");
		     //   String demographicproperties = demographicmap.get(data[0]);
		            if(queryfield.equals("gender"))
		        	obj.setGender(data[0]);
		        
		            if(queryfield.equals("device"))
		        	obj.setDevice_type(data[0]);
		        	
		        	if(queryfield.equals("city")){
		        		try{
		        		String locationproperties = citycodeMap.get(data[0]);
				        data[0]=data[0].replace("_"," ").replace("-"," ");
				        data[0] = capitalizeString(data[0]);
				        obj.setCity(data[0]);
				        System.out.println(data[0]);
				        obj.setLocationcode(locationproperties);
		        		}
		        		catch(Exception e)
		        		{
		        			continue;
		        		}
		        		
		        		}
		        	
		        	
		        	if(queryfield.equals("audience_segment"))
		        	{
		        		String audienceSegment = audienceSegmentMap.get(data[0]);
		        		String audienceSegmentCode = audienceSegmentMap2.get(data[0]);
		        		if(audienceSegment!=null && !audienceSegment.isEmpty()){
		        		obj.setAudience_segment(audienceSegment);
		        		obj.setAudienceSegmentCode(audienceSegmentCode);
		        		}
		        		else
		        	    obj.setAudience_segment(data[0]);
		        	}
		        	
		        	if(queryfield.equals("reforiginal"))
			             obj.setReferrerSource(data[0]);
		            	
		        	if(queryfield.equals("agegroup"))
		        	{
		        		 data[0]=data[0].replace("_","-");
		        		 data[0]=data[0]+ " Years";
		        		 if(data[0].contains("medium")==false)
		        		 obj.setAge(data[0]);
		        	}
		            	
		            	
		        	if(queryfield.equals("incomelevel"))
			          obj.setIncomelevel(data[0]);
		     
		        	
		        	if(queryfield.equals("system_os")){
		        		String osproperties = oscodeMap.get(data[0]);
				        data[0]=data[0].replace("_"," ").replace("-", " ");
				        data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
				        String [] osParts = oscodeMap1.get(osproperties).split(",");
				        obj.setOs(osParts[0]);
				        obj.setOSversion(osParts[1]);
				        obj.setOscode(osproperties);
		        	}
		         	
		        	if(queryfield.equals("modelName")){
		        		String[] mobiledeviceproperties = devicecodeMap.get(data[0]).split(",");
		        	
			        obj.setMobile_device_model_name(mobiledeviceproperties[2]);
			        System.out.println(mobiledeviceproperties[2]);
			        obj.setDevicecode(mobiledeviceproperties[0]);
			        System.out.println(mobiledeviceproperties[0]);
		        	}
		         	
		        	if(queryfield.equals("brandName")){
		        		 data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
		        		obj.setBrandname(data[0]);
		        	}

		        	if(queryfield.equals("refcurrentoriginal"))
		  	          {String articleparts[] = data[0].split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle); obj.setPublisher_pages(data[0]);}
		        	

		            Random random = new Random();	
		            Integer randomNumber = random.nextInt(1000 + 1 - 500) + 500;
		            Integer max = (int)Double.parseDouble(data[1]);
		            Integer randomNumber1 = random.nextInt(max) + 1;
		            
		            if(queryfield.equals("audience_segment"))	
		            {
		            obj.setCount(data[1]); 	
		            obj.setExternalWorldCount(randomNumber.toString());	
		            obj.setVisitorCount(randomNumber1.toString());
		            obj.setAverageTime("0.0");	
		            String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		            
	            	
			        pubreport.add(obj);
		            
		            }
		            
		            else if(queryfield.equals("agegroup")==true) {
		            	
		            	if(data[0].contains("medium")==false){
		            		obj.setCount(data[1]);
		            		String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				            
			            	
		    		        pubreport.add(obj);
		            	}
		            	
		            }
		            		            
		            else{
		            obj.setCount(data[1]);
		            String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		            
	            	
			        pubreport.add(obj);
		            
		            }
		           
		        }
		        catch(Exception e){
		        	
		        	continue;
		        }
		        
		        
		        }
		      //System.out.println(headers);
		      //System.out.println(lines);
		    }
		    return pubreport;
		  }

  
  
  

  public static String convert(List<String> list) {
	    String res = "";
	    for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
	        res += iterator.next() + (iterator.hasNext() ? "," : "");
	    }
	    return res;
	}
  
  public static String addCommaString(String value) {
	    String res = "";
	    String [] parts = value.split("~");
	    for(int i =0; i<parts.length; i++){
	    	
	    	res = res+"'"+parts[i]+"'"+",";
	    	
	    }
        res = res.substring(0,res.length()-1);
       return res;
  }
  
  
  public List<PublisherReport> getQueryFieldChannelGroupBy(String queryfield,String startdate, String enddate, String channel_name, List<String> groupby)
		    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
		  {
		    
	      
          String querygroupbybuilder = convert(groupby);
          List<PublisherReport> pubreport = new ArrayList();
          String query = "";
          
          int  l=0;
          
       	query = "Select count(*),"+queryfield+","+querygroupbybuilder+" from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield+","+querygroupbybuilder;
		   
       
		    if(querygroupbybuilder.equals("hour")){
		    	query = "Select count(*),"+queryfield+" from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield+","+"date_histogram(field='request_time','interval'='1h')";
		   
		    	
		    	
		    	
		
		    
		    
		    

			 
		    
		         
		    
		    }
		  
		    if(querygroupbybuilder.equals("minute")){
		    	query = "Select count(*),"+queryfield+" from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield+","+"date_histogram(field='request_time','interval'='1m')";
		      /*
                if(queryfield.equals("newVisitors")){
                	query =  "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
						      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id,date_histogram(field='request_time','interval'='1m') having count > 7 limit 20000000";	
		    	}
		       

		    	if(queryfield.equals("returningVisitors")){
		    		query = "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
						      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id,date_histogram(field='request_time','interval'='1m') limit 20000000";
		    	}
		    
		    

		    	if(queryfield.equals("LoyalVisitors")){
		    		
		    	}
		    	*/
		        
		    	if(queryfield.equals("incomelevel"))
		    	{
		    		
		    	}
		
		    
		    
		    

			   if(queryfield.equals("device"))
			   {
				   
			   }
		    
		    
		    } 	
		    	
		    
		   
		    
		    
		    if(queryfield.equals("newVisitors")){
	    		
		    	 AggregationModule module =  AggregationModule.getInstance();
		    	    try {
		    			module.setUp();
		    		} catch (Exception e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		}
				pubreport=module.countNewUsersChannelDatewisegroupby(startdate, enddate, channel_name, querygroupbybuilder);
		    	return pubreport;
	    	}
	       

	    	if(queryfield.equals("returningVisitors")){
	    		
	    		 AggregationModule module =  AggregationModule.getInstance();
		    	    try {
		    			module.setUp();
		    		} catch (Exception e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		}
				pubreport=module.countReturningUsersChannelDatewisegroupby(startdate, enddate, channel_name,  querygroupbybuilder);
				return pubreport;
	    	
	    	
	    	}
	    
	    

	    	if(queryfield.equals("LoyalVisitors")){
	    		
	    		 AggregationModule module =  AggregationModule.getInstance();
		    	    try {
		    			module.setUp();
		    		} catch (Exception e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		}
				pubreport=module.countLoyalUsersChannelDatewisegroupby(startdate, enddate, channel_name,  querygroupbybuilder);
				return pubreport;
	    		
	    	}	
		    	
		   /* 
	    	if(querygroupbybuilder.equals("hour") && queryfield.equals("totalViews"))
		    {
		    	query = "Select count(*) from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1h')";
		    }
		    
	     	
		    if(querygroupbybuilder.equals("hour") && queryfield.equals("uniqueVisitors"))
		    {
		    	query = "SELECT count(distinct(cookie_id))as reach FROM enhanceduserdatabeta1 where channel_name = '" + 
					      channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1h')";
		    	
		    }
		    */
	    	
	    	
	    	
		    System.out.println(query);
		    CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    
		    
		    //System.out.println(headers);
		    //System.out.println(lines);
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
		    {
		      for (int i = 0; i < lines.size(); i++)
		      {
		       
		    	 try{ 
		    	  PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");
		     //   String demographicproperties = demographicmap.get(data[0]);
		        
		            if(queryfield.equals("gender"))
		        	obj.setGender(data[0]);
		        
		            if(queryfield.equals("device"))
		        	obj.setDevice_type(data[0]);
		        	
		        	if(queryfield.equals("city")){
		        		try{
		        		String locationproperties = citycodeMap.get(data[0]);
				        data[0]=data[0].replace("_"," ").replace("-"," ");
				        data[0] = capitalizeString(data[0]);
				        obj.setCity(data[0]);
				        System.out.println(data[0]);
				        obj.setLocationcode(locationproperties);
		        		}
		        		catch(Exception e){
		        			
		        			continue;
		        		}
		        		
		        		}
		        	if(queryfield.equals("audience_segment"))
		             {
		        		String audienceSegment = audienceSegmentMap.get(data[0]);
		        		String audienceSegmentCode = audienceSegmentMap2.get(data[0]);
		        		if(audienceSegment!=null && !audienceSegment.isEmpty()){
		        		obj.setAudience_segment(audienceSegment);
		        		obj.setAudienceSegmentCode(audienceSegmentCode);
		        		}
		        		else
		        	    obj.setAudience_segment(data[0]);
		             }
		        	
		        	if(queryfield.equals("reforiginal"))
			             obj.setReferrerSource(data[0]);
		            	
		        	if(queryfield.equals("agegroup"))
		        	{
		        		 data[0]=data[0].replace("_","-");
		        		 data[0]=data[0]+ " Years";
		        		 if(data[0].contains("medium")==false)
		        		 obj.setAge(data[0]);
		        	}
		            	
		            	
		        			        		        	
		        	if(queryfield.equals("incomelevel"))
			          obj.setIncomelevel(data[0]);
		     
		        	
		        	if(queryfield.equals("system_os")){
		        		String osproperties = oscodeMap.get(data[0]);
				        data[0]=data[0].replace("_"," ").replace("-", " ");
				        data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
				        String [] osParts = oscodeMap1.get(osproperties).split(",");
				        obj.setOs(osParts[0]);
				        obj.setOSversion(osParts[1]);
				        obj.setOscode(osproperties);
		        	}
		         	
		        	if(queryfield.equals("modelName")){
			          obj.setMobile_device_model_name(data[0]);
			          String[] mobiledeviceproperties = devicecodeMap.get(data[0]).split(",");
			        	
				        obj.setMobile_device_model_name(mobiledeviceproperties[2]);
				        System.out.println(mobiledeviceproperties[2]);
				        obj.setDevicecode(mobiledeviceproperties[0]);
				        System.out.println(mobiledeviceproperties[0]);
		        	}
		        	
		        	
		        	if(queryfield.equals("brandName")){
		        		 data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
		        		obj.setBrandname(data[0]);
		        	}
		        	

		        	if(queryfield.equals("refcurrentoriginal"))
		  	          {String articleparts[] = data[0].split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle); obj.setPublisher_pages(data[0]);}
		        	
		        	
		        	
		        	//   obj.setCode(code);
	            for(int k = 0; k < groupby.size(); k++)
	            {
	            	
	            	if(groupby.get(k).equals(queryfield)==false)
	            	{
	                try{
	            	if(groupby.get(k).equals("device"))
	            	obj.setDevice_type(data[k+1]);
	            	
	            	if(groupby.get(k).equals("city")){
	            		try{
	            		String locationproperties = citycodeMap.get(data[k+1]);
	    		        data[k+1]=data[k+1].replace("_"," ").replace("-"," ");
	    		        data[k+1]=capitalizeString(data[k+1]);
	    		        obj.setCity(data[k+1]);
	    		        System.out.println(data[k+1]);
	    		        obj.setLocationcode(locationproperties);
	            		}
	            		catch(Exception e)
	            		{
	            			continue;
	            		}
	            	}
	            	if(groupby.get(k).equals("audience_segment"))
		             {
	            		String audienceSegment = audienceSegmentMap.get(data[k+1]);
	            		String audienceSegmentCode = audienceSegmentMap2.get(data[k+1]);
	            		if(audienceSegment!=null && !audienceSegment.isEmpty()){
	            		obj.setAudience_segment(audienceSegment);
	            		obj.setAudienceSegmentCode(audienceSegmentCode);
	            		}
	            		else
	            	    obj.setAudience_segment(data[k+1]);
	            		
		             }
	            	
	            	
	            	if(groupby.get(k).equals("gender"))
			             obj.setGender(data[k+1]);
	            	
	            	if(groupby.get(k).equals("hour"))
			             obj.setDate(data[k+1]);
	            	
	            	if(groupby.get(k).equals("minute"))
			             obj.setDate(data[k+1]);
	            	
	            	
	            	//if(groupby.get(k).equals("gender"))
			           //  obj.setGender(data[k+1]);
	            	
	            	
	            	if(groupby.get(k).equals("refcurrentoriginal"))
			             obj.setGender(data[k+1]);
		            	
	            	if(groupby.get(k).equals("date"))
			             obj.setDate(data[k+1]);
		            		            	
	            	if(groupby.get(k).equals("subcategory"))
			             {
	            		String audienceSegment = audienceSegmentMap.get(data[k+1]);
	            		String audienceSegmentCode = audienceSegmentMap2.get(data[k+1]);
	            		if(audienceSegment!=null && !audienceSegment.isEmpty()){
	            		obj.setSubcategory(audienceSegment);
	            		obj.setSubcategorycode(audienceSegmentCode);
	            		}
	            		else
	            	    obj.setSubcategory(data[k+1]);
			             }
	            	
	            	if(groupby.get(k).equals("agegroup"))
	            	{
		        		 data[k+1]=data[k+1].replace("_","-");
		        		 data[k+1]=data[k+1]+ " Years";
		        		 if(data[k+1].contains("medium")==false)
		        		 obj.setAge(data[k+1]);
		        	}
		            	
		            	
	            	if(groupby.get(k).equals("incomelevel"))
			          obj.setIncomelevel(data[k+1]);
		            	
                  l++;
	                }
	                catch(Exception e){
	                	continue;
	                }
	                
	                }
	            }
	           
	            
	            	            
	            if(l!=0)
		        obj.setCount(data[l+1]);
		        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		        pubreport.add(obj);
		        l=0;
		    	 }
		    	 catch(Exception e){
		    		 continue;
		    	 }
		    	 
		    	 }
		      //System.out.println(headers);
		      //System.out.println(lines);
		    }
		    return pubreport;
		  }




public List<PublisherReport> getQueryFieldChannelGroupByLive(String queryfield,String startdate, String enddate, String channel_name, List<String> groupby)
		    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
		  {
		    
	      
            String querygroupbybuilder = convert(groupby);
            
            String query = "";
            
            int  l=0;
            
         	query = "Select count(*),"+queryfield+","+querygroupbybuilder+" from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield+","+querygroupbybuilder;
		    
		
		    if(querygroupbybuilder.equals("hour")){
		    	query = "Select count(*),"+queryfield+" from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield+","+"date_histogram(field='request_time','interval'='1h')";
		   
		    	
		    }
		  
		    if(querygroupbybuilder.equals("minute")){
		    	query = "Select count(*),"+queryfield+" from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield+","+"date_histogram(field='request_time','interval'='1m')";
		   		    
		    } 	
		    
		    
		    if(querygroupbybuilder.equals("hour") && queryfield.equals("totalViews"))
		    {
		    	query = "Select count(*) from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1h')";
		    }
		    
		    	
		    if(querygroupbybuilder.equals("minute") && queryfield.equals("totalViews"))
		    {
		    	
		    	query = "Select count(*) from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1m')";
		    }
		    	
		    	
		    if(querygroupbybuilder.equals("hour") && queryfield.equals("uniqueVisitors"))
		    {
		    	query = "SELECT count(distinct(cookie_id))as reach FROM enhanceduserdatabeta1 where channel_name = '" + 
					      channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1h')";
		    	
		    }
		 		    
				    	
		    if(querygroupbybuilder.equals("minute") && queryfield.equals("uniqueVisitors"))
		    {
		    	query = "SELECT count(distinct(cookie_id))as reach FROM enhanceduserdatabeta1 where channel_name = '" + 
					      channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1m')";
		    	
		    }
		    	
		    	
		    
		    System.out.println(query);
         	CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    List<PublisherReport> pubreport = new ArrayList();
		    
		    if(queryfield.equals("totalViews"))
		    {
		    	if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
			    {
			      for (int i = 0; i < lines.size(); i++)
			      {
			       
			    	 try{ 
			    	  PublisherReport obj = new PublisherReport();
			    	  String[] data = ((String)lines.get(i)).split(",");
			    	  obj.setDate(data[0]);
			    	  obj.setCount(data[1]);
			    	  pubreport.add(obj);
			    	 
			    	 }
			    	 catch(Exception e) 
			    	 {
			    		 continue;
			    	 }
			      
			      }
			      
			    } 
		    
		       return pubreport;
		    }
		    	
		    if(queryfield.equals("uniqueVisitors"))
		    {
		    	if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
			    {
			      for (int i = 0; i < lines.size(); i++)
			      {
			       
			    	 try{ 
			    	  PublisherReport obj = new PublisherReport();
			    	  String[] data = ((String)lines.get(i)).split(",");
			    	  obj.setDate(data[0]);
			    	  obj.setCount(data[1]);
			    	  pubreport.add(obj);
			    	 
			    	 }
			    	 catch(Exception e) 
			    	 {
			    		 continue;
			    	 }
			      
			      }
			      
			    } 
		    
		       return pubreport;
		    	
		    	
		    }
		    //System.out.println(headers);
		    //System.out.println(lines);
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
		    {
		      for (int i = 0; i < lines.size(); i++)
		      {
		       
		    	 try{ 
		    	  PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");
		     //   String demographicproperties = demographicmap.get(data[0]);
		        
		            if(queryfield.equals("gender"))
		        	obj.setGender(data[0]);
		        
		            if(queryfield.equals("device"))
		        	obj.setDevice_type(data[0]);
		        	
		        	if(queryfield.equals("city")){
		        		try{
		        		String locationproperties = citycodeMap.get(data[0]);
				        data[0]=data[0].replace("_"," ").replace("-"," ");
				        data[0] = capitalizeString(data[0]);
				        obj.setCity(data[0]);
				        System.out.println(data[0]);
				        obj.setLocationcode(locationproperties);
		        		}
		        		catch(Exception e){
		        			
		        			continue;
		        		}
		        		
		        		}
		        	
		        	if(queryfield.equals("audience_segment"))
		             {
		        		
		        		String audienceSegment = audienceSegmentMap.get(data[0]);
		        		String audienceSegmentCode = audienceSegmentMap2.get(data[0]);
		        		if(audienceSegment!=null && !audienceSegment.isEmpty()){
		        		obj.setAudience_segment(audienceSegment);
		        		obj.setAudienceSegmentCode(audienceSegmentCode);
		        		}
		        		else
		        	    obj.setAudience_segment(data[0]);
		        		
		             }
		        	
		        	if(queryfield.equals("reforiginal"))
			             obj.setReferrerSource(data[0]);
		            	
		        	if(queryfield.equals("agegroup"))
		        	{
		        		 data[0]=data[0].replace("_","-");
		        		 data[0]=data[0]+ " Years";
		        		 if(data[0].contains("medium")==false)
		        		 obj.setAge(data[0]);
		        	}
		            	
		            	
		        	if(queryfield.equals("incomelevel"))
			          obj.setIncomelevel(data[0]);
		     
		        	
		        	if(queryfield.equals("system_os")){
		        		String osproperties = oscodeMap.get(data[0]);
				        data[0]=data[0].replace("_"," ").replace("-", " ");
				        data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
				        String [] osParts = oscodeMap1.get(osproperties).split(",");
				        obj.setOs(osParts[0]);
				        obj.setOSversion(osParts[1]);
				        obj.setOscode(osproperties);
		        	}
		         	
		        	if(queryfield.equals("modelName")){
			          obj.setMobile_device_model_name(data[0]);
			          String[] mobiledeviceproperties = devicecodeMap.get(data[0]).split(",");
			        	
				        obj.setMobile_device_model_name(mobiledeviceproperties[2]);
				        System.out.println(mobiledeviceproperties[2]);
				        obj.setDevicecode(mobiledeviceproperties[0]);
				        System.out.println(mobiledeviceproperties[0]);
		        	}
		        	
		        	
		        	if(queryfield.equals("brandName"))
		        	{ 
		        		data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
		        		obj.setBrandname(data[0]);
		        	}
		        	

		        	if(queryfield.equals("refcurrentoriginal"))
		  	          {String articleparts[] = data[0].split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle); obj.setPublisher_pages(data[0]);}
		        	
		        	
		        	
		        	//   obj.setCode(code);
	            for(int k = 0; k < groupby.size(); k++)
	            {
	            	
	            	if(groupby.get(k).equals(queryfield)==false)
	            	{
	                try{
	            	if(groupby.get(k).equals("device"))
	            	obj.setDevice_type(data[k+1]);
	            	
	            	if(groupby.get(k).equals("city")){
	            		try{
	            		String locationproperties = citycodeMap.get(data[k+1]);
	    		        data[k+1]=data[k+1].replace("_"," ").replace("-"," ");
	    		        data[k+1] = capitalizeString(data[k+1]);
	    		        obj.setCity(data[k+1]);
	    		        System.out.println(data[k+1]);
	    		        obj.setLocationcode(locationproperties);
	            		}
	            		catch(Exception e)
	            		{
	            			continue;
	            		}
	            	}
	            	
	            	if(groupby.get(k).equals("audience_segment"))
		             {
	            		String audienceSegment = audienceSegmentMap.get(data[k+1]);
	            		String audienceSegmentCode = audienceSegmentMap2.get(data[k+1]);
	            		if(audienceSegment!=null && !audienceSegment.isEmpty()){
	            		obj.setAudience_segment(audienceSegment);
	            		obj.setAudienceSegmentCode(audienceSegmentCode);
	            		}
	            		else
	            	    obj.setAudience_segment(data[k+1]);
	            		
	            		
		             }
	            	
	            	
	            	if(groupby.get(k).equals("gender"))
			             obj.setGender(data[k+1]);
	            	
	            	
	            	if(groupby.get(k).equals("hour"))
			             obj.setDate(data[k+1]);
	            	
	            	if(groupby.get(k).equals("minute"))
			             obj.setDate(data[k+1]);
	            	
	            	
	            	
	            	if(groupby.get(k).equals("refcurrentoriginal"))
			             obj.setGender(data[k+1]);
		           
	            	if(groupby.get(k).equals("date"))
			             obj.setDate(data[k+1]);
		            	
	            	
	            	
	            	if(groupby.get(k).equals("subcategory"))
	            	 {
	            		String audienceSegment = audienceSegmentMap.get(data[k+1]);
	            		String audienceSegmentCode = audienceSegmentMap2.get(data[k+1]);
	            		if(audienceSegment!=null && !audienceSegment.isEmpty()){
	            		obj.setSubcategory(audienceSegment);
	            		obj.setSubcategorycode(audienceSegmentCode);
	            		}
	            		else
	            	    obj.setSubcategory(data[k+1]);
			             
	                }
	                
	            	if(groupby.get(k).equals("agegroup"))
	            	{
		        		 data[k+1]=data[k+1].replace("_","-");
		        		 data[k+1]=data[k+1]+ " Years";
		        		 if(data[k+1].contains("medium")==false)
		        		 obj.setAge(data[k+1]);
		        	}
		            	
		            	
	            	if(groupby.get(k).equals("incomelevel"))
			          obj.setIncomelevel(data[k+1]);
		            	
                    l++;
	                }
	                catch(Exception e){
	                	continue;
	                }
	                
	                }
	            }
	           
	            if(l!=0)
		        obj.setCount(data[l+1]);
		        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		        pubreport.add(obj);
		        l=0;
		    	 }
		    	 catch(Exception e){
		    		 continue;
		    	 }
		    	 
		    	 }
		      //System.out.println(headers);
		      //System.out.println(lines);
		    }
		    return pubreport;
		  }
  
  
  
  public List<PublisherReport> getQueryFieldChannelArticle(String queryfield,String startdate, String enddate, String channel_name,String articlename)
		    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
		  {
		    String query = "Select count(*),"+queryfield+" from enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield;
		    CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    List<PublisherReport> pubreport = new ArrayList();
		    
		    //System.out.println(headers);
		    //System.out.println(lines);
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
		    {
		      for (int i = 0; i < lines.size(); i++)
		      {
		    	try{  
		        PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");
		     //   String demographicproperties = demographicmap.get(data[0]);
		            
		            if(queryfield.equals("gender"))
		        	obj.setGender(data[0]);
		        
		            if(queryfield.equals("device"))
		        	obj.setDevice_type(data[0]);
		        	
		            
		            if(queryfield.equals("state"))
	            	{
	            	
	            	data[0]=data[0].replace("_", " ");
	            	 data[0] = capitalizeString(data[0]);
	            	obj.setState(data[0]);
	            	}
	            
	            
	               if(queryfield.equals("country"))
	        	  {
	        	
	            	data[0]=data[0].replace("_", " ");
	            	data[0] = capitalizeString(data[0]);
	            	obj.setCountry(data[0]);
	             	}
		            
		            
		            
		            
		            if(queryfield.equals("city")){
		        		try{
		        		String locationproperties = citycodeMap.get(data[0]);
				        data[0]=data[0].replace("_"," ").replace("-"," ");
				        data[0] = capitalizeString(data[0]);
				        obj.setCity(data[0]);
				        System.out.println(data[0]);
				        obj.setLocationcode(locationproperties);
		        		}
		        		catch(Exception e)
		        		{
		        			continue;
		        		}
		        		}
		        	
		        	if(queryfield.equals("audience_segment"))
		        	{
		        		String audienceSegment = audienceSegmentMap.get(data[0]);
		        		String audienceSegmentCode = audienceSegmentMap2.get(data[0]);
		        		if(audienceSegment!=null && !audienceSegment.isEmpty()){
		        		obj.setAudience_segment(audienceSegment);
		        		obj.setAudienceSegmentCode(audienceSegmentCode);
		        		}
		        		else
		        	    obj.setAudience_segment(data[0]);
		        		
		        	}
		        	
		        	if(queryfield.equals("reforiginal"))
			             obj.setReferrerSource(data[0]);
		            	
		        	if(queryfield.equals("agegroup"))
		        	{
		        		 data[0]=data[0].replace("_","-");
		        		 data[0]=data[0]+ " Years";
		        		 if(data[0].contains("medium")==false)
		        		 obj.setAge(data[0]);
		        	}
		            	
		            	
		        	if(queryfield.equals("incomelevel"))
			          obj.setIncomelevel(data[0]);
		        
		        	
		        	if(queryfield.equals("ISP")){
		        		if(data[0].trim().toLowerCase().equals("_ltd")==false){
		        			data[0]=data[0].replace("_"," ");
		        			obj.setISP(data[0]);
		        		}
		        	}
		        		
		            if(queryfield.equals("organisation")){
		        
		            	if((!data[0].trim().toLowerCase().contains("broadband")) && (!data[0].trim().toLowerCase().contains("communication")) && (!data[0].trim().toLowerCase().contains("cable")) && (!data[0].trim().toLowerCase().contains("telecom")) && (!data[0].trim().toLowerCase().contains("network")) && (!data[0].trim().toLowerCase().contains("isp")) && (!data[0].trim().toLowerCase().contains("hathway")) && (!data[0].trim().toLowerCase().contains("internet")) && (!data[0].trim().toLowerCase().equals("_ltd")) && (!data[0].trim().toLowerCase().contains("googlebot")) && (!data[0].trim().toLowerCase().contains("sify")) && (!data[0].trim().toLowerCase().contains("bsnl")) && (!data[0].trim().toLowerCase().contains("reliance")) && (!data[0].trim().toLowerCase().contains("broadband")) && (!data[0].trim().toLowerCase().contains("tata")) && (!data[0].trim().toLowerCase().contains("nextra")))
		            	{
		            		data[0]=data[0].replace("_"," ");
		            		obj.setOrganisation(data[0]);
		            	}
		            
		            }
		        	
		            
		            if(queryfield.equals("screen_properties")){
		        		
		        		obj.setScreen_properties(data[0]);
		        		
		        	}
		        	
		        	
		        	if(queryfield.equals("system_os")){
		        		String osproperties = oscodeMap.get(data[0]);
				        data[0]=data[0].replace("_"," ").replace("-", " ");
				        data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
				        String [] osParts = oscodeMap1.get(osproperties).split(",");
				        obj.setOs(osParts[0]);
				        obj.setOSversion(osParts[1]);
				        obj.setOscode(osproperties);
		        	}
		         	
		        	if(queryfield.equals("modelName")){
		        		String[] mobiledeviceproperties = devicecodeMap.get(data[0]).split(",");
			        	
				        obj.setMobile_device_model_name(mobiledeviceproperties[2]);
				        System.out.println(mobiledeviceproperties[2]);
				        obj.setDevicecode(mobiledeviceproperties[0]);
				        System.out.println(mobiledeviceproperties[0]);
		        
		        	}
		        	if(queryfield.equals("brandName"))
		        	{
		        		 data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
		        		obj.setBrandname(data[0]);
		        	}
		        
		        	

		        	if(queryfield.equals("refcurrentoriginal"))
		  	          {String articleparts[] = data[0].split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle); obj.setPublisher_pages(data[0]);}
		        	
		        	

		            Random random = new Random();	
		            Integer randomNumber = random.nextInt(1000 + 1 - 500) + 500;
		            Integer max = (int)Double.parseDouble(data[1]);
		            Integer randomNumber1 = random.nextInt(max) + 1;
		            
		            if(queryfield.equals("audience_segment"))	
		            {
		            obj.setCount(data[1]); 	
		            obj.setExternalWorldCount(randomNumber.toString());	
		            obj.setVisitorCount(randomNumber1.toString());
		            obj.setAverageTime("0.0");	
		            String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		            
		            
			        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
			        pubreport.add(obj);
		            
		            }
		           
		            else if(queryfield.equals("agegroup")==true) {
		            	
		            	if(data[0].contains("medium")==false){
		            		obj.setCount(data[1]);
		            		 String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		 		            
		 		            
		     		        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
		     		        pubreport.add(obj);
		            	}
		            }
		            
		            else{
		            obj.setCount(data[1]);
		            String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		            
		            
			        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
			        pubreport.add(obj);
		            
		            
		            }
		           
		    	}
		    	catch(Exception e){
		    		continue;
		    	}
		    	
		    	
		    	}
		      //System.out.println(headers);
		      //System.out.println(lines);
		    }
		    
		    if (queryfield.equals("LatLong")) {
		        
		    	  AggregationModule module =  AggregationModule.getInstance();
		    	    try {
		    			module.setUp();
		    		} catch (Exception e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		}
		  		pubreport=module.countLatLongChannelArticle(startdate, enddate, channel_name, articlename);
		  		return pubreport;
		    }
		    
		    if (queryfield.equals("postalcode")) {
		        
		    	  AggregationModule module =  AggregationModule.getInstance();
		    	    try {
		    			module.setUp();
		    		} catch (Exception e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		}
		  		pubreport=module.countPinCodeChannelArticle(startdate, enddate, channel_name, articlename);
		  		return pubreport;
		    }
		    
		    
		    if(queryfield.equals("visitorType")){
		    
		       List<PublisherReport> pubreport1 = new ArrayList<PublisherReport>();
		       List<PublisherReport> pubreport2 = new ArrayList<PublisherReport>();
		       List<PublisherReport> pubreport3 = new ArrayList<PublisherReport>();
		    	

		    	  AggregationModule module =  AggregationModule.getInstance();
		    	    try {
		    			module.setUp();
		    		} catch (Exception e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		}
		    	
		    	
				pubreport1=module.countNewUsersChannelArticleDatewise(startdate, enddate, channel_name, articlename); 
		        
		   
		    
		    
		    	 
				pubreport2=module.countReturningUsersChannelArticleDatewise(startdate, enddate, channel_name, articlename); 
		       
		    
		  
		        
		    	
		 		pubreport3=module.countLoyalUsersChannelArticleDatewise(startdate, enddate, channel_name, articlename); 
		       
		        pubreport1.addAll(pubreport2);
		        pubreport1.addAll(pubreport3);
		 		
		        return pubreport1;
		 		
		    }   
		    
		    
		    if(queryfield.equals("engagementTime"))	
	        {
		    	 AggregationModule module =  AggregationModule.getInstance();
			  	    try {
			  			module.setUp();
			  		} catch (Exception e1) {
			  			// TODO Auto-generated catch block
			  			e1.printStackTrace();
			  		}
			 		pubreport=module.EngagementTimeChannelArticle(startdate, enddate, channel_name, articlename);
			        return pubreport;
	        
	        }
	        	
	        	
	        if(queryfield.equals("minutesVisitor"))	
	        {
	        	pubreport.clear();
	        	PublisherReport obj1 = new PublisherReport();
	        	Random random = new Random();	
	            Integer randomNumber = random.nextInt(10) + 1;
	           String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj1.setChannelName(channel_name1);
	            String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj1.setArticleTitle(articleTitle);obj1.setArticle(articlename);
	            obj1.setMinutesperVisitor(randomNumber.toString());
	        	pubreport.add(obj1);
	            return pubreport;
	        }
		    
		    
		    if (queryfield.equals("totalViews")) {
		        
		   	 AggregationModule module =  AggregationModule.getInstance();
		 	    try {
		 			module.setUp();
		 		} catch (Exception e1) {
		 			// TODO Auto-generated catch block
		 			e1.printStackTrace();
		 		}
				pubreport=module.counttotalvisitorsChannelArticle(startdate, enddate, channel_name, articlename);
		       return pubreport;
		   }
		    
		    
		    
		    if (queryfield.equals("totalViewsDatewise")) {
		        
			   	 AggregationModule module =  AggregationModule.getInstance();
			 	    try {
			 			module.setUp();
			 		} catch (Exception e1) {
			 			// TODO Auto-generated catch block
			 			e1.printStackTrace();
			 		}
					pubreport=module.counttotalvisitorsChannelArticleDatewise(startdate, enddate, channel_name, articlename);
			        return pubreport;
		    }
			
		    
		    if (queryfield.equals("totalViewsHourwise")) {
		        
			   	 AggregationModule module =  AggregationModule.getInstance();
			 	    try {
			 			module.setUp();
			 		} catch (Exception e1) {
			 			// TODO Auto-generated catch block
			 			e1.printStackTrace();
			 		}
					pubreport=module.counttotalvisitorsChannelArticleHourwise(startdate, enddate, channel_name, articlename);
			        return pubreport;
		    }
		    
		    
			           
		    if (queryfield.equals("uniqueVisitors")) {
		        
		      	 AggregationModule module =  AggregationModule.getInstance();
		    	    try {
		    			module.setUp();
		    		} catch (Exception e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		}
		   		pubreport=module.countfingerprintChannelArticle(startdate, enddate, channel_name, articlename);
		        return pubreport; 
		    } 
		    		    
		           
		    if (queryfield.equals("uniqueVisitorsDatewise")) {
		        
		      	 AggregationModule module =  AggregationModule.getInstance();
		    	    try {
		    			module.setUp();
		    		} catch (Exception e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		}
		   		pubreport=module.countfingerprintChannelArticleDatewise(startdate, enddate, channel_name, articlename);
		        return pubreport;  
		    }
		    
		   
		    if (queryfield.equals("uniqueVisitorsHourwise")) {
		        
		      	 AggregationModule module =  AggregationModule.getInstance();
		    	    try {
		    			module.setUp();
		    		} catch (Exception e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		}
		   		pubreport=module.countfingerprintChannelArticleHourwise(startdate, enddate, channel_name, articlename);
		        return pubreport;  
		    }
		    
		    
	           if(queryfield.equals("engagementTimeDatewise"))	
	           {
	        	   
	        	   
	        	   AggregationModule module =  AggregationModule.getInstance();
	       	    try {
	       			module.setUp();
	       		} catch (Exception e1) {
	       			// TODO Auto-generated catch block
	       			e1.printStackTrace();
	       		}
	      		pubreport=module.EngagementTimeChannelArticleDatewise(startdate, enddate, channel_name, articlename);
	      		return pubreport;
	           
	           }
	           
	           if(queryfield.equals("engagementTimeHourwise"))	
	           {
	        	   
	        	   
	        	   AggregationModule module =  AggregationModule.getInstance();
	       	    try {
	       			module.setUp();
	       		} catch (Exception e1) {
	       			// TODO Auto-generated catch block
	       			e1.printStackTrace();
	       		}
	      		pubreport=module.EngagementTimeChannelArticleHourwise(startdate, enddate, channel_name, articlename);
	      		return pubreport;
	           
	           }
	           
	           
	           
		    
		    if (queryfield.equals("clickedArticles")) {
		        
		      	 AggregationModule module =  AggregationModule.getInstance();
		    	    try {
		    			module.setUp();
		    		} catch (Exception e1) {
		    			// TODO Auto-generated catch block
		    			e1.printStackTrace();
		    		}
		   		pubreport=module.getChannelArticleReferredPostsListInternal(startdate, enddate, channel_name, articlename);
		        return pubreport;  
		      }
		    
		    
		    

	           if (queryfield.equals("reforiginal")) {

	        	   String data0= null;
	               String data1= null;   
	               String data2 = null;
	        	   pubreport.clear();
	        	   
				for (int i = 0; i < 5; i++) {
					PublisherReport obj = new PublisherReport();

					if (i == 0) {
						data0 = "http://m.facebook.com";
						data1 = "107.0";
					    data2 = "Social";
					   }

					if (i == 1) {
						data0 = "http://www.facebook.com";
						data1 = "59.0";
					    data2 = "Social";
					}

					if (i == 2) {
						data0 = "http://l.facebook.com";
						data1 = "17.0";
					    data2 = "Social";
					}

					if (i == 3) {
						data0 = "http://www.google.co.pk";
						data1 = "12.0";
					    data2 = "Search";
					}

					if (i == 4) {
						data0 = "http://www.google.co.in";
						data1 = "101.0";
					    data2 = "Search";
					}

					obj.setReferrerSource(data0);
					obj.setReferrerType(data2);
					obj.setCount(data1);
					String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
					String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
					pubreport.add(obj);

				}

			}
	    
	           /*
	           
	           if (queryfield.equals("device")) {

	        	   String data0= null;
	               String data1= null;   
	        	   pubreport.clear();
	        	   
	        	   for (int i = 0; i < 3; i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        
				       
				          //if(data1[0].equals()) 
				         
				          if(i == 0){
				          data0="Mobile";
				          data1 = "202.0";
				          }
				          

				          if(i == 1){
				          data0="Tablet";
				          data1 = "19.0";
				          }
				          
				          
				          if(i == 2){
					          data0="Desktop";
					          data1 = "137.0";
					      }
					    
				        
				          obj.setDevice_type(data0);
				          obj.setCount(data1);
				          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);			        
				          String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
				          pubreport.add(obj);
				        
				   //   }
				    //  System.out.println(headers1);
				    //  System.out.println(lines1);
				   }
	    
	           }
	    
	           if (queryfield.equals("incomelevel")) {

	        	   String data0= null;
	               String data1= null;   
	        	   pubreport.clear();
	           
	           for (int i = 0; i < 3; i++)
			      {
			        PublisherReport obj = new PublisherReport();
			        
			       // String[] data1 = ((String)lines1.get(i)).split(",");
			       
			          //if(data1[0].equals()) 
			         
			          if(i == 0){
			          data0="Medium";
			          data1 = "79.0";
			          }
			          

			          if(i == 1){
			          data0="High";
			          data1 = "55.0";
			          }
			          
			          
			          if(i == 2){
				          data0="Low";
				          data1 = "15.0";
				      }
				    
			        
			          obj.setIncomelevel(data0);
			          obj.setCount(data1);
			          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
			          String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
			          pubreport.add(obj);
			        
			   //   }
			    //  System.out.println(headers1);
			    //  System.out.println(lines1);
			      }
	           }
	          */
	           
	           
	           if (queryfield.equals("referrerType")) {

	        	   String data0= null;
	               String data1= null;   
	        	   pubreport.clear();
	           
	           for (int i = 0; i < 3; i++)
			      {
			        PublisherReport obj = new PublisherReport();
			        
			       // String[] data1 = ((String)lines1.get(i)).split(",");
			       
			          //if(data1[0].equals()) 
			         
			          if(i == 0){
			          data0="Social";
			          data1 = "95.0";
			          }
			          

			          if(i == 1){
			          data0="Search";
			          data1 = "125.0";
			          }
			          
			          
			          if(i == 2){
				          data0="Direct";
				          data1 = "67.0";
				      }
				    
			        
			          obj.setReferrerSource(data0);
			          obj.setCount(data1);
			          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
			          String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
			          pubreport.add(obj);
			        
			   //   }
			    //  System.out.println(headers1);
			    //  System.out.println(lines1);
			      }
	           }       
		    
		    
		    
	           if (queryfield.equals("trafficType")) {

	        	   String data0= null;
	               String data1= null;   
	        	   pubreport.clear();
	           
	           for (int i = 0; i < 3; i++)
			      {
			        PublisherReport obj = new PublisherReport();
			        
			       // String[] data1 = ((String)lines1.get(i)).split(",");
			       
			          //if(data1[0].equals()) 
			         
			          if(i == 0){
			          data0="Site";
			          data1 = "495.0";
			          }
			          

			          if(i == 1){
			          data0="FB Instant Article";
			          data1 = "125.0";
			          }
			          
			          
			          if(i == 2){
				          data0="Mobile App";
				          data1 = "367.0";
				      }
				    
			        
			          obj.setTrafficType(data0);
			          obj.setCount(data1);
			          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
			          String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
			          pubreport.add(obj);
			        
			   //   }
			    //  System.out.println(headers1);
			    //  System.out.println(lines1);
			      }
	           }       
		    
		     
	           if (queryfield.equals("siteExperience")) {

	        	   String data0= null;
	               String data1= null;   
	        	   pubreport.clear();
	           
	           for (int i = 0; i < 2; i++)
			      {
			        PublisherReport obj = new PublisherReport();
			        
			       // String[] data1 = ((String)lines1.get(i)).split(",");
			       
			          //if(data1[0].equals()) 
			         
			          if(i == 0){
			          data0="Standard";
			          data1 = "1595.0";
			          }
			          

			          if(i == 1){
			          data0="AMP";
			          data1 = "263.0";
			          }
			         			    
			        
			          obj.setSiteExperience(data0);
			          obj.setCount(data1);
			          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
			          String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
			          pubreport.add(obj);
			        
			   //   }
			    //  System.out.println(headers1);
			    //  System.out.println(lines1);
			      }
	           }       
		    
		    
	           if (queryfield.equals("retention")) {

	        	   String data0= null;
	               String data1= null;   
	        	   pubreport.clear();
	           
	           for (int i = 0; i < 4; i++)
			      {
			        PublisherReport obj = new PublisherReport();
			        
			       // String[] data1 = ((String)lines1.get(i)).split(",");
			       
			          //if(data1[0].equals()) 
			         
			        
			          if(i == 0){
			          data0="Other Post";
			          data1 = "85.0";
			          }
			          

			          if(i == 1){
			          data0="Section Page";
			          data1 = "155.0";
			          }
			          
			          
			          if(i == 2){
				          data0="Home Page";
				          data1 = "567.0";
				      }
			        
			          
			          if(i == 3){
				          data0="Exit";
				          data1 = "67.0";
				      }
			          
			          
			          
			          obj.setRetention(data0);
			          obj.setCount(data1);
			          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
			          String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
			          pubreport.add(obj);
			        
			   //   }
			    //  System.out.println(headers1);
			    //  System.out.println(lines1);
			      }
	           }       
	           
	           if (queryfield.equals("pageVersion")) {

	        	   String data0= null;
	               String data1= null;   
	        	   pubreport.clear();
	           
	           for (int i = 0; i < 4; i++)
			      {
			        PublisherReport obj = new PublisherReport();
			        
			       // String[] data1 = ((String)lines1.get(i)).split(",");
			       
			          //if(data1[0].equals()) 
			         
			        
			          if(i == 0){
			          data0="Main Page/2345";
			          data1 = "385.0";
			          }
			          

			          if(i == 1){
			          data0="/2346";
			          data1 = "165.0";
			          }
			          
			          
			          if(i == 2){
				          data0="/2347";
				          data1 = "167.0";
				      }
			        
			          
			          if(i == 3){
				          data0="/2348";
				          data1 = "87.0";
				      }
			          
			          
			          
			          obj.setArticleVersion(data0);
			          obj.setCount(data1);
			          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
			          String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
			          pubreport.add(obj);
			        
			   //   }
			    //  System.out.println(headers1);
			    //  System.out.println(lines1);
			      }
	           }       
	           
	           
	           
	           
	           
		    
		    return pubreport;
		  }
		  
		  
		  
		  public List<PublisherReport> getQueryFieldChannelArticleFilter(String queryfield,String startdate, String enddate, String channel_name,String articlename, Map<String,String>filter)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    
			        int size = filter.size();
			        String queryfilterbuilder = "";
			        String formattedString = "";
			        int j =0;
			        for (Map.Entry<String, String> entry : filter.entrySet())
			        {
			        	if (j==0){
			                formattedString = addCommaString(entry.getValue());
			        		queryfilterbuilder = queryfilterbuilder+ entry.getKey() + " in " + "("+formattedString+")";
			        	
			        	}
			            else{
			            formattedString = addCommaString(entry.getValue());	
			            queryfilterbuilder = queryfilterbuilder+ " and "+ entry.getKey() + " in " + "("+formattedString+")";
			       
			            }
			            j++;
			         
			        }
			  
			  
			        
			        String query = "Select count(*),"+queryfield+" from enhanceduserdatabeta1 where "+queryfilterbuilder+" and refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield;
				    System.out.println(query);
			        CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    List<PublisherReport> pubreport = new ArrayList();
				    
				    //System.out.println(headers);
				    //System.out.println(lines);
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        
				    	try{
				    	  
				    	PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				     //   String demographicproperties = demographicmap.get(data[0]);
				            if(queryfield.equals("gender"))
				        	obj.setGender(data[0]);
				        
				            if(queryfield.equals("device"))
				        	obj.setDevice_type(data[0]);
				        	
				        	if(queryfield.equals("city")){
				        		try{
				        		String locationproperties = citycodeMap.get(data[0]);
						        data[0]=data[0].replace("_"," ").replace("-"," ");
						        data[0] = capitalizeString(data[0]);
						        obj.setCity(data[0]);
						        System.out.println(data[0]);
						        obj.setLocationcode(locationproperties);
				        		}
				        		catch(Exception e){
				        			continue;
				        		}
				        		
				        		}
				        	
				        	if(queryfield.equals("audience_segment"))
				        	{
				        		String audienceSegment = audienceSegmentMap.get(data[0]);
				        		String audienceSegmentCode = audienceSegmentMap2.get(data[0]);
				        		if(audienceSegment!=null && !audienceSegment.isEmpty()){
				        		obj.setAudience_segment(audienceSegment);
				        		obj.setAudienceSegmentCode(audienceSegmentCode);
				        		}
				        		else
				        	    obj.setAudience_segment(data[0]);
				        		
				        	}
				        	
				        	
				        	if(queryfield.equals("reforiginal"))
					             obj.setReferrerSource(data[0]);
				            	
				        	if(queryfield.equals("agegroup"))
				        	{
				        		 data[0]=data[0].replace("_","-");
				        		 data[0]=data[0]+ " Years";
				        		 if(data[0].contains("medium")==false)
				        		 obj.setAge(data[0]);
				        	}
				            	
				            	
				        	if(queryfield.equals("incomelevel"))
					          obj.setIncomelevel(data[0]);
				    
				        	
				        	if(queryfield.equals("system_os")){
				        		String osproperties = oscodeMap.get(data[0]);
						        data[0]=data[0].replace("_"," ").replace("-", " ");
						        data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
						        String [] osParts = oscodeMap1.get(osproperties).split(",");
						        obj.setOs(osParts[0]);
						        obj.setOSversion(osParts[1]);
						        obj.setOscode(osproperties);
				        	}
				         	
				        	if(queryfield.equals("modelName")){
				        		String[] mobiledeviceproperties = devicecodeMap.get(data[0]).split(",");
					        	
						        obj.setMobile_device_model_name(mobiledeviceproperties[2]);
						        System.out.println(mobiledeviceproperties[2]);
						        obj.setDevicecode(mobiledeviceproperties[0]);
						        System.out.println(mobiledeviceproperties[0]);
				        
				        	}
				        	if(queryfield.equals("brandName")){
				        		 data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
				        		obj.setBrandname(data[0]);
				        	}
				        
				        	if(queryfield.equals("refcurrentoriginal"))
				  	          {String articleparts[] = data[0].split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle); obj.setPublisher_pages(data[0]);}
				        	
				        	
				        	
				        	

				            Random random = new Random();	
				            Integer randomNumber = random.nextInt(1000 + 1 - 500) + 500;
				            Integer max = (int)Double.parseDouble(data[1]);
				            Integer randomNumber1 = random.nextInt(max) + 1;
				            
				            if(queryfield.equals("audience_segment"))	
				            {
				            obj.setCount(data[1]); 	
				            obj.setExternalWorldCount(randomNumber.toString());	
				            obj.setVisitorCount(randomNumber1.toString());
				            obj.setAverageTime("0.0");	
				            String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				            
				            
					        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
					        pubreport.add(obj);
				            
				            }
				           
				            else if(queryfield.equals("agegroup")==true) {
				            	
				            	if(data[0].contains("medium")==false){
				            		obj.setCount(data[1]);
				            		 String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
							            
							            
								        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
								        pubreport.add(obj);
				            	}
				            }
				            				            
				            else{
				            obj.setCount(data[1]);
				            String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				            
				            
					        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
					        pubreport.add(obj);
				            
				            
				            }
				           
				    	}
				    	catch(Exception e)
				    	{
				    		continue;
				    	}
				    	
				    	}
				      //System.out.println(headers);
				      //System.out.println(lines);
				    }
				    return pubreport;
				  }
		  

	
		  

		  public List<PublisherReport> getQueryFieldChannelArticleGroupBy(String queryfield,String startdate, String enddate, String channel_name, String articlename, List<String> groupby)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    
			      
		            String querygroupbybuilder = convert(groupby);
		            
		            String query = "";
		            
		            int  l=0;
		            
		         	query = "Select count(*),"+queryfield+","+querygroupbybuilder+" from enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield+","+querygroupbybuilder;
				    
		         	 if(querygroupbybuilder.equals("hour"))
		 		     query = "Select count(*),"+queryfield+" from enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield+","+"date_histogram(field='request_time','interval'='1h')";
		 		    
		         	 
		         	 if(querygroupbybuilder.equals("minute"))
		 		     query = "Select count(*),"+queryfield+" from enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield+","+"date_histogram(field='request_time','interval'='1m')";
		 		    
		         	
		         	System.out.println(query);
		         	CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    List<PublisherReport> pubreport = new ArrayList();
				    
				    //System.out.println(headers);
				    //System.out.println(lines);
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				       try{
				    	  
				    	PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				     //   String demographicproperties = demographicmap.get(data[0]);
				        
				            if(queryfield.equals("gender"))
				        	obj.setGender(data[0]);
				        
				            if(queryfield.equals("device"))
				        	obj.setDevice_type(data[0]);
				        	
				        	if(queryfield.equals("city")){
				        		try{
				        		String locationproperties = citycodeMap.get(data[0]);
						        data[0]=data[0].replace("_"," ").replace("-"," ");
						        data[0] = capitalizeString(data[0]);
						        obj.setCity(data[0]);
						        System.out.println(data[0]);
						        obj.setLocationcode(locationproperties);
				        		}
				        		catch(Exception e)
				        		{
				        			continue;
				        			
				        		}
				        		
				        		}
				        	
				        	if(queryfield.equals("audience_segment"))
				        	{
				        		String audienceSegment = audienceSegmentMap.get(data[0]);
				        		String audienceSegmentCode = audienceSegmentMap2.get(data[0]);
				        		if(audienceSegment!=null && !audienceSegment.isEmpty()){
				        		obj.setAudience_segment(audienceSegment);
				        		obj.setAudienceSegmentCode(audienceSegmentCode);
				        		}
				        		else
				        	    obj.setAudience_segment(data[0]);
				        		
				        		
				        	}
				        	
				        	if(queryfield.equals("reforiginal"))
					             obj.setReferrerSource(data[0]);
				            	
				        	if(queryfield.equals("agegroup"))
				        	{
				        		 data[0]=data[0].replace("_","-");
				        		 data[0]=data[0]+ " Years";
				        		 if(data[0].contains("medium")==false)
				        		 obj.setAge(data[0]);
				        	}
				            	
				            	
				        	if(queryfield.equals("incomelevel"))
					          obj.setIncomelevel(data[0]);
				    
				        	
				        	
				        	if(queryfield.equals("system_os")){
				        		String osproperties = oscodeMap.get(data[0]);
						        data[0]=data[0].replace("_"," ").replace("-", " ");
						        data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
						        String [] osParts = oscodeMap1.get(osproperties).split(",");
						        obj.setOs(osParts[0]);
						        obj.setOSversion(osParts[1]);
						        obj.setOscode(osproperties);
				        	}
				         	
				        	if(queryfield.equals("modelName")){
				        		String[] mobiledeviceproperties = devicecodeMap.get(data[0]).split(",");
					        	
						        obj.setMobile_device_model_name(mobiledeviceproperties[2]);
						        System.out.println(mobiledeviceproperties[2]);
						        obj.setDevicecode(mobiledeviceproperties[0]);
						        System.out.println(mobiledeviceproperties[0]);
				        	}
				         	
				        	if(queryfield.equals("brandName"))
					          {
				        		data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
				        		obj.setBrandname(data[0]);
					          }
				        
				        	
				        	if(queryfield.equals("refcurrentoriginal"))
				  	          {String articleparts[] = data[0].split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle); obj.setPublisher_pages(data[0]);}
				        	
				        	
				        	//   obj.setCode(code);
			            for(int k = 0; k < groupby.size(); k++)
			            {
			            	
			            	if(groupby.get(k).equals(queryfield)==false)
			            	{
			            	try{
			            	if(groupby.get(k).equals("device"))
			            	obj.setDevice_type(data[k+1]);
			            	
			            	if(groupby.get(k).equals("city"))
				            {
			            		try{
			            		String locationproperties = citycodeMap.get(data[k+1]);
			    		        data[k+1]=data[k+1].replace("_"," ").replace("-"," ");
			    		        data[k+1] = capitalizeString(data[k+1]);
			    		        obj.setCity(data[k+1]);
			    		        System.out.println(data[k+1]);
			    		        obj.setLocationcode(locationproperties);
			            		}
			            		catch(Exception e)
			            		{
			            			continue;
			            		}
			            		}
			            	
			            	if(groupby.get(k).equals("audience_segment")){
			            		
			            		String audienceSegment = audienceSegmentMap.get(data[k+1]);
			            		String audienceSegmentCode = audienceSegmentMap2.get(data[k+1]);
			            		if(audienceSegment!=null && !audienceSegment.isEmpty()){
			            		obj.setAudience_segment(audienceSegment);
			            		obj.setAudienceSegmentCode(audienceSegmentCode);
			            		}
			            		else
			            	    obj.setAudience_segment(data[k+1]);
			            	}
			            	
			            	if(groupby.get(k).equals("gender"))
					             obj.setGender(data[k+1]);
			            	
			            	if(groupby.get(k).equals("subcategory"))
			            	 {
			            		String audienceSegment = audienceSegmentMap.get(data[k+1]);
			            		String audienceSegmentCode = audienceSegmentMap2.get(data[k+1]);
			            		if(audienceSegment!=null && !audienceSegment.isEmpty()){
			            		obj.setSubcategory(audienceSegment);
			            		obj.setSubcategorycode(audienceSegmentCode);
			            		}
			            		else
			            	    obj.setSubcategory(data[k+1]);
					             }
			            	
			            	if(groupby.get(k).equals("date"))
					             obj.setDate(data[k+1]);
			            	
			            	if(groupby.get(k).equals("hour"))
					             obj.setDate(data[k+1]);
			            	
			            	if(groupby.get(k).equals("minute"))
					             obj.setDate(data[k+1]);	
			            	
			            	if(groupby.get(k).equals("refcurrentoriginal"))
					             obj.setGender(data[k+1]);
				            	
			            	if(groupby.get(k).equals("agegroup"))
			            	{
				        		 data[k+1]=data[k+1].replace("_","-");
				        		 data[k+1]=data[k+1]+ " Years";
				        		 if(data[k+1].contains("medium")==false)
				        		 obj.setAge(data[k+1]);
				        	}
				            	
				            	
			            	if(groupby.get(k).equals("incomelevel"))
					          obj.setIncomelevel(data[k+1]);
				            	
		                    l++;
			            	}
			            	catch(Exception e){
			            		continue;
			            	}
			            	
			            	}
			            }
				        
			            
			           
			            	            
			            if(l!=0)
				        obj.setCount(data[l+1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
				        pubreport.add(obj);
				        l=0;
				       }
				       catch(Exception e)
				       {
				    	   continue;
				       }
				       
				       }
				      //System.out.println(headers);
				      //System.out.println(lines);
				    }
				    return pubreport;
				  }
		  
		  public List<PublisherReport> getQueryFieldChannelSection(String queryfield,String startdate, String enddate, String channel_name,String sectionid)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query = "Select count(*),"+queryfield+" from enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionid+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield;
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    List<PublisherReport> pubreport = new ArrayList();
				    
				    //System.out.println(headers);
				    //System.out.println(lines);
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				       
				    	try{  
				    	  
				    	  PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				     //   String demographicproperties = demographicmap.get(data[0]);
				            
				            if(queryfield.equals("gender"))
				        	obj.setGender(data[0]);
				        
				            if(queryfield.equals("device"))
				        	obj.setDevice_type(data[0]);
				        	
				            
				            if(queryfield.equals("state"))
			            	{
			            	
			            	data[0]=data[0].replace("_", " ");
			            	data[0] = capitalizeString(data[0]);
			            	obj.setState(data[0]);
			            	}
			            
			            
			                if(queryfield.equals("country"))
			        	  {
			        	
			            	data[0]=data[0].replace("_", " ");
			            	 data[0] = capitalizeString(data[0]);
			            	obj.setCountry(data[0]);
			             	}
				            
				            
				        	if(queryfield.equals("city")){
				        		try{
				        		String locationproperties = citycodeMap.get(data[0]);
						        data[0]=data[0].replace("_"," ").replace("-"," ");
						        data[0] = capitalizeString(data[0]);
						        obj.setCity(data[0]);
						        System.out.println(data[0]);
						        obj.setLocationcode(locationproperties);
				        		}
				        		catch(Exception e)
				        		{
				        			continue;
				        		}
				        		
				        		}
				        	if(queryfield.equals("audience_segment"))
				        	{
				        		String audienceSegment = audienceSegmentMap.get(data[0]);
				        		String audienceSegmentCode = audienceSegmentMap2.get(data[0]);
				        		if(audienceSegment!=null && !audienceSegment.isEmpty()){
				        		obj.setAudience_segment(audienceSegment);
				        		obj.setAudienceSegmentCode(audienceSegmentCode);
				        		}
				        		else
				        	    obj.setAudience_segment(data[0]);
				        		
				        	}
				        	
				        	if(queryfield.equals("reforiginal"))
					             obj.setReferrerSource(data[0]);
				            	
				        	if(queryfield.equals("agegroup"))
				        	{ 
				        		data[0]=data[0].replace("_","-");
				        		data[0]=data[0]+ " Years";
				        		if(data[0].contains("medium")==false)
				        		obj.setAge(data[0]);
				        	}
				            	
				        	if(queryfield.equals("incomelevel"))
					          obj.setIncomelevel(data[0]);
				        
				        	
				        	if(queryfield.equals("ISP")){
				        		if(data[0].trim().toLowerCase().equals("_ltd")==false)
				        		{
				        			data[0]=data[0].replace("_"," ");
				        			obj.setISP(data[0]);
				            	}
				        	}	
				            if(queryfield.equals("organisation")){
				        
				            	if((!data[0].trim().toLowerCase().contains("broadband")) && (!data[0].trim().toLowerCase().contains("communication")) && (!data[0].trim().toLowerCase().contains("cable")) && (!data[0].trim().toLowerCase().contains("telecom")) && (!data[0].trim().toLowerCase().contains("network")) && (!data[0].trim().toLowerCase().contains("isp")) && (!data[0].trim().toLowerCase().contains("hathway")) && (!data[0].trim().toLowerCase().contains("internet")) && (!data[0].trim().toLowerCase().equals("_ltd")) && (!data[0].trim().toLowerCase().contains("googlebot")) && (!data[0].trim().toLowerCase().contains("sify")) && (!data[0].trim().toLowerCase().contains("bsnl")) && (!data[0].trim().toLowerCase().contains("reliance")) && (!data[0].trim().toLowerCase().contains("broadband")) && (!data[0].trim().toLowerCase().contains("tata")) && (!data[0].trim().toLowerCase().contains("nextra")))
				            	{
				            		data[0]=data[0].replace("_"," ");
				            		obj.setOrganisation(data[0]);
				            	}
				            
				            }
				        	
				        	if(queryfield.equals("screen_properties")){
				        		
				        		obj.setScreen_properties(data[0]);
				        		
				        	}
				            
				        	
				        	if(queryfield.equals("system_os")){
				        		String osproperties = oscodeMap.get(data[0]);
						        data[0]=data[0].replace("_"," ").replace("-", " ");
						        data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
						        String [] osParts = oscodeMap1.get(osproperties).split(",");
						        obj.setOs(osParts[0]);
						        obj.setOSversion(osParts[1]);
						        obj.setOscode(osproperties);
				        	}
				         	
				        	if(queryfield.equals("modelName")){
				        		String[] mobiledeviceproperties = devicecodeMap.get(data[0]).split(",");
					        	
						        obj.setMobile_device_model_name(mobiledeviceproperties[2]);
						        System.out.println(mobiledeviceproperties[2]);
						        obj.setDevicecode(mobiledeviceproperties[0]);
						        System.out.println(mobiledeviceproperties[0]);
				        
				        	}
				        	
				        	if(queryfield.equals("brandName")){
				        		data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
				        		obj.setBrandname(data[0]);
				        	}

				        	if(queryfield.equals("refcurrentoriginal"))
				  	          {String articleparts[] = data[0].split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle); obj.setPublisher_pages(data[0]);}
				        	
				        	

				            Random random = new Random();	
				            Integer randomNumber = random.nextInt(1000 + 1 - 500) + 500;
				            Integer max = (int)Double.parseDouble(data[1]);
				            Integer randomNumber1 = random.nextInt(max) + 1;
				            
				            if(queryfield.equals("audience_segment"))	
				            {
				            obj.setCount(data[1]); 	
				            obj.setExternalWorldCount(randomNumber.toString());	
				            obj.setVisitorCount(randomNumber1.toString());
				            obj.setAverageTime("0.0");	
				            String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
					        obj.setSection(sectionid);
					        pubreport.add(obj);
				            
				            }
				            
				            else if(queryfield.equals("agegroup")==true) {
				            	
				            	if(data[0].contains("medium")==false){
				            		obj.setCount(data[1]);
				            		String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
							        obj.setSection(sectionid);
							        pubreport.add(obj);
				            	}
				            }
				            
				            
				            else{
				            obj.setCount(data[1]);
				            String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
					        obj.setSection(sectionid);
					        pubreport.add(obj);
				            
				            }
				        
				    	}
				    	catch(Exception e)
				    	{
				    		continue;
				    	}
				    	
				    	}
				      
				      
				    	
				    	
				    	//System.out.println(headers);
				      //System.out.println(lines);
				    }
				   

				    if (queryfield.equals("LatLong")) {
				        
				    	  AggregationModule module =  AggregationModule.getInstance();
				    	    try {
				    			module.setUp();
				    		} catch (Exception e1) {
				    			// TODO Auto-generated catch block
				    			e1.printStackTrace();
				    		}
				  		pubreport=module.countLatLongChannelSection(startdate, enddate, channel_name, sectionid);
				  		return pubreport;
				    }
				    
				   
				    if (queryfield.equals("postalcode")) {
				        
				    	  AggregationModule module =  AggregationModule.getInstance();
				    	    try {
				    			module.setUp();
				    		} catch (Exception e1) {
				    			// TODO Auto-generated catch block
				    			e1.printStackTrace();
				    		}
				  		pubreport=module.countPinCodeChannelSection(startdate, enddate, channel_name, sectionid);
				  		return pubreport;
				    }
				    
				
				  if(queryfield.equals("visitorType")) { 
				    
				    
					  List<PublisherReport> pubreport1 = new ArrayList<PublisherReport>();
				      List<PublisherReport> pubreport2 = new ArrayList<PublisherReport>();
				      List<PublisherReport> pubreport3 = new ArrayList<PublisherReport>();
				       
					  
					  
				    	  AggregationModule module =  AggregationModule.getInstance();
				    	    try {
				    			module.setUp();
				    		} catch (Exception e1) {
				    			// TODO Auto-generated catch block
				    			e1.printStackTrace();
				    		}
						pubreport1=module.countNewUsersChannelSectionDatewise(startdate, enddate, channel_name, sectionid);
						
						pubreport2=module.countReturningUsersChannelSectionDatewise(startdate, enddate, channel_name, sectionid);
					
						pubreport3=module.countLoyalUsersChannelSectionDatewise(startdate, enddate, channel_name, sectionid);
				 		
						
						pubreport1.addAll(pubreport2);
						pubreport1.addAll(pubreport3);
						
						return pubreport1;
				    }
				    
				    
				  
				    if (queryfield.equals("totalViews")) {
				        
				   	 AggregationModule module =  AggregationModule.getInstance();
				 	    try {
				 			module.setUp();
				 		} catch (Exception e1) {
				 			// TODO Auto-generated catch block
				 			e1.printStackTrace();
				 		}
						pubreport=module.counttotalvisitorsChannelSection(startdate, enddate, channel_name, sectionid);
						return pubreport;
				   }
				    
				    if(queryfield.equals("engagementTime"))	
			        {
				    	AggregationModule module =  AggregationModule.getInstance();
				 	    try {
				 			module.setUp();
				 		} catch (Exception e1) {
				 			// TODO Auto-generated catch block
				 			e1.printStackTrace();
				 		}
						pubreport=module.engagementTimeChannelSection(startdate, enddate, channel_name, sectionid);
						return pubreport;
			        
			        }
			        	
			        	
			        if(queryfield.equals("minutesVisitor"))	
			        {
			        	pubreport.clear();
			        	PublisherReport obj1 = new PublisherReport();
			        	Random random = new Random();	
			            Integer randomNumber = random.nextInt(10) + 1;
			           String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj1.setChannelName(channel_name1);
			            obj1.setSection(sectionid);
			            obj1.setMinutesperVisitor(randomNumber.toString());
			        	pubreport.add(obj1);
			            return pubreport;
			        }
				    
				    
				    
				    
				    if (queryfield.equals("totalViewsDatewise")) {
				        
					   	 AggregationModule module =  AggregationModule.getInstance();
					 	    try {
					 			module.setUp();
					 		} catch (Exception e1) {
					 			// TODO Auto-generated catch block
					 			e1.printStackTrace();
					 		}
							pubreport=module.counttotalvisitorsChannelSectionDatewise(startdate, enddate, channel_name, sectionid);
							return pubreport;
					   }
					    
				    
				    if (queryfield.equals("totalViewsHourwise")) {
				        
					   	 AggregationModule module =  AggregationModule.getInstance();
					 	    try {
					 			module.setUp();
					 		} catch (Exception e1) {
					 			// TODO Auto-generated catch block
					 			e1.printStackTrace();
					 		}
							pubreport=module.counttotalvisitorsChannelSectionDateHourwise(startdate, enddate, channel_name, sectionid);
							return pubreport;
					   }
					    
				    
				    
				    if (queryfield.equals("uniqueVisitors")) {
				        
				      	 AggregationModule module =  AggregationModule.getInstance();
				    	    try {
				    			module.setUp();
				    		} catch (Exception e1) {
				    			// TODO Auto-generated catch block
				    			e1.printStackTrace();
				    		}
				   		pubreport=module.countfingerprintChannelSection(startdate, enddate, channel_name, sectionid);
				   		return pubreport; 
				      }
				    
					
				    if (queryfield.equals("uniqueVisitorsDatewise")) {
				        
				      	 AggregationModule module =  AggregationModule.getInstance();
				    	    try {
				    			module.setUp();
				    		} catch (Exception e1) {
				    			// TODO Auto-generated catch block
				    			e1.printStackTrace();
				    		}
				   		pubreport=module.countfingerprintChannelSectionDatewise(startdate, enddate, channel_name, sectionid);
				   		return pubreport;
				      }
				    
				    
				    
				    if (queryfield.equals("uniqueVisitorsHourwise")) {
				        
				      	 AggregationModule module =  AggregationModule.getInstance();
				    	    try {
				    			module.setUp();
				    		} catch (Exception e1) {
				    			// TODO Auto-generated catch block
				    			e1.printStackTrace();
				    		}
				   		pubreport=module.countfingerprintChannelSectionDateHourwise(startdate, enddate, channel_name, sectionid);
				   		return pubreport;
				      }
				    
				    
				    
				    
				    if(queryfield.equals("engagementTimeDatewise"))	
			           {
			        	   
			        	   
			        	   AggregationModule module =  AggregationModule.getInstance();
			       	    try {
			       			module.setUp();
			       		} catch (Exception e1) {
			       			// TODO Auto-generated catch block
			       			e1.printStackTrace();
			       		}
			      		pubreport=module.engagementTimeChannelSectionDatewise(startdate, enddate, channel_name, sectionid);
			      		return pubreport;
			           
			           }
			            
				    

				    if(queryfield.equals("engagementTimeHourwise"))	
			           {
			        	   
			        	   
			        	   AggregationModule module =  AggregationModule.getInstance();
			       	    try {
			       			module.setUp();
			       		} catch (Exception e1) {
			       			// TODO Auto-generated catch block
			       			e1.printStackTrace();
			       		}
			      		pubreport=module.engagementTimeChannelSectionDateHourwise(startdate, enddate, channel_name, sectionid);
			      		return pubreport;
			           
			           }
			            
				    
				    
				    
				    
			           if (queryfield.equals("reforiginal")) {

			        	   String data0= null;
			               String data1= null;  
			               String data2= null;
			        	   pubreport.clear();
			        	   
						for (int i = 0; i < 5; i++) {
							PublisherReport obj = new PublisherReport();

							if (i == 0) {
								data0 = "http://m.facebook.com";
								data1 = "726.0";
							    data2 = "Social";
							}

							if (i == 1) {
								data0 = "http://www.facebook.com";
								data1 = "500.0";
							    data2 = "Social";
							}

							if (i == 2) {
								data0 = "http://l.facebook.com";
								data1 = "303.0";
							    data2 = "Social";
							}

							if (i == 3) {
								data0 = "http://www.google.co.pk";
								data1 = "504.0";
							    data2 = "Search";
							}

							if (i == 4) {
								data0 = "http://www.google.co.in";
								data1 = "700.0";
							    data2 = "Search";
							}

							obj.setReferrerSource(data0);
							obj.setReferrerType(data2);
							obj.setCount(data1);
							String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
							obj.setSection(sectionid);
							pubreport.add(obj);

						}

					}
			    
			          /* 
			           
			           if (queryfield.equals("device")) {

			        	   String data0= null;
			               String data1= null;   
			        	   pubreport.clear();
			        	   
			        	   for (int i = 0; i < 3; i++)
						      {
						        PublisherReport obj = new PublisherReport();
						        
						        
						       
						          //if(data1[0].equals()) 
						         
						          if(i == 0){
						          data0="Mobile";
						          data1 = "1067.0";
						          }
						          

						          if(i == 1){
						          data0="Tablet";
						          data1 = "305.0";
						          }
						          
						          
						          if(i == 2){
							          data0="Desktop";
							          data1 = "743.0";
							      }
							    
						        
						          obj.setDevice_type(data0);
						          obj.setCount(data1);
						          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);			        
						          obj.setSection(sectionid);
						          pubreport.add(obj);
						        
						   //   }
						    //  System.out.println(headers1);
						    //  System.out.println(lines1);
						   }
			    
			           }
			    
			           if (queryfield.equals("incomelevel")) {

			        	   String data0= null;
			               String data1= null;   
			        	   pubreport.clear();
			           
			           for (int i = 0; i < 3; i++)
					      {
					        PublisherReport obj = new PublisherReport();
					        
					       // String[] data1 = ((String)lines1.get(i)).split(",");
					       
					          //if(data1[0].equals()) 
					         
					          if(i == 0){
					          data0="Medium";
					          data1 = "700.0";
					          }
					          

					          if(i == 1){
					          data0="High";
					          data1 = "904.0";
					          }
					          
					          
					          if(i == 2){
						          data0="Low";
						          data1 = "67.0";
						      }
						    
					        
					          obj.setIncomelevel(data0);
					          obj.setCount(data1);
					          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
					          obj.setSection(sectionid);
					          pubreport.add(obj);
					        
					   //   }
					    //  System.out.println(headers1);
					    //  System.out.println(lines1);
					      }
			           }
			         
			           */
			           
			           if (queryfield.equals("referrerType")) {

			        	   String data0= null;
			               String data1= null;   
			        	   pubreport.clear();
			           
			           for (int i = 0; i < 3; i++)
					      {
					        PublisherReport obj = new PublisherReport();
					        
					       // String[] data1 = ((String)lines1.get(i)).split(",");
					       
					          //if(data1[0].equals()) 
					         
					          if(i == 0){
					          data0="Social";
					          data1 = "806.0";
					          }
					          

					          if(i == 1){
					          data0="Search";
					          data1 = "1077.0";
					          }
					          
					          
					          if(i == 2){
						          data0="Direct";
						          data1 = "115.0";
						      }
						    
					        
					          obj.setReferrerSource(data0);
					          obj.setCount(data1);
					          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
					          obj.setSection(sectionid);
					          pubreport.add(obj);
					        
					   //   }
					    //  System.out.println(headers1);
					    //  System.out.println(lines1);
					      }
			           }       
				    
				    
				    
				    return pubreport;
				  }
				  
				  
				  
				  public List<PublisherReport> getQueryFieldChannelSectionFilter(String queryfield,String startdate, String enddate, String channel_name, String sectionname, Map<String,String>filter)
						    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
						  {
						    
					        int size = filter.size();
					        String queryfilterbuilder = "";
					        String formattedString = "";
					        int j =0;
					        for (Map.Entry<String, String> entry : filter.entrySet())
					        {
					        	if (j==0){
					                formattedString = addCommaString(entry.getValue());
					        		queryfilterbuilder = queryfilterbuilder+ entry.getKey() + " in " + "("+formattedString+")";
					        	
					        	}
					            else{
					            formattedString = addCommaString(entry.getValue());	
					            queryfilterbuilder = queryfilterbuilder+ " and "+ entry.getKey() + " in " + "("+formattedString+")";
					       
					            }
					            j++;
					         
					        }
					  
					  
					        
					        String query = "Select count(*),"+queryfield+" from enhanceduserdatabeta1 where "+queryfilterbuilder+" and refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield;
						    System.out.println(query);
					        CSVResult csvResult = getCsvResult(false, query);
						    List<String> headers = csvResult.getHeaders();
						    List<String> lines = csvResult.getLines();
						    List<PublisherReport> pubreport = new ArrayList();
						    
						    //System.out.println(headers);
						    //System.out.println(lines);
						    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
						    {
						      for (int i = 0; i < lines.size(); i++)
						      {
						        try{
						    	  
						    	  PublisherReport obj = new PublisherReport();
						        
						        String[] data = ((String)lines.get(i)).split(",");
						     //   String demographicproperties = demographicmap.get(data[0]);
						            if(queryfield.equals("gender"))
						        	obj.setGender(data[0]);
						        
						            if(queryfield.equals("device"))
						        	obj.setDevice_type(data[0]);
						        	
						        	if(queryfield.equals("city")){
						        	    try{
						        		String locationproperties = citycodeMap.get(data[0]);
								        data[0]=data[0].replace("_"," ").replace("-"," ");
								        data[0] = capitalizeString(data[0]);
								        obj.setCity(data[0]);
								        System.out.println(data[0]);
								        obj.setLocationcode(locationproperties);
						        	    }
						        	    catch(Exception e)
						        	    {
						        	    	continue;
						        	    }
						        	    
						        	    }
						        	if(queryfield.equals("audience_segment"))
						             {
						        		String audienceSegment = audienceSegmentMap.get(data[0]);
						        		String audienceSegmentCode = audienceSegmentMap2.get(data[0]);
						        		if(audienceSegment!=null && !audienceSegment.isEmpty()){
						        		obj.setAudience_segment(audienceSegment);
						        		obj.setAudienceSegmentCode(audienceSegmentCode);
						        		}
						        		else
						        	    obj.setAudience_segment(data[0]);
						        		
						             }
						        	
						        	if(queryfield.equals("reforiginal"))
							             obj.setReferrerSource(data[0]);
						            	
						        	if(queryfield.equals("agegroup"))
						        	{
						        		 data[0]=data[0].replace("_","-");
						        		 data[0]=data[0]+ " Years";
						        		 if(data[0].contains("medium")==false)
						        		 obj.setAge(data[0]);
						        	}
						            	
						            	
						        	if(queryfield.equals("incomelevel"))
							          obj.setIncomelevel(data[0]);
						     
						        	
						        	if(queryfield.equals("system_os")){
						        		String osproperties = oscodeMap.get(data[0]);
								        data[0]=data[0].replace("_"," ").replace("-", " ");
								        data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
								        String [] osParts = oscodeMap1.get(osproperties).split(",");
								        obj.setOs(osParts[0]);
								        obj.setOSversion(osParts[1]);
								        obj.setOscode(osproperties);
						        	}
						         	
						        	if(queryfield.equals("modelName")){
						        		String[] mobiledeviceproperties = devicecodeMap.get(data[0]).split(",");
							        	
								        obj.setMobile_device_model_name(mobiledeviceproperties[2]);
								        System.out.println(mobiledeviceproperties[2]);
								        obj.setDevicecode(mobiledeviceproperties[0]);
								        System.out.println(mobiledeviceproperties[0]);
						        	}
						         	
						        	if(queryfield.equals("brandName")){
						        		 data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
						        		obj.setBrandname(data[0]);
						        	}
                                 
						        	if(queryfield.equals("refcurrentoriginal"))
						  	          {String articleparts[] = data[0].split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle); obj.setPublisher_pages(data[0]);}
						        	

						            Random random = new Random();	
						            Integer randomNumber = random.nextInt(1000 + 1 - 500) + 500;
						            Integer max = (int)Double.parseDouble(data[1]);
						            Integer randomNumber1 = random.nextInt(max) + 1;
						            
						            if(queryfield.equals("audience_segment"))	
						            {
						            obj.setCount(data[1]); 	
						            obj.setExternalWorldCount(randomNumber.toString());	
						            obj.setVisitorCount(randomNumber1.toString());
						            obj.setAverageTime("0.0");	

							        obj.setSection(sectionname);
							        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
							        pubreport.add(obj);
						            }
						            
						            else if(queryfield.equals("agegroup")==true) {
						            	
						            	if(data[0].contains("medium")==false){
						            		obj.setCount(data[1]);

									         obj.setSection(sectionname);
									        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
									        pubreport.add(obj);
						            	}
						            }
						            
						            
						            else{
						            obj.setCount(data[1]);

							         obj.setSection(sectionname);
							        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
							        pubreport.add(obj);
						            
						            }
						       
						        }
						        catch(Exception e){
						        	continue;
						        	
						        }
						        
						        
						        }
						      //System.out.println(headers);
						      //System.out.println(lines);
						    }
						    return pubreport;
						  }
				  

				
				  public List<PublisherReport> getQueryFieldChannelSectionGroupBy(String queryfield,String startdate, String enddate, String channel_name, String sectionname,List<String> groupby)
						    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
						  {
						    
					      
				            String querygroupbybuilder = convert(groupby);
				            
				            String query = "";
				            
				            int  l=0;
				            
				         	query = "Select count(*),"+queryfield+","+querygroupbybuilder+" from enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield+","+querygroupbybuilder;
						   
				       	     if(querygroupbybuilder.equals("hour"))
				 		     query = "Select count(*),"+queryfield+" from enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield+","+"date_histogram(field='request_time','interval'='1h')";
				 		    
				       	  if(querygroupbybuilder.equals("minute"))
					 		     query = "Select count(*),"+queryfield+" from enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY "+queryfield+","+"date_histogram(field='request_time','interval'='1m')";
				         	
				         	System.out.println(query);
				         	CSVResult csvResult = getCsvResult(false, query);
						    List<String> headers = csvResult.getHeaders();
						    List<String> lines = csvResult.getLines();
						    List<PublisherReport> pubreport = new ArrayList();
						    
						    //System.out.println(headers);
						    //System.out.println(lines);
						    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
						    {
						      for (int i = 0; i < lines.size(); i++)
						      {
						        try{
						    	  
						    	PublisherReport obj = new PublisherReport();
						        
						        String[] data = ((String)lines.get(i)).split(",");
						     //   String demographicproperties = demographicmap.get(data[0]);
						        
						            if(queryfield.equals("gender"))
						        	obj.setGender(data[0]);
						        
						            if(queryfield.equals("device"))
						        	obj.setDevice_type(data[0]);
						        	
						        	if(queryfield.equals("city")){
						        		try{
						        		String locationproperties = citycodeMap.get(data[0]);
								        data[0]=data[0].replace("_"," ").replace("-"," ");
								        data[0] = capitalizeString(data[0]);
								        obj.setCity(data[0]);
								        System.out.println(data[0]);
								        obj.setLocationcode(locationproperties);
						        		}
						        		catch(Exception e)
						        		{
						        			continue;
						        		}
						        		}
						        	
						        	if(queryfield.equals("audience_segment"))
						        	{
						        		
						        		String audienceSegment = audienceSegmentMap.get(data[0]);
						        		String audienceSegmentCode = audienceSegmentMap2.get(data[0]);
						        		if(audienceSegment!=null && !audienceSegment.isEmpty()){
						        		obj.setAudience_segment(audienceSegment);
						        		obj.setAudienceSegmentCode(audienceSegmentCode);
						        		}
						        		else
						        	    obj.setAudience_segment(data[0]);
						        		
						        		
						        	}
						        	
						        	if(queryfield.equals("reforiginal"))
							             obj.setReferrerSource(data[0]);
						            	
						        	if(queryfield.equals("agegroup"))
						        	{
						        		 data[0]=data[0].replace("_","-");
						        		 data[0]=data[0]+ " Years";
						        		 if(data[0].contains("medium")==false)
						        		 obj.setAge(data[0]);
						        	}
						            	
						            	
						        	
										        	
						        	if(queryfield.equals("incomelevel"))
							          obj.setIncomelevel(data[0]);
						     
						        	
						        	if(queryfield.equals("system_os")){
						        		String osproperties = oscodeMap.get(data[0]);
								        data[0]=data[0].replace("_"," ").replace("-", " ");
								        data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
								        String [] osParts = oscodeMap1.get(osproperties).split(",");
								        obj.setOs(osParts[0]);
								        obj.setOSversion(osParts[1]);
								        obj.setOscode(osproperties);
						        	}
						         	
						        	if(queryfield.equals("modelName")){
						        		String[] mobiledeviceproperties = devicecodeMap.get(data[0]).split(",");
							        	
								        obj.setMobile_device_model_name(mobiledeviceproperties[2]);
								        System.out.println(mobiledeviceproperties[2]);
								        obj.setDevicecode(mobiledeviceproperties[0]);
								        System.out.println(mobiledeviceproperties[0]);
						        	}
						         	
						        	if(queryfield.equals("brandName")){
						        		 data[0]= AggregationModule.capitalizeFirstLetter(data[0]);
						        		obj.setBrandname(data[0]);
						        	}

						        	if(queryfield.equals("refcurrentoriginal"))
						  	          {String articleparts[] = data[0].split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle); obj.setPublisher_pages(data[0]);}
						        	
						        	
						        	
						        	
						        	//   obj.setCode(code);
					            for(int k = 0; k < groupby.size(); k++)
					            {
					            	
					            	if(groupby.get(k).equals(queryfield)==false)
					            	{
					            	try{
					            	if(groupby.get(k).equals("device"))
					            	obj.setDevice_type(data[k+1]);
					            	
					            	if(groupby.get(k).equals("city"))
					            	{
					            		try{
					            		String locationproperties = citycodeMap.get(data[k+1]);
					    		        data[k+1]=data[k+1].replace("_"," ").replace("-"," ");
					    		        data[k+1] = capitalizeString(data[k+1]);
					    		        obj.setCity(data[k+1]);
					    		        System.out.println(data[k+1]);
					    		        obj.setLocationcode(locationproperties);
					            		}
					            		catch(Exception e)
					            		{
					            			continue;
					            		}
					            	}
					            	
					            	if(groupby.get(k).equals("audience_segment"))
					            	{
					            		
					            		String audienceSegment = audienceSegmentMap.get(data[k+1]);
					            		String audienceSegmentCode = audienceSegmentMap2.get(data[k+1]);
					            		if(audienceSegment!=null && !audienceSegment.isEmpty()){
					            		obj.setAudience_segment(audienceSegment);
					            		obj.setAudienceSegmentCode(audienceSegmentCode);
					            		}
					            		else
					            	    obj.setAudience_segment(data[k+1]);
					            		
					            	}
					            	
					            	
					            	if(groupby.get(k).equals("gender"))
							             obj.setGender(data[k+1]);
					            	
					            	
					            	if(groupby.get(k).equals("date"))
							             obj.setDate(data[k+1]);
						            	
					            	if(groupby.get(k).equals("hour"))
							             obj.setDate(data[k+1]);
					            	
					            	if(groupby.get(k).equals("minute"))
							             obj.setDate(data[k+1]);
					            	
					            	if(groupby.get(k).equals("subcategory"))
					            	 {
					            		String audienceSegment = audienceSegmentMap.get(data[k+1]);
					            		String audienceSegmentCode = audienceSegmentMap2.get(data[k+1]);
					            		if(audienceSegment!=null && !audienceSegment.isEmpty()){
					            		obj.setSubcategory(audienceSegment);
					            		obj.setSubcategorycode(audienceSegmentCode);
					            		}
					            		else
					            	    obj.setSubcategory(data[k+1]);
							             }
					            	
					            	if(groupby.get(k).equals("refcurrentoriginal"))
							             obj.setReferrerSource(data[k+1]);
						            	
					            	if(groupby.get(k).equals("agegroup"))
					            	{
						        		 data[k+1]=data[k+1].replace("_","-");
						        		 data[k+1]=data[k+1]+ " Years";
						        		 if(data[k+1].contains("medium")==false)
						        		 obj.setAge(data[k+1]);
						        	}
						            	
						            	
					            	if(groupby.get(k).equals("incomelevel"))
							          obj.setIncomelevel(data[k+1]);
						            	
				                    l++;
					            	}
					            	catch(Exception e){
					            	continue;
					            	}
					            	}
					            }
						        
					            
					            	            
					            if(l!=0)
					            obj.setCount(data[l+1]);
					            
						        obj.setSection(sectionname);
						        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
						        pubreport.add(obj);
						        l=0;
						        }
						        catch(Exception e)
						        {
						        	continue;
						        }
						        
						        }
						      //System.out.println(headers);
						      //System.out.println(lines);
						    }
						    return pubreport;
						  }
				  
				  	  
  
  
  public List<PublisherReport> getAgegroupChannel(String startdate, String enddate, String channel_name)
    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
  {
    String query = "Select count(*),agegroup from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY agegroup";
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
    {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
    //    String demographicproperties = demographicmap.get(data[0]);
        obj.setAge(data[0]);
  //      obj.setCode(code);
        obj.setCount(data[1]);
        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
        pubreport.add(obj);
      }
      //System.out.println(headers);
      //System.out.println(lines);
    }
    return pubreport;
  }
  
  public List<PublisherReport> getISPChannel(String startdate, String enddate, String channel_name)
    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
  {
    String query = "Select count(*),ISP from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY ISP";
    CSVResult csvResult = getCsvResult(false, query);
    List<String> headers = csvResult.getHeaders();
    List<String> lines = csvResult.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
    {
      for (int i = 0; i < lines.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data = ((String)lines.get(i)).split(",");
        if(data[0].trim().toLowerCase().equals("_ltd")==false){ 
        obj.setISP(data[0]);
        obj.setCount(data[1]);
        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
        pubreport.add(obj);
         }
        }
      //System.out.println(headers);
      //System.out.println(lines);
    }
    return pubreport;
  }
  
  public List<PublisherReport> getOrgChannel(String startdate, String enddate, String channel_name)
    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
  {
    String query1 = "Select count(*),organisation from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY organisation";
    CSVResult csvResult1 = getCsvResult(false, query1);
    List<String> headers1 = csvResult1.getHeaders();
    List<String> lines1 = csvResult1.getLines();
    List<PublisherReport> pubreport = new ArrayList();
    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
    {
      for (int i = 0; i < lines1.size(); i++)
      {
        PublisherReport obj = new PublisherReport();
        
        String[] data1 = ((String)lines1.get(i)).split(",");
        if ((data1[0].length() > 3) && (data1[0].charAt(0) != '_') && (!data1[0].trim().toLowerCase().contains("broadband")) && (!data1[0].trim().toLowerCase().contains("communication")) && (!data1[0].trim().toLowerCase().contains("cable")) && (!data1[0].trim().toLowerCase().contains("telecom")) && (!data1[0].trim().toLowerCase().contains("network")) && (!data1[0].trim().toLowerCase().contains("isp")) && (!data1[0].trim().toLowerCase().contains("hathway")) && (!data1[0].trim().toLowerCase().contains("internet")) && (!data1[0].trim().toLowerCase().equals("_ltd")) && (!data1[0].trim().toLowerCase().contains("googlebot")) && (!data1[0].trim().toLowerCase().contains("sify")) && (!data1[0].trim().toLowerCase().contains("bsnl")) && (!data1[0].trim().toLowerCase().contains("reliance")) && (!data1[0].trim().toLowerCase().contains("broadband")) && (!data1[0].trim().toLowerCase().contains("tata")) && (!data1[0].trim().toLowerCase().contains("nextra")))
        {
          obj.setOrganisation(data1[0]);
          obj.setCount(data1[1]);
          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
          pubreport.add(obj);
        }
      }
      //System.out.println(headers1);
      //System.out.println(lines1);
    }
    return pubreport;
  }
  
  
  
  public List<PublisherReport> countBrandNameChannelArticle(String startdate, String enddate, String channel_name, String articlename)
		    throws CsvExtractorException, Exception
		  {
		    String query = "SELECT COUNT(*)as count,brandName FROM enhanceduserdatabeta1 where refcurrentoriginal= '"+articlename+"' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by brandName";
		    //System.out.println(query);
		    CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    System.out.println(headers);
		    System.out.println(lines);
		    List<PublisherReport> pubreport = new ArrayList();
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
		    {
		      for (int i = 0; i < lines.size(); i++)
		      {
		        PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");
		        if(data[0].trim().toLowerCase().contains("logitech")==false && data[0].trim().toLowerCase().contains("mozilla")==false && data[0].trim().toLowerCase().contains("web_browser")==false && data[0].trim().toLowerCase().contains("microsoft")==false && data[0].trim().toLowerCase().contains("opera")==false && data[0].trim().toLowerCase().contains("epiphany")==false){ 
		        obj.setBrandname(data[0]);
		        obj.setCount(data[1]);
		        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
		        pubreport.add(obj);
		        } 
		       }
		  //    //System.out.println(headers);
		  //    //System.out.println(lines);
		    }
		    return pubreport;
		  }
		  
		  public List<PublisherReport> countBrowserChannelArticle(String startdate, String enddate, String channel_name, String articlename)
		    throws CsvExtractorException, Exception
		  {
		    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
		    String query = "SELECT COUNT(*)as count,browser_name FROM enhanceduserdatabeta1 where channel_name ='" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by browser_name";
		    CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    System.out.println(headers);
		    System.out.println(lines);
		    List<PublisherReport> pubreport = new ArrayList();
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
		    {
		      for (int i = 0; i < lines.size(); i++)
		      {
		        PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");
		        obj.setBrowser(data[0]);
		        obj.setCount(data[1]);
		        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
		        pubreport.add(obj);
		      }
		      //System.out.println(headers);
		      //System.out.println(lines);
		    }
		    return pubreport;
		  }
		  
		  public List<PublisherReport> countOSChannelArticle(String startdate, String enddate, String channel_name, String articlename)
		    throws CsvExtractorException, Exception
		  {
		    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
		    String query = "SELECT COUNT(*)as count,system_os FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by system_os";
		    System.out.println(query);
		    CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    System.out.println(headers);
		    System.out.println(lines);
		    List<PublisherReport> pubreport = new ArrayList();
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
		      for (int i = 0; i < lines.size(); i++)
		      {
		        PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");
		        String osproperties = oscodeMap.get(data[0]);
		        data[0]=data[0].replace("_"," ").replace("-", " ");
		        obj.setOs(data[0]);
		        obj.setOscode(osproperties);
		        System.out.println(data[0]);
		        obj.setCount(data[1]);
		        System.out.println(osproperties);
		        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
		        pubreport.add(obj);
		      }
		    }
		    
		    return pubreport;
		  }
		  
		  public List<PublisherReport> countModelChannelArticle(String startdate, String enddate, String channel_name, String articlename)
		    throws CsvExtractorException, Exception
		  {
		    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
		    String query = "SELECT COUNT(*)as count,modelName FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by modelName";
		    CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    System.out.println(headers);
		    System.out.println(lines);
		    List<PublisherReport> pubreport = new ArrayList();
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
		      for (int i = 0; i < lines.size(); i++)
		      {
		        PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");

		        if(data[0].trim().toLowerCase().contains("logitech_revue")==false && data[0].trim().toLowerCase().contains("mozilla_firefox")==false && data[0].trim().toLowerCase().contains("apple_safari")==false && data[0].trim().toLowerCase().contains("generic_web")==false && data[0].trim().toLowerCase().contains("google_compute")==false && data[0].trim().toLowerCase().contains("microsoft_xbox")==false && data[0].trim().toLowerCase().contains("google_chromecast")==false && data[0].trim().toLowerCase().contains("opera")==false && data[0].trim().toLowerCase().contains("epiphany")==false && data[0].trim().toLowerCase().contains("laptop")==false){    
		        String[] mobiledeviceproperties = devicecodeMap.get(data[0]).split(",");
		        	
		        obj.setMobile_device_model_name(mobiledeviceproperties[1]);
		        System.out.println(mobiledeviceproperties[1]);
		        obj.setDevicecode(mobiledeviceproperties[0]);
		        System.out.println(mobiledeviceproperties[0]);
		        obj.setCount(data[1]);
		        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
		        pubreport.add(obj);
		      }
		        
		        }
		    }
		  
		    System.out.println(pubreport.toString());
		    return pubreport;
		  }
		  
		  public List<PublisherReport> countCityChannelArticle(String startdate, String enddate, String channel_name, String articlename)
		    throws CsvExtractorException, Exception
		  {
		    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
		    String query = "SELECT COUNT(*)as count,city FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by city";
		    CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    System.out.println(headers);
		    System.out.println(lines);
		    
		    
		    List<PublisherReport> pubreport = new ArrayList();
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
		      for (int i = 0; i < lines.size(); i++)
		      {
		        PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");
		        String locationproperties = citycodeMap.get(data[0]);
		        data[0]=data[0].replace("_"," ").replace("-"," ");
		        obj.setCity(data[0]);
		        System.out.println(data[0]);
		        obj.setLocationcode(locationproperties);
		        System.out.println(locationproperties);
		        obj.setCount(data[1]);
		        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
		        pubreport.add(obj);
		      }
		    }
		   
		    System.out.println(pubreport.toString());
		    return pubreport;
		  }
		  
		  public List<PublisherReport> countfingerprintChannelArticle(String startdate, String enddate, String channel_name, String articlename)
		    throws CsvExtractorException, Exception
		  {
			  
			  
		//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
			  
		    
			  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal= '"+articlename+"' and channel_name = '" + 
				      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
			  
			//	 CSVResult csvResult00 = getCsvResult(false, query00);
				// List<String> headers00 = csvResult00.getHeaders();
		//		 List<String> lines00 = csvResult00.getLines();
			//	 List<PublisherReport> pubreport00 = new ArrayList();  
				
				 
			//	System.out.println(headers00);
			//	System.out.println(lines00);  
				  
				//  for (int i = 0; i < lines00.size(); i++)
			    //  {
			       
			     //   String[] data = ((String)lines00.get(i)).split(",");
			  //      //System.out.println(data[0]);
			     
				  
				  
				  
				Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
			    String query = "SELECT count(distinct(cookie_id))as reach FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + 
			      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'";
			      CSVResult csvResult = getCsvResult(false, query);
			      List<String> headers = csvResult.getHeaders();
			      List<String> lines = csvResult.getLines();
			      List<PublisherReport> pubreport = new ArrayList();
			      System.out.println(headers);
			      System.out.println(lines);
			      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
			      for (int i = 0; i < lines.size(); i++)
			      {
			        PublisherReport obj = new PublisherReport();
			        
			        String[] data = ((String)lines.get(i)).split(",");
			       // obj.setDate(data[0]);
			        obj.setReach(data[0]);
			        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
			        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
			        pubreport.add(obj);
			      }
			    }  
			    
		    return pubreport;
		  }
		  
	
		  
		  public List<PublisherReport> countfingerprintChannelArticleDatewise(String startdate, String enddate, String channel_name, String articlename)
				    throws CsvExtractorException, Exception
				  {
					  
					  
				//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
					  
				    
					  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal= '"+articlename+"' and channel_name = '" + 
						      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
					  
					//	 CSVResult csvResult00 = getCsvResult(false, query00);
						// List<String> headers00 = csvResult00.getHeaders();
				//		 List<String> lines00 = csvResult00.getLines();
					//	 List<PublisherReport> pubreport00 = new ArrayList();  
						
						 
					//	System.out.println(headers00);
					//	System.out.println(lines00);  
						  
						//  for (int i = 0; i < lines00.size(); i++)
					    //  {
					       
					     //   String[] data = ((String)lines00.get(i)).split(",");
					  //      //System.out.println(data[0]);
					     
						  
						  
						  
						Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
					    String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + 
					      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
					      CSVResult csvResult = getCsvResult(false, query);
					      List<String> headers = csvResult.getHeaders();
					      List<String> lines = csvResult.getLines();
					      List<PublisherReport> pubreport = new ArrayList();
					      System.out.println(headers);
					      System.out.println(lines);
					      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
					      for (int i = 0; i < lines.size(); i++)
					      {
					        PublisherReport obj = new PublisherReport();
					        
					        String[] data = ((String)lines.get(i)).split(",");
					        obj.setDate(data[0]);
					        obj.setReach(data[1]);
					        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
					        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
					        pubreport.add(obj);
					      }
					    }  
					    
				    return pubreport;
				  }
		  
		 
		  
		  public List<PublisherReport> countfingerprintChannelArticleHourwise(String startdate, String enddate, String channel_name, String articlename)
				    throws CsvExtractorException, Exception
				  {
					  
					  
				//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
					  
				    
					  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal= '"+articlename+"' and channel_name = '" + 
						      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
					  
					//	 CSVResult csvResult00 = getCsvResult(false, query00);
						// List<String> headers00 = csvResult00.getHeaders();
				//		 List<String> lines00 = csvResult00.getLines();
					//	 List<PublisherReport> pubreport00 = new ArrayList();  
						
						 
					//	System.out.println(headers00);
					//	System.out.println(lines00);  
						  
						//  for (int i = 0; i < lines00.size(); i++)
					    //  {
					       
					     //   String[] data = ((String)lines00.get(i)).split(",");
					  //      //System.out.println(data[0]);
					     
						  
						  
						  
						Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
					    String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + 
					      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1h')";
					      CSVResult csvResult = getCsvResult(false, query);
					      List<String> headers = csvResult.getHeaders();
					      List<String> lines = csvResult.getLines();
					      List<PublisherReport> pubreport = new ArrayList();
					      System.out.println(headers);
					      System.out.println(lines);
					      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
					      for (int i = 0; i < lines.size(); i++)
					      {
					        PublisherReport obj = new PublisherReport();
					        
					        String[] data = ((String)lines.get(i)).split(",");
					        obj.setDate(data[0]);
					        obj.setReach(data[1]);
					        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
					        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
					        pubreport.add(obj);
					      }
					    }  
					    
				    return pubreport;
				  }
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  public List<PublisherReport> counttotalvisitorsChannelArticle(String startdate, String enddate, String channel_name, String articlename)
				    throws CsvExtractorException, Exception
				  {
					  
					  
				//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
					  
				    
					  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + 
						      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
					  
					//	 CSVResult csvResult00 = getCsvResult(false, query00);
						// List<String> headers00 = csvResult00.getHeaders();
				//		 List<String> lines00 = csvResult00.getLines();
					//	 List<PublisherReport> pubreport00 = new ArrayList();  
						
						 
					//	System.out.println(headers00);
					//	System.out.println(lines00);  
						  
						//  for (int i = 0; i < lines00.size(); i++)
					    //  {
					       
					     //   String[] data = ((String)lines00.get(i)).split(",");
					  //      //System.out.println(data[0]);
					     
						  
						  
						  
						Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
					    String query = "SELECT count(*) as visits FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + 
					      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'";
					      CSVResult csvResult = getCsvResult(false, query);
					      List<String> headers = csvResult.getHeaders();
					      List<String> lines = csvResult.getLines();
					      List<PublisherReport> pubreport = new ArrayList();
					      System.out.println(headers);
					      System.out.println(lines);
					      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
					      for (int i = 0; i < lines.size(); i++)
					      {
					        PublisherReport obj = new PublisherReport();
					        
					        String[] data = ((String)lines.get(i)).split(",");
					       // obj.setDate(data[0]);
					        obj.setTotalvisits(data[0]);
					        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
					        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
					        pubreport.add(obj);
					      }
					    }  
					    
				    return pubreport;
				  }
				  
			
				  
				  public List<PublisherReport> counttotalvisitorsChannelArticleDatewise(String startdate, String enddate, String channel_name, String articlename)
						    throws CsvExtractorException, Exception
						  {
							  
							  
						//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
							  
						    
							  String query00 = "SELECT cookie FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
							  
							//	 CSVResult csvResult00 = getCsvResult(false, query00);
								// List<String> headers00 = csvResult00.getHeaders();
						//		 List<String> lines00 = csvResult00.getLines();
							//	 List<PublisherReport> pubreport00 = new ArrayList();  
								
								 
							//	System.out.println(headers00);
							//	System.out.println(lines00);  
								  
								//  for (int i = 0; i < lines00.size(); i++)
							    //  {
							       
							     //   String[] data = ((String)lines00.get(i)).split(",");
							  //      //System.out.println(data[0]);
							     
								  
								  
								  
								Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
							    String query = "SELECT count(*)as visits,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + 
							      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
							      CSVResult csvResult = getCsvResult(false, query);
							      List<String> headers = csvResult.getHeaders();
							      List<String> lines = csvResult.getLines();
							      List<PublisherReport> pubreport = new ArrayList();
							      System.out.println(headers);
							      System.out.println(lines);
							      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
							      for (int i = 0; i < lines.size(); i++)
							      {
							        PublisherReport obj = new PublisherReport();
							        
							        String[] data = ((String)lines.get(i)).split(",");
							        obj.setDate(data[0]);
							        obj.setTotalvisits(data[1]);
							        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
							        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
							        pubreport.add(obj);
							      }
							    }  
							    
						    return pubreport;
						  }
		  
		  
		  
		  
				  public List<PublisherReport> EngagementTimeChannelArticleDatewise(String startdate, String enddate, String channel_name, String articlename)
						    throws CsvExtractorException, Exception
						  {
							  
							  
						//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
							  
						    
							  String query00 = "SELECT cookie FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
							  
							//	 CSVResult csvResult00 = getCsvResult(false, query00);
								// List<String> headers00 = csvResult00.getHeaders();
						//		 List<String> lines00 = csvResult00.getLines();
							//	 List<PublisherReport> pubreport00 = new ArrayList();  
								
								 
							//	System.out.println(headers00);
							//	System.out.println(lines00);  
								  
								//  for (int i = 0; i < lines00.size(); i++)
							    //  {
							       
							     //   String[] data = ((String)lines00.get(i)).split(",");
							  //      //System.out.println(data[0]);
							     
								  
								  
								  
								Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
							    String query = "SELECT SUM(engagementTime) as eT,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + 
							      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
							      CSVResult csvResult = getCsvResult(false, query);
							      List<String> headers = csvResult.getHeaders();
							      List<String> lines = csvResult.getLines();
							      List<PublisherReport> pubreport = new ArrayList();
							      System.out.println(headers);
							      System.out.println(lines);
							      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
							      for (int i = 0; i < lines.size(); i++)
							      {
							        PublisherReport obj = new PublisherReport();
							        
							        String[] data = ((String)lines.get(i)).split(",");
							        obj.setDate(data[0]);
							        obj.setEngagementTime(data[1]);
							        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
							        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
							        pubreport.add(obj);
							      }
							    }  
							    
						    return pubreport;
						  }
		  
				  
				  
				  public List<PublisherReport> EngagementTimeChannelArticle(String startdate, String enddate, String channel_name, String articlename)
						    throws CsvExtractorException, Exception
						  {
							  
							  
						//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
							  
						    
							  String query00 = "SELECT cookie FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
							  
							//	 CSVResult csvResult00 = getCsvResult(false, query00);
								// List<String> headers00 = csvResult00.getHeaders();
						//		 List<String> lines00 = csvResult00.getLines();
							//	 List<PublisherReport> pubreport00 = new ArrayList();  
								
								 
							//	System.out.println(headers00);
							//	System.out.println(lines00);  
								  
								//  for (int i = 0; i < lines00.size(); i++)
							    //  {
							       
							     //   String[] data = ((String)lines00.get(i)).split(",");
							  //      //System.out.println(data[0]);
							     
								  
								  
								  
								Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
							    String query = "SELECT SUM(engagementTime) as eT FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + 
							      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'";
							      CSVResult csvResult = getCsvResult(false, query);
							      List<String> headers = csvResult.getHeaders();
							      List<String> lines = csvResult.getLines();
							      List<PublisherReport> pubreport = new ArrayList();
							      System.out.println(headers);
							      System.out.println(lines);
							      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
							      for (int i = 0; i < lines.size(); i++)
							      {
							        PublisherReport obj = new PublisherReport();
							        
							        String[] data = ((String)lines.get(i)).split(",");
							       // obj.setDate(data[0]);
							        obj.setEngagementTime(data[0]);
							        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
							        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
							        pubreport.add(obj);
							      }
							    }  
							    
						    return pubreport;
						  }
		  
				  
				  
				  
				  
				  
				  
				  
				  
				  
				  
				  
				  
				  
				  
				  
				  
		  
				  public List<PublisherReport> counttotalvisitorsChannelArticleHourwise(String startdate, String enddate, String channel_name, String articlename)
						    throws CsvExtractorException, Exception
						  {
							  
							  
						//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
							  
						    
							  String query00 = "SELECT cookie FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
							  
							//	 CSVResult csvResult00 = getCsvResult(false, query00);
								// List<String> headers00 = csvResult00.getHeaders();
						//		 List<String> lines00 = csvResult00.getLines();
							//	 List<PublisherReport> pubreport00 = new ArrayList();  
								
								 
							//	System.out.println(headers00);
							//	System.out.println(lines00);  
								  
								//  for (int i = 0; i < lines00.size(); i++)
							    //  {
							       
							     //   String[] data = ((String)lines00.get(i)).split(",");
							  //      //System.out.println(data[0]);
							     
								  
								  
								  
								Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
							    String query = "SELECT count(*)as visits,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + 
							      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1h')";
							      CSVResult csvResult = getCsvResult(false, query);
							      List<String> headers = csvResult.getHeaders();
							      List<String> lines = csvResult.getLines();
							      List<PublisherReport> pubreport = new ArrayList();
							      System.out.println(headers);
							      System.out.println(lines);
							      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
							      for (int i = 0; i < lines.size(); i++)
							      {
							        PublisherReport obj = new PublisherReport();
							        
							        String[] data = ((String)lines.get(i)).split(",");
							        obj.setDate(data[0]);
							        obj.setTotalvisits(data[1]);
							        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
							        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
							        pubreport.add(obj);
							      }
							    }  
							    
						    return pubreport;
						  }
		  
		  
		  
		  
				  public List<PublisherReport> EngagementTimeChannelArticleHourwise(String startdate, String enddate, String channel_name, String articlename)
						    throws CsvExtractorException, Exception
						  {
							  
							  
						//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
							  
						    
							  String query00 = "SELECT cookie FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
							  
							//	 CSVResult csvResult00 = getCsvResult(false, query00);
								// List<String> headers00 = csvResult00.getHeaders();
						//		 List<String> lines00 = csvResult00.getLines();
							//	 List<PublisherReport> pubreport00 = new ArrayList();  
								
								 
							//	System.out.println(headers00);
							//	System.out.println(lines00);  
								  
								//  for (int i = 0; i < lines00.size(); i++)
							    //  {
							       
							     //   String[] data = ((String)lines00.get(i)).split(",");
							  //      //System.out.println(data[0]);
							     
								  
								  
								  
								Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
							    String query = "SELECT SUM(engagementTime) as eT,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + 
							      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1h')";
							      CSVResult csvResult = getCsvResult(false, query);
							      List<String> headers = csvResult.getHeaders();
							      List<String> lines = csvResult.getLines();
							      List<PublisherReport> pubreport = new ArrayList();
							      System.out.println(headers);
							      System.out.println(lines);
							      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
							      for (int i = 0; i < lines.size(); i++)
							      {
							        PublisherReport obj = new PublisherReport();
							        
							        String[] data = ((String)lines.get(i)).split(",");
							        obj.setDate(data[0]);
							        obj.setEngagementTime(data[1]);
							        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
							        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
							        pubreport.add(obj);
							      }
							    }  
							    
						    return pubreport;
						  }
		  
		  
		  
		  
		  
		  
		  
				  
		  public List<PublisherReport> countAudiencesegmentChannelArticle(String startdate, String enddate, String channel_name, String articlename)
		    throws CsvExtractorException, Exception
		  {
		      List<PublisherReport> pubreport = new ArrayList(); 
			  
			  String querya1 = "SELECT COUNT(DISTINCT(cookie_id)) FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate +"' limit 20000000";   
			  
			    //Divide count in different limits 
			
			  
			  List<String> Query = new ArrayList();
			  


			    System.out.println(querya1);
			    
			    final long startTime2 = System.currentTimeMillis();
				
			    
			    CSVResult csvResult1 = null;
				try {
					csvResult1 = AggregationModule.getCsvResult(false, querya1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    
			    final long endTime2 = System.currentTimeMillis();
				
			    List<String> headers = csvResult1.getHeaders();
			    List<String> lines = csvResult1.getLines();
			    
			    
			    String count = lines.get(0);
			    Double countv1 = Double.parseDouble(count);
			    Double n = 0.0;
			    if(countv1 >= 250000)
			       n=10.0;
			    
			    if(countv1 >= 100000 && countv1 <= 250000 )
			       n=10.0;
			    
			    if(countv1 <= 100000 && countv1 > 100)
		           n=10.0;	    
			   
			    if(countv1 <= 100)
			    	n=1.0;
			    
			    if(countv1 == 0)
			    {
			    	
			    	return pubreport;
			    	
			    }
			    
			    Double total_length = countv1 - 0;
			    Double subrange_length = total_length/n;	
			    
			    Double current_start = 0.0;
			    for (int i = 0; i < n; ++i) {
			      System.out.println("Smaller range: [" + current_start + ", " + (current_start + subrange_length) + "]");
			      Double startlimit = current_start;
			      Double finallimit = current_start + subrange_length;
			      Double index = startlimit +1;
			      if(countv1 == 1)
			    	  index=0.0;
			      String query = "SELECT DISTINCT(cookie_id) FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "' Order by cookie_id limit "+index.intValue()+","+finallimit.intValue();  	
				  System.out.println(query);
			  //    Query.add(query);
			      current_start += subrange_length;
			      Query.add(query);
			      
			    }
			    
			    
			    	
			    
			  
			  ExecutorService executorService = Executors.newFixedThreadPool(2000);
		        
		       List<Callable<FastMap<String,Double>>> lst = new ArrayList<Callable<FastMap<String,Double>>>();
		    
		       for(int i=0 ; i < Query.size(); i++ ){
		       lst.add(new AudienceSegmentQueryExecutionThreads(Query.get(i),client,searchDao));
		    /*   lst.add(new AudienceSegmentQueryExecutionThreads(query1,client,searchDao));
		       lst.add(new AudienceSegmentQueryExecutionThreads(query2,client,searchDao));
		       lst.add(new AudienceSegmentQueryExecutionThreads(query3,client,searchDao));
		       lst.add(new AudienceSegmentQueryExecutionThreads(query4,client,searchDao));*/
		        
		       // returns a list of Futures holding their status and results when all complete
		       lst.add(new SubcategoryQueryExecutionThreads(Query.get(i),client,searchDao));
		   /*    lst.add(new SubcategoryQueryExecutionThreads(query6,client,searchDao));
		       lst.add(new SubcategoryQueryExecutionThreads(query7,client,searchDao));
		       lst.add(new SubcategoryQueryExecutionThreads(query8,client,searchDao));
		       lst.add(new SubcategoryQueryExecutionThreads(query9,client,searchDao)); */
		       }
		       
		       
		       List<Future<FastMap<String,Double>>> maps = executorService.invokeAll(lst);
		        
		       System.out.println(maps.size() +" Responses recieved.\n");
		        
		       for(Future<FastMap<String,Double>> task : maps)
		       {
		    	   try{
		           if(task!=null)
		    	   System.out.println(task.get().toString());
		    	   }
		    	   catch(Exception e)
		    	   {
		    		   e.printStackTrace();
		    		   continue;
		    	   }
		    	    
		    	   
		    	   }
		        
		       /* shutdown your thread pool, else your application will keep running */
		       executorService.shutdown();
			  
			
			  //  //System.out.println(headers1);
			 //   //System.out.println(lines1);
			    
			    
		       
		       FastMap<String,Double> audiencemap = new FastMap<String,Double>();
		       
		       FastMap<String,Double> subcatmap = new FastMap<String,Double>();
		       
		       Double count1 = 0.0;
		       
		       Double count2 = 0.0;
		       
		       String key ="";
		       String key1 = "";
		       Double value = 0.0;
		       Double vlaue1 = 0.0;
		       
			    for (int i = 0; i < maps.size(); i++)
			    {
			    
			    	if(maps!=null && maps.get(i)!=null){
			        FastMap<String,Double> map = (FastMap<String, Double>) maps.get(i).get();
			    	
			       if(map.size() > 0){
			       
			       if(map.containsKey("audience_segment")==true){
			       for (Map.Entry<String, Double> entry : map.entrySet())
			    	 {
			    	  key = entry.getKey();
			    	  key = key.trim();
			    	  value=  entry.getValue();
			    	if(key.equals("audience_segment")==false) { 
			    	if(audiencemap.containsKey(key)==false)
			    	audiencemap.put(key,value);
			    	else
			    	{
			         count1 = audiencemap.get(key);
			         if(count1!=null)
			         audiencemap.put(key,count1+value);	
			    	}
			      }
			    }
			  }   

			       if(map.containsKey("subcategory")==true){
			       for (Map.Entry<String, Double> entry : map.entrySet())
			    	 {
			    	   key = entry.getKey();
			    	   key = key.trim();
			    	   value=  entry.getValue();
			    	if(key.equals("subcategory")==false) {    
			    	if(subcatmap.containsKey(key)==false)
			    	subcatmap.put(key,value);
			    	else
			    	{
			         count1 = subcatmap.get(key);
			         if(count1!=null)
			         subcatmap.put(key,count1+value);	
			    	}
			    }  
			    	
			   }
			      
			     	       }
			           
			       } 
			    
			    	} 	
			   }    
			    
			    String subcategory = null;
			   
			    if(audiencemap.size()>0){
			   
			    	for (Map.Entry<String, Double> entry : audiencemap.entrySet()) {
			    	//System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
			    

			        PublisherReport obj = new PublisherReport();
			        
			   //     String[] data = ((String)lines.get(i)).split(",");
			        
			     //   if(data[0].trim().toLowerCase().contains("festivals"))
			      //  obj.setAudience_segment("");
			      //  else
			        obj.setAudience_segment( entry.getKey());	
			        obj.setCount(String.valueOf(entry.getValue()));
			      
			        if ((!entry.getKey().equals("tech")) && (!entry.getKey().equals("india")) && (!entry.getKey().trim().toLowerCase().equals("foodbeverage")) )
			        {
			         for (Map.Entry<String, Double> entry1 : subcatmap.entrySet()) {
			        	 
			        	    
			        	 
			        	 PublisherReport obj1 = new PublisherReport();
			            
			           
			            if (entry1.getKey().contains(entry.getKey()))
			            {
			              String substring = "_" + entry.getKey() + "_";
			              subcategory = entry1.getKey().replace(substring, "");
			           //   if(data[0].trim().toLowerCase().contains("festivals"))
			           //   obj1.setAudience_segment("");
			           //   else
			        
			              //System.out.println(" \n\n\n Key : " + subcategory + " Value : " + entry1.getValue());  
			              obj1.setAudience_segment(subcategory);
			              obj1.setCount(String.valueOf(entry1.getValue()));
			              obj.getAudience_segment_data().add(obj1);
			            }
			          }
			          pubreport.add(obj);
			        }
			      
			    }
			    }
			    return pubreport;
		  }
		  
		  public List<PublisherReport> gettimeofdayChannelArticle(String startdate, String enddate, String channel_name, String articlename)
		    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
		  {
		    String query = "Select count(*) from enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1h')";
		    CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    System.out.println(headers);
		    System.out.println(lines);
		    List<PublisherReport> pubreport = new ArrayList();
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
		    {
		      for (int i = 0; i < lines.size(); i++)
		      {
		        PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");
		        obj.setTime_of_day(data[0]);
		        obj.setCount(data[1]);
		        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
		        pubreport.add(obj);
		      }
		     
		    }
		    return pubreport;
		  }
		  
		  public List<PublisherReport> countPinCodeChannelArticle(String startdate, String enddate, String channel_name, String articlename)
		    throws CsvExtractorException, Exception
		  {
		    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
		    String query = "SELECT COUNT(*)as count,postalcode FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by postalcode";
		    CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    System.out.println(headers);
		    System.out.println(lines);
		    List<PublisherReport> pubreport = new ArrayList();
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
		      for (int i = 0; i < lines.size(); i++)
		      {
		        PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");
		        String[] data1 = data[0].split("_");
		        String locationproperties  = citycodeMap.get(data1[0]);
		        obj.setPostalcode(data[0]);
		        obj.setCount(data[1]);
		        obj.setLocationcode(locationproperties);
		        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
		        pubreport.add(obj);
		      }
		    }
		    return pubreport;
		  }
		  
		  public List<PublisherReport> countLatLongChannelArticle(String startdate, String enddate, String channel_name, String articlename)
		    throws CsvExtractorException, Exception
		  {
		    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
		    String query = "SELECT COUNT(*)as count,latitude_longitude FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by latitude_longitude";
		    CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    System.out.println(headers);
		    System.out.println(lines);
		    List<PublisherReport> pubreport = new ArrayList();
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
		      for (int i = 0; i < lines.size(); i++)
		      {
		        PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");
		        String[] data1 = data[0].split("_");
		        String locationproperties  = citycodeMap.get(data1[0]);
		        String[] dashcount = data[0].split("_");
		        if ((dashcount.length == 3) && (data[0].charAt(data[0].length() - 1) != '_'))
		        {
		          if (!dashcount[2].isEmpty())
		          {
		            obj.setLatitude_longitude(data[0]);
		            obj.setCount(data[1]);
		            obj.setLocationcode(locationproperties);
		            String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		            String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
		          }
		          pubreport.add(obj);
		        }
		      }
		    }
		    return pubreport;
		  }
		  
		  public List<PublisherReport> gettimeofdayQuarterChannelArticle(String startdate, String enddate, String channel_name, String articlename)
		    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
		  {
		    String query = "Select count(*) from enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='4h')";
		    CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    System.out.println(headers);
		    System.out.println(lines);
		    List<PublisherReport> pubreport = new ArrayList();
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
		    {
		      for (int i = 0; i < lines.size(); i++)
		      {
		        PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");
		        obj.setTime_of_day(data[0]);
		        obj.setCount(data[1]);
		        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
		        pubreport.add(obj);
		      }
		     
		    }
		    return pubreport;
		  }
		  
		  public List<PublisherReport> gettimeofdayDailyChannelArticle(String startdate, String enddate, String channel_name, String articlename)
		    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
		  {
		    String query = "Select count(*) from enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1d')";
		    CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    System.out.println(headers);
		    System.out.println(lines);
		    List<PublisherReport> pubreport = new ArrayList();
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
		    {
		      for (int i = 0; i < lines.size(); i++)
		      {
		        PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");
		        obj.setTime_of_day(data[0]);
		        obj.setCount(data[1]);
		        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
		        pubreport.add(obj);
		      }
		      System.out.println(headers);
		      System.out.println(lines);
		    }
		    return pubreport;
		  }
		  
		  public List<PublisherReport> getdayQuarterdataChannelArticle(String startdate, String enddate, String channel_name, String articlename)
		    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
		  {
		    String query = "Select count(*),QuarterValue from enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY QuarterValue";
		    CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    List<PublisherReport> pubreport = new ArrayList();
		    System.out.println(headers);
		      System.out.println(lines);
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
		    {
		      for (int i = 0; i < lines.size(); i++)
		      {
		        PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");
		        if (data[0].equals("quarter1")) {
		          data[0] = "quarter1 (00 - 04 AM)";
		        }
		        if (data[0].equals("quarter2")) {
		          data[0] = "quarter2 (04 - 08 AM)";
		        }
		        if (data[0].equals("quarter3")) {
		          data[0] = "quarter3 (08 - 12 AM)";
		        }
		        if (data[0].equals("quarter4")) {
		          data[0] = "quarter4 (12 - 16 PM)";
		        }
		        if (data[0].equals("quarter5")) {
		          data[0] = "quarter5 (16 - 20 PM)";
		        }
		        if (data[0].equals("quarter6")) {
		          data[0] = "quarter6 (20 - 24 PM)";
		        }
		        obj.setTime_of_day(data[0]);
		        obj.setCount(data[1]);
		        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
		        pubreport.add(obj);
		      }
		      System.out.println(headers);
		      System.out.println(lines);
		    }
		    return pubreport;
		  }
		  
		  public List<PublisherReport> getGenderChannelArticle(String startdate, String enddate, String channel_name, String articlename)
		    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
		  {
		    String query = "Select count(*),gender from enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY gender";
		    CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    List<PublisherReport> pubreport = new ArrayList();
		    
		    System.out.println(headers);
		    System.out.println(lines);
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
		    {
		      for (int i = 0; i < lines.size(); i++)
		      {
		        PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");
		        obj.setGender(data[0]);
		        obj.setCount(data[1]);
		        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
		        pubreport.add(obj);
		      }
		      System.out.println(headers);
		      System.out.println(lines);
		    }
		    return pubreport;
		  }
		  
		  public List<PublisherReport> getAgegroupChannelArticle(String startdate, String enddate, String channel_name, String articlename)
		    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
		  {
		    String query = "Select count(*),agegroup from enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY agegroup";
		    CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    System.out.println(headers);
		    System.out.println(lines);
		    List<PublisherReport> pubreport = new ArrayList();
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
		    {
		      for (int i = 0; i < lines.size(); i++)
		      {
		        PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");
		        obj.setAge(data[0]);
		        obj.setCount(data[1]);
		        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
		        pubreport.add(obj);
		      }
		    
		    }
		    return pubreport;
		  }
		  
		  public List<PublisherReport> getISPChannelArticle(String startdate, String enddate, String channel_name, String articlename)
		    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
		  {
		    String query = "Select count(*),ISP from enhanceduserdatabeta1 where refcurrentoriginal= '"+articlename+"' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY ISP";
		    CSVResult csvResult = getCsvResult(false, query);
		    List<String> headers = csvResult.getHeaders();
		    List<String> lines = csvResult.getLines();
		    System.out.println(headers);
		    System.out.println(lines);
		    List<PublisherReport> pubreport = new ArrayList();
		    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
		    {
		      for (int i = 0; i < lines.size(); i++)
		      {
		        PublisherReport obj = new PublisherReport();
		        
		        String[] data = ((String)lines.get(i)).split(",");
		        if(data[0].trim().toLowerCase().equals("_ltd")==false){ 
		        obj.setISP(data[0]);
		        obj.setCount(data[1]);
		        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		        String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
		        pubreport.add(obj);
		         }
		        }
		     // System.out.println(headers);
		     // System.out.println(lines);
		    }
		    return pubreport;
		  }
		  
		  public List<PublisherReport> getOrgChannelArticle(String startdate, String enddate, String channel_name, String articlename)
		    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
		  {
		    String query1 = "Select count(*),organisation from enhanceduserdatabeta1 where refcurrentoriginal= '"+articlename+"' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY organisation";
		    CSVResult csvResult1 = getCsvResult(false, query1);
		    List<String> headers1 = csvResult1.getHeaders();
		    List<String> lines1 = csvResult1.getLines();
		    System.out.println(headers1);
		      System.out.println(lines1);
		    List<PublisherReport> pubreport = new ArrayList();
		    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
		    {
		      for (int i = 0; i < lines1.size(); i++)
		      {
		        PublisherReport obj = new PublisherReport();
		        
		        String[] data1 = ((String)lines1.get(i)).split(",");
		        if ((data1[0].length() > 3) && (data1[0].charAt(0) != '_') && (!data1[0].trim().toLowerCase().contains("broadband")) && (!data1[0].trim().toLowerCase().contains("communication")) && (!data1[0].trim().toLowerCase().contains("cable")) && (!data1[0].trim().toLowerCase().contains("telecom")) && (!data1[0].trim().toLowerCase().contains("network")) && (!data1[0].trim().toLowerCase().contains("isp")) && (!data1[0].trim().toLowerCase().contains("hathway")) && (!data1[0].trim().toLowerCase().contains("internet")) && (!data1[0].trim().toLowerCase().equals("_ltd")) && (!data1[0].trim().toLowerCase().contains("googlebot")) && (!data1[0].trim().toLowerCase().contains("sify")) && (!data1[0].trim().toLowerCase().contains("bsnl")) && (!data1[0].trim().toLowerCase().contains("reliance")) && (!data1[0].trim().toLowerCase().contains("broadband")) && (!data1[0].trim().toLowerCase().contains("tata")) && (!data1[0].trim().toLowerCase().contains("nextra")))
		        {
		          obj.setOrganisation(data1[0]);
		          obj.setCount(data1[1]);
		          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
		          String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
		          pubreport.add(obj);
		        }
		      }
		    //  System.out.println(headers1);
		    //  System.out.println(lines1);
		    }
		    return pubreport;
		  }
		  
		  
		  public List<PublisherReport> getChannelSectionArticleList(String startdate, String enddate, String channel_name, String sectionname)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query1 = "Select count(*),refcurrentoriginal from enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY refcurrentoriginal";
				    CSVResult csvResult1 = getCsvResult(false, query1);
				    List<String> headers1 = csvResult1.getHeaders();
				    List<String> lines1 = csvResult1.getLines();
				    System.out.println(headers1);
				      System.out.println(lines1);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines1.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data1 = ((String)lines1.get(i)).split(",");
				      
				        
				          String articleparts[] = data1[0].split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle); obj.setPublisher_pages(data1[0]);
				          obj.setCount(data1[1]);
				          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				          obj.setSection(sectionname);
				          pubreport.add(obj);
				        
				      }
				    //  System.out.println(headers1);
				    //  System.out.println(lines1);
				    }
				    return pubreport;
				  }
		  
		  
		  
		  public List<PublisherReport> getChannelArticleReferrerList(String startdate, String enddate, String channel_name, String articlename)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query1 = "Select count(*),refcurrentoriginal from enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY refcurrentoriginal";
				    CSVResult csvResult1 = getCsvResult(false, query1);
				    List<String> headers1 = csvResult1.getHeaders();
				    List<String> lines1 = csvResult1.getLines();
				    System.out.println(headers1);
				      System.out.println(lines1);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines1.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data1 = ((String)lines1.get(i)).split(",");
				        if ((data1[0].trim().toLowerCase().contains("facebook") || (data1[0].trim().toLowerCase().contains("google"))))
				        {
				          //if(data1[0].equals()) 
				         
				          obj.setReferrerSource(data1[0]);
				          obj.setCount(data1[1]);
				          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				          String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
				          pubreport.add(obj);
				        }
				      }
				    //  System.out.println(headers1);
				    //  System.out.println(lines1);
				    }
				    return pubreport;
				  }
		  
		  
		  public List<PublisherReport> getChannelArticleReferrerList1(String startdate, String enddate, String channel_name, String articlename)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query1 = "Select count(*),refcurrentoriginal from enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY refcurrentoriginal";
				    CSVResult csvResult1 = getCsvResult(false, query1);
				    List<String> headers1 = csvResult1.getHeaders();
				    List<String> lines1 = csvResult1.getLines();
				    System.out.println(headers1);
				      System.out.println(lines1);
				    List<PublisherReport> pubreport = new ArrayList();
				//    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
				 //   {
				    
				    String data0="";
				    String data1="";
				    
				    for (int i = 0; i < 5; i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				      //  String[] data1 = ((String)lines1.get(i)).split(",");
				       
				          //if(data1[0].equals()) 
				         
				          if(i == 0){
				          data0="http://m.facebook.com";
				          data1 = "15.0";
				          }
				          

				          if(i == 1){
				          data0="http://www.facebook.com";
				          data1 = "5.0";
				          }
				          
				          
				          if(i == 2){
					          data0="http://l.facebook.com";
					          data1 = "3.0";
					          }
					    
				          
				          if(i == 3){
					          data0="http://www.google.co.pk";
					          data1 = "3.0";
					          }
					          
				          if(i==4){
				        	  data0="http://www.google.co.in";
				              data1 = "2.0";
				          }
				              
				           obj.setReferrerSource(data0);
				          obj.setCount(data1);
				          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				          String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
				          pubreport.add(obj);
				        
				   //   }
				    //  System.out.println(headers1);
				    //  System.out.println(lines1);
				   }
				    return pubreport;
		  
		  
		  
		  
		  
				      }  
		  
		  
		 
		  public List<PublisherReport> getDeviceTypeChannelArticle(String startdate, String enddate, String channel_name, String articlename)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query1 = "Select count(*),refcurrentoriginal from enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY refcurrentoriginal";
				    CSVResult csvResult1 = getCsvResult(false, query1);
				    List<String> headers1 = csvResult1.getHeaders();
				    List<String> lines1 = csvResult1.getLines();
				    System.out.println(headers1);
				      System.out.println(lines1);
				    List<PublisherReport> pubreport = new ArrayList();
				//    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
				 //   {
				    String data0="";
				    String data1="";
				    
				    for (int i = 0; i < 3; i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        
				       
				          //if(data1[0].equals()) 
				         
				          if(i == 0){
				          data0="Mobile";
				          data1 = "18.0";
				          }
				          

				          if(i == 1){
				          data0="Tablet";
				          data1 = "5.0";
				          }
				          
				          
				          if(i == 2){
					          data0="Desktop";
					          data1 = "5.0";
					      }
					    
				        
				          obj.setDevice_type(data0);
				          obj.setCount(data1);
				          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				          String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
				          pubreport.add(obj);
				        
				   //   }
				    //  System.out.println(headers1);
				    //  System.out.println(lines1);
				   }
				    return pubreport;
		  
		  
		  
		  
		  
				      }  
		  
		  
		
		  public List<PublisherReport> getIncomeChannelArticle(String startdate, String enddate, String channel_name, String articlename)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query1 = "Select count(*),refcurrentoriginal from enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY refcurrentoriginal";
				    CSVResult csvResult1 = getCsvResult(false, query1);
				    List<String> headers1 = csvResult1.getHeaders();
				    List<String> lines1 = csvResult1.getLines();
				    System.out.println(headers1);
				      System.out.println(lines1);
				    List<PublisherReport> pubreport = new ArrayList();
				//    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
				 //   {
				   
				    String data0="";
				    String data1="";
				    
				    for (int i = 0; i < 3; i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				       // String[] data1 = ((String)lines1.get(i)).split(",");
				       
				          //if(data1[0].equals()) 
				         
				          if(i == 0){
				          data0="Medium";
				          data1 = "15.0";
				          }
				          

				          if(i == 1){
				          data0="High";
				          data1 = "6.0";
				          }
				          
				          
				          if(i == 2){
					          data0="Low";
					          data1 = "7.0";
					      }
					    
				        
				          obj.setIncomelevel(data0);
				          obj.setCount(data1);
				          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				          String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
				          pubreport.add(obj);
				        
				   //   }
				    //  System.out.println(headers1);
				    //  System.out.println(lines1);
				   }
				    return pubreport;
		  
		  
		  
		  
		  
				      }  
		  
		  
		  public List<PublisherReport> getArticleMetaData(String startdate, String enddate, String channel_name, String articlename)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query1 = "Select count(*),refcurrentoriginal from enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY refcurrentoriginal";
				    CSVResult csvResult1 = getCsvResult(false, query1);
				    List<String> headers1 = csvResult1.getHeaders();
				    List<String> lines1 = csvResult1.getLines();
				    System.out.println(headers1);
				      System.out.println(lines1);
				    List<PublisherReport> pubreport = new ArrayList();
				//    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
				 //   {
				        PublisherReport obj = new PublisherReport();
				     
				     
					    
				        
				          obj.setArticleAuthor("admin");
				          obj.setArticleTags("filmfare,shahid kapoor,deepika padukone,bollywood");
				          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				          String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
				          pubreport.add(obj);
				        
				   //   }
				    //  System.out.println(headers1);
				    //  System.out.println(lines1);
				  
				    return pubreport;
		  
		  
		  
		  
		  
				      }  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  
		  public List<PublisherReport> getChannelArticleReferredPostsList(String startdate, String enddate, String channel_name, String articlename)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query1 = "Select count(*),clickurloriginal from enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY clickurloriginal";
				    
				    
				    Site site = GetMiddlewareData.getSiteDetails(channel_name);
				    CSVResult csvResult1 = getCsvResult(false, query1);
				    List<String> headers1 = csvResult1.getHeaders();
				    List<String> lines1 = csvResult1.getLines();
				    System.out.println(headers1);
				      System.out.println(lines1);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines1.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data1 = ((String)lines1.get(i)).split(",");
				          String articleparts[] = data1[0].split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle); obj.setPublisher_pages(data1[0]);
				          obj.setCount(data1[1]);
				          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				          String articleparts1[] = articlename.split("/"); String articleTitle1 = articleparts1[articleparts1.length-1]; obj.setArticleTitle(articleTitle1);obj.setArticle(articlename);
				          pubreport.add(obj);
				        
				      }
				    //  System.out.println(headers1);
				    //  System.out.println(lines1);
				    }
				    return pubreport;
				  }
		  
		  public List<PublisherReport> getChannelArticleReferredPostsListInternal(String startdate, String enddate, String channel_name, String articlename)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query1 = "Select count(*),clickurloriginal from enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY clickurloriginal";
				    
				    
				    Site site = GetMiddlewareData.getSiteDetails(channel_name);
				    String siteurl = site.getSiteurl();
				    CSVResult csvResult1 = getCsvResult(false, query1);
				    List<String> headers1 = csvResult1.getHeaders();
				    List<String> lines1 = csvResult1.getLines();
				    System.out.println(headers1);
				      System.out.println(lines1);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines1.size(); i++)
				      {
				    	try{  
				    	  
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data1 = ((String)lines1.get(i)).split(",");
				        if(data1[0].contains(siteurl)) { 
				        String articleparts[] = data1[0].split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle); obj.setPublisher_pages(data1[0]);
				          obj.setCount(data1[1]);
				          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				          String articleparts1[] = articlename.split("/"); String articleTitle1 = articleparts1[articleparts1.length-1]; obj.setArticleTitle(articleTitle1);obj.setArticle(articlename);
				          pubreport.add(obj);
				        }
				    	}
				    	catch(Exception e){
				    		
				    		continue;
				    	}
				        
				        
				        
				      }
				    //  System.out.println(headers1);
				    //  System.out.println(lines1);
				    }
				    return pubreport;
				  }
		  
		  
				  
		  
		  public List<PublisherReport> getChannelSectionArticleCount(String startdate, String enddate, String channel_name, String sectionname)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query1 = "Select count(*),refcurrentoriginal from enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY refcurrentoriginal";
				    CSVResult csvResult1 = getCsvResult(false, query1);
				    List<String> headers1 = csvResult1.getHeaders();
				    List<String> lines1 = csvResult1.getLines();
				    System.out.println(headers1);
				      System.out.println(lines1);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines1.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data1 = ((String)lines1.get(i)).split(",");
				        if ((data1[0].length() > 3) && (data1[0].charAt(0) != '_') && (!data1[0].trim().toLowerCase().contains("broadband")) && (!data1[0].trim().toLowerCase().contains("communication")) && (!data1[0].trim().toLowerCase().contains("cable")) && (!data1[0].trim().toLowerCase().contains("telecom")) && (!data1[0].trim().toLowerCase().contains("network")) && (!data1[0].trim().toLowerCase().contains("isp")) && (!data1[0].trim().toLowerCase().contains("hathway")) && (!data1[0].trim().toLowerCase().contains("internet")) && (!data1[0].trim().toLowerCase().equals("_ltd")) && (!data1[0].trim().toLowerCase().contains("googlebot")) && (!data1[0].trim().toLowerCase().contains("sify")) && (!data1[0].trim().toLowerCase().contains("bsnl")) && (!data1[0].trim().toLowerCase().contains("reliance")) && (!data1[0].trim().toLowerCase().contains("broadband")) && (!data1[0].trim().toLowerCase().contains("tata")) && (!data1[0].trim().toLowerCase().contains("nextra")))
				        {
				          String articleparts[] = data1[0].split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle); obj.setPublisher_pages(data1[0]);
				          obj.setCount(data1[1]);
				          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				          obj.setSection(sectionname);
				          pubreport.add(obj);
				        }
				      }
				    //  System.out.println(headers1);
				    //  System.out.println(lines1);
				    }
				    return pubreport;
				  }
		  
		  public List<PublisherReport> countBrandNameChannelSection(String startdate, String enddate, String channel_name, String sectionname)
				    throws CsvExtractorException, Exception
				  {
				    String query = "SELECT COUNT(*)as count,brandName FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by brandName";
				    //System.out.println(query);
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    System.out.println(headers);
				    System.out.println(lines);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        if(data[0].trim().toLowerCase().contains("logitech")==false && data[0].trim().toLowerCase().contains("mozilla")==false && data[0].trim().toLowerCase().contains("web_browser")==false && data[0].trim().toLowerCase().contains("microsoft")==false && data[0].trim().toLowerCase().contains("opera")==false && data[0].trim().toLowerCase().contains("epiphany")==false){ 
				        obj.setBrandname(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        obj.setSection(sectionname);
				        pubreport.add(obj);
				        } 
				       }
				  //    //System.out.println(headers);
				  //    //System.out.println(lines);
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> countBrowserChannelSection(String startdate, String enddate, String channel_name, String sectionname)
				    throws CsvExtractorException, Exception
				  {
				    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
				    String query = "SELECT COUNT(*)as count,browser_name FROM enhanceduserdatabeta1 where channel_name ='" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by browser_name";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    System.out.println(headers);
				    System.out.println(lines);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        obj.setBrowser(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        obj.setSection(sectionname);
				        pubreport.add(obj);
				      }
				      //System.out.println(headers);
				      //System.out.println(lines);
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> countOSChannelSection(String startdate, String enddate, String channel_name, String sectionname)
				    throws CsvExtractorException, Exception
				  {
				    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
				    String query = "SELECT COUNT(*)as count,system_os FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by system_os";
				    System.out.println(query);
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    System.out.println(headers);
				    System.out.println(lines);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        obj.setOs(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        obj.setSection(sectionname);
				        pubreport.add(obj);
				      }
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> countModelChannelSection(String startdate, String enddate, String channel_name, String sectionname)
				    throws CsvExtractorException, Exception
				  {
				    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
				    String query = "SELECT COUNT(*)as count,modelName FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by modelName";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    System.out.println(headers);
				    System.out.println(lines);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");

				        if(data[0].trim().toLowerCase().contains("logitech_revue")==false && data[0].trim().toLowerCase().contains("mozilla_firefox")==false && data[0].trim().toLowerCase().contains("apple_safari")==false && data[0].trim().toLowerCase().contains("generic_web")==false && data[0].trim().toLowerCase().contains("google_compute")==false && data[0].trim().toLowerCase().contains("microsoft_xbox")==false && data[0].trim().toLowerCase().contains("google_chromecast")==false && data[0].trim().toLowerCase().contains("opera")==false && data[0].trim().toLowerCase().contains("epiphany")==false && data[0].trim().toLowerCase().contains("laptop")==false){    
				        obj.setMobile_device_model_name(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        obj.setSection(sectionname);
				        pubreport.add(obj);
				      }
				        
				        }
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> countCityChannelSection(String startdate, String enddate, String channel_name, String sectionname)
				    throws CsvExtractorException, Exception
				  {
				    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
				    String query = "SELECT COUNT(*)as count,city FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by city";
				    System.out.println(query);
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    System.out.println(headers);
				    System.out.println(lines);
				    
				    
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        obj.setCity(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        obj.setSection(sectionname);
				        pubreport.add(obj);
				      }
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> countfingerprintChannelSection(String startdate, String enddate, String channel_name, String sectionname)
				    throws CsvExtractorException, Exception
				  {
					  
					  
				//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
					  
				    
					  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
						      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
					  
					//	 CSVResult csvResult00 = getCsvResult(false, query00);
						// List<String> headers00 = csvResult00.getHeaders();
				//		 List<String> lines00 = csvResult00.getLines();
					//	 List<PublisherReport> pubreport00 = new ArrayList();  
						
						 
					//	System.out.println(headers00);
					//	System.out.println(lines00);  
						  
						//  for (int i = 0; i < lines00.size(); i++)
					    //  {
					       
					     //   String[] data = ((String)lines00.get(i)).split(",");
					  //      //System.out.println(data[0]);
					     
						  
						  
						  
						Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
					    String query = "SELECT count(distinct(cookie_id))as reach FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
					      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'";
					      CSVResult csvResult = getCsvResult(false, query);
					      List<String> headers = csvResult.getHeaders();
					      List<String> lines = csvResult.getLines();
					      List<PublisherReport> pubreport = new ArrayList();
					      System.out.println(headers);
					      System.out.println(lines);
					      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
					      for (int i = 0; i < lines.size(); i++)
					      {
					        PublisherReport obj = new PublisherReport();
					        
					        String[] data = ((String)lines.get(i)).split(",");
					       // obj.setDate(data[0]);
					        obj.setReach(data[0]);
					        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
					        obj.setSection(sectionname);
					        pubreport.add(obj);
					      }
					    }  
					    
				    return pubreport;
				  }
				  
			
				  public List<PublisherReport> countfingerprintChannelDatewise(String startdate, String enddate, String channel_name)
						    throws CsvExtractorException, Exception
						  {
							  
							  
						//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
							  
						    
							  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
							  
							//	 CSVResult csvResult00 = getCsvResult(false, query00);
								// List<String> headers00 = csvResult00.getHeaders();
						//		 List<String> lines00 = csvResult00.getLines();
							//	 List<PublisherReport> pubreport00 = new ArrayList();  
								
								 
							//	System.out.println(headers00);
							//	System.out.println(lines00);  
								  
								//  for (int i = 0; i < lines00.size(); i++)
							    //  {
							       
							     //   String[] data = ((String)lines00.get(i)).split(",");
							  //      //System.out.println(data[0]);
							     
								  
								  
								  
								Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
							    String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where channel_name = '" + 
							      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
							      CSVResult csvResult = getCsvResult(false, query);
							      List<String> headers = csvResult.getHeaders();
							      List<String> lines = csvResult.getLines();
							      List<PublisherReport> pubreport = new ArrayList();
							      System.out.println(headers);
							      System.out.println(lines);
							      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
							      for (int i = 0; i < lines.size(); i++)
							      {
							        PublisherReport obj = new PublisherReport();
							        
							        String[] data = ((String)lines.get(i)).split(",");
							        obj.setDate(data[0]);
							        obj.setReach(data[1]);
							        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
							        pubreport.add(obj);
							      }
							    }  
							    
						    return pubreport;
						  }
				  
				  public List<PublisherReport> countfingerprintChannelDateHourwise(String startdate, String enddate, String channel_name)
						    throws CsvExtractorException, Exception
						  {
							  
							  
						//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
							  
						    
							  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
							  
							//	 CSVResult csvResult00 = getCsvResult(false, query00);
								// List<String> headers00 = csvResult00.getHeaders();
						//		 List<String> lines00 = csvResult00.getLines();
							//	 List<PublisherReport> pubreport00 = new ArrayList();  
								
								 
							//	System.out.println(headers00);
							//	System.out.println(lines00);  
								  
								//  for (int i = 0; i < lines00.size(); i++)
							    //  {
							       
							     //   String[] data = ((String)lines00.get(i)).split(",");
							  //      //System.out.println(data[0]);
							     
								  
								  
								  
								Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
							    String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where channel_name = '" + 
							      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1h')";
							      CSVResult csvResult = getCsvResult(false, query);
							      List<String> headers = csvResult.getHeaders();
							      List<String> lines = csvResult.getLines();
							      List<PublisherReport> pubreport = new ArrayList();
							      System.out.println(headers);
							      System.out.println(lines);
							      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
							      for (int i = 0; i < lines.size(); i++)
							      {
							        PublisherReport obj = new PublisherReport();
							        
							        String[] data = ((String)lines.get(i)).split(",");
							        obj.setDate(data[0]);
							        obj.setReach(data[1]);
							        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
							        pubreport.add(obj);
							      }
							    }  
							    
						    return pubreport;
						  }
				  
				  
				  			  
				  
				  
				  
				  
				  public List<PublisherReport> countfingerprintChannelSectionDatewise(String startdate, String enddate, String channel_name, String sectionname)
						    throws CsvExtractorException, Exception
						  {
							  
							  
						//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
							  
						    
							  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
							  
							//	 CSVResult csvResult00 = getCsvResult(false, query00);
								// List<String> headers00 = csvResult00.getHeaders();
						//		 List<String> lines00 = csvResult00.getLines();
							//	 List<PublisherReport> pubreport00 = new ArrayList();  
								
								 
							//	System.out.println(headers00);
							//	System.out.println(lines00);  
								  
								//  for (int i = 0; i < lines00.size(); i++)
							    //  {
							       
							     //   String[] data = ((String)lines00.get(i)).split(",");
							  //      //System.out.println(data[0]);
							     
								  
								  
								  
								Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
							    String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
							      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
							      CSVResult csvResult = getCsvResult(false, query);
							      List<String> headers = csvResult.getHeaders();
							      List<String> lines = csvResult.getLines();
							      List<PublisherReport> pubreport = new ArrayList();
							      System.out.println(headers);
							      System.out.println(lines);
							      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
							      for (int i = 0; i < lines.size(); i++)
							      {
							        PublisherReport obj = new PublisherReport();
							        
							        String[] data = ((String)lines.get(i)).split(",");
							        obj.setDate(data[0]);
							        obj.setReach(data[1]);
							        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
							        obj.setSection(sectionname);
							        pubreport.add(obj);
							      }
							    }  
							    
						    return pubreport;
						  }
				  
				  
				  
				  
				  public List<PublisherReport> countfingerprintChannelSectionDateHourwise(String startdate, String enddate, String channel_name, String sectionname)
						    throws CsvExtractorException, Exception
						  {
							  
							  
						//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
							  
						    
							  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
							  
							//	 CSVResult csvResult00 = getCsvResult(false, query00);
								// List<String> headers00 = csvResult00.getHeaders();
						//		 List<String> lines00 = csvResult00.getLines();
							//	 List<PublisherReport> pubreport00 = new ArrayList();  
								
								 
							//	System.out.println(headers00);
							//	System.out.println(lines00);  
								  
								//  for (int i = 0; i < lines00.size(); i++)
							    //  {
							       
							     //   String[] data = ((String)lines00.get(i)).split(",");
							  //      //System.out.println(data[0]);
							     
								  
								  
								  
								Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
							    String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
							      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1h')";
							      CSVResult csvResult = getCsvResult(false, query);
							      List<String> headers = csvResult.getHeaders();
							      List<String> lines = csvResult.getLines();
							      List<PublisherReport> pubreport = new ArrayList();
							      System.out.println(headers);
							      System.out.println(lines);
							      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
							      for (int i = 0; i < lines.size(); i++)
							      {
							        PublisherReport obj = new PublisherReport();
							        
							        String[] data = ((String)lines.get(i)).split(",");
							        obj.setDate(data[0]);
							        obj.setReach(data[1]);
							        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
							        obj.setSection(sectionname);
							        pubreport.add(obj);
							      }
							    }  
							    
						    return pubreport;
						  }
				  
				  
				  
				  
				  
				  
				  
				  
				  
				  
				  
				  
				  
				  
				  
				  public List<PublisherReport> counttotalvisitorsChannelSection(String startdate, String enddate, String channel_name, String sectionname)
						    throws CsvExtractorException, Exception
						  {
							  
							  
						//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
							  
						    
							  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
							  
							//	 CSVResult csvResult00 = getCsvResult(false, query00);
								// List<String> headers00 = csvResult00.getHeaders();
						//		 List<String> lines00 = csvResult00.getLines();
							//	 List<PublisherReport> pubreport00 = new ArrayList();  
								
								 
							//	System.out.println(headers00);
							//	System.out.println(lines00);  
								  
								//  for (int i = 0; i < lines00.size(); i++)
							    //  {
							       
							     //   String[] data = ((String)lines00.get(i)).split(",");
							  //      //System.out.println(data[0]);
							     
								  
								  
								  
								Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
							    String query = "SELECT count(*) as visits FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
							      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'";
							      CSVResult csvResult = getCsvResult(false, query);
							      List<String> headers = csvResult.getHeaders();
							      List<String> lines = csvResult.getLines();
							      List<PublisherReport> pubreport = new ArrayList();
							      System.out.println(headers);
							      System.out.println(lines);
							      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
							      for (int i = 0; i < lines.size(); i++)
							      {
							        PublisherReport obj = new PublisherReport();
							        
							        String[] data = ((String)lines.get(i)).split(",");
							       // obj.setDate(data[0]);
							        obj.setTotalvisits(data[0]);
							        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
							        obj.setSection(sectionname);
							        pubreport.add(obj);
							      }
							    }  
							    
						    return pubreport;
						  }
						  
					
						  
						  public List<PublisherReport> counttotalvisitorsChannelSectionDatewise(String startdate, String enddate, String channel_name, String sectionname)
								    throws CsvExtractorException, Exception
								  {
									  
									  
								//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
									  
								    
									  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
										      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
									  
									//	 CSVResult csvResult00 = getCsvResult(false, query00);
										// List<String> headers00 = csvResult00.getHeaders();
								//		 List<String> lines00 = csvResult00.getLines();
									//	 List<PublisherReport> pubreport00 = new ArrayList();  
										
										 
									//	System.out.println(headers00);
									//	System.out.println(lines00);  
										  
										//  for (int i = 0; i < lines00.size(); i++)
									    //  {
									       
									     //   String[] data = ((String)lines00.get(i)).split(",");
									  //      //System.out.println(data[0]);
									     
										  
										  
										  
										Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
									    String query = "SELECT count(*)as visits,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
									      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
									      CSVResult csvResult = getCsvResult(false, query);
									      List<String> headers = csvResult.getHeaders();
									      List<String> lines = csvResult.getLines();
									      List<PublisherReport> pubreport = new ArrayList();
									      System.out.println(headers);
									      System.out.println(lines);
									      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
									      for (int i = 0; i < lines.size(); i++)
									      {
									        PublisherReport obj = new PublisherReport();
									        
									        String[] data = ((String)lines.get(i)).split(",");
									        obj.setDate(data[0]);
									        obj.setTotalvisits(data[1]);
									        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
									        obj.setSection(sectionname);
									        pubreport.add(obj);
									      }
									    }  
									    
								    return pubreport;
								  }
				  
				  
						  
						  
						  public List<PublisherReport> counttotalvisitorsChannelSectionDateHourwise(String startdate, String enddate, String channel_name, String sectionname)
								    throws CsvExtractorException, Exception
								  {
									  
									  
								//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
									  
								    
									  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
										      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
									  
									//	 CSVResult csvResult00 = getCsvResult(false, query00);
										// List<String> headers00 = csvResult00.getHeaders();
								//		 List<String> lines00 = csvResult00.getLines();
									//	 List<PublisherReport> pubreport00 = new ArrayList();  
										
										 
									//	System.out.println(headers00);
									//	System.out.println(lines00);  
										  
										//  for (int i = 0; i < lines00.size(); i++)
									    //  {
									       
									     //   String[] data = ((String)lines00.get(i)).split(",");
									  //      //System.out.println(data[0]);
									     
										  
										  
										  
										Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
									    String query = "SELECT count(*)as visits,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
									      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1h')";
									      CSVResult csvResult = getCsvResult(false, query);
									      List<String> headers = csvResult.getHeaders();
									      List<String> lines = csvResult.getLines();
									      List<PublisherReport> pubreport = new ArrayList();
									      System.out.println(headers);
									      System.out.println(lines);
									      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
									      for (int i = 0; i < lines.size(); i++)
									      {
									        PublisherReport obj = new PublisherReport();
									        
									        String[] data = ((String)lines.get(i)).split(",");
									        obj.setDate(data[0]);
									        obj.setTotalvisits(data[1]);
									        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
									        obj.setSection(sectionname);
									        pubreport.add(obj);
									      }
									    }  
									    
								    return pubreport;
								  }
				  
						  public List<PublisherReport> engagementTimeChannelSection(String startdate, String enddate, String channel_name, String sectionname)
								    throws CsvExtractorException, Exception
								  {
									  
									  
								//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
									  
								    
									  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
										      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
									  
									//	 CSVResult csvResult00 = getCsvResult(false, query00);
										// List<String> headers00 = csvResult00.getHeaders();
								//		 List<String> lines00 = csvResult00.getLines();
									//	 List<PublisherReport> pubreport00 = new ArrayList();  
										
										 
									//	System.out.println(headers00);
									//	System.out.println(lines00);  
										  
										//  for (int i = 0; i < lines00.size(); i++)
									    //  {
									       
									     //   String[] data = ((String)lines00.get(i)).split(",");
									  //      //System.out.println(data[0]);
									     
										  
										  
										  
										Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
									    String query = "SELECT SUM(engagementTime) as eT FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
									      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'";
									      CSVResult csvResult = getCsvResult(false, query);
									      List<String> headers = csvResult.getHeaders();
									      List<String> lines = csvResult.getLines();
									      List<PublisherReport> pubreport = new ArrayList();
									      System.out.println(headers);
									      System.out.println(lines);
									      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
									      for (int i = 0; i < lines.size(); i++)
									      {
									        PublisherReport obj = new PublisherReport();
									        
									        String[] data = ((String)lines.get(i)).split(",");
									       // obj.setDate(data[0]);
									        obj.setEngagementTime(data[0]);
									        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
									        obj.setSection(sectionname);
									        pubreport.add(obj);
									      }
									    }  
									    
								    return pubreport;
								  }		  
						  
						  
						  
						  
						  
						  
						
						  public List<PublisherReport> engagementTimeChannelSectionDatewise(String startdate, String enddate, String channel_name, String sectionname)
								    throws CsvExtractorException, Exception
								  {
									  
									  
								//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
									  
								    
									  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
										      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
									  
									//	 CSVResult csvResult00 = getCsvResult(false, query00);
										// List<String> headers00 = csvResult00.getHeaders();
								//		 List<String> lines00 = csvResult00.getLines();
									//	 List<PublisherReport> pubreport00 = new ArrayList();  
										
										 
									//	System.out.println(headers00);
									//	System.out.println(lines00);  
										  
										//  for (int i = 0; i < lines00.size(); i++)
									    //  {
									       
									     //   String[] data = ((String)lines00.get(i)).split(",");
									  //      //System.out.println(data[0]);
									     
										  
										  
										  
										Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
									    String query = "SELECT SUM(engagementTime) as eT,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
									      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
									      CSVResult csvResult = getCsvResult(false, query);
									      List<String> headers = csvResult.getHeaders();
									      List<String> lines = csvResult.getLines();
									      List<PublisherReport> pubreport = new ArrayList();
									      System.out.println(headers);
									      System.out.println(lines);
									      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
									      for (int i = 0; i < lines.size(); i++)
									      {
									        PublisherReport obj = new PublisherReport();
									        
									        String[] data = ((String)lines.get(i)).split(",");
									        obj.setDate(data[0]);
									        obj.setEngagementTime(data[1]);
									        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
									        obj.setSection(sectionname);
									        pubreport.add(obj);
									      }
									    }  
									    
								    return pubreport;
								  }
				  
				   
						  public List<PublisherReport> engagementTimeChannelSectionDateHourwise(String startdate, String enddate, String channel_name, String sectionname)
								    throws CsvExtractorException, Exception
								  {
									  
									  
								//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
									  
								    
									  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
										      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
									  
									//	 CSVResult csvResult00 = getCsvResult(false, query00);
										// List<String> headers00 = csvResult00.getHeaders();
								//		 List<String> lines00 = csvResult00.getLines();
									//	 List<PublisherReport> pubreport00 = new ArrayList();  
										
										 
									//	System.out.println(headers00);
									//	System.out.println(lines00);  
										  
										//  for (int i = 0; i < lines00.size(); i++)
									    //  {
									       
									     //   String[] data = ((String)lines00.get(i)).split(",");
									  //      //System.out.println(data[0]);
									     
										  
										  
										  
										Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
									    String query = "SELECT SUM(engagementTime) as eT,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
									      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1h')";
									      CSVResult csvResult = getCsvResult(false, query);
									      List<String> headers = csvResult.getHeaders();
									      List<String> lines = csvResult.getLines();
									      List<PublisherReport> pubreport = new ArrayList();
									      System.out.println(headers);
									      System.out.println(lines);
									      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
									      for (int i = 0; i < lines.size(); i++)
									      {
									        PublisherReport obj = new PublisherReport();
									        
									        String[] data = ((String)lines.get(i)).split(",");
									        obj.setDate(data[0]);
									        obj.setEngagementTime(data[1]);
									        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
									        obj.setSection(sectionname);
									        pubreport.add(obj);
									      }
									    }  
									    
								    return pubreport;
								  }
				   
						  
						  
						  
						  
						  
						  public List<PublisherReport> counttotalvisitorsChannelDatewise(String startdate, String enddate, String channel_name)
								    throws CsvExtractorException, Exception
								  {
									  
									  
								//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
									  
								    
									  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
										      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
									  
									//	 CSVResult csvResult00 = getCsvResult(false, query00);
										// List<String> headers00 = csvResult00.getHeaders();
								//		 List<String> lines00 = csvResult00.getLines();
									//	 List<PublisherReport> pubreport00 = new ArrayList();  
										
										 
									//	System.out.println(headers00);
									//	System.out.println(lines00);  
										  
										//  for (int i = 0; i < lines00.size(); i++)
									    //  {
									       
									     //   String[] data = ((String)lines00.get(i)).split(",");
									  //      //System.out.println(data[0]);
									     
										  
										  
										  
										Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
									    String query = "SELECT count(*)as visits,date FROM enhanceduserdatabeta1 where channel_name = '" + 
									      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
									      CSVResult csvResult = getCsvResult(false, query);
									      List<String> headers = csvResult.getHeaders();
									      List<String> lines = csvResult.getLines();
									      List<PublisherReport> pubreport = new ArrayList();
									      System.out.println(headers);
									      System.out.println(lines);
									      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
									      for (int i = 0; i < lines.size(); i++)
									      {
									        PublisherReport obj = new PublisherReport();
									        
									        String[] data = ((String)lines.get(i)).split(",");
									        obj.setDate(data[0]);
									        obj.setTotalvisits(data[1]);
									        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
									       
									        pubreport.add(obj);
									      }
									    }  
									    
								    return pubreport;
								  }
				  
						  
						  
						  public List<PublisherReport> engagementTimeChannel(String startdate, String enddate, String channel_name)
								    throws CsvExtractorException, Exception
								  {
									  
									  
								//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
									  
								    
									  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
										      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
									  
									//	 CSVResult csvResult00 = getCsvResult(false, query00);
										// List<String> headers00 = csvResult00.getHeaders();
								//		 List<String> lines00 = csvResult00.getLines();
									//	 List<PublisherReport> pubreport00 = new ArrayList();  
										
										 
									//	System.out.println(headers00);
									//	System.out.println(lines00);  
										  
										//  for (int i = 0; i < lines00.size(); i++)
									    //  {
									       
									     //   String[] data = ((String)lines00.get(i)).split(",");
									  //      //System.out.println(data[0]);
									     
										  
										  
										  
										Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
									    String query = "SELECT SUM(engagementTime)as eT FROM enhanceduserdatabeta1 where channel_name = '" + 
									      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'";
									      CSVResult csvResult = getCsvResult(false, query);
									      List<String> headers = csvResult.getHeaders();
									      List<String> lines = csvResult.getLines();
									      List<PublisherReport> pubreport = new ArrayList();
									      System.out.println(headers);
									      System.out.println(lines);
									      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
									      for (int i = 0; i < lines.size(); i++)
									      {
									        PublisherReport obj = new PublisherReport();
									        
									        String[] data = ((String)lines.get(i)).split(",");
									      //  obj.setDate(data[0]);
									        obj.setEngagementTime(data[0]);
									        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
									       
									        pubreport.add(obj);
									      }
									    }  
									    
								    return pubreport;
								  }
				  
						  
						  
						  
						  public List<PublisherReport> engagementTimeChannelDatewise(String startdate, String enddate, String channel_name)
								    throws CsvExtractorException, Exception
								  {
									  
									  
								//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
									  
								    
									  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
										      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
									  
									//	 CSVResult csvResult00 = getCsvResult(false, query00);
										// List<String> headers00 = csvResult00.getHeaders();
								//		 List<String> lines00 = csvResult00.getLines();
									//	 List<PublisherReport> pubreport00 = new ArrayList();  
										
										 
									//	System.out.println(headers00);
									//	System.out.println(lines00);  
										  
										//  for (int i = 0; i < lines00.size(); i++)
									    //  {
									       
									     //   String[] data = ((String)lines00.get(i)).split(",");
									  //      //System.out.println(data[0]);
									     
										  
										  
										  
										Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
									    String query = "SELECT SUM(engagementTime)as eT,date FROM enhanceduserdatabeta1 where channel_name = '" + 
									      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
									      CSVResult csvResult = getCsvResult(false, query);
									      List<String> headers = csvResult.getHeaders();
									      List<String> lines = csvResult.getLines();
									      List<PublisherReport> pubreport = new ArrayList();
									      System.out.println(headers);
									      System.out.println(lines);
									      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
									      for (int i = 0; i < lines.size(); i++)
									      {
									        PublisherReport obj = new PublisherReport();
									        
									        String[] data = ((String)lines.get(i)).split(",");
									        obj.setDate(data[0]);
									        obj.setEngagementTime(data[1]);
									        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
									       
									        pubreport.add(obj);
									      }
									    }  
									    
								    return pubreport;
								  }
				  
						  
						  public List<PublisherReport> engagementTimeChannelDateHourwise(String startdate, String enddate, String channel_name)
								    throws CsvExtractorException, Exception
								  {
									  
									  
								//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
									  
								    
									  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
										      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
									  
									//	 CSVResult csvResult00 = getCsvResult(false, query00);
										// List<String> headers00 = csvResult00.getHeaders();
								//		 List<String> lines00 = csvResult00.getLines();
									//	 List<PublisherReport> pubreport00 = new ArrayList();  
										
										 
									//	System.out.println(headers00);
									//	System.out.println(lines00);  
										  
										//  for (int i = 0; i < lines00.size(); i++)
									    //  {
									       
									     //   String[] data = ((String)lines00.get(i)).split(",");
									  //      //System.out.println(data[0]);
									     
										  
										  
										  
										Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
									    String query = "SELECT SUM(engagementTime)as eT,date FROM enhanceduserdatabeta1 where channel_name = '" + 
									      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1h')";
									      CSVResult csvResult = getCsvResult(false, query);
									      List<String> headers = csvResult.getHeaders();
									      List<String> lines = csvResult.getLines();
									      List<PublisherReport> pubreport = new ArrayList();
									      System.out.println(headers);
									      System.out.println(lines);
									      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
									      for (int i = 0; i < lines.size(); i++)
									      {
									        PublisherReport obj = new PublisherReport();
									        
									        String[] data = ((String)lines.get(i)).split(",");
									        obj.setDate(data[0]);
									        obj.setEngagementTime(data[1]);
									        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
									       
									        pubreport.add(obj);
									      }
									    }  
									    
								    return pubreport;
								  }
				  
						  
						  
						  
						  public List<PublisherReport> counttotalvisitorsChannelSectionDateHourlywise(String startdate, String enddate, String channel_name, String sectionname)
								    throws CsvExtractorException, Exception
								  {
									  
									  
								//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
									  
								    
									//  String query00 = "SELECT date_histogram(field=request_time,interval=1h)cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
									//	      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
									  
									//	 CSVResult csvResult00 = getCsvResult(false, query00);
										// List<String> headers00 = csvResult00.getHeaders();
								//		 List<String> lines00 = csvResult00.getLines();
									//	 List<PublisherReport> pubreport00 = new ArrayList();  
										
										 
									//	System.out.println(headers00);
									//	System.out.println(lines00);  
										  
										//  for (int i = 0; i < lines00.size(); i++)
									    //  {
									       
									     //   String[] data = ((String)lines00.get(i)).split(",");
									  //      //System.out.println(data[0]);
									     
										  
										  
										  
										Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
									    String query = "SELECT count(*)as visits,gender FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
									      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY gender, date_histogram(field='request_time','interval'='1h')";
									      CSVResult csvResult = getCsvResult(false, query);
									      List<String> headers = csvResult.getHeaders();
									      List<String> lines = csvResult.getLines();
									      List<PublisherReport> pubreport = new ArrayList();
									      System.out.println(headers);
									      System.out.println(lines);
									      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
									      for (int i = 0; i < lines.size(); i++)
									      {
									        PublisherReport obj = new PublisherReport();
									        
									        String[] data = ((String)lines.get(i)).split(",");
									        obj.setDate(data[0]);
									        obj.setTotalvisits(data[1]);
									        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
									        obj.setSection(sectionname);
									        pubreport.add(obj);
									      }
									    }  
									    
								    return pubreport;
								  }
				  
				  
				  
						  public List<PublisherReport> counttotalvisitorsChannelSectionDateHourlyMinutewise(String startdate, String enddate, String channel_name, String sectionname)
								    throws CsvExtractorException, Exception
								  {
									  
									  
								//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
									  
								    
									//  String query00 = "SELECT date_histogram(field=request_time,interval=1h)cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
									//	      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
									  
									//	 CSVResult csvResult00 = getCsvResult(false, query00);
										// List<String> headers00 = csvResult00.getHeaders();
								//		 List<String> lines00 = csvResult00.getLines();
									//	 List<PublisherReport> pubreport00 = new ArrayList();  
										
										 
									//	System.out.println(headers00);
									//	System.out.println(lines00);  
										  
										//  for (int i = 0; i < lines00.size(); i++)
									    //  {
									       
									     //   String[] data = ((String)lines00.get(i)).split(",");
									  //      //System.out.println(data[0]);
									     
										  
										  
										  
										Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
									    String query = "SELECT count(*)as visits FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
									      channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1m')";
									      CSVResult csvResult = getCsvResult(false, query);
									      List<String> headers = csvResult.getHeaders();
									      List<String> lines = csvResult.getLines();
									      List<PublisherReport> pubreport = new ArrayList();
									      System.out.println(headers);
									      System.out.println(lines);
									      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
									      for (int i = 0; i < lines.size(); i++)
									      {
									        PublisherReport obj = new PublisherReport();
									        
									        String[] data = ((String)lines.get(i)).split(",");
									        obj.setDate(data[0]);
									        obj.setTotalvisits(data[1]);
									        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
									        obj.setSection(sectionname);
									        pubreport.add(obj);
									      }
									    }  
									    
								    return pubreport;
								  }
				  
						  public List<PublisherReport> counttotalvisitorsChannelDateHourlywise(String startdate, String enddate, String channel_name)
								    throws CsvExtractorException, Exception
								  {
									  
									  
								//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
									  
								    
									//  String query00 = "SELECT date_histogram(field=request_time,interval=1h)cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
									//	      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
									  
									//	 CSVResult csvResult00 = getCsvResult(false, query00);
										// List<String> headers00 = csvResult00.getHeaders();
								//		 List<String> lines00 = csvResult00.getLines();
									//	 List<PublisherReport> pubreport00 = new ArrayList();  
										
										 
									//	System.out.println(headers00);
									//	System.out.println(lines00);  
										  
										//  for (int i = 0; i < lines00.size(); i++)
									    //  {
									       
									     //   String[] data = ((String)lines00.get(i)).split(",");
									  //      //System.out.println(data[0]);
									     
										  
										  
										  
										Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
									    String query = "SELECT count(*)as visits FROM enhanceduserdatabeta1 where channel_name = '" + 
									      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1h')";
									      CSVResult csvResult = getCsvResult(false, query);
									      List<String> headers = csvResult.getHeaders();
									      List<String> lines = csvResult.getLines();
									      List<PublisherReport> pubreport = new ArrayList();
									      System.out.println(headers);
									      System.out.println(lines);
									      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
									      for (int i = 0; i < lines.size(); i++)
									      {
									        PublisherReport obj = new PublisherReport();
									        
									        String[] data = ((String)lines.get(i)).split(",");
									        obj.setDate(data[0]);
									        obj.setTotalvisits(data[1]);
									        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
									        
									        pubreport.add(obj);
									      }
									    }  
									    
								    return pubreport;
								  }
				  
				  
				  
						  public List<PublisherReport> counttotalvisitorsChannelDateHourlyMinutewise(String startdate, String enddate, String channel_name, String sectionname)
								    throws CsvExtractorException, Exception
								  {
									  
									  
								//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
									  
								    
									//  String query00 = "SELECT date_histogram(field=request_time,interval=1h)cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
									//	      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
									  
									//	 CSVResult csvResult00 = getCsvResult(false, query00);
										// List<String> headers00 = csvResult00.getHeaders();
								//		 List<String> lines00 = csvResult00.getLines();
									//	 List<PublisherReport> pubreport00 = new ArrayList();  
										
										 
									//	System.out.println(headers00);
									//	System.out.println(lines00);  
										  
										//  for (int i = 0; i < lines00.size(); i++)
									    //  {
									       
									     //   String[] data = ((String)lines00.get(i)).split(",");
									  //      //System.out.println(data[0]);
									     
										  
										  
										  
										Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
									    String query = "SELECT count(*)as visits FROM enhanceduserdatabeta1 where refcurrentoriginal channel_name = '" + 
									      channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1m')";
									      CSVResult csvResult = getCsvResult(false, query);
									      List<String> headers = csvResult.getHeaders();
									      List<String> lines = csvResult.getLines();
									      List<PublisherReport> pubreport = new ArrayList();
									      System.out.println(headers);
									      System.out.println(lines);
									      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
									      for (int i = 0; i < lines.size(); i++)
									      {
									        PublisherReport obj = new PublisherReport();
									        
									        String[] data = ((String)lines.get(i)).split(",");
									        obj.setDate(data[0]);
									        obj.setTotalvisits(data[1]);
									        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
									        obj.setSection(sectionname);
									        pubreport.add(obj);
									      }
									    }  
									    
								    return pubreport;
								  }
				  
				  
				  
				  
				  
				  
				  
						  
				  public List<PublisherReport> countAudiencesegmentChannelSection(String startdate, String enddate, String channel_name, String articlename)
				    throws CsvExtractorException, Exception
				  {
				      List<PublisherReport> pubreport = new ArrayList(); 
					  
					  String querya1 = "SELECT COUNT(DISTINCT(cookie_id)) FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate +"' limit 20000000";   
					  
					    //Divide count in different limits 
					
					  
					  List<String> Query = new ArrayList();
					  


					    System.out.println(querya1);
					    
					    final long startTime2 = System.currentTimeMillis();
						
					    
					    CSVResult csvResult1 = null;
						try {
							csvResult1 = AggregationModule.getCsvResult(false, querya1);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    
					    final long endTime2 = System.currentTimeMillis();
						
					    List<String> headers = csvResult1.getHeaders();
					    List<String> lines = csvResult1.getLines();
					    
					    
					    String count = lines.get(0);
					    Double countv1 = Double.parseDouble(count);
					    Double n = 0.0;
					    if(countv1 >= 250000)
					       n=10.0;
					    
					    if(countv1 >= 100000 && countv1 <= 250000 )
					       n=10.0;
					    
					    if(countv1 <= 100000 && countv1 > 100)
				           n=10.0;	    
					   
					    if(countv1 <= 100)
					    	n=1.0;
					    
					    if(countv1 == 0)
					    {
					    	
					    	return pubreport;
					    	
					    }
					    
					    Double total_length = countv1 - 0;
					    Double subrange_length = total_length/n;	
					    
					    Double current_start = 0.0;
					    for (int i = 0; i < n; ++i) {
					      System.out.println("Smaller range: [" + current_start + ", " + (current_start + subrange_length) + "]");
					      Double startlimit = current_start;
					      Double finallimit = current_start + subrange_length;
					      Double index = startlimit +1;
					      if(countv1 == 1)
					    	  index=0.0;
					      String query = "SELECT DISTINCT(cookie_id) FROM enhanceduserdatabeta1 where refcurrentoriginal= '"+articlename+"' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "' Order by cookie_id limit "+index.intValue()+","+finallimit.intValue();  	
						  System.out.println(query);
					  //    Query.add(query);
					      current_start += subrange_length;
					      Query.add(query);
					      
					    }
					    
					    
					    	
					    
					  
					  ExecutorService executorService = Executors.newFixedThreadPool(2000);
				        
				       List<Callable<FastMap<String,Double>>> lst = new ArrayList<Callable<FastMap<String,Double>>>();
				    
				       for(int i=0 ; i < Query.size(); i++ ){
				       lst.add(new AudienceSegmentQueryExecutionThreads(Query.get(i),client,searchDao));
				    /*   lst.add(new AudienceSegmentQueryExecutionThreads(query1,client,searchDao));
				       lst.add(new AudienceSegmentQueryExecutionThreads(query2,client,searchDao));
				       lst.add(new AudienceSegmentQueryExecutionThreads(query3,client,searchDao));
				       lst.add(new AudienceSegmentQueryExecutionThreads(query4,client,searchDao));*/
				        
				       // returns a list of Futures holding their status and results when all complete
				       lst.add(new SubcategoryQueryExecutionThreads(Query.get(i),client,searchDao));
				   /*    lst.add(new SubcategoryQueryExecutionThreads(query6,client,searchDao));
				       lst.add(new SubcategoryQueryExecutionThreads(query7,client,searchDao));
				       lst.add(new SubcategoryQueryExecutionThreads(query8,client,searchDao));
				       lst.add(new SubcategoryQueryExecutionThreads(query9,client,searchDao)); */
				       }
				       
				       
				       List<Future<FastMap<String,Double>>> maps = executorService.invokeAll(lst);
				        
				       System.out.println(maps.size() +" Responses recieved.\n");
				        
				       for(Future<FastMap<String,Double>> task : maps)
				       {
				    	   try{
				           if(task!=null)
				    	   System.out.println(task.get().toString());
				    	   }
				    	   catch(Exception e)
				    	   {
				    		   e.printStackTrace();
				    		   continue;
				    	   }
				    	    
				    	   
				    	   }
				        
				       /* shutdown your thread pool, else your application will keep running */
				       executorService.shutdown();
					  
					
					  //  //System.out.println(headers1);
					 //   //System.out.println(lines1);
					    
					    
				       
				       FastMap<String,Double> audiencemap = new FastMap<String,Double>();
				       
				       FastMap<String,Double> subcatmap = new FastMap<String,Double>();
				       
				       Double count1 = 0.0;
				       
				       Double count2 = 0.0;
				       
				       String key ="";
				       String key1 = "";
				       Double value = 0.0;
				       Double vlaue1 = 0.0;
				       
					    for (int i = 0; i < maps.size(); i++)
					    {
					    
					    	if(maps!=null && maps.get(i)!=null){
					        FastMap<String,Double> map = (FastMap<String, Double>) maps.get(i).get();
					    	
					       if(map.size() > 0){
					       
					       if(map.containsKey("audience_segment")==true){
					       for (Map.Entry<String, Double> entry : map.entrySet())
					    	 {
					    	  key = entry.getKey();
					    	  key = key.trim();
					    	  value=  entry.getValue();
					    	if(key.equals("audience_segment")==false) { 
					    	if(audiencemap.containsKey(key)==false)
					    	audiencemap.put(key,value);
					    	else
					    	{
					         count1 = audiencemap.get(key);
					         if(count1!=null)
					         audiencemap.put(key,count1+value);	
					    	}
					      }
					    }
					  }   

					       if(map.containsKey("subcategory")==true){
					       for (Map.Entry<String, Double> entry : map.entrySet())
					    	 {
					    	   key = entry.getKey();
					    	   key = key.trim();
					    	   value=  entry.getValue();
					    	if(key.equals("subcategory")==false) {    
					    	if(subcatmap.containsKey(key)==false)
					    	subcatmap.put(key,value);
					    	else
					    	{
					         count1 = subcatmap.get(key);
					         if(count1!=null)
					         subcatmap.put(key,count1+value);	
					    	}
					    }  
					    	
					   }
					      
					     	       }
					           
					       } 
					    
					    	} 	
					   }    
					    
					    String subcategory = null;
					   
					    if(audiencemap.size()>0){
					   
					    	for (Map.Entry<String, Double> entry : audiencemap.entrySet()) {
					    	//System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
					    

					        PublisherReport obj = new PublisherReport();
					        
					   //     String[] data = ((String)lines.get(i)).split(",");
					        
					     //   if(data[0].trim().toLowerCase().contains("festivals"))
					      //  obj.setAudience_segment("");
					      //  else
					        obj.setAudience_segment( entry.getKey());	
					        obj.setCount(String.valueOf(entry.getValue()));
					      
					        if ((!entry.getKey().equals("tech")) && (!entry.getKey().equals("india")) && (!entry.getKey().trim().toLowerCase().equals("foodbeverage")) )
					        {
					         for (Map.Entry<String, Double> entry1 : subcatmap.entrySet()) {
					        	 
					        	    
					        	 
					        	 PublisherReport obj1 = new PublisherReport();
					            
					           
					            if (entry1.getKey().contains(entry.getKey()))
					            {
					              String substring = "_" + entry.getKey() + "_";
					              subcategory = entry1.getKey().replace(substring, "");
					           //   if(data[0].trim().toLowerCase().contains("festivals"))
					           //   obj1.setAudience_segment("");
					           //   else
					        
					              //System.out.println(" \n\n\n Key : " + subcategory + " Value : " + entry1.getValue());  
					              obj1.setAudience_segment(subcategory);
					              obj1.setCount(String.valueOf(entry1.getValue()));
					              obj.getAudience_segment_data().add(obj1);
					            }
					          }
					          pubreport.add(obj);
					        }
					      
					    }
					    }
					    return pubreport;
				  }
				  
				  public List<PublisherReport> gettimeofdayChannelSection(String startdate, String enddate, String channel_name, String sectionname)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query = "Select count(*) from enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1h')";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    System.out.println(headers);
				    System.out.println(lines);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        obj.setTime_of_day(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        obj.setSection(sectionname);
				        pubreport.add(obj);
				      }
				     
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> countPinCodeChannelSection(String startdate, String enddate, String channel_name, String sectionname)
				    throws CsvExtractorException, Exception
				  {
				    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
				    String query = "SELECT COUNT(*)as count,postalcode FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by postalcode";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    System.out.println(headers);
				    System.out.println(lines);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
				      for (int i = 0; i < lines.size(); i++)
				      {
				    	  PublisherReport obj = new PublisherReport();
					        
					        String[] data = ((String)lines.get(i)).split(",");
					        String[] data1 = data[0].split("_");
					        String locationproperties  = citycodeMap.get(data1[0]);
					        obj.setPostalcode(data[0]);
					        obj.setCount(data[1]);
					        obj.setLocationcode(locationproperties);
					        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				            obj.setSection(sectionname);
				            pubreport.add(obj);
				      
				      }
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> countLatLongChannelSection(String startdate, String enddate, String channel_name, String sectionname)
				    throws CsvExtractorException, Exception
				  {
				    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
				    String query = "SELECT COUNT(*)as count,latitude_longitude FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by latitude_longitude";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    System.out.println(headers);
				    System.out.println(lines);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        String[] dashcount = data[0].split("_");
				        if ((dashcount.length == 3) && (data[0].charAt(data[0].length() - 1) != '_'))
				        {
				          if (!dashcount[2].isEmpty())
				          {
				            obj.setLatitude_longitude(data[0]);
				            obj.setCount(data[1]);
				            String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				            obj.setSection(sectionname);
				          }
				          pubreport.add(obj);
				        }
				      }
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> gettimeofdayQuarterChannelSection(String startdate, String enddate, String channel_name, String sectionname)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query = "Select count(*) from enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='4h')";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    System.out.println(headers);
				    System.out.println(lines);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        obj.setTime_of_day(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        obj.setSection(sectionname);
				        pubreport.add(obj);
				      }
				     
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> gettimeofdayDailyChannelSection(String startdate, String enddate, String channel_name, String sectionname)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query = "Select count(*) from enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1d')";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    System.out.println(headers);
				    System.out.println(lines);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        obj.setTime_of_day(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        obj.setSection(sectionname);
				        pubreport.add(obj);
				      }
				      System.out.println(headers);
				      System.out.println(lines);
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> getdayQuarterdataChannelSection(String startdate, String enddate, String channel_name, String sectionname)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query = "Select count(*),QuarterValue from enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY QuarterValue";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    List<PublisherReport> pubreport = new ArrayList();
				    System.out.println(headers);
				      System.out.println(lines);
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        if (data[0].equals("quarter1")) {
				          data[0] = "quarter1 (00 - 04 AM)";
				        }
				        if (data[0].equals("quarter2")) {
				          data[0] = "quarter2 (04 - 08 AM)";
				        }
				        if (data[0].equals("quarter3")) {
				          data[0] = "quarter3 (08 - 12 AM)";
				        }
				        if (data[0].equals("quarter4")) {
				          data[0] = "quarter4 (12 - 16 PM)";
				        }
				        if (data[0].equals("quarter5")) {
				          data[0] = "quarter5 (16 - 20 PM)";
				        }
				        if (data[0].equals("quarter6")) {
				          data[0] = "quarter6 (20 - 24 PM)";
				        }
				        obj.setTime_of_day(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        obj.setSection(sectionname);
				        pubreport.add(obj);
				      }
				      System.out.println(headers);
				      System.out.println(lines);
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> getGenderChannelSection(String startdate, String enddate, String channel_name, String sectionname)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query = "Select count(*),gender from enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY gender";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    List<PublisherReport> pubreport = new ArrayList();
				    
				    System.out.println(headers);
				    System.out.println(lines);
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        obj.setGender(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        obj.setSection(sectionname);
				        pubreport.add(obj);
				      }
				      System.out.println(headers);
				      System.out.println(lines);
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> getAgegroupChannelSection(String startdate, String enddate, String channel_name, String sectionname)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query = "Select count(*),agegroup from enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY agegroup";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    System.out.println(headers);
				    System.out.println(lines);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        obj.setAge(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        obj.setSection(sectionname);
				        pubreport.add(obj);
				      }
				    
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> getISPChannelSection(String startdate, String enddate, String channel_name, String sectionname)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query = "Select count(*),ISP from enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY ISP";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    System.out.println(headers);
				    System.out.println(lines);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        if(data[0].trim().toLowerCase().equals("_ltd")==false){ 
				        obj.setISP(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        obj.setSection(sectionname);
				        pubreport.add(obj);
				         }
				        }
				     // System.out.println(headers);
				     // System.out.println(lines);
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> getOrgChannelSection(String startdate, String enddate, String channel_name, String sectionname)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query1 = "Select count(*),organisation from enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY organisation";
				    CSVResult csvResult1 = getCsvResult(false, query1);
				    List<String> headers1 = csvResult1.getHeaders();
				    List<String> lines1 = csvResult1.getLines();
				    System.out.println(headers1);
				      System.out.println(lines1);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines1.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data1 = ((String)lines1.get(i)).split(",");
				        if ((data1[0].length() > 3) && (data1[0].charAt(0) != '_') && (!data1[0].trim().toLowerCase().contains("broadband")) && (!data1[0].trim().toLowerCase().contains("communication")) && (!data1[0].trim().toLowerCase().contains("cable")) && (!data1[0].trim().toLowerCase().contains("telecom")) && (!data1[0].trim().toLowerCase().contains("network")) && (!data1[0].trim().toLowerCase().contains("isp")) && (!data1[0].trim().toLowerCase().contains("hathway")) && (!data1[0].trim().toLowerCase().contains("internet")) && (!data1[0].trim().toLowerCase().equals("_ltd")) && (!data1[0].trim().toLowerCase().contains("googlebot")) && (!data1[0].trim().toLowerCase().contains("sify")) && (!data1[0].trim().toLowerCase().contains("bsnl")) && (!data1[0].trim().toLowerCase().contains("reliance")) && (!data1[0].trim().toLowerCase().contains("broadband")) && (!data1[0].trim().toLowerCase().contains("tata")) && (!data1[0].trim().toLowerCase().contains("nextra")))
				        {
				          obj.setOrganisation(data1[0]);
				          obj.setCount(data1[1]);
				          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				          obj.setSection(sectionname);
				          pubreport.add(obj);
				        }
				      }
				    //  System.out.println(headers1);
				    //  System.out.println(lines1);
				    }
				    return pubreport;
				  }
				  
				  
				
				  
				  
				  
				  public List<PublisherReport> getChannelSectionReferrerList(String startdate, String enddate, String channel_name, String sectionname)
						    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
						  {
						    String query1 = "Select count(*),refcurrentoriginal from enhanceduserdatabeta1 where refcurrent like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY refcurrentoriginal";
						    CSVResult csvResult1 = getCsvResult(false, query1);
						    List<String> headers1 = csvResult1.getHeaders();
						    List<String> lines1 = csvResult1.getLines();
						    System.out.println(headers1);
						      System.out.println(lines1);
						    List<PublisherReport> pubreport = new ArrayList();
						    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
						    {
						      for (int i = 0; i < lines1.size(); i++)
						      {
						        PublisherReport obj = new PublisherReport();
						        
						        String[] data1 = ((String)lines1.get(i)).split(",");
						        if ((data1[0].trim().toLowerCase().contains("facebook") || (data1[0].trim().toLowerCase().contains("google"))))
						        {
						          obj.setReferrerSource(data1[0]);
						          obj.setCount(data1[1]);
						          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
						          obj.setSection(sectionname);
						          pubreport.add(obj);
						        }
						      }
						    //  System.out.println(headers1);
						    //  System.out.println(lines1);
						    }
						    return pubreport;
						  }
				  
				  
				  public List<PublisherReport> getChannelSectionReferredPostsList(String startdate, String enddate, String channel_name, String sectionname)
						    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
						  {
						    String query1 = "Select count(*),clickedurl from enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY clickedurl";
						    CSVResult csvResult1 = getCsvResult(false, query1);
						    List<String> headers1 = csvResult1.getHeaders();
						    List<String> lines1 = csvResult1.getLines();
						    System.out.println(headers1);
						      System.out.println(lines1);
						    List<PublisherReport> pubreport = new ArrayList();
						    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
						    {
						      for (int i = 0; i < lines1.size(); i++)
						      {
						        PublisherReport obj = new PublisherReport();
						        
						        String[] data1 = ((String)lines1.get(i)).split(",");
						          String articleparts[] = data1[0].split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle); obj.setPublisher_pages(data1[0]);
						          obj.setCount(data1[1]);
						          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
						          obj.setSection(sectionname);
						          pubreport.add(obj);
						        
						      }
						    //  System.out.println(headers1);
						    //  System.out.println(lines1);
						    }
						    return pubreport;
						  }
				  
				  
				  
				  public List<PublisherReport> countNewUsersChannelSectionDatewise(String startdate, String enddate, String channel_name, String sectionname)
						    throws CsvExtractorException, Exception
						  {
							  
							  
						//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
							  
						    
							  String query00 = "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
							  
							//	 CSVResult csvResult00 = getCsvResult(false, query00);
								// List<String> headers00 = csvResult00.getHeaders();
						//		 List<String> lines00 = csvResult00.getLines();
							//	 List<PublisherReport> pubreport00 = new ArrayList();  
								
								 
							//	System.out.println(headers00);
							//	System.out.println(lines00);  
								  
								//  for (int i = 0; i < lines00.size(); i++)
							    //  {
							       
							     //   String[] data = ((String)lines00.get(i)).split(",");
							  //      //System.out.println(data[0]);
							     
								  
								  
								  
							//	Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
							  //  String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
							    //  channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
							      CSVResult csvResult = getCsvResult(false, query00);
							      List<String> headers = csvResult.getHeaders();
							      List<String> lines = csvResult.getLines();
							      List<PublisherReport> pubreport = new ArrayList();
							//      System.out.println(headers);
							//      System.out.println(lines);
							      Double count = 0.0;
							      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
							      for (int i = 0; i < lines.size(); i++)
							      {
							       
							        
							        String[] data = ((String)lines.get(i)).split(",");
							        if (Double.parseDouble(data[1].trim()) < 2.0)
							        {
							        count++;
							        
							        }
							        
							       }
							    }  
							
							      PublisherReport obj = new PublisherReport();
							      obj.setCount(count.toString());
							      obj.setVisitorType("New Visitors");
							      obj.setSection(sectionname);
							      pubreport.add(obj);
							      System.out.println("Section:"+sectionname+"Count:"+count);
							      
						    return pubreport;
						  }
				  
				  
				  public List<PublisherReport> countReturningUsersChannelSectionDatewise(String startdate, String enddate, String channel_name, String sectionname)
						    throws CsvExtractorException, Exception
						  {
							  
							  
					  String query00 = "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
						      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
					  
					//	 CSVResult csvResult00 = getCsvResult(false, query00);
						// List<String> headers00 = csvResult00.getHeaders();
				//		 List<String> lines00 = csvResult00.getLines();
					//	 List<PublisherReport> pubreport00 = new ArrayList();  
						
						 
					//	System.out.println(headers00);
					//	System.out.println(lines00);  
						  
						//  for (int i = 0; i < lines00.size(); i++)
					    //  {
					       
					     //   String[] data = ((String)lines00.get(i)).split(",");
					  //      //System.out.println(data[0]);
					     
						  
						  
						  
					//	Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
					  //  String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
					    //  channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
					      CSVResult csvResult = getCsvResult(false, query00);
					      List<String> headers = csvResult.getHeaders();
					      List<String> lines = csvResult.getLines();
					      List<PublisherReport> pubreport = new ArrayList();
					   //   System.out.println(headers);
					   //   System.out.println(lines);
					      Double count = 0.0;
					      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
					      for (int i = 0; i < lines.size(); i++)
					      {
					       
					        
					        String[] data = ((String)lines.get(i)).split(",");
					        if (Double.parseDouble(data[1].trim()) >= 2.0)
					        {
					        count++;
					        
					        }
					        
					       }
					    }  
					
					      PublisherReport obj = new PublisherReport();
					      obj.setCount(count.toString());
					      obj.setVisitorType("Returning Visitors");
					      obj.setSection(sectionname);
					      pubreport.add(obj);
					      System.out.println("Section:"+sectionname+"Count:"+count);
					      
				          return pubreport;
						  }
				  
				  
				  
				  public List<PublisherReport> countLoyalUsersChannelSectionDatewise(String startdate, String enddate, String channel_name, String sectionname)
						    throws CsvExtractorException, Exception
						  {
					  String query00 = "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
						      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
					  
					//	 CSVResult csvResult00 = getCsvResult(false, query00);
						// List<String> headers00 = csvResult00.getHeaders();
				//		 List<String> lines00 = csvResult00.getLines();
					//	 List<PublisherReport> pubreport00 = new ArrayList();  
						
						 
					//	System.out.println(headers00);
					//	System.out.println(lines00);  
						  
						//  for (int i = 0; i < lines00.size(); i++)
					    //  {
					       
					     //   String[] data = ((String)lines00.get(i)).split(",");
					  //      //System.out.println(data[0]);
					     
						  
						  
						  
					//	Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
					  //  String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
					    //  channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
					      CSVResult csvResult = getCsvResult(false, query00);
					      List<String> headers = csvResult.getHeaders();
					      List<String> lines = csvResult.getLines();
					      List<PublisherReport> pubreport = new ArrayList();
					//      System.out.println(headers);
					 //     System.out.println(lines);
					      Double count = 0.0;
					      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
					      for (int i = 0; i < lines.size(); i++)
					      {
					       
					        
					        String[] data = ((String)lines.get(i)).split(",");
					        if (Double.parseDouble(data[1].trim()) > 7.0)
					        {
					        count++;
					        
					        }
					        
					       }
					    }  
					
					      PublisherReport obj = new PublisherReport();
					      obj.setCount(count.toString());
					      obj.setVisitorType("Loyal Visitors");
					      obj.setSection(sectionname);
					      pubreport.add(obj);
					      System.out.println("Section:"+sectionname+"Count:"+count);
					      
				          return pubreport;
							  
						
						  }
				  
				  public List<PublisherReport> countNewUsersChannelArticleDatewise(String startdate, String enddate, String channel_name, String articlename)
						    throws CsvExtractorException, Exception
						  {
							  
							  
						//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
							  
						    
							  String query00 = "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
							  
							//	 CSVResult csvResult00 = getCsvResult(false, query00);
								// List<String> headers00 = csvResult00.getHeaders();
						//		 List<String> lines00 = csvResult00.getLines();
							//	 List<PublisherReport> pubreport00 = new ArrayList();  
								
								 
							//	System.out.println(headers00);
							//	System.out.println(lines00);  
								  
								//  for (int i = 0; i < lines00.size(); i++)
							    //  {
							       
							     //   String[] data = ((String)lines00.get(i)).split(",");
							  //      //System.out.println(data[0]);
							     
								  
								  
								  
							//	Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
							  //  String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
							    //  channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
							      CSVResult csvResult = getCsvResult(false, query00);
							      List<String> headers = csvResult.getHeaders();
							      List<String> lines = csvResult.getLines();
							      List<PublisherReport> pubreport = new ArrayList();
							 //     System.out.println(headers);
							 //     System.out.println(lines);
							      Double count = 0.0;
							      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
							      for (int i = 0; i < lines.size(); i++)
							      {
							       
							        
							        String[] data = ((String)lines.get(i)).split(",");
							        if (Double.parseDouble(data[1].trim()) < 2.0)
							        {
							        count++;
							        
							        }
							        
							       }
							    }  
							
							      PublisherReport obj = new PublisherReport();
							      obj.setCount(count.toString());
							      obj.setVisitorType("New Visitors");
							      String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
							      pubreport.add(obj);
							      System.out.println("Article:"+articlename+"Count:"+count);
							      
						    return pubreport;
						  }
				  
				  
				  public List<PublisherReport> countReturningUsersChannelArticleDatewise(String startdate, String enddate, String channel_name, String articlename)
						    throws CsvExtractorException, Exception
						  {
							  
							  
					  String query00 = "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + 
						      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
					  
					//	 CSVResult csvResult00 = getCsvResult(false, query00);
						// List<String> headers00 = csvResult00.getHeaders();
				//		 List<String> lines00 = csvResult00.getLines();
					//	 List<PublisherReport> pubreport00 = new ArrayList();  
						
						 
					//	System.out.println(headers00);
					//	System.out.println(lines00);  
						  
						//  for (int i = 0; i < lines00.size(); i++)
					    //  {
					       
					     //   String[] data = ((String)lines00.get(i)).split(",");
					  //      //System.out.println(data[0]);
					     
						  
						  
						  
					//	Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
					  //  String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
					    //  channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
					      CSVResult csvResult = getCsvResult(false, query00);
					      List<String> headers = csvResult.getHeaders();
					      List<String> lines = csvResult.getLines();
					      List<PublisherReport> pubreport = new ArrayList();
					//      System.out.println(headers);
					//      System.out.println(lines);
					      Double count = 0.0;
					      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
					      for (int i = 0; i < lines.size(); i++)
					      {
					       
					        
					        String[] data = ((String)lines.get(i)).split(",");
					        if (Double.parseDouble(data[1].trim()) >= 2.0)
					        {
					        count++;
					        
					        }
					        
					       }
					    }  
					
					      PublisherReport obj = new PublisherReport();
					      obj.setCount(count.toString());
					      obj.setVisitorType("Returning Visitors");
					      String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
					      pubreport.add(obj);
					      System.out.println("Article:"+articlename+"Count:"+count);
					      
				          return pubreport;
						  }
				  
				  
				  
				  public List<PublisherReport> countLoyalUsersChannelArticleDatewise(String startdate, String enddate, String channel_name, String articlename)
						    throws CsvExtractorException, Exception
						  {
					  String query00 = "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+articlename+"%' and channel_name = '" + 
						      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
					  
					//	 CSVResult csvResult00 = getCsvResult(false, query00);
						// List<String> headers00 = csvResult00.getHeaders();
				//		 List<String> lines00 = csvResult00.getLines();
					//	 List<PublisherReport> pubreport00 = new ArrayList();  
						
						 
					//	System.out.println(headers00);
					//	System.out.println(lines00);  
						  
						//  for (int i = 0; i < lines00.size(); i++)
					    //  {
					       
					     //   String[] data = ((String)lines00.get(i)).split(",");
					  //      //System.out.println(data[0]);
					     
						  
						  
						  
					//	Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
					  //  String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
					    //  channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
					      CSVResult csvResult = getCsvResult(false, query00);
					      List<String> headers = csvResult.getHeaders();
					      List<String> lines = csvResult.getLines();
					      List<PublisherReport> pubreport = new ArrayList();
					  //    System.out.println(headers);
					  //    System.out.println(lines);
					      Double count = 0.0;
					      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
					      for (int i = 0; i < lines.size(); i++)
					      {
					       
					        
					        String[] data = ((String)lines.get(i)).split(",");
					        if (Double.parseDouble(data[1].trim()) > 7.0)
					        {
					        count++;
					        
					        }
					        
					       }
					    }  
					
					      PublisherReport obj = new PublisherReport();
					      obj.setCount(count.toString());
					      obj.setVisitorType("Loyal Visitors");
					      String articleparts[] = articlename.split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle);obj.setArticle(articlename);
					      pubreport.add(obj);
					      System.out.println("Article:"+articlename+"Count:"+count);
					      
				          return pubreport;
						  }			  
						
				  
				  
				  
				  public List<PublisherReport> countNewUsersChannelDatewise(String startdate, String enddate, String channel_name)
						    throws CsvExtractorException, Exception
						  {
							  
							  
						//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
							  
						    
							  String query00 = "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
							  
							//	 CSVResult csvResult00 = getCsvResult(false, query00);
								// List<String> headers00 = csvResult00.getHeaders();
						//		 List<String> lines00 = csvResult00.getLines();
							//	 List<PublisherReport> pubreport00 = new ArrayList();  
								
								 
							//	System.out.println(headers00);
							//	System.out.println(lines00);  
								  
								//  for (int i = 0; i < lines00.size(); i++)
							    //  {
							       
							     //   String[] data = ((String)lines00.get(i)).split(",");
							  //      //System.out.println(data[0]);
							     
								  
								  
								  
							//	Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
							  //  String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
							    //  channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
							      CSVResult csvResult = getCsvResult(false, query00);
							      List<String> headers = csvResult.getHeaders();
							      List<String> lines = csvResult.getLines();
							      List<PublisherReport> pubreport = new ArrayList();
							//      System.out.println(headers);
							 //     System.out.println(lines);
							      Double count = 0.0;
							      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
							      for (int i = 0; i < lines.size(); i++)
							      {
							       
							        
							        String[] data = ((String)lines.get(i)).split(",");
							        if (Double.parseDouble(data[1].trim()) < 2.0)
							        {
							        count++;
							        
							        }
							        
							       }
							    }  
							
							      PublisherReport obj = new PublisherReport();
							      obj.setCount(count.toString());
							      obj.setVisitorType("New Visitors");
							      String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
							      pubreport.add(obj);
							   
							      
						    return pubreport;
						  }
				  
				  
				  
				  public List<PublisherReport> countNewUsersChannelDatewisegroupby(String startdate, String enddate, String channel_name, String groupby)
						    throws CsvExtractorException, Exception
						  {
							  
							  
						//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
							  
						    
							  String query00 = "SELECT COUNT(*)as count, cookie_id,"+groupby+" FROM enhanceduserdatabeta1 where channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +" group by cookie_id,"+groupby+" limit 20000000";
							  
							
							  
							  if(groupby.equals("hour")){
						    		query00 =  "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
										      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id,date_histogram(field='request_time','interval'='1h') limit 20000000";
						    	}

			                
							  if(groupby.equals("minute")){
				                	query00 =  "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
										      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id,date_histogram(field='request_time','interval'='1m') limit 20000000";	
						    	}

						       
							  
							  
							  //	 CSVResult csvResult00 = getCsvResult(false, query00);
								// List<String> headers00 = csvResult00.getHeaders();
						//		 List<String> lines00 = csvResult00.getLines();
							//	 List<PublisherReport> pubreport00 = new ArrayList();  
								
								 
							//	System.out.println(headers00);
							//	System.out.println(lines00);  
								  
								//  for (int i = 0; i < lines00.size(); i++)
							    //  {
							       
							     //   String[] data = ((String)lines00.get(i)).split(",");
							  //      //System.out.println(data[0]);
							     
								  
								  
								  
							//	Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
							  //  String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
							    //  channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
							      CSVResult csvResult = getCsvResult(false, query00);
							      List<String> headers = csvResult.getHeaders();
							      List<String> lines = csvResult.getLines();
							      List<PublisherReport> pubreport = new ArrayList();
							//      System.out.println(headers);
							 //     System.out.println(lines);
							      Double count = 0.0;
							      Map<String,Double> dates =new HashMap<String,Double>();
							      String date = "";
							      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
							      for (int i = 0; i < lines.size(); i++)
							      {
							       
							        
							        String[] data = ((String)lines.get(i)).split(",");
							        if(data.length>2){
							        date = data[1].trim();
							        if(dates.containsKey(date)==false)
							        dates.put(date,0.0);
							        if (Double.parseDouble(data[2].trim()) < 2.0)
							        {
							        count = dates.get(date);
							        dates.put(date,count+1);
							        
							        }
							        
							       }
							      }
							      
							    }  
							
							      for(Map.Entry<String,Double>entry: dates.entrySet()){
							      PublisherReport obj = new PublisherReport();
							      
							      if(groupby.equals("audience_segment"))
							             obj.setAudience_segment(entry.getKey());
						            	
						            	
						            	if(groupby.equals("gender"))
								             obj.setGender(entry.getKey());
						            	
						            	if(groupby.equals("hour"))
								             obj.setDate(entry.getKey());
						            	
						            	if(groupby.equals("minute"))
								             obj.setDate(entry.getKey());
						            	
						            	
						            	if(groupby.equals("gender"))
								             obj.setGender(entry.getKey());
						            	
						            	
						            	if(groupby.equals("refcurrentoriginal"))
								             obj.setGender(entry.getKey());
							            	
						            	if(groupby.equals("date"))
								             obj.setDate(entry.getKey());
							            		            	
						            	if(groupby.equals("subcategory"))
								             obj.setSubcategory(entry.getKey());
						            	
						            	if(groupby.equals("agegroup"))
								             obj.setAge(entry.getKey());
							            	
						            	if(groupby.equals("incomelevel"))
								          obj.setIncomelevel(entry.getKey());
							     
						            	if(groupby.equals("city"))
									    {
						            		String locationproperties = citycodeMap.get(entry.getKey());
						    		        String city =entry.getKey().replace("_"," ").replace("-"," ");
						    		        obj.setCity(city);
						    		        System.out.println(city);
						    		        obj.setLocationcode(locationproperties);
									    }
						            	
						            	obj.setCount(entry.getValue().toString());
							      String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
							      pubreport.add(obj);
							      }
							      
						    return pubreport;
						  }
				  
				  
				  
				  
				  
				  
				  
				  public List<PublisherReport> countReturningUsersChannelDatewisegroupby(String startdate, String enddate, String channel_name,String groupby)
						    throws CsvExtractorException, Exception
						  {
							  
							  
					  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
					  
					    
					  String query00 = "SELECT COUNT(*)as count, cookie_id,"+groupby+" FROM enhanceduserdatabeta1 where channel_name = '" + 
						      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id,"+groupby+" limit 20000000";
					  
					
					  
					  if(groupby.equals("hour")){
				    		query00 =  "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id,date_histogram(field='request_time','interval'='1h') limit 20000000";
				    	}

	                
					  if(groupby.equals("minute")){
		                	query00 =  "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id,date_histogram(field='request_time','interval'='1m') limit 20000000";	
				    	}

				       
					  
					  
					  //	 CSVResult csvResult00 = getCsvResult(false, query00);
						// List<String> headers00 = csvResult00.getHeaders();
				//		 List<String> lines00 = csvResult00.getLines();
					//	 List<PublisherReport> pubreport00 = new ArrayList();  
						
						 
					//	System.out.println(headers00);
					//	System.out.println(lines00);  
						  
						//  for (int i = 0; i < lines00.size(); i++)
					    //  {
					       
					     //   String[] data = ((String)lines00.get(i)).split(",");
					  //      //System.out.println(data[0]);
					     
						  
						  
						  
					//	Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
					  //  String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
					    //  channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
					      CSVResult csvResult = getCsvResult(false, query00);
					      List<String> headers = csvResult.getHeaders();
					      List<String> lines = csvResult.getLines();
					      List<PublisherReport> pubreport = new ArrayList();
					//      System.out.println(headers);
					 //     System.out.println(lines);
					      Double count = 0.0;
					      Map<String,Double> dates =new HashMap<String,Double>();
					      String date = "";
					      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
					      for (int i = 0; i < lines.size(); i++)
					      {
					       
					        
					        String[] data = ((String)lines.get(i)).split(",");
					        if(data.length > 2){
					        date = data[1].trim();
					        if(dates.containsKey(date)==false)
					        dates.put(date,0.0);
					        if (Double.parseDouble(data[2].trim()) >= 2.0)
					        {
					        count = dates.get(date);
					        dates.put(date,count+1);
					        
					        }
					       
					        } 
					       }
					    }  
					
					      for(Map.Entry<String,Double>entry: dates.entrySet()){
					      PublisherReport obj = new PublisherReport();
					      if(groupby.equals("audience_segment"))
					             obj.setAudience_segment(entry.getKey());
				            	
				            	
				            	if(groupby.equals("gender"))
						             obj.setGender(entry.getKey());
				            	
				            	if(groupby.equals("hour"))
						             obj.setDate(entry.getKey());
				            	
				            	if(groupby.equals("minute"))
						             obj.setDate(entry.getKey());
				            	
				            	
				            	if(groupby.equals("gender"))
						             obj.setGender(entry.getKey());
				            	
				            	
				            	if(groupby.equals("refcurrentoriginal"))
						             obj.setGender(entry.getKey());
					            	
				            	if(groupby.equals("date"))
						             obj.setDate(entry.getKey());
					            		            	
				            	if(groupby.equals("subcategory"))
						             obj.setSubcategory(entry.getKey());
				            	
				            	if(groupby.equals("agegroup"))
						             obj.setAge(entry.getKey());
					            	
				            	if(groupby.equals("incomelevel"))
						          obj.setIncomelevel(entry.getKey());
					     
				            	if(groupby.equals("city")){
				            	String locationproperties = citycodeMap.get(entry.getKey());
			    		        String city =entry.getKey().replace("_"," ").replace("-"," ");
			    		        obj.setCity(city);
			    		        System.out.println(city);
			    		        obj.setLocationcode(locationproperties);
				            	}
			    		  obj.setCount(entry.getValue().toString());
					      String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
					      pubreport.add(obj);
					      }
					      
				    return pubreport;
						  }
				  
				  
				  
				 
				  
				  
				  public List<PublisherReport> countReturningUsersChannelDatewise(String startdate, String enddate, String channel_name)
						    throws CsvExtractorException, Exception
						  {
							  
							  
					  String query00 = "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
						      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
					  
					//	 CSVResult csvResult00 = getCsvResult(false, query00);
						// List<String> headers00 = csvResult00.getHeaders();
				//		 List<String> lines00 = csvResult00.getLines();
					//	 List<PublisherReport> pubreport00 = new ArrayList();  
						
						 
					//	System.out.println(headers00);
					//	System.out.println(lines00);  
						  
						//  for (int i = 0; i < lines00.size(); i++)
					    //  {
					       
					     //   String[] data = ((String)lines00.get(i)).split(",");
					  //      //System.out.println(data[0]);
					     
						  
						  
						  
					//	Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
					  //  String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
					    //  channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
					      CSVResult csvResult = getCsvResult(false, query00);
					      List<String> headers = csvResult.getHeaders();
					      List<String> lines = csvResult.getLines();
					      List<PublisherReport> pubreport = new ArrayList();
					   //   System.out.println(headers);
					   //   System.out.println(lines);
					      Double count = 0.0;
					      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
					      for (int i = 0; i < lines.size(); i++)
					      {
					       
					        
					        String[] data = ((String)lines.get(i)).split(",");
					        if (Double.parseDouble(data[1].trim()) >= 2.0)
					        {
					        count++;
					        
					        }
					        
					       }
					    }  
					
					      PublisherReport obj = new PublisherReport();
					      obj.setCount(count.toString());
					      obj.setVisitorType("Returning Visitors");
					      String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
					      pubreport.add(obj);
					      
				          return pubreport;
						  }
				  
				  
				  
				  
				  
				  public List<PublisherReport> countLoyalUsersChannelDatewise(String startdate, String enddate, String channel_name)
						    throws CsvExtractorException, Exception
						  {
					  String query00 = "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
						      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
					  
					//	 CSVResult csvResult00 = getCsvResult(false, query00);
						// List<String> headers00 = csvResult00.getHeaders();
				//		 List<String> lines00 = csvResult00.getLines();
					//	 List<PublisherReport> pubreport00 = new ArrayList();  
						
						 
					//	System.out.println(headers00);
					//	System.out.println(lines00);  
						  
						//  for (int i = 0; i < lines00.size(); i++)
					    //  {
					       
					     //   String[] data = ((String)lines00.get(i)).split(",");
					  //      //System.out.println(data[0]);
					     
						  
						  
						  
					//	Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
					  //  String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
					    //  channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
					      CSVResult csvResult = getCsvResult(false, query00);
					      List<String> headers = csvResult.getHeaders();
					      List<String> lines = csvResult.getLines();
					      List<PublisherReport> pubreport = new ArrayList();
					 //     System.out.println(headers);
					 //     System.out.println(lines);
					      Double count = 0.0;
					      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
					      for (int i = 0; i < lines.size(); i++)
					      {
					       
					        
					        String[] data = ((String)lines.get(i)).split(",");
					        if (Double.parseDouble(data[1].trim()) > 7.0)
					        {
					        count++;
					        
					        }
					        
					       }
					    }  
					
					      PublisherReport obj = new PublisherReport();
					      obj.setCount(count.toString());
					      obj.setVisitorType("Loyal Visitors");
					      String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
					      pubreport.add(obj);
					  
					      
				          return pubreport;
						  }			 
				 
				
				  
				  
				  public List<PublisherReport> countLoyalUsersChannelDatewisegroupby(String startdate, String enddate, String channel_name,String groupby)
						    throws CsvExtractorException, Exception
						  {
					  String query00 = "SELECT COUNT(*)as count, cookie_id,"+groupby+" FROM enhanceduserdatabeta1 where channel_name = '" + 
						      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id,"+groupby+" limit 20000000";
					  
					
					  
					  if(groupby.equals("hour")){
				    		query00 =  "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id,date_histogram(field='request_time','interval'='1h') limit 20000000";
				    	}

	                
					  if(groupby.equals("minute")){
		                	query00 =  "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id,date_histogram(field='request_time','interval'='1m') limit 20000000";	
				    	}

				       
					  
					  
					  //	 CSVResult csvResult00 = getCsvResult(false, query00);
						// List<String> headers00 = csvResult00.getHeaders();
				//		 List<String> lines00 = csvResult00.getLines();
					//	 List<PublisherReport> pubreport00 = new ArrayList();  
						
						 
					//	System.out.println(headers00);
					//	System.out.println(lines00);  
						  
						//  for (int i = 0; i < lines00.size(); i++)
					    //  {
					       
					     //   String[] data = ((String)lines00.get(i)).split(",");
					  //      //System.out.println(data[0]);
					     
						  
						  
						  
					//	Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
					  //  String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
					    //  channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
					      CSVResult csvResult = getCsvResult(false, query00);
					      List<String> headers = csvResult.getHeaders();
					      List<String> lines = csvResult.getLines();
					      List<PublisherReport> pubreport = new ArrayList();
					//      System.out.println(headers);
					 //     System.out.println(lines);
					      Double count = 0.0;
					      Map<String,Double> dates =new HashMap<String,Double>();
					      String date = "";
					      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
					      for (int i = 0; i < lines.size(); i++)
					      {
					       
					        
					        String[] data = ((String)lines.get(i)).split(",");
					        if(data.length > 2){
					        date = data[1].trim();
					        if(dates.containsKey(date)==false)
					        dates.put(date,0.0);
					        if (Double.parseDouble(data[2].trim()) > 7.0)
					        {
					        count = dates.get(date);
					        dates.put(date,count+1);
					        
					         }
					        }  
					       }
					    }  
					
					      for(Map.Entry<String,Double>entry: dates.entrySet()){
					      PublisherReport obj = new PublisherReport();
					      if(groupby.equals("audience_segment"))
					             obj.setAudience_segment(entry.getKey());
				            	
				            	
				            	if(groupby.equals("gender"))
						             obj.setGender(entry.getKey());
				            	
				            	if(groupby.equals("hour"))
						             obj.setDate(entry.getKey());
				            	
				            	if(groupby.equals("minute"))
						             obj.setDate(entry.getKey());
				            	
				            	
				            	if(groupby.equals("gender"))
						             obj.setGender(entry.getKey());
				            	
				            	
				            	if(groupby.equals("refcurrentoriginal"))
						             obj.setGender(entry.getKey());
					            	
				            	if(groupby.equals("date"))
						             obj.setDate(entry.getKey());
					            		            	
				            	if(groupby.equals("subcategory"))
						             obj.setSubcategory(entry.getKey());
				            	
				            	if(groupby.equals("agegroup"))
						             obj.setAge(entry.getKey());
					            	
				            	if(groupby.equals("incomelevel"))
						          obj.setIncomelevel(entry.getKey());
					     
				            	if(groupby.equals("city")){
				            		String locationproperties = citycodeMap.get(entry.getKey());
				    		        String city =entry.getKey().replace("_"," ").replace("-"," ");
				    		        obj.setCity(city);
				    		        System.out.println(city);
				    		        obj.setLocationcode(locationproperties);
				            	}
							    
							    
					     obj.setCount(entry.getValue().toString());
					      String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
					      pubreport.add(obj);
					      }
					      
				          return pubreport;
						  }			 
				 
				  
				  
				  
				  
				  
				  public List<PublisherReport> counttotalvisitorsChannel(String startdate, String enddate, String channel_name)
						    throws CsvExtractorException, Exception
						  {
							  
							  
						//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
							  
						    
							  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
							  
							//	 CSVResult csvResult00 = getCsvResult(false, query00);
								// List<String> headers00 = csvResult00.getHeaders();
						//		 List<String> lines00 = csvResult00.getLines();
							//	 List<PublisherReport> pubreport00 = new ArrayList();  
								
								 
							//	System.out.println(headers00);
							//	System.out.println(lines00);  
								  
								//  for (int i = 0; i < lines00.size(); i++)
							    //  {
							       
							     //   String[] data = ((String)lines00.get(i)).split(",");
							  //      //System.out.println(data[0]);
							     
								  
								  
								  
								Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
							    String query = "SELECT count(*) as visits FROM enhanceduserdatabeta1 where channel_name = '" + 
							      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'";
							      CSVResult csvResult = getCsvResult(false, query);
							      List<String> headers = csvResult.getHeaders();
							      List<String> lines = csvResult.getLines();
							      List<PublisherReport> pubreport = new ArrayList();
							   //   System.out.println(headers);
							  //    System.out.println(lines);
							      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
							      for (int i = 0; i < lines.size(); i++)
							      {
							        PublisherReport obj = new PublisherReport();
							        
							        String[] data = ((String)lines.get(i)).split(",");
							       // obj.setDate(data[0]);
							        obj.setTotalvisits(data[0]);
							        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
							        
							        pubreport.add(obj);
							      }
							    }  
							    
						    return pubreport;
						  }
  
				  
				  public List<PublisherReport> countUniqueVisitorsChannel(String startdate, String enddate, String channel_name)
						    throws CsvExtractorException, Exception
						  {
							  
							  
						//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
							  
						    
							  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
								      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
							  
							//	 CSVResult csvResult00 = getCsvResult(false, query00);
								// List<String> headers00 = csvResult00.getHeaders();
						//		 List<String> lines00 = csvResult00.getLines();
							//	 List<PublisherReport> pubreport00 = new ArrayList();  
								
								 
							//	System.out.println(headers00);
							//	System.out.println(lines00);  
								  
								//  for (int i = 0; i < lines00.size(); i++)
							    //  {
							       
							     //   String[] data = ((String)lines00.get(i)).split(",");
							  //      //System.out.println(data[0]);
							     
								  
								  
								  
								Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
							    String query = "SELECT count(distinct(cookie_id))as reach FROM enhanceduserdatabeta1 where channel_name = '" + 
							      channel_name + "' and date between " + "'" + startdate + "'" + " and " + "'" + enddate + "'";
							      CSVResult csvResult = getCsvResult(false, query);
							      List<String> headers = csvResult.getHeaders();
							      List<String> lines = csvResult.getLines();
							      List<PublisherReport> pubreport = new ArrayList();
							  //    System.out.println(headers);
							   //   System.out.println(lines);
							      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
							      for (int i = 0; i < lines.size(); i++)
							      {
							        PublisherReport obj = new PublisherReport();
							        
							        String[] data = ((String)lines.get(i)).split(",");
							       // obj.setDate(data[0]);
							        obj.setReach(data[0]);
							        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
							        
							        pubreport.add(obj);
							      }
							    }  
							    
						    return pubreport;
						  }	  
				  
				  
				  
				  public List<PublisherReport> countBrandNameChannelLive(String startdate, String enddate, String channel_name)
				    throws CsvExtractorException, Exception
				  {
				    String query = "SELECT COUNT(*)as count,brandName FROM enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by brandName";
				    //System.out.println(query);
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        if(data[0].trim().toLowerCase().contains("logitech")==false && data[0].trim().toLowerCase().contains("mozilla")==false && data[0].trim().toLowerCase().contains("web_browser")==false && data[0].trim().toLowerCase().contains("microsoft")==false && data[0].trim().toLowerCase().contains("opera")==false && data[0].trim().toLowerCase().contains("epiphany")==false){ 
				        obj.setBrandname(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        pubreport.add(obj);
				        } 
				       }
				  //    //System.out.println(headers);
				  //    //System.out.println(lines);
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> countBrowserChannelLive(String startdate, String enddate, String channel_name)
				    throws CsvExtractorException, Exception
				  {
				    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
				    String query = "SELECT COUNT(*)as count,browser_name FROM enhanceduserdatabeta1 where channel_name ='" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by browser_name";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        obj.setBrowser(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        pubreport.add(obj);
				      }
				      //System.out.println(headers);
				      //System.out.println(lines);
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> countOSChannelLive(String startdate, String enddate, String channel_name)
				    throws CsvExtractorException, Exception
				  {
				    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
				    String query = String.format("SELECT COUNT(*)as count,system_os FROM enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by system_os", new Object[] { "enhanceduserdatabeta1" });
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				  //  //System.out.println(headers);
				  //  //System.out.println(lines);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        obj.setOs(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        pubreport.add(obj);
				      }
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> countModelChannelLive(String startdate, String enddate, String channel_name)
				    throws CsvExtractorException, Exception
				  {
				    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
				    String query = String.format("SELECT COUNT(*)as count,modelName FROM enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by modelName", new Object[] { "enhanceduserdatabeta1" });
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");

				        if(data[0].trim().toLowerCase().contains("logitech_revue")==false && data[0].trim().toLowerCase().contains("mozilla_firefox")==false && data[0].trim().toLowerCase().contains("apple_safari")==false && data[0].trim().toLowerCase().contains("generic_web")==false && data[0].trim().toLowerCase().contains("google_compute")==false && data[0].trim().toLowerCase().contains("microsoft_xbox")==false && data[0].trim().toLowerCase().contains("google_chromecast")==false && data[0].trim().toLowerCase().contains("opera")==false && data[0].trim().toLowerCase().contains("epiphany")==false && data[0].trim().toLowerCase().contains("laptop")==false){    
				        obj.setMobile_device_model_name(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        pubreport.add(obj);
				      }
				        
				        }
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> countCityChannelLive(String startdate, String enddate, String channel_name)
				    throws CsvExtractorException, Exception
				  {
				    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
				    String query = String.format("SELECT COUNT(*)as count,city FROM enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by city", new Object[] { "enhanceduserdatabeta1" });
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        obj.setCity(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        pubreport.add(obj);
				      }
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> countfingerprintChannelLive(String startdate, String enddate, String channel_name)
				    throws CsvExtractorException, Exception
				  {
					  
					  
					  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
					  
				    
					  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
						      channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
					  
						 CSVResult csvResult00 = getCsvResult(false, query00);
						 List<String> headers00 = csvResult00.getHeaders();
						 List<String> lines00 = csvResult00.getLines();
						 List<PublisherReport> pubreport00 = new ArrayList();  
							  
						//  //System.out.println(headers00);
						//  //System.out.println(lines00);  
						  
						  for (int i = 0; i < lines00.size(); i++)
					      {
					       
					        String[] data = ((String)lines00.get(i)).split(",");
					  //      //System.out.println(data[0]);
					      }
						  
						  
						  
						Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
					    String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where channel_name = '" + 
					      channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
					    CSVResult csvResult = getCsvResult(false, query);
					    List<String> headers = csvResult.getHeaders();
					    List<String> lines = csvResult.getLines();
					    List<PublisherReport> pubreport = new ArrayList();
					    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
					      for (int i = 0; i < lines.size(); i++)
					      {
					        PublisherReport obj = new PublisherReport();
					        
					        String[] data = ((String)lines.get(i)).split(",");
					        obj.setDate(data[0]);
					        obj.setReach(data[1]);
					        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
					        pubreport.add(obj);
					      }
					    }
					    
				    return pubreport;
				  }
				  
				  public List<PublisherReport> countAudiencesegmentChannelLive(String startdate, String enddate, String channel_name)
				    throws CsvExtractorException, Exception
				  {
				      List<PublisherReport> pubreport = new ArrayList(); 
					  
					  String querya1 = "SELECT COUNT(DISTINCT(cookie_id)) FROM enhanceduserdata where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate +"' limit 20000000";   
					  
					    //Divide count in different limits 
					
					  
					  List<String> Query = new ArrayList();
					  


					    System.out.println(querya1);
					    
					    final long startTime2 = System.currentTimeMillis();
						
					    
					    CSVResult csvResult1 = null;
						try {
							csvResult1 = AggregationModule.getCsvResult(false, querya1);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					    
					    final long endTime2 = System.currentTimeMillis();
						
					    List<String> headers = csvResult1.getHeaders();
					    List<String> lines = csvResult1.getLines();
					    
					    
					    String count = lines.get(0);
					    Double countv1 = Double.parseDouble(count);
					    Double n = 0.0;
					    if(countv1 >= 250000)
					       n=10.0;
					    
					    if(countv1 >= 100000 && countv1 <= 250000 )
					       n=10.0;
					    
					    if(countv1 <= 100000 && countv1 > 100)
				           n=10.0;	    
					   
					    if(countv1 <= 100)
					    	n=1.0;
					    
					    if(countv1 == 0)
					    {
					    	
					    	return pubreport;
					    	
					    }
					    
					    Double total_length = countv1 - 0;
					    Double subrange_length = total_length/n;	
					    
					    Double current_start = 0.0;
					    for (int i = 0; i < n; ++i) {
					      System.out.println("Smaller range: [" + current_start + ", " + (current_start + subrange_length) + "]");
					      Double startlimit = current_start;
					      Double finallimit = current_start + subrange_length;
					      Double index = startlimit +1;
					      if(countv1 == 1)
					    	  index=0.0;
					      String query = "SELECT DISTINCT(cookie_id) FROM enhanceduserdata where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "' Order by cookie_id limit "+index.intValue()+","+finallimit.intValue();  	
						  System.out.println(query);
					  //    Query.add(query);
					      current_start += subrange_length;
					      Query.add(query);
					     
					    }
					    
					    
					    	
					    
					  
					  ExecutorService executorService = Executors.newFixedThreadPool(2000);
				        
				       List<Callable<FastMap<String,Double>>> lst = new ArrayList<Callable<FastMap<String,Double>>>();
				    
				       for(int i=0 ; i < Query.size(); i++ ){
				       lst.add(new AudienceSegmentQueryExecutionThreads(Query.get(i),client,searchDao));
				    /*   lst.add(new AudienceSegmentQueryExecutionThreads(query1,client,searchDao));
				       lst.add(new AudienceSegmentQueryExecutionThreads(query2,client,searchDao));
				       lst.add(new AudienceSegmentQueryExecutionThreads(query3,client,searchDao));
				       lst.add(new AudienceSegmentQueryExecutionThreads(query4,client,searchDao));*/
				        
				       // returns a list of Futures holding their status and results when all complete
				       lst.add(new SubcategoryQueryExecutionThreads(Query.get(i),client,searchDao));
				   /*    lst.add(new SubcategoryQueryExecutionThreads(query6,client,searchDao));
				       lst.add(new SubcategoryQueryExecutionThreads(query7,client,searchDao));
				       lst.add(new SubcategoryQueryExecutionThreads(query8,client,searchDao));
				       lst.add(new SubcategoryQueryExecutionThreads(query9,client,searchDao)); */
				       }
				       
				       
				       List<Future<FastMap<String,Double>>> maps = executorService.invokeAll(lst);
				        
				       System.out.println(maps.size() +" Responses recieved.\n");
				        
				       for(Future<FastMap<String,Double>> task : maps)
				       {
				    	   try{
				           if(task!=null)
				    	   System.out.println(task.get().toString());
				    	   }
				    	   catch(Exception e)
				    	   {
				    		   e.printStackTrace();
				    		   continue;
				    	   }
				    	    
				    	   
				    	   }
				        
				       /* shutdown your thread pool, else your application will keep running */
				       executorService.shutdown();
					  
					
					  //  //System.out.println(headers1);
					 //   //System.out.println(lines1);
					    
					    
				       
				       FastMap<String,Double> audiencemap = new FastMap<String,Double>();
				       
				       FastMap<String,Double> subcatmap = new FastMap<String,Double>();
				       
				       Double count1 = 0.0;
				       
				       Double count2 = 0.0;
				       
				       String key ="";
				       String key1 = "";
				       Double value = 0.0;
				       Double vlaue1 = 0.0;
				       
					    for (int i = 0; i < maps.size(); i++)
					    {
					    
					    	if(maps!=null && maps.get(i)!=null){
					        FastMap<String,Double> map = (FastMap<String, Double>) maps.get(i).get();
					    	
					       if(map.size() > 0){
					       
					       if(map.containsKey("audience_segment")==true){
					       for (Map.Entry<String, Double> entry : map.entrySet())
					    	 {
					    	  key = entry.getKey();
					    	  key = key.trim();
					    	  value=  entry.getValue();
					    	if(key.equals("audience_segment")==false) { 
					    	if(audiencemap.containsKey(key)==false)
					    	audiencemap.put(key,value);
					    	else
					    	{
					         count1 = audiencemap.get(key);
					         if(count1!=null)
					         audiencemap.put(key,count1+value);	
					    	}
					      }
					    }
					  }   

					       if(map.containsKey("subcategory")==true){
					       for (Map.Entry<String, Double> entry : map.entrySet())
					    	 {
					    	   key = entry.getKey();
					    	   key = key.trim();
					    	   value=  entry.getValue();
					    	if(key.equals("subcategory")==false) {    
					    	if(subcatmap.containsKey(key)==false)
					    	subcatmap.put(key,value);
					    	else
					    	{
					         count1 = subcatmap.get(key);
					         if(count1!=null)
					         subcatmap.put(key,count1+value);	
					    	}
					    }  
					    	
					   }
					      
					     	       }
					           
					       } 
					    
					    	} 	
					   }    
					    
					    String subcategory = null;
					   
					    if(audiencemap.size()>0){
					   
					    	for (Map.Entry<String, Double> entry : audiencemap.entrySet()) {
					    	//System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
					    

					        PublisherReport obj = new PublisherReport();
					        
					   //     String[] data = ((String)lines.get(i)).split(",");
					        
					     //   if(data[0].trim().toLowerCase().contains("festivals"))
					      //  obj.setAudience_segment("");
					      //  else
					        obj.setAudience_segment( entry.getKey());	
					        obj.setCount(String.valueOf(entry.getValue()));
					      
					        if ((!entry.getKey().equals("tech")) && (!entry.getKey().equals("india")) && (!entry.getKey().trim().toLowerCase().equals("foodbeverage")) )
					        {
					         for (Map.Entry<String, Double> entry1 : subcatmap.entrySet()) {
					        	 
					        	    
					        	 
					        	 PublisherReport obj1 = new PublisherReport();
					            
					           
					            if (entry1.getKey().contains(entry.getKey()))
					            {
					              String substring = "_" + entry.getKey() + "_";
					              subcategory = entry1.getKey().replace(substring, "");
					           //   if(data[0].trim().toLowerCase().contains("festivals"))
					           //   obj1.setAudience_segment("");
					           //   else
					        
					              //System.out.println(" \n\n\n Key : " + subcategory + " Value : " + entry1.getValue());  
					              obj1.setAudience_segment(subcategory);
					              obj1.setCount(String.valueOf(entry1.getValue()));
					              obj.getAudience_segment_data().add(obj1);
					            }
					          }
					          pubreport.add(obj);
					        }
					      
					    }
					    }
					    return pubreport;
				  }
				  
				  public List<PublisherReport> gettimeofdayChannelLive(String startdate, String enddate, String channel_name)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query = "Select count(*) from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1h')";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        obj.setTime_of_day(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        pubreport.add(obj);
				      }
				      //System.out.println(headers);
				      //System.out.println(lines);
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> countPinCodeChannelLive(String startdate, String enddate, String channel_name)
				    throws CsvExtractorException, Exception
				  {
				    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
				    String query = "SELECT COUNT(*)as count,postalcode FROM enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by postalcode";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    //System.out.println(headers);
				    //System.out.println(lines);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
				      for (int i = 0; i < lines.size(); i++)
				      {
				    	  PublisherReport obj = new PublisherReport();
					        
					        String[] data = ((String)lines.get(i)).split(",");
					        String[] data1 = data[0].split("_");
					        String locationproperties  = citycodeMap.get(data1[0]);
					        obj.setPostalcode(data[0]);
					        obj.setCount(data[1]);
					        obj.setLocationcode(locationproperties);
					        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				            pubreport.add(obj);
				      }
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> countLatLongChannelLive(String startdate, String enddate, String channel_name)
				    throws CsvExtractorException, Exception
				  {
				    Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
				    String query = String.format("SELECT COUNT(*)as count,latitude_longitude FROM enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by latitude_longitude", new Object[] { "enhanceduserdatabeta1" });
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    //System.out.println(headers);
				    //System.out.println(lines);
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        String[] dashcount = data[0].split("_");
				        if ((dashcount.length == 3) && (data[0].charAt(data[0].length() - 1) != '_'))
				        {
				          if (!dashcount[2].isEmpty())
				          {
				            obj.setLatitude_longitude(data[0]);
				            obj.setCount(data[1]);
				            String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				          }
				          pubreport.add(obj);
				        }
				      }
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> gettimeofdayQuarterChannelLive(String startdate, String enddate, String channel_name)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query = "Select count(*) from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='4h')";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        obj.setTime_of_day(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        pubreport.add(obj);
				      }
				      //System.out.println(headers);
				      //System.out.println(lines);
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> gettimeofdayDailyChannelLive(String startdate, String enddate, String channel_name)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query = "Select count(*) from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY date_histogram(field='request_time','interval'='1d')";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        obj.setTime_of_day(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        pubreport.add(obj);
				      }
				      //System.out.println(headers);
				      //System.out.println(lines);
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> getdayQuarterdataChannelLive(String startdate, String enddate, String channel_name)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query = "Select count(*),QuarterValue from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY QuarterValue";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        if (data[0].equals("quarter1")) {
				          data[0] = "quarter1 (00 - 04 AM)";
				        }
				        if (data[0].equals("quarter2")) {
				          data[0] = "quarter2 (04 - 08 AM)";
				        }
				        if (data[0].equals("quarter3")) {
				          data[0] = "quarter3 (08 - 12 AM)";
				        }
				        if (data[0].equals("quarter4")) {
				          data[0] = "quarter4 (12 - 16 PM)";
				        }
				        if (data[0].equals("quarter5")) {
				          data[0] = "quarter5 (16 - 20 PM)";
				        }
				        if (data[0].equals("quarter6")) {
				          data[0] = "quarter6 (20 - 24 PM)";
				        }
				        obj.setTime_of_day(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        pubreport.add(obj);
				      }
				      //System.out.println(headers);
				      //System.out.println(lines);
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> getGenderChannelLive(String startdate, String enddate, String channel_name)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query = "Select count(*),gender from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY gender";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    List<PublisherReport> pubreport = new ArrayList();
				    
				    //System.out.println(headers);
				    //System.out.println(lines);
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        obj.setGender(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        pubreport.add(obj);
				      }
				      //System.out.println(headers);
				      //System.out.println(lines);
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> getAgegroupChannelLive(String startdate, String enddate, String channel_name)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query = "Select count(*),agegroup from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY agegroup";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        obj.setAge(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        pubreport.add(obj);
				      }
				      //System.out.println(headers);
				      //System.out.println(lines);
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> getISPChannelLive(String startdate, String enddate, String channel_name)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query = "Select count(*),ISP from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY ISP";
				    CSVResult csvResult = getCsvResult(false, query);
				    List<String> headers = csvResult.getHeaders();
				    List<String> lines = csvResult.getLines();
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data = ((String)lines.get(i)).split(",");
				        if(data[0].trim().toLowerCase().equals("_ltd")==false){ 
				        obj.setISP(data[0]);
				        obj.setCount(data[1]);
				        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				        pubreport.add(obj);
				         }
				        }
				      //System.out.println(headers);
				      //System.out.println(lines);
				    }
				    return pubreport;
				  }
				  
				  public List<PublisherReport> getOrgChannelLive(String startdate, String enddate, String channel_name)
				    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  {
				    String query1 = "Select count(*),organisation from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY organisation";
				    CSVResult csvResult1 = getCsvResult(false, query1);
				    List<String> headers1 = csvResult1.getHeaders();
				    List<String> lines1 = csvResult1.getLines();
				    List<PublisherReport> pubreport = new ArrayList();
				    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
				    {
				      for (int i = 0; i < lines1.size(); i++)
				      {
				        PublisherReport obj = new PublisherReport();
				        
				        String[] data1 = ((String)lines1.get(i)).split(",");
				        if ((data1[0].length() > 3) && (data1[0].charAt(0) != '_') && (!data1[0].trim().toLowerCase().contains("broadband")) && (!data1[0].trim().toLowerCase().contains("communication")) && (!data1[0].trim().toLowerCase().contains("cable")) && (!data1[0].trim().toLowerCase().contains("telecom")) && (!data1[0].trim().toLowerCase().contains("network")) && (!data1[0].trim().toLowerCase().contains("isp")) && (!data1[0].trim().toLowerCase().contains("hathway")) && (!data1[0].trim().toLowerCase().contains("internet")) && (!data1[0].trim().toLowerCase().equals("_ltd")) && (!data1[0].trim().toLowerCase().contains("googlebot")) && (!data1[0].trim().toLowerCase().contains("sify")) && (!data1[0].trim().toLowerCase().contains("bsnl")) && (!data1[0].trim().toLowerCase().contains("reliance")) && (!data1[0].trim().toLowerCase().contains("broadband")) && (!data1[0].trim().toLowerCase().contains("tata")) && (!data1[0].trim().toLowerCase().contains("nextra")))
				        {
				          obj.setOrganisation(data1[0]);
				          obj.setCount(data1[1]);
				          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				          pubreport.add(obj);
				        }
				      }
				      //System.out.println(headers1);
				      //System.out.println(lines1);
				    }
				    return pubreport;
				  }
				  
				    
				  				  public List<PublisherReport> countNewUsersChannelLiveDatewise(String startdate, String enddate, String channel_name)
				  						    throws CsvExtractorException, Exception
				  						  {
				  							  
				  							  
				  						//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
				  							  
				  						    
				  							  String query00 = "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
				  								      channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
				  							  
				  							//	 CSVResult csvResult00 = getCsvResult(false, query00);
				  								// List<String> headers00 = csvResult00.getHeaders();
				  						//		 List<String> lines00 = csvResult00.getLines();
				  							//	 List<PublisherReport> pubreport00 = new ArrayList();  
				  								
				  								 
				  							//	System.out.println(headers00);
				  							//	System.out.println(lines00);  
				  								  
				  								//  for (int i = 0; i < lines00.size(); i++)
				  							    //  {
				  							       
				  							     //   String[] data = ((String)lines00.get(i)).split(",");
				  							  //      //System.out.println(data[0]);
				  							     
				  								  
				  								  
				  								  
				  							//	Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
				  							  //  String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
				  							    //  channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
				  							      CSVResult csvResult = getCsvResult(false, query00);
				  							      List<String> headers = csvResult.getHeaders();
				  							      List<String> lines = csvResult.getLines();
				  							      List<PublisherReport> pubreport = new ArrayList();
				  							//      System.out.println(headers);
				  							 //     System.out.println(lines);
				  							      Double count = 0.0;
				  							      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
				  							      for (int i = 0; i < lines.size(); i++)
				  							      {
				  							       
				  							        
				  							        String[] data = ((String)lines.get(i)).split(",");
				  							        if (Double.parseDouble(data[1].trim()) < 2.0)
				  							        {
				  							        count++;
				  							        
				  							        }
				  							        
				  							       }
				  							    }  
				  							
				  							      PublisherReport obj = new PublisherReport();
				  							      obj.setCount(count.toString());
				  							      obj.setVisitorType("New Visitors");
				  							      String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				  							      pubreport.add(obj);
				  							   
				  							      
				  						    return pubreport;
				  						  }
				  				  
				  				  
				  				  public List<PublisherReport> countReturningUsersChannelLiveDatewise(String startdate, String enddate, String channel_name)
				  						    throws CsvExtractorException, Exception
				  						  {
				  							  
				  							  
				  					  String query00 = "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
				  						      channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
				  					  
				  					//	 CSVResult csvResult00 = getCsvResult(false, query00);
				  						// List<String> headers00 = csvResult00.getHeaders();
				  				//		 List<String> lines00 = csvResult00.getLines();
				  					//	 List<PublisherReport> pubreport00 = new ArrayList();  
				  						
				  						 
				  					//	System.out.println(headers00);
				  					//	System.out.println(lines00);  
				  						  
				  						//  for (int i = 0; i < lines00.size(); i++)
				  					    //  {
				  					       
				  					     //   String[] data = ((String)lines00.get(i)).split(",");
				  					  //      //System.out.println(data[0]);
				  					     
				  						  
				  						  
				  						  
				  					//	Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
				  					  //  String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
				  					    //  channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
				  					      CSVResult csvResult = getCsvResult(false, query00);
				  					      List<String> headers = csvResult.getHeaders();
				  					      List<String> lines = csvResult.getLines();
				  					      List<PublisherReport> pubreport = new ArrayList();
				  					   //   System.out.println(headers);
				  					   //   System.out.println(lines);
				  					      Double count = 0.0;
				  					      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
				  					      for (int i = 0; i < lines.size(); i++)
				  					      {
				  					       
				  					        
				  					        String[] data = ((String)lines.get(i)).split(",");
				  					        if (Double.parseDouble(data[1].trim()) >= 2.0)
				  					        {
				  					        count++;
				  					        
				  					        }
				  					        
				  					       }
				  					    }  
				  					
				  					      PublisherReport obj = new PublisherReport();
				  					      obj.setCount(count.toString());
				  					      obj.setVisitorType("Returning Visitors");
				  					      String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				  					      pubreport.add(obj);
				  					      
				  				          return pubreport;
				  						  }
				  				  
				  				  
				  				  
				  				  public List<PublisherReport> countLoyalUsersChannelLiveDatewise(String startdate, String enddate, String channel_name)
				  						    throws CsvExtractorException, Exception
				  						  {
				  					  String query00 = "SELECT COUNT(*)as count, cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
				  						      channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
				  					  
				  					//	 CSVResult csvResult00 = getCsvResult(false, query00);
				  						// List<String> headers00 = csvResult00.getHeaders();
				  				//		 List<String> lines00 = csvResult00.getLines();
				  					//	 List<PublisherReport> pubreport00 = new ArrayList();  
				  						
				  						 
				  					//	System.out.println(headers00);
				  					//	System.out.println(lines00);  
				  						  
				  						//  for (int i = 0; i < lines00.size(); i++)
				  					    //  {
				  					       
				  					     //   String[] data = ((String)lines00.get(i)).split(",");
				  					  //      //System.out.println(data[0]);
				  					     
				  						  
				  						  
				  						  
				  					//	Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
				  					  //  String query = "SELECT count(distinct(cookie_id))as reach,date FROM enhanceduserdatabeta1 where refcurrentoriginal like '%"+sectionname+"%' and channel_name = '" + 
				  					    //  channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " group by date";
				  					      CSVResult csvResult = getCsvResult(false, query00);
				  					      List<String> headers = csvResult.getHeaders();
				  					      List<String> lines = csvResult.getLines();
				  					      List<PublisherReport> pubreport = new ArrayList();
				  					 //     System.out.println(headers);
				  					 //     System.out.println(lines);
				  					      Double count = 0.0;
				  					      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
				  					      for (int i = 0; i < lines.size(); i++)
				  					      {
				  					       
				  					        
				  					        String[] data = ((String)lines.get(i)).split(",");
				  					        if (Double.parseDouble(data[1].trim()) > 7.0)
				  					        {
				  					        count++;
				  					        
				  					        }
				  					        
				  					       }
				  					    }  
				  					
				  					      PublisherReport obj = new PublisherReport();
				  					      obj.setCount(count.toString());
				  					      obj.setVisitorType("Loyal Visitors");
				  					      String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				  					      pubreport.add(obj);
				  					  
				  					      
				  				          return pubreport;
				  						  }			 
				  				 
				  				
				  				  
				  				  public List<PublisherReport> counttotalvisitorsChannelLive(String startdate, String enddate, String channel_name)
				  						    throws CsvExtractorException, Exception
				  						  {
				  							  
				  							  
				  						//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
				  							  
				  						    
				  							  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
				  								      channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
				  							  
				  							//	 CSVResult csvResult00 = getCsvResult(false, query00);
				  								// List<String> headers00 = csvResult00.getHeaders();
				  						//		 List<String> lines00 = csvResult00.getLines();
				  							//	 List<PublisherReport> pubreport00 = new ArrayList();  
				  								
				  								 
				  							//	System.out.println(headers00);
				  							//	System.out.println(lines00);  
				  								  
				  								//  for (int i = 0; i < lines00.size(); i++)
				  							    //  {
				  							       
				  							     //   String[] data = ((String)lines00.get(i)).split(",");
				  							  //      //System.out.println(data[0]);
				  							     
				  								  
				  								  
				  								  
				  								Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
				  							    String query = "SELECT count(*) as visits FROM enhanceduserdatabeta1 where channel_name = '" + 
				  							      channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'";
				  							      CSVResult csvResult = getCsvResult(false, query);
				  							      List<String> headers = csvResult.getHeaders();
				  							      List<String> lines = csvResult.getLines();
				  							      List<PublisherReport> pubreport = new ArrayList();
				  							   //   System.out.println(headers);
				  							  //    System.out.println(lines);
				  							      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
				  							      for (int i = 0; i < lines.size(); i++)
				  							      {
				  							        PublisherReport obj = new PublisherReport();
				  							        
				  							        String[] data = ((String)lines.get(i)).split(",");
				  							       // obj.setDate(data[0]);
				  							        obj.setTotalvisits(data[0]);
				  							        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				  							        
				  							        pubreport.add(obj);
				  							      }
				  							    }  
				  							    
				  						    return pubreport;
				  						  }
				    
				  				  
				  				  public List<PublisherReport> countUniqueVisitorsChannelLive(String startdate, String enddate, String channel_name)
				  						    throws CsvExtractorException, Exception
				  						  {
				  							  
				  							  
				  						//	  System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
				  							  
				  						    
				  							  String query00 = "SELECT cookie_id FROM enhanceduserdatabeta1 where channel_name = '" + 
				  								      channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" +"group by cookie_id limit 20000000";
				  							  
				  							//	 CSVResult csvResult00 = getCsvResult(false, query00);
				  								// List<String> headers00 = csvResult00.getHeaders();
				  						//		 List<String> lines00 = csvResult00.getLines();
				  							//	 List<PublisherReport> pubreport00 = new ArrayList();  
				  								
				  								 
				  							//	System.out.println(headers00);
				  							//	System.out.println(lines00);  
				  								  
				  								//  for (int i = 0; i < lines00.size(); i++)
				  							    //  {
				  							       
				  							     //   String[] data = ((String)lines00.get(i)).split(",");
				  							  //      //System.out.println(data[0]);
				  							     
				  								  
				  								  
				  								  
				  								Aggregations result = query(String.format("SELECT COUNT(*),brandName,browser_name FROM enhanceduserdatabeta1 group by brandName,browser_name", new Object[] { "enhanceduserprofilestore" }));
				  							    String query = "SELECT count(distinct(cookie_id))as reach FROM enhanceduserdatabeta1 where channel_name = '" + 
				  							      channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'";
				  							      CSVResult csvResult = getCsvResult(false, query);
				  							      List<String> headers = csvResult.getHeaders();
				  							      List<String> lines = csvResult.getLines();
				  							      List<PublisherReport> pubreport = new ArrayList();
				  							  //    System.out.println(headers);
				  							   //   System.out.println(lines);
				  							      if ((lines != null) && (!lines.isEmpty()) && (!((String)lines.get(0)).isEmpty())) {
				  							      for (int i = 0; i < lines.size(); i++)
				  							      {
				  							        PublisherReport obj = new PublisherReport();
				  							        
				  							        String[] data = ((String)lines.get(i)).split(",");
				  							       // obj.setDate(data[0]);
				  							        obj.setReach(data[0]);
				  							        String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				  							        
				  							        pubreport.add(obj);
				  							      }
				  							    }  
				  							    
				  						    return pubreport;
				  						  }	  
								  	  
				  				public List<PublisherReport> getTopPostsbyTotalPageviewschannelLive(String startdate, String enddate, String channel_name)
				  					    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  					  {
				  					    String query1 = "Select count(*),refcurrentoriginal from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY refcurrentoriginal";
				  					    CSVResult csvResult1 = getCsvResult(false, query1);
				  					    List<String> headers1 = csvResult1.getHeaders();
				  					    List<String> lines1 = csvResult1.getLines();
				  					    System.out.println(headers1);
				  					      System.out.println(lines1);
				  					    List<PublisherReport> pubreport = new ArrayList();
				  					    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
				  					    {
				  					      for (int i = 0; i < lines1.size(); i++)
				  					      {
				  					        PublisherReport obj = new PublisherReport();
				  					        
				  					        String[] data1 = ((String)lines1.get(i)).split(",");
				  					          String articleparts[] = data1[0].split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle); obj.setPublisher_pages(data1[0]);
				  					          obj.setCount(data1[1]);
				  					          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				  					          
				  					          pubreport.add(obj);
				  					        
				  					      }
				  					    //  System.out.println(headers1);
				  					    //  System.out.println(lines1);
				  					    }
				  					    return pubreport;
				  					  }
								  	  			  
				  				
				  				public List<PublisherReport> getTopPostsbyUniqueViewschannelLive(String startdate, String enddate, String channel_name)
				  					    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  					  {
				  					    String query1 = "Select count(distinct(cookies)),refcurrentoriginal from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY refcurrentoriginal";
				  					    CSVResult csvResult1 = getCsvResult(false, query1);
				  					    List<String> headers1 = csvResult1.getHeaders();
				  					    List<String> lines1 = csvResult1.getLines();
				  					    System.out.println(headers1);
				  					      System.out.println(lines1);
				  					    List<PublisherReport> pubreport = new ArrayList();
				  					    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
				  					    {
				  					      for (int i = 0; i < lines1.size(); i++)
				  					      {
				  					        PublisherReport obj = new PublisherReport();
				  					        
				  					        String[] data1 = ((String)lines1.get(i)).split(",");
				  					          String articleparts[] = data1[0].split("/"); String articleTitle = articleparts[articleparts.length-1]; obj.setArticleTitle(articleTitle); obj.setPublisher_pages(data1[0]);
				  					          obj.setCount(data1[1]);
				  					          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				  					          
				  					          pubreport.add(obj);
				  					        
				  					      }
				  					    //  System.out.println(headers1);
				  					    //  System.out.println(lines1);
				  					    }
				  					    return pubreport;
				  					  }
								  	  			  
				  				  				
				
				  				public List<PublisherReport> getRefererPostsChannelLive(String startdate, String enddate, String channel_name)
				  					    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  					  {
				  					 String query1 = "Select count(*),refcurrentoriginal from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY refcurrentoriginal";
				  					    CSVResult csvResult1 = getCsvResult(false, query1);
				  					    List<String> headers1 = csvResult1.getHeaders();
				  					    List<String> lines1 = csvResult1.getLines();
				  					    System.out.println(headers1);
				  					      System.out.println(lines1);
				  					    List<PublisherReport> pubreport = new ArrayList();
				  					    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
				  					    {
				  					      for (int i = 0; i < lines1.size(); i++)
				  					      {
				  					        PublisherReport obj = new PublisherReport();
				  					        
				  					        String[] data1 = ((String)lines1.get(i)).split(",");
				  					        if ((data1[0].trim().toLowerCase().contains("facebook") || (data1[0].trim().toLowerCase().contains("google"))))
				  					        {
				  					          obj.setReferrerSource(data1[0]);
				  					          obj.setCount(data1[1]);
				  					          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				  					         
				  					          pubreport.add(obj);
				  					        }
				  					      }
				  					    //  System.out.println(headers1);
				  					    //  System.out.println(lines1);
				  					    }
				  					    return pubreport;
				  					  }
				  				
				  				public List<PublisherReport> getNewContentChannelLive(String startdate, String enddate, String channel_name)
				  					    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  					  {
				  					    String query1 = "Select refcurrentoriginal from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY refcurrentoriginal";
				  					    CSVResult csvResult1 = getCsvResult(false, query1);
				  					    List<String> headers1 = csvResult1.getHeaders();
				  					    List<String> lines1 = csvResult1.getLines();
				  					    System.out.println(headers1);
				  					      System.out.println(lines1);
				  				
				  					    String query2 = "Select refcurrentoriginal from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time < " + "'" + startdate + "' GROUP BY refcurrentoriginal";
				  					    CSVResult csvResult2 = getCsvResult(false, query2);
				  					    List<String> headers2 = csvResult2.getHeaders();
				  					    List<String> lines2 = csvResult2.getLines();
				  					    System.out.println(headers1);
				  					      System.out.println(lines1);
				  				     
				  					     Set<String> list2 = new HashSet<String>();
				  					     list2.addAll(lines2);
				  					      
				  					     for(int i=0;i<lines1.size();i++){
				  					    	 
				  					    	 if(list2.contains(lines1.get(i))){
				  					    		 lines1.remove(i);
				  					    		 
				  					     }
				  					    	 
				  					   }
				  					     
				  					     
				  					      
				  					      List<PublisherReport> pubreport = new ArrayList();
				  					    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
				  					    {
				  					      for (int i = 0; i < lines1.size(); i++)
				  					      {
				  					        PublisherReport obj = new PublisherReport();
				  					        
				  					        String data1 = (String)lines1.get(i);
				  					      
				  					          obj.setPublisher_pages(data1);
				  					          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				  					         
				  					          pubreport.add(obj);
				  					       
				  					      }
				  					    //  System.out.println(headers1);
				  					    //  System.out.println(lines1);
				  					    }
				  					    return pubreport;
				  					  }	
				  				
				  				public List<PublisherReport> getNewContentCountChannelLive(String startdate, String enddate, String channel_name)
				  					    throws SQLFeatureNotSupportedException, SqlParseException, CsvExtractorException, Exception
				  					  {
				  					    String query1 = "Select refcurrentoriginal from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time between " + "'" + startdate + "'" + " and " + "'" + enddate + "'" + " GROUP BY refcurrentoriginal";
				  					    CSVResult csvResult1 = getCsvResult(false, query1);
				  					    List<String> headers1 = csvResult1.getHeaders();
				  					    List<String> lines1 = csvResult1.getLines();
				  					    System.out.println(headers1);
				  					      System.out.println(lines1);
				  				
				  					    String query2 = "Select refcurrentoriginal from enhanceduserdatabeta1 where channel_name = '" + channel_name + "' and request_time < " + "'" + startdate + "' GROUP BY refcurrentoriginal";
				  					    CSVResult csvResult2 = getCsvResult(false, query2);
				  					    List<String> headers2 = csvResult2.getHeaders();
				  					    List<String> lines2 = csvResult2.getLines();
				  					    System.out.println(headers1);
				  					      System.out.println(lines1);
				  				     
				  					     Set<String> list2 = new HashSet<String>();
				  					     list2.addAll(lines2);
				  					      
				  					     for(int i=0;i<lines1.size();i++){
				  					    	 
				  					    	 if(list2.contains(lines1.get(i))){
				  					    		 lines1.remove(i);
				  					    		 
				  					     }
				  					    	 
				  					   }
				  					     
				  					     
				  					      
				  					      List<PublisherReport> pubreport = new ArrayList();
				  					    if ((lines1 != null) && (!lines1.isEmpty()) && (!((String)lines1.get(0)).isEmpty()))
				  					    {
				  					      
				  					          PublisherReport obj = new PublisherReport();
				  					        
				  					      
				  					          Integer newContent = lines1.size();
				  					          obj.setCount(newContent.toString());
				  					          String[] channels = channel_name.split("_");String channel_name1 = channels[0];channel_name1 = capitalizeString(channel_name1);obj.setChannelName(channel_name1);
				  					         
				  					          pubreport.add(obj);
				  					       
				  					     
				  					    //  System.out.println(headers1);
				  					    //  System.out.println(lines1);
				  					    }
				  					    return pubreport;
				  					  }	
				  				
				  			  
				  				
				  				
				  				
  
  public static CSVResult getCsvResult(boolean flat, String query)
    throws SqlParseException, SQLFeatureNotSupportedException, Exception, CsvExtractorException
  {
    return getCsvResult(flat, query, false, false);
  }
  
  public static CSVResult getCsvResult(boolean flat, String query, boolean includeScore, boolean includeType)
    throws SqlParseException, SQLFeatureNotSupportedException, Exception, CsvExtractorException
  {
    SearchDao searchDao = getSearchDao();
    QueryAction queryAction = searchDao.explain(query);
    Object execution = QueryActionElasticExecutor.executeAnyAction(searchDao.getClient(), queryAction);
    return new CSVResultsExtractor(includeScore, includeType).extractResults(execution, flat, ",");
  }
  
  public static void sumTest()
    throws IOException, SqlParseException, SQLFeatureNotSupportedException
  {}
  
  private static Aggregations query(String query)
    throws SqlParseException, SQLFeatureNotSupportedException
  {
    SqlElasticSearchRequestBuilder select = getSearchRequestBuilder(query);
    return ((SearchResponse)select.get()).getAggregations();
  }
  
  private static SqlElasticSearchRequestBuilder getSearchRequestBuilder(String query)
    throws SqlParseException, SQLFeatureNotSupportedException
  {
    SearchDao searchDao = getSearchDao();
    return (SqlElasticSearchRequestBuilder)searchDao.explain(query).explain();
  }
  
  private static InetSocketTransportAddress getTransportAddress()
  {
    String host = System.getenv("ES_TEST_HOST");
    String port = System.getenv("ES_TEST_PORT");
    if (host == null)
    {
      host = "172.16.101.132";
      //System.out.println("ES_TEST_HOST enviroment variable does not exist. choose default 'localhost'");
    }
    if (port == null)
    {
      port = "9300";
      //System.out.println("ES_TEST_PORT enviroment variable does not exist. choose default '9300'");
    }
    //System.out.println(String.format("Connection details: host: %s. port:%s.", new Object[] { host, port }));
    return new InetSocketTransportAddress(host, Integer.parseInt(port));
  }
}
