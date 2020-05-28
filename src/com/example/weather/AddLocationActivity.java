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
		// (Ű�е� ����) �Է¹޴� ����� �����ϴ� Manager��ü
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		
		//����Ʈ�信 ����� ���
		ListView listView = (ListView)findViewById(R.id.listview);
		listView.setAdapter(myAdapter);
		
		//EditText �˻� ������ ���
		etSearch = (EditText)findViewById(R.id.et_search);
		etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        switch (actionId) {
		            case EditorInfo.IME_ACTION_SEARCH:
		                // �˻� ����
		            	search();
		            	
		                break;
		            default:
		                // �⺻ ����Ű ����
		                return false;
		        }
		        return true;
		    }
		 });

		//�˻���ư ������ ���
		ImageButton btSearch = (ImageButton)findViewById(R.id.bt_search);
		btSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//Ű�е� ������
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
