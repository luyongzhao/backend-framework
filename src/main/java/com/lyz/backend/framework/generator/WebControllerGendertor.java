package com.lyz.backend.framework.generator;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 业务逻辑生成模板
 * @author luyongzhao
 *
 */
public class WebControllerGendertor {
	
	public static String FACADE_TEMPLATE = "generator/WebController.ftl";
	
	private static Configuration cfg = new Configuration();
	
	static{
		StringTemplateLoader templates = new StringTemplateLoader();
		templates.putTemplate(FACADE_TEMPLATE, templateContent(FACADE_TEMPLATE));
		cfg.setTemplateLoader(templates);
	}
	
	private static String templateContent(String file)
	{
		InputStream in = DTOGenderator.class.getResourceAsStream(file);
		try
		{
			return IOUtils.toString(in);
		} catch(Exception e){
			e.printStackTrace();
			return null;
		}finally
		{
			IOUtils.closeQuietly(in);
		}
	}
	
	public static void generator(String storePath, String entityName, String pkgName){
		
		File dir = new File(storePath);
		if (!dir.exists())
		{
			dir.mkdirs();
		}

		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("entityName", entityName);
		datas.put("pkgName", pkgName);
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(dir, entityName + "Controller.java"));
			cfg.getTemplate(FACADE_TEMPLATE).process(datas, fw);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(fw);
		}
		
	}
}
