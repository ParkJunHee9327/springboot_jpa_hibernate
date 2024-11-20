package com.ezen.boot_JPA.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 자주 쓰는 컬럼들을 관리하는 base class이다. 자주 쓰는 시간 관련 컬럼을 관리해서 TimeBase라고 이름 붙였다.
// 즉 등록일, 수정일만 따로 빼서 관리하는 슈퍼 테이블이다.
@MappedSuperclass // Mapper의 superclass임을 나타낸다. 모든 테이블들이 TimeBase를 상속받을 수 있다(?).
@EntityListeners(value = {AuditingEntityListener.class}) // ? 필수로 꼭 지정해야 한다고 한다. 근데 얘가 뭐하는 애임?
@Getter // registerTime과 modifyTime은 보통 now()로 설정되기에 Setter는 필요 없다.
public class TimeBase {

    @CreatedDate // 생성된 일자를 의미한다.
    @Column(name = "register_time", updatable = false)
    private LocalDateTime registerTime;

    @LastModifiedDate // 마지막으로 수정된 일자를 의미한다.
    @Column(name = "modify_time")
    private LocalDateTime modifyTime;
}
