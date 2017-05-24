package com.hiekn.parser;

import java.io.BufferedReader;
import java.io.FileWriter;

import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;

public class FpDateProcess {
	public static void main(String[] args) {
		comExtraction();
	}
	
	public static void comExtraction() {
		BufferedReader br = null;
		FileWriter fw = null;
		String input = "";
		try {
			fw = new FileWriter("data/dp/com_list.txt");
			br = BufferedReaderUtil.getBuffer("data/dp/com_final.txt");
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				if (obj.containsKey("gs")) {
					JSONObject gsObj = obj.getJSONObject("gs");
					if (gsObj.containsKey("name")) {
						String comName = gsObj.getString("name");
						fw.write(comName + "\r\n");
						fw.flush();
					} else {
						System.out.println(gsObj.toJSONString());
					}
				}
			}
			br.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
