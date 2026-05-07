package com.acrobtw.elei.dto.activity;



public record CreateActivityDto(
    String name,
    Integer pointsMultiplier,
    String unitName
) {}
