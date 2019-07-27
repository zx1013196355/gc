package cc.moq.gc.creator.base.impl;

import cc.moq.gc.creator.base.ICreateCode;
import cc.moq.gc.creator.base.TableConfig;
import cc.moq.gc.creator.util.TemplateUtils;
import cc.moq.gc.utils.IOUtils;
import org.beetl.core.Template;

import java.io.File;
import java.io.IOException;

public class CreateMapperXml implements ICreateCode {

	@Override
	public void create(TableConfig p) throws IOException {
		Template t = TemplateUtils.getTemplate("mapperXml.btl");
		t.binding("cp", p);
		String str = t.render();
		
		String pname = p.getMapperPackage().replaceAll("\\.", "//")+File.separator+"xml";
		
		File root = new File(p.getPath());
		File file = new File(root + File.separator + pname,p.getBaseClassName()+"Mapper.xml");
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		
		IOUtils.fileWrite(file.getAbsolutePath(), str);
	}
	
	
	
}
