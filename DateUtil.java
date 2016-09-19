package cn.sinobest.ypgj.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ���ڹ�����
 * 
 * @author hexiaowei
 * 
 */
public class DateUtil {

	/**
	 * ��Date����ת��Ϊ�ַ��� ��:"2002-07-01 11:40:02"
	 * 
	 * @param date
	 *            ��������
	 * @return �����ַ���
	 */
	public static String format(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * ��Date����ת��Ϊ�ַ���
	 * 
	 * @param date
	 *            ��������
	 * @param pattern
	 *            �ַ�����ʽ
	 * @return �����ַ���
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
	 * ���ַ���ת��ΪDate����
	 * 
	 * @param date
	 *            �ַ�������
	 * @return ��������
	 */
	public static Date format(String date) {
		return format(date, null);
	}

	/**
	 * ���ַ���ת��ΪDate����
	 * 
	 * @param date
	 *            �ַ�������
	 * @param pattern
	 *            ��ʽ
	 * @return ��������
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
     * ���ڸ�ʽ�ַ���תUTCʱ���ʽ
     * UTCʱ���ʽת�����������ڸ�ʽ���ַ��������ظ��ַ���
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
            //1��ȡ��ʱ��ƫ������
            int zoneOffset = cal.get(Calendar.ZONE_OFFSET);

            //2��ȡ������ʱ�
            int dstOffset = cal.get(Calendar.DST_OFFSET);

            //3���Ӵ����ʱ����۳���Щ������������ȡ��UTCʱ�䣺
            cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));

            utcString = sdf.format(new Date(cal.getTimeInMillis()));
            System.out.println("UTCת�������ڸ�ʽ:"+utcString);
        } catch (ParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return utcString;
    }

    /**
     * �õ�����date��������
     *
     * @param now
     * @param returnDate
     * @return
     */
    public static int daysBetween(Date now, Date returnDate)
    {
        /*
         * ��DateתΪCalendar
         */
        Calendar cNow = Calendar.getInstance();
        Calendar cReturnDate = Calendar.getInstance();
        cNow.setTime(now);
        cReturnDate.setTime(returnDate);
        /*
         * ��Calendar��ʱ���붼��Ϊ0
         */
        setTimeToMidnight(cNow);
        setTimeToMidnight(cReturnDate);

        long todayMs = cNow.getTimeInMillis();
        long returnMs = cReturnDate.getTimeInMillis();
        long intervalMs = todayMs - returnMs;// �����������ĺ�����
        return millisecondsToDays(intervalMs);// �Ѻ���תΪ����
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
