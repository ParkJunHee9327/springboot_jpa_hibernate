package com.ezen.boot_JPA.security;

import com.ezen.boot_JPA.dto.UserDTO;
import com.ezen.boot_JPA.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
public class CustomUserService implements UserDetailsService {

    @Autowired
    public UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username에 해당하는 값이 DB에 있는지 확인한다. = user 테이블에 username으로써 들어온 이메일이 존재하는지 확인한다.
        UserDTO userDTO = userService.selectEmail(username);
        log.info(" > Loginned UserDTO: {}", userDTO);
        if(userDTO == null) {
            throw new UsernameNotFoundException(username);
        }

        return new AuthMember(userDTO);
    }
}
