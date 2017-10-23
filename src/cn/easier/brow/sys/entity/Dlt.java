package cn.easier.brow.sys.entity;

import java.sql.Timestamp;

public class Dlt {

	private Integer id;
	
	private String openid;
	
	/**
	 * 积分
	 */
	private Integer integral;
	
	private String username;
	
	private Timestamp utime;
	
	private String back1;
	
	private String back2;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Timestamp getUtime() {
		return utime;
	}

	public void setUtime(Timestamp utime) {
		this.utime = utime;
	}

	public String getBack1() {
		return back1;
	}

	public void setBack1(String back1) {
		this.back1 = back1;
	}

	public String getBack2() {
		return back2;
	}

	public void setBack2(String back2) {
		this.back2 = back2;
	}

	@Override
	public String toString() {
		return "Dlt [id=" + id + ", openid=" + openid + ", integral=" + integral + ", username=" + username + ", utime=" + utime
				+ ", back1=" + back1 + ", back2=" + back2 + "]";
	}
	
	
	
}
