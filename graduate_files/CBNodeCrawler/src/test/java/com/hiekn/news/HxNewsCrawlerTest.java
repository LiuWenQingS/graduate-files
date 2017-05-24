package com.hiekn.news;

import org.bson.Document;
import org.jsoup.Jsoup;
import org.junit.Test;

import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;
import com.hiekn.util.StringTimeUtils;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class HxNewsCrawlerTest {
	
	@Test
	public void test() throws InterruptedException {
		MongoClient client = new MongoClient("127.0.0.1",27017);
		MongoDatabase db = client.getDatabase("hx_news");
		MongoCollection<Document> collection = db.getCollection("news");
		HttpReader reader = new StaticHttpReader();
		String url = "http://www.huxiu.com/article/1/1.html";
		String source = reader.readSource(url);
		if (!source.contains("您要找的文章不存在")) {
			org.jsoup.nodes.Document doc = Jsoup.parse(source);
			try {
				String content = doc.select("div#article_content").first().html();
				String title = doc.select("h1.t-h1").first().text();
				String author = doc.select("span.author-name").first().text();
				String publishTime = doc.select("span.article-time").first().text();
				publishTime = StringTimeUtils.formatStringTime(publishTime);
				Document bDoc = new Document("url", url)
									.append("title", title)
									.append("author", author)
									.append("publish_time", publishTime)
									.append("content", content);
				collection.insertOne(bDoc);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		reader.close();
		client.close();
	}
}
