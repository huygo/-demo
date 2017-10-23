package cn.easier.brow.web.bean;

import java.util.HashMap;
import java.util.Map;

public class Result {

	private static Map<Integer, String> map = new HashMap<Integer, String>();
	public static int SUCCESS = 0;
	public static int FAIL = 1;
	public static int PARAMERROR = 2;
	public static int FILEERROR = 3;
	public static int SYSTEMERROR = 4;
	public static int DBERROR = 5;
	public static int FILENOTFIT = 6;
	public static int USERNOTLOGIN = 7;
	public static int DATANOTFOUND = 8;
	public static int JSONERROR = 9;
	public static int ORDERHASCLOSE = 10;
	public static int ORDERPAYURLERROR = 11;
	public static int SHAREMOREORDERPAY = 12;
	public static int NOTHASPAYGROUPORDER = 13;
	public static int USERNOPERMISSION = 14;
	public static int HASGIVEGOOD = 15;
	public static int CODEERROR = 16;
	public static int PHONENOTFIT = 17;
	public static int NOTGROUPMEMBER = 18;
	public static int HASCREATEITEM = 19;
	public static int HASCREATEVOTE = 20;
	public static int GROUPISFULL = 21;
	public static int TIMEOUT = 22;
	public static int USERHASBALLOT = 23;

	static {
		map.put(SUCCESS, "请求成功");
		map.put(FAIL, "请求失败");
		map.put(PARAMERROR, "请求参数错误或者缺少必填参数");
		map.put(FILEERROR, "文件保存错误");
		map.put(DBERROR, "操作数据库错误");
		map.put(SYSTEMERROR, "系统出错");
		map.put(FILENOTFIT, "文件格式不符合");
		map.put(USERNOTLOGIN, "没有你的登录信息,请先登录");
		map.put(DATANOTFOUND, "没有找到相关数据");
		map.put(JSONERROR, "解释JSON数据出错");
		map.put(ORDERHASCLOSE, "订单交易超时,请重新下单");
		map.put(ORDERPAYURLERROR, "统一支付订单的URL生成失败");
		map.put(SHAREMOREORDERPAY, "晒单次数多于成功支付订单数");
		map.put(NOTHASPAYGROUPORDER, "没有成功购买旅游团的订单,不能晒单");
		map.put(USERNOPERMISSION, "用户没有权限");
		map.put(HASGIVEGOOD, "你已点赞");
		map.put(CODEERROR, "验证码错误");
		map.put(PHONENOTFIT, "不是正确的手机号码");
		map.put(NOTGROUPMEMBER, "你不是该团的成员");
		map.put(HASCREATEITEM, "你已经创建过参选投票了");
		map.put(USERHASBALLOT, "你已经投票过了");
		map.put(HASCREATEVOTE, "你已经发起过投票活动了");
		map.put(GROUPISFULL, "报名人数已经超过最大组团人数");
		map.put(TIMEOUT, "已经过了截止报名时间了");
	}

	private int code;
	private String description;
	private Object data;

	public Result(int code) {
		this.code = code;
		this.description = map.get(code);
	}

	public Result() {
		this.code = FAIL;
		this.description = map.get(this.code);
	}

	public Result(int code, Object data) {
		this.code = code;
		this.description = map.get(code);
		this.data = data;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
		this.description = map.get(code);
	}

	public String getDescription() {
		return description;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
