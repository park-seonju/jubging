package com.ssafy.jupging.domain.repository;

import com.ssafy.jupging.domain.entity.JubgingLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JubgingLogRepository extends JpaRepository<JubgingLog, Long> {

    List<JubgingLog> findByUserId(Long userId);
}