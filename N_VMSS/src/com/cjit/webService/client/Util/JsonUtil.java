package com.cjit.webService.client.Util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cjit.common.util.UUID;
import com.cjit.webService.client.entity.ApplicationForm;
import com.cjit.webService.client.entity.BillEntity;
import com.cjit.webService.client.entity.Cover;
import com.cjit.webService.client.entity.Customer;
import com.cjit.webService.client.entity.HeXinCallBack;
import com.cjit.webService.client.entity.Policy;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * 报文工具类
 * 
 * @author jxjin
 *
 */
public class JsonUtil {
	/**
	 * 申请开票数据
	 */
	public static String applictionFormJson(ApplicationForm applicationForm) {
		JSONObject headJson = new JSONObject();
		headJson.put("REQUESTTYPE", applicationForm.getRequestionType());
		headJson.put("REQSERIALNO", applicationForm.getReqserialNo());
		headJson.put("FLOWINTIME", applicationForm.getFlowinTime());

		JSONObject bussInfoJson = new JSONObject();
		bussInfoJson.put("TTMPRCNO", StringUtil.valueOf(applicationForm.getTtmprcNo()));
		bussInfoJson.put("CHERNUM", StringUtil.valueOf(applicationForm.getChernum()));
		bussInfoJson.put("TRANS_DATE_START", StringUtil.valueOf(applicationForm.getTransDateStart()));
		bussInfoJson.put("TRANS_DATE_END", StringUtil.valueOf(applicationForm.getTransDateEnd()));
		bussInfoJson.put("CUSTOMER_NAME", StringUtil.valueOf(applicationForm.getCustomerName()));
		bussInfoJson.put("ISYK", StringUtil.valueOf(applicationForm.getIsYK()));
		bussInfoJson.put("BATCH_NO", StringUtil.valueOf(applicationForm.getBatchNo()));
		bussInfoJson.put("BATCH_TYPE", StringUtil.valueOf(applicationForm.getBatchType()));

		JSONObject bussListJson = new JSONObject();
		bussListJson.put("BUSSINFO", bussInfoJson);

		JSONObject requestJson = new JSONObject();
		requestJson.put("BUSSLIST", bussListJson);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("HEADER", headJson);
		jsonObject.put("REQUEST", requestJson);

		return jsonObject.toString();
	}
	/**
	 * 数据同步失败报文
	 * @param list
	 * @param message
	 * @param result
	 * @return
	 */
	public static String synchFailJson(List<String> list, String message, boolean result) {
		JSONObject bussListJson = new JSONObject();
		JSONArray bussInfoArray=new JSONArray();
		for (String str : list) {
			JSONObject bussInfoJson = new JSONObject();
			bussInfoJson.put("BUSINESS_ID", StringUtil.valueOf(str));
			bussInfoArray.add(bussInfoJson);
		}
		bussListJson.put("BUSSINFO", bussInfoArray);
		JSONObject requestJson = new JSONObject();
		requestJson.put("BUSSLIST", bussListJson);

		JSONObject headerJson = new JSONObject();
		headerJson.put("REQUESTTYPE", "0003");
		headerJson.put("RESULT", result);
		headerJson.put("MESSAGE", message);
		headerJson.put("REQSERIALNO", "");
		headerJson.put("FLOWINTIME", "");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("HEADER", headerJson);
		jsonObject.put("REQUEST", requestJson);

		return jsonObject.toString();
	}
	
	/**
	 * 发票开具，作废，红冲回写核心
	 * 
	 * @return
	 */
	public static String billIssueJson(List<BillEntity> list, String message, boolean result) {
		JSONObject bussListJson = new JSONObject();
		JSONArray bussInfoArray=new JSONArray();
		for (BillEntity billEntity : list) {
			JSONObject bussInfoJson = new JSONObject();
			bussInfoJson.put("BUSINESS_ID", StringUtil.valueOf(billEntity.getBusinessId()));
			bussInfoJson.put("BILL_CODE", StringUtil.valueOf(billEntity.getBillCode()));
			bussInfoJson.put("BILL_NO", StringUtil.valueOf(billEntity.getBillNo()));
			bussInfoJson.put("FAPIAO_TYPE", StringUtil.valueOf(billEntity.getFapiaoType()));
			bussInfoJson.put("DATA_STATUS", StringUtil.valueOf(billEntity.getDataStatus()));
			bussInfoJson.put("AMT", StringUtil.valueOf(billEntity.getAmt()));
			bussInfoJson.put("TAX_AMT", StringUtil.valueOf(billEntity.getTaxAmt()));
			bussInfoJson.put("CHERNUM", StringUtil.valueOf(billEntity.getChernum()));
			bussInfoJson.put("TTMPRCNO", StringUtil.valueOf(billEntity.getTtmprcno()));
			bussInfoJson.put("CUSTOMER_NAME", StringUtil.valueOf(billEntity.getCustomerName()));
			bussInfoArray.add(bussInfoJson);
		}
		bussListJson.put("BUSSINFO", bussInfoArray);
		JSONObject requestJson = new JSONObject();
		requestJson.put("BUSSLIST", bussListJson);

		JSONObject headerJson = new JSONObject();
		headerJson.put("REQUESTTYPE", "0003");
		headerJson.put("RESULT", result);
		headerJson.put("MESSAGE", message);
		headerJson.put("REQSERIALNO", "");
		headerJson.put("FLOWINTIME", "");

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("HEADER", headerJson);
		jsonObject.put("REQUEST", requestJson);

		return jsonObject.toString();
	}

	/**
	 * 将xml转为保单实体对象
	 * 
	 * @param jsonString
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Policy> formatJsonToPolicy(String jsonString, String isYK) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject = JSONObject.fromObject(jsonString);
			jsonObject = jsonObject.getJSONObject("RESPONSE");
			JSONArray jsonArray = jsonObject.getJSONArray("BUSSLIST");
			if (jsonArray == null || jsonArray.size() == 0||jsonArray.toString().equals("[{}]")) {
				return null;
			}
			List<Policy> policies = new ArrayList<Policy>();
			Policy policy = new Policy();
			Iterator<JSONObject> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				policy.setIsYK(isYK);
				JSONObject json = iterator.next().getJSONObject("BUSSINFO");
				policy.setBusinessId(json.getString("BUSINESS_ID"));
				policy.setInstId(json.getString("INST_ID"));
				policy.setQdFlag(json.getString("QD_FLAG"));
				policy.setChernum(json.getString("CHERNUM"));
				policy.setRepnum(json.getString("REPNUM"));
				policy.setTtmprcno(json.getString("TTMPRCNO"));
				policy.setBatchNo(json.getString("BATCH_NO"));
				policy.setBatchType(json.getString("BATCH_TYPE"));
				Customer customer = new Customer();
				customer.setCustomerNo(json.getString("CUSTOMER_NO"));
				customer.setCustomerName(json.getString("CUSTOMER_NAME"));
				customer.setCustomerTaxno(json.getString("CUSTOMER_TAXNO"));
				customer.setCustomerAddressand(json.getString("CUSTOMER_ADDRESSAND"));
				customer.setTaxpayerType(json.getString("TAXPAYER_TYPE"));
				customer.setCustomerPhone(json.getString("CUSTOMER_PHONE"));
				customer.setCustomerBankand(json.getString("CUSTOMER_BANKAND"));
				customer.setCustomerAccount(json.getString("CUSTOMER_ACCOUNT"));
				customer.setCustomerFapiaoFlag("M");
				customer.setDataSource("2");
				customer.setFapiaoType(json.getString("INVTYP"));
				policy.setCustomer(customer);
				policy.setOrigcurr(json.getString("ORIGCURR"));
				policy.setOrigamt(new BigDecimal(json.getString("ORIGAMT")));
				policy.setAcctamt(new BigDecimal(json.getString("ACCTAMT")));
				policy.setTrdt(json.getString("TRDT"));
				policy.setBatctrcde(json.getString("BATCTRCDE"));
				policy.setInvtyp(json.getString("INVTYP"));
				policy.setFeetyp(json.getString("FEETYP"));
				policy.setBillfreq(json.getString("BILLFREQ"));
				policy.setPolyear(json.getString("POLYEAR"));
				policy.setHissdte(json.getString("HISSDTE"));
				policy.setPlanlongdesc(json.getString("PLANLONGDESC"));
				policy.setInstfrom(json.getString("INSTFROM"));
				policy.setInstto(json.getString("INSTTO"));
				policy.setOccdate(json.getString("OCCDATE"));
				policy.setPremterm(json.getString("PREMTERM"));

				JSONArray covJsonArray = json.getJSONArray("COVLIST");
				Iterator<JSONObject> it = covJsonArray.iterator();
				while (it.hasNext()) {
					JSONObject coverJSONObject = it.next();
					Cover cover = new Cover();
					//每个险种存储为一条交易信息，该信息有一个唯一的编号 TRANS_ID
					policy.setTransId(UUID.randomUUID().toString().replace("-", ""));
					cover.setTranstype(coverJSONObject.getString("TRANSTYPE"));
					cover.setInsCod(coverJSONObject.getString("INS_COD"));
					cover.setInsNam(coverJSONObject.getString("INS_NAM"));
					cover.setAmtCny(new BigDecimal(coverJSONObject.getString("FEETYP")));
					cover.setTaxAmtCny(new BigDecimal(coverJSONObject.getString("AMT_CNY")));
					cover.setIncomeCny(new BigDecimal(coverJSONObject.getString("TAX_AMT_CNY")));
					cover.setTaxRate(Double.valueOf(coverJSONObject.getString("INCOME_CNY")));
					policy.setHesitatePeriod(coverJSONObject.getString("HESITATEPERIOD"));
					Policy policyEntity = (Policy) policy.clone();
					policyEntity.setCover(cover);
					policies.add(policyEntity);
				}
			}
			return policies;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 核心回写反馈解析
	 * 
	 * @param jsonString
	 * @return
	 */
	public static HeXinCallBack callBacks(String jsonString) {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject = JSONObject.fromObject(jsonString);
			jsonObject = jsonObject.getJSONObject("RESPONSE").getJSONObject("BUSSLIST").getJSONObject("BUSSINFO");
			String resulttype = jsonObject.getString("RESULTTYPE");
			String errorinfo = jsonObject.getString("ERRORINFO");
			HeXinCallBack back = new HeXinCallBack();
			back.setErrorInfo(errorinfo);
			back.setResulttype(resulttype);
			return back;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * 发票开具，作废，红冲回写核心 组装 xml cheng 2018/08/23
	 * 
	 * @return
	 */
	public static String billIssueXml(List<BillEntity> list, String message,
			boolean result) {
		// 组装报文
		org.dom4j.Document doc = DocumentHelper.createDocument();
		doc.setXMLEncoding("GBK");
		org.dom4j.Element root = doc.addElement("TranData");
		org.dom4j.Element header = root.addElement("Head");
		org.dom4j.Element body = root.addElement("Body");

		// 交易流水号，必填 发票请求流水号
		String uuId = java.util.UUID.randomUUID().toString();
		header.addElement("TranNo").addText(uuId);
		
		//System.out.println("***获取日期********************");
		String time ="";
		System.out.println(" list.size()********************   "+ list.size());
		if (null != list && list.size() > 0){
			 time= list.get(0).getTransDate();
		}
		//System.out.println("***获取日期 SUCCESS********************");
		//  长度为19时 时间格式为   yyyy-MM-dd HH:mm:ss
		/*if (time != null && !time.equals("")&&time.length()==19) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date date = sdf.parse(time);
					System.out.println("年月日：  "+sdf1.format(date));
					System.out.println("时分秒：   "+sdf2.format(date));
					String format1 = sdf1.format(date);
					String format2 = sdf2.format(date);
					// 交易日期（YYYY-MM-DD），必填
					header.addElement("TranDate").addText(format1);
					// 交易时间（HH:MM:SS），必填
				     header.addElement("TranTime").addText(format2);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		}else *///长度为10 时格式为 yyyy-MM-dd
		 if (time != null && !time.equals("")&&time.length()==10) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						Date date = sdf.parse(time);
						
						System.out.println("年月日：  "+sdf.format(date));
						String format1 = sdf.format(date);
						// 交易日期（YYYY-MM-DD），必填
						header.addElement("TranDate").addText(format1);
						// 交易时间（HH:MM:SS），必填
					     header.addElement("TranTime").addText("");
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}else{
				 
				// 交易日期（YYYY-MM-DD），必填
				header.addElement("TranDate").addText("");
				// 交易时间（HH:MM:SS），必填
			     header.addElement("TranTime").addText("");
				
			}		
			// ClientIp，必填 (请求IP地址)
		 String IP;
			try {
				IP = InetAddress.getLocalHost().getHostAddress();
				header.addElement("ClientIp").addText(IP);
				//System.out.println(IP);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		// 交易编号，必填  暂定  003-开票
		String dataStatus = "";
		if (null != list && list.size() > 0){
			dataStatus= list.get(0).getDataStatus();
		}
		//交易状态 
		header.addElement("FuncFlag").addText("0003");   //TODO
		// 交易来源，固定值，必填  
		header.addElement("Source").addText("VMS");  //TODO
		/*
		 * //交易子来源， 暂时为空 TODO
		 */
		 header.addElement("SubSource").addText("");
		 org.dom4j.Element billList = body.addElement("BillList");
			for (BillEntity billEntity : list) {
				
				org.dom4j.Element bussInfo = billList.addElement("BillInfo");

				// 交易流水号
				bussInfo.addElement("BUSINESS_ID").addText(billEntity.getBusinessId());
				/***********************发票代码处理******************************************************/
				if(billEntity.getDataStatus().equals("5") || billEntity.getDataStatus().equals("15")){ //TODO  cheng
					// 蓝票代码
					bussInfo.addElement("BILL_CODE").addText(billEntity.getBillCode());
					// 蓝票号码
					bussInfo.addElement("BILL_NO").addText(billEntity.getBillNo());
					//RED_BILL_CODE  红色发票代码 
					bussInfo.addElement("RED_BILL_CODE").addText("");
					//RED_BILL_N0	红色发票号码
					bussInfo.addElement("RED_BILL_N0").addText("");
				}
				if(billEntity.getDataStatus().equals("22") || billEntity.getDataStatus().equals("23")){
					// 蓝票代码
					bussInfo.addElement("BILL_CODE").addText(billEntity.getOriBillCode());
					// 蓝票号码
					bussInfo.addElement("BILL_NO").addText(billEntity.getOriBillNo());
					//RED_BILL_CODE  红色发票代码 
					bussInfo.addElement("RED_BILL_CODE").addText(billEntity.getBillCode());
					//RED_BILL_N0	红色发票号码
					bussInfo.addElement("RED_BILL_N0").addText(billEntity.getBillNo());
				}
				
				/*****************************************************************************/
				// 发票类型 0->专票 1->普票 0->增值税专用发票 2->电子发票 3->增值税专用红票 4->增值税普通红票
				bussInfo.addElement("FAPIAO_TYPE").addText(billEntity.getFapiaoType());
				// 交易状态 5-发票开具，15-作废，22-全部红冲开票，23-部分红冲）
				bussInfo.addElement("DATA_STATUS").addText(billEntity.getDataStatus());
				// 开票金额
				bussInfo.addElement("AMT").addText(billEntity.getAmt());
				// 开票税额
				bussInfo.addElement("TAX_AMT").addText(billEntity.getTaxAmt());
				// 是否部分开票 0-部分开票，1-全额开票
				//判断是否为全额开票
				int a =billEntity.getBalance().compareTo(BigDecimal.ZERO);
				String dataFlag ="1";
		        if( a>0){
		        	dataFlag = "0";
		        }else{
		        	dataFlag = "1"; //TODO 当开负数票时全额开票，不必考虑balance为负数情况
		        }
				bussInfo.addElement("DATA_FLAG").addText(dataFlag);
				// 保单号
				bussInfo.addElement("ContNo").addText(billEntity.getChernum());
				// 保全受理号 TODO (旧保单号)
				bussInfo.addElement("EdorAcceptNo").addText(billEntity.getRepNum());
				// 投保单号
				bussInfo.addElement("PrtNo").addText(billEntity.getTtmprcno());
				// 费用类型
				bussInfo.addElement("FeeType").addText(billEntity.getFeetype());
				// 主险名称
				bussInfo.addElement("MainRiskName").addText(billEntity.getPlanlongdesc());
				// 客户编码
				bussInfo.addElement("CustomerNo").addText(billEntity.getCustomerId());
				// 客户名称
				bussInfo.addElement("CustomerName").addText(billEntity.getCustomerName());
				//团个标志
				System.err.println("团个标志"+  billEntity.getGdFlag());
				bussInfo.addElement("QDFLAG").addText(billEntity.getGdFlag());
				
			}

			return doc.asXML();
		}
	/**
	 * cheng 新增  解析xml报文 2018/08/22
	 * @param returnXml
	 * @return
	 */
		public static HeXinCallBack analyzeXML(String returnXml) {

			System.out.println("回传XML:" + returnXml);
			Document document = null;

			Element head = null;
			Element body = null;

			// 解析 xml
			try {
				document = DocumentHelper.parseText(returnXml);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Element root = document.getRootElement();
			// 获取Header元素
			head = root.element("Head");
			// 获取Request元素
			body = root.element("Body");
			//暂时先解析有用的数据   状态  和
			/*
			 * <!—返回标志 <Flag>******</Flag>
			 */
			//String flag = head.element("Flag").getText();
			/*
			 * <!—返回信息 <Desc>******</Desc>
			 */
			String desc = head.element("Desc").getText();
			/*
			 * <!—交易来源 <Source>******</Source>
			 */
			//String source = head.element("Source").getText();
			/*
			 * <!—交易子来源 <SubSource>******</SubSource>  无用数据，暂不解析
			 */
			/*String SubSource = head.element("SubSource").getText();*/
			 if(desc.equals("交易成功！")){
	            	// 解析body 
	    			/* RESULTTYPE 状态 */
	    			String RESULTTYPE = body.element("RESULTTYPE").getText();
	    			// ERRORINFO 失败原因
	    			String ERRORINFO = body.element("ERRORINFO").getText();
	    			HeXinCallBack back = new HeXinCallBack();
	    			back.setErrorInfo(ERRORINFO);
	    			back.setResulttype(RESULTTYPE);
	    			System.out.println("回传核心成功");
	    			return back;
	            }else{
	            	 ///* RESULTTYPE 状态 */
	            	String RESULTTYPE = "1";
	    			// ERRORINFO 失败原因
	            	System.out.println("回传核心失败");
	    			String ERRORINFO = "回传核心失败";
	    			HeXinCallBack back = new HeXinCallBack();
	    			back.setErrorInfo(ERRORINFO);
	    			back.setResulttype(RESULTTYPE);
	    			return back;
	            }
	         
		}
}
