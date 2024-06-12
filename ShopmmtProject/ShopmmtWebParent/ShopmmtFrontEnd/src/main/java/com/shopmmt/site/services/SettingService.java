package com.shopmmt.site.services;

import java.util.List;

import com.shopmmt.common.entity.Setting;
import com.shopmmt.site.common.CurrencySettingBag;
import com.shopmmt.site.common.EmailSettingBag;
import com.shopmmt.site.common.PaymentSettingBag;

public interface SettingService {
	List<Setting> getGeneralSettings();

	EmailSettingBag getEmailSettings();
	
	CurrencySettingBag getCurrencySettings();
	
	PaymentSettingBag getPaymentSettings();
	
	String getCurrencyCode();
}
