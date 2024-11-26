package com.ezen.boot_JPA.security;

import com.ezen.boot_JPA.dto.UserDTO;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Getter
// 커스텀 엔티티 클래스인 User가 아니라 org.springframework.security.core.userdetails의 User다.
public class AuthMember extends User {

    private UserDTO userDTO;

    public AuthMember(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public AuthMember(UserDTO userDTO) {
        super(userDTO.getEmail(), userDTO.getPwd(),
                userDTO.getAuthList()
                        .stream()
                        .map(auth -> new SimpleGrantedAuthority(auth.getAuth()))
                        .collect(Collectors.toList()));
        // 이렇게 줘야 나중에 값을 빼서 쓰기 편하다.
        this.userDTO = userDTO;
    }
}
