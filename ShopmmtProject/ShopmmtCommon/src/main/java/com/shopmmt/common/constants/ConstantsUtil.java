package com.shopmmt.common.constants;

public class ConstantsUtil {
	public static final String REGEX_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	public static final String REGEX_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{6,60}$";
	public static final int USER_PAGE_SIZE = 5;
	public static final int CATEGORY_PAGE_SIZE = 2;
	public static final int BRAND_PAGE_SIZE = 5;
}
