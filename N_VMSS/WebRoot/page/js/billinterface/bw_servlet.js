BillInterface = function() {
	var billInterfaceAjax = new BillInterfaceAjax();
	var ocxObj = null;
	var taxInfoCheckOk = false;
	var diskNo = '';
	var MachineNo = '';
	/**
	 * 初始化参数
	 */
	this.init = function(params) {
		ocxObj = document.getElementById('ocxObj');
		billInterfaceAjax.init(params);
		billInterfaceAjax.createTaxInfo(
						params,
						function(ajaxReturn) {
							alert(ajaxReturn.isNormal+",tttt");//true
							if (ajaxReturn.isNormal) {
								alert(ajaxReturn.attributes.StringXml+",访问控件前");
								var StringXml = ocxObj.Operate(ajaxReturn.attributes.StringXml); //访问控件，检测是否插入税控钥匙
								alert(StringXml+",访问控件后");
								alert("进入校验...");
								billInterfaceAjax.checkTaxInfo(
												{
													StringXml : StringXml
												},
												function(ajaxReturn1) {
													if (true) {
														alert("进入创建注册");
													//if (ajaxReturn1.isNormal) { //false
														//diskNo = ajaxReturn1.attributes.diskNo;
														diskNo = "45010319740715104701";
														//MachineNo = ajaxReturn1.attributes.diskNo;
														MachineNo = "45010319740715104701";
														billInterfaceAjax.createRegistInfo(
																		{
																			diskNo : diskNo
																		},
																		function(ajaxReturn2) {
																			if (ajaxReturn2.isNormal) {
																				alert(ajaxReturn2.attributes.StringXml+",访问控件前2");
																				var StringXml = ocxObj
																						.Operate(ajaxReturn2.attributes.StringXml);
																				alert(StringXml+",访问控件后2");
																				billInterfaceAjax
																						.checkRegistInfo(
																								{
																									StringXml : StringXml
																								},
																								function(ajaxReturn3) {
																									if (ajaxReturn3.isNormal) {
																										var StringXml = ocxObj
																												.Operate(ajaxReturn3.attributes.StringXml);
																									} else {
																										alert(ajaxReturn3.message+",第三次");
																									}
																								});
																			} else {
																				alert(ajaxReturn2.message+",第二次");
																			}
																		});
													} else {
														alert(ajaxReturn1.message+",第一次");
													}
												});
							} else {
								alert(ajaxReturn.message);
							}
						});
	
	};

	/**
	 * 创建发票开具报文
	 */
	this.createBillissue = function(params) {
		var ids = new Array();
		ids = params.ids.split(",");
		alert(ids.length);//1
		var flag = true;
		for (var i = 0; i < ids.length; i++) {
			var billId = ids[i];
			billInterfaceAjax
					.createCurBillNoInfo(
							{
								fapiaoType : params.fapiaoType,
								//2018-03-07国富更改
								//diskNo : diskNo 
								billId : billId
							},
							function(ajaxReturn) {
								alert("cheng1")
								if (ajaxReturn.isNormal) {
									var massage=ajaxReturn.attributes.massage;
									if(massage!=null){
										window.confirm(massage);
									}
									/*var StringXml = ocxObj
											.Operate(ajaxReturn.attributes.StringXml);*/
									/*billInterfaceAjax
											.checkCurBillNoInfo(
													{
														StringXml : StringXml
													},
													function(ajaxReturn1) {
														if (ajaxReturn1.isNormal) {*/
															billInterfaceAjax
																	.createBillissue(
																			{
																				billId : billId
																				//fapiaoType : params.fapiaoType,
																				//diskNo : diskNo,
																				//MachineNo : MachineNo
																			},
																			function(ajaxReturn2) {
																				alert(123);
																				if (ajaxReturn2.isNormal) {
																					/*var StringXml = ocxObj
																							.Operate(ajaxReturn2.attributes.StringXml);*/ 
																					
																					//向税控发送组建好的报文(利用控件传输)
																					alert(123);
																					var SKCom = new ActiveXObject("SKServiceAPICOM.SkService");
																					alert(1234);
																				
																					//报文内容
																					var StringXml = ajaxReturn2.attributes.StringXml;
																					
																					//服务器ip地址和端口
																					var sIp = ajaxReturn2.attributes.sIp;
																					
																					var sPort = ajaxReturn2.attributes.sPort;
																			
																					var sIp2 = sIp +":"+ sPort;
																					alert(12345);
																					//税控返回信息
																					var sRet = SKCom.SkService(StringXml,sIp2);
																					alert(123456);
																				
																					alert(sRet+",税控返回信息...");
																					billInterfaceAjax.updateBillIssueResult(
																							{
																								//diskNo : diskNo,
																								//MachineNo : MachineNo,
																								//新增  cheng flag1 
																								flag1 :'lanpiao',
																								billId : billId,
																								StringXml : sRet
																							},
																							function(ajaxReturn3) {
																							
																								if (!ajaxReturn3.isNormal) {
																									alert(ajaxReturn3.message);
																									flag = false;
																								} else {
																				
																									billInterfaceAjax
																											.invoiceIssueAccessCore(
																													{
																														billId : billId
																													},
																													function(ajaxReturn4) {
																														if (!ajaxReturn4.isNormal) {
																															alert("发票开具与核心交互失败"
																																	+ "，回写核心失败原因："
																																	+ ajaxReturn4.message);
																														}else{
																															alert("发票开具与核心交互成功");
																														}
																													});
																								}
																							}
																							
																					);
																				} else {
																					alert(ajaxReturn2.message);
																					flag = false;
																				}
																			});
														/*} else {
															alert(ajaxReturn1.message);
															flag = false;
														}
													});*/
								} else {
									alert(ajaxReturn.message);
									flag = false;
								}
							});
			if (!flag) {
				break;
			}
		}
		if (flag) {
			alert("开具成功!");
		}
	};
	//测试 与核心交互 程 
	this.createBill2 = function(params) {
		var ids = new Array();
		ids = params.ids.split(",");

		var flag = true;
		for (var i = 0; i < ids.length; i++) {
			var billId = ids[i];
			billInterfaceAjax.invoiceIssueAccessCore({
				billId : billId
			}, function(ajaxReturn4) {
				if (!ajaxReturn4.isNormal) {
					alert("发票开具与核心交互失败" + "，回写核心失败原因：" + ajaxReturn4.message);
				} else {
					alert("发票" + (i + 1) + "发送成功!");
				}
			});
		}
		alert("发票开具成功！");
	}

	/**
	 * 创建打印 报文
	 */

	this.createBillPrint = function(params) {
		var ids = new Array();
		var ids = params.ids.split(",");
		$.ajaxSetup({
			async : false
		});
		var flag = true;
		for (var i = 0; i < ids.length; i++) {
			var billId = ids[i];
			billInterfaceAjax.createBillPrint({
				billId : billId
				//diskNo : diskNo
			}, function(ajaxReturn) {
				if (ajaxReturn.isNormal) {
					// alert(ajaxReturn.attributes.StringXml);
					//var StringXml = ocxObj.Operate(ajaxReturn.attributes.StringXml);
					// alert(StringXml)
					var SKCom = new ActiveXObject("SKServiceAPICOM.SkService");
					alert(ajaxReturn.attributes.StringXml+",打印报文信息...");
					//报文内容
					var StringXml = ajaxReturn.attributes.StringXml;
					//服务器ip地址和端口
					var sIp = ajaxReturn.attributes.sIp;
					var sPort = ajaxReturn.attributes.sPort;
					var sIp2 = sIp +":"+ sPort;
					//税控返回信息
					var sRet = SKCom.SkService(StringXml,sIp2);
					alert(sRet+",打印返回信息...");
					billInterfaceAjax.updateBillPrintResult({
						billId : billId,
						StringXml : sRet
					}, function(ajaxReturn1) {
						if (!ajaxReturn1.isNormal) {
							alert(ajaxReturn1.message);
							flag = false;
						}
					});
				} else {
					alert(ajaxReturn.message);
					flag = false;
				}
			});
			if (!flag) {
				break;
			}
		}
		if (flag) {
			alert("打印成功!")
		}
	}
	this.createBillCancel = function(params) {
		billInterfaceAjax.createBillCancel({
			billId : params.billId,
			flag : '1',
			//diskNo : diskNo
		}, function(ajaxReturn) {
			if (ajaxReturn.isNormal) {
				var stringXml = ajaxReturn.attributes.StringXml;
				//var resultXml = ocxObj.Operate(stringXml);
				
				var SKCom = new ActiveXObject("SKServiceAPICOM.SkService");
				alert(ajaxReturn.attributes.StringXml+",作废报文信息...");
				//报文内容
				var StringXml = ajaxReturn.attributes.StringXml;
				//服务器ip地址和端口
				var sIp = ajaxReturn.attributes.sIp;
				var sPort = ajaxReturn.attributes.sPort;
				var sIp2 = sIp +":"+ sPort;
				//税控返回信息
				var sRet = SKCom.SkService(StringXml,sIp2);
				alert(sRet+",作废返回信息...");
				
				billInterfaceAjax.updateBillCancelResult({
					StringXml : sRet,
					billId : params.billId
				}, function(ajaxReturn1) {
					if (ajaxReturn1.isNormal) {
						// 与税控交互成功
						
						//invoiceIssueAccessCore   
						//cheng 新增   2018/8/27  调用核心方法 修改
						//billInterfaceAjax.cancelBillAccessCore({
						billInterfaceAjax.invoiceIssueAccessCore({
							billId : params.billId
						}, function(ajaxReturn2) {
							if (ajaxReturn2.isNormal) {
								// 与核心交互成功
								if (ajaxReturn1.message == null
										|| ajaxReturn1.message == "") {
									alert("作废成功");
								} else {
									alert("作废成功" + "，" + ajaxReturn1.message);
								}
							} else {
								alert("作废成功" + "，" + ajaxReturn2.message);
							}
						});
					} else {
						alert(ajaxReturn1.message);
					}
					submitAction(document.forms[0], "listBillCancel.action");
				});
			} else {
				alert(ajaxReturn.message);
			}

		});
	}
	
	//发票红冲待更改 
	this.createRedInvoice = function(params) {
		alert("cheng1");
		var ids = new Array();
		//ids = params.ids.split(",");
		ids = params.billIds.split(","); //2018-06-27 cheng 更改    //2018-08-28 新增 “  .split(",")  ”;
		alert(ids.length+".....");//1
		alert(ids)
		var flag = true;
		for (var i = 0; i < ids.length; i++) {
			var billId = ids[i];
			alert("cheng2");
			billInterfaceAjax
					.createCurBillNoInfo(
							{
								fapiaoType : params.fapiaoType,
								//2018-03-07国富更改
								//diskNo : diskNo 
								billId : billId
							},
							function(ajaxReturn) {
								alert("cheng3");
								if (ajaxReturn.isNormal) {
									var massage=ajaxReturn.attributes.massage;
									if(massage!=null){
										window.confirm(massage);
									}
									/*var StringXml = ocxObj
											.Operate(ajaxReturn.attributes.StringXml);*/
									/*billInterfaceAjax
											.checkCurBillNoInfo(
													{
														StringXml : StringXml
													},
													function(ajaxReturn1) {
														if (ajaxReturn1.isNormal) {*/
															billInterfaceAjax
																	.createBillissue(
																			{
																				billId : billId
																				//fapiaoType : params.fapiaoType,
																				//diskNo : diskNo,
																				//MachineNo : MachineNo
																			},
																			function(ajaxReturn2) {
																				alert("cheng4");
																				if (ajaxReturn2.isNormal) {
																					/*var StringXml = ocxObj
																							.Operate(ajaxReturn2.attributes.StringXml);*/ 
																					
																					//向税控发送组建好的报文(利用控件传输)
																					alert(ajaxReturn2.attributes.StringXml+",开具报文组建完成...");
																					var SKCom = new ActiveXObject("SKServiceAPICOM.SkService");
																					//报文内容
																					var StringXml = ajaxReturn2.attributes.StringXml;
																					//服务器ip地址和端口
																					var sIp = ajaxReturn2.attributes.sIp;
																					var sPort = ajaxReturn2.attributes.sPort;
																					var sIp2 = sIp +":"+ sPort;
																					//税控返回信息
																					var sRet = SKCom.SkService(StringXml,sIp2);
																					alert(sRet+",税控返回信息...");
																					
																					billInterfaceAjax.updateBillIssueResult(
																							{
																								//diskNo : diskNo,
																								//MachineNo : MachineNo,
																								//新增  cheng flag1 
																								flag1 :'hongpiao',
																								billId : billId,
																								StringXml : sRet
																							},
																							function(ajaxReturn3) {
																								if (!ajaxReturn3.isNormal) {
																									alert(ajaxReturn3.message);
																									flag = false;
																								} else {
																									billInterfaceAjax
																											.invoiceIssueAccessCore(
																													{
																														billId : billId
																													},
																													function(ajaxReturn4) {
																														if (!ajaxReturn4.isNormal) {
																															alert("发票开具与核心交互失败"
																																	+ "，回写核心失败原因："
																																	+ ajaxReturn4.message);
																														}
																													});
																								}
																							});
																				} else {
																					alert(ajaxReturn2.message);
																					flag = false;
																				}
																			});
														/*} else {
															alert(ajaxReturn1.message);
															flag = false;
														}
													});*/
								} else {
									alert(ajaxReturn.message);
									flag = false;
								}
							});
			if (!flag) {
				break;
			}
		}
		if (flag) {
			alert("开具成功!");
		}
	};
	
}

