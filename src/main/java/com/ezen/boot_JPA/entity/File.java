package com.ezen.boot_JPA.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File extends TimeBase {
    @Id
    // 내가 값을 부여할 예정이라 @GeneratedValue가 필요 없다.
    private String uuid;

    // @Column 어노테이션이 없으면 DB에 Long형으로 변수 이름대로 들어간다.
    private long bno;

    @Column(name = "save_dir", nullable = false) // String은 DB에 기본적으로 varchar로 들어간다.
    private String saveDir; // 저장되는 디렉토리 경로. 2024\11\15이런 식으로 생겼다. 프로그램 상 파일이 저장될 폴더를 저장 날짜에 기반하여 생성한다.

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type", nullable = false, columnDefinition = "integer default 0")
    private int fileType;

    @Column(name = "file_size")
    private long fileSize;
}
