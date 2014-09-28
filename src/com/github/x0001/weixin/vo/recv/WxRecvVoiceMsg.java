package com.github.x0001.weixin.vo.recv;

/**
 * 微信语音消息
 * Created by 0x0001 on 14-9-28.
 */
public class WxRecvVoiceMsg extends WxRecvMsg {
	private String mediaId;
	private String format;
	private String recognition;
	public WxRecvVoiceMsg(WxRecvMsg msg, String mediaID, String format) {
		super(msg);
		this.mediaId = mediaID;
		this.format = format;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getRecognition() {
		return recognition;
	}

	public void setRecognition(String recognition) {
		this.recognition = recognition;
	}
}
