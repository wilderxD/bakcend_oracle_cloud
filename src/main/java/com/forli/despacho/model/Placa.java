package com.forli.despacho.model;

import jakarta.persistence.*;

@Entity
@Table(name = "placas")
public class Placa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String placa;

    public Placa() {}

    public Placa(String placa) {
        this.placa = placa;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
}
