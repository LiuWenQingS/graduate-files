package com.hiekn.util;

import java.io.Closeable;

import org.apache.log4j.Logger;

/**
 * 
 *
 *
 *
 * @author pzn
 * @since 1.7
 * @version 1.0
 */
public interface HttpReader extends Closeable {

    /* logger */
    Logger LOGGER = Logger.getLogger(HttpReader.class);
    
    /* static final variables */
    String HTTP_GET = "get";
    String HTTP_POST = "post";
    
    String STATIC_ENGINE = "static";
    String AJAX_ENGINE = "ajax";
    String CUSTOM_ENGINE = "custom";
    
    String CLICK_STRATEGY_LINKURL = "linkUrl";
    String CLICK_STRATEGY_REFERFIELD = "referField";
    String CLICK_STRATEGY_LINKDOM = "linkDom";
    /* static final variables */
    
    /**
     *
     * read url source code
     *
     * @param url
     * @return
     */
    String readSource(String url);

    /**
     *
     * read url source code with charset
     *
     * @param url
     * @param charset
     * @return
     */
    String readSource(String url, String charset);

    /**
     *
     * read url source code with charset and cookie
     *
     * @param url
     * @param charset
     * @param cookies
     * @return
     */
    String readSource(String url, String charset, String cookies);
    
    /**
     * 
     * read url source code with charset, requestType, params, cookies and filter.
     * 
     * @param url
     * @param charset
     * @param cookies
     * @param requestType 
     * @param params
     * @return
     */
    String readSource(String url, String charset, String cookies, String requestType, String params);
    
    /**
     *
     * release http resource.
     *
     */
    void close();

}
