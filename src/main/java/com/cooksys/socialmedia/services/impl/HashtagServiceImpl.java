package com.cooksys.socialmedia.services.impl;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.mappers.HashtagMapper;
import com.cooksys.socialmedia.repositories.HashtagRepository;
import com.cooksys.socialmedia.services.HashtagService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HashtagServiceImpl implements HashtagService{
	
	private HashtagRepository hashtagRepository;
	private HashtagMapper hashtagMapper;

}
