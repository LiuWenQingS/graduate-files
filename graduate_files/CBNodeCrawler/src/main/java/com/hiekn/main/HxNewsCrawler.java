package com.hiekn.main;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.jsoup.Jsoup;

import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;
import com.hiekn.util.StringTimeUtils;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


public class HxNewsCrawler implements Runnable{
	
	static Logger log = Logger.getLogger(HxNewsCrawler.class);
	
	private int startNum;
	
	public HxNewsCrawler(int startNum) {
		this.startNum = startNum;
	}
	
	public void crawler(int startNum) {
		log.info(startNum);
		MongoClient client = new MongoClient("127.0.0.1",27017);
		MongoDatabase db = client.getDatabase("hx_news");
		MongoCollection<Document> collection = db.getCollection("news");
		HttpReader reader = new StaticHttpReader();
		for (int i = startNum + 24000; i < startNum + 40000; i++) {
			String url = "http://www.huxiu.com/article/"+i+"/1.html";
			String cookie = "screen=%7B%22w%22%3A1920%2C%22h%22%3A1080%2C%22d%22%3A1%7D;screen=%7B%22w%22%3A1920%2C%22h%22%3A1080%2C%22d%22%3A1%7D;aliyungf_tc=AQAAAClxt1r9hgYAwHpRZVJ/v/SMySV0;PHPSESSID=pf533mpf3j9tkrlvgb6cu22lf6;huxiu_analyzer_wcy_id=hje2r0sokt7ke14g5kt0;_uab_collina=146681996258309964864124;_gat=1;screen=%7B%22w%22%3A1920%2C%22h%22%3A1080%2C%22d%22%3A1%7D;show_view_aid=154711;_ga=GA1.2.6902906.1466819962;Hm_lvt_324368ef52596457d064ca5db8c6618e=1467428109,1467431536,1467438602,1467444175;Hm_lpvt_324368ef52596457d064ca5db8c6618e=1467596784;_umdata=C234BF9D3AFA6FE79D27F503C5C38EB11E3F1621D4ADF3F7D31A367A559C9B9B93508D4158F0ADBFD29E66CBA64C187798B6A20C75E9D0325C78044F6DBDBBC06F2CC75DBFA9B8E27365CC762E8CAD2E83DCC03112561AA338F8634DE19064AF;";
			String source = reader.readSource(url,"",cookie);
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
		}
		reader.close();
		client.close();
	}
	
	public void run() {
		// TODO Auto-generated method stub
		crawler(startNum);
	}
	
}
