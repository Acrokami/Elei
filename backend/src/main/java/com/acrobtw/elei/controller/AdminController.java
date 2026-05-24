package com.acrobtw.elei.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.acrobtw.elei.dto.admin.SystemStatsDto;
import com.acrobtw.elei.service.AdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/telemetry")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SystemStatsDto> getSystemTelemetry() {
        log.info("[SECURITY] Admin Request To Watch System Telemetry");
        return ResponseEntity.ok(adminService.getGlobalTelemetry());
    }
}
