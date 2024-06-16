// Sales Report by Date
var data;
var chartOptions;

$(document).ready(function() {
	setupButtonEventHandlers("_category", loadSalesReportByDateForCategory);	
});

function loadSalesReportByDateForCategory(period) {
	if (period == "custom") {
		startDate = $("#startDate_category").val();
		endDate = $("#endDate_category").val();
		
		requestURL = contextPath + "reports/category/" + startDate + "/" + endDate;
	} else {
		requestURL = contextPath + "reports/category/" + period;		
	}
	
	$.get(requestURL, function(responseJSON) {
		prepareChartDataForSalesReportByCategory(responseJSON);
		customizeChartForSalesReportByCategory();
		formatChartData(data, 1, 2);
		drawChartForSalesReportByCategory(period);
		setSalesAmount(period, '_category', "Tổng số sản phẩm đã bán");
	});
}

function prepareChartDataForSalesReportByCategory(responseJSON) {
	data = new google.visualization.DataTable();
	data.addColumn('string', 'Danh mục');
	data.addColumn('number', 'Tổng doanh thu');
	data.addColumn('number', 'Tổng doanh thu thuần');
	data.addColumn('number', 'Tổng số sản phẩm đã bán');
	
	totalGrossSales = 0.0;
	totalNetSales = 0.0;
	totalItems = 0;
	
	$.each(responseJSON, function(index, reportItem) {
		data.addRows([[reportItem.identifier, reportItem.grossSales, reportItem.netSales, reportItem.productsCount]]);
		totalGrossSales += parseFloat(reportItem.grossSales);
		totalNetSales += parseFloat(reportItem.netSales);
		totalItems += parseInt(reportItem.productsCount);
	});
}

function customizeChartForSalesReportByCategory() {
	chartOptions = {
		height: 360,
		legend: {
			position: 'right',
			textStyle: {
          		fontSize: 20
        	}
        },
		pieSliceTextStyle: {
        	fontSize: 20,
      	}
	};
}

function drawChartForSalesReportByCategory() {
	var salesChart = new google.visualization.PieChart(document.getElementById('chart_sales_by_category'));
	salesChart.draw(data, chartOptions);
}