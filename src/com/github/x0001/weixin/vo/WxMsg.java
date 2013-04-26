package com.github.x0001.weixin.vo;


public class WxMsg {
	private String toUser;
	private String fromUser;
	private String createDt;
	private String msgType;
	
	public WxMsg(String toUser,String fromUser,String createDt,String msgType) {
		this.toUser = toUser;
		this.fromUser = fromUser;
		this.createDt = createDt;
		this.msgType = msgType;
	}
	
	public String getToUser() {
		return toUser;
	}
	public void setToUser(String toUser) {
		this.toUser = toUser;
	}
	public String getFromUser() {
		return fromUser;
	}
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	public String getCreateDt() {
		return createDt;
	}
	public void setCreateDt(String createDt) {
		this.createDt = createDt;
	}
	public String getMsgType() {
		return msgType;
	}
	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
}
