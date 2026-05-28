package com.acrobtw.elei.domain.activity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Payload for submitting progress on a specific activity")
public record ActivityCompletionDto(

    @Schema(description = "Unique identifier of the target activity", example = "42", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    Long activityId,

    @Schema(description = "Amount of units completed in this transaction", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @Min(1)
    Integer unitsCompleted
) {}
