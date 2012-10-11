package com.waveface.android.testflighter.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;


public class StringUtil {
	/**
	 * SimpleDateFormat pattern for an ISO 8601 date
	 */
	public static String ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
	public static String TIME_FORMAT = "hh:mm a";
	public static String MEDIA_DATE_FORMAT = "yyyyMMdd";

	/**
	 * Parse an ISO 8601 date converting ParseExceptions to a null result;
	 */
	public static Date parseDate(String string) {
		DateTimeFormatter parser = ISODateTimeFormat.dateTimeNoMillis();
		return parser.parseDateTime(string).toDate();
	}

    public static Date parse( String input ) throws java.text.ParseException {
        //NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
        //things a bit.  Before we go on we have to repair this.
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" );

        //this is zero time so we need to add that TZ indicator for
        if ( input.endsWith( "Z" ) ) {
            input = input.substring( 0, input.length() - 1) + "GMT-00:00";
        } else {
            int inset = 6;

            String s0 = input.substring( 0, input.length() - inset );
            String s1 = input.substring( input.length() - inset, input.length() );

            input = s0 + "GMT" + s1;
        }

        return df.parse( input );
    }

     public static String changeToLocalString(String input) {
    	Date date = null;
		try {
			date = parse(input);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long selectedDate = date.getTime();
        long offsetFromUtc =Calendar.getInstance().getTimeZone().getOffset(0);
		date = new Date(selectedDate - offsetFromUtc*2);
        return formatDate(date);
    }

	public static String getWeekDayOfTimestamp(String timestamp,Locale locale){
		Date date = parseDate(timestamp);
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		cal.setTime(date);
		int DOW = cal.get(Calendar.DAY_OF_WEEK);
		String DOWString = "";
		switch(DOW){
			case 1:
				if(locale.getLanguage().equals("zh")){
					DOWString = "星期日";
				}
				else{
					DOWString = "Sunday";
				}
				break;
			case 2:
				if(locale.getLanguage().equals("zh")){
					DOWString = "星期一";
				}
				else{
					DOWString = "Monday";
				}
				break;
			case 3:
				if(locale.getLanguage().equals("zh")){
					DOWString = "星期二";
				}
				else{
					DOWString = "Tuesday";
				}
				break;
			case 4:
				if(locale.getLanguage().equals("zh")){
					DOWString = "星期三";
				}
				else{
					DOWString = "Wednesday";
				}
				break;
			case 5:
				if(locale.getLanguage().equals("zh")){
					DOWString = "星期四";
				}
				else{
					DOWString = "Thursday";
				}
				break;
			case 6:
				if(locale.getLanguage().equals("zh")){
					DOWString = "星期五";
				}
				else{
					DOWString = "Friday";
				}
				break;
			case 7:
				if(locale.getLanguage().equals("zh")){
					DOWString = "星期六";
				}
				else{
					DOWString = "Saturday";
				}
				break;
		}
		DateFormat longFormat = DateFormat.getDateInstance(DateFormat.LONG,
				locale);
		String elapsedTime = longFormat.format(date);

		if (locale.getLanguage().equals("zh")) {
			int pos = elapsedTime.indexOf("年");
			if (pos != -1) {
				String tempString = elapsedTime.substring("年".length() + pos)
						+ "," + elapsedTime.substring(0, pos);
				elapsedTime = tempString;
			} else {
				longFormat = DateFormat.getDateInstance(DateFormat.LONG,
						Locale.US);
				elapsedTime = longFormat.format(date);
			}
		}
		DOWString += ", "+elapsedTime;
		return DOWString;
	}

	public static String getDeviceView(String from,String codeName,Locale locale){
		String deviceViewString = null;
		if(Locale.getDefault().getLanguage().equals("zh")){
			deviceViewString = from +codeName;
		}
		else{
			deviceViewString = from +" "+ codeName;
		}
		return deviceViewString;
	}

	public static String incrementSeconds(String timestamp, int seconds) {
		Date date = parseDate(timestamp);
		long longDate =date.getTime();
		longDate +=seconds*1000;
		Date date2 = new Date(longDate);
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
		cal.setTime(date2);
		return(cal.get(Calendar.YEAR)
				+ "-" + addZeroPrefix(cal.get(Calendar.MONTH)+1)
				+ "-" + addZeroPrefix(cal.get(Calendar.DATE))
				+ "T" + addZeroPrefix(cal.get(Calendar.HOUR_OF_DAY))
				+ ":" + addZeroPrefix(cal.get(Calendar.MINUTE))
				+ ":" + addZeroPrefix(cal.get(Calendar.SECOND))
				+ "Z");
	}

	public static String addZeroPrefix(int value){
		if(value<=9){
			return "0"+value;
		}
		else{
			return ""+value;
		}
	}

	public static String getTime(String string) {
		DateTimeFormatter parser = ISODateTimeFormat.dateTimeNoMillis();
		return new SimpleDateFormat(TIME_FORMAT).format(parser.parseDateTime(
				string).toDate());
	}

	public static String getTimeWithFromat(String string, String format) {
		DateTimeFormatter parser = ISODateTimeFormat.dateTimeNoMillis();
		return new SimpleDateFormat(format).format(parser.parseDateTime(
				string).toDate());
	}

	public static String getPostInfo(String timestamp, int attachmentCount,
			String photoString, String from, String device) {
		String elapsedTime = StringUtil.getTime(timestamp);
		StringBuilder builder = new StringBuilder();
		if (Locale.getDefault().getLanguage().equals("zh")) {
			if (attachmentCount > 0) {
				builder.append(attachmentCount);
				builder.append(photoString);
				// builder.append(", ");
			}
			builder.append(" ").append(elapsedTime).append(from).append(device);
		} else {
			if (attachmentCount > 0) {
				builder.append(attachmentCount);
				builder.append(" ");
				builder.append(photoString);
				builder.append(" ");
			}
			builder.append("Posted at ").append(elapsedTime).append(" ")
					.append(from).append(" ").append(device);
		}
		return builder.toString();
	}

	public static String changeDateOutput(String dateString, Locale locale) {
		// Locale locale = new Locale(localeString1, localeString2);
		// Locale locale = Locale.TAIWAN;
		DateFormat longFormat = DateFormat.getDateInstance(DateFormat.LONG,
				locale);
		String elapsedTime = longFormat
				.format(StringUtil.parseDate(dateString));
		if (locale.getLanguage().equals("zh")) {
			int pos = elapsedTime.indexOf("年");
			if (pos != -1) {
				String tempString = elapsedTime.substring("年".length() + pos)
						+ "," + elapsedTime.substring(0, pos);
				elapsedTime = tempString;
			} else {
				longFormat = DateFormat.getDateInstance(DateFormat.LONG,
						Locale.US);
				elapsedTime = longFormat.format(StringUtil
						.parseDate(dateString));
				// String[] monthday = elapsedTime.split(",")[0].split(" ");
				// String year = elapsedTime.split(",")[1];
			}
		}
		return elapsedTime;
	}
	public static String getSimpleDate(String dateString) {
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormatGmt.format(StringUtil.parseDate(dateString))+"";
	}


	public static String changeLocalDateOutput(String dateString, Locale locale) {
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat(DATE_FORMAT);
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormatGmt.format(StringUtil.parseDate(dateString))+"";
	}

	public static String changeLocalDateOutputByTimeZone(String dateString, Locale locale) {
		TimeZone tz = TimeZone.getDefault();
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat(DATE_FORMAT,locale);
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone(tz.getID()));
		return dateFormatGmt.format(StringUtil.parseDate(dateString))+"";
	}
	public static String[] getMonthAndDay(String dateString, Locale locale) {
		String[] values = new String[2];
		// Locale locale = new Locale(localeString1, localeString2);
		// Locale locale = Locale.TAIWAN;
		DateFormat longFormat = DateFormat.getDateInstance(DateFormat.LONG,
				locale);
		String elapsedTime = longFormat
				.format(StringUtil.parseDate(dateString));
		if (locale.getLanguage().equals("zh")) {
			int pos = elapsedTime.indexOf("年");
			if (pos != -1) {
				String tempString = elapsedTime.substring("年".length() + pos);
				elapsedTime = tempString;
				values[0] = tempString.substring(0, tempString.indexOf("月")
						+ "月".length());
				elapsedTime = tempString.substring(tempString.indexOf("月")
						+ "月".length());
				values[1] = elapsedTime.substring(0, elapsedTime.indexOf("日"));
			} else {
				longFormat = DateFormat.getDateInstance(DateFormat.LONG,
						Locale.US);
				elapsedTime = longFormat.format(StringUtil
						.parseDate(dateString));
				elapsedTime = elapsedTime
						.substring(0, elapsedTime.indexOf(","));
				values = elapsedTime.split(" ");
				values[0] = values[0].toUpperCase().substring(0, 3);
			}
		} else {
			elapsedTime = elapsedTime.substring(0, elapsedTime.indexOf(","));
			values = elapsedTime.split(" ");
			values[0] = values[0].toUpperCase().substring(0, 3);
		}
		if (values[1].length() == 1) {
			values[1] = "0" + values[1];
		}

		return values;
	}

	/**
	 * Format a date as an ISO 8601 string, return "" for a null date
	 */
	public static String formatDate(Date date) {
		if (date == null) {
			return "";
		}
		return new SimpleDateFormat(ISO_8601_DATE_FORMAT).format(date);
	}

	/**
	 * Format a date as an ISO 8601 string, return "" for a null date
	 */
	public static String getLocalDate() {
		SimpleDateFormat dateFormatGmt = new SimpleDateFormat(ISO_8601_DATE_FORMAT);
		dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		return dateFormatGmt.format(new Date())+"";
	}



	public static boolean before(String ISO8601_DATE1, String ISO8601_DATE2) {
		if(ISO8601_DATE1 == null)
			return true;
		return before(parseDate(ISO8601_DATE1), parseDate(ISO8601_DATE2));
	}

	public static boolean before(Date date1, Date date2) {
		return date1.before(date2);
	}

	public static String replaceAllWords(String original, String find,
			String replacement) {
		StringBuilder result = new StringBuilder(original.length());
		String delimiters = "+-*/(),. ";
		StringTokenizer st = new StringTokenizer(original, delimiters, true);
		while (st.hasMoreTokens()) {
			String w = st.nextToken();
			if (w.equals(find)) {
				result.append(replacement);
			} else {
				result.append(w);
			}
		}
		return result.toString();
	}

	public static String replaceEscape(String ori) {
		// Server should decrese this
		ori = ori.replaceAll("\\\\n", "");
		ori = ori.replace("\"{", "{");
		ori = ori.replace("}\"", "}");
		// unescape
		// StringEscapeUtils.unescapeHtml(ori);
		ori = ori.replaceAll("\\\\", "");
		ori = ori.replaceAll("\\\\/", "/");
		return ori;
	}

	public static String replaceHtmlExcapeString(String ori) {
		ori = ori.replaceAll("\\\\\"", "\\\"");
		// ori = ori.replaceAll("\\<.*?>","");
		// ori = ori.replaceAll("&nbsp;","");
		// ori = ori.replaceAll("&amp;","");
		return ori;
	}




	public static String generateTag(String mainTag, String subTag) {
		return mainTag + '_' + subTag;
	}

	public static String getMainTag(String tag) {
		String mainTag = null;
		if (tag != null) {
			int divider = tag.indexOf('_');
			if (divider >= 0) {
				mainTag = tag.substring(0, divider);
			}
		}
		return mainTag;
	}

	public static String getSubTag(String tag) {
		String subTag = null;
		if (tag != null) {
			int divider = tag.indexOf('_');
			if (divider >= 0) {
				subTag = tag.substring(divider + 1, tag.length());
			}
		}
		return subTag;
	}


	public static synchronized String getUUID() {
		return UUID.randomUUID().toString();
	}

	public static synchronized String[] getUUIDS(int count) {
		String[] uuids = new String[count];
		for(int i = 0 ; i < count;i++){
			uuids[i] = UUID.randomUUID().toString();
		}
		return uuids;
	}

	public static String generateImageName() {
		Calendar cal = Calendar.getInstance();
		return "IMG_" + new SimpleDateFormat("yyyyMMdd").format(cal.getTime())
				+ "_" + new SimpleDateFormat("HHmmss").format(cal.getTime());
	}

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文標示
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
    public static String getPath(String filename){
//    	return "file:/"+filename.substring(0,filename.lastIndexOf(File.separator)+1);
    	String[] arrays = filename.split("/");
    	String rtnString = "file:";
    	for(int i= 0 ; i < arrays.length;i++){
    		if(!arrays[i].equals("")){
    			rtnString +="\\"+arrays[i];
    		}
    	}
    	return rtnString;
    }


	public static String getEndDate(String strQueryDate) {
		String[] result = { "" };
		String startDateTime = strQueryDate + "000000000";

		try {
			java.util.Calendar calendar = java.util.Calendar.getInstance();
			calendar.set(java.util.Calendar.YEAR,
					new Integer(startDateTime.substring(0, 4)).intValue());
			calendar.set(java.util.Calendar.MONTH,
					new Integer(startDateTime.substring(4, 6)).intValue() - 1);
			calendar.set(java.util.Calendar.DATE,
					new Integer(startDateTime.substring(6, 8)).intValue());
			calendar.set(java.util.Calendar.HOUR_OF_DAY, new Integer(
					startDateTime.substring(8, 10)).intValue());
			calendar.set(java.util.Calendar.MINUTE,
					new Integer(startDateTime.substring(10, 12)).intValue());
			calendar.set(java.util.Calendar.SECOND,
					new Integer(startDateTime.substring(12, 14)).intValue());
			startDateTime = String.valueOf(calendar.getTimeInMillis())
					.substring(0, 10);

		} catch (Exception e) {
			startDateTime = "";

		} finally {
			result[0] = startDateTime;

		}
		return startDateTime;

	}





	public static  String getConverDate(Long date) {
		SimpleDateFormat sdf = new SimpleDateFormat(MEDIA_DATE_FORMAT);
		// DateFormat df = DateFormat.getDateInstance();
		return sdf.format(new Date(date * 1000));
	}

	public static  String getConverDate(Long date,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		// DateFormat df = DateFormat.getDateInstance();
		return sdf.format(new Date(date * 1000));
	}

}
