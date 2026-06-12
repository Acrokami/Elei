package com.acrobtw.elei.domain.user.event;

import com.acrobtw.elei.domain.user.User;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Event triggered when a new user registers in the system")
public record UserRegisteredEvent(

    @Schema(description = "The user who has registered", requiredMode = Schema.RequiredMode.REQUIRED)
    User user
) {}
