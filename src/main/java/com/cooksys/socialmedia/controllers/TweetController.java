package com.cooksys.socialmedia.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.socialmedia.services.TweetService;

@RestController
@RequestMapping("/tweets")
public class TweetController {
	private final TweetService tweetService;
	
	// @GetMapping
	
	// @PostMapping
	
	// @GetMapping("/{id}")
	
	// @DeleteMapping("/{id}"
	
	// POST    tweets/{id}/like
	
	// POST    tweets/{id}/reply
	
	// POST    tweets/{id}/repost
	
	// GET     tweets/{id}/tags
	
	// GET     tweets/{id}/likes
	
	// GET     tweets/{id}/context
	
	// GET     tweets/{id}/replies
	
	// GET     tweets/{id}/reposts
	
	// GET     tweets/{id}/mentions

}
