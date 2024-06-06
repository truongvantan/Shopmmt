package com.shopmmt.admin.services;

import java.util.List;

import com.shopmmt.admin.common.GeneralSettingBag;
import com.shopmmt.common.entity.Setting;

public interface SettingService {
	List<Setting> listAllSettings();
	
	GeneralSettingBag getGeneralSettings();
	
	void saveAll(Iterable<Setting> settings);
	
	List<Setting> getMailServerSettings();
	
	List<Setting> getMailTemplateSettings();
}
