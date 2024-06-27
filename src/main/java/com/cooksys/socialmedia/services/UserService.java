package com.cooksys.socialmedia.services;

import java.util.List;

import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;

public interface UserService {

	List<UserResponseDto> getAllUsers();

	UserResponseDto getUserByUsername(String username);

	UserResponseDto createUser(UserRequestDto userRequestDto);
	
	Boolean findIfUserExists(String username);
	
	UserResponseDto deleteUser(CredentialsDto userRequestDto);

	UserResponseDto editUser(UserRequestDto userRequestDto, String username);

}
