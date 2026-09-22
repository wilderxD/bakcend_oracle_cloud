package com.forli.despacho.repository;

import com.forli.despacho.model.Chofer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChoferRepository extends JpaRepository<Chofer, Long> {

    @Query("SELECT c.nombre FROM Chofer c ORDER BY c.nombre")
    List<String> findAllNombres();
}
