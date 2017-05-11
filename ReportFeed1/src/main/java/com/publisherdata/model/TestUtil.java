package com.publisherdata.model;

import java.util.ArrayList;
import java.util.List;



public class TestUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	  List<PublisherReport> rep = new ArrayList<PublisherReport>();	
	  
	
	  PublisherReport obj1  = new PublisherReport();
		
	  obj1.setCountry("IN");
	  obj1.setState("Haryana");
	  obj1.setCity("Gurugram");
	  obj1.setCount("10");
	 
	  
	  
	  
	  
	  
	  PublisherReport obj2 = new PublisherReport();
	  obj2.setCountry("IN");
	  obj2.setState("Haryana");
	  obj2.setCity("Gurugram1");
	  obj2.setCount("20");

	  PublisherReport obj3 = new PublisherReport();
	  obj3.setCountry("IN");
	  obj3.setState("Haryana");
	  obj3.setCity("Gurugram2");
	  obj3.setCount("10");
	  
	  PublisherReport obj4 =  new PublisherReport();
	  obj4.setCountry("IN");
	  obj4.setState("NE");
	  obj4.setCity("Guwahati");
	  obj3.setCount("10");
	  
	  rep.add(obj1);
	  rep.add(obj2);
	  rep.add(obj3);
	  rep.add(obj4);
	
	  List<String> keys = new ArrayList<String>();
	  keys.add("Country");
	  keys.add("State");
	  keys.add("city");
	
	  GenericTree<String> tree1  =  Aggregator7.groupBy(rep);
	  System.out.println(tree1.getNumberOfNodes());
	
	
	}

}
