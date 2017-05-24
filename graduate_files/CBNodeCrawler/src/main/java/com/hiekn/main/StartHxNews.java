package com.hiekn.main;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class StartHxNews {
	public static void main(String[] args) {
		ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
		for (int i = 0; i < 4; i++) {
			pool.submit(new HxNewsCrawler(i*40000));
		}
	}
}
