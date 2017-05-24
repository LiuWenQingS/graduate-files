package com.hiekn.other;

import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;

public class JsoupTest {
	@Test
	public void t1() {
		HttpReader reader = new StaticHttpReader();
		String source = reader.readSource("http://www.huxiu.com/");
		Document doc = Jsoup.parse(source);
		Elements elements = doc.select("div.mod-info-flow > div.mod-b > div.mob-ctt > div.mob-author > a > span.author-name");
		for (Element element : elements) {
			System.out.println(element.text());
		}
		reader.close();
	}
	
	@Test
	public void t2() {
		String uid = UUID.randomUUID().toString().replace("-", "");
		System.out.println(uid);
	}
}
