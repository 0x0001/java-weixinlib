package com.github.x0001.weixin.vo.send;

import org.jdom.Document;
import org.jdom.Element;

public class WxSendMusicMsg extends WxSendMsg {
	private String musicUrl;
	private String hqMusicUrl;
	private String description;
	private String title;
	
	public WxSendMusicMsg(WxSendMsg msg,String title,String description,String musicUrl,String hqMusicUrl) {
		super(msg);
		setMsgType("music");
		this.title = title;
		this.description = description;
		this.musicUrl = musicUrl;
		this.hqMusicUrl = hqMusicUrl;
	}
	
	public String getMusicUrl() {
		return musicUrl;
	}
	public void setMusicUrl(String musicUrl) {
		this.musicUrl = musicUrl;
	}
	public String getHqMusicUrl() {
		return hqMusicUrl;
	}
	public void setHqMusicUrl(String hqMusicUrl) {
		this.hqMusicUrl = hqMusicUrl;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	
	@Override
	public Document toDocument() {
		Document doc = super.toDocument();
		Element music = createElement(doc.getRootElement(), "Music", "");
		createElement(music, "Description", getDescription());
		createElement(music, "Title", getTitle());
		createElement(music, "MusicUrl", getMusicUrl());
		createElement(music, "HQMusicUrl", getHqMusicUrl());
		return doc;
	}
}
