package com.revise.leetcode.leetcodeRevision.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revise.leetcode.leetcodeRevision.entity.Category;
import com.revise.leetcode.leetcodeRevision.entity.User;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	List<Category> findByUserEntityEmail(String email);
	
	List<Category> findByIdAndUserEntity(Long id, User user);
	
	List<Category> findByIdAndUserEntity_Email(Long id, String email);
}
