package com.github.x0001.weixin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.x0001.weixin.vo.recv.*;

/**
 * Created by 0x0001 on 14-9-28.
 */
public abstract class WeixinHandlerAdapter extends WeixinAbstractHandler {

	public static final String EVENT_SUBSCRIBE = "subscribe";
	public static final String EVENT_UN_SUBSCRIBE = "unsubscribe";

	public WeixinHandlerAdapter(HttpServletRequest req, HttpServletResponse res, String token) {
		super(req, res, token);
	}

	@Override
	protected void onPic(WxRecvPicMsg msg) {

	}

	@Override
	protected void onLink(WxRecvLinkMsg msg) {

	}

	@Override
	protected void onGeo(WxRecvGeoMsg msg) {

	}

	@Override
	protected void onText(WxRecvTextMsg msg) {

	}

	@Override
	protected final void onEvent(WxRecvEventMsg msg) {
		String event = msg.getEvent();
		if(EVENT_SUBSCRIBE.equalsIgnoreCase(event)) {
			onSubscribe(msg);
		} else if(EVENT_UN_SUBSCRIBE.equalsIgnoreCase(event)) {
			onUnsubscribe(msg);
		} else {
			onOtherEvent(msg);
		}
	}

	@Override
	protected void onNotFromWeixin() {

	}

	/**
	 * 订阅
	 * @param msg
	 */
	protected void onSubscribe(WxRecvEventMsg msg){

	}

	/**
	 * 取消订阅
	 * @param msg
	 */
	protected void onUnsubscribe(WxRecvEventMsg msg){

	}

	/**
	 * 其它事件 (点击了自定义菜单)
	 * @param msg
	 */
	protected void onOtherEvent(WxRecvEventMsg msg){

	}

	@Override
	protected void onVideo(WxRecvVideoMsg msg) {

	}

	@Override
	protected void onVoice(WxRecvVoiceMsg msg) {

	}
}
