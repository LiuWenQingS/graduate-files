package com.hiekn.parser;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;

public class ErrorParse {
	
	@Test
	public void t1() {
		try {
			FileWriter fw = new FileWriter("data/dp/com_gs_3.txt");
			BufferedReader br = BufferedReaderUtil.getBuffer("data/dp/com_gs_2.txt");
			String input = "";
			while ((input = br.readLine())!=null) {
				String[] split = input.split("\t");
				String key = split[0];
				String json = "";
				if (split.length > 2) {
					for (int i = 1; i < split.length; i++) {
						json = json + split[i];
					}
				}
				if (!json.equals("null")&&!json.equals("")) {
					fw.write(key +"\t"+json + "\r\n");
					fw.flush();
				}
			}
			fw.close();
			br.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Test
	public void t2() {
		try {
			Set<String> crawlSet = new HashSet<String>();
			BufferedReader br = BufferedReaderUtil.getBuffer("data/dp/com_info.txt");
			String input = "";
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				String id = obj.getString("id");
				if (crawlSet.contains(id)) {
					System.out.println(id);
				} else {
					crawlSet.add(obj.getString("id"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
