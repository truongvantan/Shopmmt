package com.shopmmt.common.validate;

public class ValidateCommon {
	public static boolean isValidStringNumber(String s) {
		if (s == null || "".equals(s)) {
			return false;
		}
		return s.matches("^[0-9]+(\\.[0-9]+)?$");
	}

	public static boolean isValidStringIntegerNumber(String s) {
		if (s == null || "".equals(s)) {
			return false;
		}
		return s.matches("^[0-9]+$");
	}
}
