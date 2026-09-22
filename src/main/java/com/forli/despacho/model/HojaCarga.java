package com.forli.despacho.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "hojas_carga")
public class HojaCarga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_carga", nullable = false, unique = true, length = 50)
    private String idCarga;

    @Column(name = "fecha_hora", length = 30)
    private String fechaHora;

    @Column(name = "fecha_filtro")
    private LocalDate fechaFiltro;

    @Column(length = 100)
    private String chofer;

    @Column(length = 20)
    private String placa;

    @Column(name = "items_count")
    private Integer itemsCount = 0;

    private Integer espumas = 0;

    private Integer resortes = 0;

    @Column(name = "data_json", columnDefinition = "TEXT")
    private String dataJson;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public HojaCarga() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIdCarga() { return idCarga; }
    public void setIdCarga(String idCarga) { this.idCarga = idCarga; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    public LocalDate getFechaFiltro() { return fechaFiltro; }
    public void setFechaFiltro(LocalDate fechaFiltro) { this.fechaFiltro = fechaFiltro; }

    public String getChofer() { return chofer; }
    public void setChofer(String chofer) { this.chofer = chofer; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public Integer getItemsCount() { return itemsCount; }
    public void setItemsCount(Integer itemsCount) { this.itemsCount = itemsCount; }

    public Integer getEspumas() { return espumas; }
    public void setEspumas(Integer espumas) { this.espumas = espumas; }

    public Integer getResortes() { return resortes; }
    public void setResortes(Integer resortes) { this.resortes = resortes; }

    public String getDataJson() { return dataJson; }
    public void setDataJson(String dataJson) { this.dataJson = dataJson; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
