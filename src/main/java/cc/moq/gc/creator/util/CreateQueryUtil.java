package cc.moq.gc.creator.util;

import cc.moq.gc.creator.base.ColumnPropery;
import cc.moq.gc.utils.StrUtils;

import java.util.List;

public class CreateQueryUtil {
	
	public static String createQuery(String preffix,List<ColumnPropery> configs){
		StringBuffer sb = new StringBuffer();
		for (ColumnPropery c : configs) {
			if (c.getCol().equals("id") || c.getCol().equalsIgnoreCase("delFlag")) {
				continue;
			}
			if(c.getColType().equals("Date")){
				sb.append("\t\t\t<if test=\""+c.getCol()+"Start != null and "+c.getCol()+"Start !='' \">").append("\n");
				sb.append("\t\t\t    and "+preffix+ StrUtils.camelhumpToUnderline(c.getCol())+" &gt;=  #{"+c.getCol()+"Start}").append("\n");
				sb.append("\t\t\t</if>").append("\n");
				sb.append("\t\t\t<if test=\""+c.getCol()+"End != null and "+c.getCol()+"End !='' \">").append("\n");
				sb.append("\t\t\t    and "+preffix+StrUtils.camelhumpToUnderline(c.getCol())+" &lt;=  #{"+c.getCol()+"End}").append("\n");
				sb.append("\t\t\t</if>").append("\n");
			}else if(c.getColType().equals("Integer") || c.getColType().equals("Long")){
				sb.append("\t\t\t<if test=\""+c.getCol()+" != null and "+c.getCol()+" !='' \">").append("\n");
				sb.append("\t\t\t    and "+preffix+StrUtils.camelhumpToUnderline(c.getCol())+" =  #{"+c.getCol()+"}").append("\n");
				sb.append("\t\t\t</if>").append("\n");
			}else{
				sb.append("\t\t\t<if test=\""+c.getCol()+" != null and "+c.getCol()+" !='' \">").append("\n");
				sb.append("\t\t\t    and "+preffix+StrUtils.camelhumpToUnderline(c.getCol())+" like CONCAT( '%' , #{"+c.getCol()+"}, '%') ").append("\n");
				sb.append("\t\t\t</if>").append("\n");
			}
		}
		return sb.toString();
	}
	
}
