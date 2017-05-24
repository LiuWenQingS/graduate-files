package com.hiekn.main;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.bson.Document;

import com.hiekn.util.BufferedReaderUtil;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;


public class StartKrCompany {
	public static void main(String[] args) {
		
		ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(11);
		List<String> urlList = null;
		try {
			urlList = getUrlList();
			System.out.println("get success");
		} catch (Exception e) {
			e.printStackTrace();
		}
		int size = urlList.size() / 10;
		for (int i = 0; i < 10; i++) {
			List<String> subList = urlList.subList(i*size, (i+1)*size);
			pool.submit(new KrCompany(subList));
		}
	}
	
	public static List<String> getUrlList() throws Exception {
		Set<String> urlSet = new HashSet<String>();
		List<String> urlList = new ArrayList<String>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/kr/all.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			urlSet.add(input);
		}
		MongoClient client = new MongoClient("127.0.0.1",27017);
		MongoDatabase db = client.getDatabase("kr_company");
		MongoCollection<Document> collection = db.getCollection("company");
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				String url = doc.getString("url");
				urlSet.remove(url);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		br.close();
		client.close();
		urlList.addAll(urlSet);
		return urlList;
	}
}
