package com.forli.despacho.service;

import com.forli.despacho.model.Chofer;
import com.forli.despacho.model.Pedido;
import com.forli.despacho.model.Placa;
import com.forli.despacho.repository.ChoferRepository;
import com.forli.despacho.repository.PedidoRepository;
import com.forli.despacho.repository.PlacaRepository;
import org.apache.poi.ss.usermodel.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

@Service
public class ExcelUploadService {

    private static final Logger LOG = Logger.getLogger(ExcelUploadService.class.getName());
    private static final List<String> SUPERVISORES_ESPECIALES = List.of("E-COMMERCE", "CANAL MODERNO", "OFICINA", "TIENDAS PROPIAS");
    private static final ZoneId ZONE_LIMA = ZoneId.of("America/Lima");
    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter ISO_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final PedidoRepository pedidoRepo;
    private final ChoferRepository choferRepo;
    private final PlacaRepository placaRepo;
    private final JdbcTemplate jdbcTemplate;

    public ExcelUploadService(PedidoRepository pedidoRepo, ChoferRepository choferRepo, PlacaRepository placaRepo, JdbcTemplate jdbcTemplate) {
        this.pedidoRepo = pedidoRepo;
        this.choferRepo = choferRepo;
        this.placaRepo = placaRepo;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public int uploadExcel(MultipartFile file) {
        try (InputStream is = file.getInputStream(); Workbook wb = WorkbookFactory.create(is)) {
            Sheet sheet = wb.getSheetAt(0);
            if (sheet == null || sheet.getLastRowNum() < 1) {
                throw new IllegalArgumentException("El archivo Excel está vacío o no tiene datos");
            }

            Map<String, Integer> colMap = buildColMap(sheet);
            LOG.info("Columnas detectadas: " + colMap);

            // Clear existing data
            jdbcTemplate.execute("TRUNCATE TABLE pedidos");
            jdbcTemplate.execute("TRUNCATE TABLE choferes");
            jdbcTemplate.execute("TRUNCATE TABLE placas");

            Set<String> choferNames = new HashSet<>();
            Set<String> placaNames = new HashSet<>();
            List<Pedido> pedidos = new ArrayList<>();

            int rowCount = 0;
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Pedido p = parseRow(row, i, colMap);
                if (p != null && !p.getPedido().isEmpty() && p.getCantidad().compareTo(BigDecimal.ZERO) > 0) {
                    pedidos.add(p);
                    rowCount++;

                    // Extract chofer/placa from agencia or other fields if present
                    // These are typically from the upload form, not the Excel
                }
            }

            // Batch insert pedidos
            pedidoRepo.saveAll(pedidos);

            LOG.info("Upload completado: " + rowCount + " pedidos insertados");
            return rowCount;

        } catch (Exception e) {
            LOG.severe("Error procesando Excel: " + e.getMessage());
            throw new RuntimeException("Error procesando archivo Excel: " + e.getMessage(), e);
        }
    }

    private Pedido parseRow(Row row, int index, Map<String, Integer> colMap) {
        Pedido p = new Pedido();
        p.setIdUnico(index + 1);

        p.setPedido(getCellStr(row, colMap.get("PEDIDONUMERO")));
        p.setCliente(getCellStr(row, colMap.get("CLIENTE")));
        p.setProducto(getCellStr(row, colMap.get("NOMBRE")));
        p.setSupervisor(getCellStr(row, colMap.get("SUPERVISOR")).toUpperCase().trim());
        p.setEstado(getCellStr(row, colMap.get("PEDIDOESTADO")));
        p.setUbigeo(getCellStr(row, colMap.get("AGENCIAUBIGEO")).isEmpty() ? "SIN UBIGEO" : getCellStr(row, colMap.get("AGENCIAUBIGEO")));
        p.setDireccion(getCellStr(row, colMap.get("LLEGADIRECCION")));
        p.setObservacion(getCellStr(row, colMap.getOrDefault("OBSERVACION1", -1)));

        // Cantidad/Saldo
        int colSaldo = colMap.getOrDefault("SALDO", colMap.getOrDefault("CANTIDAD", -1));
        p.setCantidad(getCellBigDecimal(row, colSaldo));

        // Agencia (TRANSPORTE or AGENCIA)
        int colAgencia = colMap.getOrDefault("TRANSPORTE", colMap.getOrDefault("AGENCIA", -1));
        String supervisorActual = p.getSupervisor();
        if (SUPERVISORES_ESPECIALES.contains(supervisorActual)) {
            int colLlegada = colMap.getOrDefault("LLEGADANOMBRE", -1);
            String llegada = colLlegada >= 0 ? getCellStr(row, colLlegada) : "SIN LLEGADA";
            p.setAgencia(llegada.isEmpty() ? "SIN LLEGADA" : llegada);
        } else {
            String agencia = colAgencia >= 0 ? getCellStr(row, colAgencia) : "SIN AGENCIA ASIGNADA";
            p.setAgencia(agencia.isEmpty() ? "SIN AGENCIA ASIGNADA" : agencia);
        }

        // Codigo venta
        int colCodVenta = colMap.getOrDefault("CODIGOVENTAS", colMap.getOrDefault("CODIGO", -1));
        p.setCodigoVenta(colCodVenta >= 0 ? getCellStr(row, colCodVenta) : "");

        // Linea
        int colLinea = colMap.getOrDefault("LINEA", -1);
        p.setLinea(colLinea >= 0 ? getCellStr(row, colLinea).toUpperCase() : "");

        // Fecha pedido
        Date fechaRaw = getCellDate(row, colMap.get("PEDIDOFECHA"));
        if (fechaRaw != null) {
            LocalDate fechaIso = fechaRaw.toInstant().atZone(ZONE_LIMA).toLocalDate();
            p.setFecha(fechaIso.format(DISPLAY_FORMAT));
            p.setFechaIso(fechaIso);
        } else {
            p.setFecha("");
            p.setFechaIso(null);
        }

        // Fecha entrega
        int colFechaEntrega = colMap.getOrDefault("FECHAENTREGA", -1);
        Date fechaEntregaRaw = colFechaEntrega >= 0 ? getCellDate(row, colFechaEntrega) : null;
        if (fechaEntregaRaw != null) {
            LocalDate fechaEntregaIso = fechaEntregaRaw.toInstant().atZone(ZONE_LIMA).toLocalDate();
            p.setFechaEntrega(fechaEntregaIso.format(DISPLAY_FORMAT));
            p.setFechaEntregaIso(fechaEntregaIso);
        } else {
            p.setFechaEntrega("");
            p.setFechaEntregaIso(null);
        }

        return p;
    }

    private Map<String, Integer> buildColMap(Sheet sheet) {
        Row header = sheet.getRow(0);
        if (header == null) return new HashMap<>();

        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < header.getLastCellNum(); i++) {
            Cell cell = header.getCell(i);
            if (cell != null) {
                String value = getCellValueStr(cell).toUpperCase().trim();
                if (!value.isEmpty()) {
                    map.put(value, i);
                }
            }
        }
        return map;
    }

    private String getCellStr(Row row, Integer colIndex) {
        if (colIndex == null || colIndex < 0) return "";
        Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        return cell != null ? getCellValueStr(cell) : "";
    }

    private BigDecimal getCellBigDecimal(Row row, Integer colIndex) {
        if (colIndex == null || colIndex < 0) return BigDecimal.ZERO;
        Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return BigDecimal.ZERO;

        if (cell.getCellType() == CellType.NUMERIC) {
            return BigDecimal.valueOf(cell.getNumericCellValue());
        }
        if (cell.getCellType() == CellType.STRING) {
            try {
                return new BigDecimal(cell.getStringCellValue().trim());
            } catch (NumberFormatException e) {
                return BigDecimal.ZERO;
            }
        }
        return BigDecimal.ZERO;
    }

    private Date getCellDate(Row row, Integer colIndex) {
        if (colIndex == null || colIndex < 0) return null;
        Cell cell = row.getCell(colIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) return null;

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getDateCellValue();
        }
        return null;
    }

    private String getCellValueStr(Cell cell) {
        if (cell == null) return "";
        DataFormatter formatter = new DataFormatter();
        return formatter.formatCellValue(cell).trim();
    }
}
