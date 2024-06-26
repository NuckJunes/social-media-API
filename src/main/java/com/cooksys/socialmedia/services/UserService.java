package com.cooksys.socialmedia.services;

import java.util.List;

import com.cooksys.socialmedia.dtos.UserResponseDto;

public interface UserService {

	List<UserResponseDto> getAllUsers();

}
