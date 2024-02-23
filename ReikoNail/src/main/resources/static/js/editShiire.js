
$(document).ready(function () {
	$('input[type=date]').on('change', function() {
		$(this).css('color', $(this).val() ? 'black' : 'white');
	});        
	var selectedKeisanMode = '1';
	$('#zeinukiMode, #zeikomiMode').on('change', function () {
		selectedKeisanMode = $(this).val();
		console.log(selectedKeisanMode);
		if(selectedKeisanMode === '0'){
			$('#zeinukiMode').prop('checked', true);
			$('#zeikomiMode').prop('checked', false);
			$('#zeikomiGaku').prop('readonly', true);
			$('#zeinukiGaku').prop('readonly', false);
		} else {
			$('#zeinukiMode').prop('checked', false);
			$('#zeikomiMode').prop('checked', true);
			$('#zeikomiGaku').prop('readonly', false);
			$('#zeinukiGaku').prop('readonly', true);
		}
	});
	$('#keisan').on('click', function(){
		if(selectedKeisanMode === '1'){
		  var zeikomiGaku = $('#zeikomiGaku').val();
		  var zeinukiGaku = Math.round(zeikomiGaku / 1.1);
		  $('#zeiGaku').val(zeikomiGaku - zeinukiGaku);
		  $('#zeinukiGaku').val(zeinukiGaku);
		} else {
			var zeinukiGaku = $('#zeinukiGaku').val();
			var zeikomiGaku = Math.floor(zeinukiGaku * 1.1);
			$('#zeikomiGaku').val(zeikomiGaku);
			$('#zeiGaku').val(zeikomiGaku - zeinukiGaku);
		}
	});
			
	$('#daiBunruiName').on('change', function(){
		var selectDaiBunruiName = $(this).val();
		if(selectDaiBunruiName === ''){
			$('#shoBunruiName').prop('disabled', true);
			$('#shoBunruiName').val('');
			return;
		}
		$.ajax({
			url: '/GetShoBunrui',
			type: 'GET',
			data: {
				daiBunruiName : selectDaiBunruiName,
				searchKbn: '3'
				},
			success: function(data){
				updateShoBunruiOptions(data);
			},error: function(){
						
			}
		});
	});
			
	function updateShoBunruiOptions(shoBunruiList) {
		var selectBox = $('#shoBunruiName');
		selectBox.empty();
		if(shoBunruiList.length > 0){
			$('#shoBunruiName').prop('disabled', false);
		}
		for (var i = 0; i < shoBunruiList.length; i++) {
			var option = $('<option>').val(shoBunruiList[i].bunruiName).text(shoBunruiList[i].bunruiName);
			selectBox.append(option);
		}
	}
	
	$('#update').on('click', function(){
		var editShiireDto = {
			shiireId: $('#shiireId').val(),
			shiireDate: $('#shiireDate').val(),
			daiBunruiName: $('#daiBunruiName').val(),
			shoBunruiName: $('#shoBunruiName').val(),
			itemName: $('#itemName').val(),
			shiireSaki: $('#shiireSaki').val(),
			shiireMaker: $('#shiireMaker').val(),
			itemSize: $('#itemSize').val(),
			janCd: $('#janCd').val(),
			zeinukiGaku: $('#zeinukiGaku').val(),
			zeiGaku: $('#zeiGaku').val(),
			zeikomiGaku: $('#zeikomiGaku').val(),
			suryo: $('#suryo').val(),
			rirekiId: $('#rirekiId').val()
		}
		console.log(editShiireDto);
		$.ajax({
			url: '/Valide',
			type: 'GET',
			data: editShiireDto,
			success: function(response){
				if (response.hasError) {
					errorPrint(response.data, response.message)
				} else {
					$('#shiireForm').submit();
				}
			}
		});
	});
	
	function errorPrint(data, message){
		var headMessage = message;
		var modalMessage = '';
		for (var i = 0; i < data.length; i++) {
			modalMessage +=  data[i] + '<br>';
		}
		$('#staticBackdropLabel').text(headMessage);
		$('#modal').html(modalMessage);
		$('#staticBackdrop').modal('show');
	}
	
	$('#deleteBtn').on('click', function(){
		var rirekiId = $('#rirekiId').val();
		$.ajax({
			url: "/DeleteShiire",
			type: 'POST',
			data: {rirekiId: rirekiId},
			success: function(response){
				if (response.hasError) {
					errorPrint(response.data, response.message);
				} else {
					window.location.href = '/Kanryo';
				}
			}
		});
	});
	// 数値以外の入力制御および全角数値の制御
	$('#zeikomiGaku').on('input', function() {
		let inputValue = $(this).val().replace(/[^0-9０-９]/g, '');
		inputValue = inputValue.replace(/[０-９]/g, function(s) {
			return String.fromCharCode(s.charCodeAt(0) - 0xFEE0);
		});
		$(this).val(inputValue);
	});         
});