package com.revise.leetcode.leetcodeRevision.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revise.leetcode.leetcodeRevision.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByEmail(String username);
}
