package com.cooksys.socialmedia.services.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.ContextDto;
import com.cooksys.socialmedia.dtos.HashtagDto;
import com.cooksys.socialmedia.dtos.CredentialsDto;
import com.cooksys.socialmedia.dtos.TweetRequestDto;
import com.cooksys.socialmedia.dtos.TweetResponseDto;
import com.cooksys.socialmedia.dtos.UserResponseDto;
import com.cooksys.socialmedia.entities.Hashtag;
import com.cooksys.socialmedia.entities.Tweet;
import com.cooksys.socialmedia.entities.User;
import com.cooksys.socialmedia.exceptions.BadRequestException;
import com.cooksys.socialmedia.exceptions.NotFoundException;
import com.cooksys.socialmedia.mappers.HashtagMapper;
import com.cooksys.socialmedia.mappers.TweetMapper;
import com.cooksys.socialmedia.mappers.UserMapper;
import com.cooksys.socialmedia.repositories.HashtagRepository;
import com.cooksys.socialmedia.repositories.TweetRepository;
import com.cooksys.socialmedia.repositories.UserRepository;
import com.cooksys.socialmedia.services.HashtagService;
import com.cooksys.socialmedia.services.TweetService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TweetServiceImpl implements TweetService {
	private final HashtagService hashtagService;
	private final HashtagRepository hashtagRepository;
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final HashtagMapper hashtagMapper;
	
	private void validateTweetRequest(TweetRequestDto tweetRequestDto) {
		if(tweetRequestDto == null || tweetRequestDto.getCredentials() == null) {
			throw new BadRequestException("Tweet must have credentials");
		}
	}
	
	
	// GET ALL METHOD
	@Override
	public List<TweetResponseDto> getAllTweets(){
		return tweetMapper.entitiesToDtos(tweetRepository.findAllByDeletedFalse());
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
		//Validate that tweet request is a valid object
		validateTweetRequest(tweetRequestDto);
		
		//Fetch user from database
		User author = userRepository.findByCredentialsUsernameAndCredentialsPassword(tweetRequestDto.getCredentials().getUsername(), tweetRequestDto.getCredentials().getPassword());
		if (author == null) {
			throw new BadRequestException("No user could be found with matching credentials");
		}
		Tweet tweetToCreate = tweetMapper.requestDtoToEntity(tweetRequestDto);
		tweetToCreate.setAuthor(author); //Set the tweets author correctly
		
		//Sort out tweet's mentions and hashtags
        //Parse through content to fish out any @mentions
        Matcher matcher = Pattern.compile("@\\w+").matcher(tweetToCreate.getContent());
        while (matcher.find()) { //For every match we find for the given regex
            if (userRepository.existsByCredentialsUsername(matcher.group().replace("@", ""))) { //Check if mentioned user exists in the DB
                tweetToCreate.getUsers_mentions().add(userRepository.findByCredentialsUsername(matcher.group().replace("@", ""))); //If they do, set that here.
            }
        }

        //Now fish out #hashtags
        matcher = Pattern.compile("#\\w+").matcher(tweetToCreate.getContent());
        while (matcher.find()) { //For every match we find for the given regex
            String label = matcher.group().replace("#", ""); //Format label
            Hashtag hashtag;
            if (hashtagRepository.existsByLabel(label)) { //If given label is in our db (existing hashtag)
                hashtag = hashtagRepository.findByLabel(label); //Fetch the matched hashtag via label
                hashtag.setLastUsed(Timestamp.valueOf(LocalDateTime.now())); //update the haghtag's last used property
            } else {
                hashtag = hashtagService.createHashtag(label); //Create a new hashtag
            } 
            tweetToCreate.getHashtags().add(hashtag);//Add hashtag to our tweet's list of hashtags
        }
		
		//Save fully developed tweet to DB (this should also save all nested entities, such as the hashtags and user mentions)
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
	
	// GET CONTEXT
	public ContextDto getContext(Long id){
		Tweet tweet = tweetRepository.getReferenceById(id);
		if (tweet.isDeleted()) {
			throw new NotFoundException("No tweet found, id:" + id);
		}
		List<Tweet> before = new ArrayList<>();
		List<Tweet> after = new ArrayList<>();		
		List<Tweet> afterUnfiltered  = tweet.getReplies();
		Tweet currentTweet = tweet.getInReplyTo();
		
		while (currentTweet != null) {
			before.add(currentTweet);
			currentTweet = currentTweet.getInReplyTo();
		}
		
		for (Tweet t: afterUnfiltered) {
			if (!t.isDeleted()) {
				after.add(t);
			}
		}
		
		ContextDto context = new ContextDto();
		context.setTarget(tweet);
		context.setBefore(tweetMapper.entitiesToDtos(before));
		context.setAfter(tweetMapper.entitiesToDtos(after));
		return context;
	}
	
	// GET REPOST
	public List<TweetResponseDto> getReposts(Long id){
		Tweet tweet = tweetRepository.getReferenceById(id);
		if (tweet.isDeleted()) {
			throw new NotFoundException("No tweet found, id:" + id);
		}
		
		List<Tweet> res = new ArrayList<>();
		for (Tweet t: tweet.getReposts()) {
			if (!t.isDeleted()) {
				res.add(t);
			}
		}
//			if(res == null) {
//				throw new NotFoundException("No users found");
//			}
		return tweetMapper.entitiesToDtos(res);
	}

	// GET REPLIES
	public List<TweetResponseDto> getReplies(Long id){
		Tweet tweet = tweetRepository.getReferenceById(id);
		if (tweet.isDeleted()) {
			throw new NotFoundException("No tweet found, id:" + id);
		}
		
		List<Tweet> res  = new ArrayList<>();
		for (Tweet t: tweet.getReplies()) {
			if (!t.isDeleted()) {
				res.add(t);
			}
		}
//			if(res == null) {
//				throw new NotFoundException("No users found");
//			}
		
		return tweetMapper.entitiesToDtos(res);
	}
	
	// GET MENTIONS
	
	public List<UserResponseDto> getMentions(Long id){
		Tweet tweet = tweetRepository.getReferenceById(id);
		List<User> res = new ArrayList<>();
		for (User u: tweet.getUsers_mentions()) {
			if (!u.isDeleted()) {
				res.add(u);
			}
		}
//			if(res.isEmpty()) {
//				throw new NotFoundException("No users found");
//			}
		
		return userMapper.entitiesToDtos(res);
	}
	
	// GET LIKES
	public List<UserResponseDto> getLikes(Long id) {
		Tweet tweet = tweetRepository.getReferenceById(id);
		if (tweet.isDeleted()) {
			throw new NotFoundException("No tweet found, id:" + id);
		}
		List<User> res = new ArrayList<>();
		for (User u: tweet.getUsers_likes()) {
			if (!u.isDeleted()) {
				res.add(u);
			}
		}
		return userMapper.entitiesToDtos(res);
	}
	 
	// GET TAGS
	
	public List<HashtagDto> getTags(Long id) {
		Tweet tweet = tweetRepository.getReferenceById(id);
		if (tweet.isDeleted()) {
			throw new NotFoundException("No tweet found, id:" + id);
		}
		List<Hashtag> res = new ArrayList<>();
		for (Hashtag h: tweet.getHashtags()) {
			if (!h.isDeleted()) {
				res.add(h);
			}
		}
		return hashtagMapper.entitiesToDtos(res);
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
		List<TweetResponseDto> taggedTweetDtos= tweetMapper.entitiesToDtos(taggedTweets);
		//TODO: Sort the above list of tweet dtos by revers chronological order (newest first).
		taggedTweetDtos.sort((o1, o2) -> o1.getPosted().compareTo(o2.getPosted()));; //May need getTime() to compare longs, if comparing Timestamp objects doesnt work.
		return taggedTweetDtos;
	}


	@Override
	public TweetResponseDto createReplyTweet(TweetRequestDto tweetRequestDto, Long replyTweetId) { //Reply tweet Dtos MUST have content and replyTo, but null repostOf
		//1. Validate that tweet request is a valid object
		validateTweetRequest(tweetRequestDto);
		
		//2. Ensure the tweet being replied to exists.
		Tweet originalTweet = tweetRepository.findById(replyTweetId).get(); //No such element exception will be thrown if tweet does not exist
		
		//3. Validate that the user with given credentials exists
		//Fetch user from database (and throw exception if there was no match)
		User author = userRepository.findByCredentialsUsernameAndCredentialsPassword(tweetRequestDto.getCredentials().getUsername(), tweetRequestDto.getCredentials().getPassword());
		if (author == null) {
			throw new BadRequestException("No user could be found with matching credentials");
		}
		
		//2: create a new tweet object with given DTO
		Tweet replyTweet = tweetMapper.requestDtoToEntity(tweetRequestDto);
		//3: Make sure tweet content is NOT empty
		if (replyTweet.getContent() == null) {//If the reply tweet has empty content, throw error
			throw new BadRequestException("Reply tweets must have content, cannot be empty.");
		}
		//4a: establish replyTo relationship here
		replyTweet.setInReplyTo(originalTweet);
		//4b: etsablish author of tweet
		replyTweet.setAuthor(author);
		
		//5. Parse through content for hashtags and mentions
		
        //NOTE: If I had more time to refactor, I would put this block of code in a helper method in this service.
		//Sort out tweet's mentions and hashtags
        //Parse through content to fish out any @mentions
        Matcher matcher = Pattern.compile("@\\w+").matcher(replyTweet.getContent());
        while (matcher.find()) { //For every match we find for the given regex
            if (userRepository.existsByCredentialsUsername(matcher.group().replace("@", ""))) { //Check if mentioned user exists in the DB
                replyTweet.getUsers_mentions().add(userRepository.findByCredentialsUsername(matcher.group().replace("@", ""))); //If they do, set that here.
            }
        }

        //Now fish out #hashtags
        matcher = Pattern.compile("#\\w+").matcher(replyTweet.getContent());
        while (matcher.find()) { //For every match we find for the given regex
            String label = matcher.group().replace("#", ""); //Format label
            Hashtag hashtag;
            if (hashtagRepository.existsByLabel(label)) { //If given label is in our db (existing hashtag)
                hashtag = hashtagRepository.findByLabel(label); //Fetch the matched hashtag via label
                hashtag.setLastUsed(Timestamp.valueOf(LocalDateTime.now())); //update the haghtag's last used property
            } else {
                hashtag = hashtagService.createHashtag(label); //Create a new hashtag
            } 
            replyTweet.getHashtags().add(hashtag);//Add hashtag to our tweet's list of hashtags
        }
        
        //Save fully developed tweet to DB (this should also save all nested entities, such as the hashtags and user mentions)
  		tweetRepository.saveAndFlush(replyTweet);
  		return tweetMapper.entityToDto(replyTweet);
		
	}

	@Override
	public TweetResponseDto createRepostTweet(Long repostedTweetId, CredentialsDto reposterCredentials) {
		//Fetch/validate user from database (and throw exception if there was no match)
		User author = userRepository.findByCredentialsUsernameAndCredentialsPassword(reposterCredentials.getUsername(), reposterCredentials.getPassword());
		if (author == null) {
			throw new BadRequestException("No user could be found with matching credentials");
		}		
		
		//2. Ensure the tweet being reposted exists.
		Tweet originalTweet = tweetRepository.findById(repostedTweetId).get(); //NoSuchElementException will be thrown if tweet does not exist	
		//THEN, create repostOf relationship, as well as author relationship (NO CONTENT copying)
		Tweet repost = new Tweet();
		repost.setAuthor(author);
		repost.setRepostOf(originalTweet);
		
		// and save this new repost tweet into the db/return it 
		tweetRepository.saveAndFlush(repost);
		return tweetMapper.entityToDto(repost);
	}
}