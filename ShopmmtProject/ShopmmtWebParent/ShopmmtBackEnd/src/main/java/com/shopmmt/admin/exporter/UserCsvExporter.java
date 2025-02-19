package com.shopmmt.admin.exporter;

import java.io.IOException;
import java.util.List;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopmmt.common.entity.User;

import jakarta.servlet.http.HttpServletResponse;

public class UserCsvExporter extends AbstractExporter {
	public void export(List<User> listUsers, HttpServletResponse response) throws IOException {
		super.setResponseHeader(response, ".csv", "text/csv; charset=UTF-8");
		
		ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
		String[] csvHeader = {"ID", "E-mail", "Tên", "Họ đệm", "Vai trò", "Khả dụng"};
		csvBeanWriter.writeHeader(csvHeader);
		
		String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};
		
		for (User user : listUsers) {
			csvBeanWriter.write(user, fieldMapping);
		}
		
		csvBeanWriter.close();
	}
}
