package com.hiekn.other;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;

public class FpDataProcess {
	public static void main(String[] args) {
		try {
			runner();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void runner() throws Exception {
		FileWriter dfw = new FileWriter("data/dp/dedup.txt");
		int error = 0;
		Map<String,Integer> countMap = new HashMap<String,Integer>();
		Map<String,Integer> countMap2 = new HashMap<String,Integer>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/dp/all.txt");
		Set<String> resultSet = new HashSet<String>();
		String input = "";
		while ((input = br.readLine())!=null) {
			resultSet.add(input.trim());
		}
		System.out.println(resultSet.size());
		for (String string : resultSet) {
			try {
				JSONObject obj = JSONObject.parseObject(string);
				String district = obj.getString("location");
				String domain = obj.getString("biz_domain");
				dfw.write(string+"\r\n");
				dfw.flush();
				String key = district + domain;
				if (countMap.containsKey(key))	{
					int count = countMap.get(key) + 1;
					int count2 = countMap2.get(district) + 1;
					countMap.put(key, count);
					countMap2.put(district, count2);
				} else {
					countMap.put(key, 1);
					countMap2.put(district, 1);
				}
				
			} catch (Exception e) {
				// TODO: handle exception
				error++;
//				System.out.println(string);
			}
		}
		System.out.println(error);
		String json = JSON.toJSONString(countMap);
		FileWriter fw = new FileWriter("data/dp/count.txt");
		fw.write(json+"\r\n");
		fw.flush();
		json = JSONObject.toJSONString(countMap2);
		fw.write(json+"\r\n");
		fw.flush();
		fw.close();
		dfw.close();
	}
	
}
