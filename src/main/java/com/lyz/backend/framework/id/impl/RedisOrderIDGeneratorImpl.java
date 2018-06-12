package com.lyz.backend.framework.id.impl;

import com.lyz.backend.framework.id.IDGenerator;
import com.lyz.backend.framework.util.DateUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.Date;

/**
 * 订单号码生成器
 * 
 * @author luyongzhao
 *
 */
public class RedisOrderIDGeneratorImpl implements IDGenerator
{
	private IDGenerator idGenerator;
	private Integer hashLength;

	public IDGenerator getIdGenerator()
	{
		return idGenerator;
	}

	public void setIdGenerator(IDGenerator idGenerator)
	{
		this.idGenerator = idGenerator;
	}
	
	public void init(){
		
	}

	public String nextStringID()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(DateUtils.formartYMDHms(new Date()));

		String hash = idGenerator.nextLongID().toString();
		if (hash.length() > hashLength)
		{
			hash = hash.substring(hash.length() - hashLength);
		} else
		{
			for (int i = 0; i < hashLength - hash.length(); i++)
			{
				sb.append("0");
			}
		}
		sb.append(hash);
		return sb.toString();
	}

	public Integer getHashLength()
	{
		return hashLength;
	}

	public void setHashLength(Integer hashLength)
	{
		this.hashLength = hashLength;
	}


	@Override
	public Long nextLongID() {
		
		return NumberUtils.toLong(nextStringID());
	}
}
