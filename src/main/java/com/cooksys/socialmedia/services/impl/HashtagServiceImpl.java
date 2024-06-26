package com.cooksys.socialmedia.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cooksys.socialmedia.dtos.HashtagDto;
import com.cooksys.socialmedia.mappers.HashtagMapper;
import com.cooksys.socialmedia.repositories.HashtagRepository;
import com.cooksys.socialmedia.services.HashtagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService{
	
	private HashtagRepository hashtagRepository;
	private HashtagMapper hashtagMapper;
	
	@Override
	public List<HashtagDto> getAllHashTags() {
		return hashtagMapper.entitiesToDtos(hashtagRepository.findAllByDeletedFalse());
	}

}
