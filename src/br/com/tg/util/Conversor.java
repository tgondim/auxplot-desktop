package br.com.tg.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Conversor {

	public static Calendar stringToCalendar(String strData) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		calendar.setTime((Date)formatter.parse(strData));
		return calendar;
	}
	
	public static String dateToMonth(Calendar data) {
		switch (data.get(Calendar.MONTH)) {
		case 0: 
			return "Janeiro";
		case 1: 
			return "Fevereiro";
		case 2: 
			return "Março";
		case 3: 
			return "Abril";
		case 4: 
			return "Maio";
		case 5: 
			return "Junho";
		case 6: 
			return "Julho";
		case 7: 
			return "Agosto";
		case 8: 
			return "Setembro";
		case 9: 
			return "Outubro";
		case 10: 
			return "Novembro";
		case 11: 
			return "Dezembro";
		}
		return "";
	}
	
}
