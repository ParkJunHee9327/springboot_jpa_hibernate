package com.ezen.boot_JPA.service;

import com.ezen.boot_JPA.dto.AuthUserDTO;
import com.ezen.boot_JPA.dto.UserDTO;
import com.ezen.boot_JPA.entity.AuthUser;
import com.ezen.boot_JPA.entity.User;

import java.util.List;

public interface UserService {

    default User convertDtoToEntity(UserDTO userDTO) {
        return User.builder()
                .email(userDTO.getEmail())
                .pwd(userDTO.getPwd())
                .nickName(userDTO.getNickName())
                .lastLogin(userDTO.getLastLogin())
                .build();
    }

    default UserDTO convertEntityToDto(User user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .pwd(user.getPwd())
                .nickName(user.getNickName())
                .lastLogin(user.getLastLogin())
                .build();
    }

    default UserDTO convertEntityAndAuthToDto(User user, List<AuthUserDTO> authUserDTOList) {
        return UserDTO.builder()
                .email(user.getEmail())
                .pwd(user.getPwd())
                .nickName(user.getNickName())
                .lastLogin(user.getLastLogin())
                .registerTime(user.getRegisterTime())
                .modifyTime(user.getModifyTime())
                .authList(authUserDTOList)
                .build();
    }

    default AuthUser convertDtoToEntity(AuthUserDTO authUserDTO) {
        return AuthUser.builder()
                .email(authUserDTO.getEmail())
                .auth(authUserDTO.getAuth())
                .build();
    }

    default AuthUserDTO convertEntityToDto(AuthUser authUser) {
        return AuthUserDTO.builder()
                .email(authUser.getEmail())
                .auth(authUser.getAuth())
                .build();
    }

    // email을 가지고 UserDTO를 authUser로 변환하는 converter이다.
    default AuthUser convertDtoToAuthEntity(UserDTO userDTO) {
        return null;
    }

    String join(UserDTO userDTO);

    UserDTO selectEmail(String username);

    boolean updateLastLogin(String authEmail);

    List<UserDTO> getAllUser();

    List<AuthUserDTO> getUserAuth(String email);
}
