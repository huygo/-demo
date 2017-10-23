package cn.easier.brow.web.bean;

import java.util.regex.Pattern;

public class ParamValiInfo {
	private String regKey;
	private Pattern pattern;
	private int strMin;
	private int strMax;
	private boolean notMust;

	public boolean isNotMust() {
		return notMust;
	}

	public void setNotMust(boolean notMust) {
		this.notMust = notMust;
	}

	public ParamValiInfo(String regKey, Pattern pattern, int strMin, int strMax) {
		this.regKey = regKey;
		this.pattern = pattern;
		this.strMin = strMin;
		this.strMax = strMax;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public int getstrMin() {
		return strMin;
	}

	public void setstrMin(int strMin) {
		this.strMin = strMin;
	}

	public int getstrMax() {
		return strMax;
	}

	public void setstrMax(int strMax) {
		this.strMax = strMax;
	}

	public String getRegKey() {
		return regKey;
	}

	public void setRegKey(String regKey) {
		this.regKey = regKey;
	}

}
