package com.unionsearch.ver2.repository;

import com.unionsearch.ver2.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
  // 기본적으로 findAll(), findById() 등의 JPA 메서드를 상속받습니다.
}
