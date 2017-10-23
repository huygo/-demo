package cn.easier.brow.comm.weixin.dto;

public class JsTicket extends ErrMsg {
	private String ticket;
	private int expires_in;
	private long beg_time;
	private long end_time;

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public int getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	public long getBeg_time() {
		return beg_time;
	}

	public void setBeg_time(long beg_time) {
		this.beg_time = beg_time;
	}

	public long getEnd_time() {
		return end_time;
	}

	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}

	@Override
	public String toString() {
		return "JsTicket [ticket=" + ticket + ", expires_in=" + expires_in + ", beg_time=" + beg_time + ", end_time=" + end_time
				+ "]";
	}

}
