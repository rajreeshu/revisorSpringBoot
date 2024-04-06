package com.revise.leetcode.leetcodeRevision.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtRequest {
	@NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
	private String email;
    private String password;
}
