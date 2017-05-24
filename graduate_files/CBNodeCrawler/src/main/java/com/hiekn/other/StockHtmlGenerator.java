package com.hiekn.other;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;

import com.hiekn.util.BufferedReaderUtil;

public class StockHtmlGenerator {
	public static void main(String[] args) {
		try {
			oldStockPatch();
			newStock();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void oldStockPatch() throws Exception {
		FileWriter fw = new FileWriter("data/stock/correct_html.txt");
		String input = "";
		BufferedReader br = BufferedReaderUtil.getBuffer("data/stock/stock_html.txt");
		while ((input = br.readLine())!=null) {
			String name = Jsoup.parse(input).text();
			System.out.println(name);
			String comName = getComName(name);
			String id = getId(input);
			String urlP = "<li><a name='stock' id='"+id+"' href='stock.html?"+comName+"'>"+name+"</a></li>";
			fw.write(urlP + "\r\n");
			fw.flush();
		}
		fw.close();
		br.close();
	}
	
	public static String getId(String input) {
		String id = "";
		String regex = "(?<=id\\=')[\\S]*(?=\')";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		while (m.find()) {
			id = m.group();
		}
		return id;
	}
	
	public static String getComName(String input) {
		String comName = "";
		String regex = "[\\S].*(?=\\()";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		while (m.find()) {
			comName = m.group();
		}
		return comName;
	}

	private static void newStock() throws Exception {
		FileWriter fw = new FileWriter("data/stock/correct_html.txt",true);
		BufferedReader br = BufferedReaderUtil.getBuffer("data/stock/a.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			String[] split = input.split("\t");
			String id = split[0];
			String simpleName = split[2];
			String s = split[6].trim();
			String code = s.split("_-_")[1].trim();
			String urlP = "<li><a name='stock' id='"+id+"' href='stock.html?"+simpleName+"'>"+simpleName+"("+code+")</a></li>";
			fw.write(urlP + "\r\n");
			fw.flush();
		}
		fw.close();
	}

	
}
