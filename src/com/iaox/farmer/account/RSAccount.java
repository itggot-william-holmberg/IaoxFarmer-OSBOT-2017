package com.iaox.farmer.account;

public class RSAccount {

	private String login;
	private String password;
	
	private boolean completedTutorialIsland;
	
	public RSAccount(String login, String password, boolean b){
		this.login = login;
		this.password = password;
		completedTutorialIsland = b;
	}
	
	public String getLogin(){
		return login;
	}
	
	public String getPassword(){
		return password;
	}
	
	public boolean getCompletedTutorialIsland(){
		return completedTutorialIsland;
	}
	
	public void setCompletedTutorialIsland(boolean bool){
		completedTutorialIsland = bool;
	}
	
	public String toString(){
		return login;
	}
}
