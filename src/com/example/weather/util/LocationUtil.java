package com.example.weather.util;

import java.util.HashMap;

public class LocationUtil {
	public static HashMap<String, Location> locationList = new HashMap<String, LocationUtil.Location>();
	
	public static void setLocation() {
		if(locationList.size()==0) {
			locationList.put("서울", new Location("서울", "61", "126", "11B10101", "11B00000"));
			locationList.put("부산", new Location("부산", "98", "76", "11H20201", "11H20000"));
			locationList.put("대구", new Location("대구", "89", "90", "11H10701", "11H10000"));
			locationList.put("광주", new Location("광주", "58", "74", "11F20501", "11F20000"));
			locationList.put("전주", new Location("전주", "63", "89", "11F10201", "11F10000"));
			locationList.put("대전", new Location("대전", "67", "100", "11C20401", "11C20000"));
			locationList.put("청주", new Location("청주", "69", "107", "11C10301", "11C10000"));
			locationList.put("철원", new Location("철원", "65", "139", "11D10101", "11D10000"));
			locationList.put("제주", new Location("제주", "52", "38", "11G00201", "11G00000"));
		}
	}
	
	
	public static class Location {
		private String name;
		private String nx;
		private String ny;
		private String codeMidTa;
		private String codeMidLandFcst;
		
		public Location(String name, String nx, String ny, String codeMidTa, String codeMidLandFcst) {
			this.name = name;
			this.nx = nx;
			this.ny = ny;
			this.codeMidTa = codeMidTa;
			this.codeMidLandFcst = codeMidLandFcst;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getNx() {
			return nx;
		}
		public void setNx(String nx) {
			this.nx = nx;
		}
		public String getNy() {
			return ny;
		}
		public void setNy(String ny) {
			this.ny = ny;
		}
		public String getCodeMidTa() {
			return codeMidTa;
		}
		public void setCodeMidTa(String codeMidTa) {
			this.codeMidTa = codeMidTa;
		}
		public String getCodeMidLandFcst() {
			return codeMidLandFcst;
		}
		public void setCodeMidLandFcst(String codeMidLandFcst) {
			this.codeMidLandFcst = codeMidLandFcst;
		}
	}
}
