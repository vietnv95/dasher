package com.phamkhanh.game;

import com.phamkhanh.mapengine.MapEngine;

public class GameState {
	private String username;
	private int level;
	private int score;
	private int time;
	private int status;
	private int speed;
	private int volume;
	private int numberOfBoxComplete;
	
	public GameState(){
		this.level = 1;
		this.score = 0;
		this.time = 20000;
		this.status = 1;
		this.speed =  1;
		this.volume = 2;
		this.numberOfBoxComplete = 0;
	}
	
	public GameState(int level, int score, int time, int status, int speed, int volume){
		this.level = level;
		this.score = score;
		this.time = time;
		this.status = status;
		this.speed = speed;
		this.volume = volume;
	}
	public int getNumberOfBoxComplete() {
		return numberOfBoxComplete;
	}

	public void setNumberOfBoxComplete(int numberOfBoxComplete) {
		this.numberOfBoxComplete = numberOfBoxComplete;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
