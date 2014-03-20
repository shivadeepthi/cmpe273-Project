package edu.sjsu.cmpe.voting.repository;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import edu.sjsu.cmpe.voting.api.Choice;
import edu.sjsu.cmpe.voting.api.Poll;
import edu.sjsu.cmpe.voting.utils.SmsVotingUtils;

public class PollsRepository implements PollsRepositoryInterface {
	/** Never access this key directly; instead use generateKey() */
	private static AtomicLong idCounter = new AtomicLong(1000);
	
	/** In-memory map to store polls. (Key, Value) -> (ID, Poll) */
	private final ConcurrentHashMap<Long, Poll> pollInMemoryMap;
	
	/**
	 * @param pollInMemoryMap
	 */
	public PollsRepository(ConcurrentHashMap<Long, Poll> pollMap) {
		checkNotNull(pollMap, "pollMap must not be null for PollRepository");
		this.pollInMemoryMap = pollMap;
	}
	
	/**
     * This should be called if and only if you are adding new polls to the
     * repository.
     * 
     * @return a new incremental key value
     */
	public static long createID()
	{
	    return (idCounter.getAndIncrement());
	}

	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe.voting.repository.PollsRepositoryInterface#savePoll(edu.sjsu.cmpe.voting.Poll)
	 */
	@Override
	public Poll savePoll(Poll newPoll) throws Exception {
		checkNotNull(newPoll,"newPoll must not be null to add to PollRepository");
		try {
		//Generate New Unique id for the Poll
		long key = createID();
		newPoll.setId(key);
		System.out.println("(from the repository) Question is : " + newPoll.getQuestion());
//		for(int i = 0 ;i < newPoll.getChoices().size() ; i++)
//		{
		
			Iterator<Choice> it = newPoll.getChoices().iterator();
			while(it.hasNext())
			{
				it.next().setId();
			}
			
//			//System.out.println("Option "+ i + " : " +newPoll.getChoices().get(i).getOption());		
//			newPoll.getChoices().get(i).setId();
//			newPoll.getChoices().
//			//System.out.println("Option ID "+ i+1 + " : " +newPoll.getChoices().get(i).getId());
//		}
		//add the entry to the hashmap
		pollInMemoryMap.putIfAbsent(key, newPoll);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return newPoll;
	}

	@Override
	public HashMap<Long, Object> iterateHashMap() 
	{
		HashMap map = new HashMap<Long, Object>();
		try { 
		Iterator<Long> it = pollInMemoryMap.keySet().iterator();
		while(it.hasNext())
		{
			long key = it.next();
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
		Iterator<Long> it = pollInMemoryMap.keySet().iterator();
		while(it.hasNext())
		{
			long key = it.next();
			polls.add(pollInMemoryMap.get(key));
			//map.put(key+1, pollInMemoryMap.get(key).getQuestion());
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
		System.out.println("book with ISBN "+ id + " has been removed");
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
