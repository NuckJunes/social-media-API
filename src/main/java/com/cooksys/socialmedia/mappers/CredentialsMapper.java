package com.cooksys.socialmedia.mappers;

import org.mapstruct.Mapper;

import com.cooksys.quiz_api.mappers.AnswerMapper;
import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.entities.Credentials;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface CredentialsMapper {
	Credentials requestDtoToEntity (CredentialsDto credentialsDto);
	
	CredentialsDto responseDtoToEntity(Credentials entity);
	
	// not sure if we need to grad a list of credentials
	// sounds not secure but can be easily added
	
}
