package edu.sjsu.cmpe.voting.repository;

/*import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
*/
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.*;

import org.bson.types.BasicBSONList;
//import org.jboss.logging.DelegatingBasicLogger;
import org.springframework.data.mongodb.core.MongoTemplate;

import edu.sjsu.cmpe.voting.api.Choice;
import edu.sjsu.cmpe.voting.api.Poll;
import edu.sjsu.cmpe.voting.utils.SmsVotingUtils;

public class PollsRepository implements PollsRepositoryInterface {
	MongoClient mongoClient=null;
	DB db=null;
	DBCollection collection=null;
    MongoTemplate mongoTemplate=null;
public PollsRepository(){
	
}

// @return a new incremental key value
public static String createID()
{
	return UUID.randomUUID().toString().substring(0, 4);
}
/**
 * This should be called if and only if you are adding new polls to the
 * repository.
 * 
 *
 */

public Poll savePoll(Poll newpoll){
	try{
		mongoClient=new MongoClient("NavDeep",27017);
		db=mongoClient.getDB("sms-voting");
	}catch(Exception e){
		e.printStackTrace();
	}
	
	String key = createID();
	System.out.println(key);
	newpoll.setId(key);
	List<BasicDBObject> choices=new ArrayList<BasicDBObject>();
	collection=db.getCollection("pollsCollection");
	//insert the polls into database
	BasicDBObject basicDBObject=new BasicDBObject();
	basicDBObject.put("pollid", newpoll.getId());
	basicDBObject.put("question",newpoll.getQuestion());
	Iterator<Choice> it=newpoll.getChoices().iterator();
//	BasicDBObject update = new BasicDBObject();
	while(it.hasNext()){
		 		 Choice opt=it.next();
		 choices.add(new BasicDBObject("option",opt.getOption()).append("count",opt.getCount()));	
		 
		//choices.put("option",opt.getOption());
		System.out.println(opt.getOption());
		
	}
	basicDBObject.put("Choice",choices);
	//update.put("$push", new BasicDBObject("Choices",choices));

	
	basicDBObject.put("creation date", newpoll.getStart());
	basicDBObject.put("expiry date", newpoll.getEnd());
	collection.insert(basicDBObject);
	
	mongoClient.close();
	return newpoll;
}

public Poll getPollById(String id) {
	try{
		mongoClient=new MongoClient("NavDeep",27017);
		db=mongoClient.getDB("sms-voting");
		collection=db.getCollection("pollsCollection");
	}catch(Exception e){
		e.printStackTrace();
	}
	
	Poll poll=new Poll();
    
	ArrayList<Choice> choice=new ArrayList<Choice>(); 
//	collection.setObjectClass(Poll.class);
	BasicDBObject query = new BasicDBObject();
	query.put("pollid", id);
	 DBCursor cursor=collection.find(query);
	 while(cursor.hasNext()){
		 DBObject object=cursor.next();
		String pollId=(String) object.get("pollid");
		System.out.println(pollId);
		 String question=(String)object.get("question");
		 System.out.println(question);
		 @SuppressWarnings("unchecked")
		ArrayList<Choice> arrayList=(ArrayList<Choice>)object.get("Choice");
	System.out.println(arrayList.get(0));

		/* for (int i = 0; i < arrayList.size(); i++) {
			 Choice ChoiceObj =  arrayList.get(i);
			 String option=ChoiceObj.getOption();
			 long count=ChoiceObj.getCount();
			 Choice c=new Choice();
			 c.setOption(option);
			 c.setCount(count);
			 choice.add(c);
		 }	*/
		 Date creation_date=(Date)object.get("creation date");
		 Date expiry_date=(Date)object.get("expiry date");
			
			poll.setId(pollId);
			poll.setQuestion(question);
			poll.setChoices(arrayList);
			poll.setStart(creation_date);
			poll.setEnd(expiry_date);
		 
	 }
	 
	
	
	 mongoClient.close();
	return poll;
}
	
public List<Poll> getPolls()
{
	try{
		mongoClient=new MongoClient("NavDeep",27017);
		db=mongoClient.getDB("sms-voting");
		collection=db.getCollection("pollsCollection");
	}catch(Exception e){
		e.printStackTrace();
	}
	List<Poll> polls = new ArrayList<Poll>();
	DBCursor cursor=collection.find().sort(new BasicDBObject("creation date",1));
	while(cursor.hasNext())
	{
		DBObject object = cursor.next();
		String pollID=(String)object.get("pollid");
		String question=(String)object.get("question");
		Date creation_date=(Date)object.get("creation date");
		Date expiry_date=(Date)object.get("expiry date");
		Poll poll=new Poll();
		poll.setId(pollID);
		poll.setQuestion(question);
		poll.setStart(creation_date);
		poll.setEnd(expiry_date);
		//poll.setChoices(choices);
		polls.add(poll);
		}
	mongoClient.close();
	return polls;
}
	
public void removePoll(String id) {
	try{
		mongoClient=new MongoClient("NavDeep",27017);
		db=mongoClient.getDB("sms-voting");
		collection=db.getCollection("pollsCollection");
	}catch(Exception e){
		e.printStackTrace();
	}
	
	if(collection.count()!=0)
		collection.remove(new BasicDBObject("pollid",id));
	System.out.println("removed");
	mongoClient.close();
}	
	
public void setCountForOption(String id, String option) {
	Poll pollById = getPollById(id);
	for(Choice c : pollById.getChoices())
	{
		if(option.equalsIgnoreCase(c.getOption()))
		{	
			//Choice choice=new Choice();
			c.setCount(SmsVotingUtils.incrementCounter());
			System.out.println("count of " + c.getOption() + " is : " + c.getCount());
			break;
		}
	}
}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	


