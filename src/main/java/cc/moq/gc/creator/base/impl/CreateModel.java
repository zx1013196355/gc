package cc.moq.gc.creator.base.impl;

import cc.moq.gc.creator.base.ICreateCode;
import cc.moq.gc.creator.base.TableConfig;
import cc.moq.gc.creator.util.TemplateUtils;
import cc.moq.gc.utils.IOUtils;
import org.beetl.core.Template;

import java.io.File;
import java.io.IOException;

public class CreateModel implements ICreateCode {

	@Override
	public void create(TableConfig p) throws IOException {
		Template t = TemplateUtils.getTemplate("model.btl");
		t.binding("cp", p);
		String str = t.render();
		
		File root = new File(p.getPath());
		String pname = p.getModelPackage().replaceAll("\\.", "//");
		
		File file = new File(root+File.separator+pname,p.getBaseClassName()+".java");
		if(!file.getParentFile().exists()){
			file.getParentFile().mkdirs();
		}
		
		IOUtils.fileWrite(file.getAbsolutePath(), str);
	}
	
	
	
}
