package com.springboot.emergencytextback.repository;

import com.springboot.emergencytextback.entity.Emergency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmergencyRepository extends JpaRepository<Emergency, Long> {
    // 필요에 따라 커스텀 쿼리 추가 가능
}

