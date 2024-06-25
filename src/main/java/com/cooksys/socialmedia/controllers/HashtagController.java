package com.cooksys.socialmedia.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.socialmedia.services.HashtagService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
public class HashtagController {
	
	private HashtagService hashtagService;

}
