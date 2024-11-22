package com.ezen.boot_JPA.entity;

import jakarta.persistence.*;
import lombok.*;

// Entity는 DB의 테이블을 나타내는 클래스들이다. 클래스의 이름이 곧 테이블의 이름이다.
// DTO: 객체를 생성하는 클래스다.
// 자주 쓰는 코드들을 base class로 별도로 관리한다.
// 테이블마다 자주 사용되는 register_date, modify_date, isDel 등을 관리할 수 있다.
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Board extends TimeBase { // TimeBase로부터 자주 쓰는 컬럼들을 상속받았다.

    @Id // Id로 어노테이트되면 기본키다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키를 어떻게 생성할 지 지정한다. MySQL의 AUTO_INCREMENT다.
    private long bno; // wrapper 클래스가 알아서 long을 Long으로 형변환 해준다.

    // 필드의 이름을 그대로 쓸 것이라면 @Column(name = "")을 지정할 필요가 없다.
    // 문자열은 varchar()에 사용할 길이를 지정해야 한다.
    // nullable은 DB 쿼리의 NOT NULL에 대응한다.
    @Column(length = 200, nullable = false)
    private String title;

    @Column(length = 200, nullable = false)
    private String writer;

    @Column(length = 2000, nullable = false)
    private String content;

    // 컬럼 생성 시 초기값 지정 = 객체가 생길 때 객체의 기본값 생성.
    // @Builder.Default
    // private int point = 0;
}
