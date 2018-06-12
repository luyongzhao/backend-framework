package com.lyz.backend.framework.service;

import com.lyz.backend.framework.freemarker.FreemarkerStaticModels;
import com.lyz.backend.framework.util.Configs;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.util.Map;

public class FreeMarkerTemplateService {

    private Configuration freemarkerConfiguration;
    private FreemarkerStaticModels freemarkerStaticModels;

    /**
     * process an retrun response as string
     *
     * @param data
     * @param templateName
     * @return
     */
    public String process(Map<String, Object> data, String templateName)
    {
        StringWriter sw = new StringWriter();
        this.process(data, templateName, sw);
        return sw.toString();
    }

    /**
     * process and write to a file
     *
     * @param data
     * @param templateName
     * @param fileName
     */
    public void process(Map<String, Object> data, String templateName, String fileName)
    {
        try
        {
            File file = new File(fileName);
            File dir = file.getParentFile();
            if (!dir.exists())
            {// 创建父级目录
                dir.mkdirs();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
            this.process(data, templateName, writer);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * process template and write to a writer, close writer auto
     *
     * @param data
     * @param templateName
     * @param writer
     *            will be closed after method execute, what ever exception or
     *            success
     */
    public void process(Map<String, Object> data, String templateName, Writer writer)
    {
        try
        {
            data.put("_appConfig_", Configs.getProperties());
            data.putAll(freemarkerStaticModels);
            Template template = freemarkerConfiguration.getTemplate(templateName);
            template.process(data, writer);
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                writer.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public Configuration getFreemarkerConfiguration()
    {
        return freemarkerConfiguration;
    }

    public void setFreemarkerConfiguration(Configuration freemarkerConfiguration)
    {
        this.freemarkerConfiguration = freemarkerConfiguration;
    }

    public FreemarkerStaticModels getFreemarkerStaticModels()
    {
        return freemarkerStaticModels;
    }

    public void setFreemarkerStaticModels(FreemarkerStaticModels freemarkerStaticModels)
    {
        this.freemarkerStaticModels = freemarkerStaticModels;
    }
}
