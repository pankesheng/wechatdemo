package com.wechatdemo.listener;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

	private static final ConcurrentHashMap<String, HttpSession> SESSION_MAP = new ConcurrentHashMap<String, HttpSession>();
	
	@Override
	public void sessionCreated(HttpSessionEvent event) {
		synchronized (SessionListener.class) {
			System.out.println("保存 sessionid："+event.getSession().getId());
			SESSION_MAP.put(event.getSession().getId(), event.getSession());
		}
	}
	
	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		synchronized (SessionListener.class) {
			SESSION_MAP.remove(event.getSession().getId());
		}
	}
	
	public static HttpSession getSession(String sessionId){
		synchronized (SessionListener.class) {
			return SESSION_MAP.get(sessionId);
		}
	}
	

}