package com.publisherdata.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Aggregator7{


public static GenericTree<String> groupBy(List<PublisherReport>pubreport){
	
	//if(keys.size()==0)
		//return pubreport;
	
	

	GenericTree<String> tree = new GenericTree<String>(); 
	
    int start = 0;
	
    String country = "";
    String state = "";
    String city = "";
	String count = "";
    
	for(int i=0; i<pubreport.size(); i++){
		
		PublisherReport report = pubreport.get(i);
       
        
		country = report.getCountry();	
	    state = report.getState();
	    city = report.getCity();
		count = report.getCount();
		
	   
        GenericTreeNode<String> rootA = new GenericTreeNode<String>("root");
        GenericTreeNode<String> childB = new GenericTreeNode<String>(country);
        GenericTreeNode<String> childC = new GenericTreeNode<String>(state);
        GenericTreeNode<String> childD = new GenericTreeNode<String>(city);
      
        rootA.addChild(childB);
        childB.addChild(childC);
        childC.addChild(childD);
	
        tree.setRoot(rootA);
        
	}
   
	
	return tree;
        
 }



}
	
	 
       
 	 
        	 
        	 
        

        





