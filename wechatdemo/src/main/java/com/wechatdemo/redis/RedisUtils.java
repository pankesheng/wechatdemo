package com.wechatdemo.redis;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component("redisUtils")
public class RedisUtils {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

//	public void setRedisTemplate(StringRedisTemplate stringRedisTemplate) {
//		this.stringRedisTemplate = stringRedisTemplate;
//	}

	public <T> void put(String key, T obj) {
		stringRedisTemplate.opsForValue().set(key, JsonUtils.toJson(obj));
	}

	public <T> void put(String key, T obj, int timeout) {
		put(key, obj, timeout, TimeUnit.MINUTES);
	}

	public <T> void put(String key, T obj, int timeout, TimeUnit unit) {
		stringRedisTemplate.opsForValue().set(key, JsonUtils.toJson(obj), timeout, unit);
	}
	
	public <T> T get(String key, Class<T> cls) {
		return JsonUtils.fromJson(stringRedisTemplate.opsForValue().get(key), cls);
	}
	
	public <E, T extends Collection<E>> T get(String key, Class<E> cls, Class<T> collectionClass) {
		return JsonUtils.fromJson(stringRedisTemplate.opsForValue().get(key), cls, collectionClass);
	}



	public boolean exists(String key) {
		return stringRedisTemplate.hasKey(key);
	}

	public void delete(String key) {
		stringRedisTemplate.delete(key);
	}

	public boolean expire(String key, long timeout, TimeUnit timeUnit) {
		return stringRedisTemplate.expire(key, timeout, timeUnit);
	}
	
	public boolean expire(String key, long timeout) {
		return expire(key, timeout, TimeUnit.MINUTES);
	}
	
	public void put(String key, String value) {
		stringRedisTemplate.opsForValue().set(key, value);
	}

	public void put(String key, String value, int timeout) {
		put(key, value, timeout, TimeUnit.MINUTES);
	}

	public void put(String key, String value, int timeout, TimeUnit unit) {
		stringRedisTemplate.opsForValue().set(key, value, timeout, unit);
	}

	
	public String get(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}
	
}
