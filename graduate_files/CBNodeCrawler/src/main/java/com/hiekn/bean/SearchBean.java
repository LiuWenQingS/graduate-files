package com.hiekn.bean;

public class SearchBean {
	private String dbPrefix = "";
	private String dbCatalog = "";
	private String configFile = "";
	private String dbOpt = "";
	private String dbValue = "";
	private String searchPrefix = "";
	private String name = "";
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSearchPrefix() {
		return searchPrefix;
	}
	public void setSearchPrefix(String searchPrefix) {
		this.searchPrefix = searchPrefix;
	}
	public String getDbPrefix() {
		return dbPrefix;
	}
	public void setDbPrefix(String dbPrefix) {
		this.dbPrefix = dbPrefix;
	}
	public String getDbCatalog() {
		return dbCatalog;
	}
	public void setDbCatalog(String dbCatalog) {
		this.dbCatalog = dbCatalog;
	}
	public String getConfigFile() {
		return configFile;
	}
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	public String getDbOpt() {
		return dbOpt;
	}
	public void setDbOpt(String dbOpt) {
		this.dbOpt = dbOpt;
	}
	public String getDbValue() {
		return dbValue;
	}
	public void setDbValue(String dbValue) {
		this.dbValue = dbValue;
	}
}
