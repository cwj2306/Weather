package com.example.weather;

import java.lang.reflect.Array;
import java.util.ArrayList;

import com.example.weather.listviewAdapter.MyAdapter;
import com.example.weather.util.LocationUtil;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class AddLocationActivity extends Activity {
	
	private MyAdapter myAdapter;
	private InputMethodManager imm;
	
	private EditText etSearch;
	
	private ArrayList<String> searchResultList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_location);
		
		init();
	}
	
	private void init() {
		myAdapter = new MyAdapter(this);
		// (키패드 제어) 입력받는 방법을 관리하는 Manager객체
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		
		//리스트뷰에 어댑터 등록
		ListView listView = (ListView)findViewById(R.id.listview);
		listView.setAdapter(myAdapter);
		
		//EditText 검색 리스너 등록
		etSearch = (EditText)findViewById(R.id.et_search);
		etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        switch (actionId) {
		            case EditorInfo.IME_ACTION_SEARCH:
		                // 검색 동작
		            	search();
		            	
		                break;
		            default:
		                // 기본 엔터키 동작
		                return false;
		        }
		        return true;
		    }
		 });

		//검색버튼 리스너 등록
		ImageButton btSearch = (ImageButton)findViewById(R.id.bt_search);
		btSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//키패드 내리기
				imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
				
				search();
			}
		});
	}
	
	private void search() {
		String word = etSearch.getText().toString();
		
		if(word.length()==0) {
			ArrayList<String> locationList = new ArrayList<String>( LocationUtil.locationList.keySet() );
			myAdapter.addItems(locationList);
			myAdapter.notifyDataSetChanged();
		}else {
			searchResultList.clear();
			
			for (String item : LocationUtil.locationList.keySet()) {
				if( item.contains(word) ) {
					searchResultList.add(item);
				}
			}
			myAdapter.addItems(searchResultList);
			myAdapter.notifyDataSetChanged();
		}
	}
}
