package cc.moq.gc.creator.util;

import cc.moq.gc.creator.base.ColumnPropery;
import cc.moq.gc.utils.StrUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBUtils {
	
	public static List<String> getTables(Connection conn)
			throws SQLException {
		DatabaseMetaData meta = conn.getMetaData();
		ResultSet tableRet = meta.getTables(null, "%", "%",
				new String[] { "table" });
		List<String> tables = new ArrayList<String>();
		while (tableRet.next()) {
			tables.add(tableRet.getString("TABLE_NAME").toLowerCase());
		}
		return tables;
	}
	
	
	public static List<ColumnPropery> getColumns(Connection conn, String tableName)
			throws SQLException {
		DatabaseMetaData meta = conn.getMetaData();
		ResultSet colRet = meta.getColumns(null, "%", tableName, "%");
		ResultSet pks = meta.getPrimaryKeys(null, null, tableName);
		
		
		
		
		List<ColumnPropery> configs = new ArrayList<ColumnPropery>();
		while (colRet.next()) {
			
			ColumnPropery config = new ColumnPropery();
			int digits = 0;
			try {
				digits = colRet.getInt("DECIMAL_DIGITS");
			} catch (Exception e) {

			}
			int lenght = 0;
			try {
				lenght = colRet.getInt("COLUMN_SIZE");
			} catch (Exception e) {
			}

			config.setCol(StrUtils.underlineToCamelhump(colRet.getString("COLUMN_NAME").toLowerCase()));//转为驼峰风格
			String type = colRet.getString("TYPE_NAME").toUpperCase();

			if ("VARCHAR".equals(type) || "CHAR".equals(type)
					|| "TEXT".equals(type)) {
				config.setColType("String");
			} else if ("BIGINT".equals(type)) {
				config.setColType("Long");
			} else if ("INT".equals(type)) {
				config.setColType("Integer");
			} else if ("TINYINT".equals(type) || "BIT".equals(type)) {
//				if (lenght == 1) {
//					config.setColType("Boolean");
//				} else {
					config.setColType("Integer");
//				}
			} else if ("DATE".equals(type)) {
				config.setColType("Date");
			} else if ("DATETIME".equals(type)) {
				config.setColType("Date");
			} else if ("FLOAT".equals(type) || "NUMERIC".equals(type)) {
				if (digits > 5) {
					config.setColType("Double");
				} else {
					config.setColType("Float");
				}
			} else {
				config.setColType("String");
			}

			try {
				config.setLength(colRet.getInt("COLUMN_SIZE"));
			} catch (Exception e) {

			}
			config.setCnName(colRet.getString("REMARKS"));
			configs.add(config);
		}
		return configs;
	}


	public static Map<String, String> getPks(Connection conn, String tableName) throws SQLException {
		DatabaseMetaData meta = conn.getMetaData();
		ResultSet pks = meta.getPrimaryKeys(null, null, tableName);
		Map<String,String> pkMap = new HashMap<String,String>();
		while (pks.next()) {
			pkMap.put(StrUtils.underlineToCamelhump(pks.getString("COLUMN_NAME").toLowerCase()), "");
		}
		return pkMap;
	}
}
