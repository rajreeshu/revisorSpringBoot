package com.revise.leetcode.leetcodeRevision.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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

	@GetMapping("/random-question/{categoryId}")
	public ResponseEntity<QuestionDto> getRandomQuestion(@PathVariable long categoryId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User principal = (User) authentication.getPrincipal();
		String userEmail = principal.getUsername();
		
		return ResponseEntity.ok(questionService.getRandomQuestion(userEmail,categoryId)); // Implement this method to return a random
																		// question
	}

	@PutMapping("/updateLabel/{id}")
	public ResponseEntity<String> updateQuestionLabel(@PathVariable long id, @RequestBody Difficulty newLabel) {
		try {
			questionService.updateQuestionLabel(id, newLabel);
			return ResponseEntity.ok("Label updated successfully");
		} catch (Exception e) {
			return ResponseEntity.status(500).body("Error updating label: " + e.getMessage());
		}
	}
	
	@GetMapping("/count/{categoryId}")
	public ResponseEntity<Map<String, Integer>> getQuestionCounts(@PathVariable long categoryId) {
//		logger.warn(categoryId.to);
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User principal = (User) authentication.getPrincipal();
		String userEmail = principal.getUsername();
		
		logger.warn("UserEmail:"+userEmail);
		
		Map<String, Integer> counts = questionService.getCountByDifficulty(userEmail,categoryId);

	    // Check if the counts map is not empty or null
	    if (counts != null && !counts.isEmpty()) {
	        return ResponseEntity.ok(counts); // Return 200 OK with body
	    } else {
	        return ResponseEntity.notFound().build(); // Return 404 Not Found
	    }
	}
	
	@DeleteMapping("/{questionId}")
    public ResponseEntity<String> deleteQuestion(@PathVariable long questionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        String userEmail = principal.getUsername();

        try {
            boolean isDeleted = questionService.deleteQuestion(questionId, userEmail);
            if (isDeleted) {
                return ResponseEntity.ok("Question deleted successfully");
            } else {
                return ResponseEntity.notFound().build(); // If the question was not found or not deleted
            }
        } catch (Exception e) {
            logger.error("Error deleting question: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting question: " + e.getMessage());
        }
    }


}
