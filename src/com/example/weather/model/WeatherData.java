package com.example.weather.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;


public class WeatherData {
	private Date date;
	private Calendar calendar;
	
	private UltraSrtNcst ultraSrtNcst;
	private VilageFcst vilageFcst;
	private VilageFcst vilageFcst0200;
	private MidLandFcst midLandFcst;
	private MidTa midTa;
	
	private ArrayList<Weather> arrWeather = new ArrayList<Weather>();
	
	public WeatherData(Date date, UltraSrtNcst ultraSrtNcst, VilageFcst vilageFcst, VilageFcst vilageFcst0200, MidLandFcst midLandFcst, MidTa midTa) {
		this.date = date;
		
		this.ultraSrtNcst = ultraSrtNcst;
		this.vilageFcst = vilageFcst;
		this.vilageFcst0200 = vilageFcst0200;
		this.midLandFcst = midLandFcst;
		this.midTa = midTa;
		
		calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		calendar.setTime(date);
		
		init();
	}

	public ArrayList<Weather> getArrWeather() {
		return arrWeather;
	}

	public void setArrWeather(ArrayList<Weather> arrWeather) {
		this.arrWeather = arrWeather;
	}
	
	private void init() {
		for (int i = 0; i < 7; i++) {
			Weather w = new Weather();
			arrWeather.add(w);
		}
		
		//금일 날씨
		setTodayWeather();
		
		//내일, 모레 날씨
		setTomorrowWeather();
		setTheDayAfterTomorrowWeather();
		
		//주간 날씨
		setWeekWeather();
	}
	
	private String roundTemp(String temperature) {
		if(temperature == null)
			return "-";
		
		try {
			float temp = Float.parseFloat(temperature);
			int t = Math.round(temp);
			
			return Integer.toString(t) + "°";
			
		} catch (Exception e) {
		}
		
		return "-";
	}
	
	private void setTodayWeather() {
		//========== 현재기온 ==========
		for (UltraSrtNcst.Item item : ultraSrtNcst.getItem()) {
			if(item.getCategory().equals("T1H")) {
				arrWeather.get(0).setCurTemp( roundTemp(item.getObsrValue()) );
			}
		}
		
		//========== 최고,최저 기온 ==========
		int curTime = calendar.get(Calendar.HOUR_OF_DAY)*60 + calendar.get(Calendar.MINUTE);
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		String todayDate = format.format(date);

		/*
		 * 금일 최고,최저 기온은 02:00 예보에만 있기때문에
		 * 현재시간이 02:10 이후라면 vilageFcst0200 를 사용
		 * 02:10 이전이라면 vilageFcst 사용 (전날 23:00 데이터)
		 */
		if(curTime > 2*60+10) { //vilageFcst0200 사용
			// 최고기온
			for (VilageFcst.Item item : vilageFcst0200.getItem()) {
				if(item.getCategory().equals("TMX")) {
					if(item.getFcstDate().equals(todayDate)) {
						arrWeather.get(0).setHighestTemp( roundTemp(item.getFcstValue()) );
					}
				}
			}
			
			// 최저기온
			for (VilageFcst.Item item : vilageFcst0200.getItem()) {
				if(item.getCategory().equals("TMN")) {
					if(item.getFcstDate().equals(todayDate)) {
						arrWeather.get(0).setLowestTemp( roundTemp(item.getFcstValue()) );
					}
				}
			}
			
		}else { //vilageFcst 사용
			// 최고기온
			for (VilageFcst.Item item : vilageFcst.getItem()) {
				if(item.getCategory().equals("TMX")) {
					if(item.getFcstDate().equals(todayDate)) {
						arrWeather.get(0).setHighestTemp( roundTemp(item.getFcstValue()) );
					}
				}
			}
			
			// 최저기온
			for (VilageFcst.Item item : vilageFcst.getItem()) {
				if(item.getCategory().equals("TMN")) {
					if(item.getFcstDate().equals(todayDate)) {
						arrWeather.get(0).setLowestTemp( roundTemp(item.getFcstValue()) );
					}
				}
			}
		}
		
	}
	
	private void setTomorrowWeather() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		String tomorrowDate = format.format(date.getTime() + 60*60*24*1000);
		
		// ========== 강수확률 ==========
		//1. 내일 날짜의 강수확률 값 모으기
		ArrayList<Integer> arrPop = new ArrayList<Integer>();
		
		for (VilageFcst.Item item : vilageFcst.getItem()) {
			if(item.getCategory().equals("POP")) {
				if(item.getFcstDate().equals(tomorrowDate)) {
					arrPop.add( Integer.parseInt(item.getFcstValue()) );
				}
			}
		}
		
		//2. 가장 큰 값으로 세팅
		arrWeather.get(1).setRainfallProb(Collections.max(arrPop).toString() + "%"); 
		
		
		// ========== 최고,최저 기온 ==========
		// 최고기온
		for (VilageFcst.Item item : vilageFcst.getItem()) {
			if(item.getCategory().equals("TMX")) {
				if(item.getFcstDate().equals(tomorrowDate)) {
					arrWeather.get(1).setHighestTemp( roundTemp(item.getFcstValue()) );
				}
			}
		}
		
		// 최저기온
		for (VilageFcst.Item item : vilageFcst.getItem()) {
			if(item.getCategory().equals("TMN")) {
				if(item.getFcstDate().equals(tomorrowDate)) {
					arrWeather.get(1).setLowestTemp( roundTemp(item.getFcstValue()) );
				}
			}
		}
	}
	
	private void setTheDayAfterTomorrowWeather() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		String ttDate = format.format(date.getTime() + 60*60*24*2*1000);
		
		int curTime = calendar.get(Calendar.HOUR_OF_DAY)*60 + calendar.get(Calendar.MINUTE);
		
		/*
		 * 모레 최고,최저 기온은 02:00 예보에는 없기 때문에
		 * 현재시간이 05:10 이후라면 vilageFcst 를 사용
		 * 05:10 이전이라면 중기예보를 사용 (전날 18:00 데이터)
		 */
		if(curTime > 5*60+10) { // 동네예보 데이터에서 추출해야 함
			// ========== 강수확률 ==========
			//1. 모레 날짜의 강수확률 값 모으기
			ArrayList<Integer> arrPop = new ArrayList<Integer>();
			
			for (VilageFcst.Item item : vilageFcst.getItem()) {
				if(item.getCategory().equals("POP")) {
					if(item.getFcstDate().equals(ttDate)) {
						arrPop.add( Integer.parseInt(item.getFcstValue()) );
					}
				}
			}
			
			//2. 가장 큰 값
			arrWeather.get(2).setRainfallProb(Collections.max(arrPop).toString() + "%"); 
			
			
			// ========== 최고,최저 기온 ==========
			// 최고기온
			for (VilageFcst.Item item : vilageFcst.getItem()) {
				if(item.getCategory().equals("TMX")) {
					if(item.getFcstDate().equals(ttDate)) {
						arrWeather.get(2).setHighestTemp( roundTemp(item.getFcstValue()) );
					}
				}
			}
			
			// 최저기온
			for (VilageFcst.Item item : vilageFcst.getItem()) {
				if(item.getCategory().equals("TMN")) {
					if(item.getFcstDate().equals(ttDate)) {
						arrWeather.get(2).setLowestTemp( roundTemp(item.getFcstValue()) );
					}
				}
			}
			
		}else { // 중기예보 데이터에서 추출해야함
			// ========== 강수확률 ==========
			arrWeather.get(2).setRainfallProb( Math.max(midLandFcst.getRnSt3Am(), midLandFcst.getRnSt3Pm())+ "%" );
			
			// ========== 최고,최저 기온 ==========
			arrWeather.get(2).setHighestTemp(midTa.getTaMax3() + "°");
			arrWeather.get(2).setLowestTemp(midTa.getTaMin3() + "°");
		}
	}
	
	private void setWeekWeather() {
		int curTime = calendar.get(Calendar.HOUR_OF_DAY)*60 + calendar.get(Calendar.MINUTE);

		if(curTime > 6*60+30) { //오늘날짜
			// ========== 강수확률 ==========
			int[] rainFallProb = {
					Math.max(midLandFcst.getRnSt3Am(), midLandFcst.getRnSt3Pm()),
					Math.max(midLandFcst.getRnSt4Am(), midLandFcst.getRnSt4Pm()),
					Math.max(midLandFcst.getRnSt5Am(), midLandFcst.getRnSt5Pm()),
					Math.max(midLandFcst.getRnSt6Am(), midLandFcst.getRnSt6Pm())
			};
			for (int i = 3; i < 7; i++) {
				arrWeather.get(i).setRainfallProb(rainFallProb[i-3] + "%");
			}
			
			// ========== 최고,최저 기온 ==========
			int[] highTemp = {midTa.getTaMax3(), midTa.getTaMax4(), midTa.getTaMax5(), midTa.getTaMax6()};
			int[] lowTemp = {midTa.getTaMin3(), midTa.getTaMin4(), midTa.getTaMin5(), midTa.getTaMin6()};
			for (int i = 3; i < 7; i++) {
				arrWeather.get(i).setHighestTemp(highTemp[i-3] + "°");
				arrWeather.get(i).setLowestTemp(lowTemp[i-3] + "°");
			}
			
		}else { //어제날짜 1800
			// ========== 강수확률 ==========
			int[] rainFallProb = {
					Math.max(midLandFcst.getRnSt4Am(), midLandFcst.getRnSt4Pm()),
					Math.max(midLandFcst.getRnSt5Am(), midLandFcst.getRnSt5Pm()),
					Math.max(midLandFcst.getRnSt6Am(), midLandFcst.getRnSt6Pm()),
					Math.max(midLandFcst.getRnSt7Am(), midLandFcst.getRnSt7Pm())
			};
			for (int i = 3; i < 7; i++) {
				arrWeather.get(i).setRainfallProb(rainFallProb[i-3] + "%");
			}
			
			// ========== 최고,최저 기온 ==========
			int[] highTemp = {midTa.getTaMax4(), midTa.getTaMax5(), midTa.getTaMax6(), midTa.getTaMax7()};
			int[] lowTemp = {midTa.getTaMin4(), midTa.getTaMin5(), midTa.getTaMin6(), midTa.getTaMin7()};
			for (int i = 3; i < 7; i++) {
				arrWeather.get(i).setHighestTemp(highTemp[i-3] + "°");
				arrWeather.get(i).setLowestTemp(lowTemp[i-3] + "°");
			}
		}
		
	}
	
	
	
	public class Weather{
		private String curTemp;
		private String highestTemp;
		private String lowestTemp;
		private String rainfallProb;
		
		public String getCurTemp() {
			return curTemp;
		}
		public void setCurTemp(String curTemp) {
			this.curTemp = curTemp;
		}
		public String getHighestTemp() {
			return highestTemp;
		}
		public void setHighestTemp(String highestTemp) {
			this.highestTemp = highestTemp;
		}
		public String getLowestTemp() {
			return lowestTemp;
		}
		public void setLowestTemp(String lowestTemp) {
			this.lowestTemp = lowestTemp;
		}
		public String getRainfallProb() {
			return rainfallProb;
		}
		public void setRainfallProb(String rainfallProb) {
			this.rainfallProb = rainfallProb;
		}
	}
}


