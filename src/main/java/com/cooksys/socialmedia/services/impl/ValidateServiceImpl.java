package com.cooksys.socialmedia.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.mappers.UserMapper;
import com.cooksys.socialmedia.repositories.UserRepository;
import com.cooksys.socialmedia.services.ValidateService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	
	public Boolean isUsernameAvailable(String username) {
		List<User> users = userRepository.findAll();
		for(User u: users) {
			if (u.getCredentials().getUsername() == username) {
				return false;
			}
		}
		
		return true;
	}

}
