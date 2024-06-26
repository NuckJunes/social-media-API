package com.cooksys.socialmedia.services;

import java.util.List;

import com.cooksys.socialmedia.dtos.TweetResponseDto;

public interface TweetService {
	List<TweetResponseDto> getTweetsFromHashtag(String label);
}
