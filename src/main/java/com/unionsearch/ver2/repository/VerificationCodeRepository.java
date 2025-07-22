package com.unionsearch.ver2.repository;

import com.unionsearch.ver2.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
  Optional<VerificationCode> findByEmail(String email);

  void deleteByEmail(String email);
}
