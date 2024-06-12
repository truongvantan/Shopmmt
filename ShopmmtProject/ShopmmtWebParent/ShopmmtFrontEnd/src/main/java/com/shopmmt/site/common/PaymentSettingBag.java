package com.shopmmt.site.common;

import java.util.List;

import com.shopmmt.common.entity.Setting;
import com.shopmmt.common.entity.SettingBag;

public class PaymentSettingBag extends SettingBag {

	public PaymentSettingBag() {
		super();
	}
	
	public PaymentSettingBag(List<Setting> listSettings) {
		super(listSettings);
	}

	public String getURL() {
		return super.getValue("PAYPAL_API_BASE_URL");
	}
	
	public String getClientID() {
		return super.getValue("PAYPAL_API_CLIENT_ID");
	}
	
	public String getClientSecret() {
		return super.getValue("PAYPAL_API_CLIENT_SECRET");
	}
	
}
