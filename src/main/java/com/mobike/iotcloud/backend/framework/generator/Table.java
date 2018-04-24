package com.mobike.iotcloud.backend.framework.generator;

import java.util.ArrayList;
import java.util.List;

public class Table
{
	private String name;
	private String comments;
	
	private List<Column> columns;

	public Table(String tableName, String comments)
	{
		this.name = tableName;
		this.comments = comments;
		this.columns = new ArrayList<Column>();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public List<Column> getColumns()
	{
		return columns;
	}

	public void setColumns(List<Column> columns)
	{
		this.columns = columns;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}
}
