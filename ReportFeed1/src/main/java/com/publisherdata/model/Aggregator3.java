package com.publisherdata.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Aggregator3{


public static List<Country> groupBy(List<PublisherReport>pubreport){
	
	//if(keys.size()==0)
		//return pubreport;
	
	

	List<Country> countries = new ArrayList<Country>();
	
    int start = 0;
	
    String country = "";
    String state = "";
    String city = "";
	
	for(int i=0; i<pubreport.size(); i++){
		
		PublisherReport report = pubreport.get(i);
       
        
		country = report.getCountry();	
	    state = report.getState();
	    city = report.getCity();
		
		
       for(int k=0; k<pubreport.size();k++){
        	if(countries.get(k).name==null){
        		countries.get(k).name = country;
        	    countries.get(k).count = countries.get(k).count + Integer.parseInt(report.getCount());
     	        break;
        	}	
      

        	if(countries.get(k).name.equals(country)){
        	   countries.get(k).count = countries.get(k).count + Integer.parseInt(report.getCount());
               break;
        	   
        	}
         }
        	
         }
        
       System.out.println(countries);
	
       for(int i=0; i<pubreport.size(); i++){
		
		PublisherReport report = pubreport.get(i);
       
        
		country = report.getCountry();	
	    state = report.getState();
	    city = report.getCity();
		
		
       
        	
      

        	if(countries.get(i).name.equals(country)){
        		
        
           for(int j=0; j< pubreport.size(); j++){
        		
        		if(countries.get(i).states.get(j).name.equals(state)){
             	   countries.get(i).states.get(j).count = countries.get(i).states.get(j).count + Integer.parseInt(report.getCount());
                   
             	   
             	}
        		
        		
        		if(countries.get(i).states.get(j).name==null){
            		countries.get(i).states.get(j).name = state;
            	    countries.get(i).states.get(j).count = countries.get(i).states.get(j).count + Integer.parseInt(report.getCount());
         	
            	}	
          

            	
              
        	   
        	}
        	
        
        	
        	
        	
        	
        	
        	
        	
        	
        	
        	}

















}
	

return countries;

}

}
        
        
	
	 
       
 	 
        	 
        	 
        

        





