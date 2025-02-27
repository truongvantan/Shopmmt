var extraImagesCount = 0;

$(document).ready(function() {
	$("input[name='extraImage']").each(function(index) {
		extraImagesCount++;

		$(this).change(function() {
			showExtraImageThumbnail(this, index);
		});
	});
	
	$("a[name='linkRemoveExtraImage']").each(function(index) {
		$(this).click(function() {
			removeExtraImage(index);
		});
	});
});

function showExtraImageThumbnail(fileInput, index) {
	const file = fileInput.files[0];
	
	fileName = file.name;
	
	imageNameHiddenField = $("#imageName" + index);
	if (imageNameHiddenField.length) {
		imageNameHiddenField.val(fileName);
	}
	
	const reader = new FileReader();
	reader.onload = function(e) {
		$("#extraThumbnail" + index).attr("src", e.target.result);
	};

	reader.readAsDataURL(file);

	if (index >= extraImagesCount - 1) {
		addNextExtraImageSection(index + 1);
	}
}

function addNextExtraImageSection(index) {
	htmlExtraImage = `
		<div class="col border m-3 p-2" id="divExtraImage${index}">
			<div id="extraImageHeader${index}">
				<label>Hình ảnh phụ ${index + 1}:</label>
			</div>
			<div class="m-2">
				<img
					id="extraThumbnail${index}"
					alt="Hình ảnh phụ ${index + 1}"
					src="${defaultImageThumbnailSrc}"
					width="200px"
					height="200px"
				/>
			</div>
			<div>
				<input
					type="file"
					name="extraImage"
					accept="image/png, image/jpeg"
					onchange="showExtraImageThumbnail(this, ${index})" 
				/>
			</div>
		</div>
	`;
	
	htmlLinkRemove = `
		<a class="btn fas fa-times-circle fa-xl icon-red float-right"
			href="javascript:removeExtraImage(${index - 1})" 
			title="Xóa ảnh này"></a>
	`;
	
	$("#divProductImages").append(htmlExtraImage);
	
	$("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);
	
	extraImagesCount++;
}

function removeExtraImage(index) {
	$("#divExtraImage" + index).remove();
}
