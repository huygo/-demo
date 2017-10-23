package cn.easier.brow.comm.util;

public interface CommonParams {
	static String PARAM_FILES = "files";
	static String PARAM_HTML_STR = "html_str";
	static String SESSION_USER_NICK = "session_user_nick";
	static String REQ_MSG = "requestMsg";
	static String REQ_MSG_STR = "requestMsgStr";
	static String MSG_PRO_NO = "proNo";
	static String EXCEPTION_KEY = "exception";
	static String SESSION_USER_HEADURL = "session_user_headurl";
	static String EXCEPT_CHARS = ",\\|";
	static String REQ_BODY_CHARSET = "UTF-8";
	static String SESSION_USER = "ssoUserInfo";
	static String MINA_CLIENT = "simpleMinaClient";
	static String IMG_SERVER_DIR = "img.server.dir";
	static String IMG_WEB_DIR = "img.web.dir";
	static String PATH_TEMPFILE_DIR = "path.tempfile.dir";
	static String MSG_PREFIX = "msg_";
	static String FILE_TYPE_IMG = "img";
	static String FILE_TYPE_HTML = "html";
	static String FILE_TYPE_VEDIO = "vedio";
	static String FILE_REF_CS = "couse_face_url";
	static String FILE_REF_EX_ITEM_PIC = "ex_item_pic";
	static String FILE_REF_CON = "couse_content";
	static String FILE_REF_MSG = "msg_push_content";
	static String FILE_REF_EXWORDS = "ex_words";
	static String FILE_REF_WORDS = "words";
	static String FILE_REF_REPO_CON = "repo_content";
	static String FILE_REF_WORKS = "user_works";
	static String FILE_REF_HEAD_IMG = "head_img";
	// log
	static String LOG_SYS = "sys_log";
	static String LOG_MSG = "sys_log";

	// WebActionImpl service key
	static String SERVICE_APP_GETWAY = "getWayService";

	// 系统初始化filter bean 获取
	static String SYS_FILTER_CHECK_VERSION = "versionCheckFilterImpl";
	static String SYS_FILTER_CHECK_PROTOCOL = "protocolCheckFilterImpl";
	static String SYS_FILTER_CHECK_OPER = "operationCheckFilterImpl";
	static String SYS_FILTER_OS = "invokeOSFilterImpl";
	static String SYS_FILTER_VALI = "paramValiFilterImpl";

	// cookie 设置
	static String COOKIE_DOMAIN = "";
	static String COOKIE_LOGIN_NAME = "loginName";
	static String COOKIE_USER_TOKEN = "userToken";
	static String COOKIE_USER_TYPE = "userType";
	static int COOKIE_TIME_OUT = 1;

	// form
	static String FORM_JSON = "jsonData";
	static String FORM_MULTI = "multipart/form-data";
	static String FORM_PHOTO = "photo";
	static String FORM_NORMAL = "application/x-www-form-urlencoded";
	static String FORM_HTML_STR = "html_str";

	// 常用config
	static String CFG_COOKIE_TIME_OUT = "cookie.time.out";

	// token
	static String TOKEN_USER_ID = "token_user_id";
	static String TOKEN_TIMESTAMP = "token_timestamp";
	static String TOKEN_AUTH_STR = "token_auth_str";
	static String TOKEN_TIME_OUT = "token.time.out";

	static String SESSION_USER_ID = "session_user_id";
	static String SESSION_ACT_ID = "session_act_id";
	static String SESSION_LOGIN_TYPES = "session_login_types";
	static String SESSION_OPENID = "session_openid";
	/**
	* cokie name
	*/
	public final static String COKIE_NAME = "easier_town_user_token";
	/**
	 * 图片验证码session KEY
	 */
	public static final String RANDOMCODEKEY = "RANDOMVALIDATECODEKEY";
}
