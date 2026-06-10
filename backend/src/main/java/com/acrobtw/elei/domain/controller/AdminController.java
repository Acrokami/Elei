package com.acrobtw.elei.domain.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acrobtw.elei.domain.admin.AdminService;
import com.acrobtw.elei.domain.admin.dto.SystemStatsDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin", description = "Administrative panel and system telemetry")
public class AdminController {
    private final AdminService adminService;

    @Operation(summary = "Get system telemetry", description = "Returns global system statistics. Requires ADMIN role")
    @GetMapping("/telemetry")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SystemStatsDto> getSystemTelemetry() {
        log.info("[SECURITY] Admin Request To Watch System Telemetry");
        return ResponseEntity.ok(adminService.getGlobalTelemetry());
    }
}
