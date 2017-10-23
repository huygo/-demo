package cn.easier.brow.comm.vo;

/**
 * 用于存储网页的accesstoken，自带过期失效重新获取；
 * 
 * @author xuzhaojie
 * 
 */
public class WebAccesstoken {

	private String token = null;
	private String refreshToken = null;
	private long startTime;
	private long endTime;
	private long validTime;
	private String openid;
	private String scope;

	public WebAccesstoken() {
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

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
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
		return startTime + validTime > getCurrentTime() ? true : false;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	@Override
	public String toString() {
		return "WebAccesstoken [token=" + token + ", refreshToken=" + refreshToken + ", startTime=" + startTime + ", endTime="
				+ endTime + ", validTime=" + validTime + ", openid=" + openid + ", scope=" + scope + "]";
	}

}
