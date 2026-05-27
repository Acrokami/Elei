package com.acrobtw.elei.domain.activity.dto;



public record CreateActivityDto(
    String name,
    Integer pointsMultiplier,
    String unitName
) {}
