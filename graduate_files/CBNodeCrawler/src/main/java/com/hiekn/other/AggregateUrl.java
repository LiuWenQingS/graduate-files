package com.hiekn.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;

import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;

public class AggregateUrl {
	public static void main(String[] args) throws Exception {
		FileWriter fw = new FileWriter("data/kr/all.txt");
		String input = "";
		BufferedReader br = null;
		File file = new File("data/kr");
		File[] filelist = file.listFiles();
		for (File file2 : filelist) {
			String path = file2.getAbsolutePath();
			br = BufferedReaderUtil.getBuffer(path);
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				String url = obj.getString("url");
				fw.write(url+"\r\n");
				fw.flush();
			}
		}
		fw.close();
		br.close();
	}
}
