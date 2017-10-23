package cn.easier.brow.web.bean;

import java.io.Serializable;

public class Replies implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 198658153643166148L;

	private String nick;
	private String ctime;
	private String replies;
	private String head_img;
	private String rn;

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getReplies() {
		return replies;
	}

	public void setReplies(String replies) {
		this.replies = replies;
	}

	public String getHead_img() {
		return head_img;
	}

	public void setHead_img(String head_img) {
		this.head_img = head_img;
	}

	public String getRn() {
		return rn;
	}

	public void setRn(String rn) {
		this.rn = rn;
	}

	@Override
	public String toString() {
		return "Replies [nick=" + nick + ", ctime=" + ctime + ", replies=" + replies + ", head_img=" + head_img + ", rn=" + rn
				+ "]";
	}

}
