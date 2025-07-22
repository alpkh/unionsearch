package com.unionsearch.ver2.repository;

import com.unionsearch.ver2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // 이메일로 사용자를 찾기 위한 메서드
    Optional<User> findByEmail(String email);

    // 이메일 존재 여부 확인을 위한 메서드
    boolean existsByEmail(String email);
}
