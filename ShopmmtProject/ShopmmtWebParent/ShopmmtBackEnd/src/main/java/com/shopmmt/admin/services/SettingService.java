package com.shopmmt.admin.services;

import java.util.List;

import com.shopmmt.admin.common.GeneralSettingBag;
import com.shopmmt.common.entity.Setting;

import jakarta.servlet.http.HttpServletRequest;

public interface SettingService {
	List<Setting> listAllSettings();
	
	GeneralSettingBag getGeneralSettings();
	
	void saveAll(Iterable<Setting> settings);
	
	boolean checkValidSettingValue(HttpServletRequest request, List<Setting> listSettings);
	
	List<Setting> getMailServerSettings();
	
	List<Setting> getMailTemplateSettings();

	List<Setting> getCurrencySettings();
	
	List<Setting> getPaymentSettings();

}
