package cn.easier.brow.web.ctrl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;

import cn.easier.brow.comm.util.ConverterCharacterSetUtil;
import cn.easier.brow.comm.util.JsonUtils;
import cn.easier.brow.comm.util.ParamParseUtil;
import cn.easier.brow.comm.vo.ResultCode;
import cn.easier.brow.comm.weixin.dto.UserInfoJO;
import cn.easier.brow.sys.entity.User;
import cn.easier.brow.sys.service.inte.UserServece;
import cn.easier.brow.web.base.BaseCtrl;
import cn.easier.brow.web.bean.RequestMsg;
import cn.easier.brow.web.bean.ResponseMsg;


@Controller("userCtrl")
public class UserCtrl extends BaseCtrl{

	@Autowired
	private UserServece userService;
	
	@RequestMapping(value = "/user/insert")
	public String insert(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		ResponseMsg re = new ResponseMsg();
		RequestMsg reqMsg = null;
		
		try {
			String reqMsgStr = ConverterCharacterSetUtil.converterToUtf8(request);
			reqMsg = ParamParseUtil.parseReqMsg(reqMsgStr);
			log.info("请求json数据:" + reqMsgStr);
			HttpSession session = request.getSession();
			UserInfoJO userinfo = (UserInfoJO) session.getAttribute("userInfo");
			if(userinfo==null){
				re.setRepBody("获取信息失败");
				re.setResCode(ResultCode.UNKNOWNERROR);			
			}else{
				User user = new User();
				user.setIdxNick(userinfo.getNickname());
				user.setPkUserid(userinfo.getOpenid());
				user.setHeadImg(userinfo.getHeadimgurl());
				userService.insert(user);
				re.setRepBody(user);
			}
			
			JsonUtils.outJsonAndClose(response, JSON.toJSONString(re));
		}  catch (Exception e) {
			if (reqMsg != null) {
				re.setProNo(reqMsg.getProNo());
				re.setReqTime(reqMsg.getReqTime());
			}
			re.setResCode(ResultCode.UNKNOWNERROR);
			re.setResMsg(e.getMessage());
			JsonUtils.outJsonAndClose(response, JSON.toJSONString(re));
			log.error("", e);
		}
		
		return null;
	}
	
	@RequestMapping(value = "/user/price", method = RequestMethod.POST)
	public String price(ModelMap model, HttpServletRequest req, HttpServletResponse resp) {

		ResponseMsg re = new ResponseMsg();
		try {
			String reqMsgStr = ConverterCharacterSetUtil.converterToUtf8(req);
			log.info("请求json数据:" + reqMsgStr);
			int a = userService.casePrice();
			re.setResponseMsg(a);
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
