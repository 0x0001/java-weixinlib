package com.github.x0001.weixin.vo.recv;

/**
 * 视频消息
 * Created by 0x0001 on 14-9-28.
 */
public class WxRecvVideoMsg extends WxRecvMsg {
	private String mediaId;
	private String thumbMediaId;
	public WxRecvVideoMsg(WxRecvMsg msg, String mediaId, String thumbMediaId) {
		super(msg);
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}
}
