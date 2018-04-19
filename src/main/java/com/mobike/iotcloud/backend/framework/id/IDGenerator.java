package com.mobike.iotcloud.backend.framework.id;

public interface IDGenerator
{
	public String nextStringID();

	public Long nextLongID();
}
