package com.cooksys.socialmedia.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetRequestDto {
	
	private String content;
	
	private CredentialsDto credentials;
}
