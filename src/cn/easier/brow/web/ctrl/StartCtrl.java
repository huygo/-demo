package cn.easier.brow.web.ctrl;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;

import cn.easier.brow.comm.util.CommonParams;
import cn.easier.brow.comm.util.JsonUtils;
import cn.easier.brow.comm.util.PropUtil;
import cn.easier.brow.comm.util.SessionUtil;
import cn.easier.brow.comm.weixin.dto.AccessToken;
import cn.easier.brow.comm.weixin.dto.UserInfoJO;
import cn.easier.brow.comm.weixin.util.WeiXinUtil;
import cn.easier.brow.web.base.BaseCtrl;
import cn.easier.brow.web.bean.ResponseMsg;


@Controller("startCtrl")
public class StartCtrl extends BaseCtrl {

	
	/**
	 * 微信授权登陆
	 * @Description (请用一句话描述)
	 * @param model
	 * @param req
	 * @param resp
	 * @return String
	 * @date 2017年7月24日上午9:54:37
	 * @author mengtx
	 */
	@RequestMapping(value = "/start/wxlogin", method = RequestMethod.GET)
	public String weixinLogin(ModelMap model, HttpServletRequest req, HttpServletResponse resp) {
		ResponseMsg re = new ResponseMsg();

		try {
			String appID = PropUtil.get("appid");
			String appsecret = PropUtil.get("appsecret");
			String code = req.getParameter("code");
			boolean isValidate = false;
			AccessToken tokenByOauth = WeiXinUtil.getTokenByOauth(code, appID, appsecret);
			String openid = null;
			log.info("tokenByOauth:" + tokenByOauth);
			if (tokenByOauth == null) {
				isValidate = true;
			} else {
				log.debug("tokenByOauth = " + tokenByOauth.toString());
				openid = tokenByOauth.getOpenid();
				log.info("这是openid：" + openid);
				if (null == openid) {
					isValidate = true;
				} else {
					log.debug("tokenByOauth = " + tokenByOauth.toString());
					openid = tokenByOauth.getOpenid();
					if (null == openid) {
						isValidate = true;
					}
				}
				// 验证不通过
				if (isValidate) {
					re.setResCode("10003"); // 参数验证错误编码10001
					re.setResMsg("验证失败");
					JsonUtils.outJsonAndClose(resp, JSON.toJSONString(re));
					return null;
				}

			}

			UserInfoJO userInfo = WeiXinUtil.getWeiXinUserInfo(openid, tokenByOauth.getAccess_token());
			if (userInfo == null) {
				re.setResCode("10004"); // 参数验证错误编码10001
				re.setResMsg("获取用户信息失败");
				JsonUtils.outJsonAndClose(resp, JSON.toJSONString(re));
				return null;
			}
			log.debug("userInfo = " + userInfo);
			SessionUtil.setObjectAttribute(req, CommonParams.SESSION_OPENID, openid);
			HttpSession session = req.getSession();
			session.setAttribute("userInfo", userInfo);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("message", "登录成功");
			map.put("userInfo", userInfo);
			re.setResponseMsg(map);
			resp.sendRedirect(PropUtil.get("portalurl")+ "/index.jsp");
		} catch (Exception e) {
			re.setProNo("");
			re.setReqTime("");
			re.setResCode("10001"); // 参数验证错误编码10001
			re.setResMsg(e.getMessage());
			log.error("", e);
		}
		return null;
	}



}
