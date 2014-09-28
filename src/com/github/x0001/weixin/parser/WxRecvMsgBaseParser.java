package com.github.x0001.weixin.parser;

import com.github.x0001.weixin.vo.recv.WxRecvMsg;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.xpath.XPath;

public abstract class WxRecvMsgBaseParser implements WxRecvMsgParser {

	@Override
	public WxRecvMsg parser(Document doc) throws JDOMException {
		Element root = doc.getRootElement();
		String toUserName = getElementText(root, "ToUserName");
		String fromUserName = getElementText(root, "FromUserName");
		String createTime = getElementText(root, "CreateTime");
		String msgType = getElementText(root, "MsgType");
		String msgId = getElementText(root, "MsgId");

		return parser(root, new WxRecvMsg(toUserName, fromUserName, createTime, msgType, msgId));
	}

	protected abstract WxRecvMsg parser(Element root, WxRecvMsg msg) throws JDOMException;

	protected String getElementText(Element elem, String xpath) throws JDOMException {
		Text text = ((Text) XPath.selectSingleNode(elem, xpath + "/text()"));
		if (null == text) {
			return "";
		}
		return text.getText();
	}

}
