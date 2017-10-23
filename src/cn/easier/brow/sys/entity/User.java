package cn.easier.brow.sys.entity;

import java.sql.Timestamp;

public class User {

	private String pkUserid;

    private String idxNick;

    private String pwd;

    private String headImg;

    private String pname;

    private Byte state;

    private Timestamp ctime;

    private Timestamp utime;

	public String getPkUserid() {
		return pkUserid;
	}

	public void setPkUserid(String pkUserid) {
		this.pkUserid = pkUserid;
	}

	public String getIdxNick() {
		return idxNick;
	}

	public void setIdxNick(String idxNick) {
		this.idxNick = idxNick;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	public Timestamp getCtime() {
		return ctime;
	}

	public void setCtime(Timestamp ctime) {
		this.ctime = ctime;
	}

	public Timestamp getUtime() {
		return utime;
	}

	public void setUtime(Timestamp utime) {
		this.utime = utime;
	}

	@Override
	public String toString() {
		return "User [pkUserid=" + pkUserid + ", idxNick=" + idxNick + ", pwd=" + pwd + ", headImg=" + headImg + ", pname="
				+ pname + ", state=" + state + ", ctime=" + ctime + ", utime=" + utime + "]";
	}

    
}
