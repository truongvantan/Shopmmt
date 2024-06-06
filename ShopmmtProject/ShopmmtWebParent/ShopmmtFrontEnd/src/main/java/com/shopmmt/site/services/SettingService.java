package com.shopmmt.site.services;

import java.util.List;

import com.shopmmt.common.entity.Setting;
import com.shopmmt.site.common.EmailSettingBag;

public interface SettingService {
	List<Setting> getGeneralSettings();

	EmailSettingBag getEmailSettings();
}
