package com.hiekn.main;

import java.io.BufferedReader;
import java.io.IOException;

import org.bson.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ZhiDaoComCrawler {
	
	public static void main(String[] args)  {
		try {
			run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void run() throws IOException, InterruptedException {
		MongoClient client = new MongoClient("127.0.0.1",27017);
		MongoDatabase db = client.getDatabase("zhidao");
		MongoCollection<Document> collection = db.getCollection("company");
		BufferedReader br = BufferedReaderUtil.getBuffer("data/rest_com.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			String keyword = input.split("\t")[0];
			String id = input.split("\t")[1];
			String url = search(keyword);
			if (url.equals("error")) {
				Thread.sleep(3000);
				continue;
			} else if (url.equals("often")) {
				System.out.println("too often");
				Thread.sleep(60000);
				continue;
			} else if (url.equals("invalid")) {
				System.out.println("account invalid");
				break;
			}
			String source = readSource(url);
			if (source.equals("error")) break;
			String result = parse(source);
			Document doc = new Document("company", keyword)
								.append("url", url)
								.append("id", id)
								.append("source",result);
			collection.insertOne(doc);
			System.out.println("insert successfully");
		}
		br.close();
		client.close();
	}
	
	public static String parse(String source) {
		String result = "";
		org.jsoup.nodes.Document doc = Jsoup.parse(source);
		try {
			Element resultE = doc.select("div.zdmain > div.am-container").get(1);
			result = resultE.html();
		} catch (Exception e) {
			// TODO: handle exception
			result = "error";
		}
		return result;
	}
	
	public static String search(String keyword) {
		
		String url = "";
		String searchUrl = "http://zhidaoii.com/zdsearch/?type=org&q="+keyword;
		String source = readSource(searchUrl);
		if (source.contains("阁下的搜索太过频繁")) {
			url = "often";
			return url;
		} else if (source.contains("查看搜索结果详情请")) {
			url = "invalid";
			return url;
		}
		org.jsoup.nodes.Document doc = Jsoup.parse(source);
		try {
			Elements searchElement = doc.select("div#search-results > div.zd-post-list > h3 > a");
			for (Element element : searchElement) {
				String searchCompany = element.text().trim();
				searchCompany = searchCompany.replace("（", "(").replace("）", ")");
				if (searchCompany.equals(keyword)) {
					url = "http://zhidaoii.com/"+searchElement.attr("href").trim();
					break;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			url = "error";
		}
		return url;
	}
	
	public static String readSource(String url) {
//		String cookie = "sessionid=ovambossm939qbbgyvclou29qq3ccley;Hm_lvt_a6ea18bf17eb00508321029416301068=1467701652;Hm_lpvt_a6ea18bf17eb00508321029416301068=1467703879;csrftoken=nXqsohDOiczHe98oeAUTmnxHoY4w4fjo;";
		String cookie = "sessionid=gpd7q4oiigk3a01myvw025vkp38ihf9a;bdshare_firstime=1467793944252;Hm_lvt_a6ea18bf17eb00508321029416301068=1467706352,1467790385,1467796045,1467796094;Hm_lpvt_a6ea18bf17eb00508321029416301068=1467797125;csrftoken=l3jdzMeuK51cNv35ar1lOoJsLtveufvl;";
		String source = "";
		HttpReader reader = new StaticHttpReader();
		source = reader.readSource(url,"",cookie);
		reader.close();
		return source;
	}
}
