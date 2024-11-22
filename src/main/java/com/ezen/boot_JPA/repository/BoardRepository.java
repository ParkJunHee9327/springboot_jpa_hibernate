package com.ezen.boot_JPA.repository;

import com.ezen.boot_JPA.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

// JPA는 @Mapper를 사용하지 않는다.
// JpaRepository<T, Id>의 구조이다. <Entity name, Id type>을 받는다.
// JpaRepository가 제네릭이라 long을 넣고 싶으면 Long으로 넣어야 한다. 이 자료형은 Entity와 DTO가 모두 일치해야 한다.
// 따라서 Board와 BoardDTO의 기본키의 자료형이 long이거나 Long이어야 한다. wrapper 클래스의 autoboxing 기능 덕분에 long, Long 모두 가능하지만
// 기본키들의 자료형에 통일성이 있어야 한다. Board의 기본키는 long이고 BoardDTO의 기본키는 Long이고 이러면 안 된다.
public interface BoardRepository extends JpaRepository<Board, Long> {
}
