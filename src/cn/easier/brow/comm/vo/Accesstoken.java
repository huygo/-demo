package cn.easier.brow.comm.vo;

/**
 * 用于存储微信服务器获取的accesstoken，自带过期失效重新获取；
 * 
 * @author xuzhaojie
 * 
 */
public class Accesstoken {

	private String token = null;
	private long startTime;
	private long endTime;
	private long validTime;

	public Accesstoken() {
		startTime = System.currentTimeMillis();
		validTime = 7200 * 1000;
		endTime = startTime + validTime;
	}

	public long getCurrentTime() {
		return System.currentTimeMillis();
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public boolean isValid() {
		return endTime > getCurrentTime() ? true : false;
	}

	@Override
	public String toString() {
		return "Accesstoken [token=" + token + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", validTime=" + validTime
				+ ", currentTime=" + System.currentTimeMillis() + "]";
	}

}
