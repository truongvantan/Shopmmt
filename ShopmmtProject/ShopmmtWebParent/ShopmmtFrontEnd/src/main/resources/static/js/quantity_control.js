$(document).ready(function() {
	$(".linkMinus").on("click", function(evt) {
		evt.preventDefault();
		productId = $(this).attr("pid");
		quantityInput = $("#quantity" + productId);
		newQuantity = parseInt(quantityInput.val()) - 1;
		
		if (newQuantity > 0) {
			quantityInput.val(newQuantity);
		} else {
			showWarningModal('Số lượng tối thiểu cho phép là 1');
		}
	});
	
	$(".linkPlus").on("click", function(evt) {
		evt.preventDefault();
		productId = $(this).attr("pid");
		quantityInput = $("#quantity" + productId);
		newQuantity = parseInt(quantityInput.val()) + 1;
		
		if (newQuantity <= 5) {
			quantityInput.val(newQuantity);
		} else {
			showWarningModal('Số lượng tối đa cho phép là 5');
		}
	});	
});