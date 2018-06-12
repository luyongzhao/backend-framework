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
public class ApiControllerGendertor {
	
	public static String FACADE_TEMPLATE = "generator/ApiController.ftl";
	
	private static Configuration cfg = new Configuration();
	
	static{
		StringTemplateLoader templates = new StringTemplateLoader();
		templates.putTemplate(FACADE_TEMPLATE, templateContent(FACADE_TEMPLATE));
		cfg.setTemplateLoader(templates);
	}
	
	private static String templateContent(String file)
	{
		InputStream in = DTOGenderator.class.getClassLoader().getResourceAsStream(file);
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
	
	public static void generator(String storePath, String entityName, String entityDesc, String controllerPkgName, String servicePkgName, String entityPkgName, String mappingPath){
		
		File dir = new File(storePath);
		if (!dir.exists())
		{
			dir.mkdirs();
		}

		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("entityName", entityName);
		datas.put("controllerPkgName", controllerPkgName);
		datas.put("servicePkgName", servicePkgName);
		datas.put("entityPkgName",entityPkgName);
		datas.put("entityDesc",entityDesc);
		datas.put("mappingPath",mappingPath);

		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(dir, entityName + "Controller.java"));
			cfg.getTemplate(FACADE_TEMPLATE).process(datas, fw);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(fw);
		}
		
	}
}
