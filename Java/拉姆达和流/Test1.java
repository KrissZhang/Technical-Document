package com.self.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 拉姆达和流测试类
 * @author Zp
 */
public class Test1 {
	public static void main(String[] args) {
		//拉姆达表达式基本用法测试方法
		function1();
		
		//流排序测试方法
		function2();
		
		//流分组测试方法
		function3();
	}
	
	/**
	 * 拉姆达表达式基本用法测试方法
	 */
	public static void function1() {
		//初始化数据集
		List<Map<String,Object>> list = new ArrayList<>();
		Map<String,Object> map1 = new HashMap<>();
		map1.put("key1", "1-1");
		map1.put("key2", "1-2");
		list.add(map1);
		
		Map<String,Object> map2 = new HashMap<>();
		map2.put("key1", "2-1");
		map2.put("key2", "2-2");
		list.add(map2);
		
		Map<String,Object> map3 = new HashMap<>();
		map3.put("key1", "3-1");
		map3.put("key2", "3-2");
		list.add(map3);
		
		Map<String,Object> map4 = new HashMap<>();
		map4.put("key1", "4-1");
		//map4.put("key2", "4-2");
		list.add(map4);
		
		Map<String,Object> map5 = new HashMap<>();
		//map5.put("key1", "3-1");
		map5.put("key2", "3-2");
		list.add(map5);
		
		//创建流->过滤key1和key2为空的数据->循环追加key3->去重->收集数据返回
		List<Object> list1 = list.parallelStream().filter(m->m.get("key1")!=null && m.get("key2")!=null).map(m->{
			m.put("key3", "value3");
			return m;
		}).distinct().collect(Collectors.toList());
		
		//遍历返回结果集
		list1.forEach(o->{
			if(o instanceof HashMap) {
				@SuppressWarnings("unchecked")
				Map<String,String> map = (HashMap<String,String>)o;
				Set<String> ss = map.keySet();
				ss.forEach(s -> {
					System.out.println(map.getOrDefault(s, ""));
				});
				System.out.println("-------------------------");
			}
		});
	}
	
	/**
	 * 流排序测试方法
	 */
	public static void function2() {
		//初始化数据集
		List<Map<String,Integer>> list = new ArrayList<>();
		Map<String,Integer> map1 = new HashMap<>();
		map1.put("id", 1);
		map1.put("age", 25);
		map1.put("result", 86);
		list.add(map1);
		
		Map<String,Integer> map2 = new HashMap<>();
		map2.put("id", 1);
		map2.put("age", 15);
		map2.put("result", 91);
		list.add(map2);
		
		Map<String,Integer> map3 = new HashMap<>();
		map3.put("id", 2);
		map3.put("age", 25);
		map3.put("result", 95);
		list.add(map3);
		
		Map<String,Integer> map4 = new HashMap<>();
		map4.put("id", 2);
		map4.put("age", 25);
		map4.put("result", 96);
		list.add(map4);
		
		Map<String,Integer> map5 = new HashMap<>();
		map5.put("id", 2);
		map5.put("age", 25);
		map5.put("result", 10);
		list.add(map5);
		
		//依次根据id、age和result排序
		list.sort((lObj,rObj)->{
			if(lObj.get("id") == (rObj.get("id"))) {
				if(lObj.get("age") == (rObj.get("age"))) {
					return lObj.get("result").compareTo(rObj.get("result"));
				}else {
					return lObj.get("age").compareTo(rObj.get("age"));
				}
			}else {
				return lObj.get("id").compareTo(rObj.get("id"));
			}
		});
		
		//遍历查看结果
		list.forEach(l -> {
			String fStr = String.format("id:%s,age:%s,result:%s", l.getOrDefault("id", 0),l.getOrDefault("age", 0),l.getOrDefault("result", 0));
			System.out.println(fStr);
			System.out.println("-------------");
		});
	}
	
	/**
	 * 流分组测试方法（根据单个属性分组，多个属性分组暂时没有找到好办法）
	 */
	public static void function3() {
		//初始化数据集
		List<Map<String,Integer>> list = new ArrayList<>();
		Map<String,Integer> map1 = new HashMap<>();
		map1.put("id", 1);
		map1.put("age", 25);
		map1.put("result", 86);
		list.add(map1);
		
		Map<String,Integer> map2 = new HashMap<>();
		map2.put("id", 1);
		map2.put("age", 15);
		map2.put("result", 91);
		list.add(map2);
		
		Map<String,Integer> map3 = new HashMap<>();
		map3.put("id", 2);
		map3.put("age", 25);
		map3.put("result", 95);
		list.add(map3);
		
		Map<String,Integer> map4 = new HashMap<>();
		map4.put("id", 2);
		map4.put("age", 25);
		map4.put("result", 96);
		list.add(map4);
		
		Map<String,Integer> map5 = new HashMap<>();
		map5.put("id", 2);
		map5.put("age", 25);
		map5.put("result", 10);
		list.add(map5);
		
		//将list按照id进行分组
		Map<Object, List<Map<String, Integer>>> mResult = list.stream().collect(Collectors.groupingBy(i-> {
			Map<String,Integer> m = (Map<String,Integer>)i;
			return m.get("id");
		}));
		
		//遍历查看结果
		Set<Object> set = mResult.keySet();
		set.forEach(k -> {
			List<Map<String, Integer>> mList = mResult.get(k);
			System.out.println("key:" + k);
			System.out.println("values:" + mList.toString());
			System.out.println("--------------");
		});
	}
	
}
