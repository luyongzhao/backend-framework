package com.mobike.iotcloud.backend.framework.util;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;

import java.util.Date;

public class ExcelUtil
{
	public static String stringCellValue(Cell cell)
	{
		if (cell != null && cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
		{
			return StringUtils.trim(cell.getStringCellValue());
		}
		return null;
	}

	public static Integer intCellValue(Cell cell)
	{
		Double d = doubleCellValue(cell);
		if (d != null)
		{
			return d.intValue();
		} else
		{
			return null;
		}
	}

	public static Double doubleCellValue(Cell cell)
	{
		if (cell != null)
		{
			if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC)
			{
				return Double.valueOf(cell.getNumericCellValue());
			}
		}

		return null;
	}

	public static Date dateCellValue(Cell cell)
	{
		if (cell != null)
		{
			return cell.getDateCellValue();
		}

		return null;
	}

}
