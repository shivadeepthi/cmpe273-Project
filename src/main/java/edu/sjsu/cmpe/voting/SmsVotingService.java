package edu.sjsu.cmpe.voting;

import java.util.concurrent.ConcurrentHashMap;

import com.sun.jersey.api.container.filter.LoggingFilter;
import com.sun.jersey.api.core.ResourceConfig;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

import edu.sjsu.cmpe.voting.SmsVotingServiceConfiguration;
import edu.sjsu.cmpe.voting.api.Poll;
import edu.sjsu.cmpe.voting.repository.PollsRepository;
import edu.sjsu.cmpe.voting.repository.PollsRepositoryInterface;
import edu.sjsu.cmpe.voting.resources.ModeratorPollResource;
import edu.sjsu.cmpe.voting.resources.UserPollResource;

public class SmsVotingService extends Service<SmsVotingServiceConfiguration>{
	
	public static void main(String Args[]) throws Exception
	{
		new SmsVotingService().run(Args);
	}
	
	@Override
	public void initialize(Bootstrap<SmsVotingServiceConfiguration> bootstrap)
	{
		bootstrap.setName("sms-voting-service");
	}
	
	@Override
	public void run(SmsVotingServiceConfiguration config,
					Environment environment)
	{
		environment.setJerseyProperty(ResourceConfig.PROPERTY_CONTAINER_REQUEST_FILTERS, 
				LoggingFilter.class.getName());
    	environment.setJerseyProperty(ResourceConfig.PROPERTY_CONTAINER_RESPONSE_FILTERS, 
    			LoggingFilter.class.getName());
    	
    	/** Sms-Voting Moderator APIs */
    	PollsRepositoryInterface pollsRepository = new PollsRepository(
    		new ConcurrentHashMap<Long, Poll>());
    	environment.addResource(new ModeratorPollResource(pollsRepository));
    	
    	/** Sms-Voting User APIs */
    	environment.addResource(new UserPollResource(pollsRepository));
	}
}
