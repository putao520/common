package common.java.number;

public class NumberHelper {
	public static final long number2long(Object in) {
		long r = 0;
		try{
			if( in instanceof Long ){
				r = ((Long) in).longValue();
			}
			else if( in instanceof Integer ){
				r = ((Integer) in).longValue();
			}
			else if( in instanceof String ){
				r = (new Integer(((String) in).trim())).longValue();
			}
			else if( in instanceof Double ){
				r = ((Double) in).longValue();
			}
			else if( in instanceof Float ){
				r = ((Float) in).longValue();
			}
			else if( in instanceof Boolean ){
				r = ((Boolean)in).booleanValue() ? 0 : 1;
			}
			else {
				r = 1;
			}
		}
		catch(Exception e){
			r = 0;
		}
		return r;
	}

	public static final int number2int(Object in) {
		int r = 0;
		try {
			if (in instanceof Long) {
				r = ((Long) in).intValue();
			} else if (in instanceof Integer) {
				r = ((Integer) in).intValue();
			} else if (in instanceof String) {
				r = (new Integer(((String) in).trim())).intValue();
			} else if (in instanceof Double) {
				r = ((Double) in).intValue();
			} else if (in instanceof Float) {
				r = ((Float) in).intValue();
			} else if (in instanceof Boolean) {
				r = ((Boolean) in).booleanValue() ? 0 : 1;
			} else {
				r = 1;
			}
		} catch (Exception e) {
			r = 0;
		}
		return r;
	}
}
