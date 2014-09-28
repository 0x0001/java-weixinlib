package com.github.x0001.weixin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.github.x0001.weixin.parser.WxMsgKit;
import com.github.x0001.weixin.vo.recv.WxRecvMsg;
import com.github.x0001.weixin.vo.send.WxSendMsg;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.output.XMLOutputter;

public final class WeiXin {

	/**
	 * 文件上传媒体类型
	 */
	public static enum MediaType {
		IMAGE("image"),
		VOICE("voice"),
		VIDEO("video"),
		THUMB("thumb");

		private final String type;
		MediaType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}
	}

	public static boolean access(String token, String signature, String timestamp, String nonce) {
		List<String> ss = new ArrayList<String>();
		ss.add(timestamp);
		ss.add(nonce);
		ss.add(token);

		Collections.sort(ss);

		StringBuilder builder = new StringBuilder();
		for (String s : ss) {
			builder.append(s);
		}
		return signature.equalsIgnoreCase(HashKit.sha1(builder.toString()));
	}

	public static WxRecvMsg recv(InputStream in) throws JDOMException, IOException {
		return WxMsgKit.parse(in);
	}

	public static void send(WxSendMsg msg, OutputStream out) throws JDOMException, IOException {
		Document doc = WxMsgKit.parse(msg);
		if (null != doc) {
			new XMLOutputter().output(doc, out);
		} else {
			Logger.getAnonymousLogger().warning("发送消息时,解析出dom为空 msg :" + msg);
		}
	}

	/**
	 * 获取媒体资源
	 * @param accessToken
	 * @param mediaID
	 * @return
	 * @throws IOException
	 */
	public static InputStream getMedia(String accessToken, String mediaID) throws IOException {
		return HttpRequest.get("http://file.api.weixin.qq.com/cgi-bin/media/get")
				.addAttr("access_token", accessToken)
				.addAttr("media_id", mediaID)
				.getInputStream();
	}

	/**
	 * 上传媒体资源
	 * @param accessToken
	 * @param type
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static String uploadMedia(String accessToken, MediaType type,  File file) throws IOException {
		return HttpRequest.post("http://file.api.weixin.qq.com/cgi-bin/media/upload")
				.addAttr("access_token", accessToken)
				.addAttr("type", type.getType())
				.addAttr("media", file)
				.getResponse();
	}

	public static WxSendMsg builderSendByRecv(WxRecvMsg msg) {
		WxRecvMsg m = new WxRecvMsg(msg);
		String from = m.getFromUser();
		m.setFromUser(m.getToUser());
		m.setToUser(from);
		m.setCreateDt((System.currentTimeMillis() / 1000) + "");
		return new WxSendMsg(m);
	}
}
