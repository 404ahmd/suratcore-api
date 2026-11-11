package com.misitel.suratcore_api.repository;

import com.misitel.suratcore_api.entity.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DivisionRepository extends JpaRepository<Division, Long> {

    Optional<Division> findByKodeDivisi(String kodeDivisi);

    boolean existsByKodeDivisi(String kodeDivisi);
}

