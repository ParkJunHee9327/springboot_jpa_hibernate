package com.ezen.boot_JPA.repository;

import com.ezen.boot_JPA.dto.CommentDTO;
import com.ezen.boot_JPA.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByBno(long bno); // bno를 기준으로 찾는 경우를 위한 메서드다.
}
