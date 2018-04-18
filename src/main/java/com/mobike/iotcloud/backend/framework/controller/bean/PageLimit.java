package com.mobike.iotcloud.backend.framework.controller.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageLimit
{
	/**
	 * 从第一页开始的:1
	 */
	private Integer pageNumber;
	private List<?> list;
	private String listHQL;
	private String countHQL;
	private Integer pageDisplay;
	private Long totalResult = 0L;

	private Map<String, ?> params;

	public PageLimit(Integer pageDispaly, Integer pageNumber, String countHQL,
                     String listHQL, Map<String, ?> params)
	{
		this.pageDisplay = pageDispaly;
		this.pageNumber = pageNumber;
		this.countHQL = countHQL;
		this.listHQL = listHQL;
		this.params = params;
	}

	public PageLimit()
	{
	}

	public Integer getPageCount()
	{
		Long pageCount = null;
		if (this.totalResult % this.getPageDisplay() == 0)
		{
			pageCount = (this.totalResult / this.getPageDisplay());
		} else
		{
			pageCount = this.totalResult / this.getPageDisplay() + 1;
		}
		return pageCount.intValue();
	}

	public Integer getPageNumber()
	{
		if (pageNumber == null || pageNumber.intValue() < 1)
		{
			this.pageNumber = Integer.valueOf(1);
		}

		return pageNumber;
	}

	public void setPageNumber(Integer pageNumber)
	{
		this.pageNumber = pageNumber;
	}

	public Long getTotalResult()
	{
		return totalResult;
	}

	public void setTotalResult(Long totalResult)
	{
		this.totalResult = totalResult;
	}

	public List<?> getList()
	{
		return list;
	}

	public void setList(List<?> list)
	{
		this.list = list;
	}

	public String getListHQL()
	{
		return listHQL;
	}

	public void setListHQL(String listHQL)
	{
		this.listHQL = listHQL;
	}

	public String getCountHQL()
	{
		return countHQL;
	}

	public void setCountHQL(String countHQL)
	{
		this.countHQL = countHQL;
	}

	public Map<String, ?> getParams()
	{
		return params;
	}

	public void setParams(Map<String, ?> params)
	{
		this.params = params;
	}

	public Integer getPageDisplay()
	{
		return pageDisplay;
	}

	public void setPageDisplay(Integer pageDisplay)
	{
		this.pageDisplay = pageDisplay;
	}

	public Map<String, Integer> getPageShowsRange()
	{
		if (this.getTotalResult() == 0)
		{
			return null;
		} else
		{
			Map<String, Integer> map = new HashMap<String, Integer>();

			Integer begin = this.getPageNumber() - 2;
			if (begin < 1)
			{
				begin = 1;
			}

			Integer end = begin + 4;
			if (end > this.getPageCount())
			{
				end = this.getPageCount();
			}

			Integer pageLeft = 5 - (end - begin + 1);
			begin = begin - pageLeft;
			if (begin < 1)
			{
				begin = 1;
			}

			map.put("pageBegin", begin);
			map.put("pageEnd", end);
			return map;
		}
	}
}
