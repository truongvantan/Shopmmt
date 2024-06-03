$(document).ready(function() {
	$("a[name='linkRemoveDetail']").each(function(index) {
		$(this).click(function() {
			removeDetailSectionByIndex(index);
		});
	});
});

function addNextDetailSection() {
	allDivDetails = $("[id^='divDetail']");
	divDetailsCount = allDivDetails.length;
	
	htmlDetailSection = `
		<div
			class="row d-flex align-items-center mb-4"
			id="divDetail${divDetailsCount}"
		>
			<input type="hidden" name="detailIDs" value="0" />
			<div class="col-sm-2 d-flex justify-content-end">
				<label>Tên thuộc tính:</label>
			</div>
			<div class="col-sm-4">
				<input
					type="text"
					class="form-control"
					name="detailNames"
					maxlength="255"
				/>
			</div>
			<div class="col-sm-1 d-flex justify-content-end">
				<label>Giá trị:</label>
			</div>
			<div class="col-sm-4">
				<input
					type="text"
					class="form-control"
					name="detailValues"
					maxlength="255"
				/>
			</div>
		</div>
	`;
	
	$("#divProductDetails").append(htmlDetailSection);

	previousDivDetailSection = allDivDetails.last();
	previousDivDetailID = previousDivDetailSection.attr("id");
	
	 	
	htmlLinkRemove = `
		<div class="col-sm-1">
			<a
				class="btn fas fa-times-circle fa-xl icon-red"
				href="javascript:removeDetailSectionById('${previousDivDetailID}')"
				title="Xóa thuộc tính này"
			></a>
		</div>
	`;
	
	previousDivDetailSection.append(htmlLinkRemove);
	
	$("input[name='detailNames']").last().focus();
	
	// checkProductDetailsUnique();
	
}

/*function checkProductDetailsUnique() {
	let detail_name = [];
	let detail_value = [];
	detailNames = $("input[name='detailNames']");
	detailValues = $("input[name='detailValues']");
	
	detailNames.each(function() {
		detail_name.push($(this).val())
	})
	
	detailValues.each(function() {
		detail_value.push($(this).val())
	})
	
	detail_name = JSON.stringify(detail_name);
	detail_value = JSON.stringify(detail_value);
	
	console.log("detail_name: " + detail_name);
	console.log("detail_value: " + detail_value);
	
	csrfValue = $("input[name='_csrf']").val();
	params = {
		productDetailNames: detail_name,
		productDetailValues: detail_value,
		_csrf: csrfValue
	};
	$
		.post(
			checkProductDetailsUniqueUrl,
			params,
			function(response) {
				if (response == "OK") {
					// form.submit();
					alert("Thuộc tính OK")
				} else if (response == "Duplicated") {
					showModalDialog(
						"Cảnh báo",
						"Thuộc tính: "
						
						+ " đã tồn tại! Vui lòng thử lại.")
				} else {
					showModalDialog("Lỗi",
						"Đã xảy ra lỗi không xác định! Vui lòng thử lại.");
				}
				alert("response: " + response);
			}).fail(
				function() {
					showModalDialog("Lỗi",
						"Không thể kết nối đến máy chủ!");
				});

	return false;
}*/

function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal('show');
}

function removeDetailSectionById(id) {
	$("#" + id).remove();
}

function removeDetailSectionByIndex(index) {
	$("#divDetail" + index).remove();	
}