package com.mobike.iotcloud.backend.framework.generator;

import lombok.Data;
import org.apache.commons.lang.math.NumberUtils;

import java.util.Date;

@Data
public class Column
{
	//数据字段
	private String code;
	//注释
	private String comments;
	//数据字段类型
	private String type;
	//数据长度
	private int length = 0;
	//页面组件类型
	private String pageType;

	private String isNull;

	public Column(String code, String comments, String type, String isNull)
	{
		super();
		this.code = code;
		this.comments = comments;
		this.isNull = isNull;
		if(type.startsWith("varchar") || type.startsWith("char") || type.equals("text") || type.equals("longtext")){
			this.type =  String.class.getName();
		}else if(type.startsWith("int")){
			this.type = Integer.class.getName();
		}else if(type.startsWith("bigint")){
			this.type = Long.class.getName();
		}else if(type.startsWith("boolean")){
			this.type = Boolean.class.getName();
		}else if(type.startsWith("tinyint")){
			this.type = Integer.class.getName();
		}else if(type.startsWith("smallint")){
			this.type = Integer.class.getName();
		}else if(type.startsWith("float")){
			this.type = Double.class.getName();
		}else if(type.startsWith("double") || type.startsWith("decimal")){
			this.type =  Double.class.getName();
		}else if(type.startsWith("datetime") || type.equals("date") || type.startsWith("timestamp")){
			this.type =  Date.class.getName();
		}else{
			
			this.type =  "unknown";			
		}

		//如果存在左括号，说明有长度限制
		if (type.indexOf("(") != -1) {

			length = NumberUtils.toInt(type.substring(type.indexOf("(")+1,type.length()-1));
		}
		
		if(type.equals("text") || type.equals("longtext"))
		{
			pageType = "textArea";
		}else if(type.startsWith("varchar") && NumberUtils.toInt(type.substring(8, type.length()-1),100) <=2)
		{
			pageType = "select";
		}else if(type.startsWith("varchar") && (code.contains("imgURL") ||code.contains("ImgURL")))
		{
			pageType = "img";
		}else if(type.startsWith("varchar") && code.contains("URL"))
		{
			pageType = "file";
		}else
		{
			pageType = "input";
		}
	}
	
	
/**
	public Column(String columnName, String columnLabel, int columnType)
	{
		String type = null;
		switch (columnType)
		{
			case Types.BIT:// -7;
			case Types.TINYINT:// -6;
			case Types.SMALLINT:// 5;
			case Types.INTEGER:// 4;
				type = Integer.class.getName();
				break;
			case Types.BIGINT:// -5;
				type = Long.class.getName();
				break;

			case Types.FLOAT:// 6;
			case Types.REAL:// 7;
			case Types.DOUBLE:// 8;
			case Types.NUMERIC:// 2;
			case Types.DECIMAL:// 3;
				type = Double.class.getName();
				break;
			case Types.CHAR:// 1;
			case Types.VARCHAR:// 12;
			case Types.LONGVARCHAR:// -1;
				type = String.class.getName();
				break;
			case Types.DATE:// 91;
			case Types.TIME:// 92;
			case Types.TIMESTAMP:// 93;
				type = Date.class.getName();
				break;
			case Types.BINARY:// -2;
			case Types.VARBINARY:// -3;
			case Types.LONGVARBINARY:// -4;
			case Types.NULL:// 0;
			case Types.OTHER:// 1111;
			case Types.JAVA_OBJECT:// 2000;
			case Types.DISTINCT:// 2001;
			case Types.STRUCT:// 2002;
			case Types.ARRAY:// 2003;
			case Types.BLOB:// 2004;
			case Types.CLOB:// 2005;
			case Types.REF:// 2006;
			case Types.DATALINK:// 70;
			case Types.BOOLEAN:// 16;
			case Types.ROWID:// -8;
			case Types.NCHAR:// -15;
			case Types.NVARCHAR:// -9;
			case Types.LONGNVARCHAR:// -16;
			case Types.NCLOB:// 2011;
			case Types.SQLXML:// 2009;
				type = String.class.getName();
				break;
			default:
				break;
		}

		this.code = columnName;
		this.comments = columnLabel;
		this.type = type;
	}
*/


	@Override
	public String toString()
	{
		return "Column [code=" + code + ", comments=" + comments + ", type=" + type + "]";
	}


}
