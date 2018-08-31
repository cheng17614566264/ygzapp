package com.cjit.vms.taxdisk.ciitc.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.cjit.vms.taxdisk.servlet.model.Product;
import com.cjit.common.model.BillInfo;

public class EInvoice {
	
	private final static String InvoInfo        = "InvoInfo";           
	                                                                    
	                                                                    
	private final static String swno            = "swno";               
	private final static String saleTax         = "saleTax";            
	private final static String custName        = "custName";           
	private final static String custTaxNo       = "custTaxNo";          
	private final static String custAddr        = "custAddr";           
	private final static String custTelephone   = "custTelephone";      
	private final static String custPhone       = "custPhone";          
	private final static String custEmail       = "custEmail";          
	private final static String custBankAccount = "custBankAccount";    
	private final static String custType        = "custType";           
	private final static String invoMemo        = "invoMemo";           
	private final static String invType         = "invType";            
	private final static String billDate        = "billDate";           
	private final static String thdh            = "thdh";               
	private final static String billType        = "billType";           
	private final static String specialRedFlag  = "specialRedFlag";     
	private final static String operationCode   = "operationCode";      
	private final static String kpy             = "kpy";                
	private final static String sky             = "sky";                
	private final static String fhr             = "fhr";                
	private final static String yfpdm           = "yfpdm";              
	private final static String yfphm           = "yfphm";              
	private final static String chyy            = "chyy";               
	                                                                    
	                                                                    
	private final static String Orders          = "Orders";             
	private final static String Order           = "Order";              
	private final static String billNo          = "billNo";             
	private final static String Items           = "Items";              
	private final static String item            = "item";               
	private final static String name            = "name";               
	private final static String code            = "code";               
	private final static String lineType        = "lineType";           
	private final static String spec            = "spec";               
	private final static String unit            = "unit";               
	private final static String taxRate         = "taxRate";            
	private final static String quantity        = "quantity";           
	private final static String taxPrice        = "taxPrice";           
	private final static String totalAmount     = "totalAmount";        
	private final static String yhzcbs          = "yhzcbs";             
	private final static String yhzcnr          = "yhzcnr";             
	private final static String lslbs           = "lslbs";              
	private final static String zxbm            = "zxbm";               
	private final static String kce             = "kce";                
	
	
	
	public final static String 正票正常开具 = "10";                                                              
	public final static String 正票错票重开 = "11";                                                              
	//public final static String 退货折让红票 = "20";//目前航信不支持                                                              
	public final static String 错票重开红票 = "21";//目前红冲只能用这个
	//public final static String 换票冲红   = "22";//目前航信不支持
	
	
	private BillInfo billinfo;
	private List<Product> goodsList;
	
	public EInvoice(BillInfo billInfo2, List<Product> goodsList){
		this.billinfo = billInfo2;
		this.goodsList = goodsList;
	}
	
	/**
	 * 
	 * @param operation
	 * @return
	 */
	public String getBillIssueXml(String operation){
		Element InvoInfoElement = new Element(InvoInfo);
		
		//必	流水号
		InvoInfoElement.addContent(new Element(swno).setText(billinfo.getBillId()));
		//必	销方税号
		InvoInfoElement.addContent(new Element(saleTax).setText(billinfo.getTaxno()));
		//必	购方名称
		InvoInfoElement.addContent(new Element(custName).setText(billinfo.getCustomerName()));
		//非	购方税号
		InvoInfoElement.addContent(new Element(custTaxNo).setText(billinfo.getCustomerTaxno()));
		if(billinfo.getCustomerAddressandphone()!=null && !"".equals(billinfo.getCustomerAddressandphone())){
			billinfo.setCustomerAddressandphone(billinfo.getCustomerAddressandphone().trim());
			int index = billinfo.getCustomerAddressandphone().lastIndexOf(" ");//最后一个空格的位置
			if(index==-1){
				Pattern pattern = Pattern.compile("[0-9\\-]*");
				if(pattern.matcher(billinfo.getCustomerAddressandphone()).matches()){
					//非	购方地址
					InvoInfoElement.addContent(new Element(custAddr).setText(""));
					//非	购方固定电话
					InvoInfoElement.addContent(new Element(custPhone).setText(billinfo.getCustomerAddressandphone()));
				}else{
					//非	购方地址
					InvoInfoElement.addContent(new Element(custAddr).setText(billinfo.getCustomerAddressandphone()));
					//非	购方固定电话
					InvoInfoElement.addContent(new Element(custPhone).setText(""));
				}
			}else{
				//非	购方地址
				InvoInfoElement.addContent(new Element(custAddr).setText(billinfo.getCustomerAddressandphone().substring(0, index)));
				//非	购方固定电话
				InvoInfoElement.addContent(new Element(custPhone).setText(billinfo.getCustomerAddressandphone().substring(index, billinfo.getCustomerAddressandphone().length())));
			}
			
		}else{
			//非	购方地址
			InvoInfoElement.addContent(new Element(custAddr).setText(""));
			//非	购方固定电话
			InvoInfoElement.addContent(new Element(custPhone).setText(""));
		}
		//必	购货方手机号
		InvoInfoElement.addContent(new Element(custTelephone).setText(""));
		//非	购方邮箱
		InvoInfoElement.addContent(new Element(custEmail).setText(billinfo.getCustomerEmail() == null ? "" : billinfo.getCustomerEmail()));
		//非	开户行+账号
		InvoInfoElement.addContent(new Element(custBankAccount).setText(billinfo.getCustomerBankandaccount()));
		//必	购货方企业类 型	电子发票所需信息 01：企业 02：机关执业单位 03：个人 04：其他
		InvoInfoElement.addContent(new Element(custType).setText("03"));//TODO
		//非	备注
		if(this.错票重开红票.equals(operation)){
			InvoInfoElement.addContent(new Element(invoMemo).setText("对应正数发票代码:"+billinfo.getOriBillCode()+"号码:"+billinfo.getOriBillNo()));
		}else{
			InvoInfoElement.addContent(new Element(invoMemo).setText(billinfo.getRemark()));
		}
		//必	发票类型	关键字段不能为空 专用票（0）、普通票（2）、 电子票（3） 目前字段只能为 3 
		InvoInfoElement.addContent(new Element(invType).setText("3"));
		//非	单据日期 
		InvoInfoElement.addContent(new Element(billDate).setText(""));
		//非或必	退货单号	在开具红字发票退货、折让的 时候必须填写
		if(this.错票重开红票.equals(operation)){
			InvoInfoElement.addContent(new Element(thdh).setText("4444"));//TODO
		}else{
			InvoInfoElement.addContent(new Element(thdh).setText(""));
		}
		//必	开票类型	1：正票 2：红票  
		if(this.正票正常开具.equals(operation)||this.正票错票重开.equals(operation)){
			InvoInfoElement.addContent(new Element(billType).setText("1"));
		}else{
			InvoInfoElement.addContent(new Element(billType).setText("2"));
		}
		//必	特殊冲红标志		电子发票所需信息 0：正常冲红（电子发票） 1：特殊冲红（冲红纸质等） 默认为 0
		if(this.错票重开红票.equals(operation)){
			InvoInfoElement.addContent(new Element(specialRedFlag).setText("0"));//TODO
		}
		//必	操作代码 	电子发票所需信息 10：正票正常开具 11：正票错票重开 20：退货折让红票 21：错票重开红票 22：换票冲红（全冲红电子发 票，开具纸质发票） 红票目前必须给 21 
		InvoInfoElement.addContent(new Element(operationCode).setText(operation));
		//必	开票员 
		InvoInfoElement.addContent(new Element(kpy).setText(billinfo.getDrawer()));
		//非	收款员 
		InvoInfoElement.addContent(new Element(sky).setText(billinfo.getPayee()));
		//非	复核人 
		InvoInfoElement.addContent(new Element(fhr).setText(billinfo.getReviewer()));
		//非或必	原发票代码 	原发票代码，电子发票冲红使 用 如果操作代码不是 10 或开票 类型为红票时候都是必录 
		InvoInfoElement.addContent(new Element(yfpdm).setText(billinfo.getOriBillCode()));
		//非或必	原发票号码	原发票号码，电子发票冲红使 用 如果操作代码不是 10 或开票 类型为红票时候都是必 
		InvoInfoElement.addContent(new Element(yfphm).setText(billinfo.getOriBillNo()));
		//非或必	冲红原因	冲红时填写，由企业定义
		if(operation.equals(EInvoice.正票正常开具)){
			InvoInfoElement.addContent(new Element(chyy).setText(""));
		}else{
			InvoInfoElement.addContent(new Element(chyy).setText(""));
		}
		
		InvoInfoElement.addContent(getOrders(goodsList));
		
		
		Document Doc = new Document(InvoInfoElement);
		Format format=Format.getPrettyFormat(); 
		format.setEncoding("UTF-8");
		XMLOutputter xmlOut = new XMLOutputter(format);
		String outString = xmlOut.outputString(Doc);
		
		return outString;
	}

	private Element getOrders(List<Product> goodsList2) {
		Element OrdersElement = new Element(Orders);
		
		Element OrderElement = new Element(Order);
		//必	订单号	关键字段不能为空，可能为多 个订单号
		OrderElement.addContent(new Element(billNo).setText("11111"));//TODO
		Element itemsElement = new Element(Items);
		for (Product product : goodsList2) {
			Element itemElement = new Element(item);
			//必	商品名称 	如 发票行性质=1， 则此商 品行为折扣行，此 版本折扣行不允许 多行折扣，折扣行 必须紧邻被折扣 行，项目名称必须 与被折扣行一致
			itemElement.addContent(new Element(name).setText(product.getProductName()));
			//必	商品编号（税收 分类编码）	商品唯一标识，关键字段不能 为空（需符合税局要求）
			itemElement.addContent(new Element(code).setText(product.getGoodsid()));
			//必	发票行性质 		0：正常行 1：折扣行 2：被折扣行
			itemElement.addContent(new Element(lineType).setText("0"));//TODO
			//非	规格型号 		明细行规格型号，如商品无规 格型号，可为空
			itemElement.addContent(new Element(spec).setText(product.getSpecification()));
			//必	计量单位 		明细行计量单位，发票显示信 息 单价、数量和计数单位必须同 时存在 
			itemElement.addContent(new Element(unit).setText(product.getUnit()));
			//必	税率		商品税率 如果税率为 0，表示免税 
			itemElement.addContent(new Element(taxRate).setText(product.getRate().trim()));
			//必	数量 		明细行数量，发票显示信息 单价、数量和计数单位必须同 时存在 红票是为负
			itemElement.addContent(new Element(quantity).setText(product.getProductNumber()));
			//必	单价 		含税单价 单价、数量和计数单位必须同 时存在
			itemElement.addContent(new Element(taxPrice).setText(new BigDecimal(product.getAmt().trim()).add(new BigDecimal(product.getTaxamt().trim())).abs().toString()));
			//必	含税金额 	明细行含税金额，发票显示信 息 红票时为负 
			itemElement.addContent(new Element(totalAmount).setText(new BigDecimal(product.getAmt().trim()).add(new BigDecimal(product.getTaxamt().trim())).abs().multiply(new BigDecimal(product.getProductNumber().trim())).toString()));
			
			if("0.00".equals(product.getRate().trim())){
				//必	税收优惠政策 标志		预留字段 0：不使用 1：使用 
				itemElement.addContent(new Element(yhzcbs).setText("1"));
				//非	享受税收优惠 政策内容 		预留字段 
				itemElement.addContent(new Element(yhzcnr).setText("免税"));
				//非	零税率标识 		预留字段 空：非零税率，1：免税，2： 不征税，3：普通零税率 
				itemElement.addContent(new Element(lslbs).setText("1"));
			}else{
				itemElement.addContent(new Element(yhzcbs).setText("0"));
				itemElement.addContent(new Element(yhzcnr).setText(""));
				itemElement.addContent(new Element(lslbs).setText(""));
			}
			//非	自行编码 		预留字段 
			itemElement.addContent(new Element(zxbm).setText(""));
			//非	扣除额 		预留字段，企业定义 
			itemElement.addContent(new Element(kce).setText(""));
			
			itemsElement.addContent(itemElement);
		}
		OrderElement.addContent(itemsElement);
		OrdersElement.addContent(OrderElement);
		
		return OrdersElement;
	}
}
