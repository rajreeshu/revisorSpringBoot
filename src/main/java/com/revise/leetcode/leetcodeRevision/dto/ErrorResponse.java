package com.revise.leetcode.leetcodeRevision.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ErrorResponse {
	private LocalDateTime timestamp;
    private int status;
    private String error;
    private String displayMessage;
    private Map<String, String> errorMessages; 
}
