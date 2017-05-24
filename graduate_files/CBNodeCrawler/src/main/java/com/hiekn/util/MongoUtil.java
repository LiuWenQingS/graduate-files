package com.hiekn.util;

import java.io.FileWriter;
import java.io.IOException;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;


public class MongoUtil {
	public static void main(String[] args) {
		try {
			getData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void getData() throws IOException {
		FileWriter fw = new FileWriter("data/origin.txt");
		MongoClient client = new MongoClient("192.168.1.150",27017);
		System.out.println("connection success");
		MongoDatabase db = client.getDatabase("data");
		MongoCollection<Document> collection = db.getCollection("itjz");
		System.out.println("get connection success");
		MongoCursor<Document> cursor = collection.find().iterator();
		while (cursor.hasNext()) {
			fw.write(cursor.next().toJson()+"\r\n");
			fw.flush();
		}
		System.out.println("write finish");
		cursor.close();
		client.close();
		fw.close();
	}
}
