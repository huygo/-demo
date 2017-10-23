package cn.easier.brow.web.ctrl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.easier.brow.comm.util.ConverterCharacterSetUtil;
import cn.easier.brow.comm.util.JsonUtils;
import cn.easier.brow.comm.util.ParamParseUtil;
import cn.easier.brow.comm.vo.ResultCode;
import cn.easier.brow.sys.entity.Dlt;
import cn.easier.brow.sys.service.inte.DltService;
import cn.easier.brow.web.base.BaseCtrl;
import cn.easier.brow.web.bean.RequestMsg;
import cn.easier.brow.web.bean.ResponseMsg;

@Controller("dltCtrl")
public class DltCtrl extends BaseCtrl {

	@Autowired
	private DltService dltService;
	
	@RequestMapping(value = "/dlt/select")
	public String select(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		ResponseMsg re = new ResponseMsg();
		RequestMsg reqMsg = null;
		
		try {
			String reqMsgStr = ConverterCharacterSetUtil.converterToUtf8(request);
			
			reqMsg = ParamParseUtil.parseReqMsg(reqMsgStr);
			log.info("请求json数据:" + reqMsgStr);
			JSONObject jsbody = reqMsg.getReqBody();
			String openid= jsbody.getString("openid");
			log.info("body中的某字段:" + openid);
			Dlt dlt = dltService.selectByKey(openid);
			re.setRepBody(dlt);
			JsonUtils.outJsonAndClose(response, JSON.toJSONString(re));
		} catch (Exception e) {
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
	
	@RequestMapping(value = "/dlt/update")
	public String update(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		
		ResponseMsg re = new ResponseMsg();
		RequestMsg reqMsg = null;
		
		try {
			String reqMsgStr = ConverterCharacterSetUtil.converterToUtf8(request);
			
			reqMsg = ParamParseUtil.parseReqMsg(reqMsgStr);
			log.info("请求json数据:" + reqMsgStr);
			JSONObject jsbody = reqMsg.getReqBody();
			String openid= jsbody.getString("openid");
			String reason = jsbody.getString("type");
			Integer data =jsbody.getInteger("data");
			int result = dltService.updateDlt(openid, reason,data);
			if(result>0){
				re.setResCode(ResultCode.REQUESTSUCCESS);
				String repBody = "更新成功";
				re.setRepBody(repBody);				
			}else{
				re.setResCode("0000002");
				String repBody = "更新失败";
				re.setRepBody(repBody);	
			}
			JsonUtils.outJsonAndClose(response, JSON.toJSONString(re));
			
		} catch (Exception e) {
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
	
	
}
