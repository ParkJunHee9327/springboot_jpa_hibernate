package com.ezen.boot_JPA.repository;

import com.ezen.boot_JPA.entity.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    // 이메일로 AuthUser를 찾는 메서드이다. 관례적으로 findBy-로 명명한다.
    List<AuthUser> findByEmail(String email);
}
