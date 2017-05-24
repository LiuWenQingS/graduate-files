package com.hiekn.main;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;

public class ItjzTzjgCrawler {
	
	public static void main(String[] args) throws Exception{
		int count = 0;
		FileWriter fw = new FileWriter("data/invest_new_data.txt");
		String input = "";
		BufferedReader br = BufferedReaderUtil.getBuffer("data/invest_ent_no.txt");
		while ((input = br.readLine())!=null) {
			JSONObject obj = JSONObject.parseObject(input);
			String keyword = obj.getString("name");
			Long kgId = obj.getLong("kgId");
			System.out.println(keyword + " " + count++);
			String url = search(keyword);
			if (url.equals("error")) continue;
			JSONObject result = parse(url);
			result.put("kgId", kgId);
			fw.write(result.toJSONString()+"\r\n");
			fw.flush();
		}
		br.close();
		fw.close();
	}
	
	@Test
	public void parTest() {
		parse("https://www.itjuzi.com/investfirm/1765");
	}
	
	public static JSONObject parse(String url) {
		HttpReader reader = new StaticHttpReader();
		String source = reader.readSource(url);
		JSONObject obj = new JSONObject();
		Document doc = Jsoup.parse(source);
		try {
			String name = doc.select("div.picinfo > p > span.title").first().text();
			obj.put("name", name);
			String link = doc.select("div.picinfo > p > span.links").first().text().replace("网址：", "");
			obj.put("link", link);
			String abs = doc.select("div.block-inc-info > div.des").first().text();
			obj.put("abstract", abs);
			List<String> tagList = new ArrayList<String>();
			Elements tagE = doc.select("div.list-tags").get(0).select("a");
			for (Element element : tagE) {
				String tag = element.text();
				tagList.add(tag);
			}
			obj.put("tag", tagList);
			List<String> roundList = new ArrayList<String>();
			Elements roundE = doc.select("div.list-tags").get(1).select("a");
			for (Element element : roundE) {
				String round = element.text();
				roundList.add(round);
			}	
			obj.put("round", roundList);
			List<JSONObject> memList = new ArrayList<JSONObject>();
			Elements memE = doc.select("ul.list-prodcase").get(0).select("li");
			for (Element element : memE) {
				JSONObject memObj = new JSONObject();
				String memName = element.select("h4.person-name > b").first().ownText();
				memObj.put("name", memName);
				String pos = element.select("h4.person-name > b > span.c-gray").first().text();
				memObj.put("pos", pos);
				List<String> memTagList = new ArrayList<String>();
				Elements tagME = element.select("p.person-tags > span.tag");
				for (Element element2 : tagME) {
					String tagM = element2.text();
					memTagList.add(tagM);
				}
				memObj.put("tag", memTagList);
				memList.add(memObj);
			}
			obj.put("mem", memList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		reader.close();
		return obj;
	}
	
	public static String search(String keyword) throws InterruptedException {
		String url = "";
		String searchUrl = "https://www.itjuzi.com/search?type=juzi_investfirm&key="+keyword;
		String source = readSource(searchUrl);
		Document doc = Jsoup.parse(source);
		try {
			String name = doc.select("ul#the_search_list > li > a > h4").first().text();
			String type = doc.select("ul#the_search_list > li > a > p > span.tag").first().text();
			if (name.equals(keyword) && type.equals("投资机构")) {
				url = doc.select("ul#the_search_list > li > a").attr("href");
			}
		} catch (Exception e) {
			// TODO: handle exception
			url = "error";
		}
		Thread.sleep(2000);
		return url;
	}
	
	public static String readSource(String url) {
		String cookie = "acw_tc=AQAAACrqrFXnIwUAwHpRZTHvJ5EvB2+2;gr_user_id=8b976033-64fe-4861-8ff7-099b7fe55b46;session=637589fbf59030648d1b37af0047381ea78e75b2;identity=lichunxiao%40hiekn.com;remember_code=KIPWzhrEZW;_ga=GA1.2.541892866.1466752277;Hm_lvt_1c587ad486cdb6b962e94fc2002edf89=1466752277;Hm_lpvt_1c587ad486cdb6b962e94fc2002edf89=1467770455;gr_session_id_eee5a46c52000d401f969f4535bdaa78=429b2d4c-3f29-4090-8306-6cbd53a29761;";
		String source = "";
		HttpReader reader = new StaticHttpReader();
		source = reader.readSource(url,"",cookie);
		reader.close();
		return source;
	}
}
