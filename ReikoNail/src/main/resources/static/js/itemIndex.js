
$(document).ready(function(){
	var shohinCdList = [];
	
	$('#ikkatsuDelete').on('click', function(){
		
	    $('.deleteInput:checked').each(function() {
	        var checkboxId = $(this).closest('td').attr('id');
	        var shohinCd = $('#' + checkboxId).next('td').find('a').attr('href').split('/')[2];
	        
	        shohinCdList.push({shohinCd: shohinCd});
	    });
		var shohinCdJsonData = JSON.stringify(shohinCdList); 
		
		
		$.ajax({
			
			url: '/IkkatsuDeleteShohin',
			type: 'POST',
			contentType: 'application/json',
			data: shohinCdJsonData,
			success: function(response){
				if(!response.hasError){
					successShow(response);
				}
			}
		});
	});
	
	function successShow(response){
		var headMessage = response.data;
		var modalMessage = response.message;
		$('#staticBackdropLabel').text(headMessage);
		$('#modal').html(modalMessage);
		$('#staticBackdrop').modal('show');
	}
    $('#reloadButton').on('click', function () {
    	$('#loading-ring').show();
    	// 3秒後にリングを非表示
    	location.reload(true);
    	setTimeout(function () {
        	$('#loading-ring').fadeOut();
    	}, 2000);
    });
    
	// チェックボックスが変更されたときのイベントを親要素に対して設定
	$(document).on('change', '.deleteInput', function() {
	    if ($('.deleteInput:checked').length > 0) {
	        $('#ikkatsuDelete').prop('disabled', false);
	    } else {
	        $('#ikkatsuDelete').prop('disabled', true);
	    }
	});
    
    
});

$(window).on('load', function () {
    $('#loading-ring').hide();
});