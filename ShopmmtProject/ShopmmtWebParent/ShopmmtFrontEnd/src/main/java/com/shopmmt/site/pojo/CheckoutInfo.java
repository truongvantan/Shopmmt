package com.shopmmt.site.pojo;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CheckoutInfo {
	private double productCost;
	private double productTotal;
	private double shippingCostTotal;
	private double paymentTotal;
	private int deliverDays;

	public CheckoutInfo() {
		super();
	}

	public double getProductCost() {
		return productCost;
	}

	public void setProductCost(double productCost) {
		this.productCost = productCost;
	}

	public double getProductTotal() {
		return productTotal;
	}

	public void setProductTotal(double productTotal) {
		this.productTotal = productTotal;
	}

	public double getShippingCostTotal() {
		return shippingCostTotal;
	}

	public void setShippingCostTotal(double shippingCostTotal) {
		this.shippingCostTotal = shippingCostTotal;
	}

	public double getPaymentTotal() {
		return paymentTotal;
	}

	public void setPaymentTotal(double paymentTotal) {
		this.paymentTotal = paymentTotal;
	}

	public int getDeliverDays() {
		return deliverDays;
	}

	public void setDeliverDays(int deliverDays) {
		this.deliverDays = deliverDays;
	}

	public Date getDeliverDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, deliverDays);

		return calendar.getTime();
	}

//	public String getPaymentTotal4PayPal() {
//		DecimalFormat formatter = new DecimalFormat("###,###.##");
//		return formatter.format(paymentTotal);
//	}

//	public String getPaymentTotal4PayPal() {
//		DecimalFormat formatter = new DecimalFormat("###.###,##");
//		return formatter.format(paymentTotal);
//	}

	public String getPaymentTotal4PayPal() {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
		DecimalFormat formatter = new DecimalFormat("0.00", symbols);
		
		return formatter.format(paymentTotal);
	}

}
