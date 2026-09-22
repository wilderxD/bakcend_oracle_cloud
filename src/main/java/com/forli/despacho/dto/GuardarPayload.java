package com.forli.despacho.dto;

import com.forli.despacho.model.Pedido;
import java.util.List;

public class GuardarPayload {
    private String chofer;
    private String placa;
    private List<Pedido> items;

    public GuardarPayload() {}

    public String getChofer() { return chofer; }
    public void setChofer(String chofer) { this.chofer = chofer; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public List<Pedido> getItems() { return items; }
    public void setItems(List<Pedido> items) { this.items = items; }
}
