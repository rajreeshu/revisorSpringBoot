package com.revise.leetcode.leetcodeRevision.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revise.leetcode.leetcodeRevision.domains.EntityDomains.Difficulty;
import com.revise.leetcode.leetcodeRevision.dto.QuestionDto;
import com.revise.leetcode.leetcodeRevision.service.QuestionService;
import com.revise.leetcode.leetcodeRevision.service.UserService;


@RestController
@RequestMapping("/questions")
@CrossOrigin("*")
public class QuestionController {

	@Autowired
	private QuestionService questionService;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	

	@PostMapping("/add")
	public ResponseEntity<QuestionDto> addQuestion(@RequestBody QuestionDto questionDTO) {
		logger.warn(questionDTO.toString());
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User principal = (User) authentication.getPrincipal();
		String userEmail = principal.getUsername();
		
		QuestionDto savedQuestionDTO = questionService.saveQuestion(questionDTO, userEmail);
		return ResponseEntity.ok(savedQuestionDTO);
	}

	@GetMapping("/random-question/{category}")
	public ResponseEntity<QuestionDto> getRandomQuestion(@PathVariable String category) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User principal = (User) authentication.getPrincipal();
		String userEmail = principal.getUsername();
		
		return ResponseEntity.ok(questionService.getRandomQuestion(userEmail,category)); // Implement this method to return a random
																		// question
	}

	@PutMapping("/updateLabel/{id}")
	public ResponseEntity<String> updateQuestionLabel(@PathVariable Long id, @RequestBody Difficulty newLabel) {
		try {
			questionService.updateQuestionLabel(id, newLabel);
			return ResponseEntity.ok("Label updated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error updating label: " + e.getMessage());
		}
	}
	
	@GetMapping("/count/{category}")
	public ResponseEntity<Map<String, Integer>> getQuestionCounts(@PathVariable String category) {
		logger.warn(category);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User principal = (User) authentication.getPrincipal();
		String userEmail = principal.getUsername();
		
		logger.warn("UserEmail:"+userEmail);
		
		Map<String, Integer> counts = questionService.getCountByDifficulty(userEmail,category);

	    // Check if the counts map is not empty or null
	    if (counts != null && !counts.isEmpty()) {
	        return ResponseEntity.ok(counts); // Return 200 OK with body
	    } else {
	        return ResponseEntity.notFound().build(); // Return 404 Not Found
	    }
	}


}
