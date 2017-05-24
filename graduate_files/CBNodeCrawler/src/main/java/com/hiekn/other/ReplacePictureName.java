package com.hiekn.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class ReplacePictureName {
	public static void main(String[] args) {
		try {
//			comPicProcess();
//			proPicProcess();
//			copyDefaultComImage();
			copyDefaultInvestImage();
//			copyDefaultProductImage();
//			rename();
//			checkErrorPic();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void checkErrorPic() throws Exception {
		File file = new File("data/error_pic/");
		File[] fileArr = file.listFiles();
		for (File file2 : fileArr) {
			String name = file2.getName();
			long id = Long.valueOf(name.replace(".jpg", ""));
			String comName = findName(id);
			String sourcePath = findPicPath(comName);
			if (!sourcePath.equals("")) {
				File srcFile = new File(sourcePath);
				String fileName = id + ".jpg";
				int byteread = 0; // 读取的字节数  
			    InputStream in = new FileInputStream(srcFile);  
			    OutputStream out = new FileOutputStream(new File("data/error_pic/" + fileName));
			    byte[] buf = new byte[1024];
			    while ((byteread = in.read(buf))!= -1) {
			    	out.write(buf,0,byteread);
			    }
			    in.close();
			    out.close();
			}
			System.out.println(id);
		}
	}
	
	
	public static String findPicPath(String name) throws Exception {
		String path = "";
		File file = new File("data/image_com_bac");
		File[] fileArr = file.listFiles();
		for (File file2 : fileArr) {
			String fileName = file2.getName().replace(".jpg", "");
			if (fileName.equals(name)) {
				path = file2.getPath();
			}
		}
		return path;
	}
	
	
	
	public static String findName(long id) throws Exception {
		String name = "";
		MongoClient client = new MongoClient("192.168.1.189",19130);
		DB db = client.getDB("kg_cbnode_c");
		DBCollection collection = db.getCollection("entity_id");
		BasicDBObject searchObj  = new BasicDBObject("id",id);
		DBCursor cursor = collection.find(searchObj);
		try {
			while (cursor.hasNext()) {
				name = cursor.next().get("name").toString();
			}
		} catch (Exception e) {
			cursor.close();
		}
		client.close();
		return name;
	}
	
	public static long findConcept(long id) throws Exception {
		long concept = 0L;
		MongoClient client = new MongoClient("192.168.1.189",19130);
		DB db = client.getDB("kg_cbnode_c");
		DBCollection collection = db.getCollection("entity_id");
		BasicDBObject searchObj  = new BasicDBObject("id",id);
		DBCursor cursor = collection.find(searchObj);
		try {
			while (cursor.hasNext()) {
				concept = (Long) cursor.next().get("concept_id");
			}
		} catch (Exception e) {
			cursor.close();
		}
		return concept;
	}
	
	
	public static void rename() throws Exception {
		Map<String,String> map = getPeoIdMap();
		File file = new File("data/itjz/people/");
		File[] fileArr = file.listFiles();
		for (File file2 : fileArr) {
			String name = file2.getName().replace(".jpg", "");
			String path = file2.getParent();
			if (map.containsKey(name)) {
				File newFile = new File(path + File.separator + map.get(name) + ".jpg");
				file2.renameTo(newFile);
			} else {
				System.out.println(name);
			}
//			if (file2.isDirectory()) {
//				File[] fileArr2 = file2.listFiles();
//				for (File file3 : fileArr2) {
//					File newFile = new File(path + File.separator + name + ".jpg");
//					file3.renameTo(newFile);
//				}
//			}
		}
	}
	
	public static Map<String,String> getUrlIdMap() throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/itjz/new_url.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			String[] split = input.split("\t");
			String key = split[1].replace("http://www.itjuzi.com/images/", "");
			String value = split[0];
			map.put(key, value);
		}
		br.close();
		return map;
	}
	
	public static Map<String,String> getPeoIdMap() throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/itjz/people_id.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			String[] split = input.split("\t");
			String key = split[1];
			String value = split[0];
			map.put(key, value);
		}
		br.close();
		return map;
	}
	
	public static void copyDefaultInvestImage() throws Exception {
		File srcFile = new File("data/invest_default_image_new/default.jpg");
		Set<Long> allComIdSet = getNewInvestId();
		System.out.println(allComIdSet.size());
		for (Long long1 : allComIdSet) {
			String fileName = long1 + ".jpg";
			int byteread = 0; // 读取的字节数  
		    InputStream in = new FileInputStream(srcFile);  
		    OutputStream out = new FileOutputStream(new File("data/invest_default_image_new/" + fileName));
		    byte[] buf = new byte[1024];
		    while ((byteread = in.read(buf))!= -1) {
		    	out.write(buf,0,byteread);
		    }
		    in.close();
		    out.close();
		}
	}
	
	public static void copyDefaultProductImage() throws Exception {
		File srcFile = new File("data/product_default_image/default.jpg");
		Set<Long> allProductIdSet = getAllProductId();
		System.out.println(allProductIdSet.size());
		for (Long long1 : allProductIdSet) {
			String fileName = long1 + ".jpg";
			int byteread = 0; // 读取的字节数  
			InputStream in = new FileInputStream(srcFile);  
			OutputStream out = new FileOutputStream(new File("data/product_default_image/" + fileName));
			byte[] buf = new byte[1024];
			while ((byteread = in.read(buf))!= -1) {
				out.write(buf,0,byteread);
			}
			in.close();
			out.close();
		}
	}
	
	private static Set<Long> getAllProductId() {
		Set<Long> productIdSet = new HashSet<Long>();
		BufferedReader br = null;
		String input = "";
		try {
			br = BufferedReaderUtil.getBuffer("data/import_product.txt");
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				if (!obj.containsKey("source")) continue;
				long id = obj.getLongValue("_id");
				productIdSet.add(id);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return productIdSet;
	}

	private static Set<Long> getAllInvestId() {
		Set<Long> investIdSet = new HashSet<Long>();
		BufferedReader br = null;
		String input = "";
		try {
			br = BufferedReaderUtil.getBuffer("data/import_invest.txt");
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				if (!obj.containsKey("source")) continue;
				long id = obj.getLongValue("_id");
				investIdSet.add(id);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return investIdSet;
	}

	public static void copyDefaultComImage() throws Exception {
		File srcFile = new File("data/com_default_image/default.jpg");
		Set<Long> allComIdSet = getAllComId();
		System.out.println(allComIdSet.size());
		Set<Long> hasImageComIdSet = getHasImageComId();
		allComIdSet.removeAll(hasImageComIdSet);
		System.out.println(allComIdSet.size());
		for (Long long1 : allComIdSet) {
			String fileName = long1 + ".jpg";
			int byteread = 0; // 读取的字节数  
		    InputStream in = new FileInputStream(srcFile);  
		    OutputStream out = new FileOutputStream(new File("data/com_default_image/" + fileName));
		    byte[] buf = new byte[1024];
		    while ((byteread = in.read(buf))!= -1) {
		    	out.write(buf,0,byteread);
		    }
		    in.close();
		    out.close();
		}
	}
	
	private static Set<Long> getHasImageComId() {
		Set<Long> hasImageComIdSet = new HashSet<Long>();
		File file = new File("data/image_com");
		File[] fileArr = file.listFiles();
		for (File file2 : fileArr) {
			long id = Long.valueOf(file2.getName().replace(".jpg", ""));
			hasImageComIdSet.add(id);
		}
		return hasImageComIdSet;
	}

	private static Set<Long> getAllComId() {
		Set<Long> allComIdSet = new HashSet<Long>();
		String input = "";
		BufferedReader br = null;
		try {
			br = BufferedReaderUtil.getBuffer("data/fp_import_company.txt");
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				long id = obj.getLongValue("_id");
				allComIdSet.add(id);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return allComIdSet;
	}

	public static void proPicProcess() throws Exception {
		Map<String,Long> proIdMap = getProIdMap();
		Map<String,String> aliasMap = getAliasMap();
		File file = new File("data/test");
		File[] fileArr = file.listFiles();
		for (File file2 : fileArr) {
			String name = file2.getName().replace(".jpg", "").trim();
			String aliasName = "";
			if (aliasMap.containsKey(name)) {
				aliasName = aliasMap.get(name);
			}
			if (!aliasName.equals("")) {
				long id = 0;
				if (proIdMap.containsKey(aliasName)) {
					id = proIdMap.get(aliasName);
					String path = file2.getParent();
					File newFile = new File(path + File.separator + id + ".jpg");
					file2.renameTo(newFile);
				} else {
					System.out.println(aliasName);
				}
			}
		}
	}
	
	public static void getBacPic() throws Exception {
		Map<Long,String> idComMap = getIdComMap();
		File file = new File("data/image_com_bac");
		File[] fileArr = file.listFiles();
		for (File file2 : fileArr) {
			try {
				long id = Long.parseLong(file2.getName().replace(".jpg", ""));
				if (idComMap.containsKey(id)) {
					String name = idComMap.get(id);
					String path = file2.getParent();
					File newFile = new File(path + File.separator + name + ".PNG");
					file2.renameTo(newFile);
				} else {
					
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	public static void comPicProcess() throws Exception {
		Map<String,Long> comIdMap = getComIdMap();
		File file = new File("data/image_com_bac");
		File[] fileArr = file.listFiles();
		for (File file2 : fileArr) {
			String name = file2.getName().replace("（", "(").replace("）", ")");
			long id = 0;
			if (comIdMap.containsKey(name)) {
				id = comIdMap.get(name);
				String path = file2.getParent().replace("image_com_bac", "image_com");
				File newFile = new File(path + File.separator + id + ".jpg");
				file2.renameTo(newFile);
			} else {
				
			}
		}
	}
	
	public static Map<String,Long> getComIdMap() {
		Map<String,Long> comIdMap = new HashMap<String,Long>();
		BufferedReader br = null;
		String input = "";
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream("data/import_company.txt")));
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				long id = obj.getLongValue("_id");
				String comName = obj.getString("name");
				comIdMap.put(comName, id);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comIdMap;
	}
	
	public static Map<Long,String> getIdComMap() {
		Map<Long,String> idComMap = new HashMap<Long,String>();
		BufferedReader br = null;
		String input = "";
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream("data/import_company.txt")));
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				long id = obj.getLongValue("_id");
				String comName = obj.getString("name");
				idComMap.put(id, comName);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return idComMap;
	}
	
	public static Map<String,String> getAliasMap() {
		Map<String,String> aliasMap = new HashMap<String,String>();
		String input = "";
		BufferedReader br = null;
		try {
			br = BufferedReaderUtil.getBuffer("data/dp/product_name_map.txt");
			while ((input = br.readLine())!=null) {
				String[] arr = input.split("\t");
				if (input.contains("1号车位")) {
					System.out.println("sdf");
				}
				if (arr.length > 1) {
					aliasMap.put(arr[0], arr[1]);
				} else {
					System.out.println(input);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return aliasMap;
	}
	
	public static Map<String,Long> getProIdMap() {
		Map<String,Long> comIdMap = new HashMap<String,Long>();
		BufferedReader br = null;
		String input = "";
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream("data/import_product.txt")));
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				if (!obj.containsKey("_id")) continue;
				long id = obj.getLongValue("_id");
				String comName = obj.getString("name");
				comIdMap.put(comName, id);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return comIdMap;
	}
	
	public static Set<Long> getNewInvestId() {
		Set<Long> idSet = new HashSet<Long>();
		try {
			FileWriter fw = new FileWriter("data/itjz/invest_id.txt");
			MongoClient client = new MongoClient("192.168.1.189",19130);
			DB db = client.getDB("kg_cbnode_c");
			DBCollection collection = db.getCollection("entity_id");
			BasicDBObject searchObj = new BasicDBObject("concept_id",13L);
			DBCursor cursor = collection.find(searchObj);
			try {
				while (cursor.hasNext()) {
					DBObject obj = cursor.next();
					long id = (Long) obj.get("id");
					String name = obj.get("name").toString();
					if (id < 340716) continue;
					if (name.contains("未透露")) continue;
					idSet.add(id);
				}
			} catch (Exception e) {
				cursor.close();
			}
			fw.close();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return idSet;
	}
	
}
