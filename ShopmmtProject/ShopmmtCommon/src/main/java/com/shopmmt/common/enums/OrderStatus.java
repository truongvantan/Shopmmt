package com.shopmmt.common.enums;

public enum OrderStatus {
	NEW("MỚI"),
	CANCELLED("HỦY"),
	PROCESSING("ĐANG XỬ LÝ"),
	PACKAGED("ĐÃ ĐÓNG GÓI"),
	PICKED("ĐÃ TIẾP NHẬN ĐƠN HÀNG"),
	SHIPPING("ĐANG GIAO HÀNG"),
	DELIVERED("ĐÃ GIAO HÀNG"),
	RETURNED("ĐÃ TRẢ HÀNG"),
	PAID("ĐÃ THANH TOÁN"),
	REFUNDED("ĐÃ HOÀN TIỀN"),
	RETURN_REQUESTED("YÊU CẦU HOÀN TRẢ ĐƠN HÀNG");

	public final String label;

	private OrderStatus(String label) {
		this.label = label;
	}
}
