package edu.sjsu.cmpe.voting.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import edu.sjsu.cmpe.voting.api.Choice;

public class Poll {
	

	@JsonProperty("pollid")
	@JsonInclude(Include.NON_DEFAULT)
	private String id;
	
	@JsonProperty("question")
	private String question;
	
	@Valid
	@JsonProperty("choices")
	private ArrayList<Choice> choices;
	
	@JsonProperty("creation date")
	private Date start;
	
	@JsonProperty("expiry date")
	private Date end;	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the question
	 */
	public String getQuestion() {
		return question;
	}
	/**
	 * @param question the question to set
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	public ArrayList<Choice> getChoices() {
		return choices;
	}
	public void setChoices(ArrayList<Choice> choices) {
		
		this.choices=choices;
	}
	
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	
}
