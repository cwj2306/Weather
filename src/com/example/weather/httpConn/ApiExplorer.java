package com.example.weather.httpConn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import org.json.JSONObject;

import com.example.weather.model.MidLandFcst;
import com.example.weather.model.MidTa;
import com.example.weather.model.UltraSrtNcst;
import com.example.weather.model.VilageFcst;
import com.example.weather.util.BaseTimeUtil;
import com.example.weather.util.LocationUtil;
import com.google.gson.Gson;

import android.util.Log;

public class ApiExplorer {
	private String serviceKey;
	private BaseTimeUtil util;
	private String location;
	
	public ApiExplorer(Date date, String location) {
		serviceKey = "titcylZWFyj%2BdRAmjmFcb0cwSei6pmy5FYWtGrblQucx6e4dg9nyWOzmksBh0z3Rt385A5W8xcWMfOm0H3HyWw%3D%3D";
		util = new BaseTimeUtil(date);
		this.location = location;
	}
	
	// 동네예보 조회서비스 - 초단기실황조회 (현재기온)
	public UltraSrtNcst getUltraSrtNcst() throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService/getUltraSrtNcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(util.getDateUltraSrtNcst(), "UTF-8")); /*YYYYMMDD*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(util.getTimeUltraSrtNcst(), "UTF-8")); /*HHmm*/
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(LocationUtil.locationList.get(location).getNx(), "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(LocationUtil.locationList.get(location).getNy(), "UTF-8")); /*예보지점 Y 좌표*/
        // 61, 126 : 서울 강남구
        String response = getData(urlBuilder.toString());
        
        UltraSrtNcst ultraSrtNcst = null;
        try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject jsonUltraSrtNcst = jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items");
			
			Gson gson = new Gson();
			ultraSrtNcst = gson.fromJson(jsonUltraSrtNcst.toString(), UltraSrtNcst.class);
		} catch (Exception e) {
			Log.d("Weather", "parsing error : ultraSrtNcst(초단기실황조회)");
		}
        
        return ultraSrtNcst;
    }
	
	// 동네예보 조회서비스  - 동네예보조회 (오늘, 내일, 모레 최고최저 기온+강수확률)
	public VilageFcst getVilageFcst() throws IOException {
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("300", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(util.getDateVilageFcst(), "UTF-8")); /*YYYYMMDD*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(util.getTimeVilageFcst(), "UTF-8")); /*HHmm*/
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(LocationUtil.locationList.get(location).getNx(), "UTF-8")); /*예보지점 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(LocationUtil.locationList.get(location).getNy(), "UTF-8")); /*예보지점의 Y 좌표값*/
        
        String response = getData(urlBuilder.toString());
        
        VilageFcst vilageFcst = null;
        try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject jsonUltraSrtNcst = jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items");
			
			Gson gson = new Gson();
			vilageFcst = gson.fromJson(jsonUltraSrtNcst.toString(), VilageFcst.class);
		} catch (Exception e) {
			Log.d("Weather", "parsing error : vilageFcst(동네예보조회)");
		}
        
        return vilageFcst;
	}
	
	// 동네예보 조회서비스  - 동네예보조회 02:00 기준 자료
	public VilageFcst getVilageFcst0200() throws IOException {
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("300", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(util.getDateVilageFcst(), "UTF-8")); /*YYYYMMDD*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode("0200", "UTF-8")); /*HHmm*/
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode(LocationUtil.locationList.get(location).getNx(), "UTF-8")); /*예보지점 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode(LocationUtil.locationList.get(location).getNy(), "UTF-8")); /*예보지점의 Y 좌표값*/
        
        String response = getData(urlBuilder.toString());
        
        VilageFcst vilageFcst = null;
        try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject jsonUltraSrtNcst = jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items");
			
			Gson gson = new Gson();
			vilageFcst = gson.fromJson(jsonUltraSrtNcst.toString(), VilageFcst.class);
		} catch (Exception e) {
			Log.d("Weather", "parsing error : vilageFcst0200(동네예보조회)");
		}
        
        return vilageFcst;
	}
	
	// 중기예보 조회서비스 - 중기육상예보조회 (3일후~10일후 강수확률)
	public MidLandFcst getMidLandFcst() throws IOException {
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/MidFcstInfoService/getMidLandFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("regId","UTF-8") + "=" + URLEncoder.encode(LocationUtil.locationList.get(location).getCodeMidLandFcst(), "UTF-8")); /*하단 참고자료 참조*/
        urlBuilder.append("&" + URLEncoder.encode("tmFc","UTF-8") + "=" + URLEncoder.encode(util.getDateTime(), "UTF-8")); /*-일 2회(06:00,18:00)회 생성 되며 발표시각을 입력-최근 24시간 자료만 제공*/
		
		String response = getData(urlBuilder.toString());
		
		MidLandFcst midLandFcst = null;
        try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject jsonUltraSrtNcst = jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item").getJSONObject(0);
			
			Gson gson = new Gson();
			midLandFcst = gson.fromJson(jsonUltraSrtNcst.toString(), MidLandFcst.class);
		} catch (Exception e) {
			Log.d("Weather", "parsing error : midLandFcst(중기육상예보조회)");
		}
        
        return midLandFcst;
	}
	
	// 중기예보 조회서비스 - 중기기온조회 (3일후~10일후 최고최저 기온)
	public MidTa getMidTa() throws IOException {
		StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/MidFcstInfoService/getMidTa"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey","UTF-8") + "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON)Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("regId","UTF-8") + "=" + URLEncoder.encode(LocationUtil.locationList.get(location).getCodeMidTa(), "UTF-8")); /*별첨 엑셀파일 참조*/
        urlBuilder.append("&" + URLEncoder.encode("tmFc","UTF-8") + "=" + URLEncoder.encode(util.getDateTime(), "UTF-8")); /*-일 2회(06:00,18:00)회 생성 되며 발표시각을 입력-최근 24시간 자료만 제공*/
	
		String response = getData(urlBuilder.toString());
		
		MidTa midTa = null;
        try {
			JSONObject jsonObject = new JSONObject(response);
			JSONObject jsonUltraSrtNcst = jsonObject.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item").getJSONObject(0);
			
			Gson gson = new Gson();
			midTa = gson.fromJson(jsonUltraSrtNcst.toString(), MidTa.class);
		} catch (Exception e) {
			Log.d("Weather", "parsing error : midTa(중기기온조회)");
		}
        
        return midTa;
	}
	
	
	private String getData(String requestURL) throws IOException{
		URL url = new URL(requestURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        
        return sb.toString();
	}
	
}
