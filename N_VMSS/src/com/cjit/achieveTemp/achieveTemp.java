package com.cjit.achieveTemp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.cjit.achieveTemp.model.achieveTempModel;


//通过jdbc模式抓取中间表信息
public class achieveTemp extends JdbcDaoSupport {
	
	public static Connection getConnction(String driverClass, String dburl,
			String user, String pass){
		Connection dbConnection = null;
		try{
			Class.forName(driverClass).newInstance();
			dbConnection = DriverManager.getConnection(dburl, user, pass);
			System.out.println("customer+数据库连接成功");
		}catch (Exception e){
			e.printStackTrace();
		}
		return dbConnection;
	}
	
	
	public static List at(){
		java.sql.Connection c=achieveTemp.getConnction("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/heixntiaoshi","ygzapp", "ygzapp");
		
		String sql="select * from vms_customer_temp";
		
		ResultSet rs=null;
		java.sql.Statement st = null;
		List<achieveTempModel> list1=new ArrayList<achieveTempModel>();
		
		try {
			st=c.createStatement();
			
			rs=st.executeQuery(sql);
			

			  while(rs.next()){    
				  
				  achieveTempModel atm=new achieveTempModel();
	              atm.setCUSTOMER_NO( Integer.parseInt(rs.getString(1)));
	              atm.setCUSTOMER_NAME(rs.getString(2));
	              atm.setCUSTOMER_TAXNO(rs.getString(3));
	              atm.setCUSTOMER_ADDRESSAND(rs.getString(4));
	              atm.setTAXPAYER_TYPE(rs.getString(5));
	              atm.setCUSTOMER_PHONE(rs.getString(6));
	              atm.setCUSTOMER_BANKAND(rs.getString(7));
	              atm.setCUSTOMER_ACCOUNT(rs.getString(8));
 
	              list1.add(atm);
	              
	              System.out.println("customerName:"+atm.getCUSTOMER_NAME());
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
		 return list1;
		
	}
	
	
	


	public static void main(String [] arg){
		
		achieveTemp.at();
		
		
	}
	
	
	
}
