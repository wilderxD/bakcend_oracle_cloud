package com.forli.despacho.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_unico", nullable = false)
    private Integer idUnico;

    @Column(nullable = false, length = 50)
    private String pedido;

    @Column(length = 255)
    private String cliente;

    @Column(length = 255)
    private String producto;

    @Column(precision = 10, scale = 2)
    private BigDecimal cantidad = BigDecimal.ZERO;

    @Column(length = 100)
    private String supervisor;

    @Column(length = 50)
    private String estado;

    @Column(length = 20)
    private String ubigeo = "SIN UBIGEO";

    @Column(length = 255)
    private String agencia = "SIN AGENCIA ASIGNADA";

    @Column(columnDefinition = "TEXT")
    private String direccion;

    @Column(name = "codigo_venta", length = 50)
    private String codigoVenta;

    @Column(length = 100)
    private String linea;

    @Column(columnDefinition = "TEXT")
    private String observacion;

    @Column(length = 20)
    private String fecha;

    @Column(name = "fecha_iso")
    private LocalDate fechaIso;

    @Column(name = "fecha_entrega", length = 20)
    private String fechaEntrega;

    @Column(name = "fecha_entrega_iso")
    private LocalDate fechaEntregaIso;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Pedido() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getIdUnico() { return idUnico; }
    public void setIdUnico(Integer idUnico) { this.idUnico = idUnico; }

    public String getPedido() { return pedido; }
    public void setPedido(String pedido) { this.pedido = pedido; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }

    public BigDecimal getCantidad() { return cantidad; }
    public void setCantidad(BigDecimal cantidad) { this.cantidad = cantidad; }

    public String getSupervisor() { return supervisor; }
    public void setSupervisor(String supervisor) { this.supervisor = supervisor; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUbigeo() { return ubigeo; }
    public void setUbigeo(String ubigeo) { this.ubigeo = ubigeo; }

    public String getAgencia() { return agencia; }
    public void setAgencia(String agencia) { this.agencia = agencia; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCodigoVenta() { return codigoVenta; }
    public void setCodigoVenta(String codigoVenta) { this.codigoVenta = codigoVenta; }

    public String getLinea() { return linea; }
    public void setLinea(String linea) { this.linea = linea; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public LocalDate getFechaIso() { return fechaIso; }
    public void setFechaIso(LocalDate fechaIso) { this.fechaIso = fechaIso; }

    public String getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(String fechaEntrega) { this.fechaEntrega = fechaEntrega; }

    public LocalDate getFechaEntregaIso() { return fechaEntregaIso; }
    public void setFechaEntregaIso(LocalDate fechaEntregaIso) { this.fechaEntregaIso = fechaEntregaIso; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
