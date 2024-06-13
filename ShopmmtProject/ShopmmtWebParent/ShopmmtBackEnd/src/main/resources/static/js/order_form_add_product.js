$(document).ready(function() {
	$("#products").on("click", "#linkAddProduct", function(e) {
		e.preventDefault();
		link = $(this);
		url = link.attr("href");

		$("#addProductModal").on("shown.bs.modal", function() {
			$(this).find("iframe").attr("src", url);
		});

		$("#addProductModal").modal("show");
	})
});

function addProduct(productId) {
	getProductInfo(productId);
	$("#addProductModal").modal("hide");
}

function getProductInfo(productId) {
	requestURL = contextPath + "products/get/" + productId;
	$.get(requestURL, function(productJson) {
		console.log(productJson);
		productName = productJson.name;
		mainImagePath = contextPath.substring(0, contextPath.length - 1) + productJson.imagePath;
		productCost = $.number(productJson.cost, 2);
		productPrice = $.number(productJson.price, 2);

		htmlCode = generateProductCode(productId, productName, mainImagePath, productCost, productPrice);
		$("#productList").append(htmlCode);
		
		updateOrderAmounts();

	}).fail(function(err) {
		showWarningModal(err.responseJSON.message);
	});
}

function generateProductCode(productId, productName, mainImagePath, productCost, productPrice) {
	nextCount = $(".hiddenProductId").length + 1;
	rowId = "row" + nextCount;
	quantityId = "quantity" + nextCount;
	priceId = "price" + nextCount;
	subtotalId = "subtotal" + nextCount;
	blankLineId= "blankLine" + nextCount;

	htmlCode = `
		<div class="border rounded p-1" id="${rowId}">
			<input type="hidden" name="detailId" value="0" />
			<input type="hidden" name="productId" value="${productId}" class="hiddenProductId" />
			<div class="row">
				<div class="col-1">
					<div class="divCount">${nextCount}</div>
					<div><a class="fas fa-trash icon-red linkRemove" href="" rowNumber="${nextCount}" title="Xóa khỏi đơn hàng"></a></div>		
				</div>
				<div class="col-3">
					<img src="${mainImagePath}" alt="product image" class="img-fluid" />
				</div>
			</div>
			
			<div class="row m-2">
				<b>${productName}</b>
			</div>
			
			<div class="row m-2">
				<div class="col-10">
					<table>
						<tr>
							<td>Giá nhập:</td>
							<td>
								<input type="text" required class="form-control m-1 cost-input"
									name="productDetailCost"
									rowNumber="${nextCount}" 
									value="${productCost}" style="max-width: 150px" readonly/>
							</td>
						</tr>
						<tr>
							<td>Đơn giá bán:</td>
							<td>
								<input type="text" required class="form-control m-1 price-input"
									name="productPrice"
									id="${priceId}"
									rowNumber="${nextCount}" 
									value="${productPrice}" style="max-width: 150px" readonly/>
							</td>
						</tr>
						<tr>
							<td>Số lượng:</td>
							<td>
								<input type="number" step="1" min="1" max="5" class="form-control m-1 quantity-input"
									name="quantity"
									id="${quantityId}"
									rowNumber="${nextCount}" 
									value="1" style="max-width: 150px"/>
							</td>
						</tr>
						<tr>
							<td>Phí vận chuyển:</td>
							<td>
								<input type="text" required class="form-control m-1 ship-input" 
									name="productShipCost"
									value="0.0" style="max-width: 150px"/>
							</td>
						</tr>
						<tr>
							<td>Tổng tiền:</td>
							<td>
								<input type="text" readonly="readonly" class="form-control m-1 subtotal-output"
									name="productSubtotal"
									id="${subtotalId}" 
									value="${productPrice}" style="max-width: 150px" readonly/>
							</td>
						</tr>				
					</table>
				</div>
			</div>
			
		</div>
		<div id="${blankLineId}"class="row">&nbsp;</div>	
	`;

	return htmlCode;
}

function isProductAlreadyAdded(productId) {
	productExists = false;

	$(".hiddenProductId").each(function(e) {
		aProductId = $(this).val();

		if (aProductId == productId) {
			productExists = true;
			return;
		}
	});

	return productExists;
}