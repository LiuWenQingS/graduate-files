package com.hiekn.kc;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;

public class SqlFormat {
	public static void main(String[] args) {
		try {
			format();
//			appendChild();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void filter() {
		try {
			FileWriter fw = new FileWriter("data/zhihu/hudong_format_filter_b.txt");
			BufferedReader br = BufferedReaderUtil.getBuffer("data/zhihu/hudong_format_child.txt");
			String input = "";
			while ((input = br.readLine())!=null) {
				if (!input.contains("医院") && !input.contains("病医") && !input.contains("公司") && !input.contains("《") && !input.contains("？")) {
					JSONObject obj = JSONObject.parseObject(input);
					String topic = obj.getString("topic");
//					if (topic.length() ==3 ) {
//						
//					}
					fw.write(input + "\r\n");
					fw.flush();
				}
			}
			br.close();
			fw.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Test 
	public void getDedupId() {
		try {
			BufferedReader br = BufferedReaderUtil.getBuffer("data/zhihu/hudong_format.txt");
			String input = "";
			while ((input = br.readLine())!=null) {
				String[] split = input.split("\t");
				String id = split[2];
				if (id.equals("0")) {
					System.out.println(input);
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test 
	public void dedupId() {
		try {
			FileWriter fw = new FileWriter("data/zhihu/dedup_id_baike.txt");
			FileWriter ew = new FileWriter("data/zhihu/error_id_baike.txt");
			int count = 0;
			Set<Integer> hudongId = new HashSet<Integer>();
			BufferedReader br = null;
			String input = "";
			
			br = BufferedReaderUtil.getBuffer("data/zhihu/hudong_format_child.txt");
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				int id = obj.getIntValue("topicId");
				if (!hudongId.contains(id)) {
					hudongId.add(id);
				} else {
					System.out.println(input);
				}
			}
			System.out.println(hudongId.size());
			
			br = BufferedReaderUtil.getBuffer("data/zhihu/baike.txt");
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				int id = obj.getIntValue("topicId");
				if (!hudongId.contains(id)) {
					fw.write(input + "\r\n");
					fw.flush();
				} else {
					obj.put("topicId", id*10+1);
					ew.write(obj.toJSONString() + "\r\n");
					ew.flush();
				}
			}
			System.out.println(count);
			br.close();
			fw.close();
			ew.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void dedup() {
		try {
			Set<String> set = new HashSet<String>();
			FileWriter fw = new FileWriter("data/zhihu/hudong_format_dedup.txt");
			BufferedReader br = BufferedReaderUtil.getBuffer("data/zhihu/hudong_format_child.txt");
			String input = "";
			while ((input = br.readLine())!=null) {
				set.add(input.trim());
			}
			for (String string : set) {
				fw.write(string+"\r\n");
				fw.flush();
			}
			fw.close();
			br.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void appendChild() throws Exception {
		FileWriter fw = new FileWriter("data/zhihu/hudong_format_child.txt");
		int id = 90000;
		BufferedReader br = BufferedReaderUtil.getBuffer("data/zhihu/hudong_list_dedup.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			JSONObject obj = JSONObject.parseObject(input);
			JSONArray arr = obj.getJSONArray("topicList");
			int parentId = obj.getIntValue("topicId");
			JSONObject obj1 = new JSONObject();
			obj1.put("parentId", obj.getInteger("parentId"));
			obj1.put("topicId", obj.getInteger("topicId"));
			obj1.put("topic", obj.getString("topic"));
			fw.write(obj1.toJSONString() + "\r\n");
			fw.flush();
			for (Object object : arr) {
				int subId = ++id;
				JSONObject resultObj = new JSONObject();
				resultObj.put("parentId", parentId);
				resultObj.put("topic", object.toString());
				resultObj.put("topicId", subId);
				fw.write(resultObj.toJSONString() + "\r\n");
				fw.flush();
			}
		}
		br.close();
		fw.close();
	}
	
	public static void format() throws Exception {
		FileWriter fw = new FileWriter("data/zhihu/hudong_format.txt");
		BufferedReader br = BufferedReaderUtil.getBuffer("data/zhihu/integrate_id.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			JSONObject obj = JSONObject.parseObject(input);
			if (obj.containsKey("parentId")) {
				fw.write(obj.getIntValue("topicId") + "\t" + obj.getString("topic") + "\t" + obj.getIntValue("parentId") + "\r\n");
				fw.flush();
			} else {
				fw.write(obj.getIntValue("topicId") + "\t" + obj.getString("topic") + "\t" + -1 + "\r\n");
				fw.flush();
			}
		}
		br.close();
		fw.close();
	}
}
