package com.socialreputation.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtility {

	private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	public static String fromDate(final Date date) {
		DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		return dateFormat.format(date);
	}

	public static String nowAsString() {
		return fromDate(new Date());
	}

	public static long nowAsMilis() {
		return new Date().getTime();
	}
}
