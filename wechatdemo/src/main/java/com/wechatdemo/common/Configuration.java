package com.wechatdemo.common;

public class Configuration {

	private static String contextPath;

	// 通过ApplicationListener初始化
	private static String realPath;

	public static String wechat_token;
	public static String wechat_appId;
	public static String wechat_appSecret;
	public static String wechat_mch_id;
	public static String wechat_api_key;
	public static String wechat_cert_path;
	public static String wechat_domain_url;
	public static String wechat_notify_url;
	public static String wechat_pay_action;
	
	public static String getContextPath() {
		return contextPath;
	}

	public static void setContextPath(String contextPath) {
		Configuration.contextPath = contextPath;
	}

	public static String getRealPath() {
		return realPath;
	}

	public static void setRealPath(String realPath) {
		Configuration.realPath = realPath;
	}

	public static String getWechat_token() {
		return wechat_token;
	}

	public static void setWechat_token(String wechat_token) {
		Configuration.wechat_token = wechat_token;
	}

	public static String getWechat_appId() {
		return wechat_appId;
	}

	public static void setWechat_appId(String wechat_appId) {
		Configuration.wechat_appId = wechat_appId;
	}

	public static String getWechat_appSecret() {
		return wechat_appSecret;
	}

	public static void setWechat_appSecret(String wechat_appSecret) {
		Configuration.wechat_appSecret = wechat_appSecret;
	}

	public static String getWechat_mch_id() {
		return wechat_mch_id;
	}

	public static void setWechat_mch_id(String wechat_mch_id) {
		Configuration.wechat_mch_id = wechat_mch_id;
	}

	public static String getWechat_api_key() {
		return wechat_api_key;
	}

	public static void setWechat_api_key(String wechat_api_key) {
		Configuration.wechat_api_key = wechat_api_key;
	}

	public static String getWechat_cert_path() {
		return wechat_cert_path;
	}

	public static void setWechat_cert_path(String wechat_cert_path) {
		Configuration.wechat_cert_path = wechat_cert_path;
	}

	public static String getWechat_domain_url() {
		return wechat_domain_url;
	}

	public static void setWechat_domain_url(String wechat_domain_url) {
		Configuration.wechat_domain_url = wechat_domain_url;
	}

	public static String getWechat_notify_url() {
		return wechat_notify_url;
	}

	public static void setWechat_notify_url(String wechat_notify_url) {
		Configuration.wechat_notify_url = wechat_notify_url;
	}

	public static String getWechat_pay_action() {
		return wechat_pay_action;
	}

	public static void setWechat_pay_action(String wechat_pay_action) {
		Configuration.wechat_pay_action = wechat_pay_action;
	}
	
	
}
