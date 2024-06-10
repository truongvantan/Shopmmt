package com.shopmmt.site.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopmmt.common.dto.AddressFormDTO;
import com.shopmmt.common.entity.Address;
import com.shopmmt.common.entity.Customer;
import com.shopmmt.site.common.Utility;
import com.shopmmt.site.services.AddressService;
import com.shopmmt.site.services.CustomerService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class AddressController {

	@Autowired
	private AddressService addressService;

	@Autowired
	private CustomerService customerService;

	@GetMapping("/address_book")
	public String showAddressBook(Model model, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		List<Address> listAddresses = addressService.listAddressBook(customer);

		boolean usePrimaryAddressAsDefault = true;
		for (Address address : listAddresses) {
			if (address.isDefaultForShipping()) {
				usePrimaryAddressAsDefault = false;
				break;
			}
		}

		model.addAttribute("listAddresses", listAddresses);
		model.addAttribute("customer", customer);
		model.addAttribute("usePrimaryAddressAsDefault", usePrimaryAddressAsDefault);

		return "address_book/addresses";
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		return customerService.getCustomerByEmail(email);
	}

	@GetMapping("/address_book/new")
	public String newAddress(Model model) {

		AddressFormDTO addressFormDTO = new AddressFormDTO();

		model.addAttribute("address", addressFormDTO);
		model.addAttribute("pageTitle", "Thêm mới địa chỉ");

		return "address_book/address_form";
	}

	@PostMapping("/address_book/save")
	public String saveAddress(Model model, @Valid @ModelAttribute("address") AddressFormDTO addressFormDTO,
			BindingResult bindingResult, HttpServletRequest request, RedirectAttributes redirectAttributes) {

		Customer customer = getAuthenticatedCustomer(request);

		if (bindingResult.hasErrors()) {
			System.err.println("Lỗi binding result AddressFormDTO");
			model.addAttribute("pageTitle", "Thêm mới địa chỉ");

			return "address_book/address_form";
		} else {

			Address address = new Address(addressFormDTO);

			address.setCustomer(customer);
			addressService.save(address);

			redirectAttributes.addFlashAttribute("message", "Thêm mới địa chỉ thành công.");

			return "redirect:/address_book";
		}

	}

	@GetMapping("/address_book/edit/{id}")
	public String editAddress(@PathVariable(name = "id", required = false) Integer addressId, Model model,
			HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);

		Address address = addressService.get(addressId, customer.getId());

		AddressFormDTO addressFormDTO = new AddressFormDTO(address);

		model.addAttribute("address", addressFormDTO);
		model.addAttribute("pageTitle", "Cập nhật địa chỉ (ID: " + addressId + ")");

		return "address_book/address_edit_form";
	}

	@PostMapping("/address_book/edit")
	public String editAddress(Model model, @Valid @ModelAttribute("address") AddressFormDTO addressFormDTO,
			BindingResult bindingResult, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Customer customer = getAuthenticatedCustomer(request);

		if (bindingResult.hasErrors()) {
			System.err.println("Lỗi binding result AddressFormDTO");
			model.addAttribute("pageTitle", "Cập nhật địa chỉ (ID: " + addressFormDTO.getId() + ")");

			return "address_book/address_edit_form";
		} else {

			Address address = new Address(addressFormDTO);

			address.setCustomer(customer);
			addressService.save(address);

			redirectAttributes.addFlashAttribute("message", "Cập nhật địa chỉ thành công.");

			return "redirect:/address_book";

		}

	}

	@GetMapping("/address_book/delete/{id}")
	public String deleteAddress(@PathVariable("id") Integer addressId, RedirectAttributes ra,
			HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		addressService.delete(addressId, customer.getId());

		ra.addFlashAttribute("message", "Xóa địa chỉ ID " + addressId + " thành công.");

		return "redirect:/address_book";
	}

	@GetMapping("/address_book/default/{id}")
	public String setDefaultAddress(@PathVariable(name = "id", required = false) Integer addressId,
			HttpServletRequest request) {
		
		Customer customer = getAuthenticatedCustomer(request);
		addressService.setDefaultAddress(addressId, customer.getId());

		return "redirect:/address_book";
	}

}
