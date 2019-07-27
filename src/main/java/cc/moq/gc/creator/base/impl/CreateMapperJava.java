package cc.moq.gc.creator.base.impl;

import cc.moq.gc.creator.base.ICreateCode;
import cc.moq.gc.creator.base.TableConfig;
import cc.moq.gc.creator.util.TemplateUtils;
import cc.moq.gc.utils.IOUtils;
import org.beetl.core.Template;

import java.io.File;
import java.io.IOException;

public class CreateMapperJava implements ICreateCode {

	@Override
	public void create(TableConfig p) throws IOException {
		Template t = TemplateUtils.getTemplate("mapper.btl");
		t.binding("cp", p);
		String str = t.render();
		
		File root = new File(p.getPath());
		
		String pname = p.getMapperPackage().replaceAll("\\.", "//");
		
		
		File file = new File(root+File.separator+pname, p.getBaseClassName()+"Mapper.java");
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		
		IOUtils.fileWrite(file.getAbsolutePath(), str);
	}
	
	
	
}
