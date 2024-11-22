package com.ezen.boot_JPA.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentDTO {
    private long cno; // wrapper 클래스의 uncboxing, autoboxing 기능이 있어서 Long이 아닌 long으로 해도 된다.
    private long bno;
    private String writer;
    private String content;
    private LocalDateTime registerTime;
    private LocalDateTime modifyTime;
}
