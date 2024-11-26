package com.ezen.boot_JPA.controller;

import com.ezen.boot_JPA.dto.UserDTO;
import com.ezen.boot_JPA.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RequestMapping("/user/*")
@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/join")
    public void join() {

    }

    @PostMapping("/join")
    public String join(UserDTO userDTO) {
        log.info(" > UserController join operating. userDTO: {}", userDTO);
        // 비밀번호 암호화
        userDTO.setPwd(passwordEncoder.encode(userDTO.getPwd()));
        String email = userService.join(userDTO);
        return (email == null ? "/user/join" : "index");
    }

    @GetMapping("/login")
    public void login(@RequestParam(value = "error", required = false) String error,
                      @RequestParam(value = "exception", required = false) String exception,
                      Model model) {
        // 에러 발생 시 에러와 예외 값을 담아서 화면에 전달함.
        model.addAttribute("error", error);
        model.addAttribute("exception", exception);
    }

    @GetMapping("/list")
    public void getAllUser(Model model) {
        List<UserDTO> userDTOList = userService.getAllUser();
        for (UserDTO user : userDTOList) {
            user.setAuthList(userService.getUserAuth(user.getEmail()));
        }
        model.addAttribute("userList", userDTOList);
    }

}
