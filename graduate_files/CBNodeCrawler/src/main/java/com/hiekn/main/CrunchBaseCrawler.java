package com.hiekn.main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.HttpClientUtil;

public class CrunchBaseCrawler {
	
	private static CloseableHttpClient httpClient = HttpClientUtil.createSSLClientDefault();
	private static final String cookie = "D_SID=101.81.112.188:PA7+8Yro96za3bk2dI78UEUsoiQ5kc62KpWY2lTmbHI; __uvt=; __qca=P0-1944316000-1489651843253; _pxvid=0b40b230-0a20-11e7-ae2a-61cae9af130c; __cfduid=ded954673eaedd9cd2f0f31d5638377791489651841; mf_user=bb949e209870cfd170f611a33a6bce35|; _vdl=1; AMCV_6B25357E519160E40A490D44%40AdobeOrg=1256414278%7CMCMID%7C24514527424328983233745277840455029972%7CMCAID%7CNONE%7CMCAAMLH-1492162837%7C11%7CMCAAMB-1492162837%7Chmk_Lq6TPIBMW925SPhw3Q; D_PID=5C3CBE73-2DC2-3A4D-BADD-1AF74C70A192; D_IID=5C3BE1AF-E1FF-39C2-BDD5-E648D1022DDC; D_UID=AD20A8B1-30E3-3AF8-B5ED-C6016E42DB47; D_HID=mKraLX112ww5VIA5I+k6G+4Bt7EvI4yvDMWRmo2sEO4; D_ZID=90401DE3-59D4-3333-AB9A-29AE3B6B66F1; D_ZUID=C8F7B22E-8DC6-398E-8695-5C677967835D; multivariate_bot=false; s_cc=true; s_pers=%20s_getnr%3D1492398673288-Repeat%7C1555470673288%3B%20s_nrgvo%3DRepeat%7C1555470673290%3B; uvts=5mKM8MDa57aEVRjI; _okdetect=%7B%22token%22%3A%2214923986932550%22%2C%22proto%22%3A%22https%3A%22%2C%22host%22%3A%22www.crunchbase.com%22%7D; olfsk=olfsk6947764846019207; _okbk=cd4%3Dtrue%2Cvi5%3D0%2Cvi4%3D1492398693593%2Cvi3%3Dactive%2Cvi2%3Dfalse%2Cvi1%3Dfalse%2Ccd8%3Dchat%2Ccd6%3D0%2Ccd5%3Daway%2Ccd3%3Dfalse%2Ccd2%3D0%2Ccd1%3D0%2C; _ok=1554-355-10-6773; wcsid=9D13C2zTuRZxYsLI3F6pZ0H0P8REBb28; hblid=hyHaLTL25ZBGW4Sk3F6pZ0H0P0REzB6r; _ga=GA1.2.1513111953.1489647242; jaco_uid=3bd81e28-c87e-4cb0-bb70-c0b9adf52b12; jaco_referer=; _hp2_props.973801186=%7B%22Logged%20In%22%3Afalse%2C%22Pro%22%3Afalse%7D; _hp2_ses_props.973801186=%7B%22r%22%3A%22https%3A%2F%2Fwww.crunchbase.com%2F%22%2C%22ts%22%3A1492398691808%2C%22d%22%3A%22www.crunchbase.com%22%2C%22h%22%3A%22%2Fapp%2Fsearch%22%7D; _hp2_id.973801186=%7B%22userId%22%3A%227376682748375158%22%2C%22pageviewId%22%3A%222676246795407168%22%2C%22sessionId%22%3A%220982966473328125%22%2C%22identity%22%3Anull%2C%22trackerVersion%22%3A%223.0%22%7D; _oklv=1492398752921%2C9D13C2zTuRZxYsLI3F6pZ0H0P8REBb28";
	private static Set<String> orgSet = new HashSet<String>();
	
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
//		list.add("computer");
		list.add("knowledge graph");
		try {
			crawl(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void crawl(List<String> keywordList) throws IOException, InterruptedException {
		for (String kw : keywordList) {
			String payload = "{\"field_ids\":[\"identifier\",\"category_groups\",\"headquarters_identifiers\",\"short_description\",\"rank\"],\"order\":[],\"query\":[{\"type\":\"predicate\",\"field_id\":\"short_description\",\"operator_id\":\"contains\",\"values\":[\""+kw+"\"]}],\"field_aggregators\":[],\"limit\":50}";
			String url = "https://www.crunchbase.com/v4/data/companies/search"; //第一层搜索接口
			String source = "";
			source = getPostSource(url,payload);
			TimeUnit.SECONDS.sleep(3);
			JSONArray searchResultArr = JSONObject.parseObject(source).getJSONArray("entities");
			for (Object object : searchResultArr) {
				JSONObject searchResultObj = (JSONObject) object;
				String orgName = searchResultObj.getJSONObject("properties").getJSONObject("identifier").getString("permalink");
				if (orgSet.contains(orgName)) {
					continue;
				} else {
					orgSet.add(orgName);
				}
				String orgUrl = "https://www.crunchbase.com/organization/" + orgName; //公司详情页
				String orgTeamUrl = "https://www.crunchbase.com/organization/" + orgName + "/people"; //公司人员列表页
				String peoSource = getGetSource(orgTeamUrl);
				TimeUnit.SECONDS.sleep(3);
				Elements peoElements = Jsoup.parse(peoSource).select("ul.section-list.container > li");
				for (Element element : peoElements) {
					
				}
				//获得更多的人员
				int count = 0;
				try {
					count = Integer.parseInt(Jsoup.parse(peoSource).select("h2#current_team > span").text().trim().replace("(", "").replace(")", "").replace(",", "").replace(" ", ""));
				} catch (Exception e) {
					e.printStackTrace();
				}
				for (int i = 2; i < (count-6)/14 + 2; i++) {
					String peoMoreUrl = "https://www.crunchbase.com/organization/" + orgName + "/people?page=" + i;
					String peoMoreSource = getGetSource(peoMoreUrl);
					TimeUnit.SECONDS.sleep(3);
				}
			}
//			String des = Jsoup.parse(source).select("h1#profile_header_heading").text();
//			System.out.println(des);
		}
	}
	
	private static String getPostSource(String url,String payload) {
		String source = "";
		CloseableHttpResponse httpResponse = null;
		HttpPost post = new HttpPost(url);
		post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
		post.setHeader("Host", "www.crunchbase.com");
		post.setHeader("Cookie", cookie);
		post.setHeader("Connection", "keep-alive");
		post.setHeader("Content-Type", "application/json;charset=UTF-8");
		post.setHeader("Accept", "application/json, text/plain, */*");
		post.setHeader("Accept-Encoding", "gzip, deflate, sdch, br");
		post.setHeader("Pragma", "no-cache");
		if (!payload.equals("")) {
			StringEntity se = null;
			try {
				se = new StringEntity(payload);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			post.setEntity(se);
		}
		try {
			httpResponse = httpClient.execute(post);
			source = EntityUtils.toString(httpResponse.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return source;
	}
	
	private static String getGetSource(String url) {
		String source = "";
		CloseableHttpResponse httpResponse = null;
		HttpGet get = new HttpGet(url);
		get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
		get.setHeader("Host", "www.crunchbase.com");
		get.setHeader("Cookie", cookie);
		get.setHeader("Connection", "keep-alive");
		get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		get.setHeader("Accept-Encoding", "gzip, deflate, sdch, br");
		get.setHeader("Pragma", "no-cache");
		try {
			httpResponse = httpClient.execute(get);
			HttpEntity entity = httpResponse.getEntity();
			source = EntityUtils.toString(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return source;
	}
	
	
}
