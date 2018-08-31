package com.cjit.vms.taxdisk.service.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.DocumentHelper;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.stock.entity.BillDistribution;
import com.cjit.vms.stock.entity.LostRecycle;
import com.cjit.vms.taxdisk.service.BillInterfaceService;
import com.cjit.vms.taxdisk.servlet.model.BillCancel;
import com.cjit.vms.taxdisk.servlet.model.BillIssue;
import com.cjit.vms.taxdisk.servlet.model.BillPrint;
import com.cjit.vms.taxdisk.servlet.model.CurrentBillNo;
import com.cjit.vms.taxdisk.servlet.model.ParamSet;
import com.cjit.vms.taxdisk.servlet.model.Product;
import com.cjit.vms.taxdisk.servlet.model.TaxInformationQuery;
import com.cjit.vms.taxdisk.servlet.model.parseXml.BaseReturnXml;
import com.cjit.vms.taxdisk.servlet.model.parseXml.BillCancelReturnXml;
import com.cjit.vms.taxdisk.servlet.model.parseXml.BillIssueReturnXml;
import com.cjit.vms.taxdisk.servlet.model.parseXml.BillPrintReturnXml;
import com.cjit.vms.taxdisk.servlet.model.parseXml.CurrentBillNoReturnXml;
import com.cjit.vms.taxdisk.servlet.model.parseXml.ParamSetReturnXml;
import com.cjit.vms.taxdisk.servlet.model.parseXml.TaxKeyQueryReturnXml;
import com.cjit.vms.taxdisk.servlet.util.TaxSelvetUtil;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.BillItemInfo;
import com.cjit.vms.taxdisk.single.model.busiDisk.VmsTaxKeyInfo;
import com.cjit.vms.taxdisk.single.service.BillCancelDiskAssitService;
import com.cjit.vms.taxdisk.single.service.BillIssueDiskAssitService;
import com.cjit.vms.taxdisk.single.service.BillPrintDiskAssistService;
import com.cjit.vms.taxdisk.single.service.PageTaxInvoiceDiskAssitService;
import com.cjit.vms.taxdisk.single.service.TaxDiskInfoQueryService;
import com.cjit.vms.taxdisk.tools.AjaxReturn;
import com.cjit.vms.taxdisk.tools.Message;
import com.cjit.vms.trans.service.BillIssueService;

public class BWServletBillInterfaceServiceImpl extends GenericServiceImpl implements BillInterfaceService {
	private BillIssueDiskAssitService billIssueDiskAssitService;
	private TaxDiskInfoQueryService taxDiskInfoQueryService;
	private BillPrintDiskAssistService billPrintDiskAssistService;
	private BillCancelDiskAssitService billCancelDiskAssitService;
	private PageTaxInvoiceDiskAssitService pageTaxInvoiceDiskAssitService;
	private BillIssueService billIssueService;
	Logger log = Logger.getLogger(BWServletBillInterfaceServiceImpl.class);
	/**
	 * aa
	 */
	public static final String INTERFACE_TYPE = "bw_servlet";
	
	/*
	 * (non-Javadoc)
	 * 
	 * @ 创建参数设置 信息
	 */
	@Override
	public AjaxReturn createRegistInfo(Map params) throws Exception {

		AjaxReturn message = null;
		try {

			Map map = new HashMap();
			List list = find("findTaxDiskapwd", map);
			String pwd = "";
			if (list.size() == 1) {
				pwd = (String) list.get(0);
				VmsTaxKeyInfo taxKeyInfo = taxDiskInfoQueryService.findVmstaxKeyInfo((String) params.get("diskNo"));//根据纳税人识别号
				//VmsTaxKeyInfo taxKeyInfo = taxDiskInfoQueryService.findVmstaxKeyInfo("45010319740715104701");

				String StringXml = new ParamSet(pwd).createParamSetXml(taxKeyInfo);
				System.out.println(StringXml);
				log.info("参数设置Xml" + StringXml);
				Map mapm = new HashMap();
				message = new AjaxReturn(true);
				mapm.put("StringXml", StringXml);
				message.setAttributes(mapm);
			} else {
				message = new AjaxReturn(false, Message.pwd_no);
				return message;
			}
		} catch (Exception e) {
			message = new AjaxReturn(false, Message.system_exception_bill_param_Xml_error);
		}
		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @验证参数设置
	 */
	@Override
	public AjaxReturn checkRegistInfo(Map params) throws Exception {
		AjaxReturn message = null;
		try {
			log.info("参数设置返回" + (String) params.get("StringXml"));
			ParamSetReturnXml parammXml = new ParamSetReturnXml((String) params.get("StringXml"));
			System.out.println((String) params.get("StringXml"));
			if (parammXml.getReturncode().equals(BaseReturnXml.return_success)) {
				message = new AjaxReturn(true);
			} else {
				message = new AjaxReturn(false, parammXml.getReturnmsg());
			}

		} catch (Exception e) {
			message = new AjaxReturn(false, Message.parse_bill_param_set_xml_error);
		}
		return message;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public AjaxReturn createTaxInfo(Map params) throws Exception {
		AjaxReturn message = null;
		try {
			Map map = new HashMap();
			List list = find("findTaxDiskapwd", map);
			String pwd = "";
			if (list.size() == 1) {
				pwd = (String) list.get(0);
			} else {
				message = new AjaxReturn(false, Message.pwd_no);
				return message;
			}
			TaxInformationQuery taxInfo = new TaxInformationQuery(pwd);
			String StringXml = taxInfo.createTaxKeyQueryXml(); //税控钥匙信息查询报文
			System.out.println(StringXml);
			log.info("税控服务器查询Xml" + StringXml);
			map.put("StringXml", StringXml);
			message = new AjaxReturn(true);
			message.setAttributes(map);
		} catch (Exception e) {
			message = new AjaxReturn(false, Message.system_exception_tax_key_Xml_error);
			return message;

		}
		System.out.println("message://"+message.getAttributes().get("StringXml"));
		return message;
	}

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	@Override
	public AjaxReturn checkTaxInfo(Map params) throws Exception {
		AjaxReturn message = null;
		try {
			try {
				// UBaseInst inst =
				// taxDiskInfoQueryService.findTaxDiskInfoByinstID((String)params.get("instCode"));
				// if (inst.getTaxperNumber().isEmpty()) {
				// message=new AjaxReturn(false, Message.blank_inst_tax_no);
				// return message;
				/// }
				log.info("税控信息查询返回xml" + (String) params.get("StringXml"));
				TaxKeyQueryReturnXml taxDiskInfoQRXml = new TaxKeyQueryReturnXml((String) params.get("StringXml"));
				System.out.println((String) params.get("StringXml"));
				
				if (taxDiskInfoQRXml.getReturncode().equals(BaseReturnXml.return_success)) { //false
					message = new AjaxReturn(true);
					Map map = new HashMap();
					map.put("diskNo", taxDiskInfoQRXml.getTaxNo());//纳税人识别号
					message.setAttributes(map);

					/// if
					/// (!taxDiskInfoQRXml.getTaxNo().equals(inst.getTaxperNumber()))
					/// {
					// message=new AjaxReturn(false,
					/// Message.tax_no_and_inst_tax_no_not);
					// return message;
					// }
				} else {
					message = new AjaxReturn(false, taxDiskInfoQRXml.getReturnmsg());
					System.out.println(taxDiskInfoQRXml.getReturnmsg()+",/////验证失败/////");

					return message;
				}

			} catch (Exception e) {
				message = new AjaxReturn(false, Message.parse_bill_tax_key_xml_error);
				return message;
			}

		} catch (Exception e) {

		}
		
		System.out.println(message+"kkkkk");

		return message;
		
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	/*public AjaxReturn createCurBillNoInfo(Map params) throws Exception {
		AjaxReturn message = null;
		Map map = new HashMap();
		try {
			
			VmsTaxKeyInfo taxKeyInfo = taxDiskInfoQueryService.findVmstaxKeyInfo((String) params.get("diskNo"));
			
			//======================================
			String taxkeyno=taxKeyInfo.getTaxKeyNo();
			List<LostRecycle> lostRecyclest=pageTaxInvoiceDiskAssitService.findLostRecycleKPJY("id");
		    List<BillDistribution> list=pageTaxInvoiceDiskAssitService.findBillDistribution(taxkeyno);
		    List<BillDistribution> jList=new ArrayList<BillDistribution>();
			  int zpcount=0;
			  int pucount=0;
		      if(list!=null&&list.size()>0){
		    	for (BillDistribution billDistribution : list) {
					String jString =billDistribution.getJsEnter();
					if("0".equals(jString)){
						jList.add(billDistribution);
					}
				}
		    	if(jList.size()==0){
		    		if(lostRecyclest.size()==0){
		    			for(int i=0;i<list.size();i++){
		    				BillDistribution billDistribution=list.get(i) ;
		    				int count=billDistribution.getSyCount();
		    				if( "0".equals(billDistribution.getBillType())){
		    					zpcount=zpcount+count;
		    				}
		    				if("1".equals(billDistribution.getBillType())){
		    					pucount=pucount+count;
		    				}
		    			}
		    			if(zpcount<=5&&zpcount<=5){
		    				map.put("massage", "当前税控钥匙专票和普票数量不足5张，请及时购买！");
		    			}else{
		    				if(pucount<=5){
		    					map.put("massage", "当前税控钥匙普票数量为"+pucount+"张不足5张，请及时购买！");
		    				}else if(zpcount<=5){
		    					map.put("massage", "当前税控钥匙专票数量为"+zpcount+"张不足5张，请及时购买！");
		    				}
		    			}
		    			message = new AjaxReturn(true);
		    			message.setAttributes(map);
		    			CurrentBillNo bill = new CurrentBillNo((String) params.get("fapiaoType"), taxKeyInfo);
		    			String StringXml = bill.createCurrentBillXml();
		    			System.out.println(StringXml);
		    			log.info("查询当前发票号码Xml" + StringXml);
		    			map.put("StringXml", StringXml);
		    		}else{
			    		message = new AjaxReturn(false,"空白发票作废未处理请联系管理员处理！");
			    		message.setAttributes(map);
			    		return message;
		    		}
		    	}else{
		    		map.put("massage", "qiquren");
		    		message = new AjaxReturn(false,"发票统计有未确认发票信息,不可开票！需要开票,请先执行确认操作！");
		    		message.setAttributes(map);
		    		return message;
		    	}
		    } 
		} catch (Exception e) {
			message = new AjaxReturn(false, Message.system_exception_bill_cur_bill_no_Xml_error);
			return message;
		}
		message = new AjaxReturn(true);
		message.setAttributes(map);
		return message;
	}
*/
	
	public AjaxReturn createCurBillNoInfo(Map params) throws Exception {
		AjaxReturn message = null;
		Map map = new HashMap();
		try {
			//2018-03-07国富更改
			System.err.println("createCurBillNoInfo++++++cehng11");
			BillInfo billInfo = billIssueDiskAssitService.findBillInfoById((String) params.get("billId"));
			System.out.println(billInfo.getTaxno());
			VmsTaxKeyInfo taxKeyInfo = taxDiskInfoQueryService.findVmstaxKeyInfo(billInfo.getTaxno());
			System.err.println(taxKeyInfo);
			System.err.println((String) params.get("fapiaoType"));
			System.err.println((String) params.get("billId"));
			System.err.println("createCurBillNoInfo++++++cehng22");
			//VmsTaxKeyInfo taxKeyInfo = taxDiskInfoQueryService.findVmstaxKeyInfo((String) params.get("diskNo"));
			CurrentBillNo bill = new CurrentBillNo((String) params.get("fapiaoType"), taxKeyInfo);
			System.err.println("createCurBillNoInfo++++++cheng33");
			String StringXml = bill.createCurrentBillXml();
			System.out.println(StringXml+",/////查看有无返回/////");
			System.out.println("查询当前发票号码Xml" + StringXml);
			log.info("查询当前发票号码Xml" + StringXml);
			map.put("StringXml", StringXml);
		    		
		} catch (Exception e) {
			message = new AjaxReturn(false, Message.system_exception_bill_cur_bill_no_Xml_error);
			return message;
		}
		message = new AjaxReturn(true);
		message.setAttributes(map);
		return message;
	}
	
	
	@Override
	public AjaxReturn checkCurBillNoInfo(Map params) throws Exception {
		AjaxReturn message = null;

		try {
			log.info("当前发票号码返回xml" + (String) params.get("StringXml"));
			System.out.println((String) params.get("StringXml"));
			CurrentBillNoReturnXml bill = new CurrentBillNoReturnXml((String) params.get("StringXml"));
			if (bill.getReturncode().equals(BaseReturnXml.return_success)) {
				message = new AjaxReturn(true);
			} else {
				message = new AjaxReturn(false, bill.getReturnmsg());
			}
		} catch (Exception e) {
			message = new AjaxReturn(false, Message.parse_bill_cancel_Xcur_bill_no_xml_error);
		}

		return message;
	}

	@Override
	public AjaxReturn createBillissue(Map params) throws Exception {
		AjaxReturn message = null;
		BillIssue bill = null;
		try {
			System.out.println("createBillissue++++++cheng");
			String billId = (String) params.get("billId");
			BillInfo billInfo = billIssueDiskAssitService.findBillInfoById(billId);
			List<BillItemInfo> list = billIssueDiskAssitService.findBillItemByBillIdDisk(billId);
			List<Product> goodsList = new ArrayList<Product>();
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					BillItemInfo billItem = list.get(i);
					Product goods = new Product(billItem);
					goodsList.add(goods);
				}
			}
			// 税控钥匙新加
			//VmsTaxKeyInfo taxKeyInfo = taxDiskInfoQueryService.findVmstaxKeyInfo((String) params.get("diskNo"));
			//2018-03-07国富更改
			//获取服务器IP、端口号和开票终端标识
			VmsTaxKeyInfo taxKeyInfo = taxDiskInfoQueryService.findVmstaxKeyInfo(billInfo.getTaxno());
			//20180829   程     参考国富   判断蓝票，红票
			if (billInfo.getSumAmt().contains("-")) {
				System.out.println("开具红票。。。。。。。。。");
				bill = new BillIssue(billInfo, goodsList, TaxSelvetUtil.Issue_Bill_Type_1, "", taxKeyInfo);
			}else{
				System.out.println("开具蓝票。。。。。。。。。");
				bill = new BillIssue(billInfo, goodsList, TaxSelvetUtil.Issue_Bill_Type_0, "", taxKeyInfo);
				
			}
			
			
//			bill = new BillIssue(billInfo, goodsList, TaxSelvetUtil.Issue_Bill_Type_0, "", taxKeyInfo);
			
			String sIp = taxKeyInfo.getIpAddress();
			String sPort = taxKeyInfo.getServletPort();
			Map map = new HashMap();
			map.put("sIp",sIp);
			map.put("sPort", sPort);
			
			//开票终端标识
			String bilTerminalFlag = taxKeyInfo.getBilTerminalFlag();
			log.info("开具xml" + bill.createBillIssueXml());
			message = new AjaxReturn(true);
			String str = bill.createBillIssueXml().replace("UTF-8", "GBK");
			//map.put("StringXml", bill.createBillIssueXml());
			map.put("StringXml", str);
			
			message.setAttributes(map);
			System.out.println("开具xml++++cheng :"+str);
			System.out.println("createBillissue++++++cheng111");
		} catch (Exception e) {
		}
		return message;
	}

	@Override
	public AjaxReturn updateBillIssueResult(Map params) throws Exception {
		
		AjaxReturn message = null;
		BillIssueReturnXml bill = null;
		
		try {
			System.out.println((String) params.get("StringXml"));
			log.info("开具xml返回" + (String) params.get("StringXml"));
			bill = new BillIssueReturnXml((String) params.get("StringXml"));
			System.out.println(bill.getBillCode()+","+bill.getBillNo()+","+bill.getBillIssueDate()+","+bill.getReturnmsg()+","+bill.getReturncode());
         
		} catch (Exception e) {
			String getMessage = parseXML((String) params.get("StringXml"));
			if (!"".equals(getMessage)) {
				message = new AjaxReturn(false, getMessage);
			} else {
				message = new AjaxReturn(false, Message.parse_bill_Issue_info_query_erroe);
			}
			return message;
		}
		String falg = bill.getId();
		
		System.err.println((String) params.get("flag1")+"    10000000000000000000001");;
		if (falg.equals(TaxSelvetUtil.id_Issue)) {
			/*message = billIssueDiskAssitService.updateBillIssueResult(bill.getBillCode(), bill.getBillNo(),
					(String) params.get("billId"), (String) params.get("diskNo"), (String) params.get("diskNo"),
					(String) params.get("userId"), bill.getBillIssueDate(), bill.getReturnmsg(),
					bill.getReturncode().equals(BaseReturnXml.return_success));*/
			message = billIssueDiskAssitService.updateBillIssueResult(bill.getBillCode(), bill.getBillNo(),
					(String) params.get("billId"),(String) params.get("userId"), bill.getBillIssueDate(), bill.getReturnmsg(),
					bill.getReturncode().equals(BaseReturnXml.return_success),(String) params.get("flag1"));
			billIssueDiskAssitService.updateBillStatistics(bill.getBillCode(), bill.getBillNo());
		} else {
			message = new AjaxReturn(true);
		}
		return message;
	}

	public String parseXML(String xmlMessage) {
		StringReader read = new StringReader(xmlMessage);
		InputSource source = new InputSource(read);// 利用字节流创建新的输入源
		String returnmsg = "";
		SAXBuilder sb = new SAXBuilder();
		try {
			Document doc = sb.build(source);
			Element root = doc.getRootElement();
			Element body = root.getChild("body");
			returnmsg = body.getChildText("returnmsg");
			returnmsg = "税控系统提示:" + returnmsg;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnmsg;
	}

	@Override
	public AjaxReturn createBillPrint(Map params) throws Exception {
		AjaxReturn message = null;
		String StringXml = null;
		try {

			BillInfo billInfo = billIssueDiskAssitService.findBillInfoById((String) params.get("billId"));
			//VmsTaxKeyInfo taxKeyInfo = taxDiskInfoQueryService.findVmstaxKeyInfo((String) params.get("diskNo"));
			VmsTaxKeyInfo taxKeyInfo = taxDiskInfoQueryService.findVmstaxKeyInfo(billInfo.getTaxno());
			BillPrint billPrint = new BillPrint(billInfo, taxKeyInfo);
			String sIp = taxKeyInfo.getIpAddress();
			String sPort = taxKeyInfo.getServletPort();
			Map map = new HashMap();
			map.put("sIp",sIp);
			map.put("sPort", sPort);
			
			StringXml = billPrint.createPrintBillXml("发票打印").replace("UTF-8", "GBK");
			System.out.println(StringXml);
			log.info("发票打印xml" + StringXml);
			message = new AjaxReturn(true);
			map.put("StringXml", StringXml);
			message.setAttributes(map);
		} catch (Exception e) {
			message = new AjaxReturn(false, Message.system_exception_bill_print_Xml_error);
			return message;
		}
		return message;
	}

	@Override
	public AjaxReturn updateBillPrintResult(Map params) throws Exception {
		AjaxReturn message = null;
		BillPrintReturnXml billPrint = null;
		try {
			System.out.println((String) params.get("StringXml"));
			log.info("打印信息返回" + (String) params.get("StringXml"));
			billPrint = new BillPrintReturnXml((String) params.get("StringXml"));

		} catch (Exception e) {
			message = new AjaxReturn(false, Message.parse_print_Xml_error);

			return message;
		}
		billPrintDiskAssistService.updateBillStatisticsCount((String) params.get("billId"));
		return billPrintDiskAssistService.updateBillPrintResult((String) params.get("billId"), billPrint.getReturnmsg(),
				billPrint.getReturncode().equals(BaseReturnXml.return_success));

	}

	@Override
	public AjaxReturn createBillCancel(Map params) throws Exception {
		AjaxReturn message = null;
		try {
			String billIds = (String) params.get("billId");
			/**
			 * 之前billIds传的是多个billId以逗号分割。现在改为一次只作废一张，billIds只有一个
			 * 为了减少代码的改动量，后面还是沿用原有代码
			 */
			List<String> list = Arrays.asList(billIds.split(","));
			List<BillInfo> bill = billIssueDiskAssitService.findBillInfoById(list);

			//VmsTaxKeyInfo taxKeyInfo = taxDiskInfoQueryService.findVmstaxKeyInfo((String) params.get("diskNo"));
			VmsTaxKeyInfo taxKeyInfo = taxDiskInfoQueryService.findVmstaxKeyInfo(bill.get(0).getTaxno());
			String sIp = taxKeyInfo.getIpAddress();
			String sPort = taxKeyInfo.getServletPort();
			
			Map map = new HashMap();
			map.put("sIp",sIp);
			map.put("sPort", sPort);
			String stringXml="";
			for (BillInfo billInfo : bill) {
				BillCancel billCancel = new BillCancel(billInfo, billInfo.getFapiaoType(),
						(String) params.get("userId"), TaxSelvetUtil.bill_cancel_Typech, taxKeyInfo);
				stringXml =billCancel.createBillCancelXml((String) params.get("flag")).replace("UTF-8", "GBK");
				break;
			}
			message = new AjaxReturn(true);
			log.info("发票作废Xml" + stringXml);
			//Map<String, Object> map = new HashMap<String, Object>();
			map.put("StringXml", stringXml);
			message.setAttributes(map);

		} catch (Exception e) {
			message = new AjaxReturn(false, Message.system_exception_bill_cancel_Xml_error);
			return message;
		}
		return message;
	}

	@Override
	public AjaxReturn updateBillCancelResult(Map params) throws Exception {
		AjaxReturn message = null;
		
		try {
			log.info("发票作废返回" + (String) params.get("StringXml"));
			String stringXML=(String)params.get("StringXml");
			
			List<String> list=Arrays.asList(stringXML.split("#"));
			List<BillCancelReturnXml> billXmls=new ArrayList<BillCancelReturnXml>();
			for (String xml : list) {
				billXmls.add(new BillCancelReturnXml(xml));
			}
			for (BillCancelReturnXml billcancel : billXmls) {
				if (!BaseReturnXml.return_success.equals(billcancel.getReturncode())) {
					message = new AjaxReturn(false, "发票作废失败");
					return message;
				}
			}
			String idString=(String)params.get("billId");
			List<String> billId=Arrays.asList(idString.split(","));
			AjaxReturn ajaxReturn = billCancelDiskAssitService.updateBillCancelResult(billId,
					billXmls, true);
			for (String str : billId) {
				billCancelDiskAssitService.updateBillStatisticsCount(str);
			}
			return ajaxReturn;
		} catch (Exception e) {
			message = new AjaxReturn(false, Message.parse_bill_cancel_Xml_error);
			return message;
		}
	}
	
	
	@Override
	public AjaxReturn createStockInfo(Map params) throws Exception {
		AjaxReturn message = null;
		return message;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @ 税控信息基本查询
	 */
	@Override
	public AjaxReturn createStockIssue(Map params) throws Exception {
		AjaxReturn message = null;
		try {

		} catch (Exception e) {

		}
		return message;
	}

	@Override
	public AjaxReturn createStockRecover(Map params) throws Exception {
		AjaxReturn message = null;
		return message;
	}

	@Override
	public AjaxReturn createTaxItemInfo(Map params) throws Exception {
		AjaxReturn message = null;
		return message;
	}

	@Override
	public AjaxReturn createTaxMonitor(Map params) throws Exception {
		AjaxReturn message = null;
		return message;
	}

	@Override
	public AjaxReturn saveStockInfo(Map params) throws Exception {
		AjaxReturn message = null;
		return message;
	}

	@Override
	public AjaxReturn saveStockIssue(Map params) throws Exception {
		AjaxReturn message = null;
		return message;
	}

	@Override
	public AjaxReturn saveStockRecover(Map params) throws Exception {
		AjaxReturn message = null;
		return message;
	}

	@Override
	public AjaxReturn saveTaxInfo(Map params) throws Exception {
		AjaxReturn message = null;
		return message;
	}

	@Override
	public AjaxReturn saveTaxItemInfo(Map params) throws Exception {
		AjaxReturn message = null;
		return message;
	}

	@Override
	public AjaxReturn saveTaxMonitor(Map params) throws Exception {
		AjaxReturn message = null;
		return message;
	}

	public BillIssueDiskAssitService getBillIssueDiskAssitService() {
		return billIssueDiskAssitService;
	}

	public TaxDiskInfoQueryService getTaxDiskInfoQueryService() {
		return taxDiskInfoQueryService;
	}

	public BillPrintDiskAssistService getBillPrintDiskAssistService() {
		return billPrintDiskAssistService;
	}

	public BillCancelDiskAssitService getBillCancelDiskAssitService() {
		return billCancelDiskAssitService;
	}

	public PageTaxInvoiceDiskAssitService getPageTaxInvoiceDiskAssitService() {
		return pageTaxInvoiceDiskAssitService;
	}

	public void setBillIssueDiskAssitService(BillIssueDiskAssitService billIssueDiskAssitService) {
		this.billIssueDiskAssitService = billIssueDiskAssitService;
	}

	public void setTaxDiskInfoQueryService(TaxDiskInfoQueryService taxDiskInfoQueryService) {
		this.taxDiskInfoQueryService = taxDiskInfoQueryService;
	}

	public void setBillPrintDiskAssistService(BillPrintDiskAssistService billPrintDiskAssistService) {
		this.billPrintDiskAssistService = billPrintDiskAssistService;
	}

	public void setBillCancelDiskAssitService(BillCancelDiskAssitService billCancelDiskAssitService) {
		this.billCancelDiskAssitService = billCancelDiskAssitService;
	}

	public void setPageTaxInvoiceDiskAssitService(PageTaxInvoiceDiskAssitService pageTaxInvoiceDiskAssitService) {
		this.pageTaxInvoiceDiskAssitService = pageTaxInvoiceDiskAssitService;
	}

	

}

