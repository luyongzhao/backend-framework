package com.mobike.iotcloud.backend.framework.generator;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

/**
 * 列表页面生成
 * @author luyongzhao
 *
 */
public class ViewHtmGendertor {
	
	public static String TEMPLATE = "viewHtm.ftl";
	
	private static Configuration cfg = new Configuration();
	
	static{
		StringTemplateLoader templates = new StringTemplateLoader();
		templates.putTemplate(TEMPLATE, templateContent(TEMPLATE));
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
	
	public static void generator(String className,String storePath){
		
		File dir = new File(storePath);
		if (!dir.exists())
		{
			dir.mkdirs();
		}

		Connection conn = DBUtil.getConn();
		Table table = DBUtil.getTable(className, className.toLowerCase(), conn);
		
		Map<String, Object> datas = new HashMap<String, Object>();
		datas.put("table", table);
		datas.put("className", className);
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(new File(dir, "view.htm"));
			cfg.getTemplate(TEMPLATE).process(datas, fw);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			IOUtils.closeQuietly(fw);
		}
		
	}
}
