package com.forli.despacho.service;

import com.forli.despacho.dto.MetaDataDTO;
import com.forli.despacho.dto.PedidosAllDTO;
import com.forli.despacho.model.Pedido;
import com.forli.despacho.repository.ChoferRepository;
import com.forli.despacho.repository.PedidoRepository;
import com.forli.despacho.repository.PlacaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepo;
    private final ChoferRepository choferRepo;
    private final PlacaRepository placaRepo;

    public PedidoService(PedidoRepository pedidoRepo, ChoferRepository choferRepo, PlacaRepository placaRepo) {
        this.pedidoRepo = pedidoRepo;
        this.choferRepo = choferRepo;
        this.placaRepo = placaRepo;
    }

    public MetaDataDTO getMetaData() {
        List<Pedido> all = pedidoRepo.findAll();

        Map<String, List<Pedido>> byUbigeo = all.stream()
            .collect(Collectors.groupingBy(Pedido::getUbigeo));

        Map<String, Integer> ubigeoCounts = new HashMap<>();
        byUbigeo.forEach((u, ps) -> {
            Set<String> uniquePedidos = ps.stream().map(Pedido::getPedido).collect(Collectors.toSet());
            ubigeoCounts.put(u, uniquePedidos.size());
        });

        MetaDataDTO dto = new MetaDataDTO();
        dto.setUbigeos(byUbigeo.keySet().stream().sorted().collect(Collectors.toList()));
        dto.setTotalPedidos(all.size());
        dto.setUbigeoCounts(ubigeoCounts);
        dto.setEstados(all.stream().map(Pedido::getEstado).filter(Objects::nonNull).filter(s -> !s.isEmpty()).distinct().sorted().collect(Collectors.toList()));
        dto.setSupervisores(all.stream().map(Pedido::getSupervisor).filter(Objects::nonNull).filter(s -> !s.isEmpty()).distinct().sorted().collect(Collectors.toList()));
        dto.setChoferes(choferRepo.findAllNombres());
        dto.setPlacas(placaRepo.findAllPlacas());
        return dto;
    }

    public PedidosAllDTO pedidosAll(String texto, List<String> estados, List<String> supervisores, String fechaDesde, String fechaHasta) {
        LocalDate desde = parseDate(fechaDesde);
        LocalDate hasta = parseDate(fechaHasta);

        List<Pedido> all = pedidoRepo.searchWithFilters(texto, estados, supervisores, desde, hasta);

        Map<String, List<Pedido>> byUbigeo = all.stream()
            .collect(Collectors.groupingBy(Pedido::getUbigeo));

        List<PedidosAllDTO.UbigeoInfo> ubigeos = byUbigeo.keySet().stream().sorted().map(u -> {
            Set<String> uniquePedidos = byUbigeo.get(u).stream().map(Pedido::getPedido).collect(Collectors.toSet());
            return new PedidosAllDTO.UbigeoInfo(u, uniquePedidos.size());
        }).collect(Collectors.toList());

        return new PedidosAllDTO(ubigeos, byUbigeo);
    }

    public List<Pedido> search(String ubigeo, String texto, List<String> estados, List<String> supervisores, String fechaDesde, String fechaHasta) {
        LocalDate desde = parseDate(fechaDesde);
        LocalDate hasta = parseDate(fechaHasta);

        List<Pedido> all = pedidoRepo.searchWithFilters(texto, estados, supervisores, desde, hasta);
        return all.stream().filter(p -> p.getUbigeo().equals(ubigeo)).collect(Collectors.toList());
    }

    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }
}
