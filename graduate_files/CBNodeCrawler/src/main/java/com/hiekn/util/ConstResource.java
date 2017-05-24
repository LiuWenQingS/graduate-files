package com.hiekn.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * 配置信息
 * 
 * @author pzn
 * @version 1.0
 * @since 1.7
 */
public class ConstResource {

    static Properties props = new Properties();

    static {
        InputStream in = null;
        try {
            in = ConstResource.class.getClassLoader().getResourceAsStream("uyint.properties");
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    // 操作系统换行符
    public static final String NEW_LINE_CHARACTER = get("os").equals("unix") ? "\n" : "\r\n";
    
    // parser
    public static final int CONTENT_TITLE_MIN_LENGTH = Integer.parseInt(get("content.title.min.length"));
    public static final int CONTENT_MIN_LENGTH = Integer.parseInt(get("content.min.length"));
    public static final int CONTENT_MIN_CONFIRM_LENGTH = Integer.parseInt(get("content.min.confirm.length"));
    
    
    // rmi
    public static final String JAVA_RMI_METADATA_HOSTNAME = get("java.rmi.metadata.hostname");
    public static final String METADATA_RMI_SERVER_NAME = get("metadata.rmi.server.name");
    public static final int METADATA_RMI_REGISTRY_PORT = Integer.parseInt(get("metadata.rmi.registry.port"));
    
    public static final String JAVA_RMI_DEDUP_HOSTNAME = get("java.rmi.dedup.hostname");
    public static final String DEDUP_RMI_SERVER_NAME = get("dedup.rmi.server.name");
    public static final int DEDUP_RMI_REGISTRY_PORT = Integer.parseInt(get("dedup.rmi.registry.port"));
    
    public static final String JAVA_RMI_MONITOR_HOSTNAME = get("java.rmi.monitor.hostname");
    public static final String MONITOR_RMI_SERVER_NAME = get("monitor.rmi.server.name");
    public static final int MONITOR_RMI_REGISTRY_PORT = Integer.parseInt(get("monitor.rmi.registry.port"));
    
    public static final String MYSQL_URL = get("mysql_url");
	public static final String MYSQL_USER = get("mysql_user");
	public static final String MYSQL_PASSWORD = get("mysql_password");
    // spider num
    public static final int SPIDER_NUM = Integer.parseInt(get("spider.num"));
    
    // schedule spider number
    public static final int SCHEDULER_JOBS_NUM = Integer.parseInt(get("scheduler.jobs.num"));
    public static final int SCHEDULER_JOBS_PER_NUM = Integer.parseInt(get("scheduler.jobs.per.num"));
    
    // meta
    public static final String META_STORE_ENGINE = get("meta.store.engine");
    
    // get property
    public static String get(String key) { return props.getProperty(key); }

}
