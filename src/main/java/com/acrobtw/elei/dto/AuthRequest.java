package com.acrobtw.elei.dto;

public record AuthRequest(
    String username,
    String email,
    String password
) {}
