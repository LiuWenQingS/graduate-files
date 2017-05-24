package com.hiekn.main;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class StartKrNews {
	public static void main(String[] args) {
		ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
		for (int i = 0; i < 1; i++) {
			pool.submit(new KrNewsCrawler(5000000));
		}
	}
}
