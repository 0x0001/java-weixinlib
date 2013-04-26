package com.github.x0001.weixin.vo.recv;


public class WxRecvGeoMsg extends WxRecvMsg {
	// Location_x
	private double latitude;
	// Location_y
	private double longitude;
	private int scale;
	private String label;
	
	public WxRecvGeoMsg(WxRecvMsg msg,double latitude,double longitude,int scale,String label) {
		super(msg);
		this.latitude = latitude;
		this.longitude = longitude;
		this.scale = scale;
		this.label = label;
	}
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
