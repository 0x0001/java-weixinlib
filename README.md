java-weixinlib
==============

微信公众平台接入

使用了jdom作为xml解析输出框架
===============

使用说明:
	// 接入时
	final String TOKEN = "xxx";
	String signature = getRequest.getParameter("signature");
	String timestamp = getRequest.getParameter("timestamp");
	String nonce = getRequest.getParameter("nonce");
	String echostr = getRequest.getParameter("echostr");
		
	if(null != timestamp && null != nonce && null != echostr && null != signature) {
		if(WeiXin.access(TOKEN, signature, timestamp, nonce)) {
			getResponse().getWriter().write(echostr);
			return;
		}
		return;
	}
> 

=======================

	// 接收消息
	WxRecvMsg msg = WeiXin.recv(getRequest().getInputStream());
	WxSendMsg sendMsg = WeiXin.builderSendByRecv(msg);
	System.out.println(msg);
	// 微信事件消息, 关注/取消关注/菜单...
	if(msg instanceof WxRecvEventMsg) {
		WxRecvEventMsg m = (WxRecvEventMsg) msg;
		String event = m.getEvent();
		// 有人关注微信帐号
		if("subscribe".equals(event)) {
			String content = "欢迎关注xxx";
			// 构建文本消息进行发送
			sendMsg = new WxSendTextMsg(sendMsg, content);
			// 发送回微信
			WeiXin.send(sendMsg, getResponse().getOutputStream());
		}
		return;
	}
	// 文本消息..,目前支持的消息有(WxRecvEventMsg/事件消息,WxRecvGeoMsg/地理位置消息,WxRecvLinkMsg/连接消息,WxRecvPicMsg/图片消息)
	if(msg instanceof WxRecvTextMsg)
	
	
	// 发送消息

	
	
	



