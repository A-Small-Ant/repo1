package com.alphajuns.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.text.ParseException;


import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;

/**
 * @ClassName DateUtils
 * @Description 日期格式化工具
 * @Author AlphaJunS
 * @Date 2020/3/17 20:23
 * @Version 1.0
 */
public class DateUtils {
    private static Logger logger = Logger.getLogger(DateUtils.class);

    /**
     * <p>Field DATEPATTERNYYYYVMMVDD: change date to type "yyyy-MM-dd" </p>
     */
    public static final String DATEPATTERNYYYYVMMVDD = "yyyy-MM-dd";
    /**
     * <p>Field DATEPATTERNYYYYVMMVDD: change date to type "yyyy/MM/dd HH:mm:ss" </p>
     */
    public static final String DATEWITH_H_M_S = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * Description: 将时间戳转化为日期
     * @param t t
     * @param pattern pattern
     * @return String
     */
    public static String getDateFromTimestampByPattern(Timestamp t, String pattern) {
        if (t != null) {
            return DateFormatUtils.format(new Date(t.getTime()), pattern);
        } else {
            return null;
        }
    }

    /**
     * <p>Description: Date转日期字符串
     * @param t t
     * @param pattern pattern
     * @return String
     */
    public static String getStrFromDateByPattern(Date t, String pattern) {
        if (t != null) {
            return DateFormatUtils.format(t, pattern);
        } else {
            return null;
        }
    }

    /**
     * <p>Description:  将当前日期加减n天数。 如传入整型-5 意为将当前日期减去5天的日期 如传入整型5 意为将当前日期加上5天后的日期 返回字串 例(19990203)</p>
     * @param format format 格式化的类型    例如：2015-05-06 ，加7天  =2015-05-13
     * @param days days 天数
     * @return String
     */
    public static String dateAdd(int days, String format) {
        // 日期处理模块 (将日期加上某些天或减去天数)返回字符串
        Calendar canlendar;
        canlendar = Calendar.getInstance();
        canlendar.add(Calendar.DAY_OF_YEAR, days); // // 日期减 如果不够减会将月变动

        SimpleDateFormat sdfd ;
        sdfd = new SimpleDateFormat(format);

        String formatDate;
        formatDate = sdfd.format(canlendar.getTime());
        return formatDate;
    }

    /**
     * 得到最近几个月的时间区间
     * @param months
     * @param format
     * @return
     */
    public static String[] getRecentMonthDateScope(int months, String format){
        SimpleDateFormat sdfd ;
        sdfd = new SimpleDateFormat(format);
        // 日期处理模块 (将日期加上月数)返回字符串
        Calendar canlendar;
        canlendar = Calendar.getInstance();
        Date currentDate = canlendar.getTime();
        String endDate;
        endDate = sdfd.format(currentDate);
        canlendar.add(Calendar.MONTH, -3); // // 日期减 如果不够减会将月变动
        String startDate;
        startDate = sdfd.format(canlendar.getTime());
        return new String[]{startDate,endDate};
    }

    /**
     * 计算工作日
     * 具体节日包含哪些,可以在HolidayMap中修改
     * @param src 日期(源)
     * @param adddays 要加的天数
     * @exception throws [违例类型] [违例说明]
     */
    public static Date addDateByWorkDay(Date date,int adddays)
    {
        if(date==null){
            return null;
        }
        Calendar src = Calendar.getInstance();
        src.setTime(date);
        boolean holidayFlag = false;
        for (int i = 0; i < adddays; i++)
        {
            //把源日期加一天
            src.add(Calendar.DAY_OF_MONTH, 1);
            holidayFlag =checkHoliday(src);
            if(holidayFlag)
            {
                i--;
            }
            System.out.println(src.getTime());
        }
        System.out.println("Final Result:"+src.getTime());
        return src.getTime();
    }

    /**
     * 校验指定的日期是否在节日列表中
     * 具体节日包含哪些,可以在HolidayMap中修改
     * @param src 要校验的日期(源)
     */
    public static boolean checkHoliday(Calendar src)
    {
        boolean result = false;
        // 先检查是否是周六周日(有些国家是周五周六)
        if (src.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || src.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
        {
            return true;
        }
        List<Calendar> holidayList = new ArrayList<Calendar>();
        for (Calendar c : holidayList)
        {
            if (src.get(Calendar.MONTH) == c.get(Calendar.MONTH)
                    && src.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH))
            {
                result = true;
            }
        }
        return result;
    }



    /**
     * <p>Description:  按格式将字符串转为Timestamp</p>
     * @param t t
     * @param pattern pattern
     * @return Timestamp
     */
    public static Timestamp getTimestampByPattern(String t, String pattern) {
        if(pattern==null || pattern.equals("")){
            pattern = DATEWITH_H_M_S;
        }
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(t);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            return new Timestamp(date.getTime());
        }
        return null;
    }

    /**
     * <p>Description:  按格式将字符串转为Timestamp</p>
     * @param t t
     * @param pattern pattern
     * @return Timestamp
     */
    public static Date getDateByPattern(String t, String pattern) {
        if(pattern==null || pattern.equals("")){
            pattern = DATEPATTERNYYYYVMMVDD;
        }
        if(t==null || t.equals("") || t.equals("null")){
            return null;
        }
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(t);
        } catch (ParseException e) {
            logger.warn(e.toString());
            try {
                date = DateUtils.getDateByPattern(t, DATEPATTERNYYYYVMMVDD,"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm");
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return date;
    }

    /**
     * 从字符串获取日期，按时间来获取，从有秒的pattern开始，到只有天的pattern
     * @param t
     * @return
     * @throws Exception
     */
    public static Date getDateByDefaultPattern(String t) throws Exception{
        return DateUtils.getDateByPattern(t,"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm","yyyy-MM-dd");
    }

    /**
     * 按格式将字符串转为Timestamp
     * @param t
     * @param patterns
     * @return
     * @throws Exception
     */
    public static Timestamp getTimestampByPattern(String t, String ...patterns) throws Exception{
        Date  date = getDateByPattern(t, patterns);
        if(date!=null){
            Timestamp ts = new Timestamp(date.getTime());
            return ts;
        }else{
            return null;
        }
    }

    /**
     * <p>Description:  按格式将字符串转为Date</p>
     * @param t t
     * @param pattern pattern
     * @return Timestamp
     */
    public static Date getDateByPattern(String t, String ...patterns) throws Exception{
        if(patterns==null || patterns.equals("")){
            patterns = new String[1];
            patterns[0] = DATEPATTERNYYYYVMMVDD;
        }
        if(t==null || t.equals("") || t.equals("null")){
            return null;
        }
        SimpleDateFormat sdf;
        Date date = null;
        for(String simplePattern : patterns){
            try{
                sdf = new SimpleDateFormat(simplePattern);
                date = sdf.parse(t);
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int s = c.get(Calendar.YEAR);
                if(s==1970){
                    Calendar curr = Calendar.getInstance();
                    curr.setTime(new Date());
                    c.set(Calendar.YEAR, curr.get(Calendar.YEAR));
                    date = c.getTime();
                }
            }catch(ParseException e){
                continue;
            }
            break;
        }
        return date;
    }


    /**
     * 获取当前日期时间
     * @param format 时间格式yyyy-MM-dd HH:mm:ss
     * @return String String
     * */
    public static String getDayDate(String format) {
        SimpleDateFormat df ;
        df = new SimpleDateFormat(format);
        return df.format(new Date());

    }

    /**
     * <p>Description:  按格式将字符串转为Timestamp</p>
     * @param t t
     * @param pattern pattern
     * @return Timestamp
     */
    public static Timestamp getTimestampBy(String t, String pattern) {
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(t);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date != null) {
            return new Timestamp(date.getTime());
        }
        return null;
    }

    public static void main(String[] args){
        Date after = DateUtils.addDateByWorkDay(new Date(), 5);
        System.out.println(after);
    }
}
