package com.revise.leetcode.leetcodeRevision.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.revise.leetcode.leetcodeRevision.dto.UserDto;
import com.revise.leetcode.leetcodeRevision.service.UserService;

@CrossOrigin("*")
@Controller
@RequestMapping("/users")
public class UserController {
    
	@Autowired
	private UserService userService;
	
	@PostMapping()
    public ResponseEntity<UserDto> addUser(@RequestBody UserDto userDto) {
        UserDto newUser = userService.addUser(userDto);
        return ResponseEntity.ok(newUser);
    }
//	
    @GetMapping()
    public ResponseEntity<UserDto> login(@RequestParam("email") String email,
                                        @RequestParam("password") String password) {
    	UserDto userDto = userService.login(email,password);
        return ResponseEntity.ok(userDto);
    }

    

}
