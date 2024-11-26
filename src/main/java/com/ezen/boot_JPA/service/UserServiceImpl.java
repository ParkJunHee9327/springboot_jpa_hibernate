package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.AuthUserDTO;
import com.ezen.boot_JPA.dto.UserDTO;
import com.ezen.boot_JPA.entity.AuthUser;
import com.ezen.boot_JPA.entity.User;
import com.ezen.boot_JPA.repository.AuthUserRepository;
import com.ezen.boot_JPA.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthUserRepository authUserRepository;

    @Override
    public String join(UserDTO userDTO) {
        String email = userRepository.save(convertDtoToEntity(userDTO)).getEmail();
        if(email != null) {
            authUserRepository.save(convertDtoToAuthEntity(userDTO));
        }
        return email;
    }

    @Transactional
    @Override
    public UserDTO selectEmail(String username) {
        Optional<User> userOptional = userRepository.findById(username);
        // AuthRepository에 email로 권한을 찾는 메서드를 정의했다.
        List<AuthUser> authUserList = authUserRepository.findByEmail(username);
        if(userOptional.isPresent()) {
            UserDTO userDTO = convertEntityAndAuthToDto(userOptional.get(),
                    authUserList
                            .stream()
                            .map(auth -> convertEntityToDto(auth))
                            .toList()
            );
            return userDTO;
        }
        return null;
    }

    @Override
    public boolean updateLastLogin(String authEmail) {
        Optional<User> optionalUser = userRepository.findById(authEmail);
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setLastLogin(LocalDateTime.now());
            String email = userRepository.save(user).getEmail();
            return email == null ? false : true;
        }
        return false;
    }

    @Override
    public List<UserDTO> getAllUser() {
        List<UserDTO> userList = userRepository.findAll()
                .stream().map(user -> convertEntityToDto(user)).toList();
        return userList;
    }

    @Override
    public List<AuthUserDTO> getUserAuth(String email) {
        List<AuthUserDTO> userAuth = authUserRepository.findByEmail(email)
                .stream().map(auth -> convertEntityToDto(auth)).toList();
        return userAuth;
    }

    // findAll: 조건 없이 모두 가져온다.
}
