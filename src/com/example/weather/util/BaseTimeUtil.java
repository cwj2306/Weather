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
	 * �ʴܱ��Ȳ
	 * Base time : �� �� (0000, 0100, 0200, ... , 2200, 2300)
	 * API �����ð�(~����) : XX40 (0040, 0140, ... , 2340)
	 */
	public String getDateUltraSrtNcst() {
		//����ð�(��)
		int curTime = calendar.get(Calendar.HOUR_OF_DAY)*60 + calendar.get(Calendar.MINUTE);

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		String baseDate = null;
		
		if(curTime > 40) { //���ó�¥
			baseDate = format.format(date);
		}else { //������¥
			baseDate = format.format(date.getTime() - 60*60*24*1000);
		}

		return baseDate;
	}
	
	public String getTimeUltraSrtNcst() {
		//����ð�(��)
		int curTime = calendar.get(Calendar.HOUR_OF_DAY)*60 + calendar.get(Calendar.MINUTE);

		String baseTime = null;
		
		if(curTime > 40) { //���ó�¥
			if(calendar.get(Calendar.MINUTE) > 40) {
				baseTime = String.format("%02d00", calendar.get(Calendar.HOUR_OF_DAY));
			}else {
				baseTime = String.format("%02d00", calendar.get(Calendar.HOUR_OF_DAY)-1);
			}
		}else { //������¥ 2300
			baseTime = "2300";
		}
		
		return baseTime;
	}

	
	/*
	 * ���׿���
	 * Base time : 0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300
	 * API �����ð�(~����) : 0210, 0510, 0810, 1110, 1410, 1710, 2010, 2310
	 */
	public String getDateVilageFcst() {
		//����ð�(��)
		int curTime = calendar.get(Calendar.HOUR_OF_DAY)*60 + calendar.get(Calendar.MINUTE);

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		String baseDate = null;
		
		if(curTime > 2*60+10) { //���ó�¥
			baseDate = format.format(date);
		}else { //������¥
			baseDate = format.format(date.getTime() - 60*60*24*1000);
		}
		
		return baseDate;
	}
	
	public String getTimeVilageFcst() {
		//����ð�(��)
		int curTime = calendar.get(Calendar.HOUR_OF_DAY)*60 + calendar.get(Calendar.MINUTE);

		String baseTime = null;
		
		if(curTime > 2*60+10) { //���ó�¥
			int hour = ((calendar.get(Calendar.HOUR_OF_DAY) + 1)/3)*3 - 1;
			if(calendar.get(Calendar.HOUR_OF_DAY)%3 == 2 && calendar.get(Calendar.MINUTE) <= 10) {
				baseTime = String.format("%02d00", hour-3);
			}else {
				baseTime = String.format("%02d00", hour);
			}
		}else { //������¥ 2300
			baseTime = "2300";
		}
		
		return baseTime;
	}
	
	
	/*
	 * �߱����󿹺���ȸ , �߱�����ȸ
	 * Base time : 0600, 1800
	 * API �����ð�(~����) : ???
	 */
	public String getDateTime() {
		//����ð�(��)
		int curTime = calendar.get(Calendar.HOUR_OF_DAY)*60 + calendar.get(Calendar.MINUTE);

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		String baseDateTime = null;
		
		if(curTime > 6*60+30) { //���ó�¥
			if(curTime > 18*60+30) {
				baseDateTime = format.format(date) + "1800";
			}else {
				baseDateTime = format.format(date) + "0600";
			}
		}else { //������¥ 1800
			baseDateTime = format.format(date.getTime() - 60*60*24*1000) + "1800";
		}
		
		return baseDateTime;
	}
	
}
