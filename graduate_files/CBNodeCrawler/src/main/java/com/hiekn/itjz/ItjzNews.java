package com.hiekn.itjz;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.hiekn.bean.ItjzNewsBean;
import com.hiekn.html.CommonHtmlParser;
import com.hiekn.html.implement.CommonHtmlParserImpl;
import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;

public class ItjzNews {
	
	private static HttpReader reader = new StaticHttpReader();
	private static CommonHtmlParser htmlParser = new CommonHtmlParserImpl();
	
	public static void main(String[] args) {
		try {
			crawl();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void crawl() throws Exception {
		FileWriter fw = new FileWriter("data/itjz_news.txt",true);
		for (int i = 398; i < 5365; i++) {
			System.out.println("processing page " + i);
			String url = "https://www.itjuzi.com/dailynews?page=" + i;
			String ulSource = "";
			try {
				ulSource = reader.readSource(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (ulSource.equals("")) continue;
			Elements ulEle = Jsoup.parse(ulSource).select("ul.list-timeline > li");
			for (Element element : ulEle) {
				ItjzNewsBean nb = new ItjzNewsBean();
				String itjzUrl = element.select("span.title > a").attr("href");
				List<String> tagList = new ArrayList<String>();
				Elements tagEle = element.select("span.scopes > a");
				if (tagEle != null) {
					for (Element element2 : tagEle) {
						tagList.add(element2.text());
					}
				}
				nb.setTagList(tagList);
				String title = element.select("span.title").text();
				if (!title.equals("")) {
					nb.setTitle(title);
				}
				if (!itjzUrl.equals("")) {
					nb.setItjzUrl(itjzUrl);
					String itjzSource = reader.readSource(itjzUrl);
					String newsUrl = Jsoup.parse(itjzSource).select("div.news_box > iframe").attr("src");
					nb.setUrl(newsUrl);
					String newsSource = reader.readSource(newsUrl);
					String content = htmlParser.getContentHtml(title, newsSource);
					if (!content.equals("") && content != null) {
						nb.setContent(content);
					}
				}
				fw.write(JSON.toJSONString(nb) + "\r\n");
				fw.flush();
			}
		}
		fw.close();
	}
	
}
