package cn.easier.brow.web.bean;

import org.apache.commons.lang3.StringUtils;

import cn.easier.brow.comm.vo.ResultCode;

public class ResponseMsg {
	private Object repBody;
	private String resCode;
	private String resMsg;
	private String reqTime;
	private String proNo;
	private PageInfo resPage;

	public ResponseMsg() {
		this.repBody = new Object();
		this.resCode = ResultCode.REQUESTSUCCESS;
		this.resMsg = "success";
	}

	public ResponseMsg(Object rep) {
		if ("".equals(rep) || null == rep) {
			rep = new Object();
		}
		this.repBody = rep;
		this.resCode = ResultCode.REQUESTSUCCESS;
		this.resMsg = "success";
	}

	public ResponseMsg(Object rep, String resCode, String resMsg) {
		if ("".equals(rep) || null == rep) {
			rep = new Object();
		}
		this.repBody = rep;
		if (StringUtils.isEmpty(resCode)) {
			this.resCode = ResultCode.REQUESTSUCCESS;
		} else {
			this.resCode = resCode;
		}
		if (StringUtils.isEmpty(resMsg)) {
			this.resMsg = "success";
		} else {
			this.resMsg = resMsg;
		}
	}

	public void setResponseMsg(Object rep) {
		if ("".equals(rep) || null == rep) {
			rep = new Object();
		}
		this.repBody = rep;
		this.resCode = ResultCode.REQUESTSUCCESS;
		this.resMsg = "success";
	}

	public void setResponseMsg(Object rep, String resCode, String resMsg) {
		if ("".equals(rep) || null == rep) {
			rep = new Object();
		}
		this.repBody = rep;
		if (StringUtils.isEmpty(resCode)) {
			this.resCode = ResultCode.REQUESTSUCCESS;
		} else {
			this.resCode = resCode;
		}
		if (StringUtils.isEmpty(resMsg)) {
			this.resMsg = "success";
		} else {
			this.resMsg = resMsg;
		}
	}

	public String getReqTime() {
		return reqTime;
	}

	public void setReqTime(String reqTime) {
		this.reqTime = reqTime;
	}

	public String getProNo() {
		return proNo;
	}

	public void setProNo(String proNo) {
		this.proNo = proNo;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getResMsg() {
		return resMsg;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

	public Object getRepBody() {
		return repBody;
	}

	public void setRepBody(Object repBody) {
		if (repBody != null) {
			this.repBody = repBody;
		}
	}

	public PageInfo getResPage() {
		return resPage;
	}

	public void setResPage(PageInfo resPage) {
		this.resPage = resPage;
	}

}
