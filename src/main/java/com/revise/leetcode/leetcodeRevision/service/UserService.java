package com.revise.leetcode.leetcodeRevision.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revise.leetcode.leetcodeRevision.dto.UserDto;
import com.revise.leetcode.leetcodeRevision.entity.User;
import com.revise.leetcode.leetcodeRevision.repository.UserRepository;

@Service
public class UserService {
    @Autowired
	private UserRepository userRepository;
    
//    @Autowired
//    private PasswordEncoder passwordEncoder;
    
    public UserDto addUser(UserDto userDto) {
        // Convert DTO to entity
//    	userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User savedUser = userRepository.save(convertToEntity(userDto));
        
        // Convert saved entity back to DTO
        return convertToDto(savedUser);
    }
    
	public UserDto login(String email, String password) {
		User user = userRepository.getUserByEmail(email);
		if(user==null) return null;
		if(!user.getPassword().equals(password)) {
			System.out.println("Password can't be matched");
			return null;
		}
		UserDto userDto = convertToDto(user);
		return userDto;
		
	}

   

    // Business methods for user
    
    public UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        // Do not convert the password for security reasons
        dto.setGender(user.getGender());
        return dto;
    }
    
    public User convertToEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId()); // Be cautious when setting ID, depending on the use case
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // Consider encrypting the password here
        user.setGender(dto.getGender());
        return user;
    }




}
