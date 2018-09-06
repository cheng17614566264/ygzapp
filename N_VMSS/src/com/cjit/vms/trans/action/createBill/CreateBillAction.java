package com.cjit.vms.trans.action.createBill;

import java.io.File;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.springframework.context.ApplicationContext;

import com.alibaba.fastjson.JSON;
import com.cjit.common.util.SpringContextUtil;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.customer.model.CustomerTemp;
import com.cjit.vms.system.model.Customer;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.vms.trans.action.DataDealAction;
import com.cjit.vms.trans.model.BillInfo;
import com.cjit.vms.trans.model.TransInfoTemp;
//import com.cjit.vms.trans.model.TransInfo;
import com.cjit.vms.trans.model.config.TransTypeInfo;
import com.cjit.vms.trans.model.createBill.TransInfo;
import com.cjit.vms.trans.service.VmsCommonService;
import com.cjit.vms.trans.service.createBill.BatchRunService;
import com.cjit.vms.trans.service.createBill.BillValidationService;
import com.cjit.vms.trans.service.createBill.CreateBillService;
import com.cjit.vms.trans.service.createBill.billContex.BillsTaxNoContext;
import com.cjit.vms.trans.service.impl.TransInfoServiceImpl;
import com.cjit.vms.trans.util.DataUtil;
import com.cjit.vms.trans.util.ImageUtil;
import com.cjit.webService.client.entity.ApplicationForm;
import com.cjit.ws.service.impl.VmsElectronWebServiceImp;
import com.opensymphony.xwork2.ModelDriven;

import cjit.crms.util.StringUtil;
import net.sf.json.JSONObject;

public class CreateBillAction extends DataDealAction implements ModelDriven<BillInfo>{
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(CreateBillAction.class);
	private String cherNum;// 保单号
	private String repNum;// 旧保单号
	/*private String startDate;
	private String endDate;*/
	private String ttmpRcno;// 投保单号
	private String customerName;// 客户名称
	private int scale = 10;
	/**
	 * 新增
	 * 日期:2018-09-03
	 * 作者：刘俊杰
	 * 功能：用于ajax返回json字符串
	 */
	private String result;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	//end 2018-09-03




	/*实例化*/
	BillInfo bi=new BillInfo();
	
	/*获取前端传输数据*/
	@Override
	public BillInfo getModel() {
		// TODO Auto-generated method stub
		return bi;
	}
	
	
	public BillInfo getBi() {
		return bi;
	}

	public void setBi(BillInfo bi) {
		this.bi = bi;
	}

	


	/***
	 * 开票查询移植-------------------------
	 */
	protected com.cjit.vms.trans.model.TransInfo transInfo = new com.cjit.vms.trans.model.TransInfo();
	protected List transInfoList;
	// 客户纳税人类别列表
	protected List custTaxPayerTypeList = new ArrayList();
	// 交易状态列表
	protected List transDataStatusList = new ArrayList();
	protected String message;

	/***
	 * 开票查询移植 end-----------------------
	 */

	/***
	 * 开票单张循环移值-------------------start
	 */
	BillValidationService billValidationService;
	CreateBillService createBillService;
	private String[] selectTransIds;

	/***
	 * 拆分开票
	 */
	private String transId;
	private String[] money;

	private String feeTyp;// 费用类型
	private String billFreq;// 交费频率
	private String polYear;// 保单年度
	private String hissDte;// 承保日期
	private String dsouRce;// 数据来源
	private String chanNel;// 渠道
	private String premTerm;// 期数
	private String hissBeginDte;// 开始
	private String hissEndDte;// 结束
	private Map chanNelList;
	private Map billFreqlList;
	private Map feeTypList;
	private Map dsouRceList;
	private Map<String, String> applyFeeTypList;
	private VmsCommonService vmsCommonService;
	private String premTermArray;
	
	//跑批按钮
	BatchRunService batchRunService;
	
	
	/***
	 * 开票单张循环移值-------------------end
	 */

	/**
	 * 开票（选中多笔交易，每笔交易开具一张发票）
	 * 
	 * @return String
	 */
	public String transToEachBill() throws Exception {
		String transIds = "";
		List transInfoList = new ArrayList();
		User currentUser = this.getCurrentUser();
		try {
			StringBuffer sbMessage = new StringBuffer();
			int transSuccess = 0;
			System.out.println(this.selectTransIds.length);
			for(int i=0;i<this.selectTransIds.length;i++){
				System.out.println(this.selectTransIds[i]+":"+i);
			}
			if (this.selectTransIds != null && this.selectTransIds.length > 0) {
				// 循环构建交易信息List
				for (int i = 0; i < this.selectTransIds.length; i++) {

					TransInfo searPar = new TransInfo();
					searPar.setTransId(this.selectTransIds[i]);
					searPar = createBillService.findTransInfo(searPar);
					if (searPar != null) {
						searPar.setSelectTransIds(this.selectTransIds);
					}
					Integer count = billValidationService.checkingTransByCherNum(searPar, false).size();

					transIds += this.selectTransIds[i];
					StringBuffer sb = new StringBuffer();
					sb.append("投保单号：【");
					sb.append((searPar.getTtmpRcno() != null && !"".equals(searPar.getTtmpRcno())
							? searPar.getTtmpRcno() : "         "));
					sb.append("】\n保单号：【");
					sb.append((searPar.getCherNum() != null && !"".equals(searPar.getCherNum()) ? searPar.getCherNum()
							: "         "));
					sb.append("】\n");
					sb.append("险种名称：【");
					sb.append(searPar.getInsNam() != null && !"".equals(searPar.getInsNam()) ? searPar.getInsNam()
							: "         ");
					sb.append("】\n");
					searPar.setRemark(sb.toString());
					transIds += this.selectTransIds[i];
					transInfoList.add(searPar);
				}
				// 校验
				CheckResult result = (CheckResult) billValidationService.shortCircuitValidation(transInfoList);
				// 构建票据 并保存
				if (CheckResult.CHECK_FAIL.equals(result.getCheckFlag())) {
					sbMessage = sbMessage.append(result.getCheckResultMsg());
				} else {
					for (int i = 0; i < transInfoList.size(); i++) {
						List dataList = new ArrayList();
						dataList.add(transInfoList.get(i));
						// 收款人名称
						String payee = createBillService.findPayee(currentUser.getOrgId());
						createBillService.constructBillAndSaveAsSingle(dataList, currentUser, payee);
					}

				}

			}

			if (sbMessage != null && sbMessage.toString().length() > 0) {
				this.message = sbMessage.toString();
				this.request.setAttribute("message", sbMessage.toString());
				this.setMessage(sbMessage.toString());
				this.setFromFlag("bill");
				return ERROR;
			}
			logManagerService.writeLog(request, this.getCurrentUser(), "00802:0003", "开票申请", "开票",
					"对交易ID为(" + transIds.substring(0, transIds.length() - 1) + ")的交易开票 成功" + transSuccess + "笔", "1");
			return SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(), "00802:0003", "开票申请", "开票",
					"对交易ID为(" + transIds.substring(0, transIds.length() - 1) + ")的交易开票", "0");
			log.error("TransInfoAction-transToEachBill", e);
			throw e;
		}
	}

	/**
	 * 菜单进入，查询交易信息列表（且冲账标识字段为N的记录）
	 * 
	 * @return String
	 */
	public String listTrans() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		this.message = (String) this.request.getAttribute("message");
		
		System.out.println(message);
		
		User user = this.getCurrentUser();
		try {
			if ("bill".equalsIgnoreCase(fromFlag)) {
				this.setTransInfo(transInfo);

			}
			chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE"); //null
			billFreqlList = this.vmsCommonService.findCodeDictionary("PAYMENT_FREQUENCY"); //null
			feeTypList = this.vmsCommonService.findCodeDictionary("CHARGES_TYPE"); //null
			dsouRceList = this.vmsCommonService.findCodeDictionary("DATA_SOURCE"); //null
			applyFeeTypList = new HashMap<String, String>();
			for (Iterator iterator = feeTypList.keySet().iterator(); iterator.hasNext();) {
				String type = (String) iterator.next();
				// 只检索首期、续期、定结、保全
				if ("001".equals(type) || "002".equals(type) || "003".equals(type) || "015".equals(type)) {
					applyFeeTypList.put(type, (String) feeTypList.get(type));
				}
			}
			AjaxReturn ajaxReturn=null;
			// 判断保单号和投保单号是否为空，若不为空，则先进行一次数据同步
			if (StringUtils.isNotBlank(cherNum) || StringUtils.isNotBlank(ttmpRcno)) {
				ApplicationForm applicationForm = new ApplicationForm();
				// 0002:开票申请 0003:返回发票信息
				applicationForm.setRequestionType("0002");
				
				/*修改之前*/
				/*applicationForm.setTtmprcNo(repNum);
				applicationForm.setChernum(cherNum);
				
				applicationForm.setTransDateStart(startDate);
				applicationForm.setTransDateEnd(endDate);
				applicationForm.setCustomerName(customerName);
				applicationForm.setTtmprcNo(ttmpRcno);
				*/
				
				/*修改之后*/
				applicationForm.setTtmprcNo(bi.getRepNum());
				applicationForm.setChernum(bi.getCherNum());
				
				applicationForm.setTransDateStart(bi.getStartDate());
				
				applicationForm.setTransDateEnd(bi.getEndDate());
				
				
				System.out.println(bi.getStartDate()+"mmimmmiimimmimimimm");
				
				
				applicationForm.setCustomerName(bi.getCustomerName());
				
				System.out.println(bi.getCustomerName());
				applicationForm.setTtmprcNo(bi.getTtmpRcno());
				String isYK = request.getParameter("isYK");
				if (StringUtils.isBlank(isYK)) {
					isYK = "0";
				}
				applicationForm.setIsYK(isYK);
				String batchNo = request.getParameter("batchNo");
				applicationForm.setBatchNo(batchNo);
				if (StringUtils.isBlank(batchNo)) {
					applicationForm.setBatchType("1");
				}
				ajaxReturn = this.transUpdate(applicationForm);
				
				ajaxReturn = null;
				System.out.println(ajaxReturn);
			}
			if (ajaxReturn!=null&&(!ajaxReturn.getIsNormal())) {
				this.message="数据同步失败"+"\n"+ajaxReturn.getMessage();
				return SUCCESS;
			}
			// 构造查询条件
			com.cjit.vms.trans.model.TransInfo transInfo = new com.cjit.vms.trans.model.TransInfo();
			transInfo = this.getTransInfo();
			/*修改之前*/
			/*transInfo.setTtmpRcno(ttmpRcno);
			transInfo.setCustomerName(customerName);
			
			System.out.println(transInfo.getCustomerName());
			transInfo.setRepNum(repNum);
			transInfo.setTransBeginDate(startDate);
			transInfo.setTransEndDate(endDate);
			transInfo.setCherNum(cherNum);
			System.out.println(transInfo.getCherNum());*/
			/*修改之后*/
			transInfo.setTtmpRcno(bi.getTtmpRcno());
			transInfo.setCustomerName(bi.getCustomerName());
			System.out.println(bi.getCustomerName());
			transInfo.setRepNum(bi.getRepNum());
			transInfo.setCherNum(bi.getCherNum());
		
			
			System.out.println(transInfo.getCherNum());
			
			System.out.println(bi.getStartDate()+"2222222222222222222222222222222222222");	

			transInfo.setTransBeginDate(bi.getStartDate());
			transInfo.setTransEndDate(bi.getEndDate());

			System.out.println(transInfo.getTransBeginDate());
			
			
			
			transInfo.setIsYK(request.getParameter("isYk"));
			transInfo.setBatchNo(request.getParameter("batchNo"));
			String batchType = request.getParameter("batchType");
			if (DataUtil.BATCH_ALL.equals(batchType)) {
				batchType = null;
			}
			transInfo.setBatchType(batchType);
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			
			/*System.out.println(lstAuthInstId.toString()+",list.............集合");
			for(int i=0;i<lstAuthInstId.size();i++){
				Organization org=(Organization)lstAuthInstId.get(i);
				System.out.println(org.getId()+":"+i);
			}*/
			
			transInfo.setLstAuthInstId(lstAuthInstId);
			if (user != null) {
				transInfo.setUserId(user.getId());
				System.out.println(user.getId());
			}
			transInfo.setSearchFlag(DataUtil.MAKE_INVOICE);
			custTaxPayerTypeList = this.createSelectList(DataUtil.TAXPAYER_TYPE, null, true); //sql语句有问题
			transDataStatusList = DataUtil.getTransDataStatusListForPageListTrans();
			// 查询 符合条件的customer_id
			String customerName = transInfo.getCustomerName();
			List customerObjs = new ArrayList();
			List customerIds = new ArrayList();
			if (null != customerName && !customerName.equals("")) {
				customerObjs = customerService.findTransByCustomers(customerName);
			}
			for (int i = 0; i < customerObjs.size(); i++) {
				String customerId = ((Customer) customerObjs.get(i)).getCustomerID();
				customerIds.add(customerId);
			}
			transInfo.setCustomerIds(customerIds);
			// 查询符合条件的transType
			String transName = transInfo.getTransTypeName();
			List transNameObjs = new ArrayList();
			List transTypeList = new ArrayList();
			if (null != transName && !transName.equals("")) {
				Map map = new HashMap();
				map.put("transName", transName);
				map.put("customerTaxPayerType", transInfo.getCustomerTaxPayerType());
				transNameObjs = transInfoService.findTransTypeList(map);
			}
			for (int i = 0; i < transNameObjs.size(); i++) {
				TransTypeInfo transType = ((TransTypeInfo) transNameObjs.get(i));
				String transTypeId = ((TransTypeInfo) transNameObjs.get(i)).getTransTypeId();
				transTypeList.add(transTypeId);
			}
			transInfo.setTransTypeList(transTypeList);
			this.paginationList.setShowCount("true");
			
			transInfoList = transInfoService.findTransInfoList(transInfo, paginationList);
			
			this.request.setAttribute("configCustomerFlag", this.configCustomerFlag);
			this.request.setAttribute("message", this.message);
			this.request.setAttribute("transInfoList", transInfoList);
			logManagerService.writeLog(request, user, "0001.0010", "查询开票", "查询", "查询可操作的交易记录信息", "1");
			return SUCCESS;
		} catch (Exception e) {  
			logManagerService.writeLog(request, user, "0001.0010", "查询开票", "查询", "查询可操作的交易记录信息", "0");
			log.error("TransInfoAction-listTrans", e);
		}
		return ERROR;
	}
	
	
	//将数据从中间表插入到应用表中Customer
	public void insertIntoMyselfFromCustomerTemp() {
		TransInfoServiceImpl transInfoServiceImpl=new TransInfoServiceImpl();
		transInfoServiceImpl.insertIntoMyselfFromCustomerTemp("insertIntoMyselfFromCustomerTemp");
		
		
	}
	
	
	
	
	public void selectTransToOneBill() throws Exception {

		List<TransInfo> transInfoList = new ArrayList<TransInfo>();
		User currentUser = this.getCurrentUser();
		selectTransIds = request.getParameter("selectTransIds").split(",");
		CheckResult result;
		try {

			int sum = createBillService.findSum(selectTransIds);
			int bussisedSum = createBillService.findBussisedSum(selectTransIds);
			String flag = createBillService.findFlag(selectTransIds[0]);
			if (this.selectTransIds == null || this.selectTransIds.length == 0) {
				result = new CheckResult(CheckResult.CHECK_FAIL, "", "请选择数据");
			} else if ("I".equals(flag) && bussisedSum >= 2 && this.selectTransIds.length < sum) {
				result = new CheckResult(CheckResult.CHECK_FAIL, "", "请将同一保单下的数据都选出!");
			} else {
				// 循环构建交易信息List
				for (int i = 0; i < this.selectTransIds.length; i++) {
					TransInfo searPar = new TransInfo();
					searPar.setTransId(this.selectTransIds[i]);
					searPar = createBillService.findTransInfo(searPar);
					transInfoList.add(searPar);
				}
				// 作全局校验
				CheckResult checkResult = (CheckResult) billValidationService.shortCircuitValidation(transInfoList);

				// 构建票据 并保存
				if (CheckResult.CHECK_FAIL.equals(checkResult.getCheckFlag())) {
					result = checkResult;
				} else {
					// 构建票据对象
					BillsTaxNoContext billsTaxNoContext = createBillService.constructBill(transInfoList, currentUser);

					String[] taxNos = billsTaxNoContext.getTaxNos();
					String[] customerIds = billsTaxNoContext.getCustomerIds(taxNos[0]);
					int billsize = billsTaxNoContext.getTaxNoBillContext(taxNos[0]).size();

					if (billsize > 0) {
						String resultMsg = "";
						resultMsg = "预计 :  \n";
						resultMsg += "交易数 : " + transInfoList.size() + "条\n";
						resultMsg += "客户数 : " + customerIds.length + "个 \n";
						resultMsg += "预计开票数 : " + billsize + "条 \n";
						resultMsg += "请确认是否继续!";
						result = new CheckResult(CheckResult.CHECK_OK, "", resultMsg);
					} else {
						result = new CheckResult(CheckResult.CHECK_FAIL, "", "没有票据生成");
					}

				}

			}
			response.setHeader("Content-Type", "text/xml; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(JSONObject.fromObject(result).toString());
			out.close();
		} catch (Exception e) {
			e.printStackTrace();

			CheckResult checkResult = new CheckResult(CheckResult.CHECK_FAIL, "", "票据生成发生异常");
			String resultMsg = JSONObject.fromObject(checkResult).toString();

			response.setHeader("Content-Type", "text/xml; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.print(resultMsg);
			out.close();
			throw e;
		}
	}

	/**
	 * 合并开票
	 * 
	 * @return
	 * @throws Exception
	 */
	public String transToOneBill() throws Exception {
		String transIds = "";
		List transInfoList = new ArrayList();
		User currentUser = this.getCurrentUser();
		// selectTransIds = request.getParameter("selectTransIds").split(",");

		String instFrom = null;
		String instTo = null;
		CheckResult result = null;
		try {
			// StringBuffer sbMessage = new StringBuffer();
			int transSuccess = 0;
			if (this.selectTransIds == null || this.selectTransIds.length == 0) {
				result = new CheckResult(CheckResult.CHECK_FAIL, "", "请选择数据");
			} else {
				// 总计金额
				BigDecimal sumAmt = new BigDecimal("0");
				// 循环构建交易信息List
				for (int i = 0; i < this.selectTransIds.length; i++) {
					TransInfo searPar = new TransInfo();
					searPar.setTransId(this.selectTransIds[i]);
					searPar = createBillService.findTransInfo(searPar);
					sumAmt = sumAmt.add(searPar.getBalance());

					if (this.premTermArray != null && !"".equals(this.premTermArray) && this.premTermArray.length() > 1
							&& instFrom == null && instTo == null) {
						String[] p = this.premTermArray.split(",");
						searPar.setSelectTransIds(this.selectTransIds);
						List<TransInfo> transByCherNum = billValidationService.checkingTransByCherNum(searPar, true);
						if (transByCherNum.size() > 0) {
							this.setRESULT_MESSAGE(URLDecoder.decode("客户【" + searPar.getCustomerName() + "】保单号【"
									+ searPar.getCherNum() + "】下,有期数为【" + searPar.getPremTermArrayMin() + " 至  "
									+ searPar.getPremTermArrayMax() + "】中的交易且未开票,不允许跨期开票!", "utf-8"));
							return ERROR;
						}
					}
					if (this.selectTransIds.length > 1 && instFrom == null && instTo == null) {
						List l = transInfoService.findTransInfoListByTransId(this.selectTransIds);
						TransInfo t = (TransInfo) l.get(0);
						TransInfo t1 = (TransInfo) l.get(l.size() - 1);
						instFrom = t.getInstFrom();
						instTo = t1.getInstTo();
					}
					if (this.selectTransIds.length <= 1 && instFrom == null && instTo == null) {
						instFrom = searPar.getInstFrom();
						instTo = searPar.getInstTo();
					}
					transIds += this.selectTransIds[i];
					transSuccess++;
					searPar.setInstFrom(instFrom);
					searPar.setInstTo(instTo);

					StringBuffer sb = new StringBuffer();
					sb.append("投保单号：【");
					sb.append((searPar.getTtmpRcno() != null && !"".equals(searPar.getTtmpRcno())
							? searPar.getTtmpRcno() : "         "));
					sb.append("】\n保单号：【");
					sb.append((searPar.getCherNum() != null && !"".equals(searPar.getCherNum()) ? searPar.getCherNum()
							: "         "));
					sb.append("】\n");
					sb.append("主险名称:【");
					sb.append(searPar.getPlanLongDesc());
					sb.append("】\n");
					searPar.setRemark(sb.toString());
					transInfoList.add(searPar);
				}
				// 作全局校验
				CheckResult checkResult = (CheckResult) billValidationService.shortCircuitValidation(transInfoList);
				if (CheckResult.CHECK_OK.equals(checkResult.getCheckFlag())) {
					TransInfo sumTrans = new TransInfo();
					sumTrans.setBalance(sumAmt);
					sumTrans.setInstCode(((TransInfo) transInfoList.get(0)).getInstCode());
					sumTrans.setFapiaoType(((TransInfo) transInfoList.get(0)).getFapiaoType());
					checkResult = billValidationService.checkMaxAmt(sumTrans);
				}
				// 构建票据 并保存
				if (CheckResult.CHECK_FAIL.equals(checkResult.getCheckFlag())) {
					result = checkResult;
				} else {

					// 收款人名称
					String payee = createBillService.findPayee(currentUser.getOrgId());
					// 构建票据对象
					createBillService.constructBillAndSaveAsMerge(transInfoList, currentUser, payee);

				}

			}

			if (null != result) {
				this.request.setAttribute("message", result.getCheckResultMsg());
				this.setMessage(result.getCheckResultMsg());
				this.setFromFlag("bill");
				return ERROR;
			} else {
				logManagerService.writeLog(request, this.getCurrentUser(), "00802:0003", "开票申请", "开票",
						"对交易ID为(" + transIds.substring(0, transIds.length() - 1) + ")的交易开票 成功" + transSuccess + "笔",
						"1");
				return SUCCESS;

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;

		}
	}

	/**
	 * 打开拆分开票弹出窗体
	 * 
	 * @return String
	 */
	public String splitTrans() {
		try {
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			if (this.selectTransIds != null && this.selectTransIds.length == 1) {
				BillInfo info = new BillInfo();
				info.setTransId(this.selectTransIds[0]);
				info.setLstAuthInstId(lstAuthInstId);
				com.cjit.vms.trans.model.TransInfo trans = transInfoService.findTransInfo(info);
				if (trans != null && trans.getBalance() != null) {
					BigDecimal amt = trans.getBalance();
					// amt = amt.add(amt.multiply(trans.getTaxRate()));
					// amt = amt.setScale(2,BigDecimal.ROUND_HALF_UP);
					DecimalFormat df = new DecimalFormat("0.00");
					this.request.setAttribute("amt", df.format(amt));
					this.request.setAttribute("transId", trans.getTransId());
					User currentUser = this.getCurrentUser();
					if (currentUser != null) {
						this.request.setAttribute("userId", currentUser.getId());
					}
				}
			}
			if (!"error".equalsIgnoreCase(fromFlag)) {
				this.setMessage(null);
			} else {
				fromFlag = null;
			}
			logManagerService.writeLog(request, this.getCurrentUser(), "0001.0010", "查询开票", "拆分开票",
					"选中交易流水号为(" + this.selectTransIds[0] + ")的交易准备进行拆分开票处理", "1");
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(), "0001.0010", "查询开票", "拆分开票",
					"选中交易流水号为(" + this.selectTransIds[0] + ")的交易准备进行拆分开票处理", "0");
			log.error("TransInfoAction-transToManyBill", e);
		}
		return SUCCESS;
	}

	/**
	 * 执行拆分开票保存功能
	 * 
	 * @return String
	 */
	public String transToManyBill() throws Exception {
		try {
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			User currentUser = this.getCurrentUser();
			StringBuffer sbMessage = new StringBuffer();
			List transInfoList = new ArrayList();
			System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");
			System.out.println(this.money+"hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
			System.out.println(this.money.length+"pppppppppppppppppppppppppppppppp");
			System.out.println(bi.getTransId()+"yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
			/*if (StringUtil.isNotEmpty(this.transId) && this.money != null && this.money.length > 0) {*/
			if (StringUtil.isNotEmpty(bi.getTransId()) && this.money != null && this.money.length > 0) {
				
				
				TransInfo searPar = new TransInfo();
				/*searPar.setTransId(transId);*/
				searPar.setTransId(bi.getTransId());
				// 查询交易信息
				searPar = createBillService.findTransInfo(searPar);

				// 判断交易信息状态
				if (searPar == null) {
					sbMessage.append(" NotExistsTrans ");
				} else {

					this.request.setAttribute("amt", searPar.getBalance().toString());
					this.request.setAttribute("transId", searPar.getTransId());
					currentUser = this.getCurrentUser();
					if (currentUser != null) {
						this.request.setAttribute("userId", currentUser.getId());
					}

				}

				// 依据拆分金额money的数量，将该笔交易拆分为多笔票据
				for (int i = 0; i < this.money.length; i++) {
					BigDecimal transAmt = new BigDecimal(this.money[i]);
					
					System.out.println(transAmt);
					// 拆分金额格式不对，请输入小数位最多2位的正数数字。
					if (transAmt.scale() > 2 || transAmt.compareTo(new BigDecimal(0.0)) < 0) {
						if (sbMessage.toString().indexOf(" MoneyError ") < 0) {
							sbMessage.append(" MoneyError ");
						}
						continue;
					}

				}

				// 依据拆分金额money的数量，将该笔交易拆分为多笔票据
				// 合计税额
				BigDecimal taxAmt = new BigDecimal("0.00");
				for (int i = 0; i < this.money.length; i++) {
					TransInfo cloneTrans = new TransInfo();
					cloneTrans = (TransInfo) BeanUtils.cloneBean(searPar);
					BigDecimal taxRate = cloneTrans.getTaxRate();
					BigDecimal balance = new BigDecimal(money[i]);
					BigDecimal OneAddRate = taxRate.add(BigDecimal.ONE);
					BigDecimal incomeTemp = balance.divide(OneAddRate, 10, BigDecimal.ROUND_HALF_UP);
					BigDecimal taxCnyBalanceTemp = incomeTemp.multiply(taxRate);
					BigDecimal taxCnyBalance = taxCnyBalanceTemp.setScale(2, BigDecimal.ROUND_HALF_UP);
					cloneTrans.setBalance(balance);
					StringBuffer sb = new StringBuffer();
					sb.append("投保单号：【");
					sb.append((searPar.getTtmpRcno() != null && !"".equals(searPar.getTtmpRcno())
							? searPar.getTtmpRcno() : "         "));
					sb.append("】\n保单号：【");
					sb.append((searPar.getCherNum() != null && !"".equals(searPar.getCherNum()) ? searPar.getCherNum()
							: "         "));
					sb.append("】\n");
					sb.append("险种：【");
					sb.append(searPar.getInsNam());
					sb.append("】\n");
					cloneTrans.setRemark(sb.toString());
					cloneTrans.setTaxCnyBalance(taxCnyBalance);
					transInfoList.add(cloneTrans);
				}

				// 校验
				CheckResult result = (CheckResult) billValidationService.shortCircuitValidation(transInfoList);
				
				System.out.println(result.getCheckFlag()+"LLLLLLLLLLLLLLLLLLLLLLLLLLL");
				
				if (CheckResult.CHECK_OK.equals(result.getCheckFlag())) {
					for (int i = 0; i < transInfoList.size(); i++) {
						System.out.println(transInfoList.get(i)+"rrrrrrrrrrrrrrrrrrrrrrrrrrrr");
						result = billValidationService.checkMaxAmt((TransInfo) transInfoList.get(i));
					}
				}
				
				System.out.println(result.getCheckFlag()+"ttttttttttttttttttttttttt");
				// 构建票据 并保存
				if (CheckResult.CHECK_FAIL.equals(result.getCheckFlag())) {
					sbMessage = sbMessage.append(result.getCheckResultMsg());
				} else {
					for (int i = 0; i < transInfoList.size(); i++) {
						List dataList = new ArrayList();
						dataList.add(transInfoList.get(i));
						// 收款人名称
						String payee = createBillService.findPayee(currentUser.getOrgId());
						createBillService.constructBillAndSaveAsSplit(dataList, currentUser, payee);
					}

				}

				if (sbMessage != null && sbMessage.toString().length() > 0) {
					this.message = sbMessage.toString();
					this.request.setAttribute("message", sbMessage.toString());
					System.out.println(sbMessage.toString());
					this.setMessage(sbMessage.toString());
					return ERROR;
				}

			}
			logManagerService.writeLog(request, this.getCurrentUser(), "00802:0003", "开票申请", "拆分开票",
					/*"将交易流水号(" + this.transId + ")的交易进行拆分开票处理", "1");*/
					"将交易流水号(" + bi.getTransId()+ ")的交易进行拆分开票处理", "1");
		} catch (Exception e) {
			e.printStackTrace();
			logManagerService.writeLog(request, this.getCurrentUser(), "00802:0003", "开票申请", "拆分开票",
					/*"将交易流水号(" + this.transId + ")的交易进行拆分开票处理", "0");*/
					
					"将交易流水号(" + bi.getTransId() + ")的交易进行拆分开票处理", "0");
			log.error("TransInfoAction-transToManyBill", e);
			throw e;
		}
		return SUCCESS;
	}

	/**
	 * 申请开票交易
	 * 
	 * @return String
	 */
	public String applyInvoice() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		this.message = (String) this.request.getAttribute("message");
		User user = this.getCurrentUser();

		try {
			// 校验数据库中是否存在已开票的申请记录
			// String businessType =
			// this.request.getParameter("business_type2");
			String businessCode = this.request.getParameter("business_code2");
			// String source = this.request.getParameter("source2");

			// 先要把界面的东西初始化出来
			chanNelList = this.vmsCommonService.findCodeDictionary("CHANNEL_TYPE");
			billFreqlList = this.vmsCommonService.findCodeDictionary("PAYMENT_FREQUENCY");
			feeTypList = this.vmsCommonService.findCodeDictionary("CHARGES_TYPE");
			dsouRceList = this.vmsCommonService.findCodeDictionary("DATA_SOURCE");
			applyFeeTypList = new HashMap<String, String>();
			for (Iterator iterator = feeTypList.keySet().iterator(); iterator.hasNext();) {
				String type = (String) iterator.next();
				// 只检索首期、续期、定结、保全
				if ("001".equals(type) || "002".equals(type) || "003".equals(type) || "015".equals(type)) {
					applyFeeTypList.put(type, (String) feeTypList.get(type));
				}
			}
			custTaxPayerTypeList = this.createSelectList(DataUtil.TAXPAYER_TYPE, null, true);
			transDataStatusList = DataUtil.getTransDataStatusListForPageListTrans();

			Map applyMap = new HashMap();

			// 只有保全或者定结类型，才需要去数据库中校验，根据类型和业务号校验如果存在 状态<>'未开票' 的记录，则直接返回前端提示错误
			String newBusinessType = "";
			/*
			 * if("003".equals(businessType) || "015".equals(businessType)){
			 * applyMap.put("repnum",businessCode);
			 * 
			 * if("003".equals(businessType)){ //若为保全则拆分为 保全加人、保全减人、计划变更、增额几个保全项
			 * newBusinessType = "016','017','018','019"; }
			 * 
			 * //不需要区分某用户某机构下的数据，只要能查询到即认为不可删除 applyMap.put("businessType",
			 * newBusinessType); applyMap.put("source",source);
			 * applyMap.put("datastatus","1");
			 * 
			 * Long CheckNum =
			 * transInfoService.findCheckApplyTransInfoCount(applyMap);
			 * if(CheckNum > 0){ this.message = "该笔业务已存在已开票或开票中记录，不允许重新开票";
			 * return SUCCESS; } }
			 */
			// 走接口
			String returnStr = transInfoService.applyInvoice(request, user, businessCode);
			if (returnStr != null && "1".equals(returnStr.split("\\|")[0])) {
				// 接口调用失败
				this.message = returnStr.split("\\|")[1];
				return SUCCESS;
			}

			if ("bill".equalsIgnoreCase(fromFlag)) {
				this.setTransInfo(transInfo);
			}

			// 构造查询条件
			com.cjit.vms.trans.model.TransInfo transInfo = new com.cjit.vms.trans.model.TransInfo();
			transInfo = this.getTransInfo();

			// 添加发票申请的查询条件
			List lstAuthInstId = new ArrayList();
			this.getAuthInstList(lstAuthInstId);
			transInfo.setLstAuthInstId(lstAuthInstId);
			if (user != null) {
				transInfo.setUserId(user.getId());
			}
			transInfo.setSearchFlag(DataUtil.MAKE_INVOICE);
			// 查询 符合条件的customer_id
			String customerName = transInfo.getCustomerName();
			List customerObjs = new ArrayList();
			List customerIds = new ArrayList();
			if (null != customerName && !customerName.equals("")) {
				customerObjs = customerService.findTransByCustomers(customerName);
			}
			for (int i = 0; i < customerObjs.size(); i++) {
				String customerId = ((Customer) customerObjs.get(i)).getCustomerID();
				customerIds.add(customerId);
			}
			transInfo.setCustomerIds(customerIds);
			// 查询符合条件的transType
			String transName = transInfo.getTransTypeName();
			List transNameObjs = new ArrayList();
			List transTypeList = new ArrayList();
			if (null != transName && !transName.equals("")) {
				Map map = new HashMap();
				map.put("transName", transName);
				map.put("customerTaxPayerType", transInfo.getCustomerTaxPayerType());
				transNameObjs = transInfoService.findTransTypeList(map);
			}
			for (int i = 0; i < transNameObjs.size(); i++) {
				TransTypeInfo transType = ((TransTypeInfo) transNameObjs.get(i));
				String transTypeId = ((TransTypeInfo) transNameObjs.get(i)).getTransTypeId();
				transTypeList.add(transTypeId);
			}
			transInfo.setTransTypeList(transTypeList);
			this.paginationList.setShowCount("false");
			transInfoList = transInfoService.findTransInfoList(transInfo, paginationList);
			
			this.request.setAttribute("configCustomerFlag", this.configCustomerFlag);
			this.request.setAttribute("transInfoList", transInfoList);
			logManagerService.writeLog(request, user, "0001.0010", "查询开票", "查询", "查询可操作的交易记录信息", "1");
			this.message = "数据请求成功";
			return SUCCESS;
		} catch (Exception e) {
			this.message = "系统处理异常";
			e.printStackTrace();
			logManagerService.writeLog(request, user, "0001.0010", "查询开票", "查询", "查询可操作的交易记录信息", "0");
			log.error("TransInfoAction-listTrans", e);
		} finally {
			this.request.setAttribute("message", this.message);
		}
		return ERROR;
	}

	/**
	 * “VMS销项进税管理->开票管理->开票申请”中的删除方法
	 */
	public void deleteTransData() {
		// 用于记录判断删除是否成功
		PrintWriter out = null;
		try {
			// 删除的记录trans_id
			String selectTransIds = request.getParameter("selectTransIds");
			// 删除的接口方法
			transInfoService.deleteTransData(selectTransIds);
			out = response.getWriter();
			out.print("Y");
		} catch (Exception e) {
			out.print("N");
			e.printStackTrace();
		}
	}

	/**
	 * 同步开票申请
	 * 
	 * @return
	 */
	public String synchTransInfo() {
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		ApplicationForm applicationForm = new ApplicationForm();
		// 0002:开票申请 0003:返回发票信息
		cherNum = request.getParameter("cherNum");
		repNum  = request.getParameter("repNum");
		customerName = request.getParameter("customerName");
		applicationForm.setRequestionType("0002");
		applicationForm.setTtmprcNo(repNum);
		applicationForm.setChernum(cherNum);
		
		/*修改之前*/
		/*applicationForm.setTransDateStart(startDate);
		applicationForm.setTransDateEnd(endDate);*/
		
		/*修改之后*/
		applicationForm.setTransDateStart(bi.getStartDate());
		applicationForm.setTransDateEnd(bi.getEndDate());
		System.out.println(bi.getStartDate()+"333333333333333333333333333333333333");
		
		applicationForm.setCustomerName(customerName);
		String isYK = request.getParameter("isYK");
		if (StringUtils.isBlank(isYK)) {
			isYK = "0";
		}
		applicationForm.setIsYK(isYK);
		String batchNo = request.getParameter("batchNo");
		applicationForm.setBatchNo(batchNo);
		if (StringUtils.isBlank(batchNo)) {
			applicationForm.setBatchType("1");
		}
		AjaxReturn ajaxReturn = this.transUpdate(applicationForm);
		result = JSON.toJSONString(ajaxReturn);System.out.println(result);
		return SUCCESS;
	}

	/**
	 * 数据同步
	 */
	private AjaxReturn transUpdate(ApplicationForm applicationForm) {
		/*List<String> serviceList = billValidationService.findWebServiceUrl(WebServiceUtil.HEXIN_SERVICE_NAME);
		if (serviceList == null || serviceList.size() == 0) {
			return new AjaxReturn(false, "webservice接口地址没有配置，请联系管理员。");
		}
		String url = serviceList.get(0);
		try {
			InvoicePrintServiceServiceStub stub = new InvoicePrintServiceServiceStub(url);
			SubmitData submitData = new SubmitData();
			// 设置流水号
			submitData.setRequestJSON(JsonUtil.applictionFormJson(applicationForm));
			SubmitDataResponse submitDataResponse = stub.submitData(submitData);
			String json = submitDataResponse.getSubmitDataReturn();
			// 将json信息保存到日志文件
			LogPrintUtil.info(json + "\n");
			if (json.equals("{}")) {

				return new AjaxReturn(true, "可同步的交易数为零");
			}
			List<Policy> list = JsonUtil.formatJsonToPolicy(json, applicationForm.getIsYK());
			if (list == null || list.size() == 0) {
				return new AjaxReturn(true, "可同步的数据数为零");
			}
			String message = TransUtil.checkTransInfo(list);
			boolean result = true;
			if (message.equals("ERROR")) {
				result = false;
				*//**
				 * 数据校验后返回校验结果
				 *//*
				List<String> bussList=new ArrayList<String>();
				for (Policy policy : list) {
					bussList.add(policy.getBusinessId());
				}
				submitData.setRequestJSON(JsonUtil.synchFailJson(bussList, message, result));
				stub.submitData(submitData);
				return new AjaxReturn(false, "核心交易信息不全，请联系管理员。");
			}
			Set<com.cjit.webService.client.entity.Customer> customerSet = new HashSet<com.cjit.webService.client.entity.Customer>();

			for (Policy policy : list) {
				Cover cover = policy.getCover();
				// 设置是否含税标志
				if (cover.getTaxRate() == 0) {
					policy.setTaxFlag("N");
				} else {
					policy.setTaxFlag("Y");
				}
				// 未开票税额
				policy.setTaxCnyBalance(cover.getTaxAmtCny());
				// 未开票金额
				policy.setBalance(cover.getAmtCny());
				// 设置数据来源未核心
				policy.setDsource("HX");
				// 设置数据状态为未开票
				policy.setDataStatus("1");
				customerSet.add(policy.getCustomer());
			}
			
			//机构修改，将所有下属机构代码更改为二级机构代码 2018-06-27
			List authInstList = this.getAuthInstList();
			for (Policy policy : list) {
				for (int i = 0; i < authInstList.size(); i++) {
					Organization org = (Organization) authInstList.get(i);
					if (policy.getInstId().equals(org.getId())) {
						Organization orgs = (Organization) authInstList.get(0);
						System.out.println(orgs);
						policy.setInstId(orgs.getId());
					}
				}
			}
			// 保存客户信息
			billValidationService.saveCustomer(customerSet);
			// 保存保单信息
			billValidationService.synchTransInfo(list);
			return new AjaxReturn(true, "数据同步成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new AjaxReturn(false, "数据同步失败");
		} */
		try {
			syncDataOfCore(applicationForm);
			return new AjaxReturn(true, "数据同步成功");
		}catch(Exception e) {
			e.printStackTrace();
			return new AjaxReturn(false, "数据同步失败");
		}
	}

	private void returnResult(AjaxReturn ajaxReturn) throws Exception {
		response.setHeader("Content-Type", "text/xml;charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(JSON.toJSONString(ajaxReturn));
		out.close();
	}

	public com.cjit.vms.trans.model.TransInfo getTransInfo() {
		return transInfo;
	}

	public void setTransInfo(com.cjit.vms.trans.model.TransInfo transInfo) {
		this.transInfo = transInfo;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List getTransInfoList() {
		return transInfoList;
	}

	public void setTransInfoList(List transInfoList) {
		this.transInfoList = transInfoList;
	}

	public List getCustTaxPayerTypeList() {
		return custTaxPayerTypeList;
	}

	public void setCustTaxPayerTypeList(List custTaxPayerTypeList) {
		this.custTaxPayerTypeList = custTaxPayerTypeList;
	}

	public List getTransDataStatusList() {
		return transDataStatusList;
	}

	public void setTransDataStatusList(List transDataStatusList) {
		this.transDataStatusList = transDataStatusList;
	}

	public String[] getSelectTransIds() {
		return selectTransIds;
	}

	public void setSelectTransIds(String[] selectTransIds) {
		this.selectTransIds = selectTransIds;
	}

	public CreateBillService getCreateBillService() {
		return createBillService;
	}

	public void setCreateBillService(CreateBillService createBillService) {
		this.createBillService = createBillService;
	}

	public BillValidationService getBillValidationService() {
		return billValidationService;
	}

	public void setBillValidationService(BillValidationService billValidationService) {
		this.billValidationService = billValidationService;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String[] getMoney() {
		return money;
	}

	public void setMoney(String[] money) {
		this.money = money;
	}

	public String getFeeTyp() {
		return feeTyp;
	}

	public void setFeeTyp(String feeTyp) {
		this.feeTyp = feeTyp;
	}

	public String getBillFreq() {
		return billFreq;
	}

	public void setBillFreq(String billFreq) {
		this.billFreq = billFreq;
	}

	public String getPolYear() {
		return polYear;
	}

	public void setPolYear(String polYear) {
		this.polYear = polYear;
	}

	public String getHissDte() {
		return hissDte;
	}

	public void setHissDte(String hissDte) {
		this.hissDte = hissDte;
	}

	public String getDsouRce() {
		return dsouRce;
	}

	public void setDsouRce(String dsouRce) {
		this.dsouRce = dsouRce;
	}

	public String getChanNel() {
		return chanNel;
	}

	public void setChanNel(String chanNel) {
		this.chanNel = chanNel;
	}

	public String getPremTerm() {
		return premTerm;
	}

	public void setPremTerm(String premTerm) {
		this.premTerm = premTerm;
	}

	public String getHissBeginDte() {
		return hissBeginDte;
	}

	public void setHissBeginDte(String hissBeginDte) {
		this.hissBeginDte = hissBeginDte;
	}

	public String getHissEndDte() {
		return hissEndDte;
	}

	public void setHissEndDte(String hissEndDte) {
		this.hissEndDte = hissEndDte;
	}

	public Map getChanNelList() {
		return chanNelList;
	}

	public void setChanNelList(Map chanNelList) {
		this.chanNelList = chanNelList;
	}

	public Map getBillFreqlList() {
		return billFreqlList;
	}

	public void setBillFreqlList(Map billFreqlList) {
		this.billFreqlList = billFreqlList;
	}

	public Map getFeeTypList() {
		return feeTypList;
	}

	public void setFeeTypList(Map feeTypList) {
		this.feeTypList = feeTypList;
	}

	public Map getDsouRceList() {
		return dsouRceList;
	}

	public void setDsouRceList(Map dsouRceList) {
		this.dsouRceList = dsouRceList;
	}

	public Map<String, String> getApplyFeeTypList() {
		return applyFeeTypList;
	}

	public void setApplyFeeTypList(Map<String, String> applyFeeTypList) {
		this.applyFeeTypList = applyFeeTypList;
	}

	@Override
	public VmsCommonService getVmsCommonService() {
		return vmsCommonService;
	}

	@Override
	public void setVmsCommonService(VmsCommonService vmsCommonService) {
		this.vmsCommonService = vmsCommonService;
	}

	public String getPremTermArray() {
		return premTermArray;
	}

	public void setPremTermArray(String premTermArray) {
		this.premTermArray = premTermArray;
	}

	public String getCherNum() {
		return cherNum;
	}

	public void setCherNum(String cherNum) {
		this.cherNum = cherNum;
	}

	public String getRepNum() {
		return repNum;
	}

	public void setRepNum(String repNum) {
		this.repNum = repNum;
	}

	/*public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}*/

	public String getTtmpRcno() {
		return ttmpRcno;
	}

	public void setTtmpRcno(String ttmpRcno) {
		this.ttmpRcno = ttmpRcno;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	
	
	
	public BatchRunService getBatchRunService() {
		return batchRunService;
	}


	public void setBatchRunService(BatchRunService batchRunService) {
		this.batchRunService = batchRunService;
	}


	@SuppressWarnings("unchecked")
	public String upload(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		
 		MultiPartRequestWrapper mRequest = (MultiPartRequestWrapper) request;
 		try {
			mRequest.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		File[] files = mRequest.getFiles("theFile");
		String [] flNames=mRequest.getFileNames("theFile");
		String fileName =request.getParameter("fileName")+"/"+request.getParameter("fileName")+"-2";
		if (files != null && files.length > 0) {
			try {
				ImageUtil.info(fileName, files, flNames ,true);
				return SUCCESS;
			} catch (Exception e) {
				log.error(e);
				this.setResultMessages("上传文件失败:" + e.getMessage());
				e.printStackTrace();
				return ERROR;
			}
		} else {
			this.setResultMessages("上传文件失败!");
			return ERROR;
		}
	}

	
	//跑批按钮
	public String batchRun(){
		if (!sessionInit(true)) {
			request.setAttribute("msg", "用户失效");
			return ERROR;
		}
		ApplicationForm applicationForm = new ApplicationForm();
		try {
			syncDataOfCore(applicationForm);
			return SUCCESS;
		}catch(Exception e) {
			e.printStackTrace();
			return ERROR;
		}
		
	}
	
	/**
	 * 新增
	 * 日期：2018-08-20
	 * 作者：刘俊杰
	 * 功能：执行核心数据同步的具体代码
	 */
	public void syncDataOfCore(ApplicationForm applicationForm) {
		Map selectMap = new HashMap();
		selectMap.put("chernum", applicationForm.getChernum());
		try {
			List<TransInfoTemp> batchRunTransInfo=batchRunService.batchRunTransInfo(selectMap);
			//新增 2018-08-23 刘俊杰 判断机构权限
			boolean tag = false;
			List<String> customerId = new ArrayList<String>();
			//end 2018-08-23
			for(int i=0;i<batchRunTransInfo.size();i++){
				TransInfoTemp batchRunTransInfo1=batchRunTransInfo.get(i);
				Map map=new HashMap();
				map.put("TRANS_ID", batchRunTransInfo1.getTRANS_ID());
				map.put("TRANS_DATE",batchRunTransInfo1.getTRANS_DATE());
				map.put("TRANS_TYPE",batchRunTransInfo1.getTRANS_TYPE());
				map.put("TAX_FLAG", batchRunTransInfo1.getTAX_FLAG());
				map.put("AMT_CCY", batchRunTransInfo1.getAMT_CCY());
				map.put("BALANCE", batchRunTransInfo1.getBALANCE());
				map.put("ORIG_INSTCODE", batchRunTransInfo1.getINSTCODE());
				//判断如果是自动跑批,则将其机构转换为开票机构,不控制其权限
				if("auto".equals(applicationForm.getRequestionType())) {
					
				}else {
				//判断如果不是自动跑批,控制其跑批权限
					//机构判断
					List authInstList = this.getAuthInstList();
					//新增 2018-08-23 刘俊杰 判断机构权限
					tag = false;
					for (int j = 0; j < authInstList.size(); j++) {
						Organization org = (Organization) authInstList.get(j);
						if (batchRunTransInfo1.getINSTCODE().equals(org.getId())) {
							Organization orgs = (Organization) authInstList.get(0);
							System.out.println(orgs);
							batchRunTransInfo1.setINSTCODE(orgs.getId());
							tag = true;
						}
					}
					if(!tag) {
						customerId.add(batchRunTransInfo1.getCUSTOMER_ID());
						continue;
					}
					//end 2018-08-23
				}
				map.put("INSTCODE", batchRunTransInfo1.getINSTCODE());
				//税率还原
				String TAX_RATE=batchRunTransInfo1.getTAX_RATE();
				System.out.println(TAX_RATE);
				if(TAX_RATE.equals("s")||TAX_RATE.equals("S")){
					map.put("TAX_RATE", 0.06);
				}else if(TAX_RATE.equals("z")||TAX_RATE.equals("Z")){
					map.put("TAX_RATE", 0.00);
				}else if(TAX_RATE.equals("p")||TAX_RATE.equals("P")){
					map.put("TAX_RATE", 0.17);
				}else if(TAX_RATE.equals("n")||TAX_RATE.equals("N")){
					map.put("TAX_RATE", 0.03);
				}else if(TAX_RATE.equals("f")||TAX_RATE.equals("F")){
					map.put("TAX_RATE", 0.00);
				}else{
					map.put("TAX_RATE", 0.00);
				}
				map.put("CUSTOMER_ID", batchRunTransInfo1.getCUSTOMER_ID());
				map.put("AMT_CNY", batchRunTransInfo1.getAMT_CNY());
				map.put("TAX_AMT_CNY", batchRunTransInfo1.getTAX_AMT_CNY());
				map.put("INCOME_CNY", batchRunTransInfo1.getINCOME_CNY());
				map.put("TRANS_CURR", batchRunTransInfo1.getTRANS_CURR());
				map.put("SURTAX1_AMT_CNY", batchRunTransInfo1.getSURTAX1_AMT_CNY());
				map.put("SURTAX2_AMT_CNY", batchRunTransInfo1.getSURTAX2_AMT_CNY());
				map.put("SURTAX3_AMT_CNY", batchRunTransInfo1.getSURTAX3_AMT_CNY());
				map.put("SURTAX4_AMT_CNY", batchRunTransInfo1.getSURTAX4_AMT_CNY());
				map.put("FAPIAO_TYPE", batchRunTransInfo1.getFAPIAO_TYPE());
				map.put("VAT_RATE_CODE", batchRunTransInfo1.getVAT_RATE_CODE());
				map.put("INSTNAME", batchRunTransInfo1.getINSTNAME());
				map.put("CHERNUM", batchRunTransInfo1.getCHERNUM());
				map.put("REPNUM", batchRunTransInfo1.getREPNUM());
				map.put("TTMPRCNO", batchRunTransInfo1.getTTMPRCNO());
				map.put("FEETYP", batchRunTransInfo1.getFEETYP());
				map.put("BILLFREQ", batchRunTransInfo1.getBILLFREQ());
				map.put("POLYEAR", batchRunTransInfo1.getPOLYEAR());
				map.put("HISSDTE", batchRunTransInfo1.getHISSDTE());
				map.put("OCCDATE", batchRunTransInfo1.getOCCDATE());
				map.put("INSTFROM", batchRunTransInfo1.getINSTFROM());
				map.put("INSTTO", batchRunTransInfo1.getINSTTO());
				map.put("PREMTERM", batchRunTransInfo1.getPREMTERM());
				map.put("WITHDRAWYN", batchRunTransInfo1.getWITHDRAWYN());
				map.put("PLANLONGDESC", batchRunTransInfo1.getPLANLONGDESC());
				map.put("INSCOD", batchRunTransInfo1.getINSCOD());System.out.println(batchRunTransInfo1.getINSCOD());
				map.put("INSNAM", batchRunTransInfo1.getINSNAM());
				map.put("BUSINESSID", batchRunTransInfo1.getBUSINESSID());
				map.put("QDFLAG", batchRunTransInfo1.getQDFLAG());
				map.put("HESITATE_PERIOD", batchRunTransInfo1.getHESITATE_PERIOD());
				map.put("SYNCH_DATE", batchRunTransInfo1.getSYNCH_DATE());
				map.put("ISYK", batchRunTransInfo1.getISYK());
				map.put("CANCLE_STATE", batchRunTransInfo1.getCANCLE_STATE());
				map.put("REMARK", batchRunTransInfo1.getREMARK());
				map.put("DATASTATUS", 1);
				
				
				//判断是否是电票,团险,首期(契约)的数据,满足条件则执行电子发票开具
				if("2".equals(batchRunTransInfo1.getFAPIAO_TYPE()) 
						&& "1".equals(batchRunTransInfo1.getQDFLAG()) 
						&& "A".equals(batchRunTransInfo1.getFEETYP())) {
					//封装数据
					getTransInfoForINSCOD(batchRunTransInfo1.getCHERNUM(),batchRunTransInfo1.getCUSTOMER_ID(),true);
				}
				//判断是否是个险犹豫期退保,如果是,则修改vms_trans_info表中的对应个险的状态,使其不开票
				if("0".equals(batchRunTransInfo1.getQDFLAG())
						&& "I".equals(batchRunTransInfo1.getFEETYP())) {
					map.put("DATASTATUS", 88);
					batchRunService.updateTransInfoOfYouyuqi(map);
				}
				//将交易信息写入交易表中
				batchRunService.insertBatchRunTransInfo(map);
			}
			
			//客户信息遍历保存到map
			List<CustomerTemp> batchRunCustomerInfo=batchRunService.batchRunCustomerInfo(selectMap);
	        for(int i=0;i<batchRunCustomerInfo.size();i++){
	        	CustomerTemp customerTemp=batchRunCustomerInfo.get(i);
	        	//新增 2018-08-23 刘俊杰 判断机构权限
	        	tag = false;
	        	for(String str : customerId) {
	        		if(customerTemp.getCUSTOMER_ID().equals(str)) {
	        			tag = true;
	        		}
	        	}
	        	if(tag) continue;
	        	//end 2018-08-23
	        	Map map=new HashMap();
	        	map.put("ID", customerTemp.getID());
	        	map.put("CUSTOMER_ID", customerTemp.getCUSTOMER_ID());
	        	map.put("chernum", customerTemp.getChernum());
	        	map.put("CUSTOMER_CNAME", customerTemp.getCUSTOMER_CNAME());
	        	map.put("CUSTOMER_TAXNO", customerTemp.getCUSTOMER_TAXNO());
	        	map.put("CUSTOMER_ACCOUNT", customerTemp.getCUSTOMER_ACCOUNT());
	        	map.put("CUSTOMER_CBANK", customerTemp.getCUSTOMER_CBANK());
	        	map.put("CUSTOMER_PHONE", customerTemp.getCUSTOMER_PHONE());
	        	map.put("CUSTOMER_EMAIL", customerTemp.getCUSTOMER_EMAIL());
	        	map.put("CUSTOMER_ADDRESS", customerTemp.getCUSTOMER_ADDRESS());
	        	map.put("TAXPAYER_TYPE", customerTemp.getTAXPAYER_TYPE());
	        	map.put("FAPIAO_TYPE", customerTemp.getFAPIAO_TYPE());
	        	map.put("CUSTOMER_NATIONALITY", customerTemp.getCUSTOMER_NATIONALITY());
	        	map.put("CUSTOMER_ZIP_CODE", customerTemp.getCUSTOMER_ZIP_CODE());
	        	map.put("SYNCH_DATE", customerTemp.getSYNCH_DATE());
	        	
	        	try{
	        		batchRunService.deleteBatchRunCustomerInfo(map);
	        		batchRunService.insertBatchRunCustomerInfo(map);
	        		}
	        	catch(Exception e){
	        		System.out.println(e.getMessage());
	        	}
	        }

	        //开电子发票
			batchRunTimeOfElectron();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 新增
	 * 日期：2018-08-30
	 * 作者：刘俊杰
	 * 功能：对同一保单号不同险种的交易信息进行封装，合并开票
	 * @param chernum 保单号
	 * @param customerId 客户号
	 * @param tag 标识:true-团险电子发票  false-个险犹豫期发票
	 */
	public void getTransInfoForINSCOD(String chernum, String customerId, boolean tag) {
		List<TransInfoTemp> batchRunTransInfoINSList = null;
		List<CustomerTemp> batchRunCustomerInfoByID = null;
		Map map = new HashMap();
		map.put("chernum", chernum);
		map.put("customerId", customerId);
		if(tag) {  //判断是否是团险电子发票，团险电子发票因自动跑批时开具电子发票，此时还未写入应用表中，所以需要从中间表中查；
			batchRunTransInfoINSList=batchRunService.batchRunTransInfoOfINS(map);
			batchRunCustomerInfoByID=batchRunService.batchRunCustomerInfoOfINS(map);
		}else {  //而个险犹豫期的数据已经在应用表中，所以从应用表中查找数据
			batchRunTransInfoINSList=batchRunService.batchRunTransInfoOfINSForHesitate(map);
			batchRunCustomerInfoByID=batchRunService.batchRunCustomerInfoOfINSForHesitate(map);
		}
		CustomerTemp customerTemp=batchRunCustomerInfoByID.get(0);
		Map xmlmap = new HashMap();
		xmlmap.put("ID", customerTemp.getID());
		xmlmap.put("CUSTOMER_ID", customerTemp.getCUSTOMER_ID());
		xmlmap.put("chernum", customerTemp.getChernum());
		xmlmap.put("CUSTOMER_CNAME", customerTemp.getCUSTOMER_CNAME());
		xmlmap.put("CUSTOMER_TAXNO", customerTemp.getCUSTOMER_TAXNO());
		xmlmap.put("CUSTOMER_ACCOUNT", customerTemp.getCUSTOMER_ACCOUNT());
		xmlmap.put("CUSTOMER_CBANK", customerTemp.getCUSTOMER_CBANK());
		xmlmap.put("CUSTOMER_PHONE", customerTemp.getCUSTOMER_PHONE());
		xmlmap.put("CUSTOMER_EMAIL", customerTemp.getCUSTOMER_EMAIL());
		xmlmap.put("CUSTOMER_ADDRESS", customerTemp.getCUSTOMER_ADDRESS());
		xmlmap.put("TAXPAYER_TYPE", customerTemp.getTAXPAYER_TYPE());
		xmlmap.put("FAPIAO_TYPE", customerTemp.getFAPIAO_TYPE());
		xmlmap.put("CUSTOMER_NATIONALITY", customerTemp.getCUSTOMER_NATIONALITY());
		xmlmap.put("CUSTOMER_ZIP_CODE", customerTemp.getCUSTOMER_ZIP_CODE());
		xmlmap.put("SYNCH_DATE", customerTemp.getSYNCH_DATE());
		batchOfElectron(xmlmap,batchRunTransInfoINSList);
	}
	
	//按照同一保单不同险种对交易信息进行封装到list集合，电子发票开票时进行合并开票
	private List<List> list = new ArrayList<List>();
	private void batchOfElectron(Map xmlmap,List<TransInfoTemp> batchRunTransInfoINSList) {
		List newlist = new ArrayList();
		//数据验重
		boolean tag = true;
		for(List nlist : list) {
			List<TransInfoTemp> ntempList = (List<TransInfoTemp>) nlist.get(0);
			for(TransInfoTemp temp1 : ntempList) {
				for(TransInfoTemp temp : batchRunTransInfoINSList) {
					if(temp.getCHERNUM().equals(temp1.getCHERNUM())) {
						tag = false;break;
					}
				}
				if(!tag) {
					break;
				}
			}
			if(!tag) {
				break;
			}
		}
		//当数据不重复时，将数据存入集合
		if(tag) {
			newlist.add(batchRunTransInfoINSList);
			newlist.add(xmlmap);
			list.add(newlist);
		}
	}
	
	/**
	 * 新增
	 * 日期：2018-08-20
	 * 作者：刘俊杰
	 * 功能：调用税控接口，开具电子发票; 开具时间较长，防止超时，开启新线程
	 * @throws ParseException
	 */
	public void batchRunTimeOfElectron(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					ApplicationContext applicationContext = SpringContextUtil.getApplicationContext();
					VmsElectronWebServiceImp vmsElectronWebService =  (VmsElectronWebServiceImp) applicationContext.getBean("vmsElectronWebService");
					for(int i = 0; i<list.size(); i++) {
						List obj = (List)list.get(i);
						List<TransInfoTemp> transInfoTemp = (List<TransInfoTemp>) obj.get(0);
						Map xmlmap = (Map) obj.get(1);
						result = vmsElectronWebService.transService(xmlmap,transInfoTemp);
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	
}
