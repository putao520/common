package common.java.String;

import com.google.common.base.Joiner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringHelper {
	private final static Random random;
	private final static Pattern pattern;
	static{
		random = new Random();
		pattern = Pattern.compile("\\s*|\t|\r|\n");
	}
	private String str;
	private StringHelper(String str){
		this.str = str;
	}
	public final static StringHelper build(String str){
		return new StringHelper(str);
	}
	/**字符串按,指定的字符拼接
	 * @param ary
	 * @return
	 */
	public static String join(List<?> ary){
		return join(ary,",");
	}
	/**字符串按ichar指定的字符拼接
	 * @param ary
	 * @param ichar
	 * @return
	 */
	public static String join(List<?> ary,String ichar){
		return Joiner.on(ichar).skipNulls().join(ary);
	}
	public static String join(String[] strary){
		return join(strary,",",0,-1);
	}
	public static String join(String[] strary,String ichar){
		return join(strary,ichar,0,-1);
	}
	public static String join(String[] strary,String ichar,int idx){
		return join(strary,ichar,0,idx);
	}
	/**字符串数组合并成字符串
	 * @param strary	字符串数组
	 * @param ichar		字符串连接字符
	 * @param start		合并开始索引
	 * @param idx		合并执行长度,-1标识直接到结尾
	 * @return
	 */
	public static String join(String[] strary,String ichar,int start,int idx){
		if(idx == -1){
			idx = strary.length;
		}
		if(idx == 0) {
			return "";
		}
		int len = idx + start;
		if( len > strary.length ){
			len = strary.length;
		}
		String rs = "";
		for( int i = start; i < len; i++ ){
			rs = rs + strary[i] + ichar;
		}
		return StringHelper.build(rs).removeTrailingFrom(ichar.length()).toString();
	}

	/**
	 * 获得指定长度字符串
	 * */
	public final static String createRandomCode(int length) {
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; ++i) {
			int number = random.nextInt(62);// [0,62)
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}

	/**根据传入变量类型生成对应的字符串表达式
	 * @param obj
	 * @return
	 */
	public static String typeString(Object obj){
		String rString;
		String r2;
		if( obj != null ){
			r2 = obj.toString();
			if(obj instanceof String){
				rString = (isexp(r2) ? r2 : "\"" + obj + "\"");
			} else if( obj instanceof JSONObject ){
				rString = "'" + r2 + "'";
            } else if (obj instanceof JSONArray) {
				rString = "\"" + r2 + "\"";
			} else {
				rString = r2;
			}
		} else {
			rString = "null";
		}
		return rString;
	}

	/**
	 * 根据传入变量类型生成对应的字符串表达式
	 *
	 * @param obj
	 * @return
	 */
	public static String typeString(Object obj, String replace_char) {
		String rString;
		String r2;
		if (obj != null) {
			r2 = obj.toString();
			if (obj instanceof String) {
				rString = (isexp(r2) ? r2 : replace_char + obj + replace_char);
			} else if (obj instanceof JSONObject) {
				rString = replace_char + r2 + replace_char;
			} else if (obj instanceof JSONArray) {
				rString = replace_char + r2 + replace_char;
			} else {
				rString = r2;
			}
		} else {
			rString = "null";
		}
		return rString;
	}

	/**
	 * 是否是sql表达式字符串
	 *
	 * @param str
	 * @return
	 */
	private static boolean isexp(String str) {
		boolean rb = false;
		String[] vsStrings;
		String[] sl = {"\\+", "-", "\\*", "%", "\\/", "\\\\"};
		try {
			for (int i = 0; i < sl.length; i++) {
				//String[] lString = str.split(sl[i]);
				vsStrings = str.split(sl[i]);
				if( vsStrings.length > 2){
					rb = true;
					for(int n = 0; n < vsStrings.length; n++){
						if( vsStrings[n].equals("") ){
							rb = false;
							break;
						}
					}
					if( rb ){//某一个计算表达式成立，跳出循环
						break;
					}
				}
			}
		}
		catch(Exception e){
			rb = false;
		}

		return rb;
	}
	/**删除字符串第1个字符
	 * @param
	 * @return
	 */
	public StringHelper removeLeadingFrom(){
		str = str.length() > 0 ? str.substring(1) : "";
		return this;
	}
	/**删除字符串前面N个字符
	 * @param
	 * @param i
	 * @return
	 */
	public StringHelper removeLeadingFrom(int i){
		str = ( i > str.length()) ? null : str.length() > 0 ? str.substring(i) : "";
		return this;
	}
	/**删除字符串最后1个字符
	 * @param
	 * @return
	 */
	public StringHelper removeTrailingFrom(){
		str = str.length() > 0 ? str.substring(0,str.length()-1) : "";
		return this;
	}
	/**删除字符串最后N个字符
	 * @param
	 * @param i
	 * @return
	 */
	public StringHelper removeTrailingFrom(int i){
		str = ( i< 1) ? null : str.length() > 0 ? str.substring(0,str.length()-i) : "";
		return this;
	}
	/**删除字符串前后第一个字符
	 * @param
	 * @return
	 */
	public StringHelper removeFrom(){
		if( str.length() > 0 ){
			str = str.substring(1);
		}
		if( str.length() > 0 ){
			str = str.substring(0,str.length()-1);
		}
		return this;
	}
	/**修复字符串
	 *
	 * 删除第一个或者最后一个与ichar相同 的 字符
	 *
	 * @param ichar
	 * @return
	 */
	public StringHelper trimFrom(char ichar){
		leftFix(ichar);
		rightFix(ichar);
		return this;
	}

	public StringHelper left(int len) {
		str = (str.length() > len) ? str.substring(0, len) : str;
		return this;
	}
//CharMatcher.anyOf("jadk").trimFrom(sequence);trimLeading;trimTrailingFrom

	public static final String fix(String str, int len) {
		return fix(str, len, "0");
	}

	private void leftFix(char ichar) {
		int i = 0, l = str.length();
		for (; i < l; i++) {
			if (str.charAt(i) != ichar) {
				break;
			}
		}
		str = str.substring(i);
	}

	/**
	 * 向前补特定字符串方式对其字符串
	 */
	public static final String fix(String str, int len, String replace_char) {
		return String.format(len + "d", str).replace(" ", replace_char);
	}

	private void rightFix(char ichar) {
		int l = str.length();
		if (l > 0) {
			int i = 0;
			for (; i < l; l--) {
				if (str.charAt(l - 1) != ichar) {
					break;
				}
			}
			str = str.substring(0 , l);
		}
	}

	public StringHelper right(int len){
		int l = str.length();
		str = ( str.length() > len ) ? str.substring(l-len, l) : str;
		return this;
	}

	/**删除空白
	 * @return
	 */
	public StringHelper replaceBlank() {
        String dest = "";
        if (str!=null) {
            Matcher m = pattern.matcher(str);
            dest = m.replaceAll("");
        }
        str = dest;
        return this;
    }

    /**uri参数转换成json字符串
     * @param uri_data
     * @return
     */
    @SuppressWarnings("unchecked")
	public final static JSONObject path2rpc(String uri_data){
    	JSONObject rs = null;
    	String[] _iteml = uri_data.split("\\&");
    	String[] _keyValue;
    	JSONObject obj;
    	if( _iteml.length > 0 ){
    		obj = new JSONObject();
    		for(int i=0; i<_iteml.length; i++){
    			_keyValue = _iteml[i].split("=");
    			if( _keyValue.length == 2 ){
    				obj.put(_keyValue[0], _keyValue[1]);
    			}
    		}
    		rs = obj;
    	}
    	return rs != null && !rs.isEmpty() ? rs : null;
    }
    /**uri参数转换成List，保留顺序，抛弃KEY
     * @param gsc_post gsc_post格式post call
     * @return
     */
    @SuppressWarnings("unchecked")
	public final static List<String> path2list(String gsc_post){
    	return Arrays.asList(gsc_post.substring(9).split(":,"));
    }
    /**任何情况下按行切分字符串
     * @return
     */
    public String[] toArrayByLine(){
    	String data = str;
    	String[] formline = data.split("\n");
		if( formline.length < 2){
			formline = data.split("\r");
		}
		for(int i=0; i< formline.length; i++){
			formline[i] = trimFrom('\r').trimFrom('\n').toString();
		}
		return formline;
    }

	@Override
    public String toString(){
    	return str;
	}
    
    /**首字母大写
     * @return
     */
    public StringHelper upperCaseFirst(){
    	char chr = str.length() > 0 ? str.charAt(0) : '\0';
    	StringBuilder strBuilder = new StringBuilder(str);
    	strBuilder.setCharAt(0, String.valueOf(chr).toUpperCase().charAt(0));
    	str = strBuilder.toString();
    	return this;
    }
    
    /**获得最后一个字符
     * @return
     */
    public StringHelper charAtLast(){
    	 charAt( str.length() - 1 );
    	return this;
    }
	public StringHelper charAt(int idx){
		int len = str.length();
		str = (len > idx && idx >= 0) ? String.valueOf( str.charAt( idx ) ) : "";
		return this;
	}
    
    /**获得第一个字符
     * @return
     */
    public StringHelper charAtFrist(){
    	return charAt(0);
    }
    
    /**去掉第一个字符
     * @return
     */
    public StringHelper trimLeading(){
		str = str.length() > 0 ? str.substring(1) : "";
		return this;
    }
    private static String digits(long val, int digits) {  
        long hi = 1L << (digits * 4);  
        return digitsMap.toString(hi | (val & (hi - 1)), digitsMap.MAX_RADIX)
                .substring(1);  
    }  
      
    /** 
     * 以62进制（字母加数字）生成19位UUID，最短的UUID 
     *  
     * @return 
     */  
    public static final String shortUUID(){
    	UUID uuid = UUID.randomUUID();  
        StringBuilder sb = new StringBuilder();  
        sb.append(digits(uuid.getMostSignificantBits() >> 32, 8));  
        sb.append(digits(uuid.getMostSignificantBits() >> 16, 4));  
        sb.append(digits(uuid.getMostSignificantBits(), 4));  
        sb.append(digits(uuid.getLeastSignificantBits() >> 48, 4));  
        sb.append(digits(uuid.getLeastSignificantBits(), 12));  
        return sb.toString();  
    }
    
    /**输出数字唯一ID，20字节
     * @return
     */
    public static final String numUUID(){
    	return StringHelper.numCode(20);
    }
    
    /**指定长度的数字随机字符串
     * @param len
     * @return
     */
    public static final String numCode(int len){
    	String chars = "0123456789";
    	int maxPos = chars.length();
    	String pwd = "";
    	for (int i = 0; i < len; i++) {
    		pwd += chars.charAt( new Double( Math.ceil(Math.floor(Math.random() * maxPos)) ).intValue() );
    	}
    	return pwd;
    }
    
    /**输出常用验证码，6位
     * @return
     */
    public static final String checkCode(){
    	return numCode(6);
    }
    
    /**输出指定长度验证码
     * @param len
     * @return
     */
    public static final String checkCode(int len){
    	return numCode(len);
    }
    
	/**
	 * @param text	模板文本
	 * @param data	K-V对应替换的数据组
	 * @return
	 */
	public static final String createCodeText(String text, JSONObject data) {
		String rs = text;
		for (Object obj : data.keySet()) {
			rs = rs.replaceAll("@" + obj.toString(), data.get(obj).toString());
		}
		return rs;
	}

	public static final boolean invaild(String str) {
		return str == null || str.trim().length() == 0 || str.trim().equals("null") || str.trim().equals("undefined");
	}

	public static final String toString(Object obj) {
		String out;
		try {
			out = obj.toString();
		} catch (Exception e) {
			try {
				out = String.valueOf(obj);
			} catch (Exception e1) {
				out = null;
			}
		}
		return out;
	}

	/**
	 * 从左  开始 替换ichar对应字符
	 *
	 * @param ichar
	 * @return
	 */
	public StringHelper trimLeadingFrom(char ichar) {
		leftFix(ichar);
		return this;
	}

	/**
	 * 从右  开始 替换ichar对应字符
	 *
	 * @param ichar
	 * @return
	 */
	public StringHelper trimTrailingFrom(char ichar) {
		rightFix(ichar);
		return this;
	}

	public StringHelper toUTF8(String charSetName) {
		String rString = null;
		try {
			rString = new String(str.getBytes(charSetName), StandardCharsets.UTF_8);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			rString = "";
		}
		str = rString;
		return this;
	}

	/**
	 * 不够位数的在前面补0，保留num的长度位数字
	 *
	 * @param num
	 * @return
	 */
	public String autoGenericCode(int num) {
		String result = "";
		// 保留num的位数
		// 0 代表前面补充0
		// num 代表长度为4
		// d 代表参数为正数型
		result = String.format("%0" + num + "d", Integer.parseInt(str) + 1);

		return result;
	}
}
