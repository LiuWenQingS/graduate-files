package com.hiekn.other;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.DownloadImageUtil;
import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;

public class DangDangCrawler {
	public static void main(String[] args) {
		try {
			crawler();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void crawler() throws Exception {
		HttpReader reader = new StaticHttpReader();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/dd/entity.txt");
		FileWriter fw = new FileWriter("data/dd/book_info.txt");
		String input = "";
		long bookId = 0;
		while ((input = br.readLine())!=null) {
			String[] split = input.split("\t");
			String id = split[0];
			System.out.println(split[1]);
			String url = "http://search.dangdang.com/?key=" + URLEncoder.encode(split[1]) + "&act=input";
			String source = reader.readSource(url);
			Document doc = Jsoup.parse(source);
			Elements booksEle = doc.select("ul#component_0__0__6612 > li");
			for (Element element : booksEle) {
				JSONObject obj = new JSONObject();
				obj.put("bookId", bookId);
				obj.put("type", id);
				String bookUrl = element.select("a").attr("href");
				String bookSource = reader.readSource(bookUrl);
				doc = Jsoup.parse(bookSource);
				String imageUrl = doc.select("img#largePic").attr("src");
				DownloadImageUtil.downloadPic(imageUrl, id + "_"+bookId++ + ".jpg", "data/dd/");
				String bookName = doc.select("div.name_info > h1").attr("title");
				if (bookName.equals("")) continue;
				obj.put("bookUrl", bookUrl);
				obj.put("bookName", bookName);
				String author = doc.select("span#author").text();
				obj.put("author", author.replace("作者:", ""));
				String publish = "";
				String publishT = "";
				try {
					publish = doc.select("div.messbox_info > span.t1").get(1).text();
					publishT = doc.select("div.messbox_info > span.t1").get(2).text();
				} catch (Exception e) {
					e.printStackTrace();
				}
				obj.put("publish", publish.replace("出版社:", ""));
				obj.put("time", publishT.replace("出版时间:", ""));
				Elements isbnEle = doc.select("div#detail_describe > ul > li");
				for (Element element2 : isbnEle) {
					String txt = element2.text();
					String[] splitInfo = txt.split("：");
					String key = splitInfo[0];
					if (key.equals("国际标准书号ISBN") && splitInfo.length > 1) {
						obj.put("isbn", splitInfo[1]);
					} else if (key.equals("页 数") && splitInfo.length > 1) {
						obj.put("page", splitInfo[1]);
					}
					
				}
				fw.write(obj.toJSONString() + "\r\n");
				fw.flush();
			}
		}
		br.close();
		fw.close();
		reader.close();
	}
	
}
