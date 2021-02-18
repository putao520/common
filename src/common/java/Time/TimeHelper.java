package common.java.Time;

import common.java.String.StringHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimeHelper {
    private final ZoneId timeZone;
    public static final TimeHelper build(ZoneId timeZone){
        return new TimeHelper(timeZone);
    }
    public static final TimeHelper build(){
        return new TimeHelper( ZoneId.systemDefault());
    }
    private TimeHelper(ZoneId timeZone){
        this.timeZone = timeZone;
    }

    /**当前毫秒数
     * @return
     */
    public long nowMillis(){
        return Instant.now().atZone(this.timeZone).toInstant().toEpochMilli();
    }
    /**当前秒数
     * @return
     */
    public long nowSecond(){
        return Instant.now().atZone(this.timeZone).toInstant().getEpochSecond();
    }

	/**日期字符串转换成Unix时间戳
	 * @param s
	 * @return 毫秒
	 * @throws ParseException
	 */
    public long dateTimeToTimestamp(String s) {
        return dateTimeToTimestamp(s,"yyyy-MM-dd HH:mm:ss");
    }
    public long dateTimeToTimestamp(String s, String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.parse(s.split("\\.")[0], df).atZone(this.timeZone).toInstant().toEpochMilli();
    }

    /**
     * 日期字符串转换成Unix时间戳
     *
     * @param s
     * @return
     * @throws ParseException
     */
    public long dateToTimestamp(String s) {
        return dateToTimestamp(s,"yyyy-MM-dd");
    }
    public long dateToTimestamp(String s,String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return LocalDate.parse(s.split("\\.")[0], df).atStartOfDay(this.timeZone).toInstant().toEpochMilli();
    }

	/**Unix时间戳转换成日期时间字符串
	 * @param ms 时间戳
     * @return
     */
    public String timestampToDatetime(long ms){
        return timestampToDatetime(ms,"yyyy-MM-dd HH:mm:ss");
    }
    /**Unix时间戳转换成日期时间字符串
     * @param ms 时间戳
     * @param format yyyy-mm-dd hh:mm:ss
     * @return
     */
    public String timestampToDatetime(long ms, String format){
        return DateTimeFormatter.ofPattern(format).format( LocalDateTime.ofInstant( Instant.ofEpochMilli(ms), this.timeZone ) );
    }

    /**
     * 获得当前日期字符串
     */
    public String nowDate() {
        return LocalDate.now(this.timeZone).toString();
    }
    public String nowDate(String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return LocalDate.now(this.timeZone).format(df);
    }

    /**
     * 获得当前日期时间字符串
     */
    public String nowDatetime() {
        return nowDatetime("yyyy-MM-dd HH:mm:ss");
    }
    public String nowDatetime(String format) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return LocalDateTime.now(this.timeZone).format(df);
    }
	
	/** 
     * Unix时间戳转日期
     * @param ms (毫秒)
     * @return 
     */  
    public String timestampToDate(long ms){
        return timestampToDate(ms,"yyyy-MM-dd");
    }
    public String timestampToDate(long ms, String format){
        return DateTimeFormatter.ofPattern(format).format( LocalDateTime.ofInstant( Instant.ofEpochMilli(ms), this.timeZone ) );
    }

    public int nowDay(){
        return LocalDate.now(this.timeZone).getDayOfMonth();
    }

    public int nowYear() {
        return LocalDate.now(this.timeZone).getYear();
    }

    public int nowMonth() {
        return LocalDate.now(this.timeZone).getMonthValue();
    }

    public int nowWeek() {
        return LocalDate.now(this.timeZone).getDayOfWeek().getValue();
    }

    public int nowHour() {
        return LocalDateTime.now(this.timeZone).getHour();
    }

    /**
     * 获得零时区当前时间戳
     */
    public static long getNowTimestampByZero() {
        return Instant.now().toEpochMilli();
    }

    /**
     * 获得当前时区时间戳
     */
    public static long getNowTimestampByZero(ZoneId timeZone) {
        return Instant.now().atZone(timeZone).toInstant().toEpochMilli();
    }

    //根据年月 获取月份天数
    public static final int getMonthDayNum(String dyear, String dmouth) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM");
        Calendar rightNow = Calendar.getInstance();
        try {
            rightNow.setTime(simpleDate.parse(dyear + "/" + dmouth));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    //根据开始年月，到结束年月范围，生成KEY ARRAY;
    public static final List<String> buildMonthArray(long startYear, long startMonth, long endYear, long endMonth) {
        List<String> monthArray = new ArrayList<>();
        long year_num = endYear - startYear;
        if (year_num >= 0) {
            // 同一年月份处理
            if (year_num == 0) {
                for (long i = startMonth; i <= endMonth; i++) {
                    monthArray.add(startYear + "/" + i);
                }
            }
            // 非同一年
            else {
                // 开始年
                for (long i = startMonth; i <= 12; i++) {
                    monthArray.add(startYear + "/" + i);
                }
                // 中间全年
                for (long y = (startYear + 1); y < endYear; y++) {
                    for (long m = 1; m <= 12; m++) {
                        monthArray.add(y + "/" + m);
                    }
                }
                // 结束年
                for (long i = 1; i <= endMonth; i++) {
                    monthArray.add(endYear + "/" + i);
                }
            }
        }
        return monthArray;
    }

    // 根据给定的年月日生成开始结束时间戳
    public UnixTimeRanger GetUnixTimeRanger(String year, String month, String day) {
        UnixTimeRanger r = new UnixTimeRanger();
        r.start = dateTimeToTimestamp(
                StringHelper.build(year).autoGenericCode(4) + "-" + StringHelper.build(month).autoGenericCode(2) + "-" + StringHelper.build(day).autoGenericCode(2) + " 00:00:00"
        );
        r.end = dateTimeToTimestamp(
                StringHelper.build(year).autoGenericCode(4) + "-" + StringHelper.build(month).autoGenericCode(2) + "-" + StringHelper.build(day).autoGenericCode(2) + " 23:59:59"
        );
        return r;
    }
}
