package com.hiekn.bean;

import java.util.ArrayList;

public class ContentBean {
	private String title;
	private String author;
	private String institute;
	private String abstracts;
	private String url;
	private ArrayList<String> keywords;
	private String publishTime;
	/**
	 * 来源
	 */
	private String source;
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getTitle() {
		return title;
	}
	public ArrayList<String> getKeywords() {
		return keywords;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setKeywords(ArrayList<String> keywords) {
		this.keywords = keywords;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getInstitute() {
		return institute;
	}
	public void setInstitute(String institute) {
		this.institute = institute;
	}
	public String getAbstracts() {
		return abstracts;
	}
	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

}
