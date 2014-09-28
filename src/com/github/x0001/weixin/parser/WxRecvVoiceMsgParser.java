package com.github.x0001.weixin.parser;

import com.github.x0001.weixin.vo.recv.WxRecvMsg;
import com.github.x0001.weixin.vo.recv.WxRecvVoiceMsg;
import org.jdom.Element;
import org.jdom.JDOMException;

/**
 * 微信语音消息解析
 * Created by 0x0001 on 14-9-28.
 */
public class WxRecvVoiceMsgParser extends WxRecvMsgBaseParser {
	@Override
	protected WxRecvMsg parser(Element root, WxRecvMsg msg) throws JDOMException {
		String mediaID = getElementText(root, "MediaId");
		String format = getElementText(root, "Format");
		String recognition = getElementText(root, "Recognition");
		WxRecvVoiceMsg wxRecvVoiceMsg = new WxRecvVoiceMsg(msg, mediaID, format);
		wxRecvVoiceMsg.setRecognition(recognition);
		return wxRecvVoiceMsg;
	}
}
