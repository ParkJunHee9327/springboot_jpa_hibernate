package com.ezen.boot_JPA.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String email;
    private String pwd;
    private String nickName;
    private LocalDateTime lastLogin;
    private LocalDateTime registerTime, modifyTime;
    private List<AuthUserDTO> authList;
}
