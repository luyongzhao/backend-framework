package com.mobike.iotcloud.backend.framework.generator;

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
public class FacadeGendertor {
	
	public static String FACADE_TEMPLATE = "facade.ftl";
	public static String MONGODB_FACADE_TEMPLATE = "mongodbFacade.ftl";
	
	private static Configuration cfg = new Configuration();
	
	static{
		StringTemplateLoader templates = new StringTemplateLoader();
		templates.putTemplate(FACADE_TEMPLATE, templateContent(FACADE_TEMPLATE));
		templates.putTemplate(MONGODB_FACADE_TEMPLATE, templateContent(MONGODB_FACADE_TEMPLATE));
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
	
	public static void generator(String storePath, String entityName, String entityDesc, String pkgName, boolean isMysql,String moduleName,String memKey){
		
		File dir = new File(storePath);
		if (!dir.exists())
		{
			dir.mkdirs();
		}

		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("entityName", entityName);
		datas.put("pkgName", pkgName);
		datas.put("entityDesc", entityDesc);
		datas.put("moduleName", moduleName);
		datas.put("memKey", memKey);
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(dir, entityName + "Facade.java"));
			if(isMysql)
			{
				cfg.getTemplate(FACADE_TEMPLATE).process(datas, fw);
			}else
			{
				cfg.getTemplate(MONGODB_FACADE_TEMPLATE).process(datas, fw);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(fw);
		}
		
	}
}
