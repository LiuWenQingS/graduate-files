package com.hiekn.other;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class CountSearchWord {
	public static void main(String[] args) {
		count("data/count.txt");
	}
	
	public static void count(String path) {
		int cc = 0;
		FileWriter fw;
		try {
			fw = new FileWriter(path);
			Map<String,Integer> countMap = new HashMap<String,Integer>();
			MongoClient client = new MongoClient("192.168.2.158",19130);
			MongoDatabase db = client.getDatabase("data");
			MongoCollection<Document> collection = db.getCollection("news_data");
			MongoCursor<Document> cursor = collection.find().iterator();
			try {
				while (cursor.hasNext()) {
					System.out.println(cc++);
					if (cc++ > 1009) break;
					Document doc = cursor.next();
					String searchWord = doc.getString("search_word");
					if (countMap.containsKey(searchWord)) {
						countMap.put(searchWord, countMap.get(searchWord) + 1);
					} else {
						countMap.put(searchWord, 1);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			for (Entry<String,Integer> entry : countMap.entrySet()) {
				fw.write(entry.getKey() + "\t" + entry.getValue() + "\r\n");
				fw.flush();
			}
			client.close();
			fw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
