package com.forli.despacho.service;

import com.forli.despacho.dto.MetaDataDTO;
import com.forli.despacho.repository.ChoferRepository;
import com.forli.despacho.repository.PedidoRepository;
import com.forli.despacho.repository.PlacaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MetaService {

    private final PedidoRepository pedidoRepo;
    private final ChoferRepository choferRepo;
    private final PlacaRepository placaRepo;

    public MetaService(PedidoRepository pedidoRepo, ChoferRepository choferRepo, PlacaRepository placaRepo) {
        this.pedidoRepo = pedidoRepo;
        this.choferRepo = choferRepo;
        this.placaRepo = placaRepo;
    }

    public MetaDataDTO getMetaData() {
        List<String> ubigeos = pedidoRepo.findDistinctUbigeos();
        List<String> estados = pedidoRepo.findDistinctEstados();
        List<String> supervisores = pedidoRepo.findDistinctSupervisores();
        List<String> choferes = choferRepo.findAllNombres();
        List<String> placas = placaRepo.findAllPlacas();

        Map<String, Integer> ubigeoCounts = new HashMap<>();
        for (String u : ubigeos) {
            ubigeoCounts.put(u, pedidoRepo.countDistinctPedidosByUbigeo(u));
        }

        MetaDataDTO dto = new MetaDataDTO();
        dto.setUbigeos(ubigeos);
        dto.setTotalPedidos(pedidoRepo.findAll().size());
        dto.setUbigeoCounts(ubigeoCounts);
        dto.setEstados(estados);
        dto.setSupervisores(supervisores);
        dto.setChoferes(choferes);
        dto.setPlacas(placas);
        return dto;
    }
}
