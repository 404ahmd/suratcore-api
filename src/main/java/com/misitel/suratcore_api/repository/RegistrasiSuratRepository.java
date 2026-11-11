package com.misitel.suratcore_api.repository;

import com.misitel.suratcore_api.entity.RegistrasiSurat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrasiSuratRepository extends JpaRepository<RegistrasiSurat, Long> {

    Optional<RegistrasiSurat> findByNomorSurat(String nomorSurat);

    boolean existsByNomorSurat(String nomorSurat);

    List<RegistrasiSurat> findByUserId(Long userId);

    List<RegistrasiSurat> findByDivisionId(Long divisionId);

    List<RegistrasiSurat> findByStatus(String status);
}

