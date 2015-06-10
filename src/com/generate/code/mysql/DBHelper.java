package com.generate.code.mysql;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DBHelper {
	public static String url = "jdbc:mysql://127.0.0.1/student";
	public static String name = "com.mysql.jdbc.Driver";
	public static String user = "root";
	public static String password = "root";
	
	static{
		File file = FileDoUtil.findFile("config/db/db.properties");
		if(file != null){
			try{
				FileInputStream fIn = new FileInputStream(file);
				Properties p = new Properties();
				p.load(fIn);
				url = p.getProperty("db.url");
				name = p.getProperty("db.jdbc");
				user = p.getProperty("db.username");
				password = p.getProperty("db.password");
				fIn.close();
				fIn = null;
			}catch(Exception e){
				System.out.println("[error]load and read db properties error.............");
				e.printStackTrace();
			}
			
		}else{
			System.out.println("[error]the file not found......");
		}
	}

	public Connection conn = null;
	public PreparedStatement pst = null;
	
	public DBHelper() {
		try {
			Class.forName(name);//指定连接类型
			conn = DriverManager.getConnection(url, user, password);//获取连接
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection getConnection(){
		return conn;
	}

	public DBHelper(String sql) {
		try {
			Class.forName(name);//指定连接类型
			conn = DriverManager.getConnection(url, user, password);//获取连接
			pst = conn.prepareStatement(sql);//准备执行语句
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if(pst != null){
				this.pst.close();
			}
			if(conn != null){
				this.conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
