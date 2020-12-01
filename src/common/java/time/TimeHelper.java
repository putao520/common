package common.java.time;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

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
    public static long getNowTimestampByZero(ZoneId timeZone){
        return Instant.now().atZone(timeZone).toInstant().toEpochMilli();
    }
}
