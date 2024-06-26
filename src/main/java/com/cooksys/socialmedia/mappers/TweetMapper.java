package com.cooksys.socialmedia.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.entities.Tweet;

//removed hashtag mapper
@Mapper(componentModel = "spring", uses = { UserMapper.class})
public interface TweetMapper {
	
	TweetResponseDto entityToDto(Tweet entity);
	
	Tweet requestDtoToEntity(TweetRequestDto tweetRequestDto);
	
	List<TweetResponseDto> entitiesToDto(List<Tweet> entities);

}
