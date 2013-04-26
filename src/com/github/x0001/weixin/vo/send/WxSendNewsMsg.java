package com.github.x0001.weixin.vo.send;

import java.util.LinkedList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

public class WxSendNewsMsg extends WxSendMsg {
	private List<WxSendNewsMsgItem> items = new LinkedList<WxSendNewsMsgItem>();

	public WxSendNewsMsg(WxSendMsg msg) {
		super(msg);
		setMsgType("news");
	}
	public void setItems(List<WxSendNewsMsgItem> items) {
		this.items = items;
	}
	
	public WxSendNewsMsg addItem(String title,String description,String picUrl,String url) {
		if(items.size() >= 10) {
			throw new IllegalArgumentException("只能接受最多10个item...");
		}
		items.add(new WxSendNewsMsgItem(title,description,picUrl,url));
		return this;
	}
	
	@Override
	public Document toDocument() {
		Document doc = super.toDocument();
		Element root = doc.getRootElement();
		createElement(root, "ArticleCount", String.valueOf(items.size()));
		Element articles = createElement(root, "Articles","");
		for(WxSendNewsMsgItem item : items) {
			Element i = createElement(articles, "item", "");
			createElement(i, "Title", item.getTitle());
			createElement(i, "Description", item.getDescription());
			createElement(i, "PicUrl", item.getPicUrl());
			createElement(i, "Url", item.getUrl());
		}
		return doc;
	}
}