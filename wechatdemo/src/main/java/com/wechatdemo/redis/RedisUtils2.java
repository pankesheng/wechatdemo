package com.wechatdemo.redis;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component("redisUtils2")
public class RedisUtils2 {

	private RedisTemplate<String, Object> redisTemplate;

	@Autowired
	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public <T> void put(String key, T obj) {
		redisTemplate.opsForValue().set(key, obj);
	}

	public <T> void put(String key, T obj, int timeout) {
		put(key, obj, timeout, TimeUnit.MINUTES);
	}

	public <T> void put(String key, T obj, int timeout, TimeUnit unit) {
		redisTemplate.opsForValue().set(key, JsonUtils.toJson(obj), timeout, unit);
	}
	
	public <T> T get(String key, Class<T> cls) {
		return (T) redisTemplate.opsForValue().get(key);
	}
	
	public <E, T extends Collection<E>> T get(String key, Class<E> cls, Class<T> collectionClass) {
		return (T) redisTemplate.opsForValue().get(key);
	}

	public boolean exists(String key) {
		return redisTemplate.hasKey(key);
	}

	public void delete(String key) {
		redisTemplate.delete(key);
	}

	public boolean expire(String key, long timeout, TimeUnit timeUnit) {
		return redisTemplate.expire(key, timeout, timeUnit);
	}
	
	public boolean expire(String key, long timeout) {
		return expire(key, timeout, TimeUnit.MINUTES);
	}
	
	public void put(String key, String value) {
		redisTemplate.opsForValue().set(key, value);
	}

	public void put(String key, String value, int timeout) {
		put(key, value, timeout, TimeUnit.MINUTES);
	}

	public void put(String key, String value, int timeout, TimeUnit unit) {
		redisTemplate.opsForValue().set(key, value, timeout, unit);
	}

	
	public String get(String key) {
		return (String) redisTemplate.opsForValue().get(key);
	}
	
}
