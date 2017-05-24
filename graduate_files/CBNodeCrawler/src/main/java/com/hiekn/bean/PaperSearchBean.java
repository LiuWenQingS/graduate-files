package com.hiekn.bean;

public class PaperSearchBean extends SearchBean {
	private String dbPrefix = "CCND";
	private String dbCatalog = "%e4%b8%ad%e5%9b%bd%e9%87%8d%e8%a6%81%e6%8a%a5%e7%ba%b8%e5%85%a8%e6%96%87%e6%95%b0%e6%8d%ae%e5%ba%93";
	private String configFile = "CCND.xml";
	private String dbOpt = "CCND";
	private String dbValue = "%E4%B8%AD%E5%9B%BD%E9%87%8D%E8%A6%81%E6%8A%A5%E7%BA%B8%E5%85%A8%E6%96%87%E6%95%B0%E6%8D%AE%E5%BA%93";
	private String searchPrefix = "SU";
	private String name = "paper";
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
