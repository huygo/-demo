package cn.easier.brow.comm.weixin.dto;

public class UserInfoJO {
	private String nickname;
	private String openid;
	private String sex;
	private String province;
	private String city;
	private String country;
	private String headimgurl;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	@Override
	public String toString() {
		return "UserInfoJO [nickname=" + nickname + ", openid=" + openid + ", sex=" + sex + ", province=" + province + ", city="
				+ city + ", country=" + country + ", headimgurl=" + headimgurl + "]";
	}

}
