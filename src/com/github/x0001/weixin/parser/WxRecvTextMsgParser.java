package com.github.x0001.weixin.parser;

import org.jdom.Element;
import org.jdom.JDOMException;

import com.github.x0001.weixin.vo.recv.WxRecvMsg;
import com.github.x0001.weixin.vo.recv.WxRecvTextMsg;

public class WxRecvTextMsgParser extends WxRecvMsgBaseParser{

	@Override
	protected WxRecvTextMsg parser(Element root, WxRecvMsg msg) throws JDOMException {
		return new WxRecvTextMsg(msg, getElementText(root, "Content"));
	}

	
}
