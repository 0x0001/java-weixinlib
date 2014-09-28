java-weixinlib
==============

微信公众平台接入
===============

使用了jdom作为xml解析输出框架

使用说明:

```

public class WeixinHandler extends WeixinHandlerAdapter { //  WeixinAbstractHandler 继承这个类可以需要实现所有方法
	private static final String  TOKEN = "token";

	public WeixinHandler(HttpServletRequest req, HttpServletResponse res) {
		super(req, res, TOKEN);
	}
	/**
	 * 收到图片消息
	 *
	 * @param msg
	 */
	@Override
	protected void onPic(WxRecvPicMsg msg) {
		sendText("收到图片消息");
	}

	/**
	 * 收到链接消息
	 *
	 * @param msg
	 */
	@Override
	protected void onLink(WxRecvLinkMsg msg) {
		sendText("收到链接消息");
	}

	/**
	 * 收到地址消息
	 *
	 * @param msg
	 */
	@Override
	protected void onGeo(WxRecvGeoMsg msg) {
		sendText("收到地址消息");
	}

	/**
	 * 收到文本消息
	 *
	 * @param msg
	 */
	@Override
	protected void onText(WxRecvTextMsg msg) {
		sendText("收到文本消息" + msg.getContent() + " FROM :" + msg.getFromUser());
	}

	@Override
	protected void onVoice(WxRecvVoiceMsg wxRecvVoiceMsg) {
		sendText("收到语音消息");
	}

	@Override
	protected void onVideo(WxRecvVideoMsg wxRecvVideoMsg) {
		sendText("收到视频消息");
	}


	/**
  	 * 订阅
  	 */
  	@Override
  	protected void onSubscribe(WxRecvEventMsg msg) {
  		sendText("感谢您关注");
  	}
  
  	/**
  	 * 取消订阅
  	 */
  	@Override
  	protected void onUnsubscribe(WxRecvEventMsg msg) {
  		System.out.println("取消关注!!!");
  	}
  
  
  	/**
  	 * 其它事件 (点击了菜单)
  	 *
  	 * @param msg
  	 */
  	@Override
  	protected void onOtherEvent(WxRecvEventMsg msg) {
  		System.out.println("其它事件: " + msg.getEventKey());
  	}
}


// 响应文本消息
sendText(content);

// 响应多图消息
  wxSendNewsMsg msg =buildNewsMsg();
  msg.addItem(title,description,picUrl,url)
  .addItem(title,description,picUrl,url); // 最多添加10个
	sendNewsMsg(WxSendNewsMsg wxSendNewsMsg);
	
// 响应音乐消息
sendMusicMsg(title, desc, music,  hqMusic);

// 检查是否来自微信的请求 (验证签名和请求方式)
	protected boolean isFromWeiXin();
```



