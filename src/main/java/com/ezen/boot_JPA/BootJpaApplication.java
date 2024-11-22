package com.ezen.boot_JPA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing // JPA를 사용하겠다고 설정하는 어노테이션. 이게 있어야 JPA를 인식한다.
@SpringBootApplication
public class BootJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootJpaApplication.class, args);
	}

}
