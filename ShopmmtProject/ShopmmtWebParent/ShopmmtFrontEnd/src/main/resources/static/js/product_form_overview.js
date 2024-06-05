const dropdownBrands = $("#brand");
const dropdownCategories = $("#category");

$(document).ready(function() {

	$("#shortDescription").richText();
	$("#fullDescription").richText();

	dropdownBrands.change(function() {
		dropdownCategories.empty();
		getCategories();
	});

	getCategoriesForNewForm();
});

function getCategoriesForNewForm() {
	catIdField = $("#categoryId");
	editMode = false;

	if (catIdField.length) {
		editMode = true;
	}

	if (!editMode) {
		getCategories();
	}
}


function getCategories() {
	brandId = dropdownBrands.val();
	url = brandModuleURL + "/" + brandId + "/categories";

	$.get(url, function(responseJson) {
		$.each(responseJson, function(index, category) {
			$("<option>").val(category.id).text(category.name)
				.appendTo(dropdownCategories);
		});
	});
}

function checkProductNameUnique(form) {
	productId = $("#id").val();
	productName = $("#name").val();
	csrfValue = $("input[name='_csrf']").val();
	params = {
		id: productId,
		name: productName,
		_csrf: csrfValue
	};
	$
		.post(
			checkProductNameUniqueUrl,
			params,
			function(response) {
				if (response == "OK") {
					form.submit();
				} else if (response == "Duplicated") {
					showModalDialog(
						"Cảnh báo",
						"Tên sản phẩm: "
						+ productName
						+ " đã tồn tại! Vui lòng thử lại.")
				} else {
					showModalDialog("Lỗi",
						"Đã xảy ra lỗi không xác định! Vui lòng thử lại.");
				}
			}).fail(
				function() {
					showModalDialog("Lỗi",
						"Không thể kết nối đến máy chủ!");
				});

	return false;
}

function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal('show');
}