package com.github.x0001.weixin.parser;

import org.jdom.Element;
import org.jdom.JDOMException;

import com.github.x0001.weixin.vo.recv.WxRecvLinkMsg;
import com.github.x0001.weixin.vo.recv.WxRecvMsg;

public class WxRecvLinkMsgParser extends WxRecvMsgBaseParser {

	@Override
	protected WxRecvLinkMsg parser(Element root, WxRecvMsg msg) throws JDOMException {
		
		String title = getElementText(root, "Title");
		String description = getElementText(root, "Description");
		String url = getElementText(root, "Url");
		return new WxRecvLinkMsg(msg, title, description, url);
	}

}
