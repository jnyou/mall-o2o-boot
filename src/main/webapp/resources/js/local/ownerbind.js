$(function() {
	var bindUrl = '/o2o/local/bindlocalauth';

	var usertype = getQueryString("usertype");
	$('#submit').click(function() {
		var username = $('#username').val();
		var password = $('#psw').val();
		var verifyCodeActual = $('#j_captcha').val();
		var needVerify = false;
		if (!verifyCodeActual) {
			$.toast('请输入验证码！');
			return;
		}
		$.ajax({
			url : bindUrl,
			async : false,
			cache : false,
			type : "post",
			dataType : 'json',
			data : {
				username : username,
				password : password,
				verifyCodeActual : verifyCodeActual
			},
			success : function(data) {
				if (data.success) {
					$.toast('绑定成功！');
					if(usertype == 1){
						//前端首页
						window.location.href = '/o2o/frontend/index';
					}else{
						//后台管理
						window.location.href = '/o2o/shopadmin/shoplist';
					}
				} else {
					$.toast('绑定失败！' + data.errMsg);
					$('#captcha_img').click();
				}
			}
		});
	});
});