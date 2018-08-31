package com.cjit.insertIntoMysqlFromTemp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cjit.achieveTemp.achieveTemp;
import com.cjit.achieveTemp.model.achieveTempModel;
import com.cjit.inserFromTemp.ipAccess.IpAccess;

public class InsertIntotransInfo {
	public static Connection getConnction(String driverClass, String dburl,
			String user, String pass){
		Connection dbConnection = null;
		try{
			Class.forName(driverClass).newInstance();
			dbConnection = DriverManager.getConnection(dburl, user, pass);
			System.out.println("数据库连接成功");
		}catch (Exception e){
			e.printStackTrace();
		}
		return dbConnection;
	}
	
	public static void insertIntoTransInfo(){
		
		/*IpAccess ia=new IpAccess();
		List list=ia.selectCustomerIp("国富trans");
		
		String ip=(String) list.get(0);
		
		String[] ss=ip.split(",");
		
		System.out.println("ip:::::::::::::"+ss[0]);*/
		
		java.sql.Connection c=achieveTemp.getConnction("com.mysql.jdbc.Driver","jdbc:mysql://10.237.50.122:3306/vms","root", "passwd!@#");
		
		String sql="INSERT INTO vms_trans_info(trans_id,instcode,qdflag,chernum,repnum,TTMPRCNO,"
				+ "CUSTOMER_ID,TRANS_CURR,AMT_CCY,TRANS_DATE,REMARK,FAPIAO_TYPE,FEETYP,BILLFREQ,"
				+ "POLYEAR,HISSDTE,PLANLONGDESC,INSTFROM,INSTTO,OCCDATE,PREMTERM,TRANS_TYPE,INSCOD,"
				+ "INSNAM,AMT_CNY,TAX_AMT_CNY,INCOME_CNY,VAT_RATE_CODE)SELECT BUSINESS_ID ,INST_ID, "
				+ "QD_FLAG,CHERNUM,REPNUM,TTMPRCNO,CUSTOMER_NO,ORIGCURR ,ORIGAMT ,TRDT , BATCTRCDE , "
				+ "INVTYP,FEETYP ,BILLFREQ , POLYEAR, HISSDTE , PLANLONGDESC, INSTFROM,INSTTO,OCCDATE,"
				+ "PREMTERM,INS_COD,INS_COD,INS_NAM,AMT_CNY,TAX_AMT_CNY,INCOME_CNY,TAX_RATE FROM vms_trans_info_temp2 where valueFlage='0' ";
		
		String sql2="update vms_trans_info_temp2 set valueFlage='88' where valueFlage='0'";
		
		ResultSet rs=null;
		java.sql.Statement st = null;
		
		try {
			st=c.createStatement();
			
			int sum=st.executeUpdate(sql);
			st.executeUpdate(sql2);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		if(rs != null){   // 关闭记录集     
		     try{     
		         rs.close() ;     
		     }catch(SQLException e){     
		         e.printStackTrace() ;     
		     }     
		       }     
		       if(st != null){   // 关闭声明     
		     try{     
		         st.close() ;     
		     }catch(SQLException e){     
		         e.printStackTrace() ;     
		     }     
		       }     
		       if(c != null){  // 关闭连接对象     
		      try{     
		         c.close() ;     
		      }catch(SQLException e){     
		         e.printStackTrace() ;     
		      }     
		       } 

		
	}
	
	
	


	public static void main(String [] arg){
		
		InsertIntotransInfo.insertIntoTransInfo();
		
		
	}
	
	
}
