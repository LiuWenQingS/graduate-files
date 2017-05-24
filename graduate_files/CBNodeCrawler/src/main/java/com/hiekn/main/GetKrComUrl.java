package com.hiekn.main;

import java.io.FileWriter;

import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;

public class GetKrComUrl implements Runnable{
	private int num;
	
	public GetKrComUrl(int num) {
		this.num = num;
	}

	public void getUrl() throws Exception {
		FileWriter fw = new FileWriter("data/kr/kr_com_"+num+".txt");
		HttpReader reader = new StaticHttpReader();
		for (int i = 22000*num; i < 22000*num+22000; i++) {
			JSONObject obj = new JSONObject();
			String basicUrl = "https://rong.36kr.com/api/company/"+i;
			String source = reader.readSource(basicUrl);
			if (hasCompany(source)) {
				System.out.println(basicUrl);
				obj.put("url", basicUrl);
				obj.put("basic", source);
				fw.write(obj.toJSONString()+"\r\n");
				fw.flush();
			}
		}
		reader.close();
		fw.close();
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

	public void run() {
		// TODO Auto-generated method stub
		try {
			getUrl();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
