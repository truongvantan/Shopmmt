package com.shopmmt.common.enums;

public enum PaymentMethod {
	COD("THANH TOÁN KHI NHẬN HÀNG"), 
	CREDIT_CARD("THẺ TÍN DỤNG");
	
	public final String label;

    private PaymentMethod(String label) {
        this.label = label;
    }
}
