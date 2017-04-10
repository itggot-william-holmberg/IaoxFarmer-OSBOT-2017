package com.iaox.farmer.ai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.osbot.rs07.script.MethodProvider;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.account.RSAccount;



public class IaoxCommunicator extends Thread {
	String serverAddress = "0.0.0.0";
	Socket socket;
	private MethodProvider methodProvider;

	public IaoxCommunicator(MethodProvider methodProvider) {
		this.start();
		this.methodProvider = methodProvider;
	}

	public void run() {
		methodProvider.log("Started IaoxCommunicator");
		try {
			socket = new Socket(serverAddress, 2000);
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			
			String answer = input.readLine();
			
			if (answer.equals("NAME")) {
				out.println(getHostname());
			}

			// request loop
			while (IaoxAIO.shouldThreadRun) {
				methodProvider.log("Thread loop");
				// get the current request and then print it to the server
				String request = getRequest();
				methodProvider.log("lets send request: " + request);
				out.println(request);
				methodProvider.log("we sent request");
				answer = input.readLine();
				methodProvider.log("Answer: " + answer);
				handleAnswer(answer);
				this.sleep(2500);
			}
		} catch (IOException | InterruptedException e) {
			methodProvider.log(e.getMessage());
			e.printStackTrace();
		}
	}

	private void handleAnswer(String answer) throws IOException {
		if (answer.contains("POLL_ACCOUNT")) {
			String[] args = answer.split(":");
			String username = args[1];
			String password = args[2];
			IaoxAIO.currentAccount = new RSAccount(username, password, false);
			IaoxAIO.shouldUpdateTutorialIslandStatus = false;
			IaoxAIO.updatedTutorialIslandStatus = false;
		} else if (answer.equals("DISCONNECT") || answer.equals("NO_AVAILABLE_ACCOUNT")) {
			methodProvider.log("No account available!");
			socket.close();
			IaoxAIO.shouldStop = true;
		} else if (answer.equals("UPDATED_TUTORIAL_STATUS")) {
			methodProvider.log("We successfully update the status of tutorial island");
			IaoxAIO.updatedTutorialIslandStatus = true;
		} else if (answer.equals("NO_AVAILABLE_MULE")) {
			methodProvider.log("There is no mule available");
		} else {
			methodProvider.log("ping, we are alive");
		}
		methodProvider.log("still at handle answer");
	}

	private String getRequest() {
		methodProvider.log("lets get request");
		if (IaoxAIO.currentAccount == null) {
			return "POLL_ACCOUNT";
		} else if (IaoxAIO.shouldStop == true) {
			return "DISCONNECT";
		} else if (IaoxAIO.shouldUpdateTutorialIslandStatus == true) {
			methodProvider.log("what the hell");
			return "COMPLETED_TUTORIAL";
		} else if(IaoxAIO.shouldTrade && IaoxAIO.mule == null){
			return "POLL_MULE";
		}
		return "PING";
	}

	public String getHostname() {
		String hostname = "Unknown";

		try {
			InetAddress addr;
			addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
			return hostname;
		} catch (UnknownHostException ex) {
			System.out.println("Hostname can not be resolved");
		}
		return "UnkownName";
	}
}