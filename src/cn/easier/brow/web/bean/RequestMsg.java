package cn.easier.brow.web.bean;

import com.alibaba.fastjson.JSONObject;

public class RequestMsg {
	private String reqTime;
	private String proNo;
	private String token;
	private JSONObject reqBody;
	private JSONObject reqBase;

	public JSONObject getReqBase() {
		return reqBase;
	}

	public void setReqBase(JSONObject reqBase) {
		this.reqBase = reqBase;
	}

	public String getProNo() {
		return proNo;
	}

	public void setProNo(String proNo) {
		this.proNo = proNo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public RequestMsg() {
	}

	public String getReqTime() {
		return reqTime;
	}

	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}

	public JSONObject getReqBody() {
		return reqBody;
	}

	public void setReqBody(JSONObject reqBody) {
		this.reqBody = reqBody;
	}
}
