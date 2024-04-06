package com.revise.leetcode.leetcodeRevision.dto;

import java.util.List;

import lombok.Data;

@Data
public class CategoryDto {
	private Long id;
    private String category;
    private Long userId;
    private List<QuestionDto> questions;
}
