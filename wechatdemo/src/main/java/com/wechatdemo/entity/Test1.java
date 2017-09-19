package com.wechatdemo.entity;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author pks
 * @version 2017年9月19日
 */
public class Test1 {

	public static void main(String[] args) throws Exception {
		test1();
		test2();
		test3();
		test4();
		test5();
		test6();
		test7();
		test8();
		test9();
		test10();
	}

	public static void test1() {
		System.out.println("--------test1-----------");
		Class<?> demo = Person.class;
		// 取得父类
		Class<?> temp = demo.getSuperclass();
		System.out.println("继承的父类为：   " + temp.getName());
	}

	public static void test2() {
		System.out.println("--------test2------------");
		Class<?> demo = Person.class;
		Constructor<?> cons[] = demo.getConstructors();
		for (int i = 0; i < cons.length; i++) {
			System.out.println("构造方法：  " + cons[i]);
		}
	}

	public static void test3() {
		System.out.println("--------test3------------");
		Class<?> demo = Person.class;
		Constructor<?> cons[] = demo.getConstructors();
		for (int i = 0; i < cons.length; i++) {
			Class<?> p[] = cons[i].getParameterTypes();
			System.out.print("构造方法：  ");
			int mo = cons[i].getModifiers();
			System.out.print(Modifier.toString(mo) + " ");
			System.out.print(cons[i].getName());
			System.out.print("(");
			for (int j = 0; j < p.length; ++j) {
				System.out.print(p[j].getName() + " arg" + i);
				if (j < p.length - 1) {
					System.out.print(",");
				}
			}
			System.out.println("){}");
		}
	}

	public static void test4() {
		System.out.println("--------test4------------");
		Class<?> demo = Person.class;
		Method method[] = demo.getMethods();
		for (int i = 0; i < method.length; ++i) {
			Class<?> returnType = method[i].getReturnType();
			Class<?> para[] = method[i].getParameterTypes();
			int temp = method[i].getModifiers();
			System.out.print(Modifier.toString(temp) + " ");
			System.out.print(returnType.getName() + "  ");
			System.out.print(method[i].getName() + " ");
			System.out.print("(");
			for (int j = 0; j < para.length; ++j) {
				System.out.print(para[j].getName() + " " + "arg" + j);
				if (j < para.length - 1) {
					System.out.print(",");
				}
			}
			Class<?> exce[] = method[i].getExceptionTypes();
			if (exce.length > 0) {
				System.out.print(") throws ");
				for (int k = 0; k < exce.length; ++k) {
					System.out.print(exce[k].getName() + " ");
					if (k < exce.length - 1) {
						System.out.print(",");
					}
				}
			} else {
				System.out.print(")");
			}
			System.out.println();
		}
	}

	public static void test5() {
		System.out.println("--------test5------------");
		Class<?> demo = Person.class;
		System.out.println("===============本类属性========================");
		// 取得本类的全部属性
		Field[] field = demo.getDeclaredFields();
		for (int i = 0; i < field.length; i++) {
			// 权限修饰符
			int mo = field[i].getModifiers();
			String priv = Modifier.toString(mo);
			// 属性类型
			Class<?> type = field[i].getType();
			System.out.println(priv + " " + type.getName() + " "
					+ field[i].getName() + ";");
		}
		System.out
				.println("===============实现的接口或者父类的属性========================");
		// 取得实现的接口或者父类的属性
		Field[] filed1 = demo.getFields();
		for (int j = 0; j < filed1.length; j++) {
			// 权限修饰符
			int mo = filed1[j].getModifiers();
			String priv = Modifier.toString(mo);
			// 属性类型
			Class<?> type = filed1[j].getType();
			System.out.println(priv + " " + type.getName() + " "
					+ filed1[j].getName() + ";");
		}
	}

	public static void test6() {
		System.out.println("--------test6------------");
		Class<?> demo = Person.class;
		try {
			// 调用Person类中的sayChina方法
			Method method = demo.getMethod("sayChina");
			method.invoke(demo.newInstance());
			// 调用Person的sayHello方法
			method = demo.getMethod("sayHello", String.class, int.class);
			method.invoke(demo.newInstance(), "Rollen", 20);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void test7() {
		System.out.println("--------test7------------");
		Class<?> demo = Person.class;
		Object obj = null;
		try {
			obj = demo.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setter(obj, "Sex", "男", String.class);
		getter(obj, "Sex");
	}

	public static void test8() throws Exception {
		System.out.println("--------test8------------");
		Class<?> demo = Person.class;
		Object obj = null;

		obj = demo.newInstance();

		Field field = demo.getDeclaredField("sex");
		field.setAccessible(true);
		field.set(obj, "男");
		System.out.println(field.get(obj));
	}

	public static void test9() {
		System.out.println("--------test9------------");
		int[] temp = { 1, 2, 3, 4, 5 };
		Class<?> demo = temp.getClass().getComponentType();
		System.out.println("数组类型： " + demo.getName());
		System.out.println("数组长度  " + Array.getLength(temp));
		System.out.println("数组的第一个元素: " + Array.get(temp, 0));
		Array.set(temp, 0, 100);
		System.out.println("修改之后数组第一个元素为： " + Array.get(temp, 0));
	}

	public static void test10() {
		System.out.println("--------test10------------");
		int[] temp = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		int[] newTemp = (int[]) arrayInc(temp, 15);
		print(newTemp);
		System.out.println("=====================");
		String[] atr = { "a", "b", "c" };
		String[] str1 = (String[]) arrayInc(atr, 8);
		print(str1);
	}

	public static void test11() {
		System.out.println("--------test11------------");
	}

	/**
	 * @param obj
	 *            操作的对象
	 * @param att
	 *            操作的属性
	 * */
	public static void getter(Object obj, String att) {
		try {
			Method method = obj.getClass().getMethod("get" + att);
			System.out.println(method.invoke(obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param obj
	 *            操作的对象
	 * @param att
	 *            操作的属性
	 * @param value
	 *            设置的值
	 * @param type
	 *            参数的属性
	 * */
	public static void setter(Object obj, String att, Object value,
			Class<?> type) {
		try {
			Method method = obj.getClass().getMethod("set" + att, type);
			method.invoke(obj, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 修改数组大小
	 * */
	public static Object arrayInc(Object obj, int len) {
		Class<?> arr = obj.getClass().getComponentType();
		Object newArr = Array.newInstance(arr, len);
		int co = Array.getLength(obj);
		System.arraycopy(obj, 0, newArr, 0, co);
		return newArr;
	}

	/**
	 * 打印
	 * */
	public static void print(Object obj) {
		Class<?> c = obj.getClass();
		if (!c.isArray()) {
			return;
		}
		System.out.println("数组长度为： " + Array.getLength(obj));
		for (int i = 0; i < Array.getLength(obj); i++) {
			System.out.print(Array.get(obj, i) + " ");
		}
	}
}
