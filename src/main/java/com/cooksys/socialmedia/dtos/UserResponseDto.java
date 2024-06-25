package com.cooksys.socialmedia.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    @NonNull
    private String username;

    @NonNull
    private ProfileDto profileDto;
    
    @NonNull
    private LocalDateTime joined;

}
