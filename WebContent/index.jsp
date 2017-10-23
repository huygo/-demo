<!DOCTYPE html>
	<html>
		<head>
			<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
			<%
			 String path = request.getContextPath();
			 
			 System.out.println("URL:" + request.getParameter("type"));
			 String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/"; 
			 String basePath_2 = request.getScheme() + "://" + request.getServerName() + path + "/";
			%>
			<%@page import="java.net.URLEncoder"%>
			<%@page import="com.alibaba.fastjson.JSONObject"%>
			<%@page import="org.apache.commons.lang3.StringUtils"%>
			<%@page import="cn.easier.brow.comm.util.CommonParams"%>
			<%@page import="cn.easier.brow.comm.util.ParamValiUtil"%>
			<%@page import="cn.easier.brow.comm.util.PropUtil"%>
			<%
				String head = request.getHeader("user-agent");
				boolean iswx = head.contains("MicroMessenger");
				System.out.print("head:" + head);
				String openid = "";
				String tiptext = "";	
				if(iswx){
					openid = (String)session.getAttribute(CommonParams.SESSION_OPENID);//微信端打开需要获取openid
					System.out.println("首页获取的openid=" + openid);
					if (StringUtils.isBlank(openid)) {
						String appid =  PropUtil.get("appid");
						String portalurl = PropUtil.get("portalurl");
						System.out.print("portalurl:" + portalurl+"\n");
						System.out.print("openid:" + openid+"\n");
						String OAUTH_URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid
								+ "&redirect_uri=" + portalurl + "/start/wxlogin?oid="+null
								+ "&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect";
						System.out.print("微信访问首页跳转地址:" + OAUTH_URL);
						response.sendRedirect(OAUTH_URL);		
					};
				}else{
				    tiptext = "请在微信中打开！";
				}
			%>
			<meta charset=utf-8>
			<meta http-equiv=X-UA-Compatible content="IE=edge, chrome=1">
			<!-- uc强制竖屏 -->
			<meta name="screen-orientation" content="portrait">
			<!-- QQ强制竖屏 -->
			<meta name="x5-orientation" content="portrait">
			<!-- UC应用模式 -->
			<meta name="browsermode" content="application">
			<!-- QQ应用模式 -->
			<meta name="x5-page-mode" content="app">
			<meta name=viewport content="width=device-width,user-scalable=no,initial-scale=1,maximum-scale=1,minimum-scale=1">
			<title>积分乐乐透</title>
			<script src=http://res.wx.qq.com/open/js/jweixin-1.0.0.js></script>
			<script>
					var path = "<%=path%>";
					var basePath = "<%=basePath%>";
					var basePath_2 = "<%=basePath_2%>";
					var isWx = <%=iswx%>;
			</script>
			<script>
					var ua = navigator.userAgent;
					var phoneWidth = parseInt(window.screen.width);
					var phoneScale = phoneWidth / 640;
					//alert(ua);
					if(ua.match(/WX/i)){//判断是否为手机QQ
						var WX = true;
					};
					if(!isWx){
						// 这里警告框会阻塞当前页面继续加载
				        alert('请使用微信浏览器访问本页面！');
				        // 以下代码是用javascript强行关闭当前页面
				        var opened = window.open('about:blank', '_self');
				        opened.opener = null;
				        opened.close();
					};
					if (/Android (\d+\.\d+)/.test(ua)) {
						var version = parseFloat(RegExp.$1);
						if (version > 2.3) {
							document.write('<meta name="viewport" content="width=640, minimum-scale = ' + phoneScale + ', maximum-scale = ' + phoneScale + ', target-densitydpi=device-dpi">');
						} else {
							document.write('<meta name="viewport" content="width=640, target-densitydpi=device-dpi">');
						}
					} else {
						document.write('<meta name="viewport" content="width=640, user-scalable=no, maximum-scale=1">');
					}
			</script>
			<link href=./static/css/app.css rel=stylesheet>
		</head>
		<body>
			<div id=app></div>
			<script type=text/javascript src=./static/js/manifest.js></script>
			<script type=text/javascript src=./static/js/app.js></script>
		</body>
	</html>