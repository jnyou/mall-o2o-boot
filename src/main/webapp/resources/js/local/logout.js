$(function() {
	$('#log-out').click(function() {
		//清除session
		$.ajax({
			url : "/o2o/local/logout",
			async : false,
			cache : false,
			dataType : 'json',
			type : "post",
			success : function(data) {
				console.log(data);
				if (data.success) {
					var usertype= $('#log-out').attr("usertype");
					//清除后退出
					window.location.href = '/o2o/local/login?usertype=' + usertype;
					return false;
				}
			},
			error:function(data,error){
				alert(error);
			}
		});
	});
});