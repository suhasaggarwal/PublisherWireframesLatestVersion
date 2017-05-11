package com.websystique.springmvc.controller;

import java.net.URLDecoder;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.elasticsearch.plugin.nlpcn.executors.CsvExtractorException;
import org.nlpcn.es4sql.exception.SqlParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import util.EncryptionModule;
import util.GetMiddlewareData;

import com.publisherdata.Daos.AggregationModule;
import com.publisherdata.model.Article;
import com.publisherdata.model.ArticleResponse;
import com.publisherdata.model.LocationResponse;
import com.publisherdata.model.PublisherReport;
import com.publisherdata.model.Response;
import com.publisherdata.model.Section;
import com.publisherdata.model.SectionResponse;
import com.publisherdata.model.Site;
import com.publisherdata.model.SiteResponse;
import com.publisherdata.model.User;
import com.websystique.springmvc.model.Reports;
import com.websystique.springmvc.service.ReportService;

@RestController
public class ReportRestController {

	@Autowired
	ReportService reportService; // Service which will do all data
									// retrieval/manipulation work

	// -------------------Retrieve Report with
	// Id--------------------------------------------------------
	/*
	 * @RequestMapping(value = "/report/{id}/{dateRange}/{channel_name}", method
	 * = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE) public
	 * ResponseEntity<List<PublisherReport>> getReport(@PathVariable("id") long
	 * id,@PathVariable("dateRange") String
	 * dateRange,@PathVariable("channel_name") String channel_name){
	 * System.out.println("Fetching Report with id " + id);
	 * List<PublisherReport> report =
	 * reportService.extractReportsChannelNames(id,dateRange,channel_name); if
	 * (report == null) { System.out.println("Report with id " + id +
	 * " not found"); return new
	 * ResponseEntity<List<PublisherReport>>(HttpStatus.NOT_FOUND); } return new
	 * ResponseEntity<List<PublisherReport>>(report, HttpStatus.OK); }
	 */
	@CrossOrigin
	@RequestMapping(value = "/report/{id}/{dateRange}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PublisherReport>> getReport(
			@PathVariable("id") long id,
			@PathVariable("dateRange") String dateRange,
			HttpServletRequest request) {

		List<PublisherReport> report = null;

		String token;
		String userdetails;
		String[] userinfo;
		String emailId = null;
		String status = null;
		User user = new User();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("AUTHTOKEN")) {
					// Fetch User details and return in json format
					token = cookies[i].getValue();
					userdetails = EncryptionModule.decrypt(null, null,
							URLDecoder.decode(token));
					userinfo = userdetails.split(":");
					emailId = userinfo[0];
				}
			}

		}

		if (emailId.equalsIgnoreCase("vinay.rajput@cuberoot.co")) {

			System.out.println("Fetching Report with id " + id);
			report = reportService.extractReportsChannel(id, dateRange,
					"Adda52");
			if (report == null) {
				System.out.println("Report with id " + id + " not found");
				return new ResponseEntity<List<PublisherReport>>(
						HttpStatus.NOT_FOUND);
			}

		} else {

			System.out.println("Fetching Report with id " + id);
			report = reportService.extractReports(id, dateRange);
			if (report == null) {
				System.out.println("Report with id " + id + " not found");
				return new ResponseEntity<List<PublisherReport>>(
						HttpStatus.NOT_FOUND);

			}
		}

		return new ResponseEntity<List<PublisherReport>>(report, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/report/{id}/{dateRange}/{channel}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PublisherReport>> getReport(
			@PathVariable("id") long id,
			@PathVariable("dateRange") String dateRange,
			@PathVariable("channel") String channel_name) {
		System.out.println("Fetching Report with id " + id);
		List<PublisherReport> report = reportService.extractReportsChannel(id,
				dateRange, channel_name);
		if (report == null) {
			System.out.println("Report with id " + id + " not found");
			return new ResponseEntity<List<PublisherReport>>(
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<PublisherReport>>(report, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/report/{id}/{dateRange}/channelList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Set<String>> getList(@PathVariable("id") long id,
			@PathVariable("dateRange") String dateRange,
			HttpServletRequest request) {

		String token;
		String userdetails;
		String[] userinfo;
		String emailId = null;
		String status = null;
		User user = new User();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals("AUTHTOKEN")) {
					// Fetch User details and return in json format
					token = cookies[i].getValue();
					userdetails = EncryptionModule.decrypt(null, null,
							URLDecoder.decode(token));
					userinfo = userdetails.split(":");
					emailId = userinfo[0];
				}
			}

		}

		System.out.println("Fetching Report with id " + id);
		Set<String> list = reportService.extractChannelNames(id, dateRange);
		if (list == null) {
			System.out.println("Report with id " + id + " not found");
			return new ResponseEntity<Set<String>>(HttpStatus.NOT_FOUND);
		}

		if (emailId.equalsIgnoreCase("vinay.rajput@cuberoot.co")) {
			list.clear();
			list.add("momagic");
			list.add("opera");
			list.add("cricbuzz");
			list.add("cricbuzz_mob");
			list.add("taboola");
			list.add("forkmedia");
			list.add("inuxu_native");
			list.add("ixigo");
			list.add("spidio");
			list.add("shopclues");
			list.add("espn");
			list.add("gamooga");

		}

		return new ResponseEntity<Set<String>>(list, HttpStatus.OK);
	}

	/*
	 * 
	 * @RequestMapping(value =
	 * "/report/v1/{id}/ArticleAPIs/{dateRange}/{channel}/{articlename}", method
	 * = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE) public
	 * ResponseEntity<List<PublisherReport>>
	 * getReportArticle(@PathVariable("id") long id,@PathVariable("dateRange")
	 * String dateRange,@PathVariable("channel") String
	 * channel_name,@PathVariable("articlename") String articlename) {
	 * System.out.println("Fetching Report with id " + id);
	 * List<PublisherReport> report =
	 * reportService.extractReportsChannelArticle(
	 * id,dateRange,channel_name,articlename); if (report == null){
	 * System.out.println("Report with id " + id + " not found"); return new
	 * ResponseEntity<List<PublisherReport>>(HttpStatus.NOT_FOUND); } return new
	 * ResponseEntity<List<PublisherReport>>(report, HttpStatus.OK); }
	 */

	@CrossOrigin
	@RequestMapping(value = "/report/v1/{QueryField}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> getReportqueryField(
			@PathVariable("QueryField") String queryfield,
			@RequestParam("dateRange") String dateRange,
			@RequestParam("siteId") String siteId,
			@RequestParam(value = "sectionid", required = false) String sectionId,
			@RequestParam(value = "articleid", required = false) String articleid,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "gender", required = false) String gender,
			@RequestParam(value = "agegroup", required = false) String agegroup,
			@RequestParam(value = "audience_segment", required = false) String audience_segment,
			@RequestParam(value = "income_level", required = false) String income_level,
			@RequestParam(value = "device_type", required = false) String device_type,
			@RequestParam(value = "live", required = false) String live,
			@RequestParam(value = "Aggregateby", required = false) String group_by) {

		AggregationModule module = AggregationModule.getInstance();
		try {
			module.setUp();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Response response = new Response();
		response.setCode("200");
		response.setStatus("Success");
		response.setMessage("API Successful");

		Response response1 = new Response();
		response1.setCode("404");
		response1.setStatus("Error");
		response1.setMessage("Not Found");

		Site obj = GetMiddlewareData.getSiteName(siteId);
		Section obj1 = null;
		Article obj2 = null;

		if (sectionId != null && !sectionId.isEmpty()) {

			obj1 = GetMiddlewareData.getSectionName(sectionId);
			sectionId = obj1.getSectionName();
		}
		if (articleid != null && !articleid.isEmpty()) {
			obj2 = GetMiddlewareData.getArticleName(articleid);
			articleid = obj2.getArticleName();
		}

		siteId = obj.getSiteName();

		List<PublisherReport> report = null;
		Map<String, String> FilterMap = new HashMap<String, String>();
		List<String> groupby = new ArrayList<String>();
		String[] groupbyparts;
		if (group_by != null && group_by.isEmpty() == false) {

			groupbyparts = group_by.split("~");

			for (int i = 0; i < groupbyparts.length; i++) {

				groupby.add(groupbyparts[i]);

			}
		}

		
		
		if (live!=null && !live.isEmpty() && live.equals("true")) {

			String starttimestamp = "";
			String endtimestamp = "";
			// String dateRange = "";
			if (dateRange.equals("Last_24_Hours")) {

				DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				endtimestamp = sdf.format(cal.getTime()).toString();
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.HOUR_OF_DAY, -24);
				starttimestamp = sdf.format(calendar.getTime()).toString();

				dateRange = starttimestamp + "_" + endtimestamp;
				dateRange = dateRange.replace("/", "-");
			}

			if (dateRange.equals("Last_15_minutes")) {
				DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				endtimestamp = sdf.format(cal.getTime()).toString();

				cal.add(Calendar.MINUTE, -420);
				starttimestamp = sdf.format(cal.getTime()).toString();
				dateRange = starttimestamp + "_" + endtimestamp;
				dateRange = dateRange.replace("/", "-");

			}

			// if((dateRange !=null && dateRange.isEmpty()==false)&& (siteId
			// !=null && siteId.isEmpty()==false) && ((city !=null &&
			// city.isEmpty()==false) || (gender !=null &&
			// gender.isEmpty()==false) || (agegroup !=null &&
			// agegroup.isEmpty()==false) || (audience_segment !=null &&
			// audience_segment.isEmpty()==false) || (income_level !=null &&
			// income_level.isEmpty()==false) || (device_type !=null &&
			// device_type.isEmpty()==false)) )
			if (city != null && !city.isEmpty()) {
				city = AggregationModule.citycodeMap2.get(city);
				FilterMap.put("city", city);
			}

			if (gender != null && !gender.isEmpty())
				FilterMap.put("gender", gender);

			if (agegroup != null && !agegroup.isEmpty())
				FilterMap.put("agegroup", agegroup);

			if (audience_segment != null && !audience_segment.isEmpty()){
				audience_segment = AggregationModule.audienceSegmentMap1.get(audience_segment);
				//Should contain audience Code
				FilterMap.put("audience_segment", audience_segment);

			}	
				
			if (income_level != null && !income_level.isEmpty())
				FilterMap.put("income_level", income_level);

			if (device_type != null && !device_type.isEmpty())
				FilterMap.put("device_type", device_type);

			if (dateRange != null && dateRange.isEmpty() == false) {

				if (siteId != null && siteId.isEmpty() == false) {

					if (FilterMap.size() == 0) {

						if (group_by == null || group_by.isEmpty()) {

							if (sectionId == null || sectionId.isEmpty()) {
								if (articleid == null || articleid.isEmpty()) {

									String[] dateInterval = dateRange
											.split("_");

									try {
										report = module
												.getQueryFieldChannelLive(
														queryfield,
														dateInterval[0],
														dateInterval[1],
														siteId);
									} catch (SQLFeatureNotSupportedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (SqlParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (CsvExtractorException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									response.setData(report);
									if(report == null || report.isEmpty())
									{
										if(queryfield.equals("city"))
										response.setMessage("Location Not available");
										
										if(queryfield.equals("audience_segment"))
										response.setMessage("Segment Not available");
									
									
									
									}
									return new ResponseEntity<Response>(
											response, HttpStatus.OK);

								}

							}

						}

					}

					if (FilterMap.size() == 0) {

						if (group_by == null || group_by.isEmpty()) {

							if (sectionId != null && !sectionId.isEmpty()) {

								String[] dateInterval = dateRange.split("_");

								try {
									report = module
											.getQueryFieldChannelSection(
													queryfield,
													dateInterval[0],
													dateInterval[1],
													siteId,
													sectionId);
								} catch (SQLFeatureNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SqlParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CsvExtractorException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								response.setData(report);
								if(report == null || report.isEmpty())
								{
									if(queryfield.equals("city"))
									response.setMessage("Location Not available");
									
									if(queryfield.equals("audience_segment"))
									response.setMessage("Segment Not available");
								
								
								
								}
								
								return new ResponseEntity<Response>(response,
										HttpStatus.OK);

							}

						}

					}

					if (FilterMap.size() > 0) {

						if (group_by == null || group_by.isEmpty()) {

							if (sectionId != null && !sectionId.isEmpty()) {

								String[] dateInterval = dateRange.split("_");

								try {
									report = module
											.getQueryFieldChannelSectionFilter(
													queryfield,
													dateInterval[0],
													dateInterval[1],
													siteId,
													sectionId, FilterMap);
								} catch (SQLFeatureNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SqlParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CsvExtractorException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								response.setData(report);
								if(report == null || report.isEmpty())
								{
									if(queryfield.equals("city"))
									response.setMessage("Location Not available");
									
									if(queryfield.equals("audience_segment"))
									response.setMessage("Segment Not available");
								
								
								
								}
								
								return new ResponseEntity<Response>(response,
										HttpStatus.OK);

							}

						}

					}

					if (FilterMap.size() == 0) {

						if (group_by == null || group_by.isEmpty()) {

							if (articleid != null && !articleid.isEmpty()) {

								String[] dateInterval = dateRange.split("_");

								try {
									report = module
											.getQueryFieldChannelArticle(
													queryfield,
													dateInterval[0],
													dateInterval[1],
													siteId,
													articleid);
								} catch (SQLFeatureNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SqlParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CsvExtractorException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								response.setData(report);
								if(report == null || report.isEmpty())
								{
									if(queryfield.equals("city"))
									response.setMessage("Location Not available");
									
									if(queryfield.equals("audience_segment"))
									response.setMessage("Segment Not available");
								
								
								
								}
								
								return new ResponseEntity<Response>(response,
										HttpStatus.OK);

							}

						}

					}

					if (FilterMap.size() > 0) {

						if (group_by == null || group_by.isEmpty()) {

							if (articleid != null && !articleid.isEmpty()) {

								String[] dateInterval = dateRange.split("_");

								try {
									report = module
											.getQueryFieldChannelArticleFilter(
													queryfield,
													dateInterval[0],
													dateInterval[1],
													siteId,
													articleid, FilterMap);
								} catch (SQLFeatureNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SqlParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CsvExtractorException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								response.setData(report);
								if(report == null || report.isEmpty())
								{
									if(queryfield.equals("city"))
									response.setMessage("Location Not available");
									
									if(queryfield.equals("audience_segment"))
									response.setMessage("Segment Not available");
								
								
								
								}
								return new ResponseEntity<Response>(response,
										HttpStatus.OK);
							}

						}

					}

					if (FilterMap.size() == 0) {

						if (group_by == null || group_by.isEmpty()) {

							if (sectionId == null || sectionId.isEmpty()) {

								if (articleid == null || articleid.isEmpty()) {

									String[] dateInterval = dateRange
											.split("_");

									try {
										report = module
												.getQueryFieldChannelLive(
														queryfield,
														dateInterval[0],
														dateInterval[1],
														siteId);
									} catch (SQLFeatureNotSupportedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (SqlParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (CsvExtractorException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									response.setData(report);
									if(report == null || report.isEmpty())
									{
										if(queryfield.equals("city"))
										response.setMessage("Location Not available");
										
										if(queryfield.equals("audience_segment"))
										response.setMessage("Segment Not available");
									
									
									
									}
									return new ResponseEntity<Response>(
											response, HttpStatus.OK);

								}

							}

						}
					}

					if (FilterMap.size() > 0) {

						if (sectionId == null || sectionId.isEmpty()) {

							if (articleid == null || articleid.isEmpty()) {

								String[] dateInterval = dateRange.split("_");

								try {
									report = module
											.getQueryFieldChannelFilterLive(
													queryfield,
													dateInterval[0],
													dateInterval[1],
													siteId,
													FilterMap);
								} catch (SQLFeatureNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SqlParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CsvExtractorException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								response.setData(report);
								if(report == null || report.isEmpty())
								{
									if(queryfield.equals("city"))
									response.setMessage("Location Not available");
									
									if(queryfield.equals("audience_segment"))
									response.setMessage("Segment Not available");
								
								
								
								}
								return new ResponseEntity<Response>(response,
										HttpStatus.OK);

							}

						}

					}

					if (group_by != null && !group_by.isEmpty()) {

						if (sectionId == null || sectionId.isEmpty()) {

							if (articleid == null || articleid.isEmpty()) {

								String[] dateInterval = dateRange.split("_");

								try {
									report = module
											.getQueryFieldChannelGroupByLive(
													queryfield,
													dateInterval[0],
													dateInterval[1],
													siteId,
													groupby);
								} catch (SQLFeatureNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SqlParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CsvExtractorException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								response.setData(report);
								if(report == null || report.isEmpty())
								{
									if(queryfield.equals("city"))
									response.setMessage("Location Not available");
									
									if(queryfield.equals("audience_segment"))
									response.setMessage("Segment Not available");
								
								
								
								}
								return new ResponseEntity<Response>(response,
										HttpStatus.OK);

							}

						}

					}

					if (group_by != null && !group_by.isEmpty()) {

						if (sectionId != null && !sectionId.isEmpty()) {

							if (articleid == null || articleid.isEmpty()) {

								String[] dateInterval = dateRange.split("_");

								try {
									report = module
											.getQueryFieldChannelSectionGroupBy(
													queryfield,
													dateInterval[0],
													dateInterval[1],
													siteId,
													sectionId, groupby);
								} catch (SQLFeatureNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SqlParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CsvExtractorException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								response.setData(report);
								if(report == null || report.isEmpty())
								{
									if(queryfield.equals("city"))
									response.setMessage("Location Not available");
									
									if(queryfield.equals("audience_segment"))
									response.setMessage("Segment Not available");
								
								
								
								}
								return new ResponseEntity<Response>(response,
										HttpStatus.OK);

							}

						}

					}

					if (group_by != null && !group_by.isEmpty()) {

						if (sectionId == null || sectionId.isEmpty()) {

							if (articleid != null && !articleid.isEmpty()) {

								String[] dateInterval = dateRange.split("_");

								try {
									report = module
											.getQueryFieldChannelArticleGroupBy(
													queryfield,
													dateInterval[0],
													dateInterval[1],
													siteId,
													articleid, groupby);
								} catch (SQLFeatureNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SqlParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CsvExtractorException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								response.setData(report);
								if(report == null || report.isEmpty())
								{
									if(queryfield.equals("city"))
									response.setMessage("Location Not available");
									
									if(queryfield.equals("audience_segment"))
									response.setMessage("Segment Not available");
								
								
								
								}
								return new ResponseEntity<Response>(response,
										HttpStatus.OK);

							}

						}

					}

				}

			}
		}

		else {

			// if((dateRange !=null && dateRange.isEmpty()==false)&& (siteId
			// !=null && siteId.isEmpty()==false) && ((city !=null &&
			// city.isEmpty()==false) || (gender !=null &&
			// gender.isEmpty()==false) || (agegroup !=null &&
			// agegroup.isEmpty()==false) || (audience_segment !=null &&
			// audience_segment.isEmpty()==false) || (income_level !=null &&
			// income_level.isEmpty()==false) || (device_type !=null &&
			// device_type.isEmpty()==false)) )

			if (city != null && !city.isEmpty()) {
				city = AggregationModule.citycodeMap2.get(city);
				FilterMap.put("city", city);
			}

			if (gender != null && !gender.isEmpty())
				FilterMap.put("gender", gender);

			if (agegroup != null && !agegroup.isEmpty())
				FilterMap.put("agegroup", agegroup);

			if (audience_segment != null && !audience_segment.isEmpty()){
				audience_segment = AggregationModule.audienceSegmentMap1.get(audience_segment);
				//Should contain audience Code
				FilterMap.put("audience_segment", audience_segment);

			}
				
			if (income_level != null && !income_level.isEmpty())
				FilterMap.put("income_level", income_level);

			if (device_type != null && !device_type.isEmpty())
				FilterMap.put("device_type", device_type);

			if (dateRange != null && dateRange.isEmpty() == false) {

				if (siteId != null && siteId.isEmpty() == false) {

					if (FilterMap.size() == 0) {

						if (group_by == null || group_by.isEmpty()) {

							if (sectionId == null || sectionId.isEmpty()) {
								if (articleid == null || articleid.isEmpty()) {

									String[] dateInterval = dateRange
											.split("_");

									try {
										report = module.getQueryFieldChannel(
												queryfield, dateInterval[0],
												dateInterval[1],
												siteId);
									} catch (SQLFeatureNotSupportedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (SqlParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (CsvExtractorException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									response.setData(report);
									if(report == null || report.isEmpty())
									{
										if(queryfield.equals("city"))
										response.setMessage("Location Not available");
										
										if(queryfield.equals("audience_segment"))
										response.setMessage("Segment Not available");
									
									
									
									}
									return new ResponseEntity<Response>(
											response, HttpStatus.OK);

								}

							}

						}

					}

					if (FilterMap.size() == 0) {

						if (group_by == null || group_by.isEmpty()) {

							if (sectionId != null && !sectionId.isEmpty()) {

								String[] dateInterval = dateRange.split("_");

								try {
									report = module
											.getQueryFieldChannelSection(
													queryfield,
													dateInterval[0],
													dateInterval[1],
													siteId,
													sectionId);
								} catch (SQLFeatureNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SqlParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CsvExtractorException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								response.setData(report);
								if(report == null || report.isEmpty())
								{
									if(queryfield.equals("city"))
									response.setMessage("Location Not available");
									
									if(queryfield.equals("audience_segment"))
									response.setMessage("Segment Not available");
								
								
								
								}
								return new ResponseEntity<Response>(response,
										HttpStatus.OK);

							}

						}

					}

					if (FilterMap.size() > 0) {

						if (group_by == null || group_by.isEmpty()) {

							if (sectionId != null && !sectionId.isEmpty()) {

								String[] dateInterval = dateRange.split("_");

								try {
									report = module
											.getQueryFieldChannelSectionFilter(
													queryfield,
													dateInterval[0],
													dateInterval[1],
													siteId,
													sectionId, FilterMap);
								} catch (SQLFeatureNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SqlParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CsvExtractorException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								response.setData(report);
								if(report == null || report.isEmpty())
								{
									if(queryfield.equals("city"))
									response.setMessage("Location Not available");
									
									if(queryfield.equals("audience_segment"))
									response.setMessage("Segment Not available");
								
								
								
								}
								return new ResponseEntity<Response>(response,
										HttpStatus.OK);

							}

						}

					}

					if (FilterMap.size() == 0) {

						if (group_by == null || group_by.isEmpty()) {

							if (articleid != null && !articleid.isEmpty()) {

								String[] dateInterval = dateRange.split("_");

								try {
									report = module
											.getQueryFieldChannelArticle(
													queryfield,
													dateInterval[0],
													dateInterval[1],
													siteId,
													articleid);
								} catch (SQLFeatureNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SqlParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CsvExtractorException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								response.setData(report);
								if(report == null || report.isEmpty())
								{
									if(queryfield.equals("city"))
									response.setMessage("Location Not available");
									
									if(queryfield.equals("audience_segment"))
									response.setMessage("Segment Not available");
								
								
								
								}
								return new ResponseEntity<Response>(response,
										HttpStatus.OK);

							}

						}

					}

					if (FilterMap.size() > 0) {

						if (group_by == null || group_by.isEmpty()) {

							if (articleid != null && !articleid.isEmpty()) {

								String[] dateInterval = dateRange.split("_");

								try {
									report = module
											.getQueryFieldChannelArticleFilter(
													queryfield,
													dateInterval[0],
													dateInterval[1],
													siteId,
													articleid, FilterMap);
								} catch (SQLFeatureNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SqlParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CsvExtractorException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								response.setData(report);
								if(report == null || report.isEmpty())
								{
									if(queryfield.equals("city"))
									response.setMessage("Location Not available");
									
									if(queryfield.equals("audience_segment"))
									response.setMessage("Segment Not available");
								
								
								
								}
								return new ResponseEntity<Response>(response,
										HttpStatus.OK);
							}

						}

					}

					if (FilterMap.size() == 0) {

						if (group_by == null || group_by.isEmpty()) {

							if (sectionId == null || sectionId.isEmpty()) {

								if (articleid == null || articleid.isEmpty()) {

									String[] dateInterval = dateRange
											.split("_");

									try {
										report = module.getQueryFieldChannel(
												queryfield, dateInterval[0],
												dateInterval[1],
												siteId);
									} catch (SQLFeatureNotSupportedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (SqlParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (CsvExtractorException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

									response.setData(report);
									if(report == null || report.isEmpty())
									{
										if(queryfield.equals("city"))
										response.setMessage("Location Not available");
										
										if(queryfield.equals("audience_segment"))
										response.setMessage("Segment Not available");
									
									
									
									}
									return new ResponseEntity<Response>(
											response, HttpStatus.OK);

								}

							}

						}
					}

					if (FilterMap.size() > 0) {

						if (sectionId == null || sectionId.isEmpty()) {

							if (articleid == null || articleid.isEmpty()) {

								String[] dateInterval = dateRange.split("_");

								try {
									report = module.getQueryFieldChannelFilter(
											queryfield, dateInterval[0],
											dateInterval[1],
											siteId,
											FilterMap);
								} catch (SQLFeatureNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SqlParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CsvExtractorException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								response.setData(report);
								if(report == null || report.isEmpty())
								{
									if(queryfield.equals("city"))
									response.setMessage("Location Not available");
									
									if(queryfield.equals("audience_segment"))
									response.setMessage("Segment Not available");
								
								
								
								}
								return new ResponseEntity<Response>(response,
										HttpStatus.OK);

							}

						}

					}

					if (group_by != null && !group_by.isEmpty()) {

						if (sectionId == null || sectionId.isEmpty()) {

							if (articleid == null || articleid.isEmpty()) {

								String[] dateInterval = dateRange.split("_");

								try {
									report = module
											.getQueryFieldChannelGroupBy(
													queryfield,
													dateInterval[0],
													dateInterval[1],
													siteId,
													groupby);
								} catch (SQLFeatureNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SqlParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CsvExtractorException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								response.setData(report);
								if(report == null || report.isEmpty())
								{
									if(queryfield.equals("city"))
									response.setMessage("Location Not available");
									
									if(queryfield.equals("audience_segment"))
									response.setMessage("Segment Not available");
								
								
								
								}
								return new ResponseEntity<Response>(response,
										HttpStatus.OK);

							}

						}

					}

					if (group_by != null && !group_by.isEmpty()) {

						if (sectionId != null && !sectionId.isEmpty()) {

							if (articleid == null || articleid.isEmpty()) {

								String[] dateInterval = dateRange.split("_");

								try {
									report = module
											.getQueryFieldChannelSectionGroupBy(
													queryfield,
													dateInterval[0],
													dateInterval[1],
													siteId,
													sectionId, groupby);
								} catch (SQLFeatureNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SqlParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CsvExtractorException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								response.setData(report);
								if(report == null || report.isEmpty())
								{
									if(queryfield.equals("city"))
									response.setMessage("Location Not available");
									
									if(queryfield.equals("audience_segment"))
									response.setMessage("Segment Not available");
								
								
								
								}
								return new ResponseEntity<Response>(response,
										HttpStatus.OK);

							}

						}

					}

					if (group_by != null && !group_by.isEmpty()) {

						if (sectionId == null || sectionId.isEmpty()) {

							if (articleid != null && !articleid.isEmpty()) {

								String[] dateInterval = dateRange.split("_");

								try {
									report = module
											.getQueryFieldChannelArticleGroupBy(
													queryfield,
													dateInterval[0],
													dateInterval[1],
													siteId,
													articleid, groupby);
								} catch (SQLFeatureNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SqlParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CsvExtractorException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								response.setData(report);
								if(report == null || report.isEmpty())
								{
									if(queryfield.equals("city"))
									response.setMessage("Location Not available");
									
									if(queryfield.equals("audience_segment"))
									response.setMessage("Segment Not available");
								
								
								
								}
								return new ResponseEntity<Response>(response,
										HttpStatus.OK);

							}

						}

					}

				}

			}

		}

		if (report == null) {
			response1.setData(report);
			return new ResponseEntity<Response>(response1, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@CrossOrigin
	@RequestMapping(value = "/report/v1/Live/{QueryField}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> getReportqueryFieldLive(
			@PathVariable("QueryField") String queryfield,
			@RequestParam("timeDuration") String timeduration,
			@RequestParam("siteId") String siteId,
			@RequestParam(value = "sectionid", required = false) String sectionId,
			@RequestParam(value = "articleid", required = false) String articleid,
			@RequestParam(value = "city", required = false) String city,
			@RequestParam(value = "gender", required = false) String gender,
			@RequestParam(value = "agegroup", required = false) String agegroup,
			@RequestParam(value = "audience_segment", required = false) String audience_segment,
			@RequestParam(value = "income_level", required = false) String income_level,
			@RequestParam(value = "device_type", required = false) String device_type,
			@RequestParam(value = "Aggregateby", required = false) String group_by) {

		String starttimestamp = "";
		String endtimestamp = "";
		String dateRange = "";
		
		
		
		
		if (timeduration.equals("Last_24_Hours")) {

			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			endtimestamp = sdf.format(cal.getTime()).toString();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR_OF_DAY, -24);
			starttimestamp = sdf.format(calendar.getTime()).toString();

			dateRange = starttimestamp + "_" + endtimestamp;
			dateRange = dateRange.replace("/", "-");
		}

		if (timeduration.equals("Last_15_minutes")) {
			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			endtimestamp = sdf.format(cal.getTime()).toString();

			cal.add(Calendar.MINUTE, -15);
			starttimestamp = sdf.format(cal.getTime()).toString();
			dateRange = starttimestamp + "_" + endtimestamp;
			dateRange = dateRange.replace("/", "-");

		}

		AggregationModule module = AggregationModule.getInstance();
		try {
			module.setUp();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Response response = new Response();
		response.setCode("200");
		response.setStatus("Success");
		response.setMessage("API Successful");

		Response response1 = new Response();
		response1.setCode("404");
		response1.setStatus("Error");
		response1.setMessage("Not Found");

		Site obj = GetMiddlewareData.getSiteName(siteId);
		Section obj1 = null;
		Article obj2 = null;

		if (sectionId != null && !sectionId.isEmpty()) {

			obj1 = GetMiddlewareData.getSectionName(sectionId);
			sectionId = obj1.getSectionName();
		}
		if (articleid != null && !articleid.isEmpty()) {
			obj2 = GetMiddlewareData.getArticleName(articleid);
			articleid = obj2.getArticleName();
		}

		siteId = obj.getSiteName();

		List<PublisherReport> report = null;
		Map<String, String> FilterMap = new HashMap<String, String>();
		List<String> groupby = new ArrayList<String>();
		String[] groupbyparts;
		if (group_by != null && group_by.isEmpty() == false) {

			groupbyparts = group_by.split("~");

			for (int i = 0; i < groupbyparts.length; i++) {

				groupby.add(groupbyparts[i]);

			}

		}
		// if((dateRange !=null && dateRange.isEmpty()==false)&& (siteId !=null
		// && siteId.isEmpty()==false) && ((city !=null &&
		// city.isEmpty()==false) || (gender !=null && gender.isEmpty()==false)
		// || (agegroup !=null && agegroup.isEmpty()==false) ||
		// (audience_segment !=null && audience_segment.isEmpty()==false) ||
		// (income_level !=null && income_level.isEmpty()==false) ||
		// (device_type !=null && device_type.isEmpty()==false)) )

		if (city != null && !city.isEmpty()) {
			city = AggregationModule.citycodeMap2.get(city);
			FilterMap.put("city", city);
		}

		if (gender != null && !gender.isEmpty())
			FilterMap.put("gender", gender);

		if (agegroup != null && !agegroup.isEmpty())
			FilterMap.put("agegroup", agegroup);

		if (audience_segment != null && !audience_segment.isEmpty())
			FilterMap.put("audience_segment", audience_segment);

		if (income_level != null && !income_level.isEmpty())
			FilterMap.put("income_level", income_level);

		if (device_type != null && !device_type.isEmpty())
			FilterMap.put("device_type", device_type);

		if (dateRange != null && dateRange.isEmpty() == false) {

			if (siteId != null && siteId.isEmpty() == false) {

				if (FilterMap.size() == 0) {

					if (group_by == null || group_by.isEmpty()) {

						if (sectionId == null || sectionId.isEmpty()) {
							if (articleid == null || articleid.isEmpty()) {

								String[] dateInterval = dateRange.split("_");

								try {
									report = module.getQueryFieldChannelLive(
											queryfield, dateInterval[0],
											dateInterval[1],
											siteId);
								} catch (SQLFeatureNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SqlParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CsvExtractorException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								response.setData(report);
								return new ResponseEntity<Response>(response,
										HttpStatus.OK);

							}

						}

					}

				}

				if (FilterMap.size() == 0) {

					if (group_by == null || group_by.isEmpty()) {

						if (sectionId != null && !sectionId.isEmpty()) {

							String[] dateInterval = dateRange.split("_");

							try {
								report = module.getQueryFieldChannelSection(
										queryfield, dateInterval[0],
										dateInterval[1],
										siteId, sectionId);
							} catch (SQLFeatureNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SqlParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (CsvExtractorException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							response.setData(report);
							return new ResponseEntity<Response>(response,
									HttpStatus.OK);

						}

					}

				}

				if (FilterMap.size() > 0) {

					if (group_by == null || group_by.isEmpty()) {

						if (sectionId != null && !sectionId.isEmpty()) {

							String[] dateInterval = dateRange.split("_");

							try {
								report = module
										.getQueryFieldChannelSectionFilter(
												queryfield, dateInterval[0],
												dateInterval[1],
												siteId,
												sectionId, FilterMap);
							} catch (SQLFeatureNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SqlParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (CsvExtractorException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							response.setData(report);
							return new ResponseEntity<Response>(response,
									HttpStatus.OK);

						}

					}

				}

				if (FilterMap.size() == 0) {

					if (group_by == null || group_by.isEmpty()) {

						if (articleid != null && !articleid.isEmpty()) {

							String[] dateInterval = dateRange.split("_");

							try {
								report = module.getQueryFieldChannelArticle(
										queryfield, dateInterval[0],
										dateInterval[1],
										siteId, articleid);
							} catch (SQLFeatureNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SqlParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (CsvExtractorException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							response.setData(report);
							return new ResponseEntity<Response>(response,
									HttpStatus.OK);

						}

					}

				}

				if (FilterMap.size() > 0) {

					if (group_by == null || group_by.isEmpty()) {

						if (articleid != null && !articleid.isEmpty()) {

							String[] dateInterval = dateRange.split("_");

							try {
								report = module
										.getQueryFieldChannelArticleFilter(
												queryfield, dateInterval[0],
												dateInterval[1],
												siteId,
												articleid, FilterMap);
							} catch (SQLFeatureNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SqlParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (CsvExtractorException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							response.setData(report);
							return new ResponseEntity<Response>(response,
									HttpStatus.OK);
						}

					}

				}

				if (FilterMap.size() == 0) {

					if (group_by == null || group_by.isEmpty()) {

						if (sectionId == null || sectionId.isEmpty()) {

							if (articleid == null || articleid.isEmpty()) {

								String[] dateInterval = dateRange.split("_");

								try {
									report = module.getQueryFieldChannelLive(
											queryfield, dateInterval[0],
											dateInterval[1],
											siteId);
								} catch (SQLFeatureNotSupportedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (SqlParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (CsvExtractorException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								response.setData(report);
								return new ResponseEntity<Response>(response,
										HttpStatus.OK);

							}

						}

					}
				}

				if (FilterMap.size() > 0) {

					if (sectionId == null || sectionId.isEmpty()) {

						if (articleid == null || articleid.isEmpty()) {

							String[] dateInterval = dateRange.split("_");

							try {
								report = module.getQueryFieldChannelFilterLive(
										queryfield, dateInterval[0],
										dateInterval[1],
										siteId, FilterMap);
							} catch (SQLFeatureNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SqlParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (CsvExtractorException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							response.setData(report);
							return new ResponseEntity<Response>(response,
									HttpStatus.OK);

						}

					}

				}

				if (group_by != null && !group_by.isEmpty()) {

					if (sectionId == null || sectionId.isEmpty()) {

						if (articleid == null || articleid.isEmpty()) {

							String[] dateInterval = dateRange.split("_");

							try {
								report = module
										.getQueryFieldChannelGroupByLive(
												queryfield, dateInterval[0],
												dateInterval[1],
												siteId,
												groupby);
							} catch (SQLFeatureNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SqlParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (CsvExtractorException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							response.setData(report);
							return new ResponseEntity<Response>(response,
									HttpStatus.OK);

						}

					}

				}

				if (group_by != null && !group_by.isEmpty()) {

					if (sectionId != null && !sectionId.isEmpty()) {

						if (articleid == null || articleid.isEmpty()) {

							String[] dateInterval = dateRange.split("_");

							try {
								report = module
										.getQueryFieldChannelSectionGroupBy(
												queryfield, dateInterval[0],
												dateInterval[1],
												siteId,
												sectionId, groupby);
							} catch (SQLFeatureNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SqlParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (CsvExtractorException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							response.setData(report);
							return new ResponseEntity<Response>(response,
									HttpStatus.OK);

						}

					}

				}

				if (group_by != null && !group_by.isEmpty()) {

					if (sectionId == null || sectionId.isEmpty()) {

						if (articleid != null && !articleid.isEmpty()) {

							String[] dateInterval = dateRange.split("_");

							try {
								report = module
										.getQueryFieldChannelArticleGroupBy(
												queryfield, dateInterval[0],
												dateInterval[1],
												siteId,
												articleid, groupby);
							} catch (SQLFeatureNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SqlParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (CsvExtractorException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							response.setData(report);
							return new ResponseEntity<Response>(response,
									HttpStatus.OK);

						}

					}

				}

			}

		}

		if (report == null) {
			response1.setData(report);
			return new ResponseEntity<Response>(response1, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/report/v1/SectionList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SectionResponse> getSectionList(
			@RequestParam("siteid") String siteid) {
		System.out.println("SiteID Section List " + siteid);
		SectionResponse response = new SectionResponse();
		response.setCode("200");
		response.setStatus("Success");
		response.setMessage("API Successful");

		SectionResponse response1 = new SectionResponse();
		response1.setCode("404");
		response1.setStatus("Error");
		response1.setMessage("Not Found");

		List<Section> sectionList = reportService.getSectionList(siteid);
		if (sectionList == null) {
			System.out.println("Report with siteid " + siteid + " not found");
			response1.setData(sectionList);
			return new ResponseEntity<SectionResponse>(response1,
					HttpStatus.NOT_FOUND);
		}
		response.setData(sectionList);
		return new ResponseEntity<SectionResponse>(response, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/report/v1/ArticleList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArticleResponse> getArticleList(
			@RequestParam("siteid") String siteid) {
		System.out.println("SiteID Section List " + siteid);
		ArticleResponse response = new ArticleResponse();
		response.setCode("200");
		response.setStatus("Success");
		response.setMessage("API Successful");

		ArticleResponse response1 = new ArticleResponse();
		response1.setCode("404");
		response1.setStatus("Error");
		response1.setMessage("Not Found");

		List<Article> articleList = reportService.getArticleList(siteid);
		if (articleList == null) {
			System.out.println("Report with siteid " + siteid + " not found");
			response1.setData(articleList);
			return new ResponseEntity<ArticleResponse>(response1,
					HttpStatus.NOT_FOUND);
		}
		response.setData(articleList);
		return new ResponseEntity<ArticleResponse>(response, HttpStatus.OK);
	}

	
	
	@CrossOrigin
	@RequestMapping(value = "/report/v1/SiteList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<SiteResponse> getSiteList(
			@RequestParam("userid") String userid) {
		System.out.println("SiteID List " + userid);
		SiteResponse response = new SiteResponse();
		response.setCode("200");
		response.setStatus("Success");
		response.setMessage("API Successful");

		SiteResponse response1 = new SiteResponse();
		response1.setCode("404");
		response1.setStatus("Error");
		response1.setMessage("Not Found");

		List<Site> siteList = reportService.getSiteList(userid);
		if (siteList == null) {
			System.out.println("Report with userid " + userid + " not found");
			response1.setData(siteList);
			return new ResponseEntity<SiteResponse>(response1,
					HttpStatus.NOT_FOUND);
		}
		response.setData(siteList);
		return new ResponseEntity<SiteResponse>(response, HttpStatus.OK);
	}


	@CrossOrigin
	@RequestMapping(value = "/report/v1/countryList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LocationResponse> getCountryList() {
		System.out.println("Country List ");
		LocationResponse response = new LocationResponse();
		response.setCode("200");
		response.setStatus("Success");
		response.setMessage("API Successful");

	    LocationResponse response1 = new LocationResponse();
		response1.setCode("404");
		response1.setStatus("Error");
		response1.setMessage("Not Found");

		response1.setMessage("Not Found");

		AggregationModule module = AggregationModule.getInstance();
		try {
			module.setUp();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<String> countryList = new ArrayList<String>();
		countryList = module.getcountryNames();
		if (countryList == null) {
		
			response1.setData(countryList);
			return new ResponseEntity<LocationResponse>(response1,
					HttpStatus.NOT_FOUND);
		}
		response.setData(countryList);
		return new ResponseEntity<LocationResponse>(response, HttpStatus.OK);
	}
	
	
	@CrossOrigin
	@RequestMapping(value = "/report/v1/stateList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LocationResponse> getStateList(@RequestParam("countryCode") String countryCode) {
		System.out.println("State List ");
		LocationResponse response = new LocationResponse();
		response.setCode("200");
		response.setStatus("Success");
		response.setMessage("API Successful");

	    LocationResponse response1 = new LocationResponse();
		response1.setCode("404");
		response1.setStatus("Error");
		response1.setMessage("Not Found");

		AggregationModule module = AggregationModule.getInstance();
		try {
			module.setUp();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<String> stateList = new ArrayList<String>();
		stateList = module.getcountryStateNames(countryCode);
		if (stateList == null) {
			
			response1.setData(stateList);
			return new ResponseEntity<LocationResponse>(response1,
					HttpStatus.NOT_FOUND);
		}
		response.setData(stateList);
		return new ResponseEntity<LocationResponse>(response, HttpStatus.OK);
	}
	
	@CrossOrigin
	@RequestMapping(value = "/report/v1/cityList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LocationResponse> getStateList(@RequestParam("countryCode") String countryCode,@RequestParam("stateCode") String stateCode) {
		System.out.println("City List ");
		LocationResponse response = new LocationResponse();
		response.setCode("200");
		response.setStatus("Success");
		response.setMessage("API Successful");

	    LocationResponse response1 = new LocationResponse();
		response1.setCode("404");
		response1.setStatus("Error");
		response1.setMessage("Not Found");

		AggregationModule module = AggregationModule.getInstance();
		try {
			module.setUp();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<String> cityList = new ArrayList<String>();
	    cityList = module.getcountryCityNames(countryCode, stateCode);
        if (cityList == null) {
			
			response1.setData(cityList);
			return new ResponseEntity<LocationResponse>(response1,
					HttpStatus.NOT_FOUND);
		}
		response.setData(cityList);
		return new ResponseEntity<LocationResponse>(response, HttpStatus.OK);
	}
	
	
	
	
	@CrossOrigin
	@RequestMapping(value = "/report/{id}/SectionAPIs/{dateRange}/{channel}/{sectionname}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PublisherReport>> getReportSection(
			@PathVariable("id") long id,
			@PathVariable("dateRange") String dateRange,
			@PathVariable("channel") String channel_name,
			@PathVariable("sectionname") String sectionname) {
		System.out.println("Fetching Report with id " + id);
		List<PublisherReport> report = reportService
				.extractReportsChannelSection(id, dateRange, channel_name,
						sectionname);
		if (report == null) {
			System.out.println("Report with id " + id + " not found");
			return new ResponseEntity<List<PublisherReport>>(
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<PublisherReport>>(report, HttpStatus.OK);
	}

	@CrossOrigin
	@RequestMapping(value = "/report/{id}/LiveAPIs/{channel}/{timeduration}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<PublisherReport>> getReportLiveData(
			@PathVariable("id") long id,
			@PathVariable("channel") String channel_name,
			@PathVariable("timeduration") String timeduration) {
		System.out.println("Fetching Report with id " + id);

		String starttimestamp = "";
		String endtimestamp = "";
		String dateRange = "";
		if (timeduration.equals("Last_24_Hours")) {

			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			endtimestamp = sdf.format(cal.getTime()).toString();
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.HOUR_OF_DAY, -24);
			starttimestamp = sdf.format(calendar.getTime()).toString();

			dateRange = starttimestamp + "," + endtimestamp;
			dateRange = dateRange.replace("/", "-");
		}

		if (timeduration.equals("Last_15_minutes")) {
			DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			endtimestamp = sdf.format(cal.getTime()).toString();

			cal.add(Calendar.MINUTE, -15);
			starttimestamp = sdf.format(cal.getTime()).toString();
			dateRange = starttimestamp + "," + endtimestamp;
			dateRange = dateRange.replace("/", "-");

		}

		List<PublisherReport> report = reportService.extractReportsChannelLive(
				id, dateRange, channel_name);
		if (report == null) {
			System.out.println("Report with id " + id + " not found");
			return new ResponseEntity<List<PublisherReport>>(
					HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<PublisherReport>>(report, HttpStatus.OK);
	}

}
