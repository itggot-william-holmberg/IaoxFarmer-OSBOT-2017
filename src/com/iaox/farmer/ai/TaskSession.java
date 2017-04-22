package com.iaox.farmer.ai;

import com.iaox.farmer.task.Task;

public class TaskSession {

	//the amount of time before we change task
		private int playTime;

		
		private Task task;
		
		
		public TaskSession(int executeTick, Task task){
			this.playTime = executeTick;
			this.task = task;
		}

		public int getPlayTime(){
			return playTime;
		}
		

		
		public Task getTask(){
			return task;
		}
		
		public String toString(){
			return "Execute task: " + task + " for:" + playTime + " minutes";
		}
}
