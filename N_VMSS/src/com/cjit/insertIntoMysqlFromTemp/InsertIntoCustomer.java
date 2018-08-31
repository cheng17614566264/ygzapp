package com.cjit.insertIntoMysqlFromTemp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cjit.achieveTemp.achieveTemp;
import com.cjit.inserFromTemp.ipAccess.IpAccess;
import com.cjit.vms.BatchRun.model.BatchRunTime;

public class InsertIntoCustomer {
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
		IpAccess ia=new IpAccess();
		
		/*List list=ia.selectCustomerIp("国富Customer");
		
		String ip=(String) list.get(0);
		
		String[] ss=ip.split(",");
		
		System.out.println("ip:::::::::::::"+ip);*/
		
		java.sql.Connection c=achieveTemp.getConnction("com.mysql.jdbc.Driver","jdbc:mysql://10.237.50.122:3306/vms","root", "passwd!@#");
		
		String sql="INSERT INTO vms_customer_info(CUSTOMER_ID,CUSTOMER_CNAME,CUSTOMER_TAXNO,CUSTOMER_ADDRESS,"
				+ "TAXPAYER_TYPE,CUSTOMER_PHONE,CUSTOMER_CBANK,CUSTOMER_ACCOUNT)SELECT CUSTOMER_NO,CUSTOMER_NAME,"
				+ "CUSTOMER_TAXNO,CUSTOMER_ADDRESSAND,TAXPAYER_TYPE,CUSTOMER_PHONE,CUSTOMER_BANKAND,CUSTOMER_ACCOUNT "
				+ "FROM vms_customer_temp where date1>ADDDATE(DATE_FORMAT(NOW(),'%Y-%m-%d %H:%m:%s'),INTERVAL -10 MINUTE);";
		
		ResultSet rs=null;
		java.sql.Statement st = null;
		
		try {
			st=c.createStatement();
			
			int sum=st.executeUpdate(sql);
	       
			
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
	
	
	public static List findBatchRunTime(){
		List batchRunTime =new ArrayList();
		java.sql.Connection c=achieveTemp.getConnction("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/ygzapp3","ygzapp", "ygzapp");
		
		String sql="SELECT HOUR,MINUTE,SECOND FROM batchruntime where cname='核心'";	
		
		ResultSet rs=null;
		java.sql.Statement st = null;
		
		try {
			st=c.createStatement();
			
			rs=st.executeQuery(sql);
			
			
			while(rs.next()){         //rs.next()   表示如果结果集rs还有下一条记录，那么返回true；否则，返回false
                int hour = rs.getInt(1);
                int minute = rs.getInt(2);
                int second= rs.getInt(3);
                System.out.println(hour+"--->"+minute+"--------"+second);
                
                batchRunTime.add(hour);
                batchRunTime.add(minute);
                batchRunTime.add(second);
                
            }
	       
			
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
		   	return batchRunTime;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
/*   public static void main(String [] arg){
		
		InsertIntotransInfo.insertIntoTransInfo();
		
        IpAccess ia=new IpAccess();
		
		List list=ia.selectCustomerIp("国富customer");
		
		String ip=(String) list.get(0);
		
		System.out.println("ip:::::::::::::"+ip);
		
		
	}*/
	
	
	
	
}
