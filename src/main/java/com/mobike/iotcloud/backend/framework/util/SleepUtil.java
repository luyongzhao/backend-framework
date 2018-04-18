package com.mobike.iotcloud.backend.framework.util;

public class SleepUtil
{

	/**
	 * sleep for a certain time, compute next time with pre time
	 * 
	 * @param milliSeconds
	 * @return nextInterval
	 */
	public static void sleep(long milliSeconds)
	{

		try
		{
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e)
		{

		}
	}

}
