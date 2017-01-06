package com.iaox.farmer.ai;

public class RandomBreak {
	
	//the amount of time before we execute the break
	private int playTime;
	
	//for how long the break should be executed (in minutes)
	private int breakTime;
	
	public RandomBreak(int executeTick, int breakTime){
		this.playTime = executeTick;
		this.breakTime = breakTime;
	}

	public int getPlayTime(){
		return playTime;
	}
	
	public int getBreakTime(){
		return breakTime;
	}
	
	public String toString(){
		return "Play for: " + playTime + " minutes and then break for: " + breakTime + " minutes";
	}
}
