package com.shopmmt.admin.enums;

public enum ReportType {
	DAY("NGÀY"),
	MONTH("THÁNG"),
	CATEGORY("DANH MỤC"),
	PRODUCT("SẢN PHẨM");

	public final String label;

	private ReportType(String label) {
		this.label = label;
	}
}
