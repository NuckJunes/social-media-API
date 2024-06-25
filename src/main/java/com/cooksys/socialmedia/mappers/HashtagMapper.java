package com.cooksys.socialmedia.mappers;

import org.mapstruct.Mapper;

import com.cooksys.socialmedia.entities.Hashtag;
import com.cooksys.socialmedia.model.HashtagDto;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

	Hashtag DtoToEntity(HashtagDto hashTagDto);
	
	HashtagDto EntityToDto(Hashtag hashtag);
}
