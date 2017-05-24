package com.hiekn.main;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.jsoup.Jsoup;

import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class KrNewsCrawler extends Thread{
	
	static final Logger log = Logger.getLogger(KrNewsCrawler.class);
	
	private int startNum;
	
	public KrNewsCrawler(int startNum) {
		this.startNum = startNum;
	}
	
	public void run() {
		try {
			crawler(startNum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void crawler(int startNum) throws Exception {
		log.info(startNum);
		MongoClient client = new MongoClient("127.0.0.1",27017);
		MongoDatabase db = client.getDatabase("kr_news");
		MongoCollection<Document> collection = db.getCollection("news");
		HttpReader reader = new StaticHttpReader();
		for (int i = startNum; i < startNum + 49000; i++) {
			String url = "http://36kr.com/p/"+i+".html";
			log.info(url);
			String cookie = "aliyungf_tc=AQAAAGusGkOQ9wYAwHpRZXiK/WSRxExM;ADHOC_MEMBERSHIP_CLIENT_ID=33945995-96b5-a0ff-c8a5-b8fee6a4e431;kwlo_iv=4h;gr_user_id=2567ad4e-e438-42da-9805-5154b57055e0;kr_stat_uuid=QPiay24453212;_kr_p_se=4314c892-9c69-4c04-b677-2ed134dee8b2;krid_user_id=926045;krid_user_version=1001;kr_plus_id=436942459;kr_plus_token=dlnony_mU2XaT46CB_xg7oz7HzgKQh87DM892___;kr_plus_utype=0;Z-XSRF-TOKEN=eyJpdiI6IlNva0pRbmc2OVg2V21RRlwvMDVXekVBPT0iLCJ2YWx1ZSI6Ik1IQlIzdkJsa1FScmN3VGJaU1RrU1NDUlwvelIySTlXSWVwQVRacVF5V3RpTG1EZW8yeW5lVlNmK3NZbkl6dEc3RFFRVWdFdEdXQTZGTk56cFplaTVnQT09IiwibWFjIjoiYWVlNzg2Y2QxMTkyZDkyMzcwODA3MWY1MDNmZWM5OWQ2ZDgxZmQxNzVhNmRhMDlkOGI3ZmQ0YmUyOTJhY2E0ZSJ9;krchoasss=eyJpdiI6Im9yT1pyQ3lZM1VGYW1tR29EaFhocWc9PSIsInZhbHVlIjoiTjVCVXJnZEIrTEs3ZGpscUdNNG9NQjhKY3JycE5qaVFHREw0UGFHTzVmdlJBYkRUUkcxYzBlbkdiN09OaVwvcW1STW52VVN6NTVZMktOYk1HY1lISGNnPT0iLCJtYWMiOiJkMjZhZWY3YTQyODkyZTQ3YTJmZDc4ZmFlMmRhNDYyMjEzZjY0NjYyODJlOTBiOTU5MWI1Y2RiNGU3Mjc0YmMzIn0%3D;autoFresh.newsflashes.kr36=true;Hm_lvt_e8ec47088ed7458ec32cde3617b23ee3=1467190153,1467190487,1467192697,1467192804;Hm_lpvt_e8ec47088ed7458ec32cde3617b23ee3=1467363494;_ga=GA1.2.718107563.1467337400;gr_session_id_76d36bd044527820a1787b198651e2f1=f043096c-c282-424e-9afd-fff82ce575fe;Hm_lvt_713123c60a0e86982326bae1a51083e1=1466818521,1466826336,1467190129,1467190699;Hm_lpvt_713123c60a0e86982326bae1a51083e1=1467364498;_krypton_session=QXY1cmdPSTNMRmcrM2ZrY2pRVWZqU0ErbW04WXRURld4RUVrQitLYjRmTEp0NGw1ZlJNU2ZLQk9ZODhva2ttK0d3NmlvVFlOOVYxc1VCeDZ5eUpxZENHaFhVVk40RDRJZUQxZTBmdUpYSUNQSmlvelpxUDU0KzR6bktkUTJyTVVIN3hyazVYa3pSK2NHN0RkQjZWR1hIZEZSbEdqTlpQY1VCb1RuWTBDeGxaZkFLd0VhM3FiY2JlSHhNZEtkbWtIeXhjZjFMWVU1a1V2S2xrUVIvNFJFeDBRei9CRlFtdnFua0Vkak5uVEt4dld5a09tOTd2MnM2ODdvV3VIT3huK3U3czVOWUxwMmQzLysxbW84RkRYME9QYTJmd2FMRjJmb2k3TUw4b3BpQ1U9LS1DemYrM2RWR1grK1U2emk1dTIzK0FBPT0%3D--c6a4481ba5ae9b6d99dd9ebe9530df996f87e444;c_name=point;";
			String source = reader.readSource(url,"", cookie);
			if (!source.equals("")) {
				log.info(url);
				org.jsoup.nodes.Document doc = Jsoup.parse(source);
				try {
					String content = doc.select("div.js-react-on-rails-component").first().attr("data-props");
					Document bDoc = new Document("url",url)
										.append("content", content);
					collection.insertOne(bDoc);
				} catch (Exception e) {
					log.error(url+e);
				}
			}
//			Thread.sleep(2000);
		}
		reader.close();
		client.close();
	}
	
}
