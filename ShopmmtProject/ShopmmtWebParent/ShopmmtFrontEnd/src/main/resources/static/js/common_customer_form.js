function showDeleteConfirmModal(link, entityName) {
	entityId = link.attr("entityId");
	$("#confirmButton").attr("href", link.attr("href"));
	$("#confirmText").text("Bạn chắc chắn muốn xóa " + entityName + " ID " + entityId + "?");
	$("#confirmModal").modal("show");
}

function showModalDialog(title, message) {
	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal('show');
}

function showErrorModal(message) {
	showModalDialog("Lỗi", message);
}

function showWarningModal(message) {
	showModalDialog("Cảnh báo", message);
}