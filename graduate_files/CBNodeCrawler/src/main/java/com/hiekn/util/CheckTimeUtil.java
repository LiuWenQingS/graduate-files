package com.hiekn.util;

public class CheckTimeUtil {
	public static void main(String[] args) {
		System.out.println(correctTime("2099-09-17"));
	}
	
	public static String correctTime(String formatTime) {
		String correctTime = "";
		String[] formatTimeArr = formatTime.split("-");
		int year = Integer.valueOf(formatTimeArr[0]);
		int month = Integer.valueOf(formatTimeArr[1]);
		int day = Integer.valueOf(formatTimeArr[2]);
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			if (day > 30) {
				day = 30;
			}
		} else if (month == 2) {
			if (((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)) {
				if (day > 29) {
					day = 29;
				}
			} else {
				if (day > 28) {
					day = 28;
				}
			}
		}
		if (String.valueOf(day).length() == 1) {
			correctTime = formatTimeArr[0] + "-" + formatTimeArr[1] + "-0" + day;
		} else {
			correctTime = formatTimeArr[0] + "-" + formatTimeArr[1] + "-" + day;
		}
		return correctTime;
	}
}
