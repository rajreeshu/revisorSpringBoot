package com.revise.leetcode.leetcodeRevision.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.revise.leetcode.leetcodeRevision.domains.EntityDomains.Difficulty;
import com.revise.leetcode.leetcodeRevision.entity.Category;
import com.revise.leetcode.leetcodeRevision.entity.Question;
import com.revise.leetcode.leetcodeRevision.entity.User;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
	// Example using JPQL (adjust based on your database)
	@Query(value = "SELECT * FROM question WHERE label = ?1 AND user_id=?3 AND category_id=?4  ORDER BY RAND()*RAND() LIMIT ?2", nativeQuery = true)
    List<Question> findRandomQuestionsByLabelAndCategory(String label, int limit, long userId, long category);
	
	int countByUserAndLabelAndCategory(User user, Difficulty difficulty, Category category);
	
	List<Question> findByIdAndUser_Email(Long id, String userEmail);

	List<Question> getQuestionByUserAndCategory(User user, Category category);
	
}
