package com.wechatdemo.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.gson.Gson;
import com.pks.wechat.configuration.WeChatConfig;
import com.pks.wechat.configuration.WeChatConfigs;
import com.pks.wechat.material.BasicMaterial;
import com.pks.wechat.material.OtherMaterial;
import com.pks.wechat.material.UploadMaterail;
import com.pks.wechat.message.resp.Article;
import com.pks.wechat.message.tempmsg.TempData;
import com.pks.wechat.message.tempmsg.TempMsg;
import com.pks.wechat.pojo.SNSUserInfo;
import com.pks.wechat.pojo.WeChatGroup;
import com.pks.wechat.pojo.WeChatOauth2Token;
import com.pks.wechat.pojo.WeChatQRCode;
import com.pks.wechat.pojo.WeChatUserInfo;
import com.pks.wechat.util.SUtilCommon;
import com.pks.wechat.util.SUtilCustomer;
import com.pks.wechat.util.SUtilGroup;
import com.pks.wechat.util.SUtilMaterial;
import com.pks.wechat.util.SUtilMedia;
import com.pks.wechat.util.SUtilOauth2;
import com.pks.wechat.util.SUtilQrcode;
import com.pks.wechat.util.SUtilTempMsg;
import com.pks.wechat.util.SUtilUser;
import com.wechatdemo.common.Configuration;
import com.wechatdemo.listener.SessionListener;
import com.zcj.util.UtilString;
import com.zcj.web.dto.ServiceResult;

@Controller
@RequestMapping(value = "/wxpay")
public class WeChatPayController {
	
//	@Autowired
//	private OrderService orderService;
	
	@RequestMapping("/test/index")
	public String testIndex(Model model){
		model.addAttribute("orderNo", UtilString.getLongUUID());
		return "/wxpay/testindex.jsp";
	}
	
	@RequestMapping(value="/oauth")
	public void oauth(HttpServletRequest request,HttpServletResponse response,String sessionid) throws IOException{
		System.out.println(sessionid);
		String redirect_uri = Configuration.wechat_domain_url+"/wxpay/oauth2.ajax";//  : -> %3A      / -> %2F
    	String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+Configuration.wechat_appId+"&redirect_uri="+redirect_uri.replaceAll(":", "%3A").replaceAll("/", "%2F")+"&response_type=code&scope=snsapi_base&state="+sessionid+"#wechat_redirect";
		response.sendRedirect(url); 
	}
	
	@RequestMapping(value = "/oauth2")
	public String oauth2(Model model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");// [/align][align=left] //
												// 用户同意授权后，能获取到code
		String code = request.getParameter("code");// [/align][align=left] //
		String state = request.getParameter("state");
		HttpSession session = SessionListener.getSession(state);
		if(session!=null){
//			session.setAttribute("userinfo", true);
		}
		// 用户同意授权
		if (!"authdeny".equals(code)) {
			// 获取网页授权access_token
			WeChatOauth2Token weixinOauth2Token = SUtilOauth2.getOauth2AccessToken(Configuration.wechat_appId,code);
			// 用户标识
			String openId = weixinOauth2Token.getOpenId();
			// 获取用户信息
			SNSUserInfo snsUserInfo = SUtilOauth2.getSNSUserInfo(weixinOauth2Token.getAccessToken(),openId);// [/align][align=left] // 设置要传递的参数
			WeChatUserInfo userInfo = SUtilUser.getUserInfo(Configuration.wechat_appId,openId);
			request.setAttribute("snsUserInfo", snsUserInfo);
			model.addAttribute("snsUserInfo", snsUserInfo);
			request.setAttribute("param", state);
			model.addAttribute("param", state);
		}
		model.addAttribute("basePath", Configuration.getContextPath());
		return "/wxpay/index2.jsp";
	}
	
	// userId 用户id
	// orderNo 订单编号
	// descr 商品描述
	// money 金额，double类型
	@RequestMapping("/paymain")
	public void paymain(HttpServletRequest request,
			HttpServletResponse response, Long userId, Long orderNo,
			String descr, Double money, PrintWriter out) throws IOException {
		String appid = Configuration.wechat_appId;
		WeChatConfig config = WeChatConfigs.getConfig(appid);
		String backUri = config.getPay_action();
		if (orderNo == null) {
			out.write(ServiceResult.initErrorJson("订单号不能为空!"));
			return;
		}
		if(StringUtils.isBlank(descr)){
			out.write(ServiceResult.initErrorJson("商品描述不能为空！"));
			return ;
		}
		if (money == null) {
			out.write(ServiceResult.initErrorJson("金额不能为空！"));
			return;
		}
		// 授权后要跳转的链接所需的参数一般有会员号，金额，订单号之类，
		// 最好自己带上一个加密字符串将金额加上一个自定义的key用MD5签名或者自己写的签名,
		// 比如 Sign = %3D%2F%CS%
		// orderNo = appid+SHA1Util.getTimeStamp();
		backUri = backUri + "?userId=" + userId + "&orderNo=" + orderNo	+ "&describe=" + descr + "&money=" + money;
		// URLEncoder.encode 后可以在backUri 的url里面获取传递的所有参数
		backUri = URLEncoder.encode(backUri,"UTF-8");
		// scope 参数视各自需求而定，这里用scope=snsapi_base 不弹出授权页面直接授权目的只获取统一支付接口的openid
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appid
				+ "&redirect_uri=" + backUri
				+ "&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
		response.sendRedirect(url);
	}

	@RequestMapping("/qrcode")
	public void qrcode(HttpServletRequest request,PrintWriter out){
		
		UploadMaterail a = SUtilMaterial.uploadMaterail(Configuration.getWechat_appId(), new File("D:/404.png"), "asdf", "2341234");
		List<Article> list = new ArrayList<Article>();
		list.add(new Article("222222", "123123123", a.getUrl(), "www.baidu.com"));
		String s = SUtilCustomer.makeNewsCustomMessage("oXWySjiKx0vEVHmMs-Ul9u8O4_vA", list);
		System.out.println(s);
		SUtilCustomer.sendCustomMessage(Configuration.getWechat_appId(), s);
//		List<BasicMaterial> list = SUtilMaterial.getMaterailList(Configuration.getWechat_appId(), "images", 0, 20);
//		if(list!=null && list.size()>0){
//			BasicMaterial b = list.get(0);
//			if(b instanceof OtherMaterial){
//				OtherMaterial obj = (OtherMaterial) b;
//				String s = SUtilCustomer.makeImageCustomMessage("oXWySjiKx0vEVHmMs-Ul9u8O4_vA", obj.getMedia_id());
//				System.out.println(s);
//				SUtilCustomer.sendCustomMessage(Configuration.getWechat_appId(), s);
//			}
//		}
	}
	
}
