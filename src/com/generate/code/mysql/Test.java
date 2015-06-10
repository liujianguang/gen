/**
 * 
 */
package com.generate.code.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.generate.code.ConfigConst;
import com.generate.code.model.TableFieldInfo;
import com.generate.code.out.FileOut;


/**
 * @author Administrator
 *
 */
public class Test {
	private static List<TableFieldInfo> tableFieldInfoList = new ArrayList<TableFieldInfo>();
	public static void main(String[] args) {
		ConfigConst.init();
		DBHelper dbUtil = new DBHelper();
		String basePackageStr = "com.zyt.";
		String strTableName = "gldkdg_department";
		String className = "Department";
//		String strTableName = "gldkdg_security_roles";
//		String className = "Role";
		ResultSet rs = null;
		try {
			Connection conn = dbUtil.getConnection();
			DatabaseMetaData dmd = conn.getMetaData();
			rs = dmd.getColumns( null, "%", strTableName, "%");
			while( rs.next() )
			{
				TableFieldInfo model = new TableFieldInfo();
				model.setName(rs.getString(4));// 字段名称
				model.setType(rs.getString(6)); // 字段类型
				model.setLength(rs.getString(7)); // 字段长度
				model.setNote(rs.getString(12)); // 字段注释
				tableFieldInfoList.add(model);
			}
			FileOut.outVO(basePackageStr,className,strTableName, tableFieldInfoList);
			FileOut.outDao(basePackageStr, className);
			FileOut.outDaoImpl(basePackageStr, className);
//			//FileOut.outService(basePackageStr, className);
//			FileOut.outServiceImpl(basePackageStr, className);
			FileOut.outAction(basePackageStr, className);
			FileOut.outServiceByTemplate(basePackageStr, className);
			FileOut.outServiceImplByTemplate(basePackageStr, className);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}finally{
					rs =null;
				}
			}
			dbUtil.close();	
		}
	}
	
	

}
