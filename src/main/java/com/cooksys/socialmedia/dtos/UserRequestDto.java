package com.cooksys.socialmedia.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    @NonNull
    private CredentialsDto credentials;
    
    @NonNull
    private ProfileDto profile;
}
