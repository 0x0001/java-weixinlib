package com.github.x0001.weixin.parser;

import org.jdom.Document;
import org.jdom.JDOMException;

import com.github.x0001.weixin.vo.recv.WxRecvMsg;

public interface WxRecvMsgParser {
	WxRecvMsg parser(Document doc) throws JDOMException;
}
