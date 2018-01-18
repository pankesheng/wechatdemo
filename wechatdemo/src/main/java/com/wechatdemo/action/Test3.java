package com.wechatdemo.action;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.pks.wechat.util.SUtilCommon;

/**
 * @author pks
 * @version 2017年11月27日
 */
public class Test3 {
	public static void main(String[] args) throws UnsupportedEncodingException {
		Map<String, Object> params = new HashMap<String, Object>();
		String ip = "http://yun.ohedu.net";
		ip = "http://192.168.1.158:8080";
//		String result = SUtilCommon.httpGetRequest(ip+"/api/getToken.json?userName=ohedu_vpn&passWord="+new SHA1().getDigestOfString("oheduvpngood!.".getBytes("utf-8")));
//		JSONObject jsonObject = new JSONObject(result);
//		String token = jsonObject.get("d").toString();
//		params.put("token", token);
//		params.put("userName", "admin");
//		params.put("passWord", "4b7d9fc328c1806d821cea7acfd443f0");
//		params.put("methodName", "checkVPN");
//		result = SUtilCommon.httpPostRequest(ip+"/api/query.json", SUtilCommon.paramstourl(params));
//		System.out.println(result);
		
		String result = "";
//		result = SUtilCommon.httpGetRequest(ip+"/httpapi/getToken.json?account=ohedu_spon&password=spon123456");
		params.put("apiToken", "k3J2t0QOi3ysKj1EW8nw");
		params.put("moduleId", 1);
		result = SUtilCommon.httpPostRequest(ip+"/httpapi/findUsersByModuleId.json", SUtilCommon.paramstourl(params));
		System.out.println(result);
		
	}
}
