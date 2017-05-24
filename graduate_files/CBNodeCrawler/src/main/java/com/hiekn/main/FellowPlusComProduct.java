package com.hiekn.main;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;

public class FellowPlusComProduct {
	static String prefixIos = "http://dataapi.fellowplus.com/project/ios_package?_id=12463&_token=uBUUafkzqIDKT10IErz5gTQb%2F%2FngqVigptOimBB157s%3D&id=";
	static String prefixAnd = "http://dataapi.fellowplus.com/project/android_package?_id=12463&_token=uBUUafkzqIDKT10IErz5gTQb%2F%2FngqVigptOimBB157s%3D&id=";
	public static void main(String[] args) {
		try {
			while (true) {
				runner();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void runner() {
		try {
			FileWriter fw = new FileWriter("data/dp/com_product_android.txt",true);
			HttpReader reader = new StaticHttpReader();
			Set<String> crawlSet = getCrawlSet();
			System.out.println(crawlSet.size());
			for (String id : crawlSet) {
//				try {
//					String iosUrl = prefixIos + URLEncoder.encode(id, "utf-8");
//					String source = reader.readSource(iosUrl);
//					JSONObject resultObj = JSONObject.parseObject(source);
//					fw.write(id + "\t" + resultObj.getString("ios_packages") + "\r\n");
//					fw.flush();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				
				try {
					String andUrl = prefixAnd + URLEncoder.encode(id, "utf-8");
					String source = reader.readSource(andUrl);
					JSONObject resultObj = JSONObject.parseObject(source);
					fw.write(id + "\t" + resultObj.getString("android_packages") + "\r\n");
					fw.flush();
				} catch (Exception e) {
					e.printStackTrace();
				}
				Thread.sleep(500);
			}
			reader.close();
			fw.close();
		} catch (Exception e) {
			try {
				TimeUnit.MINUTES.sleep(2);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public static Set<String> getCrawlSet() throws Exception {
		Set<String> crawlSet = new HashSet<String>();
		Set<String> crawledSet = new HashSet<String>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/dp/com_info.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			JSONObject obj = JSONObject.parseObject(input);
			crawlSet.add(obj.getString("id"));
		}
		br = BufferedReaderUtil.getBuffer("data/dp/com_product_android.txt");
		while ((input = br.readLine())!=null) {
			String[] split = input.split("\t");
			crawledSet.add(split[0]);
		}
		crawlSet.removeAll(crawledSet);
		br.close();
		return crawlSet;
	}
}
