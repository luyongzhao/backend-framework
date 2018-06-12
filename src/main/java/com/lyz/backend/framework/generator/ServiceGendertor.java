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
public class ServiceGendertor {
	
	public static String SERVICE_IMPL_TEMPLATE = "generator/ServiceImpl.ftl";
	public static String SERVICE_TEMPLATE = "generator/Service.ftl";

	//public static String MONGODB_FACADE_TEMPLATE = "mongodbFacade.ftl";
	
	private static Configuration cfg = new Configuration();
	
	static{
		StringTemplateLoader templates = new StringTemplateLoader();
		templates.putTemplate(SERVICE_IMPL_TEMPLATE, templateContent(SERVICE_IMPL_TEMPLATE));
		templates.putTemplate(SERVICE_TEMPLATE, templateContent(SERVICE_TEMPLATE));
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
	
	public static void generator(String storePath, String entityName, String entityDesc, String pkgName, String entityPkgName){
		
		File dir = new File(storePath);
		if (!dir.exists())
		{
			dir.mkdirs();
		}

		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("entityName", entityName);
		datas.put("pkgName", pkgName);
		datas.put("entityDesc", entityDesc);
		datas.put("entityPkgName", entityPkgName);
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(dir+"/impl", entityName + "ServiceImpl.java"));
			cfg.getTemplate(SERVICE_IMPL_TEMPLATE).process(datas, fw);

			fw = new FileWriter(new File(dir, entityName + "Service.java"));
			cfg.getTemplate(SERVICE_TEMPLATE).process(datas, fw);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(fw);
		}
		
	}
}
