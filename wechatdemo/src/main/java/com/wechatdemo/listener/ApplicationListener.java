package com.wechatdemo.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.pks.wechat.configuration.WeChatConfigs;
import com.wechatdemo.common.Configuration;

public class ApplicationListener implements ServletContextListener {
//	private WebApplicationContext wac = null;
//	private CatalogService catalogService = null;

	public void contextInitialized(ServletContextEvent sce) {
//		wac = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
//		catalogService = (CatalogService) wac.getBean("catalogService");
//		System.out.println(1);
		
		WeChatConfigs.init(Configuration.wechat_token, Configuration.wechat_appId, Configuration.wechat_appSecret, Configuration.wechat_mch_id, 
				Configuration.wechat_api_key,Configuration.wechat_cert_path, Configuration.wechat_notify_url, Configuration.wechat_pay_action);
	}

	public void contextDestroyed(ServletContextEvent sce) {
		
	}
	
}