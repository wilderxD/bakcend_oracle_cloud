package com.forli.despacho.service;

import com.forli.despacho.dto.GuardarPayload;
import com.forli.despacho.model.HojaCarga;
import com.forli.despacho.repository.HojaCargaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HojaCargaService {

    private final HojaCargaRepository repo;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final DateTimeFormatter ID_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd-HHmm");
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public HojaCargaService(HojaCargaRepository repo) {
        this.repo = repo;
    }

    public HojaCarga guardar(GuardarPayload payload) {
        if (payload.getChofer() == null || payload.getChofer().trim().isEmpty()) {
            throw new IllegalArgumentException("Chofer requerido");
        }
        if (payload.getPlaca() == null || payload.getPlaca().trim().isEmpty()) {
            throw new IllegalArgumentException("Placa requerida");
        }
        if (payload.getItems() == null || payload.getItems().isEmpty()) {
            throw new IllegalArgumentException("Se requiere al menos un item");
        }

        LocalDateTime now = LocalDateTime.now();
        String idCarga = "C-" + now.format(ID_FORMAT);
        String fechaStr = now.format(DISPLAY_FORMAT);

        int espumas = (int) payload.getItems().stream()
            .filter(i -> i.getLinea() != null && i.getLinea().contains("ESPUMA"))
            .mapToLong(i -> i.getCantidad() != null ? i.getCantidad().longValue() : 0)
            .sum();

        int resortes = (int) payload.getItems().stream()
            .filter(i -> i.getLinea() != null && i.getLinea().contains("RESORTE"))
            .mapToLong(i -> i.getCantidad() != null ? i.getCantidad().longValue() : 0)
            .sum();

        HojaCarga hc = new HojaCarga();
        hc.setIdCarga(idCarga);
        hc.setFechaHora(fechaStr);
        hc.setFechaFiltro(now.toLocalDate());
        hc.setChofer(payload.getChofer());
        hc.setPlaca(payload.getPlaca());
        hc.setItemsCount(payload.getItems().size());
        hc.setEspumas(espumas);
        hc.setResortes(resortes);
        try {
            hc.setDataJson(objectMapper.writeValueAsString(payload.getItems()));
        } catch (Exception e) {
            throw new RuntimeException("Error serializando items", e);
        }

        return repo.save(hc);
    }

    public HojaCarga actualizar(String idCarga, List<com.forli.despacho.model.Pedido> items) {
        if (idCarga == null || idCarga.isEmpty()) {
            throw new IllegalArgumentException("id requerido");
        }
        if (items == null) {
            throw new IllegalArgumentException("items requeridos");
        }

        HojaCarga hc = repo.findByIdCarga(idCarga)
            .orElseThrow(() -> new NoSuchElementException("ID no encontrado: " + idCarga));

        LocalDateTime now = LocalDateTime.now();
        hc.setFechaHora(now.format(DISPLAY_FORMAT));
        hc.setItemsCount(items.size());

        int espumas = (int) items.stream()
            .filter(i -> i.getLinea() != null && i.getLinea().contains("ESPUMA"))
            .mapToLong(i -> i.getCantidad() != null ? i.getCantidad().longValue() : 0)
            .sum();

        int resortes = (int) items.stream()
            .filter(i -> i.getLinea() != null && i.getLinea().contains("RESORTE"))
            .mapToLong(i -> i.getCantidad() != null ? i.getCantidad().longValue() : 0)
            .sum();

        hc.setEspumas(espumas);
        hc.setResortes(resortes);
        try {
            hc.setDataJson(objectMapper.writeValueAsString(items));
        } catch (Exception e) {
            throw new RuntimeException("Error serializando items", e);
        }

        return repo.save(hc);
    }

    public boolean eliminar(String idCarga) {
        if (idCarga == null || idCarga.isEmpty()) {
            throw new IllegalArgumentException("id requerido");
        }
        if (repo.findByIdCarga(idCarga).isEmpty()) {
            throw new NoSuchElementException("ID no encontrado");
        }
        repo.deleteByIdCarga(idCarga);
        return true;
    }

    public List<Map<String, Object>> obtenerHistorial(String fecha, int page, int pageSize) {
        List<HojaCarga> all;
        if (fecha != null && !fecha.isEmpty()) {
            LocalDate fechaFiltro = parseDate(fecha);
            all = fechaFiltro != null ? repo.findByFechaFiltro(fechaFiltro) : repo.findAllOrdered();
        } else {
            all = repo.findAllOrdered();
        }

        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, all.size());

        if (start >= all.size()) return Collections.emptyList();

        return all.subList(start, end).stream().map(this::toHistorialMap).collect(Collectors.toList());
    }

    public int contarHistorial(String fecha) {
        if (fecha != null && !fecha.isEmpty()) {
            LocalDate fechaFiltro = parseDate(fecha);
            return fechaFiltro != null ? repo.countByFechaFiltro(fechaFiltro) : repo.countAll();
        }
        return repo.countAll();
    }

    private Map<String, Object> toHistorialMap(HojaCarga hc) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", hc.getIdCarga());
        map.put("fecha", hc.getFechaHora());
        map.put("fechaFiltro", hc.getFechaFiltro() != null ? hc.getFechaFiltro().toString() : "");
        map.put("chofer", hc.getChofer());
        map.put("placa", hc.getPlaca());
        map.put("cant", hc.getItemsCount());
        map.put("espumas", hc.getEspumas());
        map.put("resortes", hc.getResortes());

        List<?> items = Collections.emptyList();
        if (hc.getDataJson() != null && !hc.getDataJson().isEmpty()) {
            try {
                items = objectMapper.readValue(hc.getDataJson(), List.class);
            } catch (Exception e) {
                items = Collections.emptyList();
            }
        }
        map.put("items", items);
        return map;
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
