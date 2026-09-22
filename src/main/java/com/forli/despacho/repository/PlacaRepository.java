package com.forli.despacho.repository;

import com.forli.despacho.model.Placa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlacaRepository extends JpaRepository<Placa, Long> {

    @Query("SELECT p.placa FROM Placa p ORDER BY p.placa")
    List<String> findAllPlacas();
}
