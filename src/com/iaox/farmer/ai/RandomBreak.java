package com.iaox.farmer.ai;

import com.iaox.farmer.task.Task;

public class RandomBreak {
	
	//the amount of time before we execute the break
	private int playTime;
	
	//for how long the break should be executed (in minutes)
	private int breakTime;
	
	private Task task;
	
	public RandomBreak(int executeTick, int breakTime){
		this.playTime = executeTick;
		this.breakTime = breakTime;
	}
	
	public RandomBreak(int executeTick, int breakTime, Task task){
		this.playTime = executeTick;
		this.breakTime = breakTime;
		this.task = task;
	}

	public int getPlayTime(){
		return playTime;
	}
	
	public int getBreakTime(){
		return breakTime;
	}
	
	public Task getTask(){
		return task;
	}
	
	public String toString(){
		return "Play for: " + playTime + " minutes and then break for: " + breakTime + " minutes";
	}
}
