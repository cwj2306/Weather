package com.example.weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.example.weather.httpConn.ApiExplorer;
import com.example.weather.model.MidLandFcst;
import com.example.weather.model.MidTa;
import com.example.weather.model.UltraSrtNcst;
import com.example.weather.model.VilageFcst;
import com.example.weather.model.WeatherData;
import com.example.weather.util.LocationUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private UltraSrtNcst ultraSrtNcst = null;
	private VilageFcst vilageFcst = null;
	private VilageFcst vilageFcst0200 = null;
	private MidLandFcst midLandFcst = null;
	private MidTa midTa = null;
	
	private WeatherData weatherData = null;
	private String curLocation = "서울";
	
	private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
	
	private Date date;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//기본 SharedPreferences 환경과 관련된 객체를 얻어옵니다.
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        // SharedPreferences 수정을 위한 Editor 객체를 얻어옵니다.
        editor = preferences.edit();
        
		init();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case 1:
			if(resultCode == RESULT_OK) {
				curLocation = data.getStringExtra("result");
				editor.putString("location", curLocation);
                editor.apply();
				
				setLocation();
				setDate();
				
				HttpAsyncTask hat = new HttpAsyncTask();
                hat.execute();
			}
			break;
		default:
			break;
		}
	}
	
	private void init() {
		setLocation();
		setDate();
		setClickListener();
		
		HttpAsyncTask hat = new HttpAsyncTask();
        hat.execute();
	}
	
	private void setLocation() {
		LocationUtil.setLocation();
		
		curLocation = preferences.getString("location","서울");
		((TextView)findViewById(R.id.tv_location)).setText(curLocation);
	}
	
	private void setDate() {
		date = new Date();
		
		//==========오늘 날짜 세팅==========
		SimpleDateFormat format = new SimpleDateFormat("M/d E요일", Locale.KOREAN);
		format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		
		((TextView)findViewById(R.id.tv_date)).setText(format.format(date));
		
		//==========주간 날짜 세팅==========
		SimpleDateFormat format2 = new SimpleDateFormat("E\nM/d", Locale.KOREAN);
		format2.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		
		int[] tvIds = {R.id.tv_date1, R.id.tv_date2, R.id.tv_date3, R.id.tv_date4, R.id.tv_date5, R.id.tv_date6};
		for (int i = 0; i<tvIds.length; i++) {
			((TextView)findViewById(tvIds[i])).setText(format2.format(date.getTime() + 60*60*24*(i+1)*1000));
		}
		
		//==========업데이트 시간 세팅==========
		SimpleDateFormat format3 = new SimpleDateFormat("YY/M/d HH:mm", Locale.KOREAN);
		format3.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
		
		((TextView)findViewById(R.id.tv_update)).setText(format3.format(date));
	}
	
	private void setClickListener() {
		ImageButton btAdd = (ImageButton)findViewById(R.id.bt_add);
		btAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), AddLocationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, 1);
			}
		});
		
		ImageButton btUpdate = (ImageButton)findViewById(R.id.bt_update);
		btUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setDate();
				
				HttpAsyncTask hat = new HttpAsyncTask();
                hat.execute();
			}
		});
	}
	
	
	private class HttpAsyncTask extends AsyncTask<Void, Void, Boolean>{
		private ProgressDialog progressDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			// ProgressDialog 설정
			progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("데이터 받아오는 중...");
            progressDialog.setCancelable(false);
            // ProgressDialog 띄우기
            progressDialog.show();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			
			ApiExplorer apiExplorer = new ApiExplorer(date, curLocation);
			
			try {
				ultraSrtNcst = apiExplorer.getUltraSrtNcst();
			} catch (Exception e) {
				Log.d("Weather", "httpConn error : ultraSrtNcst");
				return false;
			}
			
			try {
				vilageFcst = apiExplorer.getVilageFcst();
			} catch (Exception e) {
				Log.d("Weather", "httpConn error : vilageFcst");
				return false;
			}
			
			try {
				vilageFcst0200 = apiExplorer.getVilageFcst0200();
			} catch (Exception e) {
				Log.d("Weather", "httpConn error : vilageFcst0200");
				return false;
			}
			
			try {
				midLandFcst = apiExplorer.getMidLandFcst();
			} catch (Exception e) {
				Log.d("Weather", "httpConn error : midLandFcst");
				return false;
			}
			
			try {
				midTa = apiExplorer.getMidTa();
			} catch (Exception e) {
				Log.d("Weather", "httpConn error : midTa");
				return false;
			}
			
			if( (ultraSrtNcst != null)&&(vilageFcst != null)&&(vilageFcst0200 != null)&&(midLandFcst != null)&&(midTa != null) ) {
				weatherData = new WeatherData(date, ultraSrtNcst, vilageFcst, vilageFcst0200, midLandFcst, midTa);
			}else {
				return false;
			}
			
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			
			progressDialog.dismiss();

			if(result) {
				//현재온도
				((TextView)findViewById(R.id.tv_curTemp)).setText(weatherData.getArrWeather().get(0).getCurTemp());
				//금일 최고,최저 기온
				((TextView)findViewById(R.id.tv_highTemp)).setText(weatherData.getArrWeather().get(0).getHighestTemp());
				((TextView)findViewById(R.id.tv_lowTemp)).setText(weatherData.getArrWeather().get(0).getLowestTemp());
				
				//주간 최고,최저 기온
				int[] tvHtIds = {R.id.tv_ht1, R.id.tv_ht2, R.id.tv_ht3, R.id.tv_ht4, R.id.tv_ht5, R.id.tv_ht6};
				for (int i = 0; i < tvHtIds.length; i++) {
					((TextView)findViewById(tvHtIds[i])).setText(weatherData.getArrWeather().get(i+1).getHighestTemp());
				}
				int[] tvLtIds = {R.id.tv_lt1, R.id.tv_lt2, R.id.tv_lt3, R.id.tv_lt4, R.id.tv_lt5, R.id.tv_lt6};
				for (int i = 0; i < tvLtIds.length; i++) {
					((TextView)findViewById(tvLtIds[i])).setText(weatherData.getArrWeather().get(i+1).getLowestTemp());
				}
				
				//주간 강수확률
				int[] tvRfpIds = {R.id.tv_rfp1, R.id.tv_rfp2, R.id.tv_rfp3, R.id.tv_rfp4, R.id.tv_rfp5, R.id.tv_rfp6};
				for (int i = 0; i < tvRfpIds.length; i++) {
					((TextView)findViewById(tvRfpIds[i])).setText(weatherData.getArrWeather().get(i+1).getRainfallProb());
				}
			}
		}
	}

}
