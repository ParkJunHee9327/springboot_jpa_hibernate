package com.ezen.boot_JPA.repository;

import com.ezen.boot_JPA.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

// JPA는 @Mapper를 사용하지 않는다.
// JpaRepository<T, Id>의 구조이다. 제네릭에 테이블과 기본키를 받는다.
// JpaRepository가 제네릭이라 long을 넣고 싶으면 Long으로 넣어야 한다. 이 자료형은 Entity와 DTO가 모두 일치해야 한다.
// 따라서 Board와 BoardDTO의 기본키의 자료형이 모두 long이 아닌 Long이어야 한다.
public interface BoardRepository extends JpaRepository<Board, Long> {
}
