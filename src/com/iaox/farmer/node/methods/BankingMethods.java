package com.iaox.farmer.node.methods;

import org.osbot.rs07.script.Script;
import org.osbot.rs07.utility.ConditionalSleep;

import com.iaox.farmer.IaoxAIO;

public class BankingMethods {
	
	private Script script;
	
	public BankingMethods(Script script){
		this.script = script;
	}

	public void depositAll(){
		if(script.bank.isOpen()){
			script.bank.depositAll();	
			new ConditionalSleep(4000){
				@Override
				public boolean condition() throws InterruptedException {
					IaoxAIO.sleep(300);
					return script.inventory.isEmpty();
				}	
			}.sleep();
		}else{
			try {
				script.bank.open();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void depositBoxDepositAll() {
		if(script.depositBox.isOpen()){
			script.depositBox.depositAll();	
			new ConditionalSleep(4000){
				@Override
				public boolean condition() throws InterruptedException {
					IaoxAIO.sleep(300);
					return script.inventory.isEmpty();
				}	
			}.sleep();
		}else{
			script.depositBox.open();
		}
		
	}
}
