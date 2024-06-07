function clearFilter() {
	window.location = moduleURL;
}

function showDeleteConfirmModal(link, entityName) {
	entityId = link.attr("entityId");
	$("#confirmButton").attr("href", link.attr("href"));
	$("#confirmText").text("Bạn chắc chắn muốn xóa " + entityName + " ID " + entityId + "?");
	$("#confirmModal").modal("show");
}