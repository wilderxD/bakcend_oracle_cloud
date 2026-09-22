package com.forli.despacho.dto;

import com.forli.despacho.model.Pedido;
import java.util.List;
import java.util.Map;

public class PedidosAllDTO {
    private List<UbigeoInfo> ubigeos;
    private Map<String, List<Pedido>> byUbigeo;

    public PedidosAllDTO() {}

    public PedidosAllDTO(List<UbigeoInfo> ubigeos, Map<String, List<Pedido>> byUbigeo) {
        this.ubigeos = ubigeos;
        this.byUbigeo = byUbigeo;
    }

    public List<UbigeoInfo> getUbigeos() { return ubigeos; }
    public void setUbigeos(List<UbigeoInfo> ubigeos) { this.ubigeos = ubigeos; }

    public Map<String, List<Pedido>> getByUbigeo() { return byUbigeo; }
    public void setByUbigeo(Map<String, List<Pedido>> byUbigeo) { this.byUbigeo = byUbigeo; }

    public static class UbigeoInfo {
        private String ubigeo;
        private int total;

        public UbigeoInfo(String ubigeo, int total) {
            this.ubigeo = ubigeo;
            this.total = total;
        }

        public String getUbigeo() { return ubigeo; }
        public void setUbigeo(String ubigeo) { this.ubigeo = ubigeo; }

        public int getTotal() { return total; }
        public void setTotal(int total) { this.total = total; }
    }
}
