package com.github.x0001.weixin.vo.recv;


public class WxRecvPicMsg extends WxRecvMsg {
	private String picUrl;

	public WxRecvPicMsg(WxRecvMsg msg,String picUrl) {
		super(msg);
		this.picUrl = picUrl;
	}
	
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
}
