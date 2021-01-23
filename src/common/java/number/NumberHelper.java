package common.java.number;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberHelper {
	public static final long number2long(Object in) {
		long r = 0;
		if (in == null) {
			return r;
		}
		try {
			if (in instanceof Long) {
				r = ((Long) in).longValue();
			} else if (in instanceof Integer) {
				r = ((Integer) in).longValue();
			} else if (in instanceof String) {
				r = (new Integer(((String) in).trim())).longValue();
			} else if (in instanceof Double) {
				r = ((Double) in).longValue();
			}
			else if( in instanceof Float ){
				r = ((Float) in).longValue();
			}
			else if (in instanceof Boolean) {
				r = ((Boolean) in).booleanValue() ? 0 : 1;
			} else if (in instanceof BigInteger) {
				r = ((BigInteger) in).longValue();
			} else if (in instanceof BigDecimal) {
				r = ((BigDecimal) in).longValue();
			} else {
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
		if (in == null) {
			return r;
		}
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
			} else if (in instanceof BigInteger) {
				r = ((BigInteger) in).intValue();
			} else if (in instanceof BigDecimal) {
				r = ((BigDecimal) in).intValue();
			} else {
				r = 1;
			}
		} catch (Exception e) {
			r = 0;
		}
		return r;
	}
}
