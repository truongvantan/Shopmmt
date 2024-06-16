package com.shopmmt.admin.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopmmt.admin.enums.ReportType;
import com.shopmmt.admin.pojo.ReportItem;
import com.shopmmt.admin.repositories.OrderDetailRepository;
import com.shopmmt.admin.services.AbstractReportService;
import com.shopmmt.common.entity.OrderDetail;

@Service("orderDetailReportService")
public class OrderDetailReportService extends AbstractReportService {

	@Autowired
	private OrderDetailRepository orderDetailRepository;

	@Override
	protected List<ReportItem> getReportDataByDateRangeInternal(Date startDate, Date endDate, ReportType reportType) {
		List<OrderDetail> listOrderDetails = null;

		if (ReportType.CATEGORY.equals(reportType)) {
			listOrderDetails = orderDetailRepository.findWithCategoryAndTimeBetween(startDate, endDate);
		} else if (ReportType.PRODUCT.equals(reportType)) {
			listOrderDetails = orderDetailRepository.findWithProductAndTimeBetween(startDate, endDate);
		}

//		printRawData(listOrderDetails);

		List<ReportItem> listReportItems = new ArrayList<ReportItem>();

		for (OrderDetail detail : listOrderDetails) {
			String identifier = "";
			
			if (ReportType.CATEGORY.equals(reportType)) {
				identifier = detail.getProduct().getCategory().getName();
			} else if (ReportType.PRODUCT.equals(reportType)) {
				identifier = detail.getProduct().getShortName();
			}
			
			ReportItem reportItem = new ReportItem(identifier);

			double grossSales = detail.getSubtotal() + detail.getShippingCost();
			double netSales = detail.getSubtotal() - detail.getProductCost();

			int itemIndex = listReportItems.indexOf(reportItem);

			if (itemIndex >= 0) {
				reportItem = listReportItems.get(itemIndex);
				reportItem.addGrossSales(grossSales);
				reportItem.addNetSales(netSales);
				reportItem.increaseProductsCount(detail.getQuantity());
			} else {
				listReportItems.add(new ReportItem(identifier, grossSales, netSales, detail.getQuantity()));
			}
		}

//		printReportData(listReportItems);

		return listReportItems;
	}

	private void printReportData(List<ReportItem> listReportItems) {
		for (ReportItem item : listReportItems) {
			System.out.printf("%-20s, %10.2f, %10.2f, %d \n", item.getIdentifier(), item.getGrossSales(),
					item.getNetSales(), item.getProductsCount());
		}
	}

	private void printRawData(List<OrderDetail> listOrderDetails) {
		for (OrderDetail detail : listOrderDetails) {
			System.out.printf("%d, %-20s, %10.2f, %10.2f, %10.2f \n", detail.getQuantity(),
					detail.getProduct().getCategory().getName(), detail.getSubtotal(), detail.getProductCost(),
					detail.getShippingCost());
		}
	}
}
