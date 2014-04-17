package edu.sjsu.cmpe.voting.ui.resources;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import edu.sjsu.cmpe.voting.api.Poll;
import edu.sjsu.cmpe.voting.repository.PollsRepositoryInterface;
import edu.sjsu.cmpe.voting.ui.views.CreatePollView;
import edu.sjsu.cmpe.voting.ui.views.UserView;

@Path("/")
@Produces(MediaType.TEXT_HTML)
public class UserResource {
	private final PollsRepositoryInterface pollRepository;

    public UserResource(PollsRepositoryInterface pollRepository) {
	this.pollRepository = pollRepository;
    }

    @GET
    public UserView getHome() {
	//return new HomeView(bookRepository.getBookByISBN(1L));
    	System.out.println("getting the polls!...");
    	return new UserView(pollRepository.getPolls());
    }
    
  /*  @GET
    @Path("createpoll")
    public CreatePollView getNewPoll(Poll newPoll) throws Exception{
    	System.out.println("creating a new poll");
    	return new CreatePollView(pollRepository.savePoll(newPoll));
    }
  */
}
