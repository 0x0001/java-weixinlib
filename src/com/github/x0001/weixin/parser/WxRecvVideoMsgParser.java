package com.github.x0001.weixin.parser;

import com.github.x0001.weixin.vo.recv.WxRecvMsg;
import com.github.x0001.weixin.vo.recv.WxRecvVideoMsg;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * 微信视频消息解析
 * Created by 0x0001 on 14-9-28.
 */
public class WxRecvVideoMsgParser extends WxRecvMsgBaseParser {
	@Override
	protected WxRecvMsg parser(Element root, WxRecvMsg msg) throws JDOMException {
		String mediaID = getElementText(root, "MediaId");
		String thumbMediaId = getElementText(root, "ThumbMediaId");
		return new WxRecvVideoMsg(msg, mediaID, thumbMediaId);
	}
}
