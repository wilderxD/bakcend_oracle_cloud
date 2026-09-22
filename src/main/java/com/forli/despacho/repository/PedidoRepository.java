package com.forli.despacho.repository;

import com.forli.despacho.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUbigeo(String ubigeo);

    @Query("SELECT DISTINCT p.ubigeo FROM Pedido p ORDER BY p.ubigeo")
    List<String> findDistinctUbigeos();

    @Query("SELECT DISTINCT p.estado FROM Pedido p WHERE p.estado IS NOT NULL ORDER BY p.estado")
    List<String> findDistinctEstados();

    @Query("SELECT DISTINCT p.supervisor FROM Pedido p WHERE p.supervisor IS NOT NULL ORDER BY p.supervisor")
    List<String> findDistinctSupervisores();

    @Query("SELECT p FROM Pedido p WHERE " +
           "(:texto IS NULL OR :texto = '' OR LOWER(p.cliente) LIKE LOWER(CONCAT('%',:texto,'%')) " +
           "OR LOWER(p.pedido) LIKE LOWER(CONCAT('%',:texto,'%')) " +
           "OR LOWER(p.producto) LIKE LOWER(CONCAT('%',:texto,'%'))) " +
           "AND (:estados IS NULL OR p.estado IN :estados) " +
           "AND (:supervisores IS NULL OR p.supervisor IN :supervisores) " +
           "AND (:fechaDesde IS NULL OR p.fechaIso >= :fechaDesde) " +
           "AND (:fechaHasta IS NULL OR p.fechaIso <= :fechaHasta)")
    List<Pedido> searchWithFilters(
        @Param("texto") String texto,
        @Param("estados") List<String> estados,
        @Param("supervisores") List<String> supervisores,
        @Param("fechaDesde") LocalDate fechaDesde,
        @Param("fechaHasta") LocalDate fechaHasta
    );

    @Query("SELECT COUNT(DISTINCT p.pedido) FROM Pedido p WHERE p.ubigeo = :ubigeo")
    int countDistinctPedidosByUbigeo(@Param("ubigeo") String ubigeo);

    @Query("SELECT COUNT(DISTINCT p.pedido) FROM Pedido p WHERE p.ubigeo = :ubigeo " +
           "AND (:texto IS NULL OR :texto = '' OR LOWER(p.cliente) LIKE LOWER(CONCAT('%',:texto,'%')) " +
           "OR LOWER(p.pedido) LIKE LOWER(CONCAT('%',:texto,'%')) " +
           "OR LOWER(p.producto) LIKE LOWER(CONCAT('%',:texto,'%'))) " +
           "AND (:estados IS NULL OR p.estado IN :estados) " +
           "AND (:supervisores IS NULL OR p.supervisor IN :supervisores) " +
           "AND (:fechaDesde IS NULL OR p.fechaIso >= :fechaDesde) " +
           "AND (:fechaHasta IS NULL OR p.fechaIso <= :fechaHasta)")
    int countDistinctPedidosByUbigeoWithFilters(
        @Param("ubigeo") String ubigeo,
        @Param("texto") String texto,
        @Param("estados") List<String> estados,
        @Param("supervisores") List<String> supervisores,
        @Param("fechaDesde") LocalDate fechaDesde,
        @Param("fechaHasta") LocalDate fechaHasta
    );
}
