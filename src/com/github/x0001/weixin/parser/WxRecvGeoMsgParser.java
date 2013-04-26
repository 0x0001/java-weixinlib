package com.github.x0001.weixin.parser;

import org.jdom.Element;
import org.jdom.JDOMException;

import com.github.x0001.weixin.vo.recv.WxRecvGeoMsg;
import com.github.x0001.weixin.vo.recv.WxRecvMsg;

public class WxRecvGeoMsgParser extends WxRecvMsgBaseParser {

	@Override
	protected WxRecvGeoMsg parser(Element root, WxRecvMsg msg) throws JDOMException {
		String locationX = getElementText(root, "Location_X");
		String locationY = getElementText(root, "Location_Y");
		int scale = parseInt(getElementText(root, "Scale"),0);
		String label = getElementText(root, "Label");
		
		double latitude = parseDouble(locationX, 0.0);
		double longitude = parseDouble(locationY, 0.0);
		
		return new WxRecvGeoMsg(msg, latitude, longitude, scale, label);
	}
	
	private double parseDouble(String val,double def) {
		try {
			return Double.parseDouble(val);
		}catch(Exception e) {
			return def;
		}
	}
	
	private int parseInt(String val,int def) {
		try {
			return Integer.parseInt(val);
		}catch(Exception e) {
			return def;
		}
	}
	

}
