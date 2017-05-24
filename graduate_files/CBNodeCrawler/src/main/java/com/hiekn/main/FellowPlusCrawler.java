package com.hiekn.main;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;

public class FellowPlusCrawler {
	
	private static final int limit = 100;
	
	public static void main(String[] args) {
		try {
			runner();
		} catch (Exception e) {
			
		}
	}
	
	@Test
	public void t3() throws Exception {
		FileWriter fw = new FileWriter("data/dp/test_result.txt");
		Map<String,Integer> map = new HashMap<String,Integer>();
		String url = "http://dataapi.fellowplus.com/project/list?_id=12463&_token=uBUUafkzqIDKT10IErz5gTQb%2F%2FngqVigptOimBB157s%3D&filter_content=%7B%22location%22:%7B%22locations%22:%5B%22%E5%BE%B7%E9%98%B3%E5%B8%82%22%5D%7D%7D&limit=100&order_by=-last_round_created_at&start=0";
		HttpReader reader = new StaticHttpReader();
		String source = reader.readSource(url);
		JSONObject obj = JSONObject.parseObject(source);
		JSONArray arr = obj.getJSONArray("projects");
		int count = 0;
		for (Object object : arr) {
			String j = object.toString();
			JSONObject o = JSONObject.parseObject(j);
			String key = o.getString("location");
			if (map.containsKey(key)) {
				int i = map.get(key)+1;
				map.put(key, i);
			} else {
				map.put(key, 1);
			}
			count++;
			fw.write(object.toString()+"\r\n");
			fw.flush();
		}
		String result = JSON.toJSONString(map);
		fw.write(result+"\r\n");
		fw.flush();
		System.out.println(count);
		reader.close();
		fw.close();
	}
	
	@Test
	public void t2() {
		HttpReader reader = new StaticHttpReader();
		int count = 0;
		try {
			for (int i = 0; i < 15; i++) {
				String url = "http://dataapi.fellowplus.com/project/list?_id=12463&_token=uBUUafkzqIDKT10IErz5gTQb%2F%2FngqVigptOimBB157s%3D&filter_content=%7B%22location%22:%7B%22locations%22:%5B%22%E4%B8%8A%E6%B5%B7%E5%B8%82%22%5D%7D,%22biz_domain%22:%7B%22biz_domains%22:%5B%22%E7%A1%AC%E4%BB%B6%22%5D%7D%7D&limit=100&order_by=-last_round_created_at&start=" + i*100;
				String source = reader.readSource(url);
				JSONObject obj = JSONObject.parseObject(source);
				JSONArray arr = obj.getJSONArray("projects");
				count = count + arr.size();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(count);
		reader.close();
	}
	@Test
	public void t1() throws Exception {
		FileWriter fw = new FileWriter("data/dp/standard.txt");
		HttpReader reader = new StaticHttpReader();
		try {
			Set<String> locationSet = getOriLocation();
			JSONObject o = new JSONObject();
			for (String location : locationSet) {
				String url = "http://dataapi.fellowplus.com/project/list?_id=12463&_token=uBUUafkzqIDKT10IErz5gTQb%2F%2FngqVigptOimBB157s%3D&filter_content=%7B%22location%22:%7B%22locations%22:%5B%22"+URLEncoder.encode(location,"utf-8")+"%22%5D%7D%7D&limit=20&order_by=-last_round_created_at&start=0";
				String source = reader.readSource(url);
				JSONObject obj = JSONObject.parseObject(source);
				int total = obj.getIntValue("total");
				o.put(location, total);
			}
			String json = o.toJSONString();
			fw.write(json+"\r\n");
			fw.flush();
		} catch (Exception e) {
			// TODO: handle exception
		}
		reader.close();
		fw.close();
	}
	
	/**
	 * 领域超过5000分量 
	 * @throws Exception
	 */
	public static void runnerB5T() throws Exception {
		FileWriter fw = new FileWriter("data/dp/result_5000.txt");
		FileWriter hw = new FileWriter("data/dp/hw.txt",true);
		FileWriter dw = new FileWriter("data/dp/dw.txt");
		int count = 0;
		HttpReader reader = new StaticHttpReader();
		Set<String> locationSet = getLocation();
		Set<String> financeSet = getFinance();
		for (String loc : locationSet) {
//			if (count ==1) break;
			System.out.println(loc);
			String location = loc;
			Set<String> domainSet = getDomain();
			for (String domain : domainSet) {
				int domainCount = 0;
				System.out.println(domain);
//				String filter = "{\"biz_domain\":{\""biz_domains\":[\""+domain+"\"]},\"location\":{\"locations\":[\""+loc+"\"]}}";
				for (String finance : financeSet) {
					String filter = "{\"biz_domain\":{\"biz_domains\":[\""+domain+"\"]},\"location\":{\"locations\":[\""+loc+"\"]},\"financing\":{\"last_round\":[\""+finance+"\"]}}";
					String prefix = "http://dataapi.fellowplus.com/project/list?_id=12463&_token=uBUUafkzqIDKT10IErz5gTQb%2F%2FngqVigptOimBB157s%3D&filter_content="+URLEncoder.encode(filter,"utf-8")+"&limit="+limit+"&order_by=-last_round_created_at&start=";
					String source = reader.readSource(prefix + 0);
					JSONObject obj = JSONObject.parseObject(source);
					int total = obj.getIntValue("total");
					if (total==0) {
						hw.write(location+"\r\n");
						continue;
					} else if (total > 5000) {
						System.out.println("> 5000" + domain);
						continue;
					} else {
						hw.write(finance+"\r\n");
						JSONArray arr = obj.getJSONArray("projects");
						for (Object object : arr) {
							String s = object.toString();
							fw.write(s.toString()+"\r\n");
							fw.flush();
							domainCount++;
						}
						TimeUnit.SECONDS.sleep(5);
						int size = total/limit + 1;
						for (int i = 1; i < size; i++) {
							int startNum = i*limit;
							System.out.println(startNum);
							String url = prefix + startNum;
							source = reader.readSource(url);
							obj =JSONObject.parseObject(source);
							arr = obj.getJSONArray("projects");
							for (Object object : arr) {
								String s = object.toString();
								fw.write(s.toString()+"\r\n");
								fw.flush();
								domainCount++;
							}
							TimeUnit.SECONDS.sleep(5);
						}
					}
				}
				dw.write(domain+domainCount+"\r\n");
				dw.flush();
			}
			
		}
		fw.close();
		hw.close();
		dw.close();
		reader.close();
	}
	
	/**
	 * 超过5000总量 分量小于5000
	 * @throws Exception
	 */
	public static void runner5T() throws Exception {
		FileWriter fw = new FileWriter("data/dp/result_5000_100.txt",true);
		FileWriter hw = new FileWriter("data/dp/hw.txt",true);
		FileWriter dw = new FileWriter("data/dp/dw.txt");
		int count = 0;
		HttpReader reader = new StaticHttpReader();
//		Set<String> locationSet = getLocation();
		Set<String> locationSet = new HashSet<String>();
		locationSet.add("成都市");
		Set<String> domainSet = getDomain(); 
		for (String loc : locationSet) {
			int allCount = 0;
//			if (count ==1) break;
			System.out.println(loc);
			String location = loc;
			for (String domain : domainSet) {
				int domainCount = 0;
				System.out.println(domain);
				String filter = "{\"biz_domain\":{\"biz_domains\":[\""+domain+"\"]},\"location\":{\"locations\":[\""+loc+"\"]}}";
//				String filter = "{\"biz_domain\":{\"biz_domains\":[\""+domain+"\"]},\"location\":{\"locations\":[\""+loc+"\"]},\"financing\":{\"last_round\":[\""+finance+"\"]}}";
				String prefix = "http://dataapi.fellowplus.com/project/list?_id=12463&_token=uBUUafkzqIDKT10IErz5gTQb%2F%2FngqVigptOimBB157s%3D&filter_content="+URLEncoder.encode(filter,"utf-8")+"&limit="+limit+"&order_by=-last_round_created_at&start=";
				String source = reader.readSource(prefix + 0);
				JSONObject obj = JSONObject.parseObject(source);
				int total = obj.getIntValue("total");
				if (total==0) {
					hw.write(location+"\r\n");
					continue;
				} else if (total > 5000) {
					System.out.println("> 5000" + domain);
					continue;
				} else {
					hw.write(location+"\r\n");
					JSONArray arr = obj.getJSONArray("projects");
					for (Object object : arr) {
						String s = object.toString();
						fw.write(s.toString()+"\r\n");
						fw.flush();
						domainCount++;
					}
//					TimeUnit.SECONDS.sleep(5);
					int size = total/limit + 1;
					for (int i = 1; i < size; i++) {
						int startNum = i*limit;
						System.out.println(startNum);
						String url = prefix + startNum;
						source = reader.readSource(url);
						obj =JSONObject.parseObject(source);
						arr = obj.getJSONArray("projects");
						for (Object object : arr) {
							String s = object.toString();
							fw.write(s.toString()+"\r\n");
							fw.flush();
							domainCount++;
						}
//						TimeUnit.SECONDS.sleep(5);
					}
				}
				dw.write(domain+"\t"+domainCount + "\r\n");
				System.out.println(domain + domainCount);
				allCount = allCount + domainCount;
				dw.flush();
			}
			System.out.println(allCount);
		}
		fw.close();
		hw.close();
		dw.close();
		reader.close();
	}
	
	public static void runner() throws Exception {
		FileWriter fw = new FileWriter("data/dp/result.txt",true);
		FileWriter hw = new FileWriter("data/dp/hw.txt",true);
		HttpReader reader = new StaticHttpReader();
		Set<String> locationSet = getLocation();
//		Set<String> locationSet = new HashSet<String>();
//		locationSet.add("德阳市");
		for (String loc : locationSet) {
			int locCount = 0;
//			if (count ==1) break;
			String location = loc;
			String prefix = "http://dataapi.fellowplus.com/project/list?_id=12463&_token=uBUUafkzqIDKT10IErz5gTQb%2F%2FngqVigptOimBB157s%3D&filter_content=%7B%22location%22:%7B%22locations%22:%5B%22"+URLEncoder.encode(location,"utf-8")+"%22%5D%7D%7D&limit="+limit+"&order_by=-last_round_created_at&start=";
			String source = reader.readSource(prefix + 0);
			JSONObject obj = JSONObject.parseObject(source);
			int total = obj.getIntValue("total");
			if (total==0) {
				hw.write(location+"\r\n");
				continue;
			} else if (total > 5000) {
				System.out.println("> 5000" + location);
				continue;
			} else {
				hw.write(location+"\r\n");
				JSONArray arr = obj.getJSONArray("projects");
				for (Object object : arr) {
					String s = object.toString();
					fw.write(s.toString()+"\r\n");
					fw.flush();
					locCount++;
				}
				System.out.println(location);
				TimeUnit.SECONDS.sleep(5);
				int size = total/limit + 1;
				for (int i = 1; i < size; i++) {
					int startNum = i*limit;
//					System.out.println(startNum);
					String url = prefix + startNum;
					source = reader.readSource(url);
					obj =JSONObject.parseObject(source);
					arr = obj.getJSONArray("projects");
					for (Object object : arr) {
						String s = object.toString();
						fw.write(s.toString()+"\r\n");
						fw.flush();
						locCount++;
					}
					TimeUnit.SECONDS.sleep(5);
				}
			}
			System.out.println(locCount);
		}
		fw.close();
		hw.close();
		reader.close();
	}
	
	public static Set<String> getLocation() throws IOException {
		Set<String> locationSet = new HashSet<String>();
		Set<String> dedupSet = new HashSet<String>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/dp/location.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			locationSet.add(input.trim());
		}
		br = BufferedReaderUtil.getBuffer("data/dp/hw.txt");
		while ((input = br.readLine())!=null) {
			dedupSet.add(input);
		}
		locationSet.removeAll(dedupSet);
		br.close();
		return locationSet;
	}
	
	public static Set<String> getOriLocation() throws IOException {
		Set<String> locationSet = new HashSet<String>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/dp/location.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			locationSet.add(input.trim());
		}
		br.close();
		return locationSet;
	}
	
	
	
	public static Set<String> getDomain() throws Exception {
		Set<String> domainSet = new HashSet<String>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/dp/domain.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			domainSet.add(input.trim());
		}
		br.close();
		return domainSet;
	}
	
	public static Set<String> getFinance() throws Exception {
		Set<String> financeSet = new HashSet<String>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/dp/finance.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			financeSet.add(input.trim());
		}
		br.close();
		return financeSet;
	}
	
}
