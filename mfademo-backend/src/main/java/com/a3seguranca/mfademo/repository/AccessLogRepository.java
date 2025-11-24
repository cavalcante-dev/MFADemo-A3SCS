package com.a3seguranca.mfademo.repository;

import com.a3seguranca.mfademo.entity.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
    List<AccessLog> findByUsernameOrderByTimestampDesc(String username);
    List<AccessLog> findAllByOrderByTimestampDesc();
}
