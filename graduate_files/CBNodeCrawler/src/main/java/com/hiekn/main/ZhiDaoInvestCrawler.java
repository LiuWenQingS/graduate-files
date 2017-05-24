package com.hiekn.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ZhiDaoInvestCrawler {
	
	public static void main(String[] args)  {
		try {
			run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void run() throws IOException {
		MongoClient client = new MongoClient("127.0.0.1",27017);
		MongoDatabase db = client.getDatabase("zhidao");
		MongoCollection<org.bson.Document> collection = db.getCollection("invest");
		List<Map<String,String> > urlList = new ArrayList<Map<String,String> >();
		for (int i = 1; i < 109; i++) {
			System.out.println(i);
			urlList = search(i);
			for (Map<String,String> map : urlList) {
				String url = map.get("url");
				String name = map.get("name");
				String source = readSource(url);
				String result = parse(source);
				org.bson.Document doc = new org.bson.Document("url",url)
											.append("name", name)
											.append("source", result);
				collection.insertOne(doc);
			}
			urlList.clear();
		}
		client.close();
	}
	
	public static String parse(String source) {
		String result = "";
		org.jsoup.nodes.Document doc = Jsoup.parse(source);
		try {
			Element resultE = doc.select("div.zdmain > div.am-container").get(1);
			result = resultE.html();
		} catch (Exception e) {
			// TODO: handle exception
			result = "error";
		}
		return result;
	}
	
	public static List<Map<String,String>> search(int i) {
		List<Map<String,String> > urlList = new ArrayList<Map<String,String> >();
		String url = "http://zhidaoii.com/investorg/?page="+i;
		String source = readSource(url);
		Document doc = Jsoup.parse(source);
		Elements urlE = doc.select("div.zd-post-list > table > tbody > tr > td > h2 > a");
		for (Element element : urlE) {
			Map<String,String> map = new HashMap<String,String>();
			String eleUrl = "http://zhidaoii.com/"+ element.attr("href");
			map.put("url", eleUrl);
			map.put("name", element.text());
			urlList.add(map);
		}
		return urlList;
	}
	
	public static String readSource(String url) {
		String cookie = "sessionid=ovambossm939qbbgyvclou29qq3ccley;Hm_lvt_a6ea18bf17eb00508321029416301068=1467701652;Hm_lpvt_a6ea18bf17eb00508321029416301068=1467703879;csrftoken=nXqsohDOiczHe98oeAUTmnxHoY4w4fjo;";
//		String cookie = "sessionid=pdaju5rx15fzbnwvn7qi0fosn5hakzmq; Hm_lvt_a6ea18bf17eb00508321029416301068=1467704519; Hm_lpvt_a6ea18bf17eb00508321029416301068=1467706061; csrftoken=VeSkwyX8F5MJqEk6LewDavfYuhCQ6UGz";
		String source = "";
		HttpReader reader = new StaticHttpReader();
		source = reader.readSource(url,"",cookie);
		reader.close();
		return source;
	}
}
