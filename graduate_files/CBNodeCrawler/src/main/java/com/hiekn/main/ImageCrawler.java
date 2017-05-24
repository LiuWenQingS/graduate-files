package com.hiekn.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.DownloadImageUtil;

public class ImageCrawler {
	public static void main(String[] args) {
//		downloadProductPic();
		try {
			downloadComPic();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void downloadComPic() throws IOException {
		int count = 0;
		BufferedReader br = BufferedReaderUtil.getBuffer("data/img_list/person_img.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			System.out.println(count++);
			String[] split = input.split("\t");
			if (split.length > 1) {
				String imageUrl = split[1];
				if (imageUrl.equals("https://www.itjuzi.com/assets/front/images/img/icon-person.png")) continue;
				String comName = split[0];
				DownloadImageUtil.downloadPic(imageUrl, comName+".jpg", "data/image_person/");
			}
		}
	}
	
	public static void downloadProductPic() {
		int count = 0;
		Map<String,String> productImageMap = getProductImageMap();
		for (Entry<String,String> entry : productImageMap.entrySet()) {
			String productName = entry.getKey();
			String imageUrl = entry.getValue();
			System.out.println(count++);
			DownloadImageUtil.downloadPic(imageUrl, productName+".jpg", "data/image_product/");
		}
	}
	
	public static Map<String,String> getProductImageMap() {
		Map<String,String> productImageMap = new HashMap<String,String>();
		BufferedReader br = null;
		String input = "";
		try {
			br = BufferedReaderUtil.getBuffer("data/dp/com_final.txt");
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				if (obj.containsKey("product")) {
					JSONArray arr = obj.getJSONArray("product");
					for (Object object : arr) {
						JSONObject productObj = (JSONObject) object;
						String productName = productObj.getString("name");
						if (productObj.containsKey("image_url")) {
							String imageUrl = productObj.getString("image_url");
							productImageMap.put(productName, imageUrl);
						}
					}
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productImageMap;
	}
	
	public static Map<String,String> getComImageMap() {
		Map<String,String> comImageMap = new HashMap<String,String>();
		BufferedReader br = null;
		String input = "";
		try {
			br = BufferedReaderUtil.getBuffer("data/dp/com_final.txt");
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				if (!obj.containsKey("gs")) continue;
				String comName = obj.getJSONObject("gs").getString("name");
				if (obj.containsKey("origin")) {
					JSONObject comObj = JSONObject.parseObject(obj.getString("origin"));
					if (comObj.containsKey("image_url")) {
						String url = comObj.getString("image_url");
						comImageMap.put(comName, url);
					}
				}
			}
			br.close();
		} catch (Exception e) {
			System.out.println(input);
			e.printStackTrace();
		}
		return comImageMap;
	}
}
