package com.revise.leetcode.leetcodeRevision.dto;

import com.revise.leetcode.leetcodeRevision.domains.EntityDomains.Difficulty;

import lombok.Data;

@Data
public class QuestionDto {
    private Long id;
    private String title;
    private String description;
    private String solution;
    private Long categoryId;
    private Difficulty label;
    private Long userId;

}
