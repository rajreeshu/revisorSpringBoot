package com.revise.leetcode.leetcodeRevision.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revise.leetcode.leetcodeRevision.domains.EntityDomains.Difficulty;
import com.revise.leetcode.leetcodeRevision.dto.QuestionDto;
import com.revise.leetcode.leetcodeRevision.entity.Category;
import com.revise.leetcode.leetcodeRevision.entity.Question;
import com.revise.leetcode.leetcodeRevision.entity.User;
import com.revise.leetcode.leetcodeRevision.repository.CategoryRepository;
import com.revise.leetcode.leetcodeRevision.repository.QuestionRepository;
import com.revise.leetcode.leetcodeRevision.repository.UserRepository;

@Service
public class QuestionService {
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	public QuestionDto saveQuestion(QuestionDto questionDTO, String userEmail) {
		questionDTO.setUserId(userRepository.getUserByEmail(userEmail).getId());
		Question question = convertToEntity(questionDTO);
		if (question.getLabel() == null) {
			question.setLabel(Difficulty.HARD);
		}
		question = questionRepository.save(question);
		return convertToDto(question);
	}

	public QuestionDto getRandomQuestion(String userEmail, String category) {
		
		User userEntity = userRepository.getUserByEmail(userEmail);
		long userId = userEntity.getId();
		Category categoryEntity = categoryRepository.findByUserEntityAndCategory(userEntity,category).get(0);
		
		List<Question> questions = new ArrayList<>();
		questions.addAll(questionRepository.findRandomQuestionsByLabelAndCategory("HARD", 4,userId, categoryEntity.getId()));
		questions.addAll(questionRepository.findRandomQuestionsByLabelAndCategory("MEDIUM", 2,userId,  categoryEntity.getId()));
		questions.addAll(questionRepository.findRandomQuestionsByLabelAndCategory("EASY", 1,userId, categoryEntity.getId()));

		if (questions.isEmpty()) {
			return null; // or handle this case as you see fit
		}

		int randomIndex = ThreadLocalRandom.current().nextInt(questions.size());
		return convertToDto(questions.get(randomIndex));
	}
	
    public void updateQuestionLabel(Long questionId, Difficulty newLabel) throws Exception {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new Exception("Question not found with id: " + questionId));
        
        question.setLabel(newLabel);
        questionRepository.save(question);
    }
    
    public Map<String, Integer> getCountByDifficulty(String userEmail, String category) {
        User user = userRepository.getUserByEmail(userEmail);
        List<Category> categoryList =  categoryRepository.findByUserEntityAndCategory(user, category);
        Map<String, Integer> countMap = new HashMap<>();
        for (Difficulty difficulty : Difficulty.values()) {
            countMap.put(difficulty.name(), questionRepository.countByUserAndLabelAndCategory(user, difficulty, categoryList.get(0)));
        }
        return countMap;
    }

	// Business methods for questions

	private QuestionDto convertToDto(Question question) {
		QuestionDto dto = new QuestionDto();
		dto.setId(question.getId());
		dto.setTitle(question.getTitle());
		dto.setDescription(question.getDescription());
		dto.setSolution(question.getSolution());
		dto.setLabel(question.getLabel()); // Convert enum to String
		return dto;
	}

	private Question convertToEntity(QuestionDto dto) {
		Question question = new Question();
		User userEntity = userRepository.findById(dto.getUserId()).get();
		Category categoryEntity = categoryRepository.findByUserEntityAndCategory(userEntity,dto.getCategory()).get(0);
		question.setId(dto.getId());
		question.setTitle(dto.getTitle());
		question.setDescription(dto.getDescription());
		question.setSolution(dto.getSolution());
		question.setLabel(dto.getLabel());
		question.setUser(userEntity);
		question.setCategory(categoryEntity);
		return question;
	}
}