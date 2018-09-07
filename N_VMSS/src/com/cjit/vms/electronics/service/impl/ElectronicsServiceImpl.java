package com.cjit.vms.electronics.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.common.util.DateUtils;
import com.cjit.common.util.NumberUtils;
import com.cjit.common.util.PaginationList;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.vms.electronics.service.ElectronicsService;
import com.cjit.vms.system.model.UBaseSysParamVmss;
import com.cjit.vms.trans.action.createBill.CheckResult;
import com.cjit.vms.trans.model.TransInfoTemp;
import com.cjit.vms.trans.model.createBill.BillGoodsInfo;
import com.cjit.vms.trans.model.createBill.BillInfo;
import com.cjit.vms.trans.model.createBill.BillTransInfo;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.createBill.billContex.BillContext;
import com.cjit.vms.trans.service.createBill.billContex.BillsTaxNoContext;
import com.cjit.vms.trans.util.DataUtil;

public class ElectronicsServiceImpl extends GenericServiceImpl implements
		ElectronicsService {

	private static final long serialVersionUID = 1L;

	@Override
	public List findElectronics(Map<String, Object> map) {
		System.out.println(map.keySet().size());
		System.out.println("00");
		return find("SelectElectronics", map);
	}

	@Override
	public List findElectronicsList(
			com.cjit.vms.trans.model.TransInfo transInfo,
			PaginationList paginationList) {
		Map map = new HashMap();
		List instIds = transInfo.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); ++i) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		String dataStatus = transInfo.getDataStatus();
		if ((dataStatus != null) && ("3,4,7".equals(dataStatus))) {
			transInfo.setDataStatus(null);
			map.put("issueStatuses", dataStatus.split(","));
		}
		map.put("transInfo", transInfo);
		return find("findElectronicsList", map, paginationList);
	}

	@Override
	public List findElectronicsList(com.cjit.vms.trans.model.TransInfo transInfo) {
		Map map = new HashMap();
		List instIds = transInfo.getLstAuthInstId();
		List lstTmp = new ArrayList();
		for (int i = 0; i < instIds.size(); ++i) {
			Organization org = (Organization) instIds.get(i);
			lstTmp.add(org.getId());
		}
		map.put("auth_inst_ids", lstTmp);
		String dataStatus = transInfo.getDataStatus();
		map.put("transInfo", transInfo);
		return find("findElectronicsList", map);
	}

	@Override
	public Long findInvalidInvoiceCount(String dataStatus, String fapiaoType,
			String instId) {
		Map map = new HashMap();
		map.put("dataStatus", dataStatus);
		map.put("fapiaoType", fapiaoType);
		map.put("instId", instId);

		return getRowCount("findInvalidEmptyPaperInvoiceCount", map);
	}

	@Override
	public String findTaxValuebyName(String itemCname) {
		Map map = new HashMap();
		map.put("itemCname", itemCname);
		List list = find("findvaluebyitemCname", map);
		UBaseSysParamVmss uBaseSysParamVmss = new UBaseSysParamVmss();
		if (list != null && list.size() == 1) {
			uBaseSysParamVmss = (UBaseSysParamVmss) list.get(0);
		}
		return uBaseSysParamVmss.getSelectedValue();
	}

	@Override
	public List findInstCodeByUserInstId(String userInstd) {
		Map map = new HashMap();
		map.put("userInstd", userInstd);
		return find("findInstCodesByUserInstId", map);
	}

	@Override
	public TransInfo findTransInfoByTransId(TransInfo info) {
		Map map = new HashMap();
		map.put("transInfo", info);
		@SuppressWarnings("unchecked")
		List<TransInfo> list = find("findTransByTransIdOrFaPiaoType", map);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public String findElectronicsPayee(String instId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("instId", instId);
		List<String> list = this.find("getElectronicsPayee", map);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return "";
	}

	@Override
	public List<BillInfo> eleConstructBillAndSaveAsMerge(List transInfoList) {
		BillsTaxNoContext billsTaxNoContext = constructBill(transInfoList);
		// 保存票据
		List<BillInfo> list = new ArrayList<BillInfo>();
		List<BillContext> billContexts = billsTaxNoContext.getAllBillContext();
		for (int i = 0; i < billContexts.size(); i++) {
			BillContext billContext = billContexts.get(i);
			billContext.getBillInfo().setDrawer("开票员");
			billContext.getBillInfo().setReviewer("复核员");
			billContext.getBillInfo().setPayee("收款员");
			saveContextAsMerge(billContext);
			list.add(billContext.getBillInfo());

		}
		return list;

	}

	@Override
	public BillsTaxNoContext constructBill(List transInfoList) {
		System.out.println(transInfoList.size());
		// 票据封装对象
		BillsTaxNoContext billsTaxNoContext = new BillsTaxNoContext();
		// 循环原始交易
		for (int i = 0; i < transInfoList.size(); i++) {
			// 取得原始交易
			TransInfo transInfo = (TransInfo) transInfoList.get(i);
			// 拆分超票据限额交易
			List<TransInfo> spilitTransList = spilitTrans(transInfo);
			System.err.println(spilitTransList.size());
			// 循环拆分后的交易 生成票据
			for (int j = 0; j < spilitTransList.size(); j++) {
				TransInfo spilitTrans = (TransInfo) spilitTransList.get(j);

				addTransToContext(billsTaxNoContext, spilitTrans);
			}

		}

		return billsTaxNoContext;
	}

	@Override
	public List<TransInfo> spilitTrans(TransInfo transInfo) {
		// 拆分后List
		List<TransInfo> spilitTransList = new ArrayList<TransInfo>();
		// 取得交易未开票金额
		BigDecimal balance = transInfo.getBalance();
		BigDecimal taxCnyBalance = transInfo.getTaxCnyBalance();
		// 发票最大限额
		//20180709 修改有问题
//		BigDecimal billMaxAmt = getBillMaxAmt(transInfo.getInstCode(),
//				transInfo.getFapiaoType());
		
		BigDecimal billMaxAmt = new BigDecimal("1000");
		billMaxAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
		// 交易未开票金超超出最大限额

		if (balance.compareTo(billMaxAmt) == 1) {
			// 拆分

			while (balance.compareTo(billMaxAmt) == 1) {
				try {
					TransInfo cloneTrans = new TransInfo();
					cloneTrans = (TransInfo) BeanUtils.cloneBean(transInfo);
					BigDecimal taxRate = cloneTrans.getTaxRate();
					BigDecimal OneAddRate = taxRate.add(BigDecimal.ONE);
					BigDecimal incomeTemp = balance.divide(OneAddRate, 10,
							BigDecimal.ROUND_HALF_UP);
					BigDecimal taxCnyBalanceTemp = incomeTemp.multiply(taxRate);
					BigDecimal cloneTaxCnyBalance = taxCnyBalanceTemp.setScale(
							2, BigDecimal.ROUND_HALF_UP);
					cloneTrans.setBalance(billMaxAmt);
					cloneTrans.setTaxCnyBalance(cloneTaxCnyBalance);

					balance = balance.subtract(billMaxAmt);
					taxCnyBalance = taxCnyBalance.subtract(cloneTaxCnyBalance);
					spilitTransList.add(cloneTrans);
				} catch (Exception e) {
					// 拆分异常 跳过当前交易
					e.printStackTrace();
					return new ArrayList<TransInfo>();
				}
			}
			transInfo.setBalance(balance);
			transInfo.setTaxCnyBalance(taxCnyBalance);
			spilitTransList.add(transInfo);
		} else {
			// 不拆分
			spilitTransList.add(transInfo);
		}

		return spilitTransList;

	}

	public BillContext addTransToContext(BillsTaxNoContext billsTaxNoContext,
			TransInfo transInfo) {

		// 取得票据分组基本信息
		BigDecimal balance = transInfo.getBalance();
		//20180709 修改有问题
//		BigDecimal billMaxAmt = getBillMaxAmt(transInfo.getInstCode(),
//				transInfo.getFapiaoType());
		BigDecimal billMaxAmt = new BigDecimal("1000");
		billMaxAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
		// 如果交易未开票金额等于票据最大限额起新票， 否则要在现有票据查找适合的票据
		if (balance.compareTo(billMaxAmt) == 0) {
			// 生成新票据
			billsTaxNoContext.addBillContext(createBillContext(transInfo));

		} else {
			// 根据税号 客户号 发票类型取得 票据List
			List<BillContext> billContextList = billsTaxNoContext
					.getBillContextList(transInfo);

			System.out.println(billContextList.size());

			// 没有相关票据 创建新的
			if (billContextList.size() == 0) {
				billsTaxNoContext.addBillContext(createBillContext(transInfo));
			} else {
				// 循环票据查找对应票据及商品
				BillContext billContextTar = null;
				BillGoodsInfo billGoodsInfoTar = null;
				for (int i = 0; i < billContextList.size(); i++) {
					BillContext billContextObj = billContextList.get(i);
					if (isMoreThanBillMaxAmt(billContextObj, transInfo)) {
						continue;
					}

					boolean isFullGoodsSize = isFullGoodsSize(billContextObj);
					BillGoodsInfo billGoodsInfo = findGoodsInfo(billContextObj,
							transInfo);

					// 如果没有找到商品且商品最大数量等于8 跳过当前票据
					if (null == billGoodsInfo && isFullGoodsSize) {
						continue;
					}
					billContextTar = billContextObj;
					billGoodsInfoTar = billGoodsInfo;
					break;

				}

				if (null == billContextTar) {
					billsTaxNoContext
							.addBillContext(createBillContext(transInfo));
				} else {
					if (null == billGoodsInfoTar) {
						billGoodsInfoTar = createBillGoodsInfo(transInfo);
						billContextTar.addGoodsInfo(billGoodsInfoTar);
						billContextTar.writeBackBillAmt(transInfo);
					} else {
						BillTransInfo billTransInfo = new BillTransInfo(
								transInfo);
						billGoodsInfoTar.addBillTransInfo(billTransInfo);
						billContextTar.writeBackBillAmt(transInfo);
					}
				}

			}

		}

		return null;

	}

	public BillGoodsInfo createBillGoodsInfo(TransInfo transInfo) {
		String billItemId = createBusinessId("BI");
		BillGoodsInfo goodsInfo = new BillGoodsInfo(billItemId, transInfo);
		return goodsInfo;
	}

	/**
	 * 生成业务主键（在插入数据时需要，由时间戳生成）
	 * 
	 * @return String
	 */
	protected String createBusinessId(String tabFlag) {
		String timeStamp = "";
		int busFlag = 1;
		if (tabFlag == null) {
			tabFlag = "";
		}
		String temp = DateUtils.serverCurrentTimeStamp();
		long random = Math.round(Math.random() * 110104 + 100000);
		if (!timeStamp.equals(temp)) {
			timeStamp = temp;
			busFlag = 1;
			return tabFlag + timeStamp
					+ NumberUtils.format(random, NumberUtils.DEFAULT_SEQNUMBER)
					+ busFlag++;
		} else {
			return tabFlag + temp
					+ NumberUtils.format(random, NumberUtils.DEFAULT_SEQNUMBER)
					+ busFlag++;
		}
	}

	public BillGoodsInfo findGoodsInfo(BillContext billContext,
			TransInfo transInfo) {

		// 校验新交易进入当前票据后的最后结果 成功后加入现有billsTaxNoContext
		String goodsId = transInfo.getVerificationInfo().getGoodsId();
		String goodsRate = transInfo.getTaxRate().toString();

		String goodsKey = goodsId + goodsRate;
		List<BillGoodsInfo> goodsList = billContext
				.getSameGoodsInfoList(goodsKey);

		BillGoodsInfo goodsInfoResult = null;
		// 商品循环
		for (int j = 0; j < goodsList.size(); j++) {
			BillGoodsInfo goodsInfo = goodsList.get(j);
			boolean isMoreThanGoodsMaxReteDiff = isMoreThanGoodsMaxReteDiff(
					goodsInfo, transInfo);
			// 如果大于最大误差 跳过当前商品
			if (isMoreThanGoodsMaxReteDiff) {
				continue;
			}
			goodsInfoResult = goodsInfo;
			break;
		}
		return goodsInfoResult;
	}

	public boolean isMoreThanGoodsMaxReteDiff(BillGoodsInfo goodsInfo,
			TransInfo transInfo) {

		// 开票金额
		BigDecimal tansAmt = transInfo.getBalance();
		// 交易税率
		BigDecimal transTaxRate = transInfo.getTaxRate();
		// 交易开票税额
		BigDecimal transTaxCny = transInfo.getTaxCnyBalance();

		// 商品金额
		BigDecimal goodsAmt = goodsInfo.getAmt();
		// 商品金额
		BigDecimal goodsTaxAmt = goodsInfo.getTaxAmt();
		// 当前票据加上当前交易开票金额
		BigDecimal transAndGoodsAmt = tansAmt.add(goodsAmt);

		// 当前商品金额当前交易开票金额
		BigDecimal transAndBillTaxCny = goodsTaxAmt.add(transTaxCny);

		BigDecimal billItemTaxAmt = DataUtil.calculateTaxAmt(transAndGoodsAmt,
				transTaxRate, null);
		// -------------------------- 判断轧差金额
		BigDecimal diff = (transAndBillTaxCny.subtract(billItemTaxAmt)).abs();

		if (diff.compareTo(DataUtil.different) >= 0) {
			return true;
		}
		return false;
	}

	/***
	 * 校验当前商品是否等于最大商品数量
	 * 
	 * @param billContext
	 * @param transInfo
	 * @return
	 */
	public boolean isFullGoodsSize(BillContext billContext) {
		int billContextGoodsInfoSize = billContext.getGoodsInfoSize();
		if (billContextGoodsInfoSize == 8) {
			return true;
		}
		return false;
	}

	/***
	 * 票据信息加上当前交易信息 是否大于票据最大金额
	 * 
	 * @param billContext
	 * @param transInfo
	 * @return
	 */
	public boolean isMoreThanBillMaxAmt(BillContext billContext,
			TransInfo transInfo) {
		// 开票金额
		BigDecimal tansAmt = transInfo.getBalance();
		// 票据金额
		BigDecimal billSumAmt = billContext.getSumAmt();
		// 当前票据加上当前交易开票金额
		BigDecimal transAndBillAmt = tansAmt.add(billSumAmt);

		// 新交易加上当前票据金额是否超限制最大金额
		if (transAndBillAmt.compareTo(getBillMaxAmt(transInfo.getInstCode(),
				transInfo.getFapiaoType())) == 1) {
			return true;
		}
		return false;
	}

	/***
	 * 创建新的票据上下文
	 * 
	 */

	public BillContext createBillContext(TransInfo transInfo) {

		String billId = createBillId("B");
		String billItemId = createBusinessId("BI");

		BillGoodsInfo billGoodsInfo = new BillGoodsInfo(billId, billItemId,
				transInfo);
		BillContext billContext = new BillContext(billId, billGoodsInfo,
				transInfo);
		return billContext;
	}

	protected String createBillId(String tabFlag) {
		String temp = DateUtils.serverCurrentDetailDate();
		String sequence = getBillIdSequence();
		return tabFlag + temp + sequence;
	}

	public String getBillIdSequence() {
		Map para = new HashMap();
		// 201801
		List list = this.find("getBillIdSequenceForPre", para);
		String sequence = (String) list.get(0);
		return sequence;
	}

	public BigDecimal getBillMaxAmt(String instCode, String fapiaoType) {
		Map<String, String> map = new HashMap();
		map.put("instCode", instCode);
		map.put("fapiaoType", fapiaoType);
		String maxAmtStr = (String) findForObject("selectMaxAmt", map);
		BigDecimal maxAmt = new BigDecimal(maxAmtStr);
		maxAmt.setScale(2, BigDecimal.ROUND_HALF_UP);
		return maxAmt;
	}

	@Override
	public void setBillStaticInfoMerge(BillInfo billInfo) {

		String billStatus = DataUtil.BILL_STATUS_1;// 状态-编辑待提交
		String isHandiwork = DataUtil.BILL_ISHANDIWORK_2;// 是否手工录入2-人工审核
		String isSueType = DataUtil.ISSUE_TYPE_2; // 发票开具类型1-单笔

		billInfo.setDatastatus(billStatus);// 状态-待提交
		billInfo.setIsHandiwork(isHandiwork);// 是否手工录入2-人工审核
		billInfo.setIssueType(isSueType); // 发票开具类型1-单笔
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		billInfo.setApplyDate(sf.format(new Date()));
	}

	public CheckResult saveContextAsMerge(BillContext billContext) {
		// 设置其他信息
		BillInfo billInfo = billContext.getBillInfo();
		setBillStaticInfoMerge(billInfo);
		// 保存
		saveBillInfo(billInfo);
		saveBillGoodsList(billContext.getGoodsInfoList());
		return null;
	}

	public void saveBillInfo(BillInfo billInfo) {
		Map map = new HashMap();
		map.put("billInfo", billInfo);
		save("saveBillInfo", map);
	}

	public void saveBillGoodsList(List<BillGoodsInfo> billGoodsInfoList) {
		List<BillGoodsInfo> billGoodsInfoList2 = new ArrayList<BillGoodsInfo>();
		int flag = 0;
		billGoodsInfoList2.add(billGoodsInfoList.get(0));
		for (int i = 1; i < billGoodsInfoList.size(); i++) {
			flag = 0;
			for (int j = 0; j < billGoodsInfoList2.size(); j++) {
				if (billGoodsInfoList.get(i).getGoodsName()
						.equals(billGoodsInfoList2.get(j).getGoodsName())) {
					billGoodsInfoList2.get(j).myAdd(billGoodsInfoList.get(i));
					flag = 1;
					break;
				}
			}
			if (flag == 0) {
				billGoodsInfoList2.add(billGoodsInfoList.get(i));
			}
		}
		for (int i = 0; i < billGoodsInfoList2.size(); i++) {
			BillGoodsInfo billGoodsInfo = billGoodsInfoList2.get(i);
			saveBillGoodsInfo(billGoodsInfo);
			// saveBillTransList(billGoodsInfo.getTransInfoList());
		}
		for (int i = 0; i < billGoodsInfoList.size(); i++) {
			BillGoodsInfo billGoodsInfo = billGoodsInfoList.get(i);
			saveBillTransList(billGoodsInfo.getTransInfoList());
			updateTransAmtAndStatus(billGoodsInfo.getTransInfoList());
		}
	}

	public void saveBillGoodsInfo(BillGoodsInfo billGoodsInfo) {
		Map map = new HashMap();
		map.put("billGoodsInfo", billGoodsInfo);
		save("saveBillGoodsInfo", map);
	}

	public void saveBillTransList(List<BillTransInfo> billTrans) {
		for (int i = 0; i < billTrans.size(); i++) {
			BillTransInfo billTransInfo = billTrans.get(i);
			saveBillTrans(billTransInfo);
		}
	}

	public void saveBillTrans(BillTransInfo billTrans) {
		Map map = new HashMap();
		map.put("billTrans", billTrans);
		save("saveBillTrans", map);
	}

	@Override
	public void updateTransAmtAndStatus(List<BillTransInfo> billTrans) {
		for (int i = 0; i < billTrans.size(); i++) {
			BillTransInfo billTransInfo = billTrans.get(i);
			Map map = new HashMap();
			map.put("billTrans", billTransInfo);
			save("updateTransAmtAndStatusManual", map);
		}
	}

	/**
	 * 新增
	 * 日期：2018-09-06
	 * 作者：刘俊杰
	 * 说明：从交易表中查出此交易对应的所有交易信息(包含不同险种)
	 * @param map
	 * @return
	 */
	@Override
	public List<TransInfoTemp> selectTransInfoOfElectronicsReuse(Map map) {
		return this.find("selectTransInfoOfElectronicsReuse", map);
	}
	/**
	 * 新增
	 * 日期：2018-09-06
	 * 作者：刘俊杰
	 * 说明：改变状态为ELECTRONICS_REDBILL_STATUS_302-未开具红票,流向电票红冲页面
	 * @param map
	 */
	@Override
	public void updateElectronicsTransRedStatusOfNotMake(Map map) {
		this.save("updateElectronicsTransRedStatusOfNotMake", map);
	}

	
	/**
	 * 新增
	 * 日期：2018-09-07
	 * 作者：cheng
	 * 说明：查看驳回原因
	 */
	@Override
	public com.cjit.vms.trans.model.createBill.BillInfo findBillInfo(
			String billId, String fapiaoType) {
		Map map = new HashMap();
		map.put("billId", billId);
		map.put("fapiaoType", fapiaoType);
		@SuppressWarnings("unchecked")
		List<BillInfo> list = find("findBillByBillIdOrFaPiaoType", map);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

}
