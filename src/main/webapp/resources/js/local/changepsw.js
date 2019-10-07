$(function() {
	var url = '/o2o/local/changelocalauthpwd';
	var usertype = getQueryString("usertype");
	$('#submit').click(function() {
		var username = $('#username').val();
		var password = $('#password').val();
		var newPassword = $('#newPassword').val();
		//新密码的确认密码
		var confirmPassword = $('#confirmPassword').val();
		if(newPassword != confirmPassword){
			$.toast('两次输入的密码不一致！');
			return;
		}
		//提交表单数据
		var formData = new FormData();
		formData.append('username', username);
		formData.append('password', password);
		formData.append('newPassword', newPassword);
		var verifyCodeActual = $('#j_captcha').val();
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		formData.append("verifyCodeActual", verifyCodeActual);
		$.ajax({
			url : url,
			type : 'POST',
			data : formData,
			contentType : false,
			processData : false,
			cache : false,
			success : function(data) {
				if (data.success) {
					$.toast('提交成功！');
					if(usertype == 1){
						window.location.href = '/o2o/frontend/index';
					}else{
						window.location.href = '/o2o/shopadmin/shoplist';
					}
				} else {
					$.toast('提交失败！');
					$('#captcha_img').click();
				}
			}
		});
	});

	$('#back').click(function() {
		if(usertype == 1){
			window.location.href = '/o2o/frontend/index';
		}else{
			window.location.href = '/o2o/shopadmin/shoplist';
		}
	});
});
