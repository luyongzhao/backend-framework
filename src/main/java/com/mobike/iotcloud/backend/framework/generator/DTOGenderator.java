package com.mobike.iotcloud.backend.framework.generator;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.HashMap;
import java.util.Map;

public class DTOGenderator {

    private static Logger log = LoggerFactory.getLogger(DTOGenderator.class);

    private Configuration cfg = new Configuration();

    public static final String HBM_TEMPLATE = "generator/HBM.ftl";
    public static final String ABSTRACT_DTO_TEMPLATE = "generator/AbstractDto.ftl";
    public static final String DTO_TEMPLATE = "generator/DTO.ftl";


    private void init() throws IOException {
        StringTemplateLoader templates = new StringTemplateLoader();
        templates.putTemplate(HBM_TEMPLATE, templateContent(HBM_TEMPLATE));
        templates.putTemplate(ABSTRACT_DTO_TEMPLATE, templateContent(ABSTRACT_DTO_TEMPLATE));
        templates.putTemplate(DTO_TEMPLATE, templateContent(DTO_TEMPLATE));
        cfg.setTemplateLoader(templates);
    }


    private String templateContent(String file) throws IOException {
        InputStream in = DTOGenderator.class.getClassLoader().getResourceAsStream(file);
        try {
            return IOUtils.toString(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     *
     * @param tableName 数据表名称
     * @param className 数据表对应的类名称
     * @param classFilePath 类存放目录
     * @param hbmFilePath hbm文件生成的路径
     * @param pkgName 存放类的包名
     * @throws Exception
     */
    public static void generator(String tableName, String className, String classFilePath,String hbmFilePath,
                                 String pkgName) throws Exception {
        new DTOGenderator()._generator(tableName, className, classFilePath, hbmFilePath, pkgName);
    }


    public void _generator(String tableName, String className, String classFilePath,String hbmFilePath,
                           String pkgName) throws Exception {
        init();

        Connection conn = DBUtil.getConn();

        DatabaseMetaData meta = conn.getMetaData();

        log.info("Load Table:\t" + tableName);

        Table table = DBUtil.getTable(className, tableName, conn);

        generator(classFilePath, hbmFilePath, pkgName, table);
    }


    private void generator(String classFilePath, String hbmFilePath,String pkgName, Table table)
            throws Exception {
        File classDir = new File(classFilePath);
        if (!classDir.exists()) {
            classDir.mkdirs();
        }

        File hbmDir = new File(hbmFilePath);
        if (!hbmDir.exists()) {
            hbmDir.mkdirs();
        }

        Map<String, Object> datas = new HashMap<String, Object>();
        datas.put("table", table);
        datas.put("pkgName", pkgName);

        FileWriter fw = new FileWriter(new File(hbmDir, table.getClassName() + ".hbm.xml"));
        cfg.getTemplate(HBM_TEMPLATE).process(datas, fw);
        IOUtils.closeQuietly(fw);

        fw = new FileWriter(new File(classDir, table.getName() + ".java"));
        cfg.getTemplate(DTO_TEMPLATE).process(datas, fw);
        IOUtils.closeQuietly(fw);

        fw = new FileWriter(new File(classDir, "Abstract" + table.getName() + ".java"));
        cfg.getTemplate(ABSTRACT_DTO_TEMPLATE).process(datas, fw);
        IOUtils.closeQuietly(fw);

    }


}
