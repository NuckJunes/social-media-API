package com.cooksys.socialmedia.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.HashtagDto;
import com.cooksys.socialmedia.mappers.HashtagMapper;
import com.cooksys.socialmedia.repositories.HashtagRepository;
import com.cooksys.socialmedia.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService{
	
	private final HashtagRepository hashtagRepository;//Variables need to be "final" for @RequiredArgsConstructor to autowire dependencies
	private final HashtagMapper hashtagMapper;
	
	@Override
	public List<HashtagDto> getAllHashTags() {
		return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
	}

}
