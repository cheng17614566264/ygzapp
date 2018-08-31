package fmss.common.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

import org.hibernate.cfg.Configuration;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.sun.org.apache.bcel.internal.generic.BranchHandle;


public class achieveTimeFromDB extends SQLDAO {
	
	
	public static String atfd(){
	
	AbstractBaseEntityManager ab=new AbstractBaseEntityManager();
	
	
	
	java.sql.Connection c=SQLDAO.getConnction("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/ygzapp3", "ygzapp", "ygzapp");
	
	String sql="select now()";
	
	String name=null;
	try {
		
		java.sql.Statement st=c.createStatement();
		
		ResultSet rs=st.executeQuery(sql);
		
		
		  while(rs.next()){                  
              name = rs.getString(1);
     
              System.out.println(name);
          }

	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return name;
	
	}
	
/*	public static void main(String[] args){
		achieveTimeFromDB.atfd();
		
		
	}*/
	
}
