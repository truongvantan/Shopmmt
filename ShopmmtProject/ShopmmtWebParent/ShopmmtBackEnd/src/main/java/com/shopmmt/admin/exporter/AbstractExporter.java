package com.shopmmt.admin.exporter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jakarta.servlet.http.HttpServletResponse;

public class AbstractExporter {
	public void setResponseHeader(HttpServletResponse response, String extension, String contentType) throws IOException {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
		String timeStamp = dateFormat.format(new Date());
		String fileName = "users_" + timeStamp + extension;
		
		response.setCharacterEncoding("UTF-8");
		response.setContentType(contentType);

		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);

	}
}
