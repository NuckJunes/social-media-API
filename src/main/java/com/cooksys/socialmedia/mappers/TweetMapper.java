package com.cooksys.socialmedia.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.quiz_api.mappers.AnswerMapper;
import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.entities.Tweet;


@Mapper(componentModel = "spring", uses = { HashtagMapper.class })
public interface TweetMapper {
	
	TweetResponseDto entityToDto(Tweet entity);
	
	Tweet requestDtoToEntity(TweetResponseDto tweetResponseDto);
	
	List<TweetResponseDto> entitiesToDto(List<TweetResponseDto> entities);

}
