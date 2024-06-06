package com.shopmmt.admin.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopmmt.admin.common.GeneralSettingBag;
import com.shopmmt.admin.repositories.SettingRepository;
import com.shopmmt.admin.services.SettingService;
import com.shopmmt.common.entity.Setting;
import com.shopmmt.common.enums.SettingCategory;

@Service("settingService")
public class SettingServiceImpl implements SettingService {

	@Autowired
	private SettingRepository settingRepository;

	@Override
	public List<Setting> listAllSettings() {
		return settingRepository.findAll();
	}

	@Override
	public GeneralSettingBag getGeneralSettings() {
		List<Setting> settings = new ArrayList<Setting>();

		List<Setting> generalSettings = settingRepository.findByCategory(SettingCategory.GENERAL);
		List<Setting> currencySettings = settingRepository.findByCategory(SettingCategory.CURRENCY);

		settings.addAll(generalSettings);
		settings.addAll(currencySettings);

		return new GeneralSettingBag(settings);
	}

	@Override
	public void saveAll(Iterable<Setting> settings) {
		settingRepository.saveAll(settings);
	}

	@Override
	public List<Setting> getMailServerSettings() {
		return settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
	}

	@Override
	public List<Setting> getMailTemplateSettings() {
		return settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
	}

}
