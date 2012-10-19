package com.harry.data;

import org.jdom2.Element;

public class HighScore {
	private int score;
	private String userName;
	
	public HighScore() {
		this.score = 0;
		this.userName = "";
	}
	
	public void setProperties(Element userElement) {
		this.userName = userElement.getChildText("name");
		this.score = Integer.parseInt(userElement.getChildText("score"));
	}
	
	public String getUserName() {
		return userName;
	}
	
	public int getScore() {
		return score;
	}
}
