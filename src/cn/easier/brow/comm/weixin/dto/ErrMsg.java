package cn.easier.brow.comm.weixin.dto;

public class ErrMsg {
	private int errcode = 0;
	private String errmsg = "success";
	public int getErrcode() {
		return errcode;
	}
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	
}
