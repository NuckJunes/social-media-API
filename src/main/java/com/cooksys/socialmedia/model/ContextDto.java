package com.cooksys.socialmedia.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ContextDto {

	private Tweet target;
	
	private Tweet before;
	
	private Tweet after;
}
