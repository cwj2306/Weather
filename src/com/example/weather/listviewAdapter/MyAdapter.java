package com.example.weather.listviewAdapter;

import java.util.ArrayList;

import com.example.weather.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{

	Activity mActivity = null;
	LayoutInflater mLayoutInflater = null;
	private ArrayList<String> items = new ArrayList<String>();
	
	public MyAdapter(Context context) {
		mActivity = (Activity)context;
		mLayoutInflater = LayoutInflater.from(context);
	}
	
	public void addItems(ArrayList<String> items) {
		this.items = items;
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public String getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		
		if(convertView == null) {
			// convertView가 null이면 Holder 객체를 생성하고
	        // 생성한 Holder 객체에 inflating 한 뷰의 참조값을 저장
			viewHolder = new ViewHolder();
			
			convertView = mLayoutInflater.inflate(R.layout.listview_item, parent, false);
			convertView.getLayoutParams().height = 150;
			if(position%2 == 1) {
				convertView.setBackgroundColor(Color.rgb(240, 240, 240));	
			}
			
			viewHolder.tv = (TextView)convertView.findViewById(R.id.tv_item_location);
			
			// View의 태그에 Holder 객체를 저장
	        convertView.setTag(viewHolder);
	        
		}else {
			// convertView가 null이 아니면 뷰를 생성할때 태그에 저장했던 Holder 객체가 존재
	        // 이 Holder 객체는 자신을 inflating한 참조값(다시 전개할 필요가 없다.)
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.tv.setText(items.get(position));
		viewHolder.tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
		        intent.putExtra("result", ((TextView)v).getText().toString());
		        mActivity.setResult(mActivity.RESULT_OK, intent);
		        mActivity.finish();
			}
		});
		
		return convertView;
	}
	
	private class ViewHolder{
		private TextView tv;
	}
	
}
