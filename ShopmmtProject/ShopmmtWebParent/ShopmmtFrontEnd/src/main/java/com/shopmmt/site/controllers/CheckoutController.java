package com.shopmmt.site.controllers;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shopmmt.common.entity.Address;
import com.shopmmt.common.entity.CartItem;
import com.shopmmt.common.entity.Customer;
import com.shopmmt.common.entity.Order;
import com.shopmmt.common.enums.PaymentMethod;
import com.shopmmt.site.common.CurrencySettingBag;
import com.shopmmt.site.common.EmailSettingBag;
import com.shopmmt.site.common.PaymentSettingBag;
import com.shopmmt.site.common.Utility;
import com.shopmmt.site.exceptions.PayPalApiException;
import com.shopmmt.site.pojo.CheckoutInfo;
import com.shopmmt.site.services.AddressService;
import com.shopmmt.site.services.CheckoutService;
import com.shopmmt.site.services.CustomerService;
import com.shopmmt.site.services.OrderService;
import com.shopmmt.site.services.SettingService;
import com.shopmmt.site.services.ShoppingCartService;
import com.shopmmt.site.services.impl.PayPalService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CheckoutController {

	@Autowired
	private CheckoutService checkoutService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private AddressService addressService;

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private SettingService settingService;

	@Autowired
	private PayPalService paypalService;

	@GetMapping("/checkout")
	public String showCheckoutPage(Model model, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);

		Address defaultAddress = addressService.getDefaultAddress(customer);

		if (defaultAddress != null) {
			model.addAttribute("shippingAddress", defaultAddress.toString());
		} else {
			model.addAttribute("shippingAddress", customer.toString());
		}

		List<CartItem> cartItems = shoppingCartService.listCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems);

		String currencyCode = settingService.getCurrencyCode();
		PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
		String paypalClientId = paymentSettings.getClientID();

		model.addAttribute("paypalClientId", paypalClientId);
		model.addAttribute("currencyCode", currencyCode);
		model.addAttribute("customer", customer);
		model.addAttribute("checkoutInfo", checkoutInfo);
		model.addAttribute("cartItems", cartItems);

		return "checkout/checkout";
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);
		return customerService.getCustomerByEmail(email);
	}

	@PostMapping("/place_order")
	public String placeOrder(HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		String paymentType = request.getParameter("paymentMethod");
		PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentType);

		Customer customer = getAuthenticatedCustomer(request);

		Address defaultAddress = addressService.getDefaultAddress(customer);

		List<CartItem> cartItems = shoppingCartService.listCartItems(customer);
		CheckoutInfo checkoutInfo = checkoutService.prepareCheckout(cartItems);

		Order createdOrder = orderService.createOrder(customer, defaultAddress, cartItems, paymentMethod, checkoutInfo);
		shoppingCartService.deleteByCustomer(customer);

		sendOrderConfirmationEmail(request, createdOrder);

		return "checkout/order_completed";
	}

	private void sendOrderConfirmationEmail(HttpServletRequest request, Order order)
			throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);
		mailSender.setDefaultEncoding("utf-8");

		String toAddress = order.getCustomer().getEmail();
		String subject = emailSettings.getOrderConfirmationSubject();
		String content = emailSettings.getOrderConfirmationContent();

		subject = subject.replace("[[orderId]]", String.valueOf(order.getId()));

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);

		DateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss, dd-MMM-yyyy");
		String orderTime = dateFormatter.format(order.getOrderTime());

		CurrencySettingBag currencySettings = settingService.getCurrencySettings();
		String totalAmount = Utility.formatCurrency(order.getTotal(), currencySettings);

		content = content.replace("[[name]]", order.getCustomer().getFullName());
		content = content.replace("[[orderId]]", String.valueOf(order.getId()));
		content = content.replace("[[orderTime]]", orderTime);
		content = content.replace("[[shippingAddress]]", order.getShippingAddress());
		content = content.replace("[[total]]", totalAmount);
		content = content.replace("[[paymentMethod]]", order.getPaymentMethod().label.toString());

		helper.setText(content, true);

		message.setContent(content, "text/html; charset=utf-8");

		mailSender.send(message);
	}

	@PostMapping("/process_paypal_order")
	public String processPayPalOrder(HttpServletRequest request, Model model)
			throws UnsupportedEncodingException, MessagingException {
		String orderId = request.getParameter("orderId");

		String pageTitle = "Thanh toán thất bại";
		String errorMessage = null;

		try {
			if (paypalService.validateOrder(orderId)) {
				return placeOrder(request);
			} else {
				pageTitle = "Thanh toán thất bại";
				errorMessage = "LỖI: Thông tin giao dịch không hợp lệ";
			}
		} catch (PayPalApiException e) {
			errorMessage = "LỖI: Giao dịch thất bại: " + e.getMessage();
		}

		model.addAttribute("pageTitle", pageTitle);
		model.addAttribute("title", pageTitle);
		model.addAttribute("error", errorMessage);

		return "message";
	}
}
