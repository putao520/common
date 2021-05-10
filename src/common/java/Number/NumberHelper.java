package common.java.Number;

import common.java.String.StringHelper;

import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberHelper {
	public static long number2long(Object in) {
		long r = 0;
		if (in == null) {
			return r;
		}
		try {
			if (in instanceof Long) {
				r = (Long) in;
			} else if (in instanceof Integer) {
				r = ((Integer) in).longValue();
			} else if (in instanceof String) {
				r = Long.valueOf(((String) in).trim());
			} else if (in instanceof Double) {
				r = ((Double) in).longValue();
			}
			else if( in instanceof Float ){
				r = ((Float) in).longValue();
			}
			else if (in instanceof Boolean) {
				r = (Boolean) in ? 0 : 1;
			} else if (in instanceof BigInteger) {
				r = ((BigInteger) in).longValue();
			} else if (in instanceof BigDecimal) {
				r = ((BigDecimal) in).longValue();
			} else {
				r = 1;
			}
		} catch (Exception e) {
			r = 0;
		}
		return r;
	}

	public static int number2int(Object in) {
		int r = 0;
		if (in == null) {
			return r;
		}
		try {
			if (in instanceof Long) {
				r = ((Long) in).intValue();
			} else if (in instanceof Integer) {
				r = (Integer) in;
			} else if (in instanceof String) {
				r = Integer.valueOf(((String) in).trim());
			} else if (in instanceof Double) {
				r = ((Double) in).intValue();
			} else if (in instanceof Float) {
				r = ((Float) in).intValue();
			} else if (in instanceof Boolean) {
				r = (Boolean) in ? 0 : 1;
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

	public static boolean isNumeric(Object str) {
		var v = StringHelper.toString(str);
		if (v == null) {
			return false;
		}
		for (int i = v.length(); --i >= 0; ) {
			if (!Character.isDigit(v.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
