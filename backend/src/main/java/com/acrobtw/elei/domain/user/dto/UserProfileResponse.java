package com.acrobtw.elei.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Basic profile information of the user")
public record UserProfileResponse(

    @Schema(description = "User's unique username", example = "acrobtw")
    String username,

    @Schema(description = "User's registered email address", example = "admin@elei.com")
    String email
) {

}
