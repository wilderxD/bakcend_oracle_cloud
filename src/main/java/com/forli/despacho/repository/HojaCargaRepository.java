package com.forli.despacho.repository;

import com.forli.despacho.model.HojaCarga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface HojaCargaRepository extends JpaRepository<HojaCarga, Long> {

    Optional<HojaCarga> findByIdCarga(String idCarga);

    void deleteByIdCarga(String idCarga);

    @Query("SELECT h FROM HojaCarga h ORDER BY h.id DESC")
    List<HojaCarga> findAllOrdered();

    @Query("SELECT h FROM HojaCarga h WHERE h.fechaFiltro = :fecha ORDER BY h.id DESC")
    List<HojaCarga> findByFechaFiltro(@Param("fecha") LocalDate fecha);

    @Query("SELECT COUNT(h) FROM HojaCarga h")
    int countAll();

    @Query("SELECT COUNT(h) FROM HojaCarga h WHERE h.fechaFiltro = :fecha")
    int countByFechaFiltro(@Param("fecha") LocalDate fecha);
}
