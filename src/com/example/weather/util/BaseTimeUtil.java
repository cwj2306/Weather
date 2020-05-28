package com.example.weather.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class BaseTimeUtil {
	private Date date = null;
	private Calendar calendar = null;
	
	public BaseTimeUtil(Date date) {
		this.date = date;
		calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		calendar.setTime(date);
	}
	
	/*
	 * 초단기실황
	 * Base time : 매 시 (0000, 0100, 0200, ... , 2200, 2300)
	 * API 제공시간(~이후) : XX40 (0040, 0140, ... , 2340)
	 */
	public String getDateUltraSrtNcst() {
		//현재시간(분)
		int curTime = calendar.get(Calendar.HOUR_OF_DAY)*60 + calendar.get(Calendar.MINUTE);

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		String baseDate = null;
		
		if(curTime > 40) { //오늘날짜
			baseDate = format.format(date);
		}else { //어제날짜
			baseDate = format.format(date.getTime() - 60*60*24*1000);
		}

		return baseDate;
	}
	
	public String getTimeUltraSrtNcst() {
		//현재시간(분)
		int curTime = calendar.get(Calendar.HOUR_OF_DAY)*60 + calendar.get(Calendar.MINUTE);

		String baseTime = null;
		
		if(curTime > 40) { //오늘날짜
			if(calendar.get(Calendar.MINUTE) > 40) {
				baseTime = String.format("%02d00", calendar.get(Calendar.HOUR_OF_DAY));
			}else {
				baseTime = String.format("%02d00", calendar.get(Calendar.HOUR_OF_DAY)-1);
			}
		}else { //어제날짜 2300
			baseTime = "2300";
		}
		
		return baseTime;
	}

	
	/*
	 * 동네예보
	 * Base time : 0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300
	 * API 제공시간(~이후) : 0210, 0510, 0810, 1110, 1410, 1710, 2010, 2310
	 */
	public String getDateVilageFcst() {
		//현재시간(분)
		int curTime = calendar.get(Calendar.HOUR_OF_DAY)*60 + calendar.get(Calendar.MINUTE);

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		String baseDate = null;
		
		if(curTime > 2*60+10) { //오늘날짜
			baseDate = format.format(date);
		}else { //어제날짜
			baseDate = format.format(date.getTime() - 60*60*24*1000);
		}
		
		return baseDate;
	}
	
	public String getTimeVilageFcst() {
		//현재시간(분)
		int curTime = calendar.get(Calendar.HOUR_OF_DAY)*60 + calendar.get(Calendar.MINUTE);

		String baseTime = null;
		
		if(curTime > 2*60+10) { //오늘날짜
			int hour = ((calendar.get(Calendar.HOUR_OF_DAY) + 1)/3)*3 - 1;
			if(calendar.get(Calendar.HOUR_OF_DAY)%3 == 2 && calendar.get(Calendar.MINUTE) <= 10) {
				baseTime = String.format("%02d00", hour-3);
			}else {
				baseTime = String.format("%02d00", hour);
			}
		}else { //어제날짜 2300
			baseTime = "2300";
		}
		
		return baseTime;
	}
	
	
	/*
	 * 중기육상예보조회 , 중기기온조회
	 * Base time : 0600, 1800
	 * API 제공시간(~이후) : ???
	 */
	public String getDateTime() {
		//현재시간(분)
		int curTime = calendar.get(Calendar.HOUR_OF_DAY)*60 + calendar.get(Calendar.MINUTE);

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		String baseDateTime = null;
		
		if(curTime > 6*60+30) { //오늘날짜
			if(curTime > 18*60+30) {
				baseDateTime = format.format(date) + "1800";
			}else {
				baseDateTime = format.format(date) + "0600";
			}
		}else { //어제날짜 1800
			baseDateTime = format.format(date.getTime() - 60*60*24*1000) + "1800";
		}
		
		return baseDateTime;
	}
	
}
