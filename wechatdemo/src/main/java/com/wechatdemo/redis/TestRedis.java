package com.wechatdemo.redis;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author pks
 * @version 2018年5月21日
 */
public class TestRedis {
	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath:conf/spring-redis-test.xml");
		RedisUtil redisUtil = (RedisUtil) context.getBean("redisUtil");
//		User u1 = new User();
//		u1.setName("张三");
//		u1.setAge(18);
//		User u2 = new User();
//		u2.setName("李四");
//		u2.setAge(19);
//		redisUtil.lSet("userList", u1);
//		redisUtil.lSet("userList", u2);
		
		System.out.println(redisUtil.lGetListSize("userList"));
		List<Object> list = redisUtil.lGet("userList", 0, 10);
		for (Object object : list) {
			if(object instanceof User){
				User u = (User) object;
				System.out.println(u.getName()+":"+u.getAge());
			}
		}
		User u3 = new User();
		u3.setName("王五");
		u3.setAge(20);
		redisUtil.lUpdateIndex("userList", 1, u3);
		
		System.out.println(redisUtil.lGetListSize("userList"));
		list = redisUtil.lGet("userList", 0, 10);
		for (Object object : list) {
			if(object instanceof User){
				User u = (User) object;
				System.out.println(u.getName()+":"+u.getAge());
			}
		}

	}
}

class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5156873651519030978L;
	
	private String name;
	private Integer age;

	public User() {
		super();
	}

	public User(String name, Integer age) {
		super();
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

}
