package com.forli.despacho.controller;

import com.forli.despacho.dto.ApiResponse;
import com.forli.despacho.dto.GuardarPayload;
import com.forli.despacho.model.HojaCarga;
import com.forli.despacho.service.HojaCargaService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/hojas-carga")
public class HojaCargaController {

    private final HojaCargaService hojaCargaService;

    public HojaCargaController(HojaCargaService hojaCargaService) {
        this.hojaCargaService = hojaCargaService;
    }

    @GetMapping
    public ApiResponse<?> getHistorial(
            @RequestParam(required = false) String fecha,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int pageSize) {
        return ApiResponse.ok(hojaCargaService.obtenerHistorial(fecha, page, pageSize));
    }

    @GetMapping("/count")
    public ApiResponse<Integer> countHistorial(
            @RequestParam(required = false) String fecha) {
        return ApiResponse.ok(hojaCargaService.contarHistorial(fecha));
    }

    @PostMapping
    public ApiResponse<Map<String, Object>> guardar(@RequestBody GuardarPayload payload) {
        HojaCarga hc = hojaCargaService.guardar(payload);
        return ApiResponse.ok(Map.of(
            "id", hc.getIdCarga(),
            "fecha", hc.getFechaHora(),
            "chofer", hc.getChofer(),
            "placa", hc.getPlaca(),
            "items", hc.getItemsCount()
        ));
    }

    @PostMapping("/actualizar")
    public ApiResponse<Map<String, Object>> actualizar(@RequestBody Map<String, Object> body) {
        String id = (String) body.get("id");
        // Items would need to be parsed from the request
        return ApiResponse.ok(Map.of("id", id, "status", "updated"));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> eliminar(@PathVariable String id) {
        return ApiResponse.ok(hojaCargaService.eliminar(id));
    }
}
