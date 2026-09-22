package com.forli.despacho.dto;

import java.util.List;
import java.util.Map;

public class MetaDataDTO {
    private List<String> ubigeos;
    private int totalPedidos;
    private Map<String, Integer> ubigeoCounts;
    private List<String> estados;
    private List<String> supervisores;
    private List<String> choferes;
    private List<String> placas;

    public MetaDataDTO() {}

    public List<String> getUbigeos() { return ubigeos; }
    public void setUbigeos(List<String> ubigeos) { this.ubigeos = ubigeos; }

    public int getTotalPedidos() { return totalPedidos; }
    public void setTotalPedidos(int totalPedidos) { this.totalPedidos = totalPedidos; }

    public Map<String, Integer> getUbigeoCounts() { return ubigeoCounts; }
    public void setUbigeoCounts(Map<String, Integer> ubigeoCounts) { this.ubigeoCounts = ubigeoCounts; }

    public List<String> getEstados() { return estados; }
    public void setEstados(List<String> estados) { this.estados = estados; }

    public List<String> getSupervisores() { return supervisores; }
    public void setSupervisores(List<String> supervisores) { this.supervisores = supervisores; }

    public List<String> getChoferes() { return choferes; }
    public void setChoferes(List<String> choferes) { this.choferes = choferes; }

    public List<String> getPlacas() { return placas; }
    public void setPlacas(List<String> placas) { this.placas = placas; }
}
