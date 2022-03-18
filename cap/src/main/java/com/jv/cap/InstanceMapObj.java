package com.jv.cap;

import org.springframework.web.reactive.function.client.WebClient;

public class InstanceMapObj {

	WebClient webClient;
	int holes;
	int fills;
	int instanceID;
	WebClient nextInstance;
	int cap = 25;
	boolean active = true;
	
	public InstanceMapObj(WebClient webClient, int fills, int holes, int instanceID, WebClient nextInstance) {
		this.webClient = webClient;
		this.fills = fills;
		this.holes = holes;
		this.instanceID = instanceID;
		this.nextInstance = nextInstance;
	}
	
	public void updateHoles(int holes) {
		if(holes < 0) {
			this.holes = 0;
		}
		else if(holes > 25) {
			this.holes = this.cap;
		}
		else {
			this.holes = holes;	
		}
		
		this.holes = this.cap - this.fills;
	}
	
	public void updateFills(int fills) {
		if(fills < 0) {
			this.fills = 0;
		}
		else if(fills > 25) {
			this.fills = 25;
		}
		else {
			this.fills = fills;	
		}
		
		this.holes = this.cap - this.fills;
	}	
	
	public void updateActiveStatus(boolean activeStatus) {
		this.active = activeStatus;
	}
}
