package com.hiekn.main;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.CheckTimeUtil;
import com.hiekn.util.HttpReader;
import com.hiekn.util.IReadHttpFileUtil;
import com.hiekn.util.ReadHttpFileUtil;
import com.hiekn.util.StaticHttpReader;
import com.hiekn.util.StringTimeUtils;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.sun.jersey.api.model.Parameter.Source;

public class ItjzCrawler {
	
	static String searchCookie = "gr_user_id=8b976033-64fe-4861-8ff7-099b7fe55b46; pgv_pvi=770369536; identity=lichunxiao%40hiekn.com; remember_code=sN8arlVES4; OUTFOX_SEARCH_USER_ID_NCOO=563651296.908239; acw_tc=AQAAAAKW7gcV3AcAUH9RZaX4F9lAM2oh; session=5ed713eff2a8662c6f8f6bdc2401655b0e8dfd53; acw_sc=581847a5e0f67d7aa764cf7c711687b0e3dad7bb; _gat=1; gr_session_id_eee5a46c52000d401f969f4535bdaa78=233e2ac7-3266-47c3-a2e2-3253230b65c1; Hm_lvt_1c587ad486cdb6b962e94fc2002edf89=1477548670,1477552076,1477552410,1477982450; Hm_lpvt_1c587ad486cdb6b962e94fc2002edf89=1477986541; _ga=GA1.2.541892866.1466752277";
	static String cookie = "gr_user_id=8b976033-64fe-4861-8ff7-099b7fe55b46; pgv_pvi=770369536; identity=lichunxiao%40hiekn.com; remember_code=sN8arlVES4; OUTFOX_SEARCH_USER_ID_NCOO=563651296.908239; acw_tc=AQAAAAKW7gcV3AcAUH9RZaX4F9lAM2oh; session=370707385869373372c9620057ee8847bb0d6e22; acw_sc=58183ff9e273f607bd1ebf9fd66c640af3131134; _gat=1; gr_session_id_eee5a46c52000d401f969f4535bdaa78=233e2ac7-3266-47c3-a2e2-3253230b65c1; Hm_lvt_1c587ad486cdb6b962e94fc2002edf89=1477548670,1477552076,1477552410,1477982450; Hm_lpvt_1c587ad486cdb6b962e94fc2002edf89=1477985116; _ga=GA1.2.541892866.1466752277";
	
	static Logger log = Logger.getLogger(ItjzCrawler.class);
	
	public static void main(String[] args) {
		try {
			long t = System.currentTimeMillis();
//			itjzProcess();
//			itjzSearchCrawler();
//			itjzPageInfoCrawler();
//			itjzTagCrawler();
//			itjzImgCrawler();
			itjzAllParse();
//			IReadHttpFileUtil reader = new ReadHttpFileUtil();
//			JSONObject obj = getPeopleInfo("https://www.itjuzi.com/person/201",reader);
//			System.out.println("sf");
//			itjzPeoCrawler();
//			itjzComCrawler();
			System.out.println(System.currentTimeMillis() - t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void itjzPeoCrawler() throws Exception {
		MongoClient client = new MongoClient("127.0.0.1",27017);
		DB db = client.getDB("data");
		DBCollection collection = db.getCollection("itjz_peo");
		IReadHttpFileUtil reader = new ReadHttpFileUtil();
		for (int i = 1; i < 37306; i++) {
			if (i< 22700) continue;
			String url = "https://www.itjuzi.com/person/"+i;
			String peoSource = reader.readPageSourceCommon(url, "utf-8");
			if (peoSource.contains("IT桔子")) {
				log.info(i + " has page");
				DBObject obj = new BasicDBObject();
				obj.put("source", peoSource);
				collection.insert(obj);
				obj.put("url", url);
				TimeUnit.SECONDS.sleep(3);
			}
		}
		client.close();
	}
	private static void itjzComCrawler() throws Exception {
		MongoClient client = new MongoClient("127.0.0.1",27017);
		DB db = client.getDB("data");
		DBCollection collection = db.getCollection("itjz_com");
		IReadHttpFileUtil reader = new ReadHttpFileUtil();
		for (int i = 1; i < 22; i++) {
			String url = "http://www.itjuzi.com/company/"+i;
			String peoSource = reader.readPageSourceCommon(url, "utf-8");
			if (peoSource.contains("IT桔子")) {
				log.info(i + " has page");
				DBObject obj = new BasicDBObject();
				obj.put("source", peoSource);
				obj.put("url", url);
				collection.insert(obj);
				TimeUnit.SECONDS.sleep(2);
			}
		}
		client.close();
	}
	
	private static void itjzAllParse() throws Exception {
		int count = 0;
		MongoClient client = new MongoClient("127.0.0.1",27017);
		MongoDatabase db = client.getDatabase("data");
		MongoCollection<org.bson.Document> comColl = db.getCollection("itjz_com");
		MongoCollection<org.bson.Document> pColl = db.getCollection("itjz_com_process");
		MongoCursor<org.bson.Document> cursor = comColl.find().iterator();
		List<org.bson.Document> list = new ArrayList<org.bson.Document>();
		Set<String> urlSet = new HashSet<String>();
		try {
			while (cursor.hasNext()) {
				System.out.println(count++);
				org.bson.Document originDoc = cursor.next();
				String comSource = originDoc.getString("source");
				String url = originDoc.getString("url");
				if (urlSet.contains(url)) {
					continue;
				} else {
					urlSet.add(url);
				}
				org.bson.Document doc = itjzComParser(comSource);
				if (doc != null) {
					doc.append("url", url);
					pColl.insertOne(doc);
				}
//				list.add(doc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		pColl.insertMany(list);
		client.close();
	}
	
	private static org.bson.Document itjzComParser(String comSource) {
//		IReadHttpFileUtil reader = new ReadHttpFileUtil();
		if (!comSource.contains("<option selected='selected'; value=\"1\">运营中</option>")) return null;
		JSONObject obj = new JSONObject();
		obj.put("source", comSource);
		Document doc = Jsoup.parse(comSource);
		//是否关闭
//		String isOpen = "";
//		try {
//			isOpen = doc.select("div.infoheadrow-v2.ugc-block-item").text();
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		if (isOpen.equals("closed")) return null;
		//行业处理
		Elements industryEle = doc.select("span.scope > a");
		String industry = "";
		for (Element element : industryEle) {
			industry = industry + element.text() + "\t";
		}
		if (!industry.equals("")) {
			obj.put("industry", industry);
		}
		//地点处理
		Elements LocEle = doc.select("span.loca > a");
		String loc = "";
		for (Element element : LocEle) {
			loc = loc + element.text() + "\t";
		}
		if (!loc.equals("")) {
			obj.put("local", loc);
		}
		//公司网站
		String web = "";
		try {
			web = doc.select("div.link-line > a.weblink").get(0).text();
		} catch (Exception e) {
			System.out.println("web error");
		}
		if (!web.equals("")) {
			obj.put("web", web);
		}
		//标签
		List<String> tagList = new ArrayList<String>();
		Elements tagEle = doc.select("div.tagset > a");
		for (Element element : tagEle) {
			tagList.add(element.text());
		}
		obj.put("tag", tagList);
		//abs
		String abs = "";
		try {
			abs = doc.select("div.des").get(0).text();
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (!abs.equals("")) {
			obj.put("abs", abs);
		}
		String fullName = "";
		try {
			fullName = doc.select("div.des-more > div > span").get(0).text();
			fullName.replace("公司全称：", "");
		} catch (Exception e) {
			// TODO: handle exception
		}
		obj.put("name", fullName);
		//时间
		String time = "";
		try {
			time = doc.select("div.des-more > div > span").get(1).text();
		} catch (Exception e) {
			// TODO: handle exception
		}
		obj.put("time", time);
		//scale
		String scale = "";
		try {
			scale = doc.select("div.des-more > div >span").get(2).text();
		} catch (Exception e) {
			// TODO: handle exception
		}
		obj.put("scale", scale);
		//invest
		List<String> investUrlList = new ArrayList<String>();
		List<JSONObject> investList = new ArrayList<JSONObject>();
		Elements investEle = doc.select("table.list-round-v2 > tbody > tr");
		for (Element element : investEle) {
			JSONObject investObj = new JSONObject();
			try {
				String investDate = element.select("span.date.c-gray").get(0).text();
				String investRou = element.select("span.round").get(0).text();
				String investMon = element.select("span.finades").get(0).text();
				String investPar = element.select("td").text();
				investObj.put("date", investDate);
				investObj.put("round", investRou);
				investObj.put("money", investMon);
				investObj.put("party", investPar);
				String investUrl = "";
				try {
					Elements urlEle = element.select("td").get(3).select("a");
					for (Element element2 : urlEle) {
						investUrl = element2.attr("href");
					}
					if (!investUrl.equals("")) {
						investUrlList.add(investUrl);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			investList.add(investObj);
		}
		//people
//		List<JSONObject> peopleList = new ArrayList<JSONObject>();
//		try {
//			Elements peoEle = doc.select("ul.list-prodcase > li > div > div.left > a");
//			for (Element element : peoEle) {
//				String peoUrl = element.attr("href");
//				JSONObject peoObj = new JSONObject();
//				peoObj = getPeopleInfo(peoUrl, reader);
//				peopleList.add(peoObj);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		obj.put("people", peopleList);
		org.bson.Document rDoc = new org.bson.Document();
		for (String key : obj.keySet()) {
			if (key.equals("source")) continue;
			rDoc.append(key, obj.get(key));
		}
//		for (String string : investUrlList) {
//			JSONObject investObj = getInvestInfo(string, reader);
//		}
		return rDoc;
	}
	
	
	private static JSONObject getInvestInfo(String url,IReadHttpFileUtil reader) {
		JSONObject investObj = new JSONObject();
		return investObj;
	}
	
	private static JSONObject getPeopleInfo(String url,IReadHttpFileUtil reader) {
		JSONObject peoObj = new JSONObject();
		url = url.replace("http", "https");
		String peoSource = reader.readPageSourceCommon(url, "utf-8");
		peoObj.put("source", peoSource);
		Document doc = Jsoup.parse(peoSource);
		try {
			String name = doc.select("span.name.marr10").text();
			peoObj.put("name", name);
			String img = doc.select("span.usericon > img").attr("src");
			peoObj.put("img", img);
			String title = doc.select("p.titleset").text();
			peoObj.put("title", title);
			String abs = doc.select("div.sec > div > div.block").text();
			peoObj.put("abs", abs);
			List<JSONObject> expList = new ArrayList<JSONObject>();
			Elements expEle = doc.select("ul.list-timeline > li > a > i.incinfo > i.right");
			for (Element element : expEle) {
				JSONObject expObj = new JSONObject();
				try {
					expObj.put("com", element.select("span.long").text());
					expObj.put("date", element.select("span.newsdate").text());
				} catch (Exception e) {
					// TODO: handle exception
				}
				expList.add(expObj);
			}
			peoObj.put("exp", expList);
			List<String> workExpList = new ArrayList<String>();
			Elements workEle = doc.select("ul.list-timeline-h").get(0).select("li");
			for (Element element : workEle) {
				String work = element.select("span.picinfo > span.right").text();
				workExpList.add(work);
			}
			peoObj.put("work", workExpList);
			List<String> eduList = new ArrayList<String>();
			Elements eduEle = doc.select("ul.list-timeline-h").get(1).select("li");
			for (Element element : eduEle) {
				String edu = element.select("span.picinfo > span.right").text();
				eduList.add(edu);
			}
			peoObj.put("edu", eduList);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return peoObj;
	}
	
	private static void itjzImgCrawler() throws Exception {
		FileWriter fw = new FileWriter("data/itjz/new_url.txt");
		String input = "";
		BufferedReader br = BufferedReaderUtil.getBuffer("data/itjz/new.txt");
		Map<String,String> sourceMap = getSource();
		while ((input = br.readLine())!=null) {
			JSONObject obj = JSONObject.parseObject(input);
			String id = obj.getString("_id");
			String key = obj.getJSONObject("source").getString("itjz_key");
			String source = sourceMap.get(key);
			String url = getImgUrl(source);
			if (!url.equals("")) {
//				fw.write(url + "\r\n");
				fw.write(id + "\t" + url + "\r\n");
				fw.flush();
//				DownloadImageUtil.downloadPic(url, id+".jpg", "data/itjz/new_com/");
			}
		}
		fw.close();
	}
	
	private static String getImgUrl(String source) {
		String url = "";
		Document doc = Jsoup.parse(source);
		try {
			url = doc.select("div.pic > img").attr("src");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}
	
	private static void itjzProcess() throws Exception {
		String input = "";
//		BufferedReader br = BufferedReaderUtil.getBuffer("data/itjz/error_com.txt");
		BufferedReader br = BufferedReaderUtil.getBuffer("data/itjz/all_result.txt");
		FileWriter fw = new FileWriter("data/itjz/info.txt");
		while ((input = br.readLine())!=null) {
			JSONObject obj = JSONObject.parseObject(input);
			String source = obj.getString("source");
			String url = obj.getString("url");
			String info = pageParser(source,url);
			if (!info.equals("")) {
				fw.write(info + "\r\n");
				fw.flush();
//				System.out.println(info);
			}
		}
		br.close();
		fw.close();
	}
	
	private static Set<String> getKeyWord() throws Exception {
		Set<String> set = new HashSet<String>();
		String input = "";
		BufferedReader br = BufferedReaderUtil.getBuffer("data/itjz/keyword.txt");
		while ((input = br.readLine())!=null) {
			set.add(input.trim());
		}
		br.close();
		return set;
	}
	
	private static void itjzPageInfoCrawler() throws Exception {
		String input = "";
		HttpReader reader = new StaticHttpReader();
		FileWriter fw = new FileWriter("data/itjz/all_info.txt",true);
		BufferedReader br = BufferedReaderUtil.getBuffer("data/itjz/all.txt");
		while ((input = br.readLine())!=null) {
			JSONObject obj = JSONObject.parseObject(input);
			String id = obj.getString("com_id");
			String url = "http://www.itjuzi.com/company/" + id;
			String info = reader.readSource(url,"utf-8",cookie);
			JSONObject resultObj = new JSONObject();
			resultObj.put("url", url);
			resultObj.put("source", info);
			fw.write(resultObj.toJSONString() + "\r\n");
			fw.flush();
			TimeUnit.SECONDS.sleep(3);
		}
		reader.close();
		fw.close();
		br.close();
	}
	
	private static void itjzTagCrawler() throws Exception {
		FileWriter fw = new FileWriter("data/itjz/371.txt",true);
		HttpReader reader = new StaticHttpReader();
		for (int i = 2; i < 4; i++) {
			String url = "http://www.itjuzi.com/company?tag=371&page="+i;
			String source = reader.readSource(url, "utf-8", searchCookie);
			List<String> urlList = getUrlList(source);
			for (String pageUrl : urlList) {
				JSONObject obj = new JSONObject();
				String infoSource = reader.readSource(pageUrl, "utf-8", cookie);
				obj.put("source",infoSource);
				obj.put("url", pageUrl);
				fw.write(obj.toJSONString() + "\r\n");
				fw.flush();
			}
			TimeUnit.SECONDS.sleep(3);
		}
		fw.close();
		reader.close();
	}
	
	private static void itjzSearchCrawler() throws Exception {
		Set<String> set = getKeyWord();
		FileWriter fw = new FileWriter("data/itjz/all.txt",true);
		HttpReader reader = new StaticHttpReader();
		for (String keyword : set) {
			int pageCount = 1;
			while (true) {
				String url = "https://www.itjuzi.com/search/more?key="+keyword+"&type=juzi_company&page=" + pageCount;
				String source = reader.readSource(url,"utf-8",searchCookie);
				JSONObject obj = JSONObject.parseObject(source);
				JSONArray comArr = obj.getJSONObject("juzi_company").getJSONArray("detail");
				if (comArr.size() == 0) {
					break;
				} else {
					for (Object object : comArr) {
						fw.write(object.toString() + "\r\n");
						fw.flush();
					}
				}
				pageCount++;
				TimeUnit.SECONDS.sleep(5);
			}
			TimeUnit.SECONDS.sleep(10);
		}
		fw.close();
		reader.close();
	}
	
	private static List<String> getUrlList(String source) {
		List<String> urlList = new ArrayList<String>();
		Document doc = Jsoup.parse(source);
		Elements urlE = doc.select("i.maincell > p.title > a");
		for (Element element : urlE) {
			String url = element.attr("href");
			urlList.add(url);
		}
		return urlList;
	}
	
	private static String pageParser(String source,String url) {
		if (source.equals("")) return "";
		String info = "";
		Document doc = Jsoup.parse(source);
		String comName = "";
		try {
			comName = doc.select("div.des-more > div > span").get(0).text().replace("公司全称：", "").trim();
		} catch (Exception e) {
			System.out.println(source);
		}
		if (comName.equals("")) {
			System.out.println("comName error \t" + url);
			return "";
		}
		if (!comName.contains("暂未")) {
			JSONObject infoObj = new JSONObject();
			JSONObject originObj = new JSONObject();
			JSONObject gsObj = new JSONObject();
			JSONArray peopleArr = new JSONArray();
			JSONArray productArr = new JSONArray();
			JSONArray investArr = new JSONArray();
			infoObj.put("key", url);
			gsObj.put("name", comName);
			String tel = doc.select("ul.list-block.aboutus > li > i.fa.fa-phone ~ span").text();
			String address = doc.select("ul.list-block.aboutus > li > i.fa.fa-map-marker ~ span").text();
			String province = "";
			String district = "";
			try {
				province = doc.select("span.loca > a").get(0).text();
				district = doc.select("span.loca > a").get(1).text().replace("新区", "区").replace("区", "");
			} catch (Exception e) {
				System.out.println(comName);
			}
			if (!tel.contains("暂未")) {
				gsObj.put("tel", tel);
			} else {
				gsObj.put("tel", "");
			}
			if (!address.contains("暂未")) {
				gsObj.put("address", address);
			} else {
				gsObj.put("address", "");
			}
			gsObj.put("province", province);
			gsObj.put("district", district);
			infoObj.put("gs", gsObj);
			Elements investElements = doc.select("table.list-round-v2 > tbody > tr");
			for (Element element : investElements) {
				JSONObject investObj = new JSONObject();
				String time = element.select("td > span.date").text();
				String createTime = CheckTimeUtil.correctTime(StringTimeUtils.formatStringTime(time).substring(0,10));
				investObj.put("created_at", createTime);
				String round = element.select("td > span.round").text();
				investObj.put("round", round);
				String money = element.select("td > span.finades").text();
				investObj.put("money", money);
				List<JSONObject> investList = new ArrayList<JSONObject>();
				String investStr = element.select("td").get(3).html();
				String[] investStrArr = investStr.split("<br />");
				for (String invest : investStrArr) {
					JSONObject obj = new JSONObject();
					obj.put("name", Jsoup.parse(invest).text());
					investList.add(obj);
				}
				investObj.put("vc", investList);
				investArr.add(investObj);
			}
			infoObj.put("invest", investArr);
			Elements productElements = doc.select("ul.list-prod > li");
			for (Element element : productElements) {
				JSONObject productObj = new JSONObject();
				String productName = element.select("div.on-edit-hide > h4 > b").text();
				String productAbs = element.select("div.on-edit-hide > p").text();
				productObj.put("name", productName);
				productObj.put("abs", productAbs);
				productObj.put("tags", new ArrayList<String>());
				productArr.add(productObj);
			}
			infoObj.put("product", productArr);
			Elements peopleElements = doc.select("ul.list-prodcase > li");
			for (Element element : peopleElements) {
				JSONObject peoObj = new JSONObject();
				String peoAbs = element.select("div.right > p.person-des").text();
				String peoName = element.select("h4.person-name > a > b > span.c").text();
				peoObj.put("abs", peoAbs);
				peoObj.put("name", peoName);
				peopleArr.add(peoObj);
			}
			infoObj.put("people", peopleArr);
			String mainInd = "";
			String subInd = "";
			try {
				mainInd = doc.select("span.scope > a").get(0).text();
			} catch (Exception e) {
				System.out.println("main industry error \t" + url);
			}
			try {
				subInd = doc.select("span.scope > a").get(1).text();
			} catch (Exception e) {
				System.out.println("sub industry error \t" + url);// TODO: handle exception
			}
			Elements tagElements = doc.select("div.tagset > a");
			List<String> tagList = new ArrayList<String>();
			for (Element element : tagElements) {
				tagList.add(element.text());
			}
			originObj.put("tags", tagList);
			originObj.put("biz_domain", mainInd);
			originObj.put("sub_ind", subInd);
			String intro = doc.select("div.des").text();
			originObj.put("brief_intro", intro);
			String scale = doc.select("div.des-more > div").get(1).select("span").get(1).text().substring(5);
			if (!scale.contains("暂未")) {
				originObj.put("scale", scale);
			} else {
				originObj.put("scale", "");
			}
			infoObj.put("origin", originObj);
			info = infoObj.toJSONString();
		}
		return info;
	}
	
	private static long getProId() {
		long proId = 0;
		return proId;
	}
	
	private static long getDisId() {
		long disId = 0;
		return disId;
	}
	
	private static Map<String,String> getSource() {
		Map<String,String> map = new HashMap<String,String>();
		BufferedReader br = null;
		String input = "";
		try {
			br = BufferedReaderUtil.getBuffer("data/itjz/all_result.txt");
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				map.put(obj.getString("url"), obj.getString("source"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	@Test
	public void integrateResult() {
		BufferedReader br = null;
		FileWriter fw = null;
		String input = "";
		try {
			fw = new FileWriter("data/itjz/all_result.txt");
			br = BufferedReaderUtil.getBuffer("data/itjz/ai_result.txt");
			while ((input = br.readLine())!=null) {
				fw.write(input + "\r\n");
				fw.flush();
			}
			br = BufferedReaderUtil.getBuffer("data/itjz/blockchain_result.txt");
			while ((input = br.readLine())!=null) {
				fw.write(input + "\r\n");
				fw.flush();
			}
			br = BufferedReaderUtil.getBuffer("data/itjz/energy_result.txt");
			while ((input = br.readLine())!=null) {
				fw.write(input + "\r\n");
				fw.flush();
			}
			br = BufferedReaderUtil.getBuffer("data/itjz/finance_result.txt");
			while ((input = br.readLine())!=null) {
				fw.write(input + "\r\n");
				fw.flush();
			}
			br = BufferedReaderUtil.getBuffer("data/itjz/zhibo_result.txt");
			while ((input = br.readLine())!=null) {
				fw.write(input + "\r\n");
				fw.flush();
			}
			br = BufferedReaderUtil.getBuffer("data/itjz/371.txt");
			while ((input = br.readLine())!=null) {
				fw.write(input + "\r\n");
				fw.flush();
			}
			br.close();
			fw.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
