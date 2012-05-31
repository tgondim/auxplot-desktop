package br.com.tg.gui.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarFormatter {
	private final static SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
	private final static SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");

	public static String formatDate(Object obj) {
		Calendar cal = (Calendar) obj;
		return dateFormatter.format(cal.getTime());
	}

	public static String formatTime(Object obj) {
		Calendar cal = (Calendar) obj;
		return timeFormatter.format(cal.getTime());
	}

	public static Object parse(String s) throws ParseException {
		Calendar cal = new GregorianCalendar();
		cal.setTime(dateFormatter.parse(s));
		return cal;
	}
}