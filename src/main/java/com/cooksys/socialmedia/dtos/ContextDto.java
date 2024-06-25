package com.cooksys.socialmedia.dtos;

import com.cooksys.socialmedia.entities.Tweet;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Data
public class ContextDto {

	@NonNull
	private Tweet target;
	
	@NonNull
	private Tweet before;
	
	@NonNull
	private Tweet after;
}
