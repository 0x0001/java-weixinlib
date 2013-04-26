package com.github.x0001.weixin.vo.send;

import org.jdom.Document;
import org.jdom.Element;

import com.github.x0001.weixin.vo.WxMsg;

public class WxSendMsg extends WxMsg {
	// FuncFlag 位0x0001被标志时，星标刚收到的消息。
	private boolean star;
	
	public WxSendMsg(String toUser,String fromUser,String createDt,String msgType,boolean star) {
		super(toUser, fromUser, createDt, msgType);
		this.star = star;
	}
	
	public WxSendMsg(WxMsg msg) {
		this(msg.getToUser(),msg.getFromUser(),msg.getCreateDt(),msg.getMsgType(),false);
	}
	
	public WxSendMsg(WxSendMsg msg) {
		this(msg.getToUser(), msg.getFromUser(), msg.getCreateDt(), msg.getMsgType(), msg.isStar());
	}
	
	public boolean isStar() {
		return star;
	}
	public void setStar(boolean star) {
		this.star = star;
	}
	
	
	public Document toDocument() {
		Document doc = new Document();
		Element root = new Element("xml");
		doc.setRootElement(root);

		createElement(root,"ToUserName", getToUser());
		createElement(root,"FromUserName", getFromUser());
		createElement(root,"CreateTime", getCreateDt());
		createElement(root,"MsgType", getMsgType());
		createElement(root,"FuncFlag", isStar()?"1":"0");
		
		return doc;
	}
	
	@SuppressWarnings("unchecked")
	protected Element createElement(Element parent,String name,String value) {
		Element elem = new Element(name);
		elem.setText(value);
		parent.getChildren().add(elem);
		return elem;
	}
	
}
