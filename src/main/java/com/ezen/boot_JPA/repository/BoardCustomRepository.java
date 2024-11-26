package com.ezen.boot_JPA.repository;

import com.ezen.boot_JPA.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardCustomRepository {
    // type, keyword, pageable 값을 주고 Page<Board>를 반환받는 메서드 생성
    Page<Board> searchBoard(String sortType, String keyword, Pageable pageable);
}
