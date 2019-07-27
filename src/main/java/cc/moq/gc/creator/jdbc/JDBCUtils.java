package cc.moq.gc.creator.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 数据库连接类
 * @author yunmel
 *
 */
public class JDBCUtils {
	
	public static String createMySqlConnectStr(DBConnProperty dbPropety){
		String connStr = "jdbc:mysql://{ip}:{port}/{sid}?user={user}&password={pwd}";
		connStr = connStr.replace("{ip}", dbPropety.getIp())
		.replace("{port}", dbPropety.getPort())
		.replace("{sid}", dbPropety.getSid())
		.replace("{user}", dbPropety.getUsername())
		.replace("{pwd}", dbPropety.getPassword());
		return connStr;
	}
	
	//"com.mysql.jdbc.Driver"
	public static Connection getConnection(String connStr,String driver){
		try {
			Class.forName(driver).newInstance();
		    Connection conn = DriverManager
		      .getConnection(connStr);
		    return conn;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	//关闭连接
	public static void release(Connection conn,Statement st,ResultSet rs){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				conn = null;
			}
		}
		
		if(st!=null){
			try {  
				st.close();
			} catch (SQLException e) {
				st = null;
			}
		}
		
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				rs = null;
			}
		}		
	}
}
