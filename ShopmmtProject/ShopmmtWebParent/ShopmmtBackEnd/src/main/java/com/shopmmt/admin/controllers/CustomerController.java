package com.shopmmt.admin.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopmmt.admin.services.CustomerService;
import com.shopmmt.common.dto.CustomerRegisterFormDTO;
import com.shopmmt.common.entity.Customer;
import com.shopmmt.common.exception.CustomerNotFoundException;

import jakarta.validation.Valid;

@Controller
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@GetMapping("/customers")
	public String listFirstPage(Model model) {
		return listByPage(model, 1, "firstName", "asc", null);
	}

	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(Model model, @PathVariable(name = "pageNum", required = false) int pageNum,
			@RequestParam(name = "sortField", required = false) String sortField,
			@RequestParam(name = "sortDir", required = false) String sortDir,
			@RequestParam(name = "keyword", required = false) String keyword) {

		String reverseSortDir = "asc".equals(sortDir) ? "desc" : "asc";

		Page<Customer> pageCustomers = customerService.listByPage(pageNum, sortField, sortDir, keyword);
		List<Customer> listCustomers = pageCustomers.getContent();

		model.addAttribute("listCustomers", listCustomers);

		model.addAttribute("totalPages", pageCustomers.getTotalPages());
		model.addAttribute("totalItems", pageCustomers.getTotalElements());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("reverseSortDir", reverseSortDir);

		return "customers/customers";
	}

	@GetMapping("/customers/{id}/enabled/{status}")
	public String updateCustomerEnabledStatus(@PathVariable(name = "id", required = false) Integer id,
			@PathVariable(name = "status", required = false) boolean enabled, RedirectAttributes redirectAttributes) {

		customerService.updateCustomerEnabledStatus(id, enabled);

		String status = enabled ? "mở khóa" : "khóa";
		String message = "Đã " + status + " khách hàng ID " + id;

		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/customers";
	}

	@GetMapping("/customers/showEdit/{id}")
	public String showEditCustomer(@PathVariable(name = "id", required = false) Integer id, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Customer customer = customerService.get(id);

			CustomerRegisterFormDTO customerRegisterFormDTO = new CustomerRegisterFormDTO(customer);

			model.addAttribute("customer", customerRegisterFormDTO);

			return "customers/customer_edit_form";

		} catch (CustomerNotFoundException e) {
			if (e.getMessage().contains("Could not find any customer with ID")) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy khách hàng có ID " + id);
			}
			return "redirect:/customers";
		}
	}

	@PostMapping("/customers/edit")
	public String editCustomer(Model model, @RequestParam(name = "id", required = false) Integer id,
			@RequestParam(name = "newPassword", required = false) String newPassword,
			@Valid @ModelAttribute("customer") CustomerRegisterFormDTO customerRegisterFormDTO,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		
		if (bindingResult.hasErrors()) {
			System.err.println("Lỗi binding result CustomerRegisterFormDTO");
			model.addAttribute("id", id);
			
			return "customers/customer_edit_form";
		} else {

			if (!customerService.validatePassword(newPassword)) {
				model.addAttribute("id", id);
				model.addAttribute("errorNewPassword",
						"Mật khẩu phải có độ dài ít nhất 6 ký tự và tối đa 60 ký tự. Chứa ít nhất 1 kí tự chữ số, chữ cái thường, chữ cái in hoa và kí tự đặc biệt.");
				
				return "customers/customer_edit_form";
			} else {
				customerRegisterFormDTO.setPassword(newPassword);
				Customer customer = new Customer(customerRegisterFormDTO);
				
				customerService.save(customer);
				
				redirectAttributes.addFlashAttribute("message", "Cập nhật khách hàng ID " + customerRegisterFormDTO.getId() + " thành công.");
				return "redirect:/customers";
			}
		}
	}
	
	@GetMapping("/customers/delete/{id}")
	public String deleteCustomer(@PathVariable(name = "id", required = false) Integer id, RedirectAttributes redirectAttributes) {
		try {
			customerService.delete(id);			
			redirectAttributes.addFlashAttribute("message", "Xóa khách hàng ID " + id + " thành công.");
			
		} catch (CustomerNotFoundException e) {
			if (e.getMessage().contains("Could not find any customer with ID")) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy khách hàng có ID " + id);
			}
			return "redirect:/customers";
		}
		
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/detail/{id}")
	public String viewCustomer(@PathVariable(name = "id", required = false) Integer id, Model model, RedirectAttributes redirectAttributes) {
		try {
			Customer customer = customerService.get(id);
			model.addAttribute("customer", customer);
			
			return "customers/customer_detail_modal";
		} catch (CustomerNotFoundException e) {
			if (e.getMessage().contains("Could not find any customer with ID")) {
				redirectAttributes.addFlashAttribute("error", "Không tìm thấy khách hàng có ID " + id);
			}
			return "redirect:/customers";			
		}
	}
}
