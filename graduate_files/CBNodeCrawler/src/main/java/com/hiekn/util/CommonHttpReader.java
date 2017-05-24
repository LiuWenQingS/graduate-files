package com.hiekn.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 */
public abstract class CommonHttpReader implements HttpReader {

    public String readSource(String url) { return readSource(url, null); }

    public String readSource(String url, String charset) { return readSource(url, charset, null); }
    
    public String readSource(String url, String charset, String cookies) { return readSource(url, charset, cookies, HTTP_GET, null); }
    
    public String parseCharset(String pageSource) {
    	Matcher charsetMatcher;
		// <meta http-equiv="Content-Type" content="text/html" charset="UTF-8" />
		// <meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
		// <meta charset="gb2312" />
    	charsetMatcher = Pattern.compile("(?i)<meta.*?charset=['\"]?(\\S+?)['\"]+?").matcher(pageSource);
    	if (charsetMatcher.find()) return charsetMatcher.group(1).toLowerCase();
        
    	// <meta http-equiv='Content-Language' content='utf-8'/>
    	charsetMatcher = Pattern.compile("(?i)<meta.*?content=['\"]?(\\S+?)['\"]+?").matcher(pageSource);
    	if (charsetMatcher.find()) return charsetMatcher.group(1).toLowerCase().replace("no-cache", "");
    	
        return "";
    }
    
}
