package com.hiekn.other;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class NewsImportMongo {
	public static void main(String[] args) throws Exception{
		huxiuImport();
		krImport();
	}
	
	@Test
	public void connectiontest() {
		MongoClient client = new MongoClient("poa.hiekn.com",19170);
		if (client !=null) {
			System.out.println("scff");
		}
		client.close();
	}
	
	public static void huxiuImport() {
		String input = "";
		MongoClient tarclient = new MongoClient("poa.hiekn.com",19170);
		MongoDatabase tardb = tarclient.getDatabase("data");
		MongoCollection<Document> tarcollection = tardb.getCollection("news_data");
		List<Document> bList = new ArrayList<Document>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/news/hx_news.txt");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = df.format(new Date());
		try {
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				Document doc = new Document("url", obj.getString("url"))
				.append("title", obj.getString("title"))
				.append("content", obj.getString("content"))
				.append("author", obj.getString("author"))
				.append("source", "虎嗅")
				.append("publish_time", obj.getString("publishTime"))
				.append("publish_date", obj.getString("publishDate"))
				.append("portal_id", "")
				.append("channel", "")
				.append("source_group","1000100000000")
				.append("persist_time_mills", System.currentTimeMillis())
				.append("presist_date", now)
				.append("crawl_time", now)
				.append("search_word", "")
				.append("sentiment", -1)// 4负面 5中性 6中性
				.append("simhash", -1L)// 
				.append("upsert", -1)// 1表示新增 0表示重复
				.append("keywords", 0)// 1表示已经计算抽取 0表示没有计算抽取 
				.append("indexed", 0);// 1表示已经索引 0表示还没有索引
				bList.add(doc);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		tarcollection.insertMany(bList);
		System.out.println("insert success");
		tarclient.close();
	}
	
	public static void krImport() {
		String input = "";
		MongoClient tarclient = new MongoClient("poa.hiekn.com",19170);
		MongoDatabase tardb = tarclient.getDatabase("data");
		MongoCollection<Document> tarcollection = tardb.getCollection("news_data");
		List<Document> bList = new ArrayList<Document>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/news/36kr_news.txt");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = df.format(new Date());
		try {
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				Document doc = new Document("url", obj.getString("url"))
				.append("title", obj.getString("title"))
				.append("content", obj.getString("content"))
				.append("author", obj.getString("author"))
				.append("source", "36kr")
				.append("publish_time", obj.getString("publishTime"))
				.append("publish_date", obj.getString("publishDate"))
				.append("portal_id", "")
				.append("channel", "")
				.append("source_group","1000100000000")
				.append("persist_time_mills", System.currentTimeMillis())
				.append("presist_date", now)
				.append("crawl_time", now)
				.append("search_word", "")
				.append("sentiment", -1)// 4负面 5中性 6中性
				.append("simhash", -1L)// 
				.append("upsert", -1)// 1表示新增 0表示重复
				.append("keywords", 0)// 1表示已经计算抽取 0表示没有计算抽取 
				.append("indexed", 0);// 1表示已经索引 0表示还没有索引
				bList.add(doc);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		tarcollection.insertMany(bList);
		System.out.println("insert success");
		tarclient.close();
	}
}
