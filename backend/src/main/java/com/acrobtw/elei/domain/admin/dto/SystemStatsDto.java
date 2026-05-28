package com.acrobtw.elei.domain.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Global system telemetry and statistics for the admin dashboard")
public record SystemStatsDto(

    @Schema(description = "Total number of registered users", example = "1024", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    Long totalUsers,

    @Schema(description = "Total number of active tasks/activities created", example = "5430", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    Long totalActivities,

    @Schema(description = "Total number of experience logs recorded across the system", example = "25000", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    Long totalExperienceLogs
) {}
