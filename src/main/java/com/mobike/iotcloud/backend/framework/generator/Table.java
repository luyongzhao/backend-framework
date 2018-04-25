package com.mobike.iotcloud.backend.framework.generator;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Table
{
	private String name;
	private String comments;
	private String className;
	
	private List<Column> columns;

	public Table(String className, String tableName, String comments)
	{
		this.className = className;
		this.name = tableName;
		this.comments = comments;
		this.columns = new ArrayList<Column>();
	}

}
