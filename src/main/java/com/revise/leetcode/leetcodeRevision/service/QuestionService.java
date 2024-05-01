package com.revise.leetcode.leetcodeRevision.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	Logger logger = LoggerFactory.getLogger(getClass());

	public QuestionDto saveQuestion(QuestionDto questionDTO, String userEmail) {
		questionDTO.setUserId(userRepository.getUserByEmail(userEmail).getId());
		Question question = convertToEntity(questionDTO);
		if (question.getLabel() == null) {
			question.setLabel(Difficulty.HARD);
		}
		question = questionRepository.save(question);
		return convertToDto(question);
	}
	
	public List<QuestionDto> questionsByUserAndCategory(String userEmail, Long categoryId){
		User user = userRepository.getUserByEmail(userEmail);
		Category category = categoryRepository.findById(categoryId).get();
		
		List<Question> questionByUser = questionRepository.getQuestionByUserAndCategory(user, category);
		return questionByUser.stream().map(this::convertToDto).collect(Collectors.toList());
	}

	public QuestionDto getRandomQuestion(String userEmail, long categoryId) {
		
		User userEntity = userRepository.getUserByEmail(userEmail);
		long userId = userEntity.getId();
		Category categoryEntity = categoryRepository.findByIdAndUserEntity(categoryId,userEntity).get(0);
		
		List<Question> questions = new ArrayList<>();
		
		//data last modified more than 1 month ago
		questions.addAll(questionRepository.findRandomQuestionsByLabelAndCategory("HARD", 4,userId, categoryEntity.getId(), LocalDateTime.now().minusDays(30)));
		questions.addAll(questionRepository.findRandomQuestionsByLabelAndCategory("MEDIUM", 2,userId,  categoryEntity.getId(), LocalDateTime.now().minusDays(30)));
		questions.addAll(questionRepository.findRandomQuestionsByLabelAndCategory("EASY", 1,userId, categoryEntity.getId(), LocalDateTime.now().minusDays(30)));
		
		String[] quesDiff = new String[] {"HARD","MEDIUM","EASY"};
		int count=0;
		while(questions.size()<7 && count<7) {
			questions.addAll(questionRepository.findRandomQuestionsByLabelAndCategory(quesDiff[count/3], 1,userId, categoryEntity.getId(), LocalDateTime.now()));
			count++;
		}

		if (questions.isEmpty()) {
			logger.info("null value found");
			return null; // or handle this case as you see fit
		}
		
		
		
		// Shuffle the combined list to mix difficulties randomly
	    Collections.shuffle(questions);
	    
//	    logger.info("Question List: {}",questions);
	    for(Question ques : questions) {
	    	logger.info("Question: {}",ques.toString());
	    }

		int randomIndex = ThreadLocalRandom.current().nextInt(questions.size());
		return convertToDto(questions.get(randomIndex));
	}
	
    public void updateQuestionLabel(long questionId, Difficulty newLabel) throws Exception {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new Exception("Question not found with id: " + questionId));
        
        question.setLabel(newLabel);
        questionRepository.save(question);
    }
    
    public Map<String, Integer> getCountByDifficulty(String userEmail, long categoryId) {
        User user = userRepository.getUserByEmail(userEmail);
        List<Category> categoryList =  categoryRepository.findByIdAndUserEntity(categoryId,user);
        Map<String, Integer> countMap = new HashMap<>();
        for (Difficulty difficulty : Difficulty.values()) {
            countMap.put(difficulty.name(), questionRepository.countByUserAndLabelAndCategory(user, difficulty, categoryList.get(0)));
        }
        return countMap;
    }

	// Business methods for questions

	public QuestionDto convertToDto(Question question) {
		QuestionDto dto = new QuestionDto();
		dto.setId(question.getId());
		dto.setTitle(question.getTitle());
		dto.setDescription(question.getDescription());
		dto.setSolution(question.getSolution());
		dto.setLabel(question.getLabel()); // Convert enum to String
		return dto;
	}

	public Question convertToEntity(QuestionDto dto) {
		Question question = new Question();
		User userEntity = userRepository.findById(dto.getUserId()).get();
		Category categoryEntity = categoryRepository.findByIdAndUserEntity(dto.getCategoryId(),userEntity).get(0);
		question.setId(dto.getId());
		question.setTitle(dto.getTitle());
		question.setDescription(dto.getDescription());
		question.setSolution(dto.getSolution());
		question.setLabel(dto.getLabel());
		question.setUser(userEntity);
		question.setCategory(categoryEntity);
		return question;
	}

	@Transactional
    public boolean deleteQuestion(long questionId, String userEmail) {
//		User user = 
		Question question = questionRepository.findByIdAndUser_Email(questionId, userEmail).get(0);
        if (question != null) {
            questionRepository.delete(question);
            return true;
        }
        return false;
    }
}