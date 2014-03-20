package edu.sjsu.cmpe.voting.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class SmsVotingUtils {
	private static AtomicInteger atomicId = new AtomicInteger();
	
	public static long incrementCounter()
	{
		long count = atomicId.incrementAndGet();
		System.out.println("###### SMS VOTING UTILS /counter value is : "+count);
		return(count);
	}
}
