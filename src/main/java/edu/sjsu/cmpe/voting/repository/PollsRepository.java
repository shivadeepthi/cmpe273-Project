package edu.sjsu.cmpe.voting.repository;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import edu.sjsu.cmpe.voting.api.Choice;
import edu.sjsu.cmpe.voting.api.Poll;
import edu.sjsu.cmpe.voting.utils.SmsVotingUtils;

public class PollsRepository implements PollsRepositoryInterface {
	
	/** In-memory map to store polls. (Key, Value) -> (ID, Poll) */
	private final ConcurrentHashMap<String, Poll> pollInMemoryMap;
	
	/**
	 * @param pollInMemoryMap
	 */
	public PollsRepository(ConcurrentHashMap<String, Poll> pollMap) {
		checkNotNull(pollMap, "pollMap must not be null for PollRepository");
		this.pollInMemoryMap = pollMap;
	}
	
	/**
     * This should be called if and only if you are adding new polls to the
     * repository.
     * 
     * @return a new incremental key value
     */
	public static String createID()
	{
		return UUID.randomUUID().toString().substring(0, 4);
	}

	@Override
	public Poll savePoll(Poll newPoll) throws Exception {
		checkNotNull(newPoll,"newPoll must not be null to add to PollRepository");
		try {
		//Generate New Unique id for the Poll
		String key = createID();
		newPoll.setId(key);
		System.out.println("(from the repository) Question is : " + newPoll.getQuestion());
<<<<<<< HEAD
=======
		
>>>>>>> aed72de9701dfb1d36ac4ed0372e299582b794b7
			Iterator<Choice> it = newPoll.getChoices().iterator();
			while(it.hasNext())
			{
				it.next().setId();
			}
<<<<<<< HEAD
=======
			
		//add the entry to the hashmap
>>>>>>> aed72de9701dfb1d36ac4ed0372e299582b794b7
		pollInMemoryMap.putIfAbsent(key, newPoll);
		}
		catch (Exception e)
		{			e.printStackTrace();
		}
		return newPoll;
	}

	@Override
	public HashMap<Long, Object> iterateHashMap() 
	{
		HashMap map = new HashMap<Long, Object>();
		try { 
			Iterator<String> it = pollInMemoryMap.keySet().iterator();
		while(it.hasNext())
		{
			String key = it.next();
			map.put(key+1, pollInMemoryMap.get(key).getQuestion());
			System.out.println("question in hash repository is : "+pollInMemoryMap.get(key).getQuestion());
		}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}
	
	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe.voting.repository.PollsRepositoryInterface#getPolls()
	 */
	@Override
	public List<Poll> getPolls()
	{
		List<Poll> polls = new ArrayList<Poll>();
		Iterator<String> it = pollInMemoryMap.keySet().iterator();
		while(it.hasNext())
		{
			String key = it.next();
			polls.add(pollInMemoryMap.get(key));
			System.out.println("question in hash repository is : "+pollInMemoryMap.get(key));
		}
		return polls;
	}
	
	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe.voting.repository.PollsRepositoryInterface#removePoll(java.lang.Long)
	 */
	@Override
	public void removePoll(Long id) {
		if(!pollInMemoryMap.isEmpty())
			pollInMemoryMap.remove(id);
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe.voting.repository.PollsRepositoryInterface#getPollById(java.lang.Long)
	 */
	@Override
	public Poll getPollById(Long id) {
		Poll pollById = pollInMemoryMap.get(id);
		return pollById;
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe.voting.repository.PollsRepositoryInterface#setCountForOption(java.lang.Long, java.lang.String)
	 */
	@Override
	public void setCountForOption(Long id, String option) {
		Poll pollById = getPollById(id);
		for(Choice c : pollById.getChoices())
		{
			if(option.equalsIgnoreCase(c.getOption()))
			{
				c.setCount(SmsVotingUtils.incrementCounter());
				System.out.println("count of " + c.getOption() + " is : " + c.getCount());
				break;
			}
		}
	}
	
	

}
