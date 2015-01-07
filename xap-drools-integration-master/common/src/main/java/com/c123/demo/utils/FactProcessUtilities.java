package com.c123.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FactProcessUtilities {
	public static String DEFAULT_FORMAT = "dd_MM_yyyy";
	public static String getDateAndHour(Date date){
		String outcome = getFormattedDate(date);
		outcome += ":";
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
		outcome += hour;
		return outcome;
	}
	
	public static String getFormattedDate(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		SimpleDateFormat formatter = new SimpleDateFormat(DEFAULT_FORMAT);
		String formatDate = formatter.format(calendar.getTime());
		return formatDate;
	}
}
