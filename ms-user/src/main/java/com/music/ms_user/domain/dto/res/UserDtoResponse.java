package com.music.ms_user.domain.dto.res;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDtoResponse {
    private String name;
    private String email;
    private Date dateOfBirth;
    private byte[] profileImage;
    private boolean locked;
    private Integer attemptedCount;
}
