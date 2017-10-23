package cn.easier.brow.comm.weixin.dto;

/**
 * jssdk签名
 * @author l
 *
 */
public class JsSign {
	private String jsticket;
	private String noncestr;
	private String timestamp;
	private String signature;
	private String appid;

	public JsSign() {
		super();
	}

	public JsSign(String jsticket, String noncestr, String timestamp, String signature) {
		super();
		this.jsticket = jsticket;
		this.noncestr = noncestr;
		this.timestamp = timestamp;
		this.signature = signature;
	}

	public String getJsticket() {
		return jsticket;
	}

	public void setJsticket(String jsticket) {
		this.jsticket = jsticket;
	}

	public String getNoncestr() {
		return noncestr;
	}

	public void setNoncestr(String noncestr) {
		this.noncestr = noncestr;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

}
