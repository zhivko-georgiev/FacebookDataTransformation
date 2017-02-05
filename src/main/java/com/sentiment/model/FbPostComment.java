package com.sentiment.model;

import java.util.Date;

import org.apache.commons.lang3.Validate;

public class FbPostComment {
	private String id;
	private String message;
	private Date createdTime;

	public FbPostComment(String id, String message, Date createdTime) {
		super();
		Validate.notEmpty(id, "Id can't be null");
		Validate.notEmpty(message, "Message can't be null");
		Validate.notNull(createdTime, "CreatedTime can't be null");
		this.id = id;
		this.message = message;
		this.createdTime = createdTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}
}
