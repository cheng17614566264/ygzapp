package com.cjit.webService.server.service.serviceImpl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cjit.vms.input.model.BillDetailEntity;
import com.cjit.webService.client.CemClient;
import com.cjit.webService.server.dao.CostControlDao;
import com.cjit.webService.server.entity.InputInfoData;
import com.cjit.webService.server.entity.InputInfoTemp;
import com.cjit.webService.server.entity.ResponseBody;
import com.cjit.webService.server.entity.ResponseHeader;
import com.cjit.webService.server.entity.ResponseWsdl;
import com.cjit.webService.server.entity.RollOutInfo;
import com.cjit.webService.server.service.CostControlService;
import com.cjit.webService.server.util.DataUtil;
import com.cjit.webService.server.util.ValidateUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CostControlServiceImpl implements CostControlService {
	private CostControlDao dao;

	@Override
	public String loadInputInfo(String inputInfoJson) {
		// 保存报文信息到vms_webservice_log
		dao.saveWebserviceInfo(inputInfoJson, "FK");
		Gson gson = new Gson();
		InputInfoData inputInfoData = gson.fromJson(inputInfoJson, InputInfoData.class);
		if (inputInfoData == null) {
			return this.faultWsdl("", "报文信息为空");
		}
		if (DataUtil.INPUT_DATE_REQUEST_TYPE_1.equals(inputInfoData.getHeader().getRequestType())) {
			String message = this.inputInfo(inputInfoData);
			if (DataUtil.RESPONSE_SUCCESS.equals(message)) {
				return this.successWsdl(DataUtil.INPUT_DATE_REQUEST_TYPE_1);
			} else {
				return this.faultWsdl(DataUtil.INPUT_DATE_REQUEST_TYPE_1, message);
			}

		} else if (DataUtil.INPUT_DATE_REQUEST_TYPE_2.equals(inputInfoData.getHeader().getRequestType())) {
			String message = this.rollOutTax(gson.fromJson(inputInfoJson, RollOutInfo.class));
			if (DataUtil.RESPONSE_SUCCESS.equals(message)) {
				return this.successWsdl(DataUtil.INPUT_DATE_REQUEST_TYPE_2);
			} else {
				return this.faultWsdl(DataUtil.INPUT_DATE_REQUEST_TYPE_2, message);
			}

		} else {
			return this.faultWsdl(inputInfoData.getHeader().getRequestType(), "请求类型不存在");
		}
	}

	/**
	 * 费控传转出税额处理过程
	 * 
	 * @param inputInfoData
	 * @return
	 */
	private String rollOutTax(RollOutInfo rollOutInfo) {

		List<BillDetailEntity> list = rollOutInfo.getBody().getBillDetailList();
		if (CollectionUtils.isEmpty(list)) {
			return "转出信息不能为空";
		}
		for (BillDetailEntity billDetailEntity : list) {
			String[] billFileds = { "id", "rollOutAmt", "remark" };
			String message = ValidateUtil.fieldsisEmpty(billDetailEntity, billFileds);
			if (StringUtils.isNotEmpty(message)) {
				return message;
			}
		}
		List<BillDetailEntity> list2=new ArrayList<BillDetailEntity>();
		for(BillDetailEntity billDetailEntity : list){
			String id=billDetailEntity.getId();
			List<String> lists=dao.getRoutAmt(id);
			int routAmt=0;
			String rollOutAmt="";
			if(lists.size()>0){
				rollOutAmt=lists.get(0);
			}
			BigDecimal rAmt=billDetailEntity.getRollOutAmt();
			if("".equals(rollOutAmt)&&null==rollOutAmt){
			}else{
				rAmt=new BigDecimal(rollOutAmt).add(rAmt);
			}
			billDetailEntity.setRollOutAmt(rAmt);
			list2.add(billDetailEntity);
		}
		dao.updateRollOut(list2);
		return DataUtil.RESPONSE_SUCCESS;
	}

	/**
	 * 费控传进项发票信息处理过程
	 * 
	 * @param inputInfoData
	 * @return
	 */
	private String inputInfo(InputInfoData inputInfoData) {
		InputInfoTemp inputInfo = inputInfoData.getBody().getInputInfoList();
		if (inputInfo == null) {
			return "进项发票信息不能为空";
		}
		String[] fileds = { "billId", "billCode", "billType", "id", "amt", "tax", "taxRate", "sumAmt",
				"isCredit", "purpose","available","name","taxNo" };
		String result = ValidateUtil.fieldsisEmpty(inputInfo, fileds);
		if (StringUtils.isNotEmpty(result)) {
			return result;
		}
		//通行费发票无需认证
		if (DataUtil.FAPIAO_TOLL.equals(inputInfo.getBillType())) {
			inputInfo.setBillStatus("2");
		}else {
			inputInfo.setBillStatus("0");
		}
		List<InputInfoTemp> list=new ArrayList<InputInfoTemp>();
		list.add(inputInfo);
		if ("1".equals(inputInfo.getAvailable())) {
			dao.saveInputInfoTemp(list);
		}else{
			//更新数据状态为无效
			dao.updateInputInfoTemp(list);
		}
		String bill=inputInfo.getBillId()+inputInfo.getBillCode();
		dao.deleteInputInfoNew(bill);
		if ("1".equals(inputInfo.getAvailable())) {
			dao.saveInputInfo(inputInfo);
		}else {
			dao.updateInputInfo(inputInfo);
		}
		//对通行费对发票修改为无需认证状态
		if (DataUtil.FAPIAO_TOLL.equals(inputInfo.getBillType())) {
			List<String> urList=dao.findWebServiceUrl("feikong");
			CemClient cemClient=new CemClient(urList.get(0));
			List<Map<String, String>> billList=new ArrayList<Map<String,String>>();
			Map<String, String> map=new HashMap<String, String>();
			map.put("billId", inputInfo.getBillId());
			map.put("billCode", inputInfo.getBillCode());
			map.put("billStatu", inputInfo.getBillStatus());
			billList.add(map);
			String json=enterOkJson(billList);
			cemClient.receiver("wsVatServer", "invoiceVerificationHandle", json);
		}
		return DataUtil.RESPONSE_SUCCESS;
	}
	/**
	 * 认证通过到发票json信息
	 * @param billList
	 */
	private String enterOkJson(List<Map<String, String>> billList) {
		JSONObject jsonObject=new JSONObject();
		JSONArray jsonArray=new JSONArray();
		jsonObject.put("taxTransferInfo", jsonArray);
		for (Map<String, String> map : billList) {
			JSONObject bill=new JSONObject();
			bill.put("billId", map.get("billId"));
			bill.put("billCode", map.get("billCode"));
			bill.put("billStatu", map.get("billStatu"));
			bill.put("billDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())); 
			jsonArray.add(bill);
		}
		return jsonObject.toJSONString();
	}
	/**
	 * 成功返回报文
	 * 
	 * @return
	 */
	private String successWsdl(String requestType) {
		ResponseHeader header = new ResponseHeader();
		header.setResponseType(requestType);
		header.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		ResponseBody body = new ResponseBody();
		body.setResult(DataUtil.RESPONSE_SUCCESS);
		body.setMessage("");
		ResponseWsdl responseWsdl = new ResponseWsdl(header, body);
		Gson gson = new GsonBuilder().serializeNulls().create();
		return gson.toJson(responseWsdl);
	}

	/**
	 * 失败返还报文
	 * 
	 * @param message
	 * @return
	 */
	private String faultWsdl(String requestType, String message) {
		ResponseHeader header = new ResponseHeader();
		header.setResponseType(requestType);
		header.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		ResponseBody body = new ResponseBody();
		body.setResult(DataUtil.RESPONSE_FAULT);
		body.setMessage(message);
		ResponseWsdl responseWsdl = new ResponseWsdl(header, body);
		Gson gson = new GsonBuilder().serializeNulls().create();
		return gson.toJson(responseWsdl);
	}

	public CostControlDao getDao() {
		return dao;
	}

	public void setDao(CostControlDao dao) {
		this.dao = dao;
	}
}
