package com.wechatdemo.entity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

/**
 * @author pks
 * @version 2017年9月25日
 */
public class Test2 {
	
public static void main(String[] args) throws Exception {

		
		String s = "#id#btn_name#";
		String s2 = s.substring(1, s.length()-1);
		String str[] = s2.split("#");
		List<MenuButton> list = new ArrayList<MenuButton>();
		for (int i = 0; i < 99; i++) {
			MenuButton btn = new MenuButton();
			btn.setId(100000000L+i);
			btn.setBtn_name("测试"+i);
			btn.setBtn_url("testUrl"+i);
			list.add(btn);
		}

		Long a = System.currentTimeMillis();
		List<MenuButton> showList = new ArrayList<MenuButton>();
		for (MenuButton btn : list) {
			MenuButton show = new MenuButton();
			for (String string : str) {
				Field f = MenuButton.class.getDeclaredField(string);
				f.setAccessible(true);
				Object val = f.get(btn);
				f.set(show, val);
			}
			showList.add(show);
		}
		Long b = System.currentTimeMillis();
		System.out.println(b-a);
		System.out.println(new Gson().toJson(showList));
		
		
		List<MenuButton> showList2 = new ArrayList<MenuButton>();
		Field[] fs = MenuButton.class.getDeclaredFields();
		for (MenuButton btn : list) {
			MenuButton show = new MenuButton();
			for (int i = 0; i < fs.length; i++) {
				Field f = fs[i];
				f.setAccessible(true); // 设置些属性是可以访问的
				Object val = f.get(btn);// 得到此属性的值
				if(s.contains("#"+f.getName()+"#")){
					f.set(show, val);
				}
			}
			showList2.add(show);
		}
		Long c = System.currentTimeMillis();
		System.out.println(c-b);
		System.out.println(new Gson().toJson(showList2));
		
	}

}

