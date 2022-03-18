package com.jv.jv;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class JvApplication {

	
	public ArrayList<String> Array_wordsCollection = new ArrayList<String>();
	public boolean active = true;

	
	@GetMapping("/words")
	public ArrayList<String> getAllWords() {
		ArrayList<String> allwords =  new ArrayList<String>(this.Array_wordsCollection);
		this.getStats();
		return allwords;
	}
    
    @PostMapping("/words")//you can give this any name you want and after adding this string to the end of base url you can use this
    public ArrayList<String> setword(@RequestBody ArrayList<String> words) {
    	Iterator<String> wItr = words.iterator();
    	while(wItr.hasNext()) {
    		String word = wItr.next();
    		Array_wordsCollection.add(word);
    	}
    	this.getStats();
    	return words;
    }    
    
	@DeleteMapping("/words")
	public boolean deleteAllWords() {
		this.Array_wordsCollection.clear();
		this.getStats();
		return true;
	}    
    
	
	@PatchMapping("/words")
	public boolean deleteWords(@RequestParam("start") int startIndex, @RequestParam("end")  int endIndex) {
		this.Array_wordsCollection.subList(startIndex, endIndex).clear();
		return true;
	} 
	
	
    @PatchMapping("/node")
    public boolean updateActiveStatus(@RequestParam("active") boolean status) {
    	this.active = status;
    	this.getStats();
    	return this.active;
    }
    
    
    @DeleteMapping("/words/{wordValue}")
    public Integer deleteWord(@PathVariable("wordValue") String wordValue) {
    	if(this.Array_wordsCollection.contains(wordValue)) {
    		this.Array_wordsCollection.remove(wordValue);
    		this.getStats();
    		return 200;
    	}
    	else {
    		this.getStats();
    		return 300;
    	}
    }
    
    public void getStats() {
    	Iterator<String> itr =  this.Array_wordsCollection.iterator();
    	System.out.println("INSTANCE 2 ===============>");
    	System.out.println();
    	while(itr.hasNext()) {
    		String v = itr.next();
    		System.out.println(v);
    	}
    	System.out.println();
    }
    
	public static void main(String[] args) {
		SpringApplication.run(JvApplication.class, args);
	}

}
