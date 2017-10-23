package cn.easier.brow.comm.vo;

public class ResultCode {
	/**
	 * 请求成功 
	 * REQUESTSUCCESS = "000000"
	 */
	public static String REQUESTSUCCESS = "000000";

	/**
	 * 用户名或者密码错误
	 * ACCORPASERROR = "100001"
	 */
	public static String ACCORPASERROR = "100001";

	/**
	 * 用户名或者密码为空
	 */
	public static String ACCORPASENULL = "100005";
	/**
	 * 用户未登陆
	 * NLERROR = "100002"
	 */
	public static String NLERROR = "100002";
	/**
	 * 账号已停用
	 * ACCORHALT = "100003"
	 */
	public static String ACCORHALT = "100003";
	/**
	 * 注册用户已存在
	 * USER_EXIST = "100004
	 */
	public static String USER_EXIST = "100004";
	/**
	 * Token 验证错误
	 * TOKEN_ERROR = "100100"
	 */
	public static String TOKEN_ERROR = "100100";
	/**
	 * 未知错误
	 * UNKNOWNERROR = "100200"
	 */
	public static String UNKNOWNERROR = "100200";
	/**
	 * 请求参数错误
	 * PARAMERROR = "200001"
	 */
	public static String PARAMERROR = "200001";
	/**
	 * 上传失败
	 * UPLOAD_FALSE = "200002"
	 */
	public static String UPLOAD_FALSE = "200002";
	/**
	 * 超时
	 * TIMEOUT = "200100"
	 */
	public static String TIMEOUT = "200100";
	/**
	 * 验证码错误
	 * VALIDATECODEERROR = "200201"
	 */
	public static String VALIDATECODEERROR = "200201";
	/**
	 * 短信验证码下发失败
	 * MSG_SEND_FAIL = "200202"
	 */
	public static String MSG_SEND_FAIL = "200202";
	/**
	 * 短信验证码验证错误
	 * MSG_CHECK_FAIL = "200203"
	 */
	public static String MSG_CHECK_FAIL = "200203";
	/**
	 * 服務不可用 
	 * SERVER_ERROR = "200300"
	 */
	public static String SERVER_ERROR = "200300";
	/**
	 * 404错误
	 * SERVER_ERROR = "200300"
	 */
	public static String ERROR_404 = "404";

}
