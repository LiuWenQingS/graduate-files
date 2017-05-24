package com.hiekn.bean;

import java.util.List;

public class ItjzNewsBean {
	private String url;
	private String itjzUrl;
	private String title;
	private String content;
	private List<String> tagList;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getItjzUrl() {
		return itjzUrl;
	}
	public void setItjzUrl(String itjzUrl) {
		this.itjzUrl = itjzUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<String> getTagList() {
		return tagList;
	}
	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}
}
