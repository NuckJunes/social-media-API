package com.cooksys.socialmedia.services;

import java.util.List;

import com.cooksys.socialmedia.dtos.ContextDto;
import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;

public interface TweetService {
	List<TweetResponseDto> getTweetsFromHashtag(String label);

	List<TweetResponseDto> getAllTweets();

	TweetResponseDto getTweetById(Long id); 

	TweetResponseDto createTweet(TweetRequestDto tweetRequestDto);

	TweetResponseDto deleteTweet(Long id);

	List<UserResponseDto> getMentions(Long id);

	List<TweetResponseDto> getReposts(Long id);

	ContextDto getContext(Long id);

	List<TweetResponseDto> getReplies(Long id);

	TweetResponseDto createReplyTweet(TweetRequestDto tweetRequestDto, Long replyTweetId);
}
