package com.hiekn.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class KrCompany implements Runnable{
	
	private static final Logger LOGGER = Logger.getLogger(KrCompany.class);
	
	private volatile String cookie;
	private List<String> urlList = new ArrayList<String>();
	private List<String> cookieList = getCookieList();
//	private String[] cookieArray = {
//			"kr_stat_uuid=JApRJ24446004;gr_user_id=f4614c52-18a0-42d4-8076-52659ca0b6a3;aliyungf_tc=AQAAAARmYAmZRgwAwHpRZeboD8S9ZKmU;_kr_p_se=0c975bf6-1b3b-4e6d-8626-8b1c8b5b41e1;kr_plus_id=2004857910;kr_plus_token=jthrTUA3f3IofOW22sRpD9afYItYR1z2796_____;krid_user_id=923137;krid_user_version=1001;Hm_lvt_e8ec47088ed7458ec32cde3617b23ee3=1466838107;Hm_lpvt_e8ec47088ed7458ec32cde3617b23ee3=1466838145;kr_plus_utype=0;Hm_lvt_e8ec47088ed7458ec32cde3617b23ee3=1466838107;Hm_lpvt_e8ec47088ed7458ec32cde3617b23ee3=1467185545;Z-XSRF-TOKEN=eyJpdiI6ImdIWmhsNDA4T1J1aTA0clFhZEp1ZkE9PSIsInZhbHVlIjoiVUwySnJ4VUdQN0pmSFJJRUcxdUlaK0NiSkdsWTYzQVd3OHNEWnVRa3VqbFwvNmZxT0lsenVma0ZWVkNmbVF2RXFJTW1iczFwcnJ1SW1NY3F0YjNqQXd3PT0iLCJtYWMiOiI0MDExZDAwMDA2ZmM0OGNlYjdlNTYxMmFkNjgwMzQ1OGUyMmYzMTViN2Y2M2FiODc2ZGVhOTdiODUxMTllYWNhIn0%3D;krchoasss=eyJpdiI6IlJ1SE1cL2g0WnI3RXAzd1ZscUhkOExnPT0iLCJ2YWx1ZSI6Ijkrb2lWWHVIN2cxbGt2MUlyR2JjbE5xNmNJT1NwU3JQMFRvUWN1R1dCRlNraHNpck9wV2lYaXp0MzZTZW5WRStZRVRvZ1lhRGROU1lueWRvU1NnUDl3PT0iLCJtYWMiOiI3OGYwZWM4MTI5ZWEwNzY2NTRmNmNlNGI5NDUzZDZmMjY2NDNkMzJlNmVlMjIyZWZhMGRlNTAyYWVjNTgyYTQ1In0%3D;c_name=point;Hm_lvt_713123c60a0e86982326bae1a51083e1=1466760247,1466818520,1466818521,1466826336;Hm_lpvt_713123c60a0e86982326bae1a51083e1=1467187642;gr_session_id_76d36bd044527820a1787b198651e2f1=85220342-b1a2-4909-a01f-249d820b4c4e;_krypton_session=WVlYajc4L21ML2prR0tsVFcyc0U5WUJsSnFEekluVm9RZnBIYjBOMFBUcUp5L0VkNHhBWFdsYUhrekhtOUxEMkF6OUdONERKbC9JbHdXbzVOK2RzVm1pZnJMSUs1V3FkTTNTZ0M0N3Byc01pRE04T09vMmdLSS8zNGp1a3p1cFpFMThUNDRXbG9yTWR3U0ttdGFXRno2L3FJWktidmVybHdWL3B3M1BrWm9JenpqN2JKaTJGUU9QU2Y3VC9CcXNrbUR0NzcyTDVOY3dLUndESVkzYkdvNzJBaDhrUi9FWkZzSUV0bnFqU1RQeXRqdStsUzVkeWY0dUI2bSt6TXhRZ3Q4S09HTlVwOXZHci9qaGkwd2ZNb0Z5Mm9YZXBvaHp3R2szTXRNOUQybUk9LS10NUJvOHpMSy9sUkVwQnVTaEZCM0NRPT0%3D--990730ec86a53e0e8fb4d4bd6dc505b15af17894;",
//			"aliyungf_tc=AQAAAGusGkOQ9wYAwHpRZXiK/WSRxExM;ADHOC_MEMBERSHIP_CLIENT_ID=33945995-96b5-a0ff-c8a5-b8fee6a4e431;gr_user_id=b95fa7e6-b196-4d9f-8e2a-c09d7e6211c6;kr_stat_uuid=ZhJTG24453178;kwlo_iv=3h;Hm_lvt_e8ec47088ed7458ec32cde3617b23ee3=1466838107,1467190153,1467190487;Hm_lpvt_e8ec47088ed7458ec32cde3617b23ee3=1467190703;c_name=point;_kr_p_se=0bce8430-b4c5-467c-a5fa-021c10a14c9e;krid_user_id=926009;krid_user_version=1001;kr_plus_id=2002392845;kr_plus_token=4cz5Um4F4yDgHbVMkez7Xw2nXUe6dYle852_____;Hm_lvt_713123c60a0e86982326bae1a51083e1=1466818521,1466826336,1467190129,1467190699;Hm_lpvt_713123c60a0e86982326bae1a51083e1=1467190802;gr_session_id_76d36bd044527820a1787b198651e2f1=e9734744-f1f1-45f1-bb48-7a20f86c1dff;kr_plus_utype=0;_krypton_session=Njg2Smtvc0Nwckx0aVQ3Qm1UaG5mUnNaTEZGVFRqQnplTHlYTk1wYzR4K1FGOFd5SFA0ek9VR1BWSFI0b2RzYURqZ1lsdmhCSDhOUWt3WHBqRXRGZld0MDBDMjVPd2NpN0U4Mng3UytQRzlVQzFEdFhGSDg1S25rZTNnOWxGRkQ1S294UG9qMVFIK2ZxTFYyMkVKbm9kL2hnSTJyMW1waGUzTHpHM2VTUzQyTXJEVHFiR2dLamdDN2Z4TnR6ZjRwWnQ1bXRKRVlMWXA0WGN2NEFxTFVodEFvM3FJdUZ3dzFhL0RIZ0paMldPNStRRzB2QkFGODB1UnlQcWQvZmtLZUp5VTFmQ0p0bm83dTIxaUhGZmZjTVlQZ29qdHRpTmZPd0h4dFAwNFB0Zjg9LS1MQ1dod3VRWjdJSkpvbEhCTXZ3cmZ3PT0%3D--3a1fa894e8bb9c52028747b422f0279af507ffe8;",
//			"aliyungf_tc=AQAAAARmYAmZRgwAwHpRZeboD8S9ZKmU;kwlo_iv=4h;Hm_lvt_713123c60a0e86982326bae1a51083e1=1466818521,1466826336,1467190129,1467190699;Hm_lpvt_713123c60a0e86982326bae1a51083e1=1467192759;gr_user_id=2567ad4e-e438-42da-9805-5154b57055e0;kr_stat_uuid=QPiay24453212;Hm_lvt_e8ec47088ed7458ec32cde3617b23ee3=1467190153,1467190487,1467192697,1467192804;Hm_lpvt_e8ec47088ed7458ec32cde3617b23ee3=1467192804;_kr_p_se=4314c892-9c69-4c04-b677-2ed134dee8b2;krid_user_id=926045;krid_user_version=1001;kr_plus_id=436942459;kr_plus_token=dlnony_mU2XaT46CB_xg7oz7HzgKQh87DM892___;gr_session_id_76d36bd044527820a1787b198651e2f1=ee79ca1f-405b-424d-959c-3ae20d01152c;Hm_lvt_e8ec47088ed7458ec32cde3617b23ee3=1466838107,1467190153,1467190487,1467192697;Hm_lpvt_e8ec47088ed7458ec32cde3617b23ee3=1467192882;kr_plus_utype=0;Z-XSRF-TOKEN=eyJpdiI6IkxLS3haUzVCbzhCZW9YcGRFSUpLY3c9PSIsInZhbHVlIjoicVlxVDdqNjJEOFZpK1NsMUc0d0w5ZlZRK29oMGRkMWlpdjArOEtuMUFvTU9IRHVaRU9IMWtPOElWeXRWWHB6RFMyTkgxVm5FTkI5WFFhUVdFWUJsZlE9PSIsIm1hYyI6ImU2NGU0ZjZjOTJiMzg0MWU3MWM2NWUxZDg5YmQxNWQwZjAxZTk5Y2U1ZTA0MDgzNGM2Nzk2NmE1ZjBlZjRkYzQifQ%3D%3D;krchoasss=eyJpdiI6ImpzZFpiY2h6TzlKdkFldnViZFBjNHc9PSIsInZhbHVlIjoiY1MyTk1Xb2JwUDFSUDU3cnBQWFdMWE5mQmQremxaS1BhWmptSVdFbDNjVHRBNUZHSlByN01HUmsrWHo5RFdkNlVab1dqN2xlY1hMOXR2dndDcFpQalE9PSIsIm1hYyI6ImJiN2JlN2RkZTFmN2UzODFkNTMwYmI3ZmVjNDYzYTE4M2UzZWUxZjc1YWU5ZjczMDg2NGUzNmRiODZjNWM2OTQifQ%3D%3D;_krypton_session=eFVWSDFUSUkwVFQzeE90WHlJTThybjI1aHo1aFdITk5lNmhSRjE0MzRNSE9lQ0x5ckV2ZG50NUpyVlhsWGN3SWt2SURwMDRSTVlWQ2JLZkxIM0ZTdml6bWxHNnVTLzE2ek9YbDd1QWJRQmQzSndRbDc5ZmNlQzN4cUkrTHhjR05YTGl2TjJnZ1k5RTZnS0FmbnplM0NWNEhnSFJSaFdlaG12d0tGR0V2Q3dab0xESVM3czlnSVRSRGhTVFF4d0tWR1ArRWo3OC80cC81UFp1Z0g2L2JubmZWeU1OMzVKaWhxc1doMXhZbU5neC9CU214Q0Vpbkt4ZHhZdHRiR2xWSlNNbkgrODFZS25IS1ZZUWh5TlRhZk5kSGp5OWNsTWJycFBSUDhBU0IyQTQ9LS1qcEdEK2UyU3NXRDU2ekpqdFNJcklBPT0%3D--5af1909bdc5dbe0f0acb7e88f9ad6be716627ba1;"
//	};
	
	private int index;
	
	public KrCompany(List<String> urlList) {
		this.urlList = urlList;
	}
	
	public void crawler() throws InterruptedException {
		index = 0;
		MongoClient client = getMongo();
		MongoDatabase db = client.getDatabase("kr_company");
		MongoCollection<Document> collection = db.getCollection("company");
		for (int i = 0; i < urlList.size(); i++) {
//			if (index > cookieArray.length) {
//				break;
//			}
			if (index > cookieList.size()) {
				break;
			}
//			cookie = cookieArray[index];
			cookie = cookieList.get(index);
			String url = urlList.get(i);
			int urlId = Integer.valueOf(url.substring(34));
			String basicUrl = "https://rong.36kr.com/api/company/"+urlId;
			String basicInfo = readSource(basicUrl,cookie);
			if (!isValid(basicInfo)) {
				cookie = null;
				synchronized(this) {
					if (cookie == null) {
						index = index + 1;
						LOGGER.info("invalid");
						i--;
						continue;
					}
				}
			}
			if (!hasCompany(basicInfo)) continue;
			LOGGER.info(" " + url);
			String employUrl = "https://rong.36kr.com/api/company/"+urlId+"/employee?pageSize=1000";
			String employInfo = readSource(employUrl,cookie);
			Thread.sleep(2000);
			String founderUrl = "https://rong.36kr.com/api/company/"+urlId+"/founder?pageSize=1000";
			String founderInfo = readSource(founderUrl,cookie);
			Thread.sleep(2000);
			String qixinUrl = "https://rong.36kr.com/api/company/"+urlId+"/qichacha";
			String qixinInfo = readSource(qixinUrl,cookie);
			Thread.sleep(2000);
			String proUrl = "https://rong.36kr.com/api/company/"+urlId+"/product";
			String proInfo = readSource(proUrl,cookie);
			Thread.sleep(2000);
			String finanUrl = "https://rong.36kr.com/api/company/"+urlId+"/past-finance";
			String finanInfo = readSource(finanUrl,cookie);
			Thread.sleep(2000);
			String investUrl = "https://rong.36kr.com/api/company/"+urlId+"/past-investor?pageSize=100";
			String investInfo = readSource(investUrl,cookie);
			Thread.sleep(2000);
			String memberUrl = "https://rong.36kr.com/api/company/"+urlId+"/former-member?pageSize=1000";
			String memInfo = readSource(memberUrl,cookie);
			Thread.sleep(2000);
			String feedUrl = "https://rong.36kr.com/api/company/"+urlId+"/feed?pageSize=1000&type=0";
			String feedInfo = readSource(feedUrl,cookie);
			Document doc = new Document("url",url)
								.append("basicInfo", basicInfo)
								.append("employInfo", employInfo)
								.append("founder", founderInfo)
								.append("qixin", qixinInfo)
								.append("product", proInfo)
								.append("finance", finanInfo)
								.append("invest", investInfo)
								.append("member", memInfo)
								.append("feedInfo", feedInfo);
			collection.insertOne(doc);
			Thread.sleep(5000);
		}
	}
	
	public boolean isValid(String str) {
		boolean bool;
		JSONObject obj = JSONObject.parseObject(str);
		int code = obj.getIntValue("code");
		if (code == 1) {
			bool = false;
		} else {
			bool = true;
		}
		return bool;
	}
	
	public static boolean hasCompany(String input) {
		JSONObject obj = JSONObject.parseObject(input);
		String msg = obj.getString("msg");
		int code = obj.getIntValue("code");
		if (code == 404) {
			return false;
		} else if (msg.equals("公司不存在")) {
			return false;
		}
		return true;
	}
	
	public String readSource(String url,String cookie) {
		String source = "";
		HttpReader reader = new StaticHttpReader();
		source = reader.readSource(url,"",cookie);
		reader.close();
		return source;
	}
	
	private MongoClient getMongo() {
		MongoClient client = new MongoClient("127.0.0.1",27017);
		return client;
	}
	
	private List<String> getCookieList() {
		List<String> cookieList = new ArrayList<String>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cookie.txt");
		String input = "";
		try {
			while ((input = br.readLine())!=null) {
				cookieList.add(input.trim());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cookieList;
	}
	
	public void run() {
		try {
			crawler();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
