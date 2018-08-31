package com.cjit.achieveTemp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cjit.achieveTemp.model.achieveTempModelTransInfo;

public class AchieveTempTransInfo {
	public static Connection getConnction(String driverClass, String dburl,
			String user, String pass){
		Connection dbConnection = null;
		try{
			Class.forName(driverClass).newInstance();
			dbConnection = DriverManager.getConnection(dburl, user, pass);
			System.out.println("trans+数据库连接成功");
		}catch (Exception e){
			e.printStackTrace();
		}
		return dbConnection;
	}
	
	
	public static void atti(){
		java.sql.Connection c=achieveTemp.getConnction("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/heixntiaoshi","ygzapp", "ygzapp");
		
		String sql2="select * from vms_trans_info_temp2";
		
		
		
		List<achieveTempModelTransInfo> list2=new ArrayList<achieveTempModelTransInfo>();
		try {
		
			java.sql.Statement st=c.createStatement();
		
			ResultSet rs2=st.executeQuery(sql2);

			  while(rs2.next()){    
				  
				  achieveTempModelTransInfo atmti=new achieveTempModelTransInfo();
				
	              //获取vms_trans_info_tempe2中的数据

				  atmti.setBUSINESS_ID(rs2.getString(1));
				  atmti.setINST_ID(rs2.getString(2));
				  atmti.setQD_FLAG(rs2.getString(3));
				  atmti.setCHERNUM(rs2.getString(4));
				  atmti.setREPNUM(rs2.getString(5));
				  atmti.setTTMPRCNO(rs2.getString(6));
				  atmti.setCUSTOMER_NO(Integer.parseInt(rs2.getString(7)));
				  atmti.setORIGCURR(rs2.getString(8));
				  atmti.setORIGAMT(Double.parseDouble(rs2.getString(9)));
				  atmti.setACCTAMT(Double.parseDouble(rs2.getString(10)));
				  atmti.setTRDT(rs2.getString(11));
				  atmti.setBATCTRCDE(rs2.getString(12));
				  atmti.setINVTYP(rs2.getString(13));
				  atmti.setFEETYP(rs2.getString(14));
				  atmti.setBILLFREQ(rs2.getString(15));
				  atmti.setPOLYEAR(Integer.parseInt(rs2.getString(16)));
				  atmti.setHISSDTE(rs2.getString(17));
				  atmti.setPLANLONGDESC(rs2.getString(18));
				  atmti.setINSTFROM(rs2.getString(19));
				  atmti.setINSTTO(rs2.getString(20));
				  atmti.setOCCDATE(rs2.getString(21));
				  atmti.setPREMTERM(Integer.parseInt(rs2.getString(22)));
				  atmti.setTRANSTYPE(rs2.getString(23));
				  atmti.setINS_COD(rs2.getString(24));
				  atmti.setINS_NAM(rs2.getString(25));
				  atmti.setAMT_CNY(Double.parseDouble(rs2.getString(26)));
				  atmti.setTAX_AMT_CNY(Double.parseDouble(rs2.getString(27)));
				  atmti.setINCOME_CNY(Double.parseDouble(rs2.getString(28)));
				  atmti.setTAX_RATE(rs2.getString(29));
				  atmti.setValueflage(rs2.getString(30));
				  atmti.setTransId(Integer.parseInt(rs2.getString(31)));
	              
	           
	              list2.add(atmti);
	              System.out.println(atmti.getBUSINESS_ID());
	          }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


	public static void main(String [] arg){
		
		AchieveTempTransInfo.atti();
		
		
	}
	
}
