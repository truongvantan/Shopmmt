package com.shopmmt.common.entity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.shopmmt.common.enums.OrderStatus;
import com.shopmmt.common.enums.PaymentMethod;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "first_name", nullable = false, length = 45)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = 45)
	private String lastName;

	@Column(name = "phone_number", nullable = false, length = 15)
	private String phoneNumber;

	@Column(name = "address_line_1", nullable = false, length = 64)
	private String addressLine1;

	@Column(name = "address_line_2", length = 64)
	private String addressLine2;

	@Column(nullable = false, length = 45)
	private String city;

	@Column(nullable = false, length = 45)
	private String state;

	@Column(name = "postal_code", nullable = false, length = 10)
	private String postalCode;

	private Date orderTime;

	private double shippingCost;
	private double productCost;
	private double subtotal;
	private double tax;
	private double total;

	private int deliverDays;
	private Date deliverDate;

	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<OrderDetail> orderDetails = new HashSet<OrderDetail>();

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("updatedTime ASC")
	private List<OrderTrack> orderTracks = new ArrayList<OrderTrack>();

	public Order() {
		super();
	}

	public Order(Integer id, Date orderTime, double productCost, double subtotal, double total) {
		super();
		this.id = id;
		this.orderTime = orderTime;
		this.productCost = productCost;
		this.subtotal = subtotal;
		this.total = total;
	}

	public List<OrderTrack> getOrderTracks() {
		return orderTracks;
	}

	public void setOrderTracks(List<OrderTrack> orderTracks) {
		this.orderTracks = orderTracks;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public double getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(double shippingCost) {
		this.shippingCost = shippingCost;
	}

	public double getProductCost() {
		return productCost;
	}

	public void setProductCost(double productCost) {
		this.productCost = productCost;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public double getTax() {
		return tax;
	}

	public void setTax(double tax) {
		this.tax = tax;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getDeliverDays() {
		return deliverDays;
	}

	public void setDeliverDays(int deliverDays) {
		this.deliverDays = deliverDays;
	}

	public Date getDeliverDate() {
		return deliverDate;
	}

	public void setDeliverDate(Date deliverDate) {
		this.deliverDate = deliverDate;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Set<OrderDetail> getOrderDetails() {
		return orderDetails;
	}

	public void setOrderDetails(Set<OrderDetail> orderDetails) {
		this.orderDetails = orderDetails;
	}

	public void copyAddressFromCustomer() {
		setFirstName(customer.getFirstName());
		setLastName(customer.getLastName());
		setPhoneNumber(customer.getPhoneNumber());
		setAddressLine1(customer.getAddressLine1());
		setAddressLine2(customer.getAddressLine2());
		setCity(customer.getCity());
		setPostalCode(customer.getPostalCode());
		setState(customer.getState());
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", subtotal=" + subtotal + ", paymentMethod=" + paymentMethod + ", status=" + status
				+ ", customer=" + customer.getFullName() + "]";
	}

	@Transient
	public String getDestination() {
		String destination = "";

		if (state != null && !state.isEmpty())
			destination += state + ", ";

		if (city != null && !city.isEmpty()) {
			destination += city;
		}

		return destination;
	}

	public void copyShippingAddress(Address address) {
		setFirstName(address.getFirstName());
		setLastName(address.getLastName());
		setPhoneNumber(address.getPhoneNumber());
		setAddressLine1(address.getAddressLine1());
		setAddressLine2(address.getAddressLine2());
		setCity(address.getCity());
		setPostalCode(address.getPostalCode());
		setState(address.getState());
	}

	@Transient
	public String getShippingAddress() {
		String address = lastName != null ? lastName : "";

		if (firstName != null && !firstName.isEmpty()) {
			address += " " + firstName;
		}

		if (addressLine1 != null && !addressLine1.isEmpty()) {
			address += ", " + addressLine1;
		}

		if (addressLine2 != null && !addressLine2.isEmpty()) {
			address += ", " + addressLine2;
		}

		if (state != null && !state.isEmpty()) {
			address += ", " + state;
		}

		if (city != null && !city.isEmpty()) {
			address += ", " + city;
		}

		if (postalCode != null && !postalCode.isEmpty()) {
			address += ". Mã bưu chính: " + postalCode;
		}

		if (phoneNumber != null && !phoneNumber.isEmpty()) {
			address += ". Số điện thoại: " + phoneNumber;
		}

		return address;
	}

	@Transient
	public String getDeliverDateOnForm() {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

		if (this.deliverDate == null) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, deliverDays);

			this.deliverDate = cal.getTime();
		}

		return dateFormatter.format(this.deliverDate);
	}

	public void setDeliverDateOnForm(String dateString) {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

		try {
			this.deliverDate = dateFormatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Transient
	public String getRecipientName() {
		String name = lastName != null ? lastName : "";
		if (firstName != null && !firstName.isEmpty())
			name += " " + firstName;
		return name;
	}

	@Transient
	public String getRecipientAddress() {
		String address = addressLine1 != null ? addressLine1 : "";

		if (addressLine2 != null && !addressLine2.isEmpty()) {
			address += ", " + addressLine2;
		}

		if (state != null && !state.isEmpty()) {
			address += ", " + state;
		}

		if (city != null && !city.isEmpty()) {
			address += ", " + city;
		}

		if (postalCode != null && !postalCode.isEmpty()) {
			address += ". " + postalCode;
		}

		return address;
	}

	@Transient
	public boolean isCOD() {
		return paymentMethod.equals(PaymentMethod.COD);
	}

	@Transient
	public boolean isPicked() {
		return hasStatus(OrderStatus.PICKED);
	}

	@Transient
	public boolean isShipping() {
		return hasStatus(OrderStatus.SHIPPING);
	}

	@Transient
	public boolean isDelivered() {
		return hasStatus(OrderStatus.DELIVERED);
	}

	@Transient
	public boolean isReturned() {
		return hasStatus(OrderStatus.RETURNED);
	}

	@Transient
	public boolean isReturnRequested() {
		return hasStatus(OrderStatus.RETURN_REQUESTED);
	}

	@Transient
	public boolean isProcessing() {
		return hasStatus(OrderStatus.PROCESSING);
	}

	public boolean hasStatus(OrderStatus status) {
		for (OrderTrack aTrack : orderTracks) {
			if (aTrack.getStatus().equals(status)) {
				return true;
			}
		}

		return false;
	}

	@Transient
	public String getProductNames() {
		String productNames = "";

		productNames = "<ol>";

		for (OrderDetail detail : orderDetails) {
			productNames += "<li>" + detail.getProduct().getShortName() + "</li>";
		}

		productNames += "</ol>";

		return productNames;
	}

}
