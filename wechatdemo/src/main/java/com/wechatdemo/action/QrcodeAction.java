package com.wechatdemo.action;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.pks.wechat.util.SUtilSign;
import com.wechatdemo.common.Configuration;
import com.zcj.web.dto.ServiceResult;
import com.zcj.web.springmvc.action.BasicAction;

@Controller
@RequestMapping(value="qrcode")
public class QrcodeAction extends BasicAction {
	
	
	@RequestMapping("/toqrcode")
	public String tolist(HttpServletRequest request,Model model){
//		String redirect_uri = Configuration.wechat_domain_url+"/wxpay/oauth2.ajax";//  : -> %3A      / -> %2F
//		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Configuration.wechat_appId+"&redirect_uri="+redirect_uri.replaceAll(":", "%3A").replaceAll("/", "%2F")+"&response_type=code&scope=snsapi_userinfo&state="+request.getSession().getId()+"#wechat_redirect";
		String url = Configuration.wechat_domain_url+"/wxpay/oauth.ajax?sessionid="+request.getSession().getId();
		model.addAttribute("qrcodeurl", url);
		System.out.println(url);
		request.getSession().setAttribute("test", "这是一个测试文本！！！！！");
		return "/WEB-INF/ftl/admin/weixin/qrcode_list.ftl";
	}
	
	@RequestMapping("/checkSession")
	public void checkSession(HttpServletRequest request,PrintWriter out){
		Boolean bool = (Boolean) request.getSession().getAttribute("userinfo");
		if(bool!=null && bool){
			out.write(ServiceResult.initSuccessJson("http://yun.ohedu.net"));
		}else{
			out.write(ServiceResult.initErrorJson(null));
		}
	}
	
}
