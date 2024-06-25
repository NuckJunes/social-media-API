package com.cooksys.socialmedia.mappers;

import org.mapstruct.Mapper;

import com.cooksys.socialmedia.dtos.HashtagDto;
import com.cooksys.socialmedia.entities.Hashtag;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

	Hashtag DtoToEntity(HashtagDto hashTagDto);
	
	HashtagDto EntityToDto(Hashtag hashtag);
}
