package com.forli.despacho.controller;

import com.forli.despacho.dto.ApiResponse;
import com.forli.despacho.dto.MetaDataDTO;
import com.forli.despacho.dto.PedidosAllDTO;
import com.forli.despacho.model.Pedido;
import com.forli.despacho.service.PedidoService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/meta")
    public ApiResponse<MetaDataDTO> getMetaData() {
        return ApiResponse.ok(pedidoService.getMetaData());
    }

    @GetMapping("/all")
    public ApiResponse<PedidosAllDTO> pedidosAll(
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) String estados,
            @RequestParam(required = false) String supervisores,
            @RequestParam(required = false) String fechaDesde,
            @RequestParam(required = false) String fechaHasta) {
        return ApiResponse.ok(pedidoService.pedidosAll(
            texto,
            parseCsv(estados),
            parseCsv(supervisores),
            fechaDesde,
            fechaHasta
        ));
    }

    @GetMapping
    public ApiResponse<List<Pedido>> search(
            @RequestParam String ubigeo,
            @RequestParam(required = false) String texto,
            @RequestParam(required = false) String estados,
            @RequestParam(required = false) String supervisores,
            @RequestParam(required = false) String fechaDesde,
            @RequestParam(required = false) String fechaHasta) {
        return ApiResponse.ok(pedidoService.search(
            ubigeo,
            texto,
            parseCsv(estados),
            parseCsv(supervisores),
            fechaDesde,
            fechaHasta
        ));
    }

    private List<String> parseCsv(String csv) {
        if (csv == null || csv.isEmpty()) return null;
        return Arrays.stream(csv.split(",")).map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }
}
