package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.publisherdata.Daos.AggregationModule;
import com.websystique.springmvc.service.ReportService;
import com.websystique.springmvc.service.ReportServiceImpl;

public class TestUtil {

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub

		 
		GetMiddlewareData.getArticleData("1");
		GetMiddlewareData.getSectionData("1");
		Date date1=new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-01");
		Date date2= new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-19");
		int days = AggregationModule.getDifferenceDays(date1,date2);
		System.out.println(days);
		List<Long> dates = AggregationModule.getDaysBetweenDates(date1, date2);
		System.out.println(dates);
		/*
		String starttimestamp= "";
		String endtimestamp="";
		String dateRange="";
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
        endtimestamp=sdf.format(cal.getTime()).toString();
		
		cal.add(Calendar.MINUTE, -15);
		starttimestamp = sdf.format(cal.getTime()).toString();
        dateRange=starttimestamp+","+endtimestamp;
		System.out.println(dateRange);
		
		*/
	}

}
