package com.hiekn.other;

import java.io.BufferedReader;
import java.io.FileWriter;

import org.bson.Document;
import org.junit.Test;

import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;
import com.hiekn.util.StringTimeUtils;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class RandomTest {
	
	@Test
	public void t5() {
		String s = "中国平安保险（集团）股份有限公司 ";
		String input = "";
		BufferedReader br = null;
		try {
			FileWriter fw = new FileWriter("data/itjz/error_com.txt");
			br = BufferedReaderUtil.getBuffer("data/itjz/all_result.txt");
			while ((input = br.readLine())!=null) {
				if (input.contains("中国平安保险（集团）")) {
					System.out.println(true);
					fw.write(input + "\r\n");
					fw.flush();
				}
			}
			fw.close();
			br.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(s.trim());
	}
	
	@Test
	public void t4() {
		MongoClient client = new MongoClient("192.168.2.158",19170);
		MongoDatabase db = client.getDatabase("data");
		MongoCollection<Document> collection = db.getCollection("news_data");
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				 collection.updateOne(
                         Filters.eq("_id", doc.get("_id")),
                         new Document("$set", new Document("indexed", 0)));
			}
		} catch (Exception e) {
			// TODO: handle exception
			cursor.close();
		}
		client.close();
	}
	
	
	@Test
	public void t3() {
		System.out.println(199/1000);
		String str = "2016.5";
		try {
			System.out.println(StringTimeUtils.formatStringTime(str));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	@Test
	public void t2() {
		String url = "https://rong.36kr.com/api/company/4404";
		int i = Integer.valueOf(url.substring(34));
		System.out.println(i);
	}

	@Test
	public void test() {
		HttpReader reader = new StaticHttpReader();
		String[] cookieArray = {
				"kr_stat_uuid=JApRJ24446004;gr_user_id=f4614c52-18a0-42d4-8076-52659ca0b6a3;aliyungf_tc=AQAAAARmYAmZRgwAwHpRZeboD8S9ZKmU;_kr_p_se=0c975bf6-1b3b-4e6d-8626-8b1c8b5b41e1;kr_plus_id=2004857910;kr_plus_token=jthrTUA3f3IofOW22sRpD9afYItYR1z2796_____;krid_user_id=923137;krid_user_version=1001;Hm_lvt_e8ec47088ed7458ec32cde3617b23ee3=1466838107;Hm_lpvt_e8ec47088ed7458ec32cde3617b23ee3=1466838145;kr_plus_utype=0;Hm_lvt_e8ec47088ed7458ec32cde3617b23ee3=1466838107;Hm_lpvt_e8ec47088ed7458ec32cde3617b23ee3=1467185545;Z-XSRF-TOKEN=eyJpdiI6ImdIWmhsNDA4T1J1aTA0clFhZEp1ZkE9PSIsInZhbHVlIjoiVUwySnJ4VUdQN0pmSFJJRUcxdUlaK0NiSkdsWTYzQVd3OHNEWnVRa3VqbFwvNmZxT0lsenVma0ZWVkNmbVF2RXFJTW1iczFwcnJ1SW1NY3F0YjNqQXd3PT0iLCJtYWMiOiI0MDExZDAwMDA2ZmM0OGNlYjdlNTYxMmFkNjgwMzQ1OGUyMmYzMTViN2Y2M2FiODc2ZGVhOTdiODUxMTllYWNhIn0%3D;krchoasss=eyJpdiI6IlJ1SE1cL2g0WnI3RXAzd1ZscUhkOExnPT0iLCJ2YWx1ZSI6Ijkrb2lWWHVIN2cxbGt2MUlyR2JjbE5xNmNJT1NwU3JQMFRvUWN1R1dCRlNraHNpck9wV2lYaXp0MzZTZW5WRStZRVRvZ1lhRGROU1lueWRvU1NnUDl3PT0iLCJtYWMiOiI3OGYwZWM4MTI5ZWEwNzY2NTRmNmNlNGI5NDUzZDZmMjY2NDNkMzJlNmVlMjIyZWZhMGRlNTAyYWVjNTgyYTQ1In0%3D;c_name=point;Hm_lvt_713123c60a0e86982326bae1a51083e1=1466760247,1466818520,1466818521,1466826336;Hm_lpvt_713123c60a0e86982326bae1a51083e1=1467187642;gr_session_id_76d36bd044527820a1787b198651e2f1=85220342-b1a2-4909-a01f-249d820b4c4e;_krypton_session=WVlYajc4L21ML2prR0tsVFcyc0U5WUJsSnFEekluVm9RZnBIYjBOMFBUcUp5L0VkNHhBWFdsYUhrekhtOUxEMkF6OUdONERKbC9JbHdXbzVOK2RzVm1pZnJMSUs1V3FkTTNTZ0M0N3Byc01pRE04T09vMmdLSS8zNGp1a3p1cFpFMThUNDRXbG9yTWR3U0ttdGFXRno2L3FJWktidmVybHdWL3B3M1BrWm9JenpqN2JKaTJGUU9QU2Y3VC9CcXNrbUR0NzcyTDVOY3dLUndESVkzYkdvNzJBaDhrUi9FWkZzSUV0bnFqU1RQeXRqdStsUzVkeWY0dUI2bSt6TXhRZ3Q4S09HTlVwOXZHci9qaGkwd2ZNb0Z5Mm9YZXBvaHp3R2szTXRNOUQybUk9LS10NUJvOHpMSy9sUkVwQnVTaEZCM0NRPT0%3D--990730ec86a53e0e8fb4d4bd6dc505b15af17894;",
				"aliyungf_tc=AQAAAGusGkOQ9wYAwHpRZXiK/WSRxExM;ADHOC_MEMBERSHIP_CLIENT_ID=33945995-96b5-a0ff-c8a5-b8fee6a4e431;gr_user_id=b95fa7e6-b196-4d9f-8e2a-c09d7e6211c6;kr_stat_uuid=ZhJTG24453178;kwlo_iv=3h;Hm_lvt_e8ec47088ed7458ec32cde3617b23ee3=1466838107,1467190153,1467190487;Hm_lpvt_e8ec47088ed7458ec32cde3617b23ee3=1467190703;c_name=point;_kr_p_se=0bce8430-b4c5-467c-a5fa-021c10a14c9e;krid_user_id=926009;krid_user_version=1001;kr_plus_id=2002392845;kr_plus_token=4cz5Um4F4yDgHbVMkez7Xw2nXUe6dYle852_____;Hm_lvt_713123c60a0e86982326bae1a51083e1=1466818521,1466826336,1467190129,1467190699;Hm_lpvt_713123c60a0e86982326bae1a51083e1=1467190802;gr_session_id_76d36bd044527820a1787b198651e2f1=e9734744-f1f1-45f1-bb48-7a20f86c1dff;kr_plus_utype=0;_krypton_session=Njg2Smtvc0Nwckx0aVQ3Qm1UaG5mUnNaTEZGVFRqQnplTHlYTk1wYzR4K1FGOFd5SFA0ek9VR1BWSFI0b2RzYURqZ1lsdmhCSDhOUWt3WHBqRXRGZld0MDBDMjVPd2NpN0U4Mng3UytQRzlVQzFEdFhGSDg1S25rZTNnOWxGRkQ1S294UG9qMVFIK2ZxTFYyMkVKbm9kL2hnSTJyMW1waGUzTHpHM2VTUzQyTXJEVHFiR2dLamdDN2Z4TnR6ZjRwWnQ1bXRKRVlMWXA0WGN2NEFxTFVodEFvM3FJdUZ3dzFhL0RIZ0paMldPNStRRzB2QkFGODB1UnlQcWQvZmtLZUp5VTFmQ0p0bm83dTIxaUhGZmZjTVlQZ29qdHRpTmZPd0h4dFAwNFB0Zjg9LS1MQ1dod3VRWjdJSkpvbEhCTXZ3cmZ3PT0%3D--3a1fa894e8bb9c52028747b422f0279af507ffe8;",
				"aliyungf_tc=AQAAAARmYAmZRgwAwHpRZeboD8S9ZKmU;kwlo_iv=4h;Hm_lvt_713123c60a0e86982326bae1a51083e1=1466818521,1466826336,1467190129,1467190699;Hm_lpvt_713123c60a0e86982326bae1a51083e1=1467192759;gr_user_id=2567ad4e-e438-42da-9805-5154b57055e0;kr_stat_uuid=QPiay24453212;Hm_lvt_e8ec47088ed7458ec32cde3617b23ee3=1467190153,1467190487,1467192697,1467192804;Hm_lpvt_e8ec47088ed7458ec32cde3617b23ee3=1467192804;_kr_p_se=4314c892-9c69-4c04-b677-2ed134dee8b2;krid_user_id=926045;krid_user_version=1001;kr_plus_id=436942459;kr_plus_token=dlnony_mU2XaT46CB_xg7oz7HzgKQh87DM892___;gr_session_id_76d36bd044527820a1787b198651e2f1=ee79ca1f-405b-424d-959c-3ae20d01152c;Hm_lvt_e8ec47088ed7458ec32cde3617b23ee3=1466838107,1467190153,1467190487,1467192697;Hm_lpvt_e8ec47088ed7458ec32cde3617b23ee3=1467192882;kr_plus_utype=0;Z-XSRF-TOKEN=eyJpdiI6IkxLS3haUzVCbzhCZW9YcGRFSUpLY3c9PSIsInZhbHVlIjoicVlxVDdqNjJEOFZpK1NsMUc0d0w5ZlZRK29oMGRkMWlpdjArOEtuMUFvTU9IRHVaRU9IMWtPOElWeXRWWHB6RFMyTkgxVm5FTkI5WFFhUVdFWUJsZlE9PSIsIm1hYyI6ImU2NGU0ZjZjOTJiMzg0MWU3MWM2NWUxZDg5YmQxNWQwZjAxZTk5Y2U1ZTA0MDgzNGM2Nzk2NmE1ZjBlZjRkYzQifQ%3D%3D;krchoasss=eyJpdiI6ImpzZFpiY2h6TzlKdkFldnViZFBjNHc9PSIsInZhbHVlIjoiY1MyTk1Xb2JwUDFSUDU3cnBQWFdMWE5mQmQremxaS1BhWmptSVdFbDNjVHRBNUZHSlByN01HUmsrWHo5RFdkNlVab1dqN2xlY1hMOXR2dndDcFpQalE9PSIsIm1hYyI6ImJiN2JlN2RkZTFmN2UzODFkNTMwYmI3ZmVjNDYzYTE4M2UzZWUxZjc1YWU5ZjczMDg2NGUzNmRiODZjNWM2OTQifQ%3D%3D;_krypton_session=eFVWSDFUSUkwVFQzeE90WHlJTThybjI1aHo1aFdITk5lNmhSRjE0MzRNSE9lQ0x5ckV2ZG50NUpyVlhsWGN3SWt2SURwMDRSTVlWQ2JLZkxIM0ZTdml6bWxHNnVTLzE2ek9YbDd1QWJRQmQzSndRbDc5ZmNlQzN4cUkrTHhjR05YTGl2TjJnZ1k5RTZnS0FmbnplM0NWNEhnSFJSaFdlaG12d0tGR0V2Q3dab0xESVM3czlnSVRSRGhTVFF4d0tWR1ArRWo3OC80cC81UFp1Z0g2L2JubmZWeU1OMzVKaWhxc1doMXhZbU5neC9CU214Q0Vpbkt4ZHhZdHRiR2xWSlNNbkgrODFZS25IS1ZZUWh5TlRhZk5kSGp5OWNsTWJycFBSUDhBU0IyQTQ9LS1qcEdEK2UyU3NXRDU2ekpqdFNJcklBPT0%3D--5af1909bdc5dbe0f0acb7e88f9ad6be716627ba1;"
		};
		String url = "https://rong.36kr.com/api/company/218435";
		for (int i = 0; i < cookieArray.length; i++) {
			String source = "";
			String cookie = cookieArray[i];
			source = reader.readSource(url,"",cookie);
			System.out.println(source);
		}
		reader.close();
	}
	
}
