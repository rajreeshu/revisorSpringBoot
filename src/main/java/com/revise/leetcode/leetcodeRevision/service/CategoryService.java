package com.revise.leetcode.leetcodeRevision.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.revise.leetcode.leetcodeRevision.dto.CategoryDto;
import com.revise.leetcode.leetcodeRevision.dto.QuestionDto;
import com.revise.leetcode.leetcodeRevision.entity.Category;
import com.revise.leetcode.leetcodeRevision.entity.User;
import com.revise.leetcode.leetcodeRevision.repository.CategoryRepository;
import com.revise.leetcode.leetcodeRevision.repository.QuestionRepository;
import com.revise.leetcode.leetcodeRevision.repository.UserRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private QuestionService questionService;
	
	public CategoryDto addCategory(CategoryDto categoryDTO, String userEmail) {
		categoryDTO.setUserId(userRepository.getUserByEmail(userEmail).getId());
        Category category = convertToEntity(categoryDTO); // Map DTO to entity
        Category savedCategory = categoryRepository.save(category);
        return convertToDto(savedCategory); // Map entity back to DTO and return
    }

	public List<CategoryDto> getAllCategoriesForUser(String userEmail) {
        List<Category> categories = categoryRepository.findByUserEntityEmail(userEmail);
        return categories.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
	
	public Boolean deleteCategory(Long categoryId, String userEmail) throws NotFoundException{
		// TODO Auto-generated method stub
		User user = userRepository.getUserByEmail(userEmail);
		Category categoryEntity = categoryRepository.findByIdAndUserEntity_Email(categoryId, userEmail).get(0);
		 if (null==categoryEntity) {
	            throw new NotFoundException();
	        }
		 categoryRepository.deleteById(categoryId);
		return true;
	}
	
	public CategoryDto getCategoryByIdAndUserEmail(Long categoryId, String userEmail) {
        Category category = categoryRepository.findByIdAndUserEntity_Email(categoryId, userEmail).get(0);
        
        return convertToDto(category);
    }
    
    
    
//    Converter
    
	private CategoryDto convertToDto(Category category) {
	    CategoryDto categoryDTO = new CategoryDto();
	    categoryDTO.setId(category.getId());
	    categoryDTO.setCategory(category.getCategory());
	    categoryDTO.setUserId(category.getUserEntity().getId());
	    
	    List<QuestionDto> questionDto = category.getQuestions()
	    								.stream()
	    								.map(questionService::convertToDto)
	    								.collect(Collectors.toList());
	    categoryDTO.setQuestions(questionDto);
	    		
	    return categoryDTO;
	}

	private Category convertToEntity(CategoryDto categoryDTO) {
	    Category category = new Category();
	    category.setId(categoryDTO.getId());
	    category.setCategory(categoryDTO.getCategory());
	    User user = new User();
	    user.setId(categoryDTO.getUserId());
	    category.setUserEntity(user);
	    return category;
	}




}
