package com.cooksys.socialmedia.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.socialmedia.dtos.HashtagDto;
import com.cooksys.socialmedia.services.HashtagService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/tags")
@AllArgsConstructor
public class HashtagController {
	
	private HashtagService hashtagService;

	@GetMapping
	public List<HashtagDto> getAllHashTags() {
		return hashtagService.getAllHashTags();
	}
}
