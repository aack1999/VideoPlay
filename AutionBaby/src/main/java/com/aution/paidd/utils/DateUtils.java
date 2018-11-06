package com.aution.paidd.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by aacc on 2015/7/21.
 */
public class DateUtils {
    static final String formatPattern = "yyyy-MM-dd";

    static final String formatPattern_Short = "yyyyMMdd";

    /**
     * 获取当前日期
     * @return
     */
    public static String getCurrentDate(){
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        return format.format(new Date());
    }



    public static String dateToStr(Date date,String formatPattern){
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        return format.format(date);
    }

    /**
     * 获取制定毫秒数之前的日期
     * @param timeDiff
     * @return
     */
    public static String getDesignatedDate(long timeDiff){
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        long nowTime = System.currentTimeMillis();
        long designTime = nowTime - timeDiff;
        return format.format(designTime);
    }

    /**
     *
     * 获取前几天的日期
     */
    public static String getPrefixDate(String count){
        Calendar cal = Calendar.getInstance();
        int day = 0-Integer.parseInt(count);
        cal.add(Calendar.DATE,day);   // int amount   代表天数
        Date datNew = cal.getTime();
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        return format.format(datNew);
    }
    /**
     * 日期转换成字符串
     * @param date
     * @return
     */
    public static String dateToString(Date date){
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        return format.format(date);
    }
    /**
     * 字符串转换日期
     * @param str
     * @return
     */
    public static Date stringToDate(String str){
        //str =  " 2008-07-10 19:20:00 " 格式
        SimpleDateFormat format = new SimpleDateFormat(formatPattern);
        if(!str.equals("")&&str!=null){
            try {
                return format.parse(str);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 字符串转换日期
     * @param str
     * @return
     */
    public static Date stringToDate(String str,String formatStr){
        //str =  " 2008-07-10 19:20:00 " 格式
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        if(!TextUtils.isEmpty(str)){
            try {
                return format.parse(str);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 时间字符串一种格式转另一种格式
     * @param str
     * @param formatStr
     * @param formatStr1
     * @return
     */
    public static String stringTostring(String str,String formatStr,String formatStr1){
        Date d=stringToDate(str,formatStr);
        return dateToStr(d,formatStr1);
    }

    //java中怎样计算两个时间如：“21:57”和“08:20”相差的分钟数、小时数 java计算两个时间差小时 分钟 秒 .
    public void timeSubtract(){
        SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date begin = null;
        Date end = null;
        try {
            begin = dfs.parse("2004-01-02 11:30:24");
            end = dfs.parse("2004-03-26 13:31:40");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒

        long day1 = between / (24 * 3600);
        long hour1 = between % (24 * 3600) / 3600;
        long minute1 = between % 3600 / 60;
        long second1 = between % 60;
        System.out.println("" + day1 + "天" + hour1 + "小时" + minute1 + "分"
                + second1 + "秒");
    }

    public static int getYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getMonth(){
        return Calendar.getInstance().get(Calendar.MONTH)+1;
    }

    public static int getDay(){
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getHour(){
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute(){
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    public static String getYestoMonth(){
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
       return dateToStr(calendar.getTime(), "M");
    }

    public static long getTimeStamp(){
        return System.currentTimeMillis();
    }

    /** * 获取指定日期是星期几
     * 参数为null时表示获取当前日期是星期几
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        String[] weekOfDays = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar calendar = Calendar.getInstance();
        if(date != null){
            calendar.setTime(date);
        }
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
        return weekOfDays[w];
    }

    public static String formatZero(int num){
        return num>9?(num+""):("0"+num);
    }

    public static String getBabyAge(String date1,String date2) {
        String d2[] = date2.split("-");
        int iyear = Integer.parseInt(d2[0]);
        int imonth = Integer.parseInt(d2[1]) - 1;
        int iday = Integer.parseInt(d2[2]);
        Calendar birthday = new GregorianCalendar(iyear, imonth, iday);//2010年10月12日，month从0开始

        String d[] = date1.split("-");
        int nyear = Integer.parseInt(d[0]);
        int nmonth = Integer.parseInt(d[1]) - 1;
        int nday = Integer.parseInt(d[2]);
        Calendar now = new GregorianCalendar(nyear, nmonth, nday);

        int day = now.get(Calendar.DAY_OF_MONTH) - birthday.get(Calendar.DAY_OF_MONTH);

        int month = now.get(Calendar.MONTH) - birthday.get(Calendar.MONTH);

        System.err.println(now.get(Calendar.MONTH) + " " + birthday.get(Calendar.MONTH));

        int year = now.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);

        System.err.println(day + " " + month + " " + year);

        //按照减法原理，先day相减，不够向month借；然后month相减，不够向year借；最后year相减。

        if (day < 0) {
            month -= 1;
            now.add(Calendar.MONTH, -1);//得到上一个月，用来得到上个月的天数。
            day = day + now.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        if (month < 0) {
            month = (month + 12) % 12;
            year--;

        }

        String kk = "宝宝";

        if (year==0&&month==0&&day==0){
            kk="宝宝出生";
            return kk;
        }
        if (year>0){
            kk=kk+year+"周岁";
        }
        if (month>0){
            kk=kk+month+"月";
        }
        if (day>0){
            kk=kk+day+"天";
        }
        return kk;
    }

    public static final int getHour(String str,String formatPattern){
        return Integer.parseInt(stringTostring(str,formatPattern,"H"));
    }

    public static final int getMin(String str,String formatPattern){
        return Integer.parseInt(stringTostring(str, formatPattern, "m"));
    }

    public static final int getSec(String str,String formatPattern){
        return Integer.parseInt(stringTostring(str,formatPattern,"s"));
    }

    public static final int getYear(String str,String formatPattern){
        return Integer.parseInt(stringTostring(str,formatPattern,"yyyy"));
    }

    public static final int getMonth(String str,String formatPattern){
        return Integer.parseInt(stringTostring(str,formatPattern,"MM"));
    }

    public static final int getDay(String str,String formatPattern){
        return Integer.parseInt(stringTostring(str,formatPattern,"dd"));
    }

    public static long getTimeCurrent(String currentTime){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date formatTime = null;
        try {
            formatTime = formatter.parse(currentTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatTime.getTime();

    }

}
