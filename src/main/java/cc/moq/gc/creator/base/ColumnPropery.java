package cc.moq.gc.creator.base;

import cc.moq.gc.utils.StrUtils;

/**
 * 列的属性
 * 从数据属读取
 * @author yunmel
 *
 */
public class ColumnPropery {
	private	String	col;			//col
	private	String	cnName;			//cn_name
	private String  colType;
	private	Integer	length;			//length
	private boolean isPk;
	
	
	public String getCol() {
		return col;
	}
	public void setCol(String col) {
		this.col = col;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	public String getColType() {
		return colType;
	}
	public void setColType(String colType) {
		this.colType = colType;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	
	
	public String getMinName(){
		return StrUtils.underlineToCamelhump(col);
	}
	
	public String getMaxName(){
		return StrUtils.firstCharToUpper(StrUtils.underlineToCamelhump(col));
	}
	public boolean getIsPk() {
		return isPk;
	}
	public void setIsPk(boolean isPk) {
		this.isPk = isPk;
	}
	
	
}
