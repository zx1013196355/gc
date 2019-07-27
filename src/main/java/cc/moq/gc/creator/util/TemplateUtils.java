package cc.moq.gc.creator.util;

import java.io.IOException;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.ResourceLoader;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.core.resource.FileResourceLoader;

public class TemplateUtils {
	private static GroupTemplate gt;
	public static Template getTemplate(String path)throws IOException{
		if(gt == null){
			init();
		}

		ResourceLoader loader = new FileResourceLoader();
		((FileResourceLoader) loader).setRoot("F:\\gc\\src\\main\\java\\cc\\moq\\gc\\creator\\template");
		gt.setResourceLoader(loader);

		return gt.getTemplate(path);
	}
	
	private static void init() throws IOException{
		ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader();
		Configuration cfg = Configuration.defaultConfiguration();
		gt = new GroupTemplate(resourceLoader, cfg);
	}
	

}
