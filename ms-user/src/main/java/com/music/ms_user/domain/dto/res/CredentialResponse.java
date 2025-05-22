package com.music.ms_user.domain.dto.res;


import com.music.ms_user.event.OtpRequestEvent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CredentialResponse {
    private boolean flag;
    private String message;
    private OtpRequestEvent otpRequestEvent;
}
