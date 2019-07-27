package cc.moq.gc.creator.base;

import cc.moq.gc.creator.base.impl.CreateMapperJava;
import cc.moq.gc.creator.base.impl.CreateMapperXml;
import cc.moq.gc.creator.base.impl.CreateModel;
import cc.moq.gc.creator.base.impl.CreateService;
import cc.moq.gc.creator.util.CreateQueryUtil;
import cc.moq.gc.creator.util.DBUtils;
import cc.moq.gc.utils.StrUtils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class TableConfig {
	private List<ColumnPropery> props;
	private String tableName;
	private String baseClassName;
	private String modelPackage;
	private String mapperPackage;
	private String servicePackage;
	private String actionPackage;
	private String aliasName = "a";
	private String alisColums;
	private String colums;
	private String path;
	private String querySql;
	private String sort = "${sort}";
	private String order = "${order}";
	private Connection conn;
	private Map<String,String> pks;
	
	
	
	/**
	 * 配置表的基本信息
	 * @param tableName      表名
	 * @param baseClassName  类名(一般是指model的类名)
	 * @param modelPackage   模型包名
	 * @param mapperPackage  mapper包名
	 * @param servicePackage service包名
	 * @param path           代码保存路径
	 * @param aliasName      别名，如果是不生成mapper.xml不用传
	 * @param conn           数据库连接 ，用于读取字段
	 * @throws SQLException
	 */
	public TableConfig(String tableName,String baseClassName, String actionPackage,String modelPackage,String mapperPackage,String servicePackage,String path,String aliasName,Connection conn) throws SQLException {
		this.tableName = tableName;
		this.baseClassName = baseClassName;
		this.modelPackage = modelPackage;
		this.actionPackage = actionPackage;
		this.mapperPackage = mapperPackage;
		this.servicePackage = servicePackage;
		this.path = path;
		
		this.conn = conn;
		
		//读取表结构
		props = DBUtils.getColumns(conn, tableName);
		pks = DBUtils.getPks(conn,tableName);
		if(pks != null && !pks.isEmpty()){
			for(ColumnPropery p : props){
				if(pks.containsKey(p.getCol())){
					p.setIsPk(true);
				}else{
					p.setIsPk(false);
				}
			}
		}
		
		StringBuilder colums = new StringBuilder();
		StringBuilder alisColums = new StringBuilder();
		for (int i = 0; i < props.size(); i++) {
			colums.append(StrUtils.camelhumpToUnderline(props.get(i).getCol()));
			alisColums.append(aliasName+"."+StrUtils.camelhumpToUnderline(props.get(i).getCol()));
			if (i < props.size() - 1) {
				colums.append(",");
				alisColums.append(",");
			}
		}
		this.colums = colums.toString();
		this.aliasName = aliasName;
		this.alisColums = alisColums.toString();
		this.querySql = CreateQueryUtil.createQuery(this.aliasName+".", this.props);
	}
	
	
	public void create(boolean model,boolean mapperJava,boolean mapperXml,boolean service) throws IOException{
		ICreateCode ic = null;
		if(model){
			ic = new CreateModel();
			ic.create(this);
		}
		if(mapperJava){
			ic = new CreateMapperJava();
			ic.create(this);
		}
		if(mapperXml){
			ic = new CreateMapperXml();
			ic.create(this);
		}
		if(service){
			ic = new CreateService();
			ic.create(this);
		}
		
//		ic = new CreateAction();
//		ic.create(this);
	}
	
	
	public List<ColumnPropery> getProps() {
		return props;
	}

	public void setProps(List<ColumnPropery> props) {
		this.props = props;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}
	
	public String getMinClassName(){
		return StrUtils.firstCharToLower(StrUtils.underlineToCamelhump(this.baseClassName));
	}

	public String getBaseClassName() {
		return baseClassName;
	}

	public void setBaseClassName(String baseClassName) {
		this.baseClassName = baseClassName;
	}

	public String getModelPackage() {
		return modelPackage;
	}

	public void setModelPackage(String modelPackage) {
		this.modelPackage = modelPackage;
	}

	public String getMapperPackage() {
		return mapperPackage;
	}

	public void setMapperPackage(String mapperPackage) {
		this.mapperPackage = mapperPackage;
	}

	public String getServicePackage() {
		return servicePackage;
	}

	public void setServicePackage(String servicePackage) {
		this.servicePackage = servicePackage;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getAlisColums() {
		return alisColums;
	}

	public void setAlisColums(String alisColums) {
		this.alisColums = alisColums;
	}

	public String getColums() {
		return colums;
	}

	public void setColums(String colums) {
		this.colums = colums;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}


	public String getQuerySql() {
		return querySql;
	}


	public void setQuerySql(String querySql) {
		this.querySql = querySql;
	}


	public String getSort() {
		return sort;
	}


	public void setSort(String sort) {
		this.sort = sort;
	}


	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public Map<String, String> getPks() {
		return pks;
	}
	public void setPks(Map<String, String> pks) {
		this.pks = pks;
	}


	public String getActionPackage() {
		return actionPackage;
	}


	public void setActionPackage(String actionPackage) {
		this.actionPackage = actionPackage;
	}
	
	
}
