package com.revise.leetcode.leetcodeRevision.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revise.leetcode.leetcodeRevision.dto.CategoryDto;
import com.revise.leetcode.leetcodeRevision.service.CategoryService;

@CrossOrigin("*")
@RestController
@RequestMapping("/categories")
public class CategoryController {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CategoryService categoryService;

	@PostMapping()
	public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDTO) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User principal = (User) authentication.getPrincipal();
		String userEmail = principal.getUsername();
		
		CategoryDto newCategory = categoryService.addCategory(categoryDTO,userEmail);
		return ResponseEntity.ok(newCategory);
	}

	@GetMapping
	public ResponseEntity<List<CategoryDto>> getAllCategoriesForUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		User principal = (User) authentication.getPrincipal();
		String userEmail = principal.getUsername();
		
		List<CategoryDto> categories = categoryService.getAllCategoriesForUser(userEmail);
		return ResponseEntity.ok(categories);
	}
}
