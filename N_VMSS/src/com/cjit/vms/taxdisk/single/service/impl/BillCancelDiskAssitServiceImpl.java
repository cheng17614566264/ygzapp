package com.cjit.vms.taxdisk.single.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.vms.taxdisk.servlet.model.parseXml.BillCancelReturnXml;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillCancelTiansInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.TaxDiskInfo;
import com.cjit.vms.taxdisk.single.service.BillCancelDiskAssitService;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.vms.taxdisk.tools.Message;
import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.billInvalid.BillCancelInfo;
import com.cjit.vms.trans.util.DataUtil;

import cjit.crms.util.date.DateUtil;

public class BillCancelDiskAssitServiceImpl extends GenericServiceImpl implements BillCancelDiskAssitService {

	/**
	 * 根据BillId获取票据信息
	 * 
	 */
	@Override
	public BillInfo findBillInfo(String billId) {
		BillInfo billInfo = new BillInfo();
		billInfo.setBillId(billId);

		List list = this.findBillInfoList(billInfo);
		if (list != null && list.size() == 1) {
			return (BillInfo) list.get(0);
		} else {
			return null;
		}
	}

	public List findBillInfoList(BillInfo billInfo) {
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		return find("findBillInfo", map);
	}

	/**
	 * 根据taxDiskNo获取税控盘信息
	 */
	@Override
	public TaxDiskInfo findTaxDiskInfo(String taxDiskNo) {

		Map param = new HashMap();
		param.put("taxDiskNo", taxDiskNo);
		return (TaxDiskInfo) this.findForObject("findTaxDiskInfoXmlByTaxDiskNo", param);

	}

	/*
	 * 更改票据状态
	 */
	@Override
	public AjaxReturn updateBillCancelResult(String billId, String returnMsg, boolean flag) throws Exception {
		AjaxReturn message = null;
		String resultMessage = "";
		try {
			if (flag) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("billId", billId);
				// 得到交易id
				@SuppressWarnings("unchecked")
				List<BillCancelInfo> list = this.find("getBillCancel", map);
				List<String> billIds = new ArrayList<String>();
				HashSet<String> transId = new HashSet<String>();
				for (BillCancelInfo billCancelInfo : list) {
					transId.add(billCancelInfo.getTransId());
					if (StringUtils.isEmpty(billCancelInfo.getBillCode())
							|| StringUtils.isEmpty(billCancelInfo.getBillNo())) {
						billIds.add(billCancelInfo.getBillId());
					} else if (billId.equals(billCancelInfo.getBillId())) {
						billIds.add(billCancelInfo.getBillId());
					} else if (!DataUtil.TRANS_STATUS_15.equals(billCancelInfo.getDataStatus())) {
						resultMessage = resultMessage + billCancelInfo.getBillCode() + "-" + billCancelInfo.getBillNo()
								+ ",";
					}
				}
				if (billIds.size() > 0) {
					Map<String, List<String>> idmap = new HashMap<String, List<String>>();
					idmap.put("trans_id", new ArrayList<String>(transId));
					// 更新 票据 信息 状态
					this.update("updateBillCancelResult", map);
					// 更新交易信息状态
					this.update("updateBillCancelTrans", idmap);
				}

				// //查询这些交易信息下的所有票据信息
				//
				// openBillCancelTrans(billId);
				if (resultMessage.length() > 0) {
					resultMessage = resultMessage.substring(0, resultMessage.length() - 1);
					message = new AjaxReturn(true, "该发票对应的交易下还有如下发票需要作废：" + resultMessage);
				} else {
					message = new AjaxReturn(true);
				}
			} else {
				message = new AjaxReturn(false, returnMsg);
				return message;
			}
		} catch (Exception e) {
			message = new AjaxReturn(false, Message.system_exception_update_bill_cancel_datastauas_error);
		}
		return message;

	}

	/**
	 * 1.根据当前票据billId更改当前票据信息为已作废状态 2.将当前发票的金额回填原交易
	 */
	public AjaxReturn updateBillCancelResult(List<String> billId, List<BillCancelReturnXml> billCancelList,
			boolean flag) throws Exception {
		AjaxReturn message = null;
		try {
			if (flag) {
				Map<String, List<String>> map = new HashMap<String, List<String>>();
				map.put("billId", billId);
				// 得到交易id
				@SuppressWarnings("unchecked")
				// 如果是合并开票，会出现多个transId对应一个billId，发生自动拆分，则会多个transId对应多个billId
				// 如果是拆分开票，会出现多个billId对应一个transId
				List<BillCancelInfo> list = this.find("getBillCancels", map);
				// 将交易数据以key-billId，value-BillCancelInfo集合的形式整理
				HashMap<String, List<BillCancelInfo>> billMap = new HashMap<String, List<BillCancelInfo>>();
				for (BillCancelInfo billCancelInfo : list) {
					String bill = billCancelInfo.getBillId();
					List<BillCancelInfo> billList = billMap.get(bill);
					if (billList != null) {
						billList.add(billCancelInfo);
					} else {
						billList = new ArrayList<BillCancelInfo>();
						billList.add(billCancelInfo);
						billMap.put(bill, billList);
					}
				}
				// 存储要更新的trans
				List<TransInfo> transList = new ArrayList<TransInfo>();
				for (BillCancelInfo billCancelInfo : list) {
					List<BillCancelInfo> billInfoList = billMap
							.get(billCancelInfo.getBillId());
					if (!DataUtil.BILL_STATUS_14.equals(billCancelInfo.getDataStatus())) {
						continue;
					}
					//票据价税合计
					BigDecimal sumAmt = billCancelInfo.getSumAmt();
					//票据税额
					BigDecimal taxAmtSum = billCancelInfo.getTaxAmtSum();
					for (BillCancelInfo info : billInfoList) {
						// 已开票价税合计金额
						BigDecimal ykAmt = info.getAmt().subtract(info.getBalance());
						//已开票税额   税额-未开票税额
						BigDecimal ykTaxAmt=info.getTaxAmtCny().subtract(info.getTaxCnyBalance());
						TransInfo transInfo = new TransInfo();
						transInfo.setDataStatus(DataUtil.TRANS_STATUS_3);
						transInfo.setTransId(info.getTransId());
						// 如果当前交易的已开票金额小于票据金额，则将该交易的未开票金额设为总金额。票据剩余金额回填下一条交易，直到金额回填完为止
						if (ykAmt.compareTo(sumAmt) == -1) {
							sumAmt = sumAmt.subtract(ykAmt);
							transInfo.setBalance(info.getAmt());
							//未开票税额
							taxAmtSum=taxAmtSum.subtract(ykTaxAmt);
							transInfo.setTaxCnyBalance(info.getTaxAmtCny());
							transList.add(transInfo);
						} else {
							transInfo.setBalance(info.getBalance().add(sumAmt));
							transInfo.setTaxCnyBalance(info.getTaxCnyBalance().add(taxAmtSum));
							transList.add(transInfo);
							break;
						}
					}
				}
				// 更新 票据 信息 状态为已作废
				this.update("updateBillCancelResultList", map);
				// 回填发票金额到原交易
				this.updateBatch("updateBillCancelTrans", transList);
				message=new AjaxReturn(true);
			} else {
				message = new AjaxReturn(false, billCancelList.get(0).getReturnmsg());
				return message;
			}
		} catch (Exception e) {
			message = new AjaxReturn(false, Message.system_exception_update_bill_cancel_datastauas_error);
		}
		return message;

	}

	/*
	 * 发票作废交易信息
	 */
	@Override
	public List billCancelTransInfo(String billId) {
		Map map = new HashMap();
		map.put("billId", billId);
		return find("billCancelTiansInfo", map);
	}

	// 释放交易
	@Override
	public void openBillCancelTrans(String billId) {
		List list = billCancelTransInfo(billId);
		for (int i = 0; i < list.size(); i++) {
			BillCancelTiansInfo billCancelTiansInfo = (BillCancelTiansInfo) list.get(i);
			// 拆分时更改数据
			BigDecimal balance = billCancelTiansInfo.getBalance().add(billCancelTiansInfo.getSumAmt());
			BigDecimal taxCnyBalance = billCancelTiansInfo.getTaxCnyBalance().add(billCancelTiansInfo.getTaxAmtSum());
			String transId = billCancelTiansInfo.getTransId();
			if ("3".equals(billCancelTiansInfo.getIssueType())) {
				// 开具类型 为拆分时
				if (balance.compareTo(billCancelTiansInfo.getAmtCny()) == 0) {
					// 置状态为未开票1
					updateBillDataStatusChai(transId, balance, taxCnyBalance, "1");
				} else {
					// 置状态为开票中3
					updateBillDataStatusChai(transId, balance, taxCnyBalance, "3");
				}
			} else {
				// 开具类型为单笔 合并时--置状态为未开票1
				updateBillDataStatus(transId, "1");
			}
		}
	}

	// 根据 billId 更改状态
	public void updateBillDataStatusChai(String transId, BigDecimal balance, BigDecimal taxCnyBalance,
			String dataStatus) {
		Map map = new HashMap();
		map.put("transId", transId);
		map.put("dataStatus", dataStatus);
		map.put("balance", balance);
		map.put("taxCnyBalance", taxCnyBalance);
		update("updateBillDataStatusChai", map);
	}

	public void updateBillDataStatus(String transId, String dataStatus) {
		Map map = new HashMap();
		map.put("transId", transId);
		map.put("dataStatus", dataStatus);
		update("updateBillDataDiskStatus", map);
	}

	@Override
	public void updateBillStatisticsCount(String billId) {
			Map<String, String> map=new HashMap<String, String>();
			map.put("billId", billId);
			@SuppressWarnings("unchecked")
			List<BillInfo> billInfos=this.find("findBillInfoDiskById", map);
			BillInfo billInfo=null;
			if (CollectionUtils.isNotEmpty(billInfos)) {
				billInfo=billInfos.get(0);
			}
			map.clear();
			map.put("billId", billInfo.getBillCode());
			map.put("billNo", billInfo.getBillNo());
			map.put("yffCount", "Y");
			this.update("updateBillCount", map);
			map.clear();
			map.put("billId", billInfo.getBillCode());
			map.put("billNo", billInfo.getBillNo());
			map.put("ykpCount", "Y");
			this.update("updateBillKJCount", map);
	}

}
