package com.wechatdemo.redis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.wechatdemo.entity.MenuButton;

@Component("redisUtils3")
public class RedisUtils3 {

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	public void set(String objectKey,String key,Object obj){
		redisTemplate.opsForHash().put(objectKey, key, obj);
	}
	
	public void remove(String objectKey,String key){
		redisTemplate.opsForHash().delete(objectKey, key);
	}
	
	public MenuButton get(String objectKey,String key){
		return (MenuButton) redisTemplate.opsForHash().get(objectKey, key);
	}
	
	public void set(String key,Object obj){
		redisTemplate.opsForList().rightPush("list_menubutton", obj);
		redisTemplate.opsForValue().set(key, obj);
	}
	
	public MenuButton get(String key){
		return (MenuButton) redisTemplate.opsForValue().get(key);
	}

}
