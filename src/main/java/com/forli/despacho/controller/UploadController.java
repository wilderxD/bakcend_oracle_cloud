package com.forli.despacho.controller;

import com.forli.despacho.dto.ApiResponse;
import com.forli.despacho.service.ExcelUploadService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/upload")
public class UploadController {

    private final ExcelUploadService excelUploadService;

    public UploadController(ExcelUploadService excelUploadService) {
        this.excelUploadService = excelUploadService;
    }

    @PostMapping("/excel")
    public ApiResponse<Map<String, Object>> uploadExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.error("VALIDATION", "No se proporcionó archivo");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || (!filename.endsWith(".xlsx") && !filename.endsWith(".xls"))) {
            return ApiResponse.error("VALIDATION", "Formato de archivo no soportado. Use .xlsx o .xls");
        }

        try {
            int count = excelUploadService.uploadExcel(file);
            return ApiResponse.ok(Map.of(
                "count", count,
                "message", count + " pedidos cargados exitosamente"
            ));
        } catch (Exception e) {
            return ApiResponse.error("UPLOAD_ERROR", e.getMessage());
        }
    }
}
