package com.hiekn.main;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


public class StartKrComUrl {
	public static void main(String[] args) {
		ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
		for (int i = 10; i < 11; i++) {
			pool.submit(new GetKrComUrl(i));
		}
	}
}
