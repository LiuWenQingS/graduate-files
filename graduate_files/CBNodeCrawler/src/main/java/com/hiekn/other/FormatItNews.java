package com.hiekn.other;

import org.bson.Document;

import com.hiekn.util.StringTimeUtils;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class FormatItNews {
	public static void main(String[] args) {
		MongoClient client = new MongoClient("127.0.0.1",27017);
		MongoDatabase db = client.getDatabase("it_news");
		MongoCollection<Document> collectionOri = db.getCollection("news_data_ori");
		MongoCollection<Document> collection = db.getCollection("news_data");
		MongoCursor<Document> cursor = collectionOri.find().iterator();
		try {
			while (cursor.hasNext()) {
				Document doc = new Document();
				Document objOri = cursor.next();
				String companyName = objOri.getString("company_name");
				String source = objOri.getString("news_from");
				String title = objOri.getString("news_title");
				String time = StringTimeUtils.formatStringTime(objOri.getString("news_time"));
				String tag = objOri.getString("news_tag");
				String content = objOri.getString("content");
				doc.append("company_name", companyName).append("news_from", source).append("news_title", title)
					.append("news_time", time).append("news_tag", tag).append("content", content);
				collection.insertOne(doc);
			}
		} catch (Exception e) {
			// TODO: handle exception
			client.close();
		}
		client.close();
	}
	
	
	
}
