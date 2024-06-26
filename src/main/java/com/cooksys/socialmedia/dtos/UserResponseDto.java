package com.cooksys.socialmedia.dtos;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    @NonNull
    private String username;

    @NonNull
    private ProfileDto profileDto;
    
    @NonNull
    private Timestamp joined;

}
