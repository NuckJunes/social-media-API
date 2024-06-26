package com.cooksys.socialmedia.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.exceptions.BadRequestException;
import com.cooksys.socialmedia.exceptions.NotFoundException;
import com.cooksys.socialmedia.mappers.UserMapper;
import com.cooksys.socialmedia.repositories.UserRepository;
import com.cooksys.socialmedia.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	
	private void validateUserRequest(UserRequestDto userRequestDto) {
		if(userRequestDto == null || userRequestDto.getCredentials() == null || userRequestDto.getProfile() == null) {
			throw new BadRequestException("Must have some credentials and profile");
		}
	}
	
	@Override
	public List<UserResponseDto> getAllUsers() {
		return userMapper.entitiesToDtos(userRepository.findAllByDeletedFalse());
	}

	@Override
	public UserResponseDto getUserByUsername(String username) {
		User optionalUser = userRepository.findByCredentialsUsername(username);
		if(optionalUser == null) {
			throw new NotFoundException("No User with the username: " + username);
		}
		return userMapper.entityToDto(optionalUser);
	}

	@Override
	public UserResponseDto createUser(UserRequestDto userRequestDto) {
		validateUserRequest(userRequestDto);
		User userToCreate = userMapper.requestDtoToEntity(userRequestDto);
		userRepository.saveAndFlush(userToCreate);
		return userMapper.entityToDto(userToCreate);
	}

	@Override
	public Boolean findIfUserExists(String username) {
		return userRepository.existsByCredentialsUsername(username);
	}

    
}
