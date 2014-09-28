package com.github.x0001.weixin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import com.github.x0001.weixin.vo.recv.*;
import com.github.x0001.weixin.vo.send.WxSendMsg;
import com.github.x0001.weixin.vo.send.WxSendMusicMsg;
import com.github.x0001.weixin.vo.send.WxSendNewsMsg;
import com.github.x0001.weixin.vo.send.WxSendTextMsg;
import org.jdom.JDOMException;

/**
 * 非线程安全 (应该每个请求创建一个)
 * Created by 0x0001 on 14-9-28.
 */
public abstract class WeixinAbstractHandler {
	private final HttpServletRequest req;
	private final HttpServletResponse res;
	private final String TOKEN;
	private WxRecvMsg recvMsg;

	public WeixinAbstractHandler(HttpServletRequest req, HttpServletResponse res, String token) {
		this.req = req;
		this.res = res;
		this.TOKEN = token;
	}

	public void handler() {
		if (!isFromWeiXin()) {
			onNotFromWeixin();
			return;
		}

		if (isWeixinVerifyURL()) {
			renderText(getPara("echostr"));
			return;
		}

		try {
			WxRecvMsg msg = WeiXin.recv(req.getInputStream());
			handler(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ------------------------------------------------------------------- protected Methods

	protected void sendText(String content) {
		WxSendMsg send = WeiXin.builderSendByRecv(this.recvMsg);
		send(new WxSendTextMsg(send, content));
	}

	protected void sendNewsMsg(WxSendNewsMsg wxSendNewsMsg) {
		send(wxSendNewsMsg);
	}

	protected void sendMusicMsg(String title, String desc, String music, String hqMusic) {
		WxSendMsg send = WeiXin.builderSendByRecv(this.recvMsg);
		send(new WxSendMusicMsg(send, title, desc, music, hqMusic));
	}

	protected WxSendNewsMsg buildNewsMsg() {
		return new WxSendNewsMsg(WeiXin.builderSendByRecv(this.recvMsg));
	}

	protected void send(WxSendMsg send) {
		try {
			WeiXin.send(send, res.getOutputStream());
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected boolean isFromWeiXin() {
		if (verifySignature()) {
			return isWeixinVerifyURL() || isPost();
		}
		return false;
	}

	// ------------------------------------------------------------------- Abstract Methods

	/**
	 * 收到图片消息
	 * @param msg
	 */
	protected abstract void onPic(WxRecvPicMsg msg);

	/**
	 * 收到链接消息
	 * @param msg
	 */
	protected abstract void onLink(WxRecvLinkMsg msg);

	/**
	 * 收到地址消息
	 * @param msg
	 */
	protected abstract void onGeo(WxRecvGeoMsg msg);

	/**
	 * 收到文本消息
	 * @param msg
	 */
	protected abstract void onText(WxRecvTextMsg msg);

	/**
	 * 收到事件消息
	 * @param msg
	 */
	protected abstract void onEvent(WxRecvEventMsg msg);

	/**
	 * 语音消息
	 * @param msg
	 */
	protected abstract void onVoice(WxRecvVoiceMsg msg);

	/**
	 * 视频消息
	 * @param msg
	 */
	protected abstract void onVideo(WxRecvVideoMsg msg);


	/**
	 * 请求可能不是来自微信
	 */
	protected abstract void onNotFromWeixin();


	// -------------------------------------------------------- Get Set

	public HttpServletRequest getRequest() {
		return req;
	}

	public HttpServletResponse getResponse() {
		return res;
	}


	// -------------------------------------------------------- Private Methods

	private boolean isPost() {
		return "POST".equalsIgnoreCase(req.getMethod());
	}

	private boolean isWeixinVerifyURL() {
		return getPara("echostr") != null;
	}

	private boolean verifySignature() {
		String signature = getPara("signature");
		String timestamp = getPara("timestamp");
		String nonce = getPara("nonce");
		if (null != timestamp && null != nonce && null != signature) {
			return WeiXin.access(TOKEN, signature, timestamp, nonce);
		}
		return false;
	}

	private void handler(WxRecvMsg msg) {
		this.recvMsg = msg;
		if (msg instanceof WxRecvTextMsg) {
			onText((WxRecvTextMsg) msg);
		} else if (msg instanceof WxRecvEventMsg) {
			onEvent((WxRecvEventMsg) msg);
		} else if (msg instanceof WxRecvGeoMsg) {
			onGeo((WxRecvGeoMsg) msg);
		} else if (msg instanceof WxRecvLinkMsg) {
			onLink((WxRecvLinkMsg) msg);
		} else if (msg instanceof WxRecvPicMsg) {
			onPic((WxRecvPicMsg) msg);
		} else if (msg instanceof WxRecvVoiceMsg) {
			onVoice((WxRecvVoiceMsg) msg);
		} else if (msg instanceof  WxRecvVideoMsg) {
			onVideo((WxRecvVideoMsg) msg);
		}
	}

	private String getPara(String key) {
		return req.getParameter(key);
	}

	private void renderText(String text) {
		try {
			PrintWriter pw = res.getWriter();
			pw.write(text);
			pw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
