package com.ezen.boot_JPA.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileDTO {
    private String uuid;
    private String saveDir; // 저장되는 디렉토리 경로. 2024\11\15이런 식으로 생겼다. 프로그램 상 파일이 저장될 폴더를 저장 날짜에 기반하여 생성한다.
    private String fileName;
    private int fileType;
    private long bno;
    private long fileSize;
    private LocalDateTime registerTime;
    private LocalDateTime modifyTime;
}
