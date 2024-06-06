package com.shopmmt.admin.common;

import java.util.List;

import com.shopmmt.common.entity.Setting;
import com.shopmmt.common.entity.SettingBag;

public class GeneralSettingBag extends SettingBag {
	public GeneralSettingBag() {
		super();
	}

	public GeneralSettingBag(List<Setting> listSettings) {
		super(listSettings);
	}

	public void updateCurrencySymbol(String value) {
		super.update("CURRENCY_SYMBOL", value);
	}

	public void updateSiteLogo(String value) {
		super.update("SITE_LOGO", value);
	}

}
