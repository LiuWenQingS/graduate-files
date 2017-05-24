package com.hiekn.other;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.Document;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class NewsDedup {
	public static void main(String[] args) {
		HxExtract();
		KrExtract();
	}
	
	public static void HxExtract() {
		int count = 0;
		List<Document> dedupList = new ArrayList<Document>();
		Set<String> urlSet = new HashSet<String>();
		MongoClient client = new MongoClient("127.0.0.1",27017);
		MongoDatabase db = client.getDatabase("hx_news");
		MongoCollection<Document> collection = db.getCollection("news");
		MongoCollection<Document> dCollection = db.getCollection("dedup_news");
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()) {
				Document docCursor = cursor.next();
				String url = docCursor.getString("url");
				if (urlSet.contains(url)) {
					System.out.println(count++);
					continue;
				} else {
					dedupList.add(docCursor);
					urlSet.add(url);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			cursor.close();
		}
		dCollection.insertMany(dedupList);
		client.close();
	}
	
	public static void KrExtract() {
		int count = 0;
		List<Document> dedupList = new ArrayList<Document>();
		Set<String> urlSet = new HashSet<String>();
		MongoClient client = new MongoClient("127.0.0.1",27017);
		MongoDatabase db = client.getDatabase("kr_news");
		MongoCollection<Document> collection = db.getCollection("news");
		MongoCollection<Document> dCollection = db.getCollection("dedup_news");
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()) {
				Document docCursor = cursor.next();
				String url = docCursor.getString("url");
				if (urlSet.contains(url)) {
					System.out.println(count++);
					continue;
				} else {
					Document dedupDoc = new Document("url",url);
					JSONObject obj = JSONObject.parseObject(docCursor.getString("content"));
					JSONObject dataObj = (JSONObject) JSONObject.parseObject(obj.getString("data")).get("post");
					for (String key : dataObj.keySet()) {
						dedupDoc.append(key, dataObj.get(key));
					}
					dedupList.add(dedupDoc);
					urlSet.add(url);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			cursor.close();
		}
		dCollection.insertMany(dedupList);
		client.close();
	}
}
