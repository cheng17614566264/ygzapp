package com.cjit.vms.taxdisk.servlet.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.baiwang.utility.encrypt.MyAES;
import com.cjit.vms.taxServer.util.TaxSelvetUtil;
import com.cjit.vms.taxdisk.servlet.model.Product;
import com.cjit.vms.trans.model.createBill.BillInfo;
import com.cjit.vms.trans.service.impl.VmsCommonServiceImpl;
import com.cjit.ws.common.utils.JsonUtils;
import com.cjit.ws.common.utils.RequestUtils;
import com.cjit.ws.common.utils.Utils;

@SuppressWarnings("all")
public class ElectronicsIssue {

	/**
	 * 发票请求流水号
	 */
	private static final String invoice_flow_number = "FPQQLSH";
	/**
	 * 开票类型
	 */
	private static final String issue_bill_tppe_ch = "KPLX";
	/**
	 * 编码表版本号
	 */
	private static final String code_table_version = "BMB_BBH";
	/**
	 * 征税方式
	 */
	private static final String taxation_mode = "ZSFS";
	/**
	 * 销售方纳税人识别号
	 */
	private static final String sale_taxpayer_no = "XSF_NSRSBH";
	/**
	 * 销售方名称
	 */
	private static final String sale_name = "XSF_MC";
	/**
	 * 销售方地址、电话
	 */
	private static final String sale_address_tel = "XSF_DZDH";
	/**
	 * 销售方银行账号
	 */
	private static final String sale_bank_no = "XSF_YHZH";
	/**
	 * 购买方纳税人识别号
	 */
	private static final String buy_taxpayer_no = "GMF_NSRSBH";
	/**
	 * 购买方名称
	 */
	private static final String buy_name = "GMF_MC";
	/**
	 * 购买方地址、电话
	 */
	private static final String buy_address_tel = "GMF_DZDH";
	/**
	 * 购买方银行账号
	 */
	private static final String buy_bank_no = "GMF_YHZH";
	/**
	 * 购买方手机号
	 */
	private static final String buy_phone_no = "GMF_SJH";
	/**
	 * 购买方电子邮箱
	 */
	private static final String buy_email = "GMF_DZYX";
	/**
	 * 发票通账户
	 */
	private static final String invoice_through_account = "FPT_ZH";
	/**
	 * 微信
	 */
	private static final String we_chat = "WX_OPENID";
	/**
	 * 开票人
	 */
	private static final String invoice_open = "KPR";
	/**
	 * 收款人
	 */
	private static final String collect_money = "SKR";
	/**
	 * 复核人
	 */
	private static final String to_examine = "FHR";
	/**
	 * 原发票代码
	 */
	private static final String old_fapiaocode = "YFP_DM";
	/**
	 * 原发票号码
	 */
	private static final String old_fapiaono = "YFP_HM";
	/**
	 * 价税合计
	 */
	private static final String total_valorem = "JSHJ";
	/**
	 * 合计金额
	 */
	private static final String total_money_amount = "HJJE";
	/**
	 * 合计税额
	 */
	private static final String total_tax_amount = "HJSE";
	/**
	 * 扣除税
	 */
	private static final String taz_deductible = "KCS";
	/**
	 * 备注
	 */
	private static final String remarks = "BZ";
	/**
	 * 行业类型
	 */
	private static final String industry_type = "HYLX";
	/**
	 * 微信用于预制卡券的唯一识别 ID
	 */
	private static final String wx_order_id = "WX_ORDER_ID";
	/**
	 * 商户所属微信公众号APP ID或发票通 APP ID
	 */
	private static final String wx_app_id = "WX_APP_ID";
	/**
	 * 支付宝UID
	 */
	private static final String zfb_uid = "ZFB_UID";
	/**
	 * 特殊票种标识
	 */
	private static final String special_invoice_id = "TSPZ";
	/**
	 * 全局唯一订单
	 */
	private static final String global_unique_order = "QJ_ORDER_ID";
	/**
	 * 发票行性质
	 */
	private static final String invoice_nature = "FPHXZ";
	/**
	 * 商品编码
	 */
	private static final String goods_code = "SPBM";
	/**
	 * 自行编码
	 */
	private static final String self_code = "ZXBM";
	/**
	 * 优惠政策标识
	 */
	private static final String policy_logo = "YHZCBS";
	/**
	 * 零税率标识
	 */
	private static final String tax_rate_logo = "LSLBS";
	/**
	 * 增值税特殊管理
	 */
	private static final String special_management = "ZZSTSGL";
	/**
	 * 项目名称
	 */
	private static final String entry_name = "XMMC";
	/**
	 * 规格型号
	 */
	private static final String specification_model = "GGXH";
	/**
	 * 单位
	 */
	private static final String unit = "DW";
	/**
	 * 项目数量
	 */
	private static final String entry_num = "XMSL";
	/**
	 * 项目单价
	 */
	private static final String entry_price = "XMDJ";
	/**
	 * 项目金额
	 */
	private static final String entry_money = "XMJE";
	/**
	 * 税率
	 */
	private static final String tax_rate = "SL";
	/**
	 * 税额
	 */
	private static final String tax_money = "SE";
	/**
	 * 纳税人识别号
	 */
	private static final String taxpayer_no = "NSRSBH";
	/**
	 * 发票代码
	 */
	private static final String fapiaoCode = "FP_DM";
	/**
	 * 发票号码
	 */
	private static final String fapiaoNo = "FP_HM";

	/**
	 * 电票开具税控返回代码
	 * */
	public static final String deal_with_success = "0";
	public static final String request_timeout = "9991";
	public static final String request_exception = "9992";
	public static final String request_runtime_exception = "9995";
	public static final String message_format_error = "9996";
	public static final String incorrect_data = "9997";
	public static final String deal_with_fail = "9999";
	public static final String deal_with_success_ch = "业务处理成功";
	public static final String request_timeout_ch = "请求税控超时";
	public static final String request_exception_ch = "请求税控异常";
	public static final String request_runtime_exception_ch = "开票过程中运行异常";
	public static final String message_format_error_ch = "xml报文格式错误";
	public static final String incorrect_data_ch = "数据不正确";
	public static final String deal_with_fail_ch = "业务处理失败";

	public static final String Issueheader = "REQUEST_COMMON_FPKJ"; // 电票开具
	public static final String Issuecommon = "COMMON_FPKJ_XMXXS";
	public static final String queryheader = "REQUEST_COMMON_FPCX"; // 电票查询
	public static final String downloadheader = "REQUEST_COMMON_FPXZDZCX"; // 电票下载地址查询

	public static final String billIssueFileXml = "发票开具.xml";

	public static String getInvoiceFlowNumber() {
		return invoice_flow_number;
	}

	public static String getIssueBillTppeCh() {
		return issue_bill_tppe_ch;
	}

	public static String getCodeTableVersion() {
		return code_table_version;
	}

	public static String getTaxationMode() {
		return taxation_mode;
	}

	public static String getSaleTaxpayerNo() {
		return sale_taxpayer_no;
	}

	public static String getSaleName() {
		return sale_name;
	}

	public static String getSaleAddressTel() {
		return sale_address_tel;
	}

	public static String getSaleBankNo() {
		return sale_bank_no;
	}

	public static String getBuyTaxpayerNo() {
		return buy_taxpayer_no;
	}

	public static String getBuyName() {
		return buy_name;
	}

	public static String getBuyAddressTel() {
		return buy_address_tel;
	}

	public static String getBuyBankNo() {
		return buy_bank_no;
	}

	public static String getBuyPhoneNo() {
		return buy_phone_no;
	}

	public static String getBuyEmail() {
		return buy_email;
	}

	public static String getInvoiceThroughAccount() {
		return invoice_through_account;
	}

	public static String getWeChat() {
		return we_chat;
	}

	public static String getInvoiceOpen() {
		return invoice_open;
	}

	public static String getCollectMoney() {
		return collect_money;
	}

	public static String getToExamine() {
		return to_examine;
	}

	public static String getOldFapiaocode() {
		return old_fapiaocode;
	}

	public static String getOldFapiaono() {
		return old_fapiaono;
	}

	public static String getTotalValorem() {
		return total_valorem;
	}

	public static String getTotalMoneyAmount() {
		return total_money_amount;
	}

	public static String getTotalTaxAmount() {
		return total_tax_amount;
	}

	public static String getTazDeductible() {
		return taz_deductible;
	}

	public static String getRemarks() {
		return remarks;
	}

	public static String getIndustryType() {
		return industry_type;
	}

	public static String getWxOrderId() {
		return wx_order_id;
	}

	public static String getWxAppId() {
		return wx_app_id;
	}

	public static String getZfbUid() {
		return zfb_uid;
	}

	public static String getSpecialInvoiceId() {
		return special_invoice_id;
	}

	public static String getGlobalUniqueOrder() {
		return global_unique_order;
	}

	public static String getInvoiceNature() {
		return invoice_nature;
	}

	public static String getGoodsCode() {
		return goods_code;
	}

	public static String getSelfCode() {
		return self_code;
	}

	public static String getPolicyLogo() {
		return policy_logo;
	}

	public static String getTaxRateLogo() {
		return tax_rate_logo;
	}

	public static String getSpecialManagement() {
		return special_management;
	}

	public static String getEntryName() {
		return entry_name;
	}

	public static String getSpecificationModel() {
		return specification_model;
	}

	public static String getUnit() {
		return unit;
	}

	public static String getEntryNum() {
		return entry_num;
	}

	public static String getEntryPrice() {
		return entry_price;
	}

	public static String getEntryMoney() {
		return entry_money;
	}

	public static String getTaxRate() {
		return tax_rate;
	}

	public static String getTaxMoney() {
		return tax_money;
	}

	public static String getBillissuefilexml() {
		return billIssueFileXml;
	}

	public ElectronicsIssue() {
		super();
	}
	
	/***
	 * 创建通用报文
	 * */
	public String getSendToTaxXML(String appid, String contentPassword,String interfaceCode, Map map) throws Exception {
		String content = "";
		StringBuffer sb = new StringBuffer("");
		sb.append("<?xml version='1.0' encoding='UTF-8' ?>");
		sb.append("<interface xmlns=\"\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:schemaLocation=\"http://www.chinatax.gov.cn/tirip/dataspec/interfaces.xsd\" version=\"DZFPQZ0.2\"> ");
		sb.append("<globalInfo>");
		sb.append("<appId>").append(appid).append("</appId>");
		sb.append("<interfaceId></interfaceId>");
		sb.append("<interfaceCode>").append(interfaceCode).append("</interfaceCode>");
		sb.append("<requestCode>DZFPQZ</requestCode>");
		sb.append("<requestTime>").append(Utils.formatToTime()).append("</requestTime>");
		sb.append("<responseCode>DS</responseCode>");
		sb.append("<dataExchangeId>").append("DZFPQZ").append(interfaceCode).append(Utils.formatToDay()).append(Utils.randNineData()).append("</dataExchangeId>");
		sb.append("</globalInfo>");
		sb.append("<returnStateInfo>");
		sb.append("<returnCode></returnCode>");
		sb.append("<returnMessage></returnMessage>");
		sb.append("</returnStateInfo>");
		sb.append("<Data>");
		sb.append("<dataDescription>");
		sb.append("<zipCode>0</zipCode>");
		sb.append("</dataDescription>");
		sb.append("<content>");
		if (interfaceCode.equals(Utils.dfxj1001)) {
			content = createXML(map); // 创建开具报文
		} 
		else if (interfaceCode.equals(Utils.dfxj1003)) {
			content = createQuerySurplus(map);	//发票结余查询接口
		}
		else if (interfaceCode.equals(Utils.dfxj1004)) {
			content = createQueryXML(map);  //发票查询接口
		}
		else if (interfaceCode.equals(Utils.dfxj1005)) {
			content = createDownloadXML(map);	//下载地址查询接口
		}
		content = content.replaceAll("\r\n", "").replaceAll("\n", "");// 去掉空格和换行
		sb.append(content);
		sb.append("</content>");
		sb.append("<contentKey>");
		String contentMD5 = MyAES.MD5(content.getBytes("UTF-8"));// 对content数据进行MD5加密
		String contentKey = MyAES.encryptBASE64(MyAES.encrypt(contentMD5.getBytes("UTF-8"),contentPassword)).replaceAll("\r\n", "").replaceAll("\n", "");// 对md5后的数据进行AES加密
		sb.append(contentKey);
		sb.append("</contentKey>");
		sb.append("</Data>");
		sb.append("</interface>");
		System.out.println("发票开具报文："+sb.toString());
		return sb.toString();
	}
	
	/**
	 * 创建发票剩余查询报文
	 * @throws IOException 
	 * */
	public String createQuerySurplus(Map map) {
		BillInfo billInfo = (BillInfo) map.get("billInfo");
		Product product = (Product) map.get("product");
		StringBuffer sb = new StringBuffer();
		sb.append("<REQUEST_COMMON_FPKCCX class='REQUEST_COMMON_FPKCCX'> ");
		sb.append("<NSRSBH>"+ billInfo.getTaxno() +"</NSRSBH>");
		sb.append("<DETAIL_FLAG>"+""+"</DETAIL_FLAG>");//是否获取各终端票数 0-只获取总数  1-获取各终端数
		return null;
	}
	
	/**
	 * 创建开具报文
	 * @throws IOException 
	 * */
	public String createXML1(Map map) throws IOException {
		StringBuffer sb = new StringBuffer();
		BillInfo billInfo = (BillInfo) map.get("billInfo");
		List<Product> list= (List<Product>) map.get("goodsList");
		sb.append("<REQUEST_COMMON_FPKJ class='REQUEST_COMMON_FPKJ'> ");
		sb.append("<FPQQLSH>TEST" + Utils.formatToTime() + "01" + "</FPQQLSH>");// 发票请求流水号
		sb.append("<KPLX>" + TaxSelvetUtil.Issue_Bill_Type_0 + "</KPLX>");// 开票类型
		sb.append("<BMB_BBH>" +""+ "</BMB_BBH>");// 编码表版本号
		sb.append("<ZSFS>" + "0" + "</ZSFS>");// 征税方式
		sb.append("<XSF_NSRSBH>" + billInfo.getTaxno() + "</XSF_NSRSBH>");// 销售方纳税人识别号
		sb.append("<XSF_MC>"+ billInfo.getName()+"</XSF_MC>");// 销售方名称
		sb.append("<XSF_DZDH>"+billInfo.getAddressandphone()+"</XSF_DZDH>");// 销售方地址、电话
		sb.append("<XSF_YHZH>"+billInfo.getBankandaccount()+"</XSF_YHZH>");// 销售方银行账号
		sb.append("<GMF_NSRSBH>"+billInfo.getCustomerTaxno()+"</GMF_NSRSBH>");// 购买方纳税人识别号
		sb.append("<GMF_MC>" +billInfo.getCustomerName()+"</GMF_MC>");// 购买方名称
		sb.append("<GMF_DZDH>" +billInfo.getCustomerAddressandphone()+ "</GMF_DZDH>");// 购买方地址、电话
		sb.append("<GMF_YHZH>" +billInfo.getCustomerBankandaccount()+ "</GMF_YHZH>");// 购买方银行账户
		sb.append("<GMF_SJH>" +""+ "</GMF_SJH>");// 购买方手机号
		sb.append("<GMF_DZYX>"+billInfo.getCustomerEmail()+"</GMF_DZYX>");// 购买电子邮箱
		sb.append("<FPT_ZH/>");// 发票通账户
		sb.append("<WX_OPENID/>");// 微信
		sb.append("<KPR>"+billInfo.getDrawer()+"</KPR>");
		sb.append("<SKR>"+billInfo.getPayee()+"</SKR>");
		sb.append("<FHR>"+billInfo.getReviewer()+"</FHR>");
		sb.append("<YFP_DM>"+billInfo.getOriBillCode()+"</YFP_DM>");// 原发票代码
		sb.append("<YFP_HM>"+billInfo.getOriBillNo()+"</YFP_HM>");// 原发票号码
		sb.append("<JSHJ>"+billInfo.getSumAmt()+"</JSHJ>");// 价税合计
		sb.append("<HJJE>"+billInfo.getAmtSum()+"</HJJE>");// 合计金额
		sb.append("<KCE>"+""+"</KCE>");// 扣除额
		sb.append("<BZ>"+billInfo.getRemark()+"</BZ>");
		sb.append("<HYLX>"+""+"</HYLX>");// 行业类型
		sb.append("<BY1></BY1>");
		sb.append("<BY2></BY2>");
		sb.append("<BY3></BY3>");
		sb.append("<BY4></BY4>");
		sb.append("<BY5></BY5>");
		sb.append("<BY6></BY6>");
		sb.append("<BY7></BY7>");
		sb.append("<BY8></BY8>");
		sb.append("<BY9></BY9>");
		sb.append("<BY10></BY10>");
		sb.append("<WX_ORDER_ID/>");// 微信用于预制卡券的唯一识别id
		sb.append("<WX_APP_ID/>");// 微信APP ID
		sb.append("<ZFB_UID/>");//
		sb.append("<TSPZ>"+"00"+"</TSPZ>");//特殊票种标识
		sb.append("<QJ_ORDER_ID/>");
//		sb.append("<WX_GROUP_ID>");
		sb.append("<COMMON_FPKJ_XMXXS class='COMMON_FPKJ_XMXX' size='1'>");
		sb.append("<COMMON_FPKJ_XMXX>");
		for (int i = 0; i < list.size(); i++) {
			Product product = list.get(i);
			sb.append("<FPHXZ>"+"0"+"</FPHXZ>");// 发票性质
			sb.append("<SPBM>"+product.getGoodsid()+"</SPBM>");// 商品编码
			sb.append("<ZXBM/>");// 自行编码
			sb.append("<YHZCBS/>");
			sb.append("<LSLBS/>");
			sb.append("<ZZSTSGL/>");
			sb.append("<XMMC>"+product.getProductName()+"</XMMC>");
			sb.append("<GGXH>"+product.getSpecification()+"</GGXH>");
			sb.append("<DW>"+product.getUnit()+"</DW>");
			sb.append("<XMSL>"+product.getProductNumber()+"</XMSL>");
			sb.append("<XMDJ>"+product.getPrice()+"</XMDJ>");
			sb.append("<XMJE>"+product.getAmt()+"</XMJE>");
			sb.append("<SL>"+product.getRate()+"</SL>");
			sb.append("<SE>"+product.getTaxamt()+"</SE>");
			sb.append("<BY1></BY1>");
			sb.append("<BY2></BY2>");
			sb.append("<BY3></BY3>");
			sb.append("<BY4></BY4>");
			sb.append("<BY5></BY5>");
		}
		sb.append("</COMMON_FPKJ_XMXX>");
		sb.append("</COMMON_FPKJ_XMXXS>");
		sb.append("</REQUEST_COMMON_FPKJ>");
		System.out.println("content内容："+sb.toString());
		return new BASE64Encoder().encodeBuffer(sb.toString().getBytes("UTF-8"));
	}
	
	/**
	 * 创建开具报文
	 * @throws IOException 
	 * */
	public String createXML(Map map) throws IOException {
		StringBuffer content = new StringBuffer("");
		BillInfo billInfo = (BillInfo) map.get("billInfo");
		List<Product> list= (List<Product>) map.get("goodsList");
		content.append("<REQUEST_COMMON_FPKJ class='REQUEST_COMMON_FPKJ'> ");
		content.append("<FPQQLSH>TEST" + Utils.formatToTime() + "01"+ "</FPQQLSH>");
		content.append("<KPLX>0</KPLX>");
		content.append("<ZSFS>0</ZSFS>");
		content.append("<XSF_NSRSBH>" + billInfo.getTaxno() + "</XSF_NSRSBH>");
		content.append("<XSF_MC>"+ billInfo.getName()+"</XSF_MC>");
		content.append("<XSF_DZDH>"+billInfo.getAddressandphone()+"</XSF_DZDH>");
		content.append("<XSF_YHZH>"+billInfo.getBankandaccount()+"</XSF_YHZH>");
		content.append("<GMF_NSRSBH/>");
		content.append("<GMF_MC>" +billInfo.getCustomerName()+"</GMF_MC>");
		content.append("<GMF_DZDH/>");
		content.append("<GMF_YHZH/>");
		content.append("<KPR>"+billInfo.getDrawer()+"</KPR>");
		content.append("<SKR>"+billInfo.getPayee()+"</SKR>");
		content.append("<FHR>"+billInfo.getReviewer()+"</FHR>");
		content.append("<YFP_DM/>");
		content.append("<YFP_HM/>");
		content.append("<JSHJ>"+billInfo.getSumAmt()+"</JSHJ>");
		content.append("<HJJE>"+billInfo.getAmtSum()+"</HJJE>");
		content.append("<HJSE>"+billInfo.getTaxAmtSum()+"</HJSE>");
		content.append("<BZ>备注</BZ>");
		content.append("<GMF_SJH></GMF_SJH>");
		content.append("<GMF_DZYX>"+billInfo.getCustomerEmail()+"</GMF_DZYX>");
		content.append("<FPT_ZH></FPT_ZH>");
		content.append("<HYLX>0</HYLX>");
		content.append("<BY1></BY1>");
		content.append("<BY2></BY2>");
		content.append("<BY3></BY3>");
		content.append("<BY4></BY4>");
		content.append("<BY5></BY5>");
		content.append("<BY6></BY6>");
		content.append("<BY7></BY7>");
		content.append("<BY8></BY8>");
		content.append("<BY9></BY9>");
		content.append("<BY10></BY10>");
		content.append("<COMMON_FPKJ_XMXXS class='COMMON_FPKJ_XMXX' size='"+list.size()+"'>");
		for (int i = 0; i < list.size(); i++) {
			Product product = list.get(i);
			content.append("<COMMON_FPKJ_XMXX>");
			content.append("<FPHXZ>0</FPHXZ>");
			content.append("<SPBM>"+product.getGoodsid()+"</SPBM>");
			content.append("<XMMC>"+product.getProductName()+"</XMMC>");
			content.append("<GGXH>"+product.getSpecification()+"</GGXH>");
			content.append("<DW>"+product.getUnit()+"</DW>");
			content.append("<XMSL>"+product.getProductNumber()+"</XMSL>");
			content.append("<XMDJ>"+product.getPrice()+"</XMDJ>");
			content.append("<XMJE>"+product.getAmt()+"</XMJE>");
			content.append("<SL>"+product.getRate()+"</SL>");
			content.append("<SE>"+product.getTaxamt()+"</SE>");
			content.append("<BY1></BY1>");
			content.append("<BY2></BY2>");
			content.append("<BY3></BY3>");
			content.append("<BY4></BY4>");
			content.append("<BY5></BY5>");
			content.append("<BY6></BY6>");
			content.append("</COMMON_FPKJ_XMXX>");
		}
		content.append("</COMMON_FPKJ_XMXXS>");
		content.append("</REQUEST_COMMON_FPKJ>");
		System.out.println("content内容："+content.toString());
		return  new BASE64Encoder().encodeBuffer(content.toString().getBytes("UTF-8"));
	}
	
	/**
	 * 创建开具报文
	 * @throws IOException 
	 * */
	public String createXML2(Map map) throws IOException {
		StringBuffer content = new StringBuffer("");
		content.append("<REQUEST_COMMON_FPKJ class='REQUEST_COMMON_FPKJ'> ");
		content.append("<FPQQLSH>TEST" + Utils.formatToTime() + "01"+ "</FPQQLSH>");
		content.append("<KPLX>0</KPLX>");
		content.append("<ZSFS>0</ZSFS>");
		content.append("<XSF_NSRSBH>110109500321655</XSF_NSRSBH>");
		content.append("<XSF_MC>百旺电子测试2</XSF_MC>");
		content.append("<XSF_DZDH>南山区蛇口、83484949</XSF_DZDH>");
		content.append("<XSF_YHZH>xx银行、88888888888</XSF_YHZH>");
		content.append("<GMF_NSRSBH/>");
		content.append("<GMF_MC>张三</GMF_MC>");
		content.append("<GMF_DZDH/>");
		content.append("<GMF_YHZH/>");
		content.append("<KPR>开票人</KPR>");
		content.append("<SKR>收款人</SKR>");
		content.append("<FHR>复核人</FHR>");
		content.append("<YFP_DM/>");
		content.append("<YFP_HM/>");
		content.append("<JSHJ>117</JSHJ>");
		content.append("<HJJE>100</HJJE>");
		content.append("<HJSE>17</HJSE>");
		content.append("<BZ>备注</BZ>");
		content.append("<GMF_SJH></GMF_SJH>");
		content.append("<GMF_DZYX></GMF_DZYX>");
		content.append("<FPT_ZH></FPT_ZH>");
		content.append("<HYLX>0</HYLX>");
		content.append("<BY1></BY1>");
		content.append("<BY2></BY2>");
		content.append("<BY3></BY3>");
		content.append("<BY4></BY4>");
		content.append("<BY5></BY5>");
		content.append("<BY6></BY6>");
		content.append("<BY7></BY7>");
		content.append("<BY8></BY8>");
		content.append("<BY9></BY9>");
		content.append("<BY10></BY10>");
		content.append("<COMMON_FPKJ_XMXXS class='COMMON_FPKJ_XMXX' size='2'>");
		content.append("<COMMON_FPKJ_XMXX>");
		content.append("<FPHXZ>0</FPHXZ>");
		content.append("<SPBM>1010101050000000000</SPBM>");
		content.append("<XMMC>红高粱</XMMC>");
		content.append("<GGXH>500克</GGXH>");
		content.append("<DW>袋</DW>");
		content.append("<XMSL>1</XMSL>");
		content.append("<XMDJ>50</XMDJ>");
		content.append("<XMJE>50</XMJE>");
		content.append("<SL>0.17</SL>");
		content.append("<SE>8.5</SE>");
		content.append("<BY1></BY1>");
		content.append("<BY2></BY2>");
		content.append("<BY3></BY3>");
		content.append("<BY4></BY4>");
		content.append("<BY5></BY5>");
		content.append("<BY6></BY6>");
		content.append("</COMMON_FPKJ_XMXX>");
		content.append("<COMMON_FPKJ_XMXX>");
		content.append("<FPHXZ>0</FPHXZ>");
		content.append("<SPBM>1010101010000000000</SPBM>");
		content.append("<XMMC>东北大米</XMMC>");
		content.append("<GGXH>500克</GGXH>");
		content.append("<DW>袋</DW>");
		content.append("<XMSL>1</XMSL>");
		content.append("<XMDJ>50</XMDJ>");
		content.append("<XMJE>50</XMJE>");
		content.append("<SL>0.17</SL>");
		content.append("<SE>8.5</SE>");
		content.append("<BY1></BY1>");
		content.append("<BY2></BY2>");
		content.append("<BY3></BY3>");
		content.append("<BY4></BY4>");
		content.append("<BY5></BY5>");
		content.append("<BY6></BY6>");
		content.append("</COMMON_FPKJ_XMXX>");
		content.append("</COMMON_FPKJ_XMXXS>");
		content.append("</REQUEST_COMMON_FPKJ>");
		System.out.println("content内容："+content.toString());
		return  new BASE64Encoder().encodeBuffer(content.toString().getBytes("UTF-8"));
	}
	
	/**
	 * 创建电票查询报文
	 * **/
	public String createQueryXML(Map map) {
		BillInfo billInfo = (BillInfo) map.get("billInfo");
		Product product = (Product) map.get("product");
		String strXML = null;
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement(queryheader);
		root.setAttributeValue("class", queryheader);
		Element fpqqlsh = root.addElement(invoice_flow_number);
		Element xsfnsrsbh = root.addElement(sale_taxpayer_no);

		fpqqlsh.addText("发票请求流水号");
		xsfnsrsbh.addText(billInfo.getTaxno());//"销售方纳税人识别号" 

		StringWriter strWtr = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		XMLWriter xmlWriter = new XMLWriter(strWtr, format);
		try {
			xmlWriter.write(document);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		strXML = strWtr.toString();
		return strXML;
	}

	/**
	 * 创建电票下载地址查询接口
	 * **/
	public String createDownloadXML(Map map) {
		BillInfo billInfo = (BillInfo) map.get("billInfo");
		Product product = (Product) map.get("product");
		String strXML = null;
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement(downloadheader);
		root.setAttributeValue("class", downloadheader);
		Element nsrsbh = root.addElement(taxpayer_no);
		Element fpqqlsh = root.addElement(invoice_flow_number);
		Element fpdm = root.addElement(fapiaoCode);
		Element fphm = root.addElement(fapiaoNo);

		nsrsbh.addText(billInfo.getTaxno());//"纳税人识别号"
		fpqqlsh.addText("发票请求流水号");
		fpdm.addText(billInfo.getBillCode());//"发票代码"
		fphm.addText(billInfo.getBillNo());//"发票号码"

		StringWriter strWtr = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		XMLWriter xmlWriter = new XMLWriter(strWtr, format);
		try {
			xmlWriter.write(document);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		strXML = strWtr.toString();
		return strXML;
	}

	/**
	 * 创建电票红冲报文
	 * **/
	public String createRedflushXML(Map map) {
		BillInfo billInfo = (BillInfo) map.get("billInfo");
		Product product = (Product) map.get("product");
		String strXML = null;
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement(downloadheader);
		root.setAttributeValue("class", downloadheader);
		Element fpqqlsh = root.addElement(invoice_flow_number);
		Element xsfnsrsbh = root.addElement(sale_taxpayer_no);
		Element xsfmc = root.addElement(sale_name);
		Element fpdm = root.addElement(old_fapiaocode);
		Element fphm = root.addElement(old_fapiaono);

		fpqqlsh.addText("发票请求流水号");
		xsfnsrsbh.addText("销售方纳税人识别号");
		fpdm.addText("原发票代码");
		fphm.addText("原发票号码");

		StringWriter strWtr = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		XMLWriter xmlWriter = new XMLWriter(strWtr, format);
		try {
			xmlWriter.write(document);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		strXML = strWtr.toString();
		return strXML;
	}

	/**
	 * 调用税控接口
	 * 
	 * @throws Exception
	 * */
	public String taxWebservice(Map map) throws Exception {
		
		String requestData = null;// 初始化请求报文
		String rsData = null;// 初始化结果报文
		String requestMethod = null;// 初始化请求方法
		String requestUrlMethod = null;// 初始化连接方法
		String requestUrl = "https://dev.fapiao.com:18943/fpt-dsqz/invoice";// 初始化地址
		String appid = "6d29f136114544bcc73edcce960c430231183cc192c433e2b9ebcad56e8ceb08";// appid
		String contentPassword = "5EE6C2C11DD421F2";// AES加密密钥
//		String fpqqlsh = "TEST2017022415272501";// 需要查询发票的流水号
//		String nsrsbh = "110109500321655"; //纳税人识别号
//		String fpdm = "050003521333"; //发票代码
//		String fphm = "85004524";//发票号码
		String interfaceLau = Utils.interfaceLau_xml;
		String interfaceCode = Utils.dfxj1001;// 开具
		String requestInterface = Utils.post_https;// 使用post请求方式
		// 组装请求报文
		requestData = getSendToTaxXML(appid,contentPassword,interfaceCode,map);
		//requestData = getSendToTaxXML(fpqqlsh,nsrsbh,fpdm,fphm,appid,contentPassword,interfaceCode,map);
		//this.printXML(requestData);
		//requestMethod = Utils.requestMethod_xml;// xml的请求方法
		//requestUrlMethod = Utils.requestUrlMethod_xml;// xml的连接后缀
		System.out.println("组装报文完毕,请求使用的语言是:" + interfaceLau + ",请求的方式是："+ requestInterface + ",请求报文为:" + requestData + ",开始请求。");
		// 调用接口
		rsData = RequestUtils.getHttpConnectResult(requestData, requestUrl);
		Date requestEndDate = new Date();// 初始化请求结束时间
		System.out.println("请求接口结束，获得结果:" + rsData);
		return rsData;
	}

	/**
	 * 解析税控返回报文
	 * @throws Exception 
	 * */
	public Map printXML(String strXML) throws Exception {
		Document document = DocumentHelper.parseText(strXML);
		Element root = document.getRootElement();
		Element globalInfo = root.element("globalInfo");
		Element appId = globalInfo.element("appId");
		Element interfaceId = globalInfo.element("interfaceId");
		Element interfaceCode = globalInfo.element("interfaceCode");
		Element requestCode = globalInfo.element("requestCode");
		Element requestTime = globalInfo.element("requestTime");
		Element responseCode = globalInfo.element("responseCode");
		Element dataExchangeId = globalInfo.element("dataExchangeId");
		Element returnStateInfo = root.element("returnStateInfo");
		Element returnCode = returnStateInfo.element("returnCode");
		Element returnMessage = returnStateInfo.element("returnMessage");
		Element Data = root.element("Data");
		Element dataDescription = Data.element("dataDescription");
		Element zipCode = dataDescription.element("zipCode");
		Element content = Data.element("content");
		
		//解密
		byte  result[]= new BASE64Decoder().decodeBuffer(content.getTextTrim());
        String recontent = new String(result,"utf-8");
		System.out.println("税控返回的content：" + recontent);
		
		Map remap = new HashMap();
		if (interfaceCode.getTextTrim().equals(Utils.dfxj1001)) {	// 解析开具报文
			//获取content节点
			Document reXML = DocumentHelper.parseText(recontent);
			Element reroot = reXML.getRootElement();
			Element refapiaocode = reroot.element("FP_DM"); //发票代码
			Element refapiaono = reroot.element("FP_HM");  //发票号码
			Element repdfurl = reroot.element("PDF_URL"); //pdf下载地址
			Element checkCode = reroot.element("JYM"); //校验码
			Element billDate = reroot.element("KPRQ"); //开票日期
			Element spurl = reroot.element("SP_URL");  //收票地址,可用该地址生成二维码
			
			remap.put("fapiaoCode",refapiaocode.getTextTrim());
			remap.put("fapiaoNo",refapiaono.getTextTrim());
			remap.put("PDFURL",repdfurl.getTextTrim());
			remap.put("checkCode", checkCode.getTextTrim());
			remap.put("BillDate", billDate.getTextTrim());
			remap.put("SPURL", spurl.getTextTrim());
		} else if (interfaceCode.getTextTrim().equals(Utils.dfxj1003)) {	//解析发票结余查询
			Document reXML = DocumentHelper.parseText(recontent);
			Element reroot = reXML.getRootElement();
			Element retotal = reroot.element("SUM_NUM");
			Element resurplus = reroot.element("UNDISTRIBUTED_NUM");
			Element relis = reroot.element("TERMINAL_LIST");
			Element reinfo = relis.element("TERMINAL_INFO");
			Element remark = reinfo.element("TERMINAL_MARK");
			Element renum = reinfo.element("REMAIN_NUM");
			
			remap.put("retotal",retotal.getTextTrim());
			remap.put("resurplus",resurplus.getTextTrim());
			remap.put("remark",remark.getTextTrim());
			remap.put("renum", renum.getTextTrim());
		} else if (interfaceCode.getTextTrim().equals(Utils.dfxj1004)) {	//解析发票查询
			Document reXML = DocumentHelper.parseText(recontent);
			Element reroot = reXML.getRootElement();
			Element refapiaocode = reroot.element("FP_DM");
			Element refapiaono = reroot.element("FP_HM");
			Element repdfurl = reroot.element("PDF_URL");
			Element readdress = reroot.element("SP_URL");
			
			remap.put("fapiaoCode",refapiaocode.getTextTrim());
			remap.put("fapiaoNo",refapiaono.getTextTrim());
			remap.put("PDFURL",repdfurl.getTextTrim());
			remap.put("readdress",readdress.getTextTrim());
		} else if (interfaceCode.getTextTrim().equals(Utils.dfxj1005)) {	//解析下载地址查询
			Document reXML = DocumentHelper.parseText(recontent);
			Element reroot = reXML.getRootElement();
			Element repdfurl = reroot.element("PDF_URL");
			remap.put("PDFURL",repdfurl.getTextTrim());
		}
		
		// 解析税控返回的状态信息
		switch (returnCode.getTextTrim()) {
		case request_timeout:
			System.err.println(request_timeout_ch);
			remap.put("result",request_timeout_ch);
			break;
		case request_exception:
			System.err.println(request_exception_ch);
			remap.put("result",request_exception_ch);
			break;
		case request_runtime_exception:
			System.err.println(request_runtime_exception_ch);
			remap.put("result",request_runtime_exception_ch);
			break;
		case message_format_error:
			System.err.println(message_format_error_ch);
			remap.put("result",message_format_error_ch);
			break;
		case incorrect_data:
			System.err.println(incorrect_data_ch);
			remap.put("result",incorrect_data_ch);
			break;
		case deal_with_fail:
			System.err.println(deal_with_fail_ch);
			remap.put("result",deal_with_fail_ch);
			break;
		default:
			System.err.println(deal_with_success_ch);
			remap.put("result",deal_with_success_ch);
			break;
		}
		return remap;
	}
	
	/**
	 * 解析税控返回报文
	 * @throws Exception 
	 * */
	public Map printXML1(String strXML) throws Exception {
		Document document = DocumentHelper.parseText(strXML);
		Element root = document.getRootElement();
		Element globalInfo = root.element("globalInfo");
		Element appId = globalInfo.element("appId");
		Element interfaceId = globalInfo.element("interfaceId");
		Element interfaceCode = globalInfo.element("interfaceCode");
		Element requestCode = globalInfo.element("requestCode");
		Element requestTime = globalInfo.element("requestTime");
		Element responseCode = globalInfo.element("responseCode");
		Element dataExchangeId = globalInfo.element("dataExchangeId");
		Element returnStateInfo = root.element("returnStateInfo");
		Element returnCode = returnStateInfo.element("returnCode");
		Element returnMessage = returnStateInfo.element("returnMessage");
		Element Data = root.element("Data");
		Element dataDescription = Data.element("dataDescription");
		Element zipCode = dataDescription.element("zipCode");
		Element content = globalInfo.element("content");
		
		//解密
		byte  result[]= new BASE64Decoder().decodeBuffer(content.toString());
        String recontent = new String(result,"utf-8");
		System.out.println("税控返回的content：" + recontent);
		
		Map remap = new HashMap();
		if (interfaceCode.getTextTrim().equals(Utils.dfxj1001)) {	// 解析开具报文
			//获取content节点
			Document reXML = DocumentHelper.parseText(recontent);
			Element reroot = reXML.getRootElement();
			Element refapiaocode = reroot.element("FP_DM");
			Element refapiaono = reroot.element("FP_HM");
			Element repdfurl = reroot.element("PDF_URL");
			
			remap.put("fapiaoCode",refapiaocode.getTextTrim());
			remap.put("fapiaoNo",refapiaono.getTextTrim());
			remap.put("PDFURL",repdfurl.getTextTrim());
		} else if (interfaceCode.getTextTrim().equals(Utils.dfxj1003)) {	//解析发票结余查询
			Document reXML = DocumentHelper.parseText(recontent);
			Element reroot = reXML.getRootElement();
			Element retotal = reroot.element("SUM_NUM");
			Element resurplus = reroot.element("UNDISTRIBUTED_NUM");
			Element relis = reroot.element("TERMINAL_LIST");
			Element reinfo = relis.element("TERMINAL_INFO");
			Element remark = reinfo.element("TERMINAL_MARK");
			Element renum = reinfo.element("REMAIN_NUM");
			
			remap.put("retotal",retotal.getTextTrim());
			remap.put("resurplus",resurplus.getTextTrim());
			remap.put("remark",remark.getTextTrim());
			remap.put("renum", renum.getTextTrim());
		} else if (interfaceCode.getTextTrim().equals(Utils.dfxj1004)) {	//解析发票查询
			Document reXML = DocumentHelper.parseText(recontent);
			Element reroot = reXML.getRootElement();
			Element refapiaocode = reroot.element("FP_DM");
			Element refapiaono = reroot.element("FP_HM");
			Element repdfurl = reroot.element("PDF_URL");
			Element readdress = reroot.element("SP_URL");
			
			remap.put("fapiaoCode",refapiaocode.getTextTrim());
			remap.put("fapiaoNo",refapiaono.getTextTrim());
			remap.put("PDFURL",repdfurl.getTextTrim());
			remap.put("readdress",readdress.getTextTrim());
		} else if (interfaceCode.getTextTrim().equals(Utils.dfxj1005)) {	//解析下载地址查询
			Document reXML = DocumentHelper.parseText(recontent);
			Element reroot = reXML.getRootElement();
			Element repdfurl = reroot.element("PDF_URL");
			remap.put("PDFURL",repdfurl.getTextTrim());
		}
		
		// 解析税控返回的状态信息
		switch (returnCode.getTextTrim()) {
		case request_timeout:
			System.err.println(request_timeout_ch);
			remap.put("result",request_timeout_ch);
			break;
		case request_exception:
			System.err.println(request_exception_ch);
			remap.put("result",request_exception_ch);
			break;
		case request_runtime_exception:
			System.err.println(request_runtime_exception_ch);
			remap.put("result",request_runtime_exception_ch);
			break;
		case message_format_error:
			System.err.println(message_format_error_ch);
			remap.put("result",message_format_error_ch);
			break;
		case incorrect_data:
			System.err.println(incorrect_data_ch);
			remap.put("result",incorrect_data_ch);
			break;
		case deal_with_fail:
			System.err.println(deal_with_fail_ch);
			remap.put("result",deal_with_fail_ch);
			break;
		default:
			System.err.println(deal_with_success_ch);
			remap.put("result",deal_with_success_ch);
			break;
		}
		return remap;
	}
	
	/**
	 * 后台输出报文
	 */
	public void printIssueXML(String strXML) {
		ElectronicsIssue els = new ElectronicsIssue();
		System.err.println(strXML);
		System.out.println("结束：-----------");
		els.GetPathsty();
	}

	public void GetPathsty() {
		String classPath = VmsCommonServiceImpl.getClassPath(this.getClass());
		System.out.println("我的坐标:" + this.getClass());
	}
}
