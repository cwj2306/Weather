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
			// convertView�� null�̸� Holder ��ü�� �����ϰ�
	        // ������ Holder ��ü�� inflating �� ���� �������� ����
			viewHolder = new ViewHolder();
			
			convertView = mLayoutInflater.inflate(R.layout.listview_item, parent, false);
			convertView.getLayoutParams().height = 150;
			if(position%2 == 1) {
				convertView.setBackgroundColor(Color.rgb(240, 240, 240));	
			}
			
			viewHolder.tv = (TextView)convertView.findViewById(R.id.tv_item_location);
			
			// View�� �±׿� Holder ��ü�� ����
	        convertView.setTag(viewHolder);
	        
		}else {
			// convertView�� null�� �ƴϸ� �並 �����Ҷ� �±׿� �����ߴ� Holder ��ü�� ����
	        // �� Holder ��ü�� �ڽ��� inflating�� ������(�ٽ� ������ �ʿ䰡 ����.)
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
