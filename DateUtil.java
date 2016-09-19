package cn.sinobest.ypgj.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 * 
 * @author hexiaowei
 * 
 */
public class DateUtil {

	/**
	 * 将Date类型转换为字符串 如:"2002-07-01 11:40:02"
	 * 
	 * @param date
	 *            日期类型
	 * @return 日期字符串
	 */
	public static String format(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 将Date类型转换为字符串
	 * 
	 * @param date
	 *            日期类型
	 * @param pattern
	 *            字符串格式
	 * @return 日期字符串
	 */
	public static String format(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		if (pattern == null || pattern.equals("")) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		return new java.text.SimpleDateFormat(pattern).format(date);
	}

	/**
	 * 将字符串转换为Date类型
	 * 
	 * @param date
	 *            字符串类型
	 * @return 日期类型
	 */
	public static Date format(String date) {
		return format(date, null);
	}

	/**
	 * 将字符串转换为Date类型
	 * 
	 * @param date
	 *            字符串类型
	 * @param pattern
	 *            格式
	 * @return 日期类型
	 */
	public static Date format(String date, String pattern) {
		if (pattern == null || pattern.equals("") || pattern.equals("null")) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		if (date == null || date.equals("") || date.equals("null")) {
			return new Date();
		}
		Date d = null;
		try {
			d = new java.text.SimpleDateFormat(pattern).parse(date);
		} catch (ParseException pe) {
		}
		return d;
	}

    /**
     * 日期格式字符串转UTC时间格式
     * UTC时间格式转换成正常日期格式的字符串并返回改字符串
     * @param date
     * @param pattern
     * @return
     */
    public static String UTCDate(String date, String pattern) {
        if (pattern == null || pattern.equals("") || pattern.equals("null")) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        if (date == null || date.equals("") || date.equals("null")) {
            return sdf.format(new Date());
        }
        Date d = null;
        String utcString = "";
        try {
            d = sdf.parse(date);
            Calendar cal = sdf.getCalendar();
            //1、取得时间偏移量：
            int zoneOffset = cal.get(Calendar.ZONE_OFFSET);

            //2、取得夏令时差：
            int dstOffset = cal.get(Calendar.DST_OFFSET);

            //3、从传入的时间里扣除这些差量，即可以取得UTC时间：
            cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));

            utcString = sdf.format(new Date(cal.getTimeInMillis()));
            System.out.println("UTC转正常日期格式:"+utcString);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return utcString;
    }

    /**
     * 得到两个date相差的天数
     *
     * @param now
     * @param returnDate
     * @return
     */
    public static int daysBetween(Date now, Date returnDate)
    {
        /*
         * 把Date转为Calendar
         */
        Calendar cNow = Calendar.getInstance();
        Calendar cReturnDate = Calendar.getInstance();
        cNow.setTime(now);
        cReturnDate.setTime(returnDate);
        /*
         * 把Calendar的时分秒都设为0
         */
        setTimeToMidnight(cNow);
        setTimeToMidnight(cReturnDate);

        long todayMs = cNow.getTimeInMillis();
        long returnMs = cReturnDate.getTimeInMillis();
        long intervalMs = todayMs - returnMs;// 两个日期相差的毫秒数
        return millisecondsToDays(intervalMs);// 把毫秒转为天数
    }

    public static int millisecondsToDays(long intervalMs)
    {
        return (int) (intervalMs / (1000 * 86400));
    }

    public static void setTimeToMidnight(Calendar calendar)
    {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }

}
