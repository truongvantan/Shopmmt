var iconNames = {
	'PICKED': 'fa-people-carry',
	'SHIPPING': 'fa-shipping-fast',
	'DELIVERED': 'fa-box-open',
	'RETURNED': 'fa-undo'
};

var confirmText;
var confirmModalDialog;
var yesButton;
var noButton;

$(document).ready(function() {
	confirmText = $("#confirmText");
	confirmModalDialog = $("#confirmModal");
	yesButton = $("#yesButton");
	noButton = $("#noButton");

	$(".linkUpdateStatus").on("click", function(e) {
		e.preventDefault();
		link = $(this);
		showUpdateConfirmModal(link);
	});

	addEventHandlerForYesButton();
});

function addEventHandlerForYesButton() {
	yesButton.click(function(e) {
		e.preventDefault();
		sendRequestToUpdateOrderStatus($(this));
	});
}

function sendRequestToUpdateOrderStatus(button) {
	requestURL = button.attr("href");
	
	$.ajax({
		type: 'POST',
		url: requestURL,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response) {
		showMessageModal("Cập nhật trạng thái đơn hàng thành công");
		updateStatusIconColor(response.orderId, response.status);
		
		console.log(response);
	}).fail(function(err) {
		showMessageModal("Đã có lỗi xảy ra khi cập nhật trạng thái đơn hàng");
	})
}

function updateStatusIconColor(orderId, status) {
	link = $("#link" + status + orderId);
	link.replaceWith("<i class='fas " + iconNames[status] + " fa-2x icon-green'></i>");
}

function showUpdateConfirmModal(link) {
	noButton.text("HỦY");
	yesButton.show();
		
	orderId = link.attr("orderId");
	status = link.attr("status");
	yesButton.attr("href", link.attr("href"));
	
	confirmText.text("Bạn chắc chắc muốn cập nhật trạng thái đơn hàng ID #" + orderId
					 + " thành " + status + "?");
					 
	confirmModalDialog.modal("show");
}

function showMessageModal(message) {
	noButton.text("Đóng");
	yesButton.hide();
	confirmText.text(message);
}