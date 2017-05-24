package com.hiekn.other;

import java.util.HashMap;
import java.util.Map;

import org.bson.Document;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoTest {
	@Test
	public void t1() {
		MongoClient client = new MongoClient("192.168.1.31",27017);
		MongoDatabase db = client.getDatabase("test");
		MongoCollection<Document> collection = db.getCollection("test");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("id", 5);
		Document $find = new Document(map);
		Document doc = collection.find($find).first();
		System.out.println("sdfs");
		client.close();
	}
}
