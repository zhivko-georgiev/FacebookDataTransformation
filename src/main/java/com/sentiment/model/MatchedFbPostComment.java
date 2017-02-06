package com.sentiment.model;

import com.google.gson.annotations.SerializedName;

public class MatchedFbPostComment {
	@SerializedName("test")
	private FbPostComment fbPostComment;
	
	public MatchedFbPostComment(FbPostComment fbPostComment) {
		super();
		this.fbPostComment = fbPostComment;
	}

	public FbPostComment getFbPostComment() {
		return fbPostComment;
	}

	public void setFbPostComment(FbPostComment fbPostComment) {
		this.fbPostComment = fbPostComment;
	}
}
