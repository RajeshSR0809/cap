package com.jv.cap;

import java.net.http.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class WordsController {

	
	@Autowired
	EC2Instance ec2Instance;

	public WordsController() {
		
	}
	
	@GetMapping("/words")
	public ArrayList<String> getAllWords() {
		this.ec2Instance.initProcess();
		return this.ec2Instance.getAllWords();
	}
	
	
	@DeleteMapping("/words")
	public boolean deleteAllWords() {
		this.ec2Instance.initProcess();
		this.ec2Instance.deleteAllWords();
		return true;
	}
	
	@PostMapping("/words")
	public ArrayList<String> addWords(@RequestBody ArrayList<String> words) {
		this.ec2Instance.initProcess();
		ArrayList<String> resp = new ArrayList<String>();
		if(ec2Instance.isAllInstancesFilled()) {
			ArrayList<String> respFilled = ec2Instance.filledOprV2(words);
			resp.addAll(respFilled);
		}
		else {
			ArrayList<String> respOpr =  ec2Instance.oprv2(words);
			resp.addAll(respOpr);
		}
		//ec2Instance.getStats();
		return resp;
	}

	@DeleteMapping("/words/{wordValue}")
	public boolean deleteWord(@PathVariable("wordValue") String wordValue) {
		this.ec2Instance.initProcess();
		return this.ec2Instance.deleteWord(wordValue);
	}
	
	@PatchMapping("/node/{nodeId}")
	public boolean changeNodeStatus(@PathVariable("nodeId") String nodeId, @RequestParam("active") String status) {
		this.ec2Instance.initProcess();
		boolean statusMap = "enable".equals(status) ? true : false;
		return this.ec2Instance.changeInstanceStatus(Integer.parseInt(nodeId), statusMap);
	}
	
	@GetMapping("/nodes/status")
	public ArrayList<Integer> getActiveNodes(@RequestParam("type") String type) {
		this.ec2Instance.initProcess();
		return this.ec2Instance.getActiveNodeIds();
	}
	
	@GetMapping("check")
	public String check() {
		ec2Instance.getStats();
		return "check console";
		
	}

	@GetMapping("get1")
	public ArrayList<String> get1() {
		return this.ec2Instance.get1();
	}	
	
	@GetMapping("get2")
	public ArrayList<String> get2() {
		return this.ec2Instance.get2();
	}	
	
	@GetMapping("get3")
	public ArrayList<String> get3() {
		return this.ec2Instance.get3();
	}	
	
	@GetMapping("get4")
	public ArrayList<String> get4() {
		return this.ec2Instance.get4();
	}	
	
	@GetMapping("get5")
	public ArrayList<String> get5() {
		return this.ec2Instance.get5();
	}		
}
