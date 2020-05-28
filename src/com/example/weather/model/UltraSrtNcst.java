package com.example.weather.model;

import java.util.ArrayList;

public class UltraSrtNcst {
	
	private ArrayList<Item> item = new ArrayList<Item>();
	
	public ArrayList<Item> getItem() {
		return item;
	}
	public void setItem(ArrayList<Item> item) {
		this.item = item;
	}

	public class Item {
		private String baseDate;
		private String baseTime;
		private String category;
		private int nx;
		private int ny;
		private String obsrValue;
		
		public String getBaseDate() {
			return baseDate;
		}
		public void setBaseDate(String baseDate) {
			this.baseDate = baseDate;
		}
		public String getBaseTime() {
			return baseTime;
		}
		public void setBaseTime(String baseTime) {
			this.baseTime = baseTime;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public int getNx() {
			return nx;
		}
		public void setNx(int nx) {
			this.nx = nx;
		}
		public int getNy() {
			return ny;
		}
		public void setNy(int ny) {
			this.ny = ny;
		}
		public String getObsrValue() {
			return obsrValue;
		}
		public void setObsrValue(String obsrValue) {
			this.obsrValue = obsrValue;
		}
	}
}
