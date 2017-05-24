package com.hiekn.parser;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;

public class FpParser {
	public static void main(String[] args) {
		try {
			extractComGs();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 公司工商信息
	 * @throws Exception
	 */
	public static void extractComGs() throws IOException {
		FileWriter ew = new FileWriter("data/dp/com_gs_error.txt");
		int count = 0;
		int dcount = 0;
		int zcount = 0;
		BufferedReader br = BufferedReaderUtil.getBuffer("data/dp/com_gs.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			String[] split = input.split("\t");
			String key = split[0];
			String json = split[1];
			try {
				JSONArray arr = JSONArray.parseArray(json);
				count++;
				if (arr.size() > 1) {
					dcount++;
				} else if (arr.size() == 0) {
					zcount++;
				}
			} catch (Exception e) {
				ew.write(key + "\r\n");
				ew.flush();
			}
		}
		System.out.println(dcount);
		System.out.println(count);
		System.out.println(zcount);
		System.out.println(count - dcount - zcount);
		ew.close();
		br.close();
	}
	/**
	 * 抽取公司独有id
	 * @throws Exception
	 */
	public static void parser() throws Exception {
		FileWriter fw = new FileWriter("data/dp/com_info.txt");
		BufferedReader br = BufferedReaderUtil.getBuffer("data/dp/dedup.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			JSONObject resultObj = new JSONObject();
			JSONObject obj = JSONObject.parseObject(input);
			String comName = obj.getString("name");
			String id = obj.getString("id");
			resultObj.put("name", comName);
			resultObj.put("id", id);
			fw.write(resultObj.toJSONString()+"\r\n");
			fw.flush();
		}
		br.close();
		fw.close();
	}
}
