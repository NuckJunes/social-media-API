package com.cooksys.socialmedia.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.entities.Hashtag;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.exceptions.BadRequestException;
import com.cooksys.socialmedia.exceptions.NotFoundException;
import com.cooksys.socialmedia.mappers.TweetMapper;
import com.cooksys.socialmedia.repositories.HashtagRepository;
import com.cooksys.socialmedia.repositories.TweetRepository;
import com.cooksys.socialmedia.services.TweetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
	private final HashtagRepository hashtagRepository;
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	
	private void validateTweetRequest(TweetRequestDto tweetRequestDto) {
		if(tweetRequestDto == null || tweetRequestDto.getCredentials() == null) {
			throw new BadRequestException("Tweet must have credentials");
		}
	}
	
	
	// GET ALL METHOD
	@Override
	public List<TweetResponseDto> getAllTweets(){
		return tweetMapper.entitiesToDto(tweetRepository.findAllByDeletedFalse());
	}
	
	// GET BY ID
	@Override
	public TweetResponseDto getTweetById(Long id) {
		Tweet optionalTweet = tweetRepository.findById(id).get();
		if (optionalTweet == null) {
			throw new NotFoundException("No Tweet Found with the following ID: " + id);
		}
		if (optionalTweet.isDeleted()) {
			throw new NotFoundException("Tweet No Longer Exist");
		}
		return tweetMapper.entityToDto(tweetRepository.getReferenceById(id));
	}
	
	// POST 
	public TweetResponseDto createTweet(TweetRequestDto tweetRequestDto) {
		validateTweetRequest(tweetRequestDto);
		Tweet tweetToCreate = tweetMapper.requestDtoToEntity(tweetRequestDto);
		System.out.println(tweetToCreate);
		
		tweetRepository.saveAndFlush(tweetToCreate);
		return tweetMapper.entityToDto(tweetToCreate);
	}
	
	// DELETE
	public TweetResponseDto deleteTweet(Long id) {
		Tweet tweetToDelete = tweetRepository.getReferenceById(id);
		if (tweetToDelete == null) {
			throw new BadRequestException("No Tweet with Id:" + id);
		}
		tweetToDelete.setDeleted(true);
		tweetRepository.saveAndFlush(tweetToDelete);
		return tweetMapper.entityToDto(tweetToDelete);

	}
	

	@Override
	public List<TweetResponseDto> getTweetsFromHashtag(String label) {
		label = label.toLowerCase();//Case insensitive labels
		
		//label = "#" + label; //When using Seeded DB data, uncomment this line to append # to the label (seeded data is incorrect)
		
		Hashtag fetchedHashtag = hashtagRepository.findByLabel(label);
		if (fetchedHashtag == null) {//Throw exception if no hashtag could be found
			throw new BadRequestException("No hashtag with the given label could be found");
		}
		//We have the hashtag_id. Now fetch tweets with findByHashtags_Id() from tweet repository
		List<Tweet> taggedTweets = tweetRepository.findAllByHashtags_Id(fetchedHashtag.getId());
		List<TweetResponseDto> taggedTweetDtos= tweetMapper.entitiesToDto(taggedTweets);
		//TODO: Sort the above list of tweet dtos by revers chronological order (newest first).
		taggedTweetDtos.sort((o1, o2) -> o1.getPosted().compareTo(o2.getPosted()));; //May need getTime() to compare longs, if comparing Timestamp objects doesnt work.
		return taggedTweetDtos;
	}
}