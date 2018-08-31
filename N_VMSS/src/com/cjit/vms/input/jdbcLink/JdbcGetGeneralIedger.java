package com.cjit.vms.input.jdbcLink;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cjit.common.service.impl.GenericServiceImpl;
import com.cjit.gjsz.system.model.Organization;
import com.cjit.gjsz.system.model.User;
import com.cjit.vms.input.jdbcLink.service.JdbcGetGeneralLedgerService;
import com.cjit.vms.trans.action.DataDealAction;

public class JdbcGetGeneralIedger extends DataDealAction{
	//JdbcGetGeneralLedgerService jdbcGetGeneralIedger;
	public List jdbcGetGeneralIedger() throws ClassNotFoundException, SQLException{
		//获取上月日期
		Calendar calendar=Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)-1);
		String yearMonth = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
		
		System.out.println("开始获取数据...");
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    PreparedStatement stmt1 = null;
	    PreparedStatement stmt2 = null;
	    PreparedStatement stmt3 = null;
	    ResultSet rs = null;
	    ResultSet rs1 = null;
	    ResultSet rs2 = null;
	    ResultSet rs3 = null;
	    List list=new ArrayList();
	    Class.forName("oracle.jdbc.OracleDriver");
        System.out.println("开始尝试连接数据库！");
       /* Map<String, String> map2 =new HashMap<String, String>() ;
		map2.put("systemt", "zongzhang");
        List list2=jdbcGetGeneralIedger.findGeneralLedgerUrl("findGeneralLedgerUrl", map2);
        String str=(String) list2.get(0);
        System.out.println(str);*/
        String url = "jdbc:oracle:" + "thin:@10.10.100.30:1547:UAT";
        String user = "zzsuser";
        String password = "zzsuser";
        conn = DriverManager.getConnection(url, user, password);
        System.out.println("连接成功！");
        //String sql = "SELECT * FROM APPS.VMS_ACC_BALANCE_INFO";
        String sql = "SELECT * FROM APPS.VMS_ACC_BALANCE_INFO WHERE YEAR_MONTH = '" + yearMonth + "'";
        String mon = yearMonth.split("-")[1];
		if("12".equals(mon)){
			String sql1 = "select * from apps.vms_acc_balance_info where year_month = '" + yearMonth.split("-")[0] + "-13'";
			String sql2 = "select * from apps.vms_acc_balance_info where year_month = '" + yearMonth.split("-")[0] + "-14'";
			String sql3 = "select * from apps.vms_acc_balance_info where year_month = '" + yearMonth.split("-")[0] + "-15'";
			
			stmt1 = conn.prepareStatement(sql1);
			stmt2 = conn.prepareStatement(sql2);
			stmt3 = conn.prepareStatement(sql3);
			
	        rs1 = stmt1.executeQuery();
	        rs2 = stmt2.executeQuery();
	        rs3 = stmt3.executeQuery();
	        
	        /*if(rs3.next()){
	        	list.add(getJdbcGeneral(rs3,stmt3,conn));
	        	return list;
	        }
	        if(rs2.next()){
	        	list.add(getJdbcGeneral(rs2,stmt2,conn));
	        	return list;
	        }
	        if(rs1.next()){
	        	list.add(getJdbcGeneral(rs1,stmt1,conn));
	        	return list;
	        }*/
	        
	        //国富项目修改，2018-02-12
	        if(rs1.next()){
	        	list.add(getJdbcGeneral(rs1,stmt1,conn));
	        }
	        if(rs2.next()){
	        	list.add(getJdbcGeneral(rs2,stmt2,conn));
	        }
	        if(rs3.next()){
	        	list.add(getJdbcGeneral(rs3,stmt3,conn));
	        }
		}
        
        stmt = conn.prepareStatement(sql);
        rs = stmt.executeQuery();
        list.add(getJdbcGeneral(rs,stmt,conn));
       
        	
        /*while (rs.next()){
        	Map map=new HashMap();
            // 当结果集不为空时  
        	map.put("INST_ID", rs.getString("INST_ID"));
        	
            String YEAR_MONTH=rs.getString("YEAR_MONTH");

            String[] YEAR_MONTH2=YEAR_MONTH.split("-");
            
            String year=YEAR_MONTH2[0];
            String month=YEAR_MONTH2[1];

            if(month.equals("13")||month.equals("14")||month.equals("15")){
            	map.put("YEAR_MONTH", year+"-12");
            	
            }else{
                map.put("YEAR_MONTH", rs.getString("YEAR_MONTH"));
            }
            map.put("DIRECTION_ID", rs.getString("DIRECTION_ID"));
            map.put("DIRECTION_NAME", rs.getString("DIRECTION_NAME"));
            map.put("PLANLONGDESC_ID", rs.getString("PLANLONGDESC_ID"));
            map.put("PLANLONGDESC_NAME", rs.getString("PLANLONGDESC_NAME"));
            
            String IS_TAXEXEMPTION=rs.getString("IS_TAXEXEMPTION");
            if(IS_TAXEXEMPTION.equals("1")||IS_TAXEXEMPTION.equals("2")||IS_TAXEXEMPTION.equals("3")){
            	
            	map.put("IS_TAXEXEMPTION", "N");
            }else{
            	
            	map.put("IS_TAXEXEMPTION", "Y");
            }
            
            
            map.put("DEBIT_DESC", rs.getString("DEBIT_DESC"));
            map.put("CREDIT_DESC", rs.getString("CREDIT_DESC"));
            map.put("BALANCE_SOURCE", rs.getString("BALANCE_SOURCE"));
  
            list.add(map);
        
		}
        
        if(rs!=null){
        	rs.close();
        }
        if(stmt!=null){
        	stmt.close();
        }
        if(conn!=null){
           conn.close();
        }*/
       return list;
	}
	/*public JdbcGetGeneralLedgerService getJdbcGetGeneralIedger() {
		return jdbcGetGeneralIedger;
	}
	public void setJdbcGetGeneralIedger(JdbcGetGeneralLedgerService jdbcGetGeneralIedger) {
		this.jdbcGetGeneralIedger = jdbcGetGeneralIedger;
	}*/
	
	public Map getJdbcGeneral(ResultSet rs,PreparedStatement stmt,Connection conn) throws ClassNotFoundException, SQLException{
		while (rs.next()){
        	Map map=new HashMap();
            // 当结果集不为空时  
        	map.put("ORIG_INSTCODE", rs.getString("INST_ID"));
        	
        	//2018-07-25新增
        	User user=this.getCurrentUser();
			System.out.println(user.getOrgId()+"***************");//输出86
			List authInstList = this.getAuthInstList();
        	for (int j = 0; j < authInstList.size(); j++) {
				Organization org = (Organization) authInstList.get(j);
				if (rs.getString("INST_ID").equals(org.getId())) {  //成本中心代码未放入session中
					Organization orgs = (Organization) authInstList.get(0);
					System.out.println(orgs);
					map.put("INST_ID", orgs.getId());
				}
			}
            String YEAR_MONTH=rs.getString("YEAR_MONTH");

            String[] YEAR_MONTH2=YEAR_MONTH.split("-");
            
            String year=YEAR_MONTH2[0];
            String month=YEAR_MONTH2[1];

            if(month.equals("13")||month.equals("14")||month.equals("15")){
            	map.put("YEAR_MONTH", year+"-12");
            	
            }else{
                map.put("YEAR_MONTH", rs.getString("YEAR_MONTH"));
            }
            map.put("DIRECTION_ID", rs.getString("DIRECTION_ID"));
            map.put("DIRECTION_NAME", rs.getString("DIRECTION_NAME"));
            map.put("PLANLONGDESC_ID", rs.getString("PLANLONGDESC_ID"));
            map.put("PLANLONGDESC_NAME", rs.getString("PLANLONGDESC_NAME"));
            
            String IS_TAXEXEMPTION=rs.getString("IS_TAXEXEMPTION");
            if(IS_TAXEXEMPTION.equals("1")||IS_TAXEXEMPTION.equals("2")||IS_TAXEXEMPTION.equals("3")){
            	
            	map.put("IS_TAXEXEMPTION", "N");
            }else{
            	
            	map.put("IS_TAXEXEMPTION", "Y");
            }
            
            map.put("DEBIT_DESC", rs.getString("DEBIT_DESC"));
            map.put("CREDIT_DESC", rs.getString("CREDIT_DESC"));
            map.put("BALANCE_SOURCE", rs.getString("BALANCE_SOURCE"));
            
            if(rs!=null){
            	rs.close();
            }
            if(stmt!=null){
            	stmt.close();
            }
            if(conn!=null){
            	conn.close();
            }
  
            return map;
		}
		return null;
		
	}

}
