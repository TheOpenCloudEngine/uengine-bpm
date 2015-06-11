package org.uengine.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BusinessDayUtil {

	private static final int TERM = 5;
	
	public static String getBusinessDayEnddate(String beginDate) {
		return getBusinessDayEnddate(beginDate, TERM);
	}
	
	public static String getBusinessDayEnddate(String beginDate, int term) {
		int bDate = Integer.parseInt(beginDate.substring(6,8));
	    int bMonth = Integer.parseInt(beginDate.substring(4,6))-1;
	    int bYear = Integer.parseInt(beginDate.substring(0,4));
		
		Calendar cal = Calendar.getInstance();
		cal.set(bYear, bMonth, bDate);
		cal.add(Calendar.DATE, term);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String endDate = sdf.format(cal.getTime());
		
		int eDate = 0;
		int eMonth = 0;
		int eYear = 0;
		int businessDay = 0;
		for(int i=0; i<term; i++) {
			businessDay = getBusinessDay(beginDate, endDate);
			if(term == businessDay) {
				break;
			} else {
				eDate = Integer.parseInt(endDate.substring(6,8));
				eMonth = Integer.parseInt(endDate.substring(4,6))-1;
				eYear = Integer.parseInt(endDate.substring(0,4));
				
				cal.set(eYear, eMonth, eDate);
				cal.add(Calendar.DATE, (term-businessDay));
				endDate = sdf.format(cal.getTime());
			}
		}
		
		return endDate;
	}
	
	public static int getBusinessDay(String beginDate, String endDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    Date bd = null;
		try {
			bd = sdf.parse(beginDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Date ed = null;
		try {
			ed = sdf.parse(endDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    int businessDay = (int)((ed.getTime() - bd.getTime()) / (60 * 60 * 24 * 1000));
	    if(businessDay < 1)
	    	return 0;

	    int bDate = Integer.parseInt(beginDate.substring(6,8));
	    int bMonth = Integer.parseInt(beginDate.substring(4,6))-1;
	    int bYear = Integer.parseInt(beginDate.substring(0,4));
	    
	    Calendar cal = Calendar.getInstance();
	    
	    cal.set(bYear, bMonth, bDate);
	    int beginDayofWeek = cal.get(Calendar.DAY_OF_WEEK);
	    
		int weeks = businessDay / 7;
		int mod = businessDay % 7;
		
		int holiday = weeks * 2;
	
		businessDay = businessDay - (holiday);
	
		if(mod > 0) {
			if(beginDayofWeek == Calendar.MONDAY) {
				if(mod == 6)
					businessDay = businessDay - 1;
				if(mod >= 7)
					businessDay = businessDay - 2;
			}
			if(beginDayofWeek == Calendar.TUESDAY) {
				if(mod == 5)
					businessDay = businessDay - 1;
				if(mod >= 6)
					businessDay = businessDay - 2;
			}
			if(beginDayofWeek == Calendar.WEDNESDAY) {
				if(mod == 4)
					businessDay = businessDay - 1;
				if(mod >= 5)
					businessDay = businessDay - 2;
			}
			if(beginDayofWeek == Calendar.THURSDAY) {
				if(mod == 3)
					businessDay = businessDay - 1;
				if(mod >= 4)
					businessDay = businessDay - 2;
			}
			if(beginDayofWeek == Calendar.FRIDAY) {
				if(mod == 2)
					businessDay = businessDay - 1;
				if(mod >= 3)
					businessDay = businessDay - 2;
			}
			if(beginDayofWeek == Calendar.SATURDAY) {
				if(mod == 1)
					businessDay = businessDay - 1;
				if(mod >= 2)
					businessDay = businessDay - 2;
			}
			if(beginDayofWeek == Calendar.SUNDAY) {
				if(mod >= 0)
					businessDay = businessDay - 1;
			}
		}
		
		return businessDay;
	}
	
	
	public static int getHolidayCount() {
		int holidayCount = 0;
		List holidayList = getHolidayList();
		Map map = null;
		String holidayDate = null;
		Calendar cal = Calendar.getInstance();
		int date = 0;
	    int month = 0;
	    int year = 0;
	    int dayOfWeek = 0;
		for(int i=0; i<holidayList.size(); i++) {
			map = (Map)holidayList.get(i);
			holidayDate = (String)map.get("holiday");
			
			System.out.println("[holidayDate] : " + holidayDate);
			
			date = Integer.parseInt(holidayDate.substring(6,8));
			month = Integer.parseInt(holidayDate.substring(4,6))-1;
			year = Integer.parseInt(holidayDate.substring(0,4));
			
			cal.set(Calendar.DATE, date);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.YEAR, year);
			dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
			if(dayOfWeek >= 2 && dayOfWeek <=6) {
				holidayCount++;
			}
		}
		System.out.println("[holidayCount] : " + holidayCount);
		return holidayCount;
	}
	
	public static List getHolidayList() {
		List holidayList = new ArrayList();
		Map map = new HashMap();
		map.put("holiday","20070101");
		holidayList.add(map);
		
		
		return holidayList;
	}
}
