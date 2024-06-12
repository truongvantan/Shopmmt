package com.shopmmt.site.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopmmt.common.entity.Currency;
import com.shopmmt.common.entity.Setting;
import com.shopmmt.common.enums.SettingCategory;
import com.shopmmt.site.common.CurrencySettingBag;
import com.shopmmt.site.common.EmailSettingBag;
import com.shopmmt.site.common.PaymentSettingBag;
import com.shopmmt.site.repositories.CurrencyRepository;
import com.shopmmt.site.repositories.SettingRepository;
import com.shopmmt.site.services.SettingService;

@Service("settingService")
public class SettingServiceImpl implements SettingService {

	@Autowired
	private SettingRepository settingRepository;

	@Autowired
	private CurrencyRepository currencyRepository;

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

	@Override
	public CurrencySettingBag getCurrencySettings() {
		List<Setting> settings = settingRepository.findByCategory(SettingCategory.CURRENCY);
		return new CurrencySettingBag(settings);
	}

	@Override
	public PaymentSettingBag getPaymentSettings() {
		List<Setting> settings = settingRepository.findByCategory(SettingCategory.PAYMENT);
		return new PaymentSettingBag(settings);
	}

	@Override
	public String getCurrencyCode() {
		Setting setting = settingRepository.findByKey("CURRENCY_ID");
		Integer currencyId = Integer.parseInt(setting.getValue());
		Currency currency = currencyRepository.findById(currencyId).get();

		return currency.getCode();
	}

}
