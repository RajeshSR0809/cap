package com.jv.cap;

import org.springframework.web.reactive.function.client.WebClient;

public class FilledInstanceInfo {
	InstanceMapObj instanceMapObj;
	int pointer;
	
	
	public FilledInstanceInfo(InstanceMapObj instanceMapObj, int pointer) {
		this.instanceMapObj = instanceMapObj;
		this.pointer = pointer;
	}
	
	
	public InstanceMapObj  updateInstance(InstanceMapObj instanceMapObj) {
		this.instanceMapObj = instanceMapObj;
		return this.instanceMapObj;
	}
	
	public int updatePointer(int pointer) {
		this.pointer = pointer;
		return this.pointer;
	}

}
