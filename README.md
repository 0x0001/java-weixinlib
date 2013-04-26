java-weixinlib
==============

微信公众平台接入
===============

使用了jdom作为xml解析输出框架

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
	
	
	// 发送消息构建
	// 通过 WxSendMsg sendMsg = WeiXin.builderSendByRecv(msg); 将收到的消息转为发送消息(交换了sendUser和fromUser)
	// 文本消息
	new WxSendTextMsg(sendMsg, content);
	
	// 多图消息
	WxSendNewsMsg newsMsg = new WxSendNewsMsg(sendMsg)
					.addItem("标题", "描述", "图片地址", "点击后跳转的链接")
					.addItem....
					最多可以添加10个
	// 音乐消息
	new WxSendMusicMsg(sendMsg, "标题","描述","低品质音乐地址", "高品质音乐地址 (wifi环境会使用这个地址进行播放)");

	// 发送这些消息的时候可以直接使用`WeiXin.send(sendMsg, getResponse().getOutputStream());`会将msg转换成xml输出
	
	
	



