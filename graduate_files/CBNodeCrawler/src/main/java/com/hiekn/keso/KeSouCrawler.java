package com.hiekn.keso;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hiekn.util.BufferedReaderUtil;

public class KeSouCrawler {
	public static void main(String[] args) {
		try {
			long start = System.currentTimeMillis();
//			test();
			process();
			long finish = System.currentTimeMillis() - start;
			System.out.println(finish);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	public static void process() throws Exception {
		FileWriter fw = new FileWriter("data/cnki/keso_all.txt");	
		OkHttpClient client = new OkHttpClient();
//		Set<String> peoSet = new HashSet<String>();
//		peoSet.add("梅凤翔");
		Set<String> peoSet = getAllPeo();
		for (String search : peoSet) {
			JSONObject obj = new JSONObject();
			JSONArray paperArr = new JSONArray();
			RequestBody formBody = new FormBody.Builder()
			.add("core", "people").add("query", search).add("org", "北京理工大学")
			.build();
			Request request = new Request.Builder()
			.url("http://kejso.com/servlet/SearchServlet")
			.post(formBody)
			.build();
			Response response = client.newCall(request).execute();
			String source = response.body().string();
			Document doc = Jsoup.parse(source);
			Elements elements = doc.select("div.span7.column");
			for (Element element : elements) {
				String txt = element.text();
				if (txt.contains("所在单位")) {
					obj.put("school", txt);
					System.out.println(txt);
				} else if (txt.contains("职位")) {
					obj.put("position", txt);
					System.out.println(txt);
				}
			}
			int start = 0;
			while (true) {
				JSONArray pa = getPaperMoreInfo(search, start + "");
				paperArr.add(pa);
				start = start + 10;
				if (pa.size() < 10) {
					paperArr.add(pa);
					break;
				}
				TimeUnit.SECONDS.sleep(2);
			}
			obj.put("paper", paperArr);
			obj.put("name", search);
			fw.write(JSON.toJSONString(obj,SerializerFeature.DisableCircularReferenceDetect) + "\r\n");
			fw.flush();
			TimeUnit.SECONDS.sleep(1);
		}
		System.out.println();
		fw.close();
	}
	
	public static JSONArray getPaperMoreInfo(String search,String start) throws IOException {
		System.out.println(search + "\t" + start + "页");
		OkHttpClient client = new OkHttpClient();
		RequestBody formBody = new FormBody.Builder()
		.add("start", start).add("name", search).add("org", "北京理工大学").add("which", "paper")
		.build();
		Request request = new Request.Builder()
		.url("http://kejso.com/servlet/PeopleMoreInfo")
		.post(formBody)
		.build();
		
		Response response = client.newCall(request).execute();
		String source = response.body().string();
		JSONArray arr = JSONObject.parseObject(source).getJSONArray("re");
		return arr;
	}
	
	/**
	 * 获得所有人物
	 * @return
	 * @throws Exception
	 */
	public static Set<String> getAllPeo() throws Exception {
		Set<String> peoSet = new HashSet<String>();
		Set<String> hasSet = new HashSet<String>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/bit_people_all.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			peoSet.add(input.split("\t")[1]);
		}
		System.out.println(peoSet.size());
		br = BufferedReaderUtil.getBuffer("data/cnki/bit_people.txt");
		while ((input = br.readLine())!=null) {
			hasSet.add(input);
		}
		peoSet.removeAll(hasSet);
		System.out.println(peoSet.size());
		br.close();
		return peoSet;
	}
}
