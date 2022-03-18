package com.jv.cap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

@Service
public class EC2Instance {

	
	// INTEGRATE THE REST CALLS
	// TEST THE CHANGES
	// DO THE REQUIRED CHANGES
	// CONSIDER THE NODE ACTIVE STATUS FOR THE OPRATIONS
	// TEST THE CHANGES
	// DO THE REQUIED CHANGES

	boolean init = false;
	
	@Autowired
	@Qualifier("getWebClient1")
	WebClient webClient1;

	@Autowired
	@Qualifier("getWebClient2")
	WebClient webClient2;
	
	@Autowired
	@Qualifier("getWebClient3")
	WebClient webClient3;

	@Autowired
	@Qualifier("getWebClient4")
	WebClient webClient4;

	@Autowired
	@Qualifier("getWebClient5")
	WebClient webClient5;	

	
	InstanceMapObj i1; 
    InstanceMapObj i2; 
    InstanceMapObj i3; 
    InstanceMapObj i4; 
    InstanceMapObj i5; 
	FilledInstanceInfo filledInstanceInfo;
	
	public EC2Instance() {
	}

	public void initProcess() {
		if(!init) {
			this.i1 = new InstanceMapObj(this.webClient1, 0, 25, 1, this.webClient2);
	        this.i2 = new InstanceMapObj(this.webClient2, 0, 25, 2, this.webClient3);
	        this.i3 = new InstanceMapObj(this.webClient3, 0, 25, 3, this.webClient4);
	        this.i4 = new InstanceMapObj(this.webClient4, 0, 25, 4, this.webClient5);
	        this.i5 = new InstanceMapObj(this.webClient5, 0, 25, 5, this.webClient1);
			this.filledInstanceInfo = new FilledInstanceInfo(this.i1, 0);		
			this.init = true;
		}
		
	}
	
	public void getStats() {
		System.out.println("i1 stats ==========> ");
		System.out.println(this.i1.fills);
		System.out.println(this.i1.holes);
		
		System.out.println();
		
		System.out.println("i2 stats ==========> ");
		System.out.println(this.i2.fills);
		System.out.println(this.i2.holes);
		
		System.out.println();
		
		System.out.println("i3 stats ==========> ");
		System.out.println(this.i3.fills);
		System.out.println(this.i3.holes);
		
		System.out.println();
		
		System.out.println("i4 stats ==========> ");
		System.out.println(this.i4.fills);
		System.out.println(this.i4.holes);
		
		System.out.println();
		
		System.out.println("i5 stats ==========> ");
		System.out.println(this.i5.fills);
		System.out.println(this.i5.holes);
		
		System.out.println();
		
		System.out.println("filled instance info ==========>");
		System.out.println(this.filledInstanceInfo.instanceMapObj.instanceID);
		System.out.println(this.filledInstanceInfo.instanceMapObj.fills);
		System.out.println(this.filledInstanceInfo.pointer);


		
	}
	
	public InstanceMapObj  getNextInstanceMapObj(InstanceMapObj ix,  int currentInstanceId) {
		InstanceMapObj imo = ix;
		if(currentInstanceId == 1) imo =  this.i2;
		if(currentInstanceId == 2) imo =  this.i3;
		if(currentInstanceId == 3) imo =  this.i4;
		if(currentInstanceId == 4) imo =  this.i5;
		if(currentInstanceId == 5) imo =  this.i1;
		
		return imo;
	}
	
	public InstanceMapObj getInstanceMapObjFromInstanceID(int instanceID) {
		if(instanceID == 1) return this.i1;
		else if(instanceID == 2) return this.i2;
		else if(instanceID == 3) return this.i3;
		else if(instanceID == 4) return this.i4;
		else return this.i5;
		
	}
	
	public ArrayList<String> getChunk(ArrayList<String> words, int to) {
		ArrayList<String> chunk = new ArrayList<String>();
		List<String> c = words.subList(0, to);
		chunk = new ArrayList<String>(c);
		words.subList(0, to).clear();
		return chunk;
	}
	
	public boolean isAllInstancesFilled() {
		return (this.i1.fills == 25 && this.i2.fills == 25 && this.i3.fills == 25 && this.i4.fills == 25 && this.i5.fills == 25);
	}
	
	public int coreV2(InstanceMapObj ix, int available, String srcOfWords, int chunkSize, ArrayList<String> words) {
		int chunks_5 = 0;
		while(chunks_5 < 5) {
			
			if(!ix.active) {
				ix = this.getNextInstanceMapObj(ix, ix.instanceID);
				chunks_5 = chunks_5 + 1;				
				continue;
			}
			
			if(ix.holes == 0) {
				ix = this.getNextInstanceMapObj(ix, ix.instanceID);
				chunks_5 = chunks_5 + 1;
				continue;
			}
			
			if(available > ix.holes) {
				srcOfWords = "holes";
				chunkSize = ix.holes;
			}
			
			if(available < ix.holes) {
				srcOfWords = "input";
				chunkSize = available;
			}
			
			if(available == ix.holes) {
				srcOfWords = "input";
				chunkSize = available;
			}
			
			if(chunkSize == 0) {
				ix = this.getNextInstanceMapObj(ix, ix.instanceID);
				chunks_5 = chunks_5 + 1;
				continue;				
			}
			
			if(srcOfWords == "holes") {
				int toIndex = ix.holes;
				ArrayList<String> chunk = this.getChunk(words, toIndex);
				this.INSTANCE_insert(ix.webClient, chunk);
				available = available - ix.holes;
				ix.updateFills(ix.fills + ix.holes);
				ix = this.getNextInstanceMapObj(ix, ix.instanceID);
				chunks_5 = chunks_5 + 1;
				
			}
			else if(srcOfWords == "input") {
				ArrayList<String> chunk = words;
				this.INSTANCE_insert(ix.webClient, chunk);
				available = available - words.size();
				ix.updateFills(ix.fills + words.size());
				words.clear();
			}
			
		}
		
		return available;
	}
	
	public ArrayList<String> filledOprV2(ArrayList<String> words) {
		ArrayList<String> wordsClone = (ArrayList<String>)words.clone();
		InstanceMapObj ix = this.filledInstanceInfo.instanceMapObj;
		int available = words.size();
		while(available > 0) {
			
			String srcOfWords = "";
			int chunkSize = 0;
			for(int a=0; a< 5 && available > 0; a++) {
				ix = this.filledInstanceInfo.instanceMapObj;
				//ix.updateFills(this.filledInstanceInfo.pointer);
				int holes = 25 - this.filledInstanceInfo.pointer;
				

				if(!ix.active) {
					ix = this.getNextInstanceMapObj(ix, ix.instanceID);
					this.filledInstanceInfo.updateInstance(ix);					
					continue;
				}
				
				if(holes == 0) {
					ix = this.getNextInstanceMapObj(ix, ix.instanceID);
					this.filledInstanceInfo.updateInstance(ix);
					this.filledInstanceInfo.updatePointer(0);					
					continue;
				}
				
				if(available > holes) {
					srcOfWords = "holes";
					chunkSize = holes;
				}
				
				if(available < holes) {
					srcOfWords = "input";
					chunkSize = available;
				}
				
				if(available == holes) {
					srcOfWords = "input";
					chunkSize = available;
				}				
				
				if(chunkSize == 0) {
					ix = this.getNextInstanceMapObj(ix, ix.instanceID);
					this.filledInstanceInfo.updateInstance(ix);
					this.filledInstanceInfo.updatePointer(0);						
					continue;				
				}
				
				//means that we have to remove the existing records 
				//words are added at the end of an array, so start removing from 0th index till the chunk size
				if(chunkSize > 0) {
					int startIndex = 0;
					int endIndex =  chunkSize;
					this.INSTANCE_deleteAtSpecified(ix.webClient, startIndex, endIndex);
				} 				
				
				if(srcOfWords == "holes") {
					int toIndex = holes;
					ArrayList<String> chunk = this.getChunk(words, toIndex);
					this.INSTANCE_insert(ix.webClient, chunk);
					available = available - holes;
					//ix.updateFills(ix.fills + holes);
					ix = this.getNextInstanceMapObj(ix, ix.instanceID);
					this.filledInstanceInfo.updateInstance(ix);
					this.filledInstanceInfo.updatePointer(0);
					
				}
				else if(srcOfWords == "input") {
					ArrayList<String> chunk = words;
					this.INSTANCE_insert(ix.webClient, chunk);
					available = available - words.size();
					this.filledInstanceInfo.pointer = this.filledInstanceInfo.pointer + words.size();
					words.clear();
				}
				
				if(chunkSize < 25 && available == 0) {
					//this.filledInstanceInfo.pointer = this.filledInstanceInfo.pointer + holes;
				}
				
			}
		}	
		
		return wordsClone;
	}

	public ArrayList<String> oprv2(ArrayList<String> words) {
		ArrayList<String> wordsClone = (ArrayList<String>)words.clone();
		int available = words.size();
		InstanceMapObj ix = i1;
		while(available > 0) {
			String srcOfWords = "";
			int chunkSize = 0;
			available = this.coreV2(ix, available, srcOfWords, chunkSize, words);
			

			// all the instances are filled, refill based on the filledInstanceInfo
			if(available > 0) {
				System.out.println("call the filled algo and break the loop");
				this.filledOprV2(words);
				break;
			}
			
			
		}
		
		return wordsClone;
	}
	
	private ArrayList<String> INSTANCE_insert(WebClient webClient, ArrayList<String> words) {
		ArrayList<String> wordsResp = webClient.post().uri("/words").bodyValue(words).retrieve().bodyToMono(ArrayList.class).block();
		return wordsResp;
	}	
	private ArrayList<String> INSTANCE_getWords(WebClient webClient){
		ArrayList<String> wordsResp = webClient.get().uri("/words").retrieve().bodyToMono(ArrayList.class).block();
		return wordsResp;
	}
	
	private boolean INSTANCE_deleteAllWords(WebClient webClient) {
		return webClient.delete().uri("/words").retrieve().bodyToMono(Boolean.class).block();
	}
	
	
	private boolean INSTANCE_deleteAtSpecified(WebClient webClient, int startIndex, int endIndex) {
		return webClient.patch().uri("/words?start="+startIndex+"&end="+endIndex).retrieve().bodyToMono(Boolean.class).block();
	}
	
	
	
	private boolean INSTANCE_changeStatus(WebClient webClient, boolean status) {
		return webClient.patch().uri("/node?active="+status).retrieve().bodyToMono(Boolean.class).block();
	}
	
	
	private DeleteResp INSTANCE_deleteWord(WebClient webClient,  String word) {
		Integer statusCode = webClient.delete().uri("/words/"+word).retrieve().bodyToMono(Integer.class).block();
		return new DeleteResp(statusCode);
	}
	
	
	public ArrayList<String> getAllWords(){
		int checkInsts_5 = 0;
		ArrayList<String> allWords = new ArrayList<String>();
		InstanceMapObj ix = i1;
		while(checkInsts_5 < 5) {
			if(!ix.active) {
				ix = this.getNextInstanceMapObj(ix, ix.instanceID);
				checkInsts_5 = checkInsts_5 + 1;				
				continue;
			}
			ArrayList<String> wordsFromInstance =  this.INSTANCE_getWords(ix.webClient);
			allWords.addAll(wordsFromInstance);
			ix = this.getNextInstanceMapObj(ix, ix.instanceID);
			checkInsts_5 = checkInsts_5 + 1;
		}
		return allWords;
	}
	
	public boolean deleteAllWords() {
		int checkInsts_5 = 0;
		InstanceMapObj ix = i1;
		while(checkInsts_5 < 5) {
			if(!ix.active) {
				ix = this.getNextInstanceMapObj(ix, ix.instanceID);
				checkInsts_5 = checkInsts_5 + 1;				
				continue;
			}			
			this.INSTANCE_deleteAllWords(ix.webClient);
			ix.updateFills(0);
			ix = this.getNextInstanceMapObj(ix, ix.instanceID);
			checkInsts_5 = checkInsts_5 + 1;
		}	
		
		return true;
	}
	
	public boolean changeInstanceStatus(int instanceID, boolean status) {
		InstanceMapObj ix = this.getInstanceMapObjFromInstanceID(instanceID);
		ix.active = status;
		this.INSTANCE_changeStatus(ix.webClient, status);
		return ix.active;
	}
	
	public ArrayList<Integer> getActiveNodeIds(){
		ArrayList<Integer> activeNodes = new ArrayList<Integer>();
		if(i1.active) activeNodes.add(i1.instanceID);
		if(i2.active) activeNodes.add(i2.instanceID);
		if(i3.active) activeNodes.add(i3.instanceID);
		if(i4.active) activeNodes.add(i4.instanceID);
		if(i5.active) activeNodes.add(i5.instanceID);
		
		return activeNodes;
	}
	
	public boolean deleteWord(String word) {
		InstanceMapObj ix = i1;
		int checkInsts_5 = 0;
		boolean found = false;
		while(!found && checkInsts_5 < 5) {
			checkInsts_5 = checkInsts_5 + 1;
			if(!ix.active) {
				ix = this.getNextInstanceMapObj(ix, ix.instanceID);				
				continue;
			}
			DeleteResp delResp = this.INSTANCE_deleteWord(ix.webClient, word);
			if(delResp.status == 200) {
				found = true;
				ix.updateFills(ix.fills - 1);
				if(this.filledInstanceInfo.instanceMapObj.instanceID == ix.instanceID && this.filledInstanceInfo.pointer > 0) {
					this.filledInstanceInfo.pointer = this.filledInstanceInfo.pointer - 1;
				}
				// need to think on the filledInstanceInfo 
				// if this ix and that ix is same 
				// set some value
			}
			else if(delResp.status == 300) {
				ix = this.getNextInstanceMapObj(ix, ix.instanceID);
			}
		}
		return found;
	}
	
	public ArrayList<String> get1(){
		return this.INSTANCE_getWords(this.i1.webClient);
	}
	
	public ArrayList<String> get2(){
		return this.INSTANCE_getWords(this.i2.webClient);
	}

	public ArrayList<String> get3(){
		return this.INSTANCE_getWords(this.i3.webClient);
	}

	public ArrayList<String> get4(){
		return this.INSTANCE_getWords(this.i4.webClient);
	}

	public ArrayList<String> get5(){
		return this.INSTANCE_getWords(this.i5.webClient);
	}
	
}
