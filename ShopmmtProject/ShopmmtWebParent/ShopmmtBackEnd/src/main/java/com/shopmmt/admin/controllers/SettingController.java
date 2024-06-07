package com.shopmmt.admin.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopmmt.admin.common.GeneralSettingBag;
import com.shopmmt.admin.services.CurrencyService;
import com.shopmmt.admin.services.SettingService;
import com.shopmmt.admin.utils.FileUploadUtil;
import com.shopmmt.common.entity.Currency;
import com.shopmmt.common.entity.Setting;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class SettingController {

	@Autowired
	private SettingService settingService;

	@Autowired
	private CurrencyService currencyService;

	@GetMapping("/settings")
	public String listAll(Model model) {
		List<Setting> listSettings = settingService.listAllSettings();
		List<Currency> listCurrencies = currencyService.findAllByOrderByNameAsc();

		model.addAttribute("listCurrencies", listCurrencies);

		for (Setting setting : listSettings) {
			model.addAttribute(setting.getKey(), setting.getValue());
		}

		return "settings/settings";
	}

	@PostMapping("/settings/save_general")
	public String saveGeneralSettings(@RequestParam(name = "fileImage", required = false) MultipartFile multipartFile,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {

		if (!FileUploadUtil.isValidFileSize(multipartFile)) {
			redirectAttributes.addFlashAttribute("error", "Vui lòng chọn tệp có kích thước không vượt quá 1MB");

			return "redirect:/settings";
		} else {
			GeneralSettingBag settingBag = settingService.getGeneralSettings();
			
			if (!settingService.checkValidSettingValue(request, settingBag.list())) {
				redirectAttributes.addFlashAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
				
				return "redirect:/settings";
			} else {
				saveSiteLogo(multipartFile, settingBag);
				saveCurrencySymbol(request, settingBag);
				
				updateSettingValuesFromForm(request, settingBag.list());
				
				redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin thành công.");
				
				return "redirect:/settings";
			}
		}
	}

	private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
		if (!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String value = "/site-logo/" + fileName;
			settingBag.updateSiteLogo(value);

			String uploadDir = "../site-logo/";
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		}
	}

	private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag) {
		Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
		Optional<Currency> findByIdResult = currencyService.findById(currencyId);

		if (findByIdResult.isPresent()) {
			Currency currency = findByIdResult.get();
			settingBag.updateCurrencySymbol(currency.getSymbol());
		}
	}

	private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
		for (Setting setting : listSettings) {
			String value = request.getParameter(setting.getKey());
			if (value != null) {
				setting.setValue(value);
			}
		}

		settingService.saveAll(listSettings);
	}

	@PostMapping("/settings/save_mail_server")
	public String saveMailServerSetttings(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		List<Setting> mailServerSettings = settingService.getMailServerSettings();
		
		if (!settingService.checkValidSettingValue(request, mailServerSettings)) {
			redirectAttributes.addFlashAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
			
			return "redirect:/settings";
		} else {
			updateSettingValuesFromForm(request, mailServerSettings);
			
			redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin Mail Server thành công.");
			
			return "redirect:/settings";
		}
	}

	@PostMapping("/settings/save_mail_templates")
	public String saveMailTemplateSetttings(HttpServletRequest request, RedirectAttributes redirectAttributes) {
		List<Setting> mailTemplateSettings = settingService.getMailTemplateSettings();
		
		if (!settingService.checkValidSettingValue(request, mailTemplateSettings)) {
			redirectAttributes.addFlashAttribute("error", "Vui lòng nhập đầy đủ thông tin.");
			
			return "redirect:/settings";
		} else {
			updateSettingValuesFromForm(request, mailTemplateSettings);
			
			redirectAttributes.addFlashAttribute("message", "Cập nhật biểu mẫu E-mail thành công.");
			
			return "redirect:/settings";
		}
	}
}
