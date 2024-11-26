package com.ezen.boot_JPA.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity // 클래스 이름대로 테이블을 생성한다.
@Table(name = "auth_user") // 테이블의 속성을 정하고 싶을 때 사용하는 어노테이션. 테이블의 이름 등을 설정할 수 있다.
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 200, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String auth;
}
