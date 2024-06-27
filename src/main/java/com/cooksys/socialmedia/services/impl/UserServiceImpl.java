package com.cooksys.socialmedia.services.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserRequestDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.exceptions.BadRequestException;
import com.cooksys.socialmedia.exceptions.NotFoundException;
import com.cooksys.socialmedia.mappers.TweetMapper;
import com.cooksys.socialmedia.mappers.UserMapper;
import com.cooksys.socialmedia.repositories.TweetRepository;
import com.cooksys.socialmedia.repositories.UserRepository;
import com.cooksys.socialmedia.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	
	// Validate that the request has all the required fields
	private void validateUserRequest(UserRequestDto userRequestDto) {
		if(userRequestDto == null || userRequestDto.getCredentials() == null || userRequestDto.getProfile() == null) {
			throw new BadRequestException("Must have some credentials and profile");
		}
	}
	
	// Check that the user exists and is not deleted in our DB
	private void checkUserExists(User user) {
		if(user == null || user.isDeleted()) {
			throw new NotFoundException("No users with match username found.");
		}
	}
	
	//quicksort algorithm for sorting tweets by date
	private void quickSort(List<Tweet> tweets, int begin, int end) {
		if(begin < end) {
			int partitionIndex = partition(tweets, begin, end);
			
			quickSort(tweets, begin, partitionIndex - 1);
			quickSort(tweets, partitionIndex + 1, end);
		}
	}
	
	//quicksorts partition for sorting tweets by date
	private int partition(List<Tweet> tweets, int begin, int end) {
		Timestamp pivot = tweets.get(end).getPosted();
		int i = (begin - 1);
		
		for(int j = begin; j < end; j++) {
			if(tweets.get(j).getPosted().compareTo(pivot) <= 0) {
				i++;
				
				Tweet swapTemp = tweets.get(i);
				tweets.set(i, tweets.get(j));
				tweets.set(j, swapTemp);
			}
		}
		
		Tweet swapTemp = tweets.get(i + 1);
		tweets.set(i + 1, tweets.get(end));
		tweets.set(end, swapTemp);
		
		return i + 1;
	}
	
	@Override
	public List<UserResponseDto> getAllUsers() {
		return userMapper.entitiesToDtos(userRepository.findAllByDeletedFalse());
	}

	@Override
	public UserResponseDto getUserByUsername(String username) {
		User optionalUser = userRepository.findByCredentialsUsername(username);
		if(optionalUser == null) {
			throw new NotFoundException("No User with the username: " + username);
		}
		return userMapper.entityToDto(optionalUser);
	}

	@Override
	public UserResponseDto createUser(UserRequestDto userRequestDto) {
		validateUserRequest(userRequestDto);
		User userToCreate = userMapper.requestDtoToEntity(userRequestDto);
		userRepository.saveAndFlush(userToCreate);
		return userMapper.entityToDto(userToCreate);
	}

	@Override
	public Boolean findIfUserExists(String username) {
		return userRepository.existsByCredentialsUsername(username);
	}

	@Override
	public UserResponseDto deleteUser(CredentialsDto userRequestDto) {
		User userToDelete = userRepository.findByCredentialsUsernameAndCredentialsPassword(userRequestDto.getUsername(), userRequestDto.getPassword()); //IMPORTANT: mmay need to make CredentialsPassword
		if (userToDelete == null) {
			throw new BadRequestException("No user with matching username and password found.");
		}
		userToDelete.setDeleted(true); //Soft delete a user and any of their tweets
		for (Tweet userTweet : userToDelete.getTweets()) {
			userTweet.setDeleted(true);
		}
		userRepository.saveAndFlush(userToDelete);//This is creating a second user/password? Yes, because it is not PATCHING
		return userMapper.entityToDto(userToDelete); //Return the soft deleted user
	}

	@Override
	public UserResponseDto editUser(UserRequestDto userRequestDto, String username) {
		User userToUpdate = userRepository.findByCredentialsUsername(username);
		checkUserExists(userToUpdate);
		User newUser = userMapper.requestDtoToEntity(userRequestDto);
		userToUpdate.setCredentials(newUser.getCredentials());
		userToUpdate.setProfile(newUser.getProfile());
		return userMapper.entityToDto(userRepository.saveAndFlush(userToUpdate));
	}

	@Override
	public List<TweetResponseDto> getFeed(String username) {
		User userFeed = userRepository.findByCredentialsUsername(username);
		checkUserExists(userFeed);
		
		List<Tweet> feed = userFeed.getTweets();
		
		//get all tweets for user and following users
		for(User u : userFeed.getFollowing()) {
			List<Tweet> uFeedTweets = u.getTweets();
			for(Tweet t : uFeedTweets) {
				if(!feed.contains(t) || !t.isDeleted()) {
					feed.add(t);
				}
			}
		}
		
		//quicksort all tweets
		quickSort(feed, 0, feed.size()-1);
		
		return tweetMapper.entitiesToDto(feed);
	}

	@Override
	public List<TweetResponseDto> getUserTweets(String username) {
		User userTweets = userRepository.findByCredentialsUsername(username);
		checkUserExists(userTweets);
		
		List<Tweet> tweets = userTweets.getTweets();
		return tweetMapper.entitiesToDto(tweets);
	}

	@Override
	public List<TweetResponseDto> getUserMentions(String username) {
		User userMentions = userRepository.findByCredentialsUsername(username);
		checkUserExists(userMentions);
		List<Tweet> mentions = userMentions.getMentions();
		quickSort(mentions, 0, mentions.size()-1);
		return tweetMapper.entitiesToDto(mentions);
	}

	@Override
	public List<UserResponseDto> getUserFollowers(String username) {
		User userFollowers = userRepository.findByCredentialsUsername(username);
		checkUserExists(userFollowers);
		List<User> followers = userFollowers.getFollowers();
		return userMapper.entitiesToDtos(followers);
	}

	@Override
	public List<UserResponseDto> getUserFollowing(String username) {
		User userFollowing = userRepository.findByCredentialsUsername(username);
		checkUserExists(userFollowing);
		List<User> following = userFollowing.getFollowing();
		return userMapper.entitiesToDtos(following);
	}

    
}
