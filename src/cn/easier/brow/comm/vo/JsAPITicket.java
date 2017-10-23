package cn.easier.brow.comm.vo;

/*{
"errcode":0,
"errmsg":"ok",
"ticket":"bxLdikRXVbTPdHSM05e5u5sUoXNKd8-41ZO3MhKoyN5OfkWITDGgnr2fwJ0m9E8NYzWKVZvdVtaUgWvsdshFKA",
"expires_in":7200
}*/
public class JsAPITicket {
	private String ticket = null;
	private long startTime;
	private long endTime;
	private long validTime;
	private String errcode;
	private String errmsg;
	private String expires_in;
	public JsAPITicket() {
		startTime = System.currentTimeMillis();
		validTime = 7200 * 1000;
		endTime = startTime + validTime;
	}

	public long getCurrentTime() {
		return System.currentTimeMillis();
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getValidTime() {
		return validTime;
	}

	public void setValidTime(long validTime) {
		this.validTime = validTime;
	}

	
	
	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(String expires_in) {
		this.expires_in = expires_in;
	}

	public boolean isValid() {
		return endTime> getCurrentTime() ? true : false;
	}

	@Override
	public String toString() {
		return "JsAPITicket [ticket=" + ticket + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", validTime=" + validTime
				+ ", currentTime=" + System.currentTimeMillis()+", errcode=" + errcode + ", errmsg=" + errmsg
				+ ", expires_in=" + expires_in + "]";
	}
	
	
}
