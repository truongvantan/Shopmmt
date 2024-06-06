package com.shopmmt.site.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopmmt.common.entity.Setting;
import com.shopmmt.common.enums.SettingCategory;
import com.shopmmt.site.common.EmailSettingBag;
import com.shopmmt.site.repositories.SettingRepository;
import com.shopmmt.site.services.SettingService;

@Service("settingService")
public class SettingServiceImpl implements SettingService {

	@Autowired
	private SettingRepository settingRepository;

	@Override
	public List<Setting> getGeneralSettings() {
		return settingRepository.findByTwoCategories(SettingCategory.GENERAL, SettingCategory.CURRENCY);
	}

	@Override
	public EmailSettingBag getEmailSettings() {
		List<Setting> settings = settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
		settings.addAll(settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES));

		return new EmailSettingBag(settings);
	}

}
