package com.cjit.common.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import com.cjit.vms.trans.model.JxlExcelInfo;

import jxl.Cell;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.DateFormats;
import jxl.write.Label;
import jxl.write.NumberFormats;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class JXLTool {

	public static WritableCellFormat getHeader() throws WriteException {

		WritableFont font = new WritableFont(WritableFont.TIMES,10);
		WritableCellFormat format = new WritableCellFormat(font);
		try {

			format.setAlignment(jxl.format.Alignment.CENTRE);

			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

			format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
			format.setWrap(true);  
			format.setWrap(true);
		} catch (WriteException e) {
			throw e;
		}
		return format;
	}
	
	
	public static void setAutoWidth(WritableSheet sheet,List list) throws RowsExceededException, WriteException{
		if(list.size()>0){
		List listRow=(List) list.get(0);
		int columnBestWidth[]=new  int[listRow.size()];    //��������п����ݵ�����
		Cell[] title=  sheet.getRow(0) ;
        for(int i=0;i<list.size();i++){
            List row=(List)list.get(i);
            for(int j=0;j<row.size();j++){
                 sheet.addCell(new Label(j,i+1,(String) row.get(j)));
                 int width=0;
                 if(StringUtil.isNotEmpty((String)row.get(j))){
                	 width=((String) row.get(j)).length()+getChineseNum((String)row.get(j)); 
                 }   ///����ռ2����λ����
                 if(columnBestWidth[j]<width) {   ///��ȡ��ĿǰΪֹ������п�
                     columnBestWidth[j]=width;
                     if((title[0].getContents().length()+getChineseNum(title[j].getContents())+5)>columnBestWidth[j]){
                    	 columnBestWidth[j]= (title[j].getContents().length()+getChineseNum(title[j].getContents())+5);
                     }
            }
        }
	}
        for(int i=0;i<columnBestWidth.length;i++){    ///����ÿ�п�
            sheet.setColumnView(i, columnBestWidth[i]);
        }}else{
        	Cell[] title=  sheet.getRow(0) ;
        	int columnBestWidth[]=new  int[title.length]; 
        	for(int i=0;i<title.length;i++){
                int width=(title[i].getContents().length()+getChineseNum(title[i].getContents())+5);    ///����ռ2����λ����
                columnBestWidth[i]=width;
        	}
        	 for(int i=0;i<columnBestWidth.length;i++){    ///����ÿ�п�
                 sheet.setColumnView(i, columnBestWidth[i]);
             }
        }

	}
	public static int getChineseNum(String context){    ///ͳ��context���Ǻ��ֵĸ���
        int lenOfChinese=0;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");    //���ֵ�Unicode���뷶Χ
        Matcher m = p.matcher(context);
        while(m.find()){
            lenOfChinese++;
        }
        return lenOfChinese==0?2:lenOfChinese;
    }

	public static WritableCellFormat getHeaderB() throws WriteException {

		WritableFont font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);
		WritableCellFormat format = new WritableCellFormat(font);
		try {

			format.setAlignment(jxl.format.Alignment.CENTRE);

			format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);

			format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
//			format.setBackground(Colour.YELLOW2);
			format.setWrap(true);
		} catch (WriteException e) {
			throw e;
		}
		return format;
	}
	/**
	 * �Զ���title
	 * 
	 */
	public static WritableCellFormat getHeaderC(JxlExcelInfo info) throws WriteException {
		int fontSize = info.getFontSize()==0?12:info.getFontSize();
		WritableFont font = new WritableFont(WritableFont.TIMES, fontSize);
		WritableCellFormat format = new WritableCellFormat(font);
		try {
			if(info.getAlignMent()==null || info.getAlignMent().equals("centre")){
				format.setAlignment(jxl.format.Alignment.CENTRE);
			}else if(info.getAlignMent().equals("left")){
				format.setAlignment(jxl.format.Alignment.LEFT);
			}else if(info.getAlignMent().equals("right")){
				format.setAlignment(jxl.format.Alignment.RIGHT);
			}
			if(info.getvAlignMent()==null || info.getvAlignMent().equals("centre")){
				format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
			}else if(info.getvAlignMent().equals("top")){
				format.setVerticalAlignment(jxl.format.VerticalAlignment.TOP);
			}else if(info.getvAlignMent().equals("bottom")){
				format.setVerticalAlignment(jxl.format.VerticalAlignment.BOTTOM);
			}
			if(info.getBorderColor()!=null){
				format.setBorder(Border.ALL, BorderLineStyle.THIN, info.getBorderColor());
			}
			if(info.getBgColor()!=null){
				format.setBackground(info.getBgColor());
			}
			format.setWrap(true);
		} catch (WriteException e) {
			throw e;
		}
		return format;
	}
	
	public static WritableCellFormat getContentFormat() throws WriteException {
		WritableCellFormat format = new WritableCellFormat(NumberFormats.TEXT);
		try {
			//format.setWrap(true);
			format.setAlignment(jxl.format.Alignment.LEFT);
  			format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); //���ñ߿�   

		} catch (WriteException e) {
			throw e;
		}
		return format;
	}
	//������Ԫ���ʽΪС�������Զ�����
	public static WritableCellFormat getContentFormatNumberFloat() throws WriteException {
		WritableCellFormat format = new WritableCellFormat(NumberFormats.FLOAT);
		try {
			format.setAlignment(jxl.format.Alignment.LEFT);
  			format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); //���ñ߿�   
		} catch (WriteException e) {
			throw e;
		}
		return format;
	}
	
	//������Ԫ���ʽΪ���������Զ�����
	public static WritableCellFormat getContentFormatNumberInt() throws WriteException {
		WritableCellFormat format = new WritableCellFormat(NumberFormats.INTEGER);
		try {
			format.setAlignment(jxl.format.Alignment.LEFT);
  			format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); //���ñ߿�   
		} catch (WriteException e) {
			throw e;
		}
		return format;
	}
	//������Ԫ���ʽΪ���л��ҷ��ŵ�������
	public static WritableCellFormat getContentFormatAccountInt() throws WriteException {
		WritableCellFormat format = new WritableCellFormat(NumberFormats.ACCOUNTING_INTEGER);
		try {
			format.setAlignment(jxl.format.Alignment.LEFT);
  			format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); //���ñ߿�   
		} catch (WriteException e) {
			throw e;
		}
		return format;
	}
	//������Ԫ���ʽΪ���л��ҷ��ŵ�С����
		public static WritableCellFormat getContentFormatAccountFloat() throws WriteException {
			WritableCellFormat format = new WritableCellFormat(NumberFormats.ACCOUNTING_FLOAT);
			try {
				format.setAlignment(jxl.format.Alignment.LEFT);
	  			format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); //���ñ߿�   
			} catch (WriteException e) {
				throw e;
			}
			return format;
		}
		//������Ԫ���ʽΪ###,000����
				public static WritableCellFormat getContentFormatInt() throws WriteException {
					WritableCellFormat format = new WritableCellFormat(NumberFormats.FORMAT1);
					try {
						format.setAlignment(jxl.format.Alignment.LEFT);
			  			format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); //���ñ߿�   
					} catch (WriteException e) {
						throw e;
					}
					return format;
				}
		//������Ԫ���ʽΪ###,000.00����
				public static WritableCellFormat getContentFormatFloat() throws WriteException {
					WritableCellFormat format = new WritableCellFormat(NumberFormats.FORMAT3);
					try {
						format.setAlignment(jxl.format.Alignment.LEFT);
			  			format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); //���ñ߿�   
					} catch (WriteException e) {
						throw e;
					}
					return format;
				}		
	//������Ԫ���ʽΪ�����Զ�����
		public static WritableCellFormat getContentFormatDateFormat() throws WriteException {
			WritableCellFormat format = new WritableCellFormat(DateFormats.DEFAULT);
			try {
				format.setAlignment(jxl.format.Alignment.LEFT);
	  			format.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN); //���ñ߿�   
			} catch (WriteException e) {
				throw e;
			}
			return format;
		}

}
