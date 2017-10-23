package cn.easier.brow.web.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.easier.brow.comm.util.CommonParams;
import cn.easier.brow.comm.util.ConverterCharacterSetUtil;
import cn.easier.brow.comm.util.JsonUtils;
import cn.easier.brow.comm.util.ParamParseUtil;
import cn.easier.brow.comm.util.ParamValiUtil;
import cn.easier.brow.comm.util.SessionUtil;
import cn.easier.brow.comm.vo.ResultCode;
import cn.easier.brow.sys.entity.Demo;
import cn.easier.brow.sys.service.inte.DemoService;
import cn.easier.brow.web.base.BaseCtrl;
import cn.easier.brow.web.bean.RequestMsg;
import cn.easier.brow.web.bean.ResponseMsg;

@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
@Controller("demoCtrl")
public class DemoCtrl extends BaseCtrl {
	@Autowired
	protected DemoService demoService;

	/***
	 * 用户登录
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/demo/login")
	public String login(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> map = new HashMap<String, String>();
		try {
			log.info("请求用户数据:");
			//Integer userid = (Integer) SessionUtil.getObjectAttribute(request, CommonParams.TOKEN_USER_ID);
			Demo demo = demoService.insertSelective();
			Integer usid = demo.getId();
			map.put(CommonParams.TOKEN_USER_ID, "123456");
			map.put(CommonParams.SESSION_USER, "ZhangSan");
			map.put(CommonParams.TOKEN_TIMESTAMP, System.currentTimeMillis() + "");
			// 传值到页面
			SessionUtil.setObjectAttribute(request, CommonParams.SESSION_USER, usid);
			model.addAttribute("demo", demo);
			return "demo/info";
		} catch (Exception e) {
			log.error("login() 失败", e);
			return ResultCode.ERROR_404;
		}
	}

	@RequestMapping(value = "/demo/{id}")
	public String h2001(ModelMap model, HttpServletRequest req, HttpServletResponse resp) {
		ResponseMsg re = new ResponseMsg();
		RequestMsg reqMsg = null;
		try {
			String reqMsgStr = ConverterCharacterSetUtil.converterToUtf8(req);
			log.info("请求json数据:" + reqMsgStr);
			Map params = new HashMap<String, String>();
			params.put("title", "测试哦");
			params.put("status", "ok");
			// 写json到客户端
			re.setResponseMsg(params);
			log.info(JSON.toJSONString(re));
			JsonUtils.outJsonAndClose(resp, JSON.toJSONString(re));
		} catch (Exception e) {
			if (reqMsg != null) {
				re.setProNo(reqMsg.getProNo());
				re.setReqTime(reqMsg.getReqTime());
			}
			re.setResCode(ResultCode.UNKNOWNERROR);
			re.setResMsg(e.getMessage());
			JsonUtils.outJsonAndClose(resp, JSON.toJSONString(re));
			log.error("", e);
		}
		return null;
	}

	@RequestMapping(value = "/demo/userinfo", method = RequestMethod.POST)
	public String userInfo(ModelMap model, HttpServletRequest req, HttpServletResponse resp) {

		ResponseMsg re = new ResponseMsg();
		try {
			String reqMsgStr = ConverterCharacterSetUtil.converterToUtf8(req);
			log.info("请求json数据:" + reqMsgStr);

			// 构造请求消息对象
			RequestMsg reqMsg = ParamParseUtil.parseReqMsg(reqMsgStr);

			// 请求数据有效性校验...
			Map vali = ParamValiUtil.valiReqBody(reqMsg.getProNo(), reqMsg.getReqBody());
			log.info("vali:" + vali);
			if ("1".equals(vali.get(ParamValiUtil.EX_CODE))) {
				re.setProNo(reqMsg.getProNo());
				re.setResCode(ResultCode.PARAMERROR); // 参数验证错误
				re.setResMsg(vali.get(ParamValiUtil.EX_MSG).toString());
				JsonUtils.outJsonAndClose(resp, JSON.toJSONString(re));
				return null;
			}
			// 请求体
			JSONObject jsbody = reqMsg.getReqBody();
			String login_name = jsbody.getString("login_name");
			log.info("body中的某字段:" + login_name);
			// 业务逻辑处理......
			// 获取返回数据
			Map<String, String> user = new HashMap<String, String>();
			user.put(CommonParams.TOKEN_USER_ID, "123456");
			user.put(CommonParams.SESSION_USER, login_name);

			List list = new ArrayList();
			list.add("1");
			list.add("2");
			list.add("3");
			Map map = new HashMap();
			map.put("user", user);
			map.put("list", list);

			re.setResponseMsg(map);
			// 写json到客户端
			JsonUtils.outJsonAndClose(resp, JSON.toJSONString(re));
		} catch (Exception e) {
			re.setProNo("");
			re.setResCode(ResultCode.UNKNOWNERROR);
			re.setResMsg(e.getMessage());
			JsonUtils.outJsonAndClose(resp, JSON.toJSONString(re));
			log.error("", e);
		}
		return null;

	}

}
