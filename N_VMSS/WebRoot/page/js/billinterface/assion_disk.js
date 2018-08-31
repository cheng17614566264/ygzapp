BillInterface = function() {
	var billInterfaceAjax = new BillInterfaceAjax();
	var url = '';
	var doPost = function(params, callback) {
		$.post(url, params, function(result) {
			callback(result);
		}, 'json');
	};
	/**
	 *  初始化参数
	 */
	this.init = function(params) {
		url = params.url;
		billInterfaceAjax.init(params);
	};
	/**
	 *  创建发票开具报文
	 */
	this.createBillissue = function(params) {
		billInterfaceAjax.createBillissue(params, function(ajaxReturn) {
			if (ajaxReturn.isNormal) {
				doPost({SID: '1', SIDPARAM: ajaxReturn.message}, function(result) {
					
				});
			} else {
				alert(ajaxReturn.message);
			}
		});
	};
}