package com.shopmmt.admin.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.shopmmt.admin.enums.ReportType;
import com.shopmmt.admin.pojo.ReportItem;

public abstract class AbstractReportService {
	
	protected DateFormat dateFormatter;
	
	protected abstract List<ReportItem> getReportDataByDateRangeInternal(
			Date startDate, Date endDate, ReportType reportType);
	
	public List<ReportItem> getReportDataLast7Days(ReportType reportType) {
		return getReportDataLastXDays(7, reportType);
	}

	public List<ReportItem> getReportDataLast28Days(ReportType reportType) {
		return getReportDataLastXDays(28, reportType);
	}
	
	protected List<ReportItem> getReportDataLastXDays(int days, ReportType reportType) {
		Date endTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -(days - 1));
		Date startTime = cal.getTime();
		
		System.out.println("Start time: " + startTime);
		System.out.println("End time: " + endTime);
		
		dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		
		return getReportDataByDateRangeInternal(startTime, endTime, reportType);
	}
	
	public List<ReportItem> getReportDataLast6Months(ReportType reportType) {
		return getReportDataLastXMonths(6, reportType);
	}
	
	public List<ReportItem> getReportDataLastYear(ReportType reportType) {
		return getReportDataLastXMonths(12, reportType);
	}
	
	protected List<ReportItem> getReportDataLastXMonths(int months, ReportType reportType) {
		Date endTime = new Date();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -(months - 1));
		Date startTime = cal.getTime();
		
		System.out.println("Start time: " + startTime);
		System.out.println("End time: " + endTime);
		
		dateFormatter = new SimpleDateFormat("MM-yyyy");
		
		return getReportDataByDateRangeInternal(startTime, endTime, reportType);
	}
	
	public List<ReportItem> getReportDataByDateRange(Date startTime, Date endTime, ReportType reportType) {
		dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
		return getReportDataByDateRangeInternal(startTime, endTime, reportType);
	}
	
}
