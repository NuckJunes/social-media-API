package com.cooksys.socialmedia.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetRequestDto {
	
	private UserResponseDto userResponseDto;
	private LocalDateTime posted;
	private String content;
//	private TweetResponseDto inReplyTo;
//	private TweetResponseDto repostOf;
}
